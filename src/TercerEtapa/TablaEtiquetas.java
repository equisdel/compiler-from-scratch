package TercerEtapa;
import SegundaEtapa.Parser;
import java.util.*;

public class TablaEtiquetas {

    private static HashMap<String,HashMap<String,Set<GoToInfo>>> tabla = new HashMap<>();
    
    // Registro de scopes en espera

    public static class GoToInfo {
        String tag; String scope; int terceto; int line;
        public GoToInfo(String tag, String scope, int terceto, int line){
            this.tag = tag;         this.scope = scope;
            this.terceto = terceto; this.line = line;
        }
    }
    private static final Stack<Set<GoToInfo>> gotos_en_espera = new Stack<>();   // representa noción de ámbito de las tags
    private static String scope_actual;                     // Apuntan a la misma variable (denota el scope)

    public static void initialize() {
        if (gotos_en_espera.isEmpty())
            gotos_en_espera.add(new HashSet<>());   // Conjunto del ámbito main (debe estar en todos los programas)
    }


    // Esto debería actualizarse por cada cambio en el ámbito (llamar desde Parser.pushScope y Parser.popScope respectivamente)
    public static void pushScope() {
        initialize();
        scope_actual = Parser.actualScope;
        System.out.println("Entering "+scope_actual);
        // Apilar en gotos_en_espera
        gotos_en_espera.push(new HashSet<>());      // Se apila el conjunto del nuevo ámbito
    }

    public static void popScope() {
        System.out.println("Exiting "+scope_actual);
        scope_actual = Parser.actualScope;

        Set<GoToInfo> gotos_en_espera_scope_actual = gotos_en_espera.peek();
        gotos_en_espera.pop();                      // Se desapila el conjunto del ámbito actual
        if (gotos_en_espera.isEmpty())
            TablaEtiquetas.end();
        else if (!gotos_en_espera_scope_actual.isEmpty()) // Si un goto declarado en el scope a cerrar no fue asignado aún ...
            gotos_en_espera.peek().addAll(gotos_en_espera_scope_actual);   // entonces se lo pasa al conjunto de abajo.
        // Si al final del programa quedan gotos sin asignar en el tope de la pila: se informa del error indicando el número de línea
        // (ver método end())

    }

    private static boolean tagIsDeclared(String tag) {
        return tabla.containsKey(tag);
    }

    private static boolean tagIsDeclaredLocal(String tag, String scope) {
        return tagIsDeclared(tag) && tabla.get(tag).containsKey(scope);
    }

    private static String tagIsDeclaredNonLocal(String tag, String scope) {
        while (!scope.equals("MAIN")) {
            String scope_padre = String.join(":", Arrays.copyOfRange(scope.split(":"), 0, scope.split(":").length - 1));
            if (tagIsDeclaredLocal(tag,scope))
                return scope;
            scope = scope_padre;
        }
        return null;
    }

    private static void verifyGoToStack(String tag) {
        if (!gotos_en_espera.isEmpty()) {
            for (GoToInfo go_to : gotos_en_espera.peek()) {
                if (go_to.tag.equals(tag)) {
                    tabla.get(tag).get(scope_actual).add(go_to);
                    gotos_en_espera.peek().remove(go_to);
                }
            }
        }
    }

    private static void stealFromParents(String tag, String scope) {
        HashMap<String,Set<GoToInfo>> tag_entry = tabla.get(tag);
        while (!scope.equals("MAIN")) {
            String scope_padre = String.join(":", Arrays.copyOfRange(scope.split(":"), 0, scope.split(":").length - 1));
            if (tag_entry.containsKey(scope_padre)) {
                Iterator<GoToInfo> iterator = tag_entry.get(scope_padre).iterator();
                System.out.println("scope_padre: "+scope_padre);
                while (iterator.hasNext()) {
                    GoToInfo go_to = iterator.next();
                    System.out.println("scope padre: " + scope_padre);
                    if (go_to.scope.startsWith(scope)) {  // se produce el "robo"
                        tag_entry.get(scope_actual).add(go_to);
                        iterator.remove();  // Safely remove from scope_padre using the iterator
                    }
                }
            }
            scope = scope_padre;
        }
    }

    // Cuando se declara una etiqueta en alguna parte del código - se supone que ya se chequeó que no existe?
    public static void add_tag(String tag) {
        initialize();
        scope_actual = Parser.actualScope;
        // Key: tag; Value: scope - gotos asociados.
        if (!tagIsDeclared(tag)) { 
            HashMap<String,Set<GoToInfo>> value = new HashMap<>();     // 
            value.put(scope_actual,new HashSet<GoToInfo>());
            tabla.put(tag,value); 
        } else {
            tabla.get(tag).put(scope_actual,new HashSet<GoToInfo>());
        }
        // verificacion de gotos en espera (pueden ser asignados a un tag en este momento) - para el caso en que primero se pone el goto, luego la etiqueta
        verifyGoToStack(tag);
        // verificacion de tags de padres: "robos" de gotos que tienen un scope compatible.
        stealFromParents(tag,scope_actual);
        // desde el scope actual, recorro los padres.
        // miro cada goto asociado, robo aquellos cuyo scope contienen (substring) al actual.
    }

    public static void add_goto(String tag, int terceto, int line) {
        initialize();
        scope_actual = Parser.actualScope;
        GoToInfo nuevo_goto = new GoToInfo(tag,scope_actual,terceto,line);    // Agregar número de línea donde se declaró
        // se verifica si ya se declaró la etiqueta localmente: implica búsqueda simple en tabla
        if (tagIsDeclaredLocal(tag,scope_actual)) {     // para el caso en que primero se pone la etiqueta y luego el goto
            //    si se declaró: termina acá, el goto está asociado a esa etiqueta (actualización de tabla).
            System.out.println("camino1");
            tabla.get(tag).get(scope_actual).add(nuevo_goto);
            TablaEtiquetas.display(); 
        } else {
            System.out.println("camino2");
            //    si no se declaró localmente, se extiende la búsqueda a ámbitos padres
            String scope_of_declaration = tagIsDeclaredNonLocal(tag,scope_actual);
            if (scope_of_declaration != null) 
                tabla.get(tag).get(scope_of_declaration).add(nuevo_goto);
            else gotos_en_espera.peek().add(nuevo_goto); 
            // Si no hay match, queda en espera (pueden haber etiquetas más cercanas, pueden no haber).
        }
    }
    //metod0 de immpresion de tabla
    public static void display() {
        System.out.println("Tamaño de la pila: "+gotos_en_espera.size());
        for (Map.Entry<String, HashMap<String, Set<GoToInfo>>> entry : tabla.entrySet()) {
            for (Map.Entry<String, Set<GoToInfo>> entry2 : entry.getValue().entrySet()) {
                for (GoToInfo value : entry2.getValue()) {
                    System.out.print(entry.getKey()+" - "+entry2.getKey()+" - "+value.terceto+" - "+value.line);
                }
            }
        }
        System.out.println();
    }

    public static void end() {

        // Verificar que no existan gotos en espera, si los hay entonces debería dar error y enlistar las líneas de las tags huerfanas
        if (gotos_en_espera.size() != 1)
            System.out.println("[TablaEtiquetas:end()] Error en la actualización de ámbitos (el número de elementos en la pila es "+gotos_en_espera.size());
        else if (!gotos_en_espera.peek().isEmpty()) {
            for (GoToInfo goTo : gotos_en_espera.peek()) {
                System.out.println("ERROR en linea ["+goTo.line+"]: sentencia GOTO "+goTo.tag+" referencia a una etiqueta inexistente/inalcanzable.");
            }
        }

        TablaEtiquetas.display();

        for (Map.Entry<String, HashMap<String, Set<GoToInfo>>> entry : tabla.entrySet()) {
            for (Map.Entry<String, Set<GoToInfo>> entry2 : entry.getValue().entrySet()) {
                for (GoToInfo value : entry2.getValue()) {
                    //Terceto.print();
                    System.out.print(value.terceto+" - "+value.line+" - "+entry.getKey()+":"+entry2.getKey()+"\n");
                    Terceto.completeTerceto(value.terceto, entry.getKey()+":"+entry2.getKey(), null);
                }
            }
        }
        // rellena todos los tercetos presentes en la tabla:
        /*
            EJEMPLO:
            TAG1@
            [ {ambito A}
              GOTO TAG1@
              [ {ambito B}
                TAG1@
              ]
            ]
            resulta en el terceto: ["JUMP_TAG","?","?"]
            que al final se completa con "TAG1_main:", para que se distinga de la etiqueta "TAG1_main_A_B"

         */
        // en el assembler, deberíamos generar las etiquetas respetando los scopes
    }
}
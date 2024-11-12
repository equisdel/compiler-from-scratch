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
    private static Stack<Set<GoToInfo>> gotos_en_espera = new Stack<>();   // representa noción de ámbito
    private static String scope_actual = Parser.actualScope;              // apuntan a la misma variable

    // debe haber una estructura que marque los gotos "solidificados" (no van a cambiar) en contraposición a los que está en espera
    // idealmente tabla cumple esta función

    // Esto debería actualizarse por cada cambio en el ámbito (llamar desde Parser.pushScope y Parser.popScope respectivamente)
    public static void pushScope() {
        // Apilar en gotos_en_espera
    }

    public static void popScope() {
        // Verificar existencia de gotos_huerfanos (quizás convendría guardar tambien la linea donde fueron declaradas las sentencias)
        // Notificar el error, si es que hay 
        // Desapilar en gotos_en_espera
    }

    private static boolean tagIsDeclared(String tag) {
        return tabla.containsKey(tag);
    }

    private static boolean tagIsDeclaredLocal(String tag, String scope) {
        return tagIsDeclared(tag) && tabla.get(tag).containsKey(scope);
    }

    private static String tagIsDeclaredNonLocal(String tag, String scope) {
        while (!scope.equals("main")) {
            String scope_padre = String.join(":", Arrays.copyOfRange(scope.split(":"), 0, scope.split(":").length - 1));
            if (tagIsDeclaredLocal(tag,scope))
                return scope;
            scope = scope_padre;
        }
        return null;
    }

    private static void verifyGoToStack(String tag) {
        for (GoToInfo go_to : gotos_en_espera.peek()) {
            if (go_to.tag.equals(tag)) {
                tabla.get(tag).get(scope_actual).add(go_to);
                gotos_en_espera.peek().remove(go_to);
            }
        }
    }

    private static void stealFromParents(String tag, String scope) {
        HashMap<String,Set<GoToInfo>> tag_entry = tabla.get(tag);
        while (!scope.equals("main")) {
            String scope_padre = String.join(":", Arrays.copyOfRange(scope.split(":"), 0, scope.split(":").length - 1));
            if (tag_entry.containsKey(scope_padre)) {
                for (GoToInfo go_to : tag_entry.get(scope_padre)) {
                    if (go_to.scope.startsWith(scope)) {    // se produce el "robo"
                        tag_entry.get(scope_actual).add(go_to);
                        tag_entry.get(scope_padre).remove(go_to);
                    }
                }
            }
            scope = scope_padre;
        }
    }

    // Cuando se declara una etiqueta en alguna parte del código - se supone que ya se chequeó que no existe?
    public static void add_tag(String tag) {
        // Key: tag; Value: scope - gotos asociados.
        if (!tagIsDeclared(tag)) { 
            HashMap<String,Set<GoToInfo>> value = new HashMap<>();     // 
            value.put(scope_actual,null);
            tabla.put(tag,value); 
        } else {
            tabla.get(tag).put(scope_actual,null);
        }
        // verificacion de gotos en espera (pueden ser asignados a un tag en este momento) - para el caso en que primero se pone el goto, luego la etiqueta
        verifyGoToStack(tag);
        // verificacion de tags de padres: "robos" de gotos que tienen un scope compatible.
        stealFromParents(tag,scope_actual);
        // desde el scope actual, recorro los padres.
        // miro cada goto asociado, robo aquellos cuyo scope contienen (substring) al actual.
    }

    public static void add_goto(String tag, int terceto, int line) {
        GoToInfo nuevo_goto = new GoToInfo(tag,scope_actual,terceto,line);    // Agregar número de línea donde se declaró
        // se verifica si ya se declaró la etiqueta localmente: implica búsqueda simple en tabla
        if (tagIsDeclaredLocal(tag,scope_actual)) {     // para el caso en que primero se pone la etiqueta y luego el goto
            //    si se declaró: termina acá, el goto está asociado a esa etiqueta (actualización de tabla).
            tabla.get(tag).get(scope_actual).add(nuevo_goto);
        } else {
            
            //    si no se declaró localmente, se extiende la búsqueda a ámbitos padres
            String scope_of_declaration = tagIsDeclaredNonLocal(tag,scope_actual);
            if (scope_of_declaration != null) 
                tabla.get(tag).get(scope_of_declaration).add(nuevo_goto);
            else gotos_en_espera.peek().add(nuevo_goto); 
            // Si no hay match, queda en espera (pueden haber etiquetas más cercanas, pueden no haber).
        }
    }

    public static void end() {
        // rellena todos los tercetos presentes en la tabla:
        /*
            EJEMPLO:
            TAG1:
            [ {ambito A}
              GOTO TAG1:
              [ {ambito B}
                TAG1:
              ]
            ]
            resulta en el terceto: ["JUMP_TAG","?","?"]
            que al final se completa con "TAG1_main:", para que se distinga de la etiqueta "TAG1_main_A_B"

         */
        // en el assembler, deberíamos generar las etiquetas respetando los scopes
    }
}
package PrimerEtapa;
import java.util.ArrayList;
import java.util.function.*;

public class AccionSemantica {

    protected static ArrayList<AccionSemantica> all_actions = new ArrayList<>();
    
    private int id;
    private String descripcion;
    private Function<Void,Integer> action;
    static String error_msg = "Error";    // Variable compartida entre las acciones por si quieren elevar mensajes de Warning

    public AccionSemantica(int id, String descripcion, Function<Void,Integer> action) {
        this.id = id;
        this.descripcion = descripcion;
        this.action = action;
        all_actions.add(this);      // Construcción: cada acción se agrega a la lista de todas las acciones
    }

    public Integer execute() {
        return this.action.apply(null);
    }

    public static void main(String[] args) {

        // Definición de las expresiones lambda que definen a cada instancia de Acción Semántica

        String desc_0 = "Levanta un Warning con el mensaje de error actual.";
        Function<Void,Integer> action_0 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Creo que debería existir una lista de Warnings, no estoy segura.
                System.out.println("WARNING - [LINE "+AnalizadorLexico.line_number+"]: "+error_msg);
                error_msg = null;
                return 0;
            }
        };

        String desc_1 = "Anexa el último caracter leído al final de la cadena.";
        Function<Void,Integer> action_1 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + AnalizadorLexico.last_char;  // ¿Hay manera de que tenga estos datos más a mano?
                return 0;
            }
        };  // Testeado

        String desc_2 = "Elimina el último caracter de la cadena.";
        Function<Void,Integer> action_2 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema.substring(0,AnalizadorLexico.lexema.length()-1);
                return 0;
            }
        };  // Testeado

        String desc_3 = "Devuelve el último caracter a la entrada (evita que el lector avance).";
        Function<Void,Integer> action_3 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                return -1;
            }
        };

        String desc_4 = "Verifica si se trata de una palabra reservada o de un identificador.";
        Function<Void,Integer> action_4 = new Function<Void,Integer>() {
            int max_length = AnalizadorLexico.id_max_length;
            String key = AnalizadorLexico.lexema;
            Simbolo value = AnalizadorLexico.t_simbolos.get_entry(key.substring(0,max_length));
            public Integer apply(Void t) {
                if (value != null) {    // Las palabras reservadas (PR) estan precargadas en la TS
                    if (value.getTipo()=="reserved") {  // Es una palabra reservada (PR)
                        AnalizadorLexico.lexema_type = key.toUpperCase();           // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                        AnalizadorLexico.token = AnalizadorLexico.tokens.get(key);  // Actualiza el token con el asociado a la PR.
                    } else {  // Es una referencia a un identificador existente
                        // Cuestiones que pueden surgir más adelante... ¿Qué pasa si es de otro scope?
                        AnalizadorLexico.token = AnalizadorLexico.tokens.get("ID");
                    }
                } else {    // Es un nuevo identificador
                    AnalizadorLexico.token = AnalizadorLexico.tokens.get("ID");
                    all_actions.get(5).execute();   // Verifica la longitud según límite superior
                    all_actions.get(6).execute();   // Determina el tipo según la inicial
                    all_actions.get(7).execute();   // Agrega el identificador a la tabla de símbolos
                }
                return 0;
            }
        };

        String desc_5 = "Controla la longitud del identificador (máxima long: 15).";
        Function<Void,Integer> action_5 = new Function<Void,Integer>() {
            int max_length = AnalizadorLexico.id_max_length;
            public Integer apply(Void t) {
                if (AnalizadorLexico.lexema.length() > max_length) {
                    AccionSemantica.error_msg = "El identificador "+AnalizadorLexico.lexema+" supera la longitud máxima permitida ("+max_length+" caracteres).";
                    AnalizadorLexico.lexema = AnalizadorLexico.lexema.substring(0, max_length);
                    all_actions.get(0).execute();
                }
                return 0;
            }
        };

        String desc_6 = "Verifica el tipo de identificador.";
        Function<Void,Integer> action_6 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                char inicial = AnalizadorLexico.lexema.charAt(0);
                switch(inicial) {
                    case 's':   AnalizadorLexico.lexema_type = "ID_SINGLE";
                    case 'u':   AnalizadorLexico.lexema_type = "ID_UINTEGER";
                    case 'v':   AnalizadorLexico.lexema_type = "ID_UINTEGER";
                    case 'w':   AnalizadorLexico.lexema_type = "ID_UINTEGER";
                    default:    AnalizadorLexico.lexema_type = "ID";
                }
                return 0;
            }
        };

        String desc_7 = "Agrega una nueva entrada a la tabla de símbolos.";
        Function<Void,Integer> action_7 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.t_simbolos.add_entry(AnalizadorLexico.token, AnalizadorLexico.lexema, AnalizadorLexico.lexema_type);
                // Debería reiniciar el estado de la cadena y eso
                return 0;
            }
        };

        // Instanciación de acciones semánticas

        AccionSemantica as_0 = new AccionSemantica(0, desc_0, action_0);
        AccionSemantica as_1 = new AccionSemantica(1, desc_1, action_1);
        AccionSemantica as_2 = new AccionSemantica(2, desc_2, action_2);
        AccionSemantica as_3 = new AccionSemantica(3, desc_3, action_3);
        AccionSemantica as_4 = new AccionSemantica(4, desc_4, action_4);
        AccionSemantica as_5 = new AccionSemantica(5, desc_5, action_5);
        AccionSemantica as_6 = new AccionSemantica(6, desc_6, action_6);
        AccionSemantica as_7 = new AccionSemantica(7, desc_7, action_7);
        //AccionSemantica as_8 = new AccionSemantica(8, desc_8, action_8);

        // TEST
        AnalizadorLexico.lexema = "Delfina";
        AnalizadorLexico.last_char = "F";
        as_4.execute();
        System.out.println(AnalizadorLexico.lexema);

        //as_1.execute();
    }
}
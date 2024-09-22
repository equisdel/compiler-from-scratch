package PrimerEtapa;
import java.util.ArrayList;
import java.util.function.*;

public class AccionSemantica {

    protected static ArrayList<AccionSemantica> all_actions = new ArrayList<>();
    
    private int id;
    private String descripcion;
    private Function<Void,Integer> action;
    static String msg = "Error";    // Variable compartida entre las acciones por si quieren elevar mensajes de Warning

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

        // Definición de las expresiones lambda

        // La acción cero la dejamos para testing o alguna función especial
        String desc_0 = "Reservada para testeo.";
        Function<Void,Integer> action_0 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                return 0;
            }
        };

        String desc_1 = "Anexa el último caracter leído al final de la cadena.";
        Function<Void,Integer> action_1 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + AnalizadorLexico.last_char;  // ¿Hay manera de que tenga estos datos más a mano?
                return 0;
            }
        };

        String desc_2 = "Devuelve el último caracter a la entrada (evita que el lector avance).";
        Function<Void,Integer> action_2 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                return -1;
            }
        };

        String desc_3 = "Verifica si se trata de una palabra reservada o de un identificador.";
        Function<Void,Integer> action_3 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Las palabras reservadas (PR) estan precargadas en la TS bajo el tipo "reserved".
                // Si es identificador, o no está y hay que cargarlo (A.S.3) o está (es una referencia a un id existente u otro distinto según scope, ver más adelante)
                // Actualiza token acorde al resultado
                String key = AnalizadorLexico.lexema;
                Simbolo value = AnalizadorLexico.t_simbolos.get_entry(key);
                if (value != null) {
                    if (value.getTipo()=="reserved") {
                        AnalizadorLexico.lexema_type = key.toUpperCase();           // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                        AnalizadorLexico.token = AnalizadorLexico.tokens.get(key);  // Actualiza el token con el asociado a la PR.
                    }
                    else {
                        AnalizadorLexico.token = AnalizadorLexico.tokens.get("ID"); // Es identificador: se asocia automáticamente el token de ID.
                        all_actions.get(4).execute();   // Verifica la longitud del identificador (trunca + Warning).
                        all_actions.get(5).execute();   // De fija la primera letra para determinar el tipo de id y agregarlo a la TS.
                    }
                } else all_actions.get(6).execute();
                return 0;
            }
        };

        String desc_4 = "Agrega una entrada a la tabla de símbolos.";
        Function<Void,Integer> action_4 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Agrega el identificador como entrada accediendo directo a los atributos del Analizador Léxico (token, tipo, lexema)

                return 0;
            }
        };

        String desc_5 = "Agrega una entrada a la tabla de símbolos.";
        Function<Void,Integer> action_5 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Agrega el identificador como entrada accediendo directo a los atributos del Analizador Léxico (token, tipo, lexema)
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
        AccionSemantica as_8 = new AccionSemantica(8, desc_8, action_8);

        //as_1.execute();
    }
}
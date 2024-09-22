package PrimerEtapa;
import java.util.ArrayList;
import java.util.function.*;

public class AccionSemantica {

    protected static ArrayList<AccionSemantica> all_actions = new ArrayList<>();
    
    private int id;
    private String descripcion;
    private Function<Void,Void> action;

    public AccionSemantica(int id, String descripcion, Function<Void,Void> action) {
        this.id = id;
        this.descripcion = descripcion;
        this.action = action;
        all_actions.add(this);
    }

    public void execute() {
        this.action.apply(null);
    }

    public static void main(String[] args) {

        // Definición de las expresiones lambda

        Function<Void,Void> action_0 = new Function<Void,Void>() {
            public Void apply(Void t) {
                //a_lexico.token = a_lexico.getToken();
                return t;
            }
        };

        // Mezcla A.S.1 y A.S.2 (la 1 es implícita)
        String desc_1 = "El último caracter leído se anexa a la cadena.";
        Function<Void,Void> action_1 = new Function<Void,Void>() {
            public Void apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + AnalizadorLexico.last_char;  // ¿Hay manera de que tenga estos datos más a mano?
                return t;
            }
        };

        String desc_2 = "Verifica si se trata de una palabra reservada o de un identificador.";
        Function<Void,Void> action_2 = new Function<Void,Void>() {
            public Void apply(Void t) {
                // Si es PR está precargada en la TS con el tipo "reserved"
                // Si es identificador, o no está y hay que cargarlo (A.S.3) o está (es una referencia a un id existente u otro distinto según scope, ver más adelante)
                // Actualiza token acorde al resultado
                return t;
            }
        };

        String desc_3 = "Agrega una entrada a la tabla de símbolos.";
        Function<Void,Void> action_3 = new Function<Void,Void>() {
            public Void apply(Void t) {
                // Agrega el identificador como entrada accediendo directo a los atributos del Analizador Léxico (token, tipo, lexema)
                return t;
            }
        };


        // Instanciación de acciones semánticas


        //AccionSemantica as_0 = new AccionSemantica(0, desc_0, action_0);
        AccionSemantica as_1 = new AccionSemantica(1, desc_1, action_1);
        AccionSemantica as_2 = new AccionSemantica(2, desc_2, action_2);
        AccionSemantica as_3 = new AccionSemantica(3, desc_3, action_3);
        AccionSemantica as_4 = new AccionSemantica(4, "Se ejecuta cuando ", null);
        AccionSemantica as_5 = new AccionSemantica(5, "Se ejecuta cuando ", null);
        AccionSemantica as_6 = new AccionSemantica(6, "Se ejecuta cuando ", null);



        //as_1.execute();
    }
}
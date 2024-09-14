package PrimerEtapa;
import java.util.ArrayList;
import java.util.function.*;

public class AccionSemantica {

    protected static ArrayList<AccionSemantica> all_actions = new ArrayList<>();
    private static AnalizadorLexico a_lexico;
    
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
        System.out.println("XX");
    }

    public static void main(String[] args) {

        System.out.println("AAA");

        // Definición de las expresiones lambda
        Function<Void,Void> action_0 = new Function<Void,Void>() {
            public Void apply(Void t) {
                //a_lexico.token = a_lexico.getToken();
                return null;
            }
        };

        AccionSemantica as_0 = new AccionSemantica(0, "El contenido de la cadena es un token. Se lo asocia a su entero correspondiente para posterior pasaje al Analizador Sintáctico.", action_0);
        
        as_0.execute();

        AccionSemantica as_1 = new AccionSemantica(0, "Se ejecuta cuando ", null);
        AccionSemantica as_2 = new AccionSemantica(0, "Se ejecuta cuando ", null);
        AccionSemantica as_3 = new AccionSemantica(0, "Se ejecuta cuando ", null);
        AccionSemantica as_4 = new AccionSemantica(0, "Se ejecuta cuando ", null);
        AccionSemantica as_5 = new AccionSemantica(0, "Se ejecuta cuando ", null);
        AccionSemantica as_6 = new AccionSemantica(0, "Se ejecuta cuando ", null);
    }
}
package TercerEtapa;
import java.util.*;

public class Terceto {

    String operacion;
    String op1;
    String op2;

    static ArrayList<Terceto> listaTercetos = new ArrayList<>();

    Terceto(String operacion, String op1, String op2) { // desp hace referencia al terceto con esa id
        this.operacion = operacion;
        this.op1 = op1;
        this.op2 = op2;
        // si algun operador tiene <>, sabemos se trata de un ID de un terceto, y no un objeto PARSERVAL (o ref a TS)
    }


    static public String addTerceto(String operacion, String op1,String op2){
        Terceto t = new Terceto(operacion,op1,op2);
        Terceto.listaTercetos.add(t);
        return ("["+ (listaTercetos.size()-1) +"]");
    }

}

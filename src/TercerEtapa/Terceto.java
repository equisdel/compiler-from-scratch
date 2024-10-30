package TercerEtapa;
import java.util.*;

public class Terceto {

    String operacion;
    String op1;
    String op2;
    public String subtipo = null;

    static ArrayList<Terceto> listaTercetos = new ArrayList<>();

    Terceto(String operacion, String op1, String op2) { // desp hace referencia al terceto con esa id
        this.operacion = "<"+operacion+">";
        this.op1 = op1;
        this.op2 = op2;
        // si algun operador tiene <>, sabemos se trata de un ID de un terceto, y no un objeto PARSERVAL (o ref a TS)
    }

    Terceto(String operacion, String op1, String op2, String subtipo) { // desp hace referencia al terceto con esa id
        this.operacion = "<"+operacion+">";
        this.op1 = op1;
        this.op2 = op2;
        this.subtipo = subtipo;
        // si algun operador tiene <>, sabemos se trata de un ID de un terceto, y no un objeto PARSERVAL (o ref a TS)
    }
    
    static public String addTerceto(String operacion, String op1,String op2){
        Terceto t = new Terceto(operacion,op1,op2);
        Terceto.listaTercetos.add(t);
        return ("["+ (listaTercetos.size()-1) +"]"); // DEVUELVE NUMERO DE TERCETO (ID)
    }
    static public String addTercetoT(String operacion, String op1, String op2, String subtipo){
        Terceto t = new Terceto(operacion,op1,op2,subtipo);
        Terceto.listaTercetos.add(t);
        return ("["+ (listaTercetos.size()-1) +"]"); // DEVUELVE NUMERO DE TERCETO (ID)
    }
    // tal vez es mejor hacer clase ListaTercetos, que tenga un arraylist de tercetos, y metodos para agregar tercetos, y para obtener tercetos por id
    static public String getSubtype(String id){
        return listaTercetos.get(Integer.parseInt(id)).subtipo;
    }

}
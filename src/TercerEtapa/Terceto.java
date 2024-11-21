package TercerEtapa;
import java.util.*;

public class Terceto {

    static Boolean debug = true;
    public String operacion;
    public String op1;
    public String op2;
    public String subtipo = null;


    public static ArrayList<Terceto> TerList = new ArrayList<>();

    static Stack<Integer> TerStack = new Stack<>();

    Terceto(String operacion, String op1, String op2) { // desp hace referencia al terceto con esa id
        this.operacion = operacion;
        this.op1 = op1;
        this.op2 = op2;
        this.subtipo = "";
        // si algun operador tiene <>, sabemos se trata de un ID de un terceto, y no un objeto PARSERVAL (o ref a TS)
    }

    Terceto(String operacion, String op1, String op2, String subtipo) { // desp hace referencia al terceto con esa id
        this.operacion = operacion;
        this.op1 = op1;
        this.op2 = op2;
        this.subtipo = subtipo;
        // si algun operador tiene <>, sabemos se trata de un ID de un terceto, y no un objeto PARSERVAL (o ref a TS)
    }
    
    static public String addTerceto(String operacion, String op1,String op2){
        Terceto t = new Terceto(operacion,op1,op2);
        Terceto.TerList.add(t);
        if (debug) {System.out.println("Terceto agregado: "+ (TerList.size()-1) +" ("+ t.operacion + "," + t.op1 + "," + t.op2+")");}
        return (formatTercetoId(TerList.size()-1)); // DEVUELVE NUMERO DE TERCETO (ID)
    }
    static public String addTercetoT(String operacion, String op1, String op2, String subtipo){
        Terceto t = new Terceto(operacion,op1,op2,subtipo);
        Terceto.TerList.add(t);
        if (debug) {System.out.println("Terceto agregado: "+ (TerList.size()-1) +" ("+ t.operacion + "," + t.op1 + "," + t.op2+ "," + t.subtipo + ")");}
        return (formatTercetoId(TerList.size()-1)); // DEVUELVE NUMERO DE TERCETO (ID)
    }
    // tal vez es mejor hacer clase TerList, que tenga un arraylist de tercetos, y metodos para agregar tercetos, y para obtener tercetos por id
    static public String getSubtipo(String id){ // id viene con '<' '>'
        return TerList.get(parseTercetoId(id)).subtipo;
    }

    static public String getOperacion(String id){
        return TerList.get(parseTercetoId(id)).operacion;
    }

    static public String getOp1(String id){
        return TerList.get(parseTercetoId(id)).op1;
    }

    static public String getOp2(String id){
        return TerList.get(parseTercetoId(id)).op2;
    }

    // agregar a la pila
    static public void pushTerceto(String id){  //id viene con '< y '>'
        TerStack.push(parseTercetoId(id));
    }

    // desapilar
    static public int popTerceto(){
        return TerStack.pop();
    }
    
    //completar terceto
    static public void completeTerceto(int id, String op1, String op2){
        // llega un int o un string? que pasa con los <> con los q se maneja el id del terceto en la gramatica?
        Terceto t = TerList.get(id);
        if (op1 != null) {
            if (t.op1 == null){    
            t.op1 = op1;
            } else {System.out.println("DEBUGGING: SE INTENTO COMPLETAR UN TERCETO QUE YA TENIA VALOR");}
        }
        if (op2!= null) {
            if (t.op2 == null){
            t.op2 = op2;
        } else {System.out.println("DEBUGGING: SE INTENTO COMPLETAR UN TERCETO QUE YA TENIA VALOR");}
        }
        if (debug){System.out.println("Terceto completado: "+ id);}
    }

    static public String getTercetoCount(){
        return ""+TerList.size();
    }

    @Override
    public String toString() {
        return "Terceto{" +
                "operacion='" + operacion + '\'' +
                ", op1='" + op1 + '\'' +
                ", op2='" + op2 + '\'' +
                ", subtipo='" + subtipo + '\'' +
                '}';
    }
    
    static public void print_all() {
        int i = 0;
        for (Terceto t : Terceto.TerList) {
            System.out.println("["+(i++)+"] "+t.toString());
        }
    }


    public static String formatTercetoId(int id) {
        return "<" + id + ">";
    }

    public static int parseTercetoId(String id) {
        return Integer.parseInt(id.substring(1, id.length() - 1));
    }

}
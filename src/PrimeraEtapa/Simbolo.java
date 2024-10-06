package PrimeraEtapa;

// Entradas de la tabla de símbolos
public class Simbolo {
    
    private int id;
    private String type;        // 'ID', 'CTE', 'CHARCH', 'IF', 'NEQ' ...
    private String subtype;     // caso 'ID' o 'CTE': 'uinteger', 'single', 'reserved' y las cte no podrian ser uinteger o single o hexa?? 
    // scope, modificadores, etc.
    // nro de linea?

    // Implementar getters y setters
    Simbolo(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        //this.id = Simbolo.counter++;
    }

    Simbolo(String type) {
        this(type,null);
    }

    public int getTokenId(){
        return this.id;
    }

    public String getTipo(){
        return this.type;
    }

    public String getSubtipo(){
        return this.subtype;
    }

    public void setTipo(String typo){
        this.type = typo;
    }
    
    public void display() {
        System.out.println("ID: "+this.id+"; Tipo: "+this.type+"; Subtipo: "+this.subtype+";");
    }
}

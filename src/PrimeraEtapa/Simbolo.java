package PrimeraEtapa;

// Entradas de la tabla de símbolos
public class Simbolo {

    private String type;        // 'ID', 'CTE', 'CHARCH', 'IF', 'NEQ' ...
    private String subtype;     // caso 'ID' o 'CTE': 'uinteger', 'single', 'reserved' y las cte no podrian ser uinteger o single o hexa?? 
    // scope, modificadores, punteros, etc.
    private int line; // linea en la que se leyó

    // Implementar getters y setters
    Simbolo(String type, String subtype, int linea) {
        this.type = type;
        this.subtype = subtype;
        this.line = linea;
        //this.id = Simbolo.counter++;
    }

    Simbolo(String type, int linea) {
        this(type,null, linea);
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

    public int getLine(){
        return this.line;
    }
    
    public void display() {
        System.out.println("Tipo: "+this.type+"; Subtipo: "+this.subtype+";");
    }
}

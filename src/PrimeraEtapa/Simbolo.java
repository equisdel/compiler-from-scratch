package PrimeraEtapa;

// Entradas de la tabla de símbolos
public class Simbolo {

    private String type;        // 'ID', 'CTE', 'CHARCH', 'IF', 'NEQ' ...
    private String subtype;     // caso 'ID' o 'CTE': 'uinteger', 'single', 'reserved' y las cte no podrian ser uinteger o single o hexa?? 
    // scope, modificadores, punteros, etc.

    // Implementar getters y setters
    Simbolo(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        //this.id = Simbolo.counter++;
    }

    Simbolo(String type) {
        this(type,null);
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
        System.out.println("Tipo: "+this.type+"; Subtipo: "+this.subtype+";");
    }
}

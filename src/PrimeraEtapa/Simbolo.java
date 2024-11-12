package PrimeraEtapa;

// Entradas de la tabla de símbolos
public class Simbolo {

    private String type;        // 'ID', 'CTE', 'CHARCH', 'IF', 'NEQ' ...
    private String subtype;     // caso 'ID' o 'CTE': 'uinteger', 'single', 'reserved' y las cte no podrian ser uinteger o single o hexa?? 
    private String usage; //nombre de variable, nombre de función, nombre de tipo, nombre de parámetro, etc..
    private String value;
    // tipo de parametro para funciones?


    // Implementar getters y setters
    Simbolo(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        //this.id = Simbolo.counter++;
    }

    Simbolo(String type, String subtype, String usage) {
        this.type = type;
        this.subtype = subtype;
        this.usage = usage;
        //this.id = Simbolo.counter++;
    }

    Simbolo (String type, String subtype, String usage, String value){
        this.type = type;
        this.subtype = subtype;
        this.usage = usage;
        this.value = value;
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

    public void setUse(String uso){
        this.usage = uso;
    }

    public String getUse (){
        return this.usage;
    }

    public void setValue(String valor){
        this.value = valor;
    }

    public void display() {
        System.out.println("Tipo: "+this.type+"; Subtipo: "+this.subtype+"; Uso: "+this.usage+"; Valor: "+this.value);
    }
}

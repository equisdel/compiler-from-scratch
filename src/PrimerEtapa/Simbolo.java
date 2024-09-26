package PrimerEtapa;

// Entradas de la tabla de símbolos
public class Simbolo {
    
    private int id;
    private int token;
    private String lexema;      // '!='
    private String type;        // 'ID', 'CTE', 'CHARCH', 'IF'. 'NEQ'. CREO Q NO, es lexema_type, APARTE DE ESA VARIABLE deberiamos tener el tipo de identificador.
    private String subtype;     // caso 'ID': 'uinteger', 'single', 'reserved'.
    // scope, modificadores, etc.
    // nro de linea?

    // Implementar getters y setters
    Simbolo(int token, String lexema, String type) {
        this.token = token;
        this.lexema = lexema;
        this.type = type;
    }

    Simbolo(int token, String lexema) {
        this.token = token;
        this.lexema = lexema;
    }

    public int getTokenId(){
        return this.id;
    }

    public String getTipo(){
        return this.type;
    }

    public String getLexema(){
        return this.lexema;
    }

    public void setTipo(String typo){
        this.type = typo;
    }

    public void display() {
        System.out.println("ID: "+this.id+"; Token: "+this.token+"; Lexema: "+this.lexema+";");
    }
}

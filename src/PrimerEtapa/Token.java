package PrimerEtapa;

public class Token {
    private int id;
    // nro de linea?
    private String tipo;


    Token(int num){
        this.id = num;
    }

    Token(int num, String tipo){
        this.id = num;
        this.tipo = tipo;
    }

    public int getTokenId(){
        return this.id;
    }

    public String getTipo(){
        return this.tipo;
    }
}

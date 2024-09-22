package PrimerEtapa;

import java.util.HashMap;

public class TablaDeSimbolos {

    private HashMap<String,Simbolo> symbols;        // Estructura eficiente. Key = Lexema.

    public TablaDeSimbolos() {
        symbols = new HashMap<>();
    }

    public void add_entry(int token, String lexema, String type) {
        Simbolo new_entry = new Simbolo(token, lexema.toUpperCase(), type);   // Se pasa todo a upper case
        symbols.put(lexema,new_entry);
    }

    public void display() {
        System.out.println("TABLA DE SIMBOLOS");
        for (Simbolo s : symbols.values())
            s.display();
        System.out.println();
    }

    
    public static void main (String args[]) {

    

    //  TESTEO
        // Creación de la tabla de símbolos
        TablaDeSimbolos ts = new TablaDeSimbolos();
        // Inserción de una entrada piloto
        ts.add_entry(0,"test_0","type_any");
        // Impresión de la tabla
        ts.display();

    }
}

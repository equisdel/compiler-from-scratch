package PrimeraEtapa;

import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolos {

    private HashMap<String,Simbolo> symbols;        // Estructura eficiente. Key = Lexema.

    public TablaDeSimbolos() {
        symbols = new HashMap<>();
    }

    public void add_entry(String lexema, String type, String subtype) {
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }

    public Simbolo get_entry(String key) {
        Simbolo retorno = symbols.get(key);
        if (AnalizadorLexico.isdebug())System.out.println(retorno==null);
        return retorno;
    }

    public void display() {
        System.out.println("TABLA DE SIMBOLOS");
        for (Map.Entry<String, Simbolo> entry : symbols.entrySet()) {
            System.out.print("Clave: " + entry.getKey() + ", Valor: ");
            entry.getValue().display();
        }
        System.out.println();
    }

    
    public static void main (String args[]) {

    

    //  TESTEO
        // Creación de la tabla de símbolos
        TablaDeSimbolos ts = new TablaDeSimbolos();
        // Inserción de una entrada piloto
        ts.add_entry("","test_0","type_any");
        // Impresión de la tabla
        ts.display();

    }
}

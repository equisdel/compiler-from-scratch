package PrimeraEtapa;

import java.util.LinkedHashMap;
import java.util.Map;

public class TablaDeSimbolos {

    private LinkedHashMap<String,Simbolo> symbols;        // Estructura eficiente. Key = Lexema.

    public TablaDeSimbolos() {
        symbols = new LinkedHashMap<>();
    }

    public void add_entry(String lexema, String type, String subtype) {
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }

    public void add_entry_typedef(String lexema, String type, String subtype) { 
        // podria pasarse menos cosas, pero es un tipo. En nuestro caso, es un tipo de 'tipo' PAIR.
        // solo tenemos pair pero podriamos contemplar más..
        //si 'pairsito' es el lexema, hay conflictos con posibles variables que se llamen igual..
        // asiq no se q poner de lexema, pero de tipo seria ID, y de subtipo PAIR
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }

    public Simbolo get_entry(String key) {
        Simbolo retorno = symbols.get(key);
        if (AnalizadorLexico.isdebug())System.out.println(retorno==null);
        return retorno;
    }

    public String get_type(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return null;
        return retorno.getTipo();
    }

    public String get_subtype(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return null;
        return retorno.getSubtipo();
    }

    public void set_use(String key, String uso) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return;
        retorno.setUse(uso);
    }

    public void display() {
        System.out.println("TABLA DE SIMBOLOS");
        for (Map.Entry<String, Simbolo> entry : symbols.entrySet()) {
            System.out.print("Clave: " + entry.getKey()+" ");
            entry.getValue().display();
        }
        System.out.println();
    }

    public void clear(){
        symbols.clear();
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

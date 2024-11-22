package PrimeraEtapa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TablaDeSimbolos {

    private LinkedHashMap<String,Simbolo> symbols;       // Estructura eficiente. Key = Lexema.

    public TablaDeSimbolos() {
        symbols = new LinkedHashMap<>();
    }

    public void add_entry(String lexema, String type, String subtype) {
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }

    public void add_entry(String lexema, String type, String subtype, String uso, String value) {
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase(),uso.toUpperCase(),value.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }

    public void add_entry(String lexema, String type, String subtype, String uso) {
        Simbolo new_entry = new Simbolo(type.toUpperCase(), subtype.toUpperCase(),uso.toUpperCase());   // Se pasa todo a upper case
        symbols.put(lexema.toUpperCase(),new_entry);
    }


    public void del_entry(String key){
        symbols.remove(key);
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

    public String get_use(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return null;
        return retorno.getUse();
    }

    public void set_value(String key, String valor) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return;
        retorno.setValue(valor);
    }

    public String get_value(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return null;
        return retorno.getValue();
    }

    public void addCont(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return;
        retorno.addCont();
    }

    public int getCont(String key) {
        Simbolo retorno = symbols.get(key);
        if (retorno == null) return -1;
        return retorno.getCont();
    }

    public Set<String> getAllEntries() {

        // Implementation to return all entries in the symbol table
        return new HashSet<>(symbols.keySet());
    }
    
    public void clean() {
        // solo deja las palabras reservadas y a los identificadores

        Iterator<Map.Entry<String, Simbolo>> iterator = symbols.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Simbolo> entry = iterator.next();
            String lexema = entry.getKey();
            //Simbolo simbolo_asociado = entry.getValue();
            //if (!simbolo_asociado.getSubtipo().equals("RESERVED") && !(lexema.contains(":"))) {
            if (!(lexema.contains(":"))) {      // Se conserva todo lo que tenga scope
                iterator.remove();
            }
        }
    }

    public void display() {
        System.out.println("TABLA DE SIMBOLOS");
        /*
        for (Map.Entry<String, Simbolo> entry : symbols.entrySet()) {
            System.out.print("Clave: " + entry.getKey()+" ");
            entry.getValue().display();
        }
        */
        /* Esto es para mostrar más linda la lista:::
        // Calculate max length for each column
        int lexemaWidth = lexema.stream().mapToInt(String::length).max().orElse(0);
        int tipoWidth = tipo.stream().mapToInt(String::length).max().orElse(0);
        int subtipoWidth = subtipo.stream().mapToInt(String::length).max().orElse(0);
        int usoWidth = uso.stream().mapToInt(String::length).max().orElse(0);
        int valorWidth = valor.stream().mapToInt(String::length).max().orElse(0);

        // Build the dynamic format string
        String format = "| %-"+lexemaWidth+"s | %-"+tipoWidth+"s | %-"+subtipoWidth+"s | %-"+usoWidth+"s | %-"+valorWidth+"s |%n";

        // Print header
        System.out.printf(format, "LEXEMA", "TIPO", "SUBTIPO", "USO", "VALOR");

        // Print rows
        for (int i = 1; i < lexema.size(); i++) {
            System.out.printf(format, lexema.get(i), tipo.get(i), subtipo.get(i), uso.get(i), valor.get(i));
        }

        */
        System.out.printf("| %-20s | %-10s |  %-12s |  %-14s | %-6s | %n","LEXEMA","TIPO","SUBTIPO","USO","VALOR");
        System.out.printf("|%-20s|%-10s|%-12s|%-14s|%-6s|%n","----------------------","------------","---------------","-----------------","--------");
        for (Map.Entry<String, Simbolo> entry : symbols.entrySet()) {
            //System.out.print("Clave: " + entry.getKey()+" ");
            Simbolo value = entry.getValue();
            System.out.printf("| %-20s | %-10s |  %-12s |  %-14s | %-6s | %n",entry.getKey(),value.getTipo(),value.getSubtipo(),value.getUse(),value.getValue());
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

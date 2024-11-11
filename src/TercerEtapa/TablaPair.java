package TercerEtapa;

import java.util.HashMap;

public class TablaPair {
    
    static HashMap<String, TipoPair> Tabla = new HashMap<>();  //nombre del tipo, y sus componentes


    public TablaPair() {}

    public static void addPair(String nombre, String tipo) {
        if (!Tabla.containsKey(nombre)) {
            Tabla.put(nombre, new TipoPair(nombre));
        }
    }

    public static void set_values(String nombre, String c1, String c2) {
        Tabla.get(nombre).setC1(c1);
        Tabla.get(nombre).setC2(c2);
    }

    public static String getTipo(String nombre) {
        return Tabla.get(nombre).getTipo();
    }

    
}

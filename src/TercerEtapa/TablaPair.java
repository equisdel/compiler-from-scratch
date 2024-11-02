package TercerEtapa;

import java.util.HashMap;

public class TablaPair {
    HashMap<String, TipoPair> Tabla = new HashMap<>();


    public TablaPair() {}

    public void addTipo(String tipo, String c1, String c2) {
        if (!Tabla.containsKey(tipo)) {
            Tabla.put(tipo, new TipoPair(tipo, c1, c2));
        }
    }

    
}

package TercerEtapa;

import java.util.HashMap;
import java.util.Map;

public class AnalizadorSemantico {

    static Map<String, Integer> tipos = new HashMap<>();

    static {
        tipos.put("uinteger", 0);
        tipos.put("single", 1);
        tipos.put("hexa", 2);
    }

    private static TablaPair tabla = new TablaPair();

    public static boolean validID(String type, String ID) {
        String inicial = ID.substring(0,0);
        if (inicial.equals("s")) {
            if (!type.equals("single")) 
                return false;
        } else if ((inicial.equals("u")) || (inicial.equals("v")) || (inicial.equals("w"))) {
            if (!type.equals("uinteger") || !type.equals("hexa")) 
                return false;
        }/*  else {
            if (type.equals("single") || (type.equals("uinteger")) || !type.equals("hexa")) 
                return false; }*/
        return true;
    }

    public static int getTipo(String key) {
        return tipos.getOrDefault(key, -1);
    }

    static String[][] tablaCompatibilidadTipos = {                 // Válido para las operaciones aritméticas
            {"-",            "single",       "-"     },
            {"uinteger",     "-",            "uinteger" },
            {"uinteger",     "uinteger",     "-"        },
            
    };

    public static String getCompatibilidad(String tipo1, String tipo2) {
        int i = getTipo(tipo1);
        int j = getTipo(tipo2);
        // HARDCODED: hexa y uinteger
        if (tipo1.equals("HEXA") && tipo2.equals("UINTEGER") || tipo1.equals("UINTEGER") && tipo2.equals("HEXA")) {
            return "UINTEGER";
        }
        if (i == -1 || j == -1) return null;
        return tablaCompatibilidadTipos[i][j];
    }

    public static Boolean isCompatible(String tipo1, String tipo2) {
        return getCompatibilidad(tipo1, tipo2) != null;
    }

    public static void main(String[] args) {
        TablaEtiquetas.initialize();
    }

}


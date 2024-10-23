package TercerEtapa;

import java.util.Map;

public class AnalizadorSemantico {

    static String ambito = "";
    Map<String, Integer> tipos = Map.of(
            "uinteger", 0,
            "single", 1,
            "hexa", 2
    );

    public static void setAmbito(String new_ambito) {
        AnalizadorSemantico.ambito = new_ambito;
    }

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

    public int getTipo(String key) {
        return tipos.getOrDefault(key, -1);
    }

    String[][] tablaCompatibilidadTipos = {                 // Válido para las operaciones aritméticas
            {"-",            "single",       "-"     },
            {"uinteger",     "-",            "uinteger" },
            {"uinteger",     "uinteger",     "-"        },
            // Hexa + Single = se pasan ambas a uinteger
    };

    public String getCompatibilidad(String tipo1, String tipo2) {
        int i = getTipo(tipo1);
        int j = getTipo(tipo2);
        if (i == -1 || j == -1) return null;
        return tablaCompatibilidadTipos[i][j];
    }

    public Boolean isCompatible(String tipo1, String tipo2) {
        // primero chequear si es un simbolo o un terceto
        // si es simbolo, accedo a TS y obtengo el tipo
        // si es terceto ?? el terceto tendria q ir llevando tipo?
        return getCompatibilidad(tipo1, tipo2) != null;
    }

    public static void main(String[] args) {
        
    }

}


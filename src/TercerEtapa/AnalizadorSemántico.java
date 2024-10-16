package TercerEtapa;

import java.util.Map;

public class AnalizadorSemántico {

    Map<String, Integer> tipos = Map.of(
            "uinteger", 0,
            "single", 1,
            "hexa", 2
    );

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

}


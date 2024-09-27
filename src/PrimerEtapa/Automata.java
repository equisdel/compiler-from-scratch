package PrimerEtapa;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automata {
    
	private int estado_actual = 0;
    private int estados[];      // Cambiar a mapa: int -> int
    private Pattern regex[];     // Cambiar a mapa: String -> int
    private int matrizTransicionEstados[][];
    private int matrizAccionesSemanticas[][];

    public Automata(String path_csv_mE, String path_csv_mA) {
        // Lectura de encabezado - expresiones regulares
        // Lectura de la primera columna - identificadores de los estados
        int offset_f = 2;	    // offset de filas determinado por el formato de la matriz      // offset donde va?
		int offset_c = 1;	// offset de columnas determinado por el formato de la matriz       // offset donde va?

        // Creación de las matrices
        CSVReader csv_mE = new CSVReader(path_csv_mE);  // Lector de matriz de transición de estados
        CSVReader csv_mA = new CSVReader(path_csv_mA);  // Lector de matriz de acciones semánticas
        
        // Esto lo podemos volar despúes, es para control nuestro
        if (csv_mA.columnas!=csv_mE.columnas || csv_mA.filas!=csv_mE.filas) System.out.println("Ojo, no coinciden las dimensiones de las matrices.");
        int columnas = Math.max(csv_mA.columnas,csv_mE.columnas); // ver offset (ojo, deberían ser iguales)
        int filas = Math.max(csv_mA.filas,csv_mE.filas);          // ver offset (ojo, deberían ser iguales)
        
        filas -= offset_f; 
        columnas -= offset_c;
        
        this.matrizTransicionEstados    = new int[filas][columnas];
        this.matrizAccionesSemanticas   = new int[filas][columnas];
        
        this.estados = new int[filas];
        this.regex = new Pattern[columnas];

        // MATRIZ T. DE ESTADOS
                
        List<String[]> file_csv_mE = csv_mE.readAll();
        String[] header1 = file_csv_mE.get(1);  file_csv_mE.remove(header1);
        String[] header0 = file_csv_mE.get(0);  file_csv_mE.remove(header0);
        //System.out.println(file_csv_mE.size());
        
        // Header1: expresiones regulares de cada columna
        for (int i = 0; i < columnas; i++) {
            String rE = new String(header1[i+offset_c]);
            regex[i] = Pattern.compile(rE,Pattern.CASE_INSENSITIVE);
        }
            
            
            /* 
             * 
             // Header2: estados asociados a cada fila
             for (int i = 1; i < filas; i++) {
                //System.out.println(Integer.parseInt(file_csv_mE.get(i)[0]));
                estados[i] = Integer.parseInt(file_csv_mE.get(i)[0]);   
            }
            */

        // Llena la matriz de transicion de estados
        System.out.println("filas: "+filas+"; cols: "+columnas);
        for (int i = 0; i<filas; i++)
            for (int j = 1; j<columnas; j++) {
                //System.out.println(Integer.parseInt(file_csv_mE.get(i)[j]));
                matrizTransicionEstados[i][j] = Integer.parseInt(file_csv_mE.get(i)[j]);

            }
    }

    protected void reset() {
        estado_actual = 0;
    }

    protected boolean estadoFinal() {
        return estado_actual == -1;
    }
        
    protected int getNext(String caracter) {

        // Actualiza su estado.
        // Retorna el identificador de la acción semántica a ejecutar.

        Pattern p_regex;
        for(int i = 0; i < regex.length; i++) {
            p_regex = regex[i];
            Matcher matcher = p_regex.matcher(caracter);
            if (matcher.matches()) {
                estado_actual = matrizTransicionEstados[estado_actual][i];
                return matrizAccionesSemanticas[estado_actual][i];
            }
        }
        System.out.println("ERROR: el caracter no coincide con ninguna expresión regular.");
        return 0;
    
    }
/* 
 * 
 switch (caracter) {
    // case regex[0] : pos = 3 ...
            case "0" : pos = 3;
            case "[1-9]": pos = 4;
            case "x": pos = 5;
            case "s": pos = 6;
            case "[abcdeghijklmnopqrtuvwyzABCDEGHIJKLMNOPQRTUVWYZ]": pos=7;
            case "[a-fA-F]": pos = 8;
            case "_": pos = 9;
            case "#" : pos = 10;
            case "[": pos = 11;
            case "]": pos = 12;
            case "+": pos = 13;
            case "-": pos = 14;
            case ".": pos = 15;
            case "=": pos = 16;
            case ":": pos = 17;
            case "!": pos = 18;
            case "<": pos = 19;
            case ">": pos = 20;
            case "*": pos = 21;
            case "(": pos = 22;
            case ")": pos = 23;
            case ",": pos = 24;
            case ";": pos = 25;
            default: { pos = 26; retorno = true; }
        }
        estado_actual = matrizTransicion[estado_actual][pos];
        if (estado_actual == -1) {
            retorno = true;
        }
        return matrizAccionesSemanticas[0][0];
        */
    

    public static void main(String[] args) {
        Automata a = new Automata("src/PrimerEtapa/Matrices/matrizEstados.csv","src/PrimerEtapa/Matrices/matrizEstados.csv");
        /*
        String regex = "[g-rt-wyz]";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher("h");
        System.out.println(matcher.matches());
        */
    }

}
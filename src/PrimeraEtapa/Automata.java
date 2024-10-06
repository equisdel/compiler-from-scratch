package PrimeraEtapa;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automata {
    
	private int estado_actual = 0;
    //private int estados[];      // Cambiar a mapa: int -> int
    private String regex[];     // Cambiar a mapa: String -> int
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
        System.out.println(filas);
        this.matrizTransicionEstados    = new int[filas][columnas];
        this.matrizAccionesSemanticas   = new int[filas][columnas];
        
        //this.estados = new int[filas];
        this.regex = new String[columnas];

        // MATRIZ T. DE ESTADOS
                
        List<String[]> file_csv_mE = csv_mE.readAll();
        String[] header1 = file_csv_mE.get(1);  file_csv_mE.remove(header1);
        String[] header0 = file_csv_mE.get(0);  file_csv_mE.remove(header0);
        //System.out.println(file_csv_mE.size());
        
        // Header1: expresiones regulares de cada columna
        for (int i = 0; i < columnas; i++) {
            String rE = header1[i+offset_c];
            rE = rE.substring(1,rE.length()-1);
            regex[i] = rE;

            //System.out.println(rE);
            //regex[i] = Pattern.compile(rE,Pattern.CASE_INSENSITIVE);
        }

        // Llena la matriz de transicion de estados
        System.out.println("filas: "+filas+"; cols: "+columnas);
        for (int i = 0; i<filas; i++)
            for (int j = 0; j<columnas; j++) {
                //System.out.println(Integer.parseInt(file_csv_mE.get(i)[j]));
                matrizTransicionEstados[i][j] = Integer.parseInt(file_csv_mE.get(i)[j+offset_c]);
            }


        List<String[]> file_csv_mA = csv_mA.readAll();
        header1 = file_csv_mA.get(1);  file_csv_mA.remove(header1);
        header0 = file_csv_mA.get(0);  file_csv_mA.remove(header0);
        for (int i = 0; i<filas; i++)
            for (int j = 0; j<columnas; j++) 
                matrizAccionesSemanticas[i][j] = Integer.parseInt(file_csv_mA.get(i)[j+offset_c]);
    }

    protected void reset() {
        estado_actual = 0;
    }

    protected void finalize() {
        estado_actual = -1;
    }

    protected boolean estadoFinal() {
        return estado_actual == -1;
    }
        
    protected int getNext(String caracter) {

        // Actualiza su estado.
        // Retorna el identificador de la acción semántica a ejecutar.

        //Pattern p_regex;
        int x=-1;
        for(int i = 0; i < this.regex.length-1; i++) {
            Pattern p_regex = Pattern.compile(regex[i],Pattern.CASE_INSENSITIVE);
            Matcher matcher = p_regex.matcher(caracter);
            if (matcher.matches()) {
                System.out.println("Coincidencia con regex: "+i+". Patrón: "+p_regex.pattern());
                x = estado_actual;
                System.out.println("Nuevo E: mE["+x+"]["+i+"] = "+matrizTransicionEstados[x][i]);
                estado_actual = matrizTransicionEstados[estado_actual][i];
                System.out.println("Retorno: mA["+x+"]["+i+"] = "+matrizAccionesSemanticas[x][i]);
                return matrizAccionesSemanticas[x][i];
            }
        }
        // Caso default
        x = estado_actual;
        estado_actual = matrizTransicionEstados[estado_actual][this.regex.length-1];
        return matrizAccionesSemanticas[x][this.regex.length-1];
    
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
        /*
        */
        //a.getNext(new String("s"));
        Automata a = new Automata("src/PrimerEtapa/Matrices/matrizEstados.csv","src/PrimerEtapa/Matrices/matrizEstados.csv");
        Pattern p = Pattern.compile(a.regex[2],Pattern.CASE_INSENSITIVE);
        System.out.println(p.pattern());
        Matcher matcher = p.matcher("0");
        System.out.println(matcher.find());
    }

}
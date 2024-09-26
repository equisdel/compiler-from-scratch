package PrimerEtapa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Automata {
    
	private int estado_actual = 0;
    private int estados[];      // Cambiar a mapa: int -> int
    private String regex[];     // Cambiar a mapa: String -> int
    private int matrizTransicionEstados[][];
    private int matrizAccionesSemanticas[][];

    public Automata(String path_csv_mE, String path_csv_mA) {
        // Lectura de encabezado - expresiones regulares
        // Lectura de la primera columna - identificadores de los estados
        int offset_f = -2;	    // offset de filas determinado por el formato de la matriz
		int offset_c = -1;	// offset de columnas determinado por el formato de la matriz
        matrizTransicionEstados = new int[][] {
            { -1, -1, -1,  1,  1,  1,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  2,  2, -1, -1, -1, -1, -1, -1, -1,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  3,  2,  7, -1, -1, -1, -1, -1, -1,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  5,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  5,  5, -1,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  9,  9, -1, -1, -1, -1, -1,  8,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1, 10, 10, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  9,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1,  9,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1, 10, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, -1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
            { 0, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15 }
        };

        CSVReader csv_mE = new CSVReader(path_csv_mE);  // Lector de matriz de transición de estados
        CSVReader csv_mA = new CSVReader(path_csv_mA);  // Lector de matriz de acciones semánticas

        // Esto lo podemos volar despúes, es para control nuestro
        if (csv_mA.columnas!=csv_mE.columnas || csv_mA.filas!=csv_mE.filas) System.out.println("Ojo, no coinciden las dimensiones de las matrices.");
        int columnas = Math.max(csv_mA.columnas,csv_mE.columnas); // ver offset (ojo, deberían ser iguales)
        int filas = Math.max(csv_mA.filas,csv_mE.filas);          // ver offset (ojo, deberían ser iguales)

        this.matrizTransicionEstados    = new int[filas+offset_f][columnas+offset_c];
        this.matrizAccionesSemanticas   = new int[filas+offset_c][columnas+offset_c];
        this.estados = new int[filas];
        this.regex = new String[columnas];
        /* 
        String[] line;
        while ((line = csv_mE.getNextLine()) != null) {
            for (String value : line) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
         * 
        */

        List<String[]> file_csv_mE = csv_mE.readAll();
        filas += offset_f;                   
        columnas += offset_c;

            // Header1: expresiones regulares de cada columna
            String[] header1 = file_csv_mE.get(1);
            for (int i = 0; i < columnas; i++)
                regex[i] = header1[i];

            // Header2: estados asociados a cada fila
            for (int i = 0; i < filas; i++)
                estados[i] = Integer.parseInt(file_csv_mE.get(i)[0]);

            file_csv_mE.remove(0); file_csv_mE.remove(1);

            // Cambiar a mapas y rellenar la matriz de enteros
            for (int i = 0; i<filas; i++)
                for (int j = 0; i<columnas; j++)
                    matrizTransicionEstados[i][j] = Integer.parseInt(file_csv_mE.get(i)[j]);

            System.out.println("III");

        }

    }

    protected AccionSemantica getNext(String caracter) {
        int pos;
        boolean retorno = false;
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
        return null;
    }

    public static void main(String[] args) {
        
        try (CsvFileReader csv = new CsvFileReader(path_csv)) {
            System.out.println("worked");
        }
    }

}
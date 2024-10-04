package PrimeraEtapa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    
    String line;
    String filePath;
    int filas,columnas;
    BufferedReader reader;

    public CSVReader(String path) {
        this.filePath = path;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            this.reader = br;
            try { this.reader.mark(1000000); } catch (IOException e) {}
            this.filas = this.getFilas();
            this.columnas = this.getColumnas();
            this.reset();
        } catch (FileNotFoundException e) { 
            System.out.println("not found");
            e.printStackTrace(); 
        }
    }

    public List<String[]> readAll() {
        this.reset();
        List<String[]> output = new ArrayList<>();
        String[] line = this.getNextLine();
        while (line!=null) {
            output.add(line);
            line = this.getNextLine();
        } return output;
    }

    private String[] getNextLine() {
        try { line = reader.readLine(); } 
        catch (IOException e) { e.printStackTrace(); }
        if (line != null) {
            String[] values = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);       // El -1 es para que se incluyan los String vacíos en el arreglo
            return values;
        } return null;
    }

    public void reset() {
        try { reader.reset(); } catch (IOException e) {}
    }

    private int getFilas() {     // No puede llamarse MIENTRAS se lee línea por línea
        this.reset();
        return this.readAll().size();
    }

    private int getColumnas() {  // No puede llamarse MIENTRAS se lee línea por línea
        this.reset();
        int columnas = (this.getNextLine()).length;
        return columnas;
    }

    public static void main(String[] args) {
        CSVReader test = new CSVReader("src/PrimerEtapa/Matrices/matrizEstados.csv");
        System.out.println(test.getFilas()+" : "+test.getColumnas());
    }
}

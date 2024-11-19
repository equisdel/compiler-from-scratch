package CuartaEtapa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    
    String file_path;
    File file;
    BufferedReader reader;
    BufferedWriter writer;

    public FileManager(String path) {
        file_path = path;
        file = new File(path);
        try {
            if (file.createNewFile()) {
            }
        } catch (IOException e) {
            System.out.println("Problema ¿?");
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new FileReader(file_path));
            this.reader.mark(1024); 
            //reader.mark(1024);
            writer = new BufferedWriter(new FileWriter(file_path, true));
        } catch (IOException e) { e.printStackTrace(); System.out.println("PROBLEMA (FileManager): el archivo no fue creado correctamente."); }
    }

    public FileManager(File file) {
        this.file = file;
        this.file_path = file.getPath();
        try {
            reader = new BufferedReader(new FileReader(file_path));
            reader.mark(1024);
            writer = new BufferedWriter(new FileWriter(file_path, true));
        } catch (IOException e) { e.printStackTrace(); System.out.println("PROBLEMA (FileManager): el archivo no fue tomado correctamente."); }
    }

    public void display() {
        try {
            reader.mark(1024);
        } catch (IOException e) {
        }
        System.out.println("Displaying file located at: "+this.file_path);
        String line = this.readLine();
        while (line != null) {
            System.out.println(line);
            line = this.readLine();
        }
        try {
            reader.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Problema en el reset");
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {}
        System.out.println(this.file.getAbsolutePath());
        System.out.println(this.file.delete());
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Problema en el read line");
            e.printStackTrace();
            return null;
        }
    }

    public void appendLine(String new_line) {
        try {
            writer.write(new_line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Problema en el writer.write del write line");
            e.printStackTrace();
        }
    }

    public void appendFile(FileManager fileManager) throws IOException {
        String nextLine;

        // Ensure the reader is at the beginning of the file
        fileManager.reader.mark(0); // Mark the current position
        fileManager.reader.reset(); // Reset to the marked position
    
        // Read each line from the fileManager's reader and append it to the current file
        while ((nextLine = fileManager.readLine()) != null) {
            this.appendLine(nextLine); // Assuming appendLine(String line) is implemented
        }

    }

    public File getFile() {
        return file;
    }
    // appendLine
    // appendFile
    // rename file
    // delete file
    // print
}

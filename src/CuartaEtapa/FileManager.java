package CuartaEtapa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
            //reader.mark(1024);
            writer = new BufferedWriter(new FileWriter(file_path, true));
        } catch (IOException e) { e.printStackTrace(); System.out.println("PROBLEMA (FileManager): el archivo no fue creado correctamente."); }
    }

    public void display() {
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

    public void rename(String new_name) {

        // Update file path
        String[] split_file_path = file_path.split("\\");
        split_file_path[split_file_path.length-1] = new_name;
        file_path = split_file_path.toString();

        // Delete old file and create new one?
        FileManager new_file = new FileManager(file_path);
        try {
            new_file.appendFile(this);
        } catch (IOException e) {
            System.out.println("Problema en el append file");
            e.printStackTrace();
        }
        file.delete();
        file = new_file.file;
        try {
            reader = new BufferedReader(new FileReader(file_path));
        } catch (FileNotFoundException e) {
            System.out.println("Problema en la asignacion del reader");
            e.printStackTrace();
        }
        try {
            writer = new BufferedWriter(new FileWriter(file_path));
        } catch (IOException e) {
            System.out.println("Problema en la asignacion del writer");
            e.printStackTrace();
        }
        
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

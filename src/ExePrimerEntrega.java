import PrimeraEtapa.*;
import SegundaEtapa.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ExePrimerEntrega {

    public static void main(String[] args) {
        // Ruta donde se buscarán los archivos (puedes cambiarla a otra carpeta)
        String directoryPath = "test_codes";
        File directory = new File(directoryPath);
        
        // Listar los archivos del directorio
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No se encontraron archivos en el directorio.");
            return;
        }

        // Mostrar las opciones de archivos
        System.out.println("Seleccione un archivo para leer:");
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println(i + 1 + ". " + files[i].getName());
            }
        }

        // Obtener la elección del usuario
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        // Validar elección
        if (choice < 1 || choice > files.length) {
            System.out.println("Selección inválida.");
            scanner.close();
            return;
        }

        // Leer y mostrar el contenido del archivo seleccionado ACA DEBERIA EJECUTAR LO DE MAIN DE SEGUNDA ETAPA Y PASARLE EL PATH
        File selectedFile = files[choice - 1];
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            System.out.println("\nContenido del archivo " + selectedFile.getName() + ":");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            scanner.close();
        }
        // "src/test_codes/P1"
        String filePath = ("test_codes/")+selectedFile.getName();
        System.out.println("archivo elegido: "+filePath);
        AnalizadorLexico.compile(filePath);
		Parser parser = new Parser();
		parser.run();
		AnalizadorLexico.display();
        
    }
}

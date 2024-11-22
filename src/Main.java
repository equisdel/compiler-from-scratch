import CuartaEtapa.*;
import PrimeraEtapa.AnalizadorLexico;
import PrimeraEtapa.Error;
import SegundaEtapa.*;
import TercerEtapa.Terceto;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
   
   public static String displayTestMenu() {
      
      String rel_path = "src/test_codes";
      //File var2 = new File(rel_path);
      File[] list_of_tests = new File(rel_path).listFiles();

      if (list_of_tests != null && list_of_tests.length != 0) {
         System.out.println("\nMENU DE PRUEBAS.");
         System.out.println("\nSeleccione un archivo para leer:");

         for(int i = 0; i < list_of_tests.length; ++i) {
            if (list_of_tests[i].isFile()) {
               System.out.println(i + 1 + ". " + list_of_tests[i].getName());
            }
         }

         Scanner input = new Scanner(System.in);
         System.out.print(">> ");
         int test = input.nextInt();
         if (test >= 1 && test <= list_of_tests.length) {
            File selected_test = list_of_tests[test - 1];
            input.close();
            return selected_test.getAbsolutePath();
         }
         input.close();
      }
      return null;
   }

	public static void main(String[] args) {
   
      String p_path;
      if (Arrays.toString(args).equals("[]")) {  // si se pasa como argumento, todo esto se saltea
         // Pide al usuario el camino al programa a compilar
         System.out.print("\nIngrese el path absoluto del programa a compilar. Si solo presiona ENTER, se lo redirigirá a nuestro menú de programas de prueba.\n >> ");
         Scanner input = new Scanner(System.in);
         p_path = input.nextLine().replace("\\","/").replace("\"", "");
         if (p_path.equals("")) p_path = displayTestMenu();      // retorna null cuando la opción es incorrecta
         input.close();
      } else p_path = args[0].replace("\\","/").replace("\"", "");
      
      
      if (p_path != null) {                              // Verifica la validez del input del usuario

         File program = new File(p_path);

         if (program.isAbsolute()) {                     // Se asegura de que sea un path absoluto
            
            if (program.exists() && program.isFile()) {  // Garantiza que el archivo existe

               // Procede a compilar
               System.out.println("\nCompilando \"" + program.getAbsolutePath()+"\"...\n");
               Parser parser = new Parser();
               AnalizadorLexico.compile(program.getAbsolutePath());
               parser.run();

               // Métricas de la ejecución
               Error.display_all();      // errores y warnings
               AnalizadorLexico.display();   // tabla de símbolos
               Terceto.print_all();          // tercetos generados

               // Si no hay errores, genera código assembler
               String executablePath = "src\\CuartaEtapa\\AsmCode\\";   // ¿Dónde se almacena el ejecutable?
               if (!Error.isCompilable()) {    // Verifica que no haya errores fatales
                  AsmGenerator.generate(executablePath,program.getName());
               } else {
                  System.out.println("Hay errores fatales en el programa. No es posible generar el ejecutable.");
                  for (String s : Parser.errores) {
                     System.out.println(s);
                  }
               }

            } else System.out.println("El archivo no existe o no es un archivo válido.\n");
         } else System.out.println("Debe ingresar un path absoluto.\n");
      } else System.out.println("Opción inválida.\n");

   }

}

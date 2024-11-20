import CuartaEtapa.*;
import PrimeraEtapa.AnalizadorLexico;
import SegundaEtapa.*;
import TercerEtapa.Terceto;
import java.io.File;
import java.util.Scanner;

public class Main {

   // ESTO ES TESTING
   private static String mapSingleToFloat(String single) {    // Convención IEEE 754

      String signo, exponenteBin, mantisaBin;       // 1, 8 y 23 bits, respectivamente.
      
      // Determinar el signo

      switch (single.substring(0,0)) {    // signo del single
          case "+" : {
              signo = "0"; single = single.substring(1,single.length());
           }
          case "-" : {
              signo = "1"; single = single.substring(1,single.length());
           }
          default : signo = "0";        // positivo por defecto
      }
       // signo del single
      System.out.println("El signo es: "+signo);
      // Separar la base (mantisa) y el exponente

      String[] singlePorPartes = single.split("s");
      float base;
      int exponente;

      try {
          base = Float.parseFloat(singlePorPartes[0]);    // Puede no tener exponente (ausencia de "s")
          exponente = singlePorPartes.length == 2 ? Integer.parseInt(singlePorPartes[1]) : 0;
      } catch (Exception e) {
          throw new IllegalArgumentException("Formato inválido para el número single: " + single);
      }

      // Normalizar la base para la forma 1.mantisa
      int desplazamiento = 0;
      while (base >= 2.0) {  // Ajustar para que la base esté en el rango [1, 2)
          base /= 2.0;
          desplazamiento++;
      }
      while (base < 1.0) {  // Ajustar para que la base esté en el rango [1, 2)
          base *= 2.0;
          desplazamiento--;
      }

      int exponenteAjustado = exponente + desplazamiento + 127;  // Sesgo para precisión simple (127)
      if (exponenteAjustado < 0 || exponenteAjustado > 255) {
          throw new IllegalArgumentException("El exponente resultante está fuera del rango permitido por IEEE 754.");
      }

      // Convertir el exponente a binario de 8 bits
      exponenteBin = String.format("%8s", Integer.toBinaryString(exponenteAjustado)).replace(' ', '0');
      System.out.println("El exponente es: "+exponenteBin);

      // Calcular la mantisa (23 bits, sin el 1 implícito)
      float fraccion = base - 1.0f;
      StringBuilder mantisa = new StringBuilder();
      for (int i = 0; i < 23; i++) {
          fraccion *= 2;
          if (fraccion >= 1.0) {
              mantisa.append("1");
              fraccion -= 1.0;
          } else {
              mantisa.append("0");
          }
      }
      mantisaBin = mantisa.toString();
      System.out.println("La mantisa es: "+mantisaBin);
      String IEEE_754_representation = signo + " | " + exponenteBin +  " | " + mantisaBin;
      if (IEEE_754_representation.length() == 32+6)
          return IEEE_754_representation;
      else System.out.println("PROBLEMA: la representación IEEE 754 es errónea, tiene "+IEEE_754_representation.length()+" bits.");
      return null;
   }

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
         int test = input.nextInt();
         if (test >= 1 && test <= list_of_tests.length) {
            File selected_test = list_of_tests[test - 1];


            FileManager fm_selected_test = new FileManager(selected_test);

            System.out.println("\nContenido del archivo " + selected_test.getName() + ":");
            fm_selected_test.display();

            input.close();
            return selected_test.getAbsolutePath();
         }
         input.close();
      }
      return null;
   }


	public static void main(String[] args) {
      
      // Pide al usuario el camino al programa a compilar
      System.out.print("\nIngrese el path absoluto del programa a compilar. Si solo presiona ENTER, se lo redirigirá a nuestro menú de programas de prueba.\n >> ");
      Scanner input = new Scanner(System.in);
      String p_path = input.nextLine().replace("\\","/").replace("\"", "");
      if (p_path.equals("")) p_path = displayTestMenu();      // retorna null cuando la opción es incorrecta
      
      // Verifica la validez del input del usuario
      if (p_path != null) {

         File program = new File(p_path);

         if (program.isAbsolute()) {  // Se asegura de que sea un path absoluto
            
            if (program.exists() && program.isFile()) {  // El archivo es válido

               // Procede a compilar
               System.out.println("Archivo encontrado: " + program.getAbsolutePath());
               Parser parser = new Parser();
               AnalizadorLexico.compile(program.getAbsolutePath());
               parser.run();
               AnalizadorLexico.display();
               Terceto.print_all();

               // Solo si no hay errores se procede a generar código assembler
               String executablePath = "src\\CuartaEtapa\\AsmCode\\";   // ¿Dónde se almacena el ejecutable?
               if (Parser.errores.isEmpty()) {
                  AsmGenerator.generate(executablePath,program.getName());
               } else {
                  System.out.println("Hay errores.");
                  for (String s : Parser.errores) {
                     System.out.println(s);
                  }
               }

            } else System.out.println("El archivo no existe o no es un archivo válido.");
         } else System.out.println("Debe ingresar un path absoluto.");

	   }

   }


}

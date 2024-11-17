import PrimeraEtapa.AnalizadorLexico;
import SegundaEtapa.*;
import TercerEtapa.Terceto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import CuartaEtapa.AsmGenerator;

public class Main {

   // ESTO ES TESTING
   private static String mapSingleToFloat(String single) {    // Convención IEEE 754

      String signo, exponenteBin, mantisaBin;       // 1, 8 y 23 bits, respectivamente.
      
      // Determinar el signo

      switch (single.substring(0,0)) {    // signo del single
          case "+":   signo = "0"; single = single.substring(1,single.length());
                      break;
          case "-":   signo = "1"; single = single.substring(1,single.length());
                      break;
          default:    signo = "0";        // positivo por defecto
                      break;
      }
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

	public static void main(String[] args) {
      File test = new File("./src/CuartaEtapa/test.txt");
      System.out.println(test.getAbsolutePath());
		/* 
		String single = "+2.5s+20";
      System.out.println(mapSingleToFloat(single));
      * 
      3*/
      // Pide al usuario el camino al programa a compilar
      String var1 = "src/test_codes";
      File var2 = new File(var1);
      File[] var3 = var2.listFiles();

      if (var3 != null && var3.length != 0) {
         System.out.println("Seleccione un archivo para leer:");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].isFile()) {
               System.out.println(var4 + 1 + ". " + var3[var4].getName());
            }
         }

         Scanner var19 = new Scanner(System.in);
         int var5 = var19.nextInt();
         if (var5 >= 1 && var5 <= var3.length) {
            File var6 = var3[var5 - 1];

            try {
               BufferedReader var7 = new BufferedReader(new FileReader(var6));

               try {
                  System.out.println("\nContenido del archivo " + var6.getName() + ":");
                  
                  String var8;
                  while((var8 = var7.readLine()) != null) {
                     System.out.println(var8);
                  }
               } catch (Throwable var16) {
                  try {
                     var7.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }

                  throw var16;
               }

               var7.close();
            } catch (IOException var17) {
               System.out.println("Error al leer el archivo: " + var17.getMessage());
            } finally {
               var19.close();
            }

            String var20 = "src/test_codes/" + var6.getName();
            System.out.println("archivo elegido: " + var20);
            AnalizadorLexico.compile(var20);
            Parser var21 = new Parser();
            var21.run();
            AnalizadorLexico.display();
            Terceto.print_all();

            // Solo si no hay errores se procede a generar código assembler
            String executablePath = "src\\CuartaEtapa\\AsmCode\\";   // ¿Dónde se almacena el ejecutable?
            if (Parser.errores.isEmpty()) {
               AsmGenerator.generate(executablePath);
            } else System.out.println("Hay errores.");

         } else {
            System.out.println("Selecci\u00f3n inv\u00e1lida.");
            var19.close();
         }
      } else {
		 System.out.println("No se encontraron archivos en el directorio!!!!.");
      }
      
	}
   
}
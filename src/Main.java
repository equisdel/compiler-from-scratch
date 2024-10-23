import PrimeraEtapa.AnalizadorLexico;
import SegundaEtapa.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
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
         } else {
            System.out.println("Selecci\u00f3n inv\u00e1lida.");
            var19.close();
         }
      } else {
		 System.out.println("No se encontraron archivos en el directorio!!!!.");
      }

	}

}
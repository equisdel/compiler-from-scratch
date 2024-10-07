import java.util.Scanner;

import PrimeraEtapa.*;
import SegundaEtapa.*;

public class Main {

	public static void main(String[] args) {
		
		// Pide al usuario el camino al programa a compilar
		String filePath = "src/test_codes/P1";
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();  // Lee una línea de input

		AnalizadorLexico.compile(filePath);
		int x;
		while(!input.equals("xd")) {
			x = AnalizadorLexico.yylex();
			input = sc.nextLine(); 
		}
	
		AnalizadorLexico.display();


	}

}
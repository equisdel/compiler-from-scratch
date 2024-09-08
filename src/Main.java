import java.nio.file.Path;
import java.nio.file.Paths;

import PrimerEtapa.AnalizadorLexico;

// 

public class Main {

	public static void main(String[] args) {
		AnalizadorLexico a_lexico = new AnalizadorLexico(10,10);
		// a_lexico.setMatriz(5);
		// https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.
		String filePath = "src/PrimerEtapa/1";
		a_lexico.getTokens(filePath);
	}

}
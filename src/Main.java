import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PrimerEtapa.AccionSemantica;
import PrimerEtapa.AnalizadorLexico;

// 

public class Main {

	public static void main(String[] args) {
		AnalizadorLexico a_lexico = new AnalizadorLexico();
		AnalizadorLexico.main(args);
		// a_lexico.setMatriz(5);
		// https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.
		String filePath = "src/test_codes/1";
		//Pattern pattern = Pattern.compile(".{4}(.{3}).*");
		//Matcher matcher = pattern.matcher("testXXXtest");
		//matcher.matches();
		//String whatYouNeed = matcher.group(1);
		//System.out.println(whatYouNeed);
		Pattern p = Pattern.compile("[a-zA-Z&&[^a-fA-F]]");
		Matcher m = p.matcher("a");
 		boolean b = m.matches(); 
		if (b) {
			System.out.println("yei");
		}
		
		System.out.println(p.matches("[a-z]", "a"));
		//a_lexico.getTokens(filePath);
	}

}
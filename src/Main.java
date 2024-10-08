import PrimeraEtapa.AnalizadorLexico;
import SegundaEtapa.*;

public class Main {

	public static void main(String[] args) {
		
		// Pide al usuario el camino al programa a compilar
		String filePath = "test_codes/1";
		AnalizadorLexico.compile(filePath);
		Parser parser = new Parser();
		parser.run();
		AnalizadorLexico.display();

	}

}
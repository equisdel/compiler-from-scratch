package PrimerEtapa;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnalizadorLexico {

	private Automata automata;
	private String cadena;

	public AnalizadorLexico(){

		// Inicialización del autómata
		String matriz_filePath = "src/PrimerEtapa/Matrices/matrizEstados.csv";
		this.automata = new Automata(matriz_filePath);

		// Inicialización de la tabla de símbolos
		
		
	}

	// Recibe un path y lee el archivo asociado línea por línea
	public ArrayList<String> getTokens(String filePath) {
		
		ArrayList<String> output = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				for (int i = 0; i<line.length(); i++) {
					String ultimo_caracter = line.substring(i,i);
					System.out.println(ultimo_caracter);
					if (automata.getNext(ultimo_caracter));
						// cuando sale del : 1 token.
					// Acá va la lógica de la matriz
					// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
				}
				
				// Ojo, acá va salto de línea y tiene que cambiar el estado acorde.
				// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public ArrayList<String> getNextToken(String filePath) {
		
		ArrayList<String> output = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				for (int i = 0; i<line.length(); i++) {
					char ultimo_caracter = line.charAt(i);
					System.out.println(ultimo_caracter);
					// Acá va la lógica de la matriz
					// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
				}
				
				// Ojo, acá va salto de línea y tiene que cambiar el estado acorde.
				// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

}

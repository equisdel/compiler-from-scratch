package PrimerEtapa;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnalizadorLexico {

	private int estado_actual = 0;
	private int matrizTransicionDeEstados[][];
	private AccionSemantica matrizAccionesSemanticas[][];

	
	public AnalizadorLexico(int cant_estados, int cant_caracteres){
		this.matrizTransicionDeEstados = new int[cant_estados][cant_caracteres];
		this.matrizAccionesSemanticas = new AccionSemantica[cant_estados][cant_caracteres];
	}
	
	public void setMatriz(int e) {
		// matrizAutomata[0][0] = 1;
	}

	// Recibe un path y lee el archivo asociado línea por línea
	public ArrayList<String> getTokens(String filePath) {
		
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

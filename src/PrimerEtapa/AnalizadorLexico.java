package PrimerEtapa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.ArrayList;

public class AnalizadorLexico {

	private Automata automata;
	protected int token = -1;
	protected String lexema = "";
	protected String lexemaType = "";
	protected char ult_caracter;
	private ConcurrentHashMap<String,Token> TablaSimbolos = new ConcurrentHashMap<>();
	
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


	public int getNextToken(String filePath) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {  // !!!! cuando el A.S. quiera otro token deberia seguir leyendo desde donde quedo
				//guardar donde quedó....
				for (int i = 0; i<line.length(); i++) {
					String ultimo_caracter = line.substring(i,i);
					//System.out.println(ultimo_caracter);
					lexema = lexema + ultimo_caracter.toString();
					if (automata.getNext(ultimo_caracter)){
						int id = getToken();
						this.lexema = "";
						this.lexemaType = "";
						return id;
					}

				}
				// Ojo, acá va salto de línea y tiene que cambiar el estado acorde.
				// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; //si no encontro ningun token...
	}

	protected int getToken(){
		// de lexema a numero, mapear a id
		int id = lexToToken(lexema);
		//int id = 0;
		Token token = new Token(id);
		// pero antes de devolverlo (al A.S.) chequear tabla simbolos y agregarlo si corresponde:
		TablaSimbolos.put(lexema,token); // si existe el lexema no lo pone.
		return id;
	}

	private int lexToToken(String lexeme){
		int id = 0;
		// como mierda sabemos si es id, cte, o cadena multilinea?
		switch (lexeme) {
			case ":=": id = 1;
			case "!=": id = 2;
			case "<": id = 3;
			case ">": id = 4;
			case "<=": id = 5;
			case ">=": id = 6;
			case "+": id = 7;
			case "-": id = 8;
			case "*": id = 9;
			case "=": id = 10;
			case "(": id = 11;
			case ")": id = 12;
			case ",": id = 13;
			case ".": id = 14;
			case ";": id = 15;
			default:  switch (this.lexemaType) {
				case "identifier": id = 16;
				case "const": id = 17;
				case "charChain": id = 18;
				// si no es algun operador, pero ya entra antes.
			} 
		}
		return id;
	}

	public static void main(String[] args) {
		AccionSemantica.main(args);
	}
}

/*
 * identificadores:
 * cadena de caracteres multilinea:
 * constantes:
 * los que no van en tabla de simbolos (tokens unicos):
 * <,>,!,( .....
 * 
 */
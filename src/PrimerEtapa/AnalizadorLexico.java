package PrimerEtapa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class AnalizadorLexico {
	
	// BORRAR ANTES DE LA ENTREGA:
	boolean printmode = true;

	// La clase AccionSemántica modifica directamente los atributos del A.l.
	static private Automata automata;
	static private TablaDeSimbolos t_simbolos;
	static private ArrayList<AccionSemantica> acciones;
	
	static private String[] reserved = {"if","else","while"};	// Completar! Es lo mismo masc y mayuscula, TS lo convierte todo a mayúscula
	static protected int token = -1;
	static protected String lexema = "";
	static protected String lexemaType = "";
	static protected String last_char;		// Se almacena acá, no se pasa como parámetro.
	
	// INICIALIZACION
	public AnalizadorLexico(){

		// Inicialización del autómata
		String matriz_filePath = "src/PrimerEtapa/Matrices/matrizEstados.csv";		// Quizás se pasa desde Main (parámetro)
		this.automata = new Automata(matriz_filePath);

		// Inicialización de la tabla de símbolos + precarga de palabras reservadas
		this.t_simbolos = new TablaDeSimbolos();
		for (String p_reservada : reserved)
			t_simbolos.add_entry(token, p_reservada, "reserved");
	
		// Inicialización de acciones semánticas
		acciones = AccionSemantica.all_actions;
		
	}

	// MODULOS DEL METODO PRINCIPAL


	// METODO PRINCIPAL
	public int getNextToken(String filePath) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {  // !!!! cuando el A.S. quiera otro token deberia seguir leyendo desde donde quedo
				//guardar donde quedó....
				int i = 0;
				while (i<line.length()) {
					
					AnalizadorLexico.last_char = line.substring(i,i);	// Se actualiza el último caracter
					if (printmode) System.out.println(last_char);
					automata.getNext(last_char).execute();				// Punto a resolver: solo con acciones semánticas?
					// Esto en particular es horrible:
					if (flag) {
						i++;
						flag = False;
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

		public static void main(String[] args) {

			// Inicialización de acciones semánticas
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
package PrimerEtapa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {
	
	// BORRAR ANTES DE LA ENTREGA:
	boolean printmode = true;

	// La clase AccionSemántica modifica directamente los atributos del A.L.
	static Automata automata;
	static TablaDeSimbolos t_simbolos;
	static ArrayList<AccionSemantica> acciones;
	//static Set<Character> grammar_symbols = new HashSet<>();
	// pasa que en un comnetario puede estar literalmente cualqueir caracter..

	static int line_number = 0;
	static final int id_max_length = 15;

	static private String[] reserved = {"if","then","else","begin","end","end_if","outf","typedef","fun","ret","uinteger","single","repeat","until","pair","goto"};	// Completar! Es lo mismo masc y mayuscula, TS lo convierte todo a mayúscula
	static protected Map<String, Integer> tokens = new HashMap<>();
	static {
        tokens.put("ID", 		1);
        tokens.put("CTE", 		2);
        tokens.put("CHARCH",	3);
        tokens.put("NEQ", 		4);
        tokens.put("LEQ", 		5);
        tokens.put("MEQ", 		6);
        tokens.put("IF", 		7);		// Palabra Reservada
        tokens.put("THEN", 		8);		// Palabra Reservada
        tokens.put("ELSE", 		9);		// Palabra Reservada
        tokens.put("BEGIN", 	10);	// Palabra Reservada
        tokens.put("END", 		11);	// Palabra Reservada
        tokens.put("END_IF",	12);	// Palabra Reservada
        tokens.put("OUTF", 		13);	// Palabra Reservada
        tokens.put("TYPEDEF",	14);	// Palabra Reservada
        tokens.put("FUN", 		15);	// Palabra Reservada
        tokens.put("RET", 		16);	// Palabra Reservada
        tokens.put("UINTEGER",	17);	// Palabra Reservada
        tokens.put("SINGLE",	18);	// Palabra Reservada
        tokens.put("REPEAT",	19);	// Palabra Reservada
        tokens.put("UNTIL", 	20);	// Palabra Reservada
        tokens.put("PAIR", 		21);	// Palabra Reservada
        tokens.put("GOTO", 		22);	// Palabra Reservada
        tokens.put("ASIG", 		23);
    }
	// Coordinar el mapeo de tokens con lo que determine el YACC

	static protected int token = -1;
	static protected String lexema = "";
	static protected String lexema_type = ""; //SE REFIERE A CONSTANTE, IDENTIFICADOR O CADENA.
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
				line_number++;
				int i = 0;
				while (i<line.length()) {
					
					AnalizadorLexico.last_char = line.substring(i,i);	// Se actualiza el último caracter
					if (printmode) System.out.println(last_char);
					i += 1 + (automata.getNext(last_char)).execute();	// Punto a resolver: solo con acciones semánticas?
					// La mayoría de las A.S. retornan cero, pero si retorna -1 por ej se queda en el lugar.
					// El flag era horrendo, mala mía.
				}
				// Ojo, acá va salto de línea y tiene que cambiar el estado acorde.
				// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
			}
			line_number = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; //si no encontro ningun token...
	}

	protected int getToken(){
	/* 	// de lexema a numero, mapear a id
		int id = lexToToken(lexema);
		//int id = 0;
		Token token = new Token(id);
		// pero antes de devolverlo (al A.S.) chequear tabla simbolos y agregarlo si corresponde:
		TablaDeSimbolos.put(lexema,token); // si existe el lexema no lo pone.
		return id;
	*/
		return 0;
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
			default:  switch (AnalizadorLexico.lexema_type) {
				case "identifier": id = 16;
				case "const": id = 17;
				case "charChain": id = 18;
				// si no es algun operador, pero ya entra antes.
			} 
		}
		return id;
	}
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
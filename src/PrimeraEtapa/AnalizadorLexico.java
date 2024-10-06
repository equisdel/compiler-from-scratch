package PrimeraEtapa;
import SegundaEtapa.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AnalizadorLexico {

	static Automata automata;
	static TablaDeSimbolos t_simbolos;
	static AccionSemantica[] acciones;

	// Variables relativas a la lectura
	static BufferedReader program_file_reader;
	static public int line_number = 0;

	// Especificaciones del lenguaje
	static final int id_max_length = 15;
	static final int cte_uinteger_min = 0;
	static final int cte_uinteger_max = (int)(Math.pow(2.0,16.0) - 1);	// Rango: [0, 65535]
	static private String[] reserved = {"if","then","else","begin","end","end_if","outf","typedef","fun","ret","uinteger","single","repeat","until","pair","goto"};	// La TdeS lo convierte todo a mayúscula

	// Variables de comunicación entre el A.L. y las acciones semánticas
	static protected int 	token 		= -1;
	static protected String lexema 		= "";
	static protected String lexema_type = ""; 		//ID', 'CTE', 'CHARCH',
	static protected String lexema_subtype = "";	//'uinteger', 'single', 'reserved'
	static protected String next_char;
	

	// INICIALIZACION
	public AnalizadorLexico(){

		// Inicialización del autómata
		String matrizE_filePath = "src/PrimerEtapa/Matrices/matrizEstados.csv";			// Quizás se pasa desde Main (parámetro)
		String matrizA_filePath = "src/PrimerEtapa/Matrices/matrizAcciones.csv";		// Quizás se pasa desde Main (parámetro)
		AnalizadorLexico.automata = new Automata(matrizE_filePath,matrizA_filePath);	

		// Inicialización de la tabla de símbolos + precarga de palabras reservadas
		AnalizadorLexico.t_simbolos = new TablaDeSimbolos();
		for (String p_reservada : reserved)
			t_simbolos.add_entry(p_reservada.toUpperCase(), p_reservada.toUpperCase(), "reserved");
	
		// Inicialización de las acciones semánticas
		AccionSemantica.main(new String[0]);
		acciones = AccionSemantica.all_actions;

	}

	public static void compile(String program_file_path) {
		try { AnalizadorLexico.program_file_reader = new BufferedReader(new FileReader(program_file_path)); } 
		catch (FileNotFoundException e) { System.out.println("NOT FOUND"); e.printStackTrace(); }
	}

	// MODULOS DEL METODO PRINCIPAL

	// METODO PRINCIPAL
	public static int yylex() {

		acciones[0].execute();		// Resetea el estado de las variables estáticas.
		automata.reset();				// Resetea el autómata: estado inicial (0).

		// Lee el próximo token
		int next_char, next_accion_semantica;
		while (!automata.estadoFinal()) {
			try {
				program_file_reader.mark(100);
				next_char = program_file_reader.read();
				AnalizadorLexico.next_char = (new Character((char)next_char)).toString();
				System.out.println(AnalizadorLexico.next_char);
				if (next_char == 10) line_number++;			// Salto de línea
				next_accion_semantica = automata.getNext(AnalizadorLexico.next_char);
				System.out.println("Acción semántica: "+next_accion_semantica);
				if (next_accion_semantica>=0) acciones[next_accion_semantica].execute();
				if (next_char == -1) automata.finalize();
			} catch (IOException e) { e.printStackTrace(); }
		}
		System.out.println("["+AnalizadorLexico.lexema+"]");
		token = lexToToken(lexema);
		//ParserVal yylval = new ParserVal(lexema);
		// Retorna el token asociado (implementar función de mapeo) -> lo que hay en el lexema como entrada
		return token;
		
	/*
	 
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

				}
				// Ojo, acá va salto de línea y tiene que cambiar el estado acorde.
				// estado_actual = matrizTransicionDeEstados[estado_actual][ultimo_caracter];
			}
			line_number = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1; //si no encontro ningun token...
		*/
	}

	static public int lexToToken(String lexeme){
		int id = 0;
		// como mierda sabemos si es id, cte, o cadena multilinea?
		switch (lexeme) { // estos de 2 char empiezan en 257
			case ":=": id = Parser.ASSIGN;
			case "!=": id = Parser.NEQ;
			case "<": id = (int)'<' ;	// castear un char a un int lo transforma en ascii
			case ">": id = (int)'>' ;
			case "<=": id = Parser.LEQ;
			case ">=": id = Parser.MEQ;
			case "+": id = (int)'+';
			case "-": id = (int)'-';
			case "*": id = (int)'*';
			case "=": id = (int)'=';
			case "(": id = (int)'(';			
			case ")": id = (int)')';
			case ",": id = (int)',';
			case ".": id = (int)'.';
			case ";": id = (int)';';
			default:  switch (AnalizadorLexico.lexema_type) {
				case "ID": id = Parser.ID;
				case "CTE": id = Parser.CTE;
				case "CHARCH": id = Parser.CHARCH;
				case "TAG": id = Parser.TAG;
				// si lexema_type es reservada...
				default: switch (lexema_subtype) {
					case "IF": id = Parser.IF;
					case "THEN": id = Parser.THEN;
					case "ELSE": id = Parser.ELSE;
					case "BEGIN": id = Parser.BEGIN;
					case "END": id = Parser.END;
					case "END_IF": id = Parser.END_IF;
					case "OUTF": id = Parser.OUTF;
					case "TYPEDEF": id = Parser.TYPEDEF;
					case "FUN": id = Parser.FUN;
					case "RET": id = Parser.RET;
					case "UINTEGER": id = Parser.UINTEGER;
					case "SINGLE": id = Parser.SINGLE;
					case "REPEAT": id = Parser.REPEAT;
					case "UNTIL": id = Parser.UNTIL;
					case "PAIR": id = Parser.PAIR;
					case "GOTO": id = Parser.GOTO;
				}
			} 
		}
		return id;
	}

	public static void yyerror(String msg){
		System.out.println("Error en la línea "+line_number+": "+msg);
	}
	public static void main(String[] args) {
		AnalizadorLexico.compile("src/test_codes/fulltest");
		Scanner scanner = new Scanner(System.in);
        int input = 0;
        // Mientras el usuario no ingrese -1, el ciclo sigue
		input = scanner.nextInt();
        while (input != -1) {
			System.out.println("token: "+AnalizadorLexico.yylex());
            input = scanner.nextInt();
        }

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
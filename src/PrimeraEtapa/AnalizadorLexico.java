package PrimeraEtapa;
import SegundaEtapa.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AnalizadorLexico {

	public static Automata automata;
	static TablaDeSimbolos t_simbolos;
	static AccionSemantica[] acciones;

	// Variables relativas a la lectura
	static BufferedReader program_file_reader;
	static public int line_number = 0;
	static private ArrayList<String> lex_list = new ArrayList<>();

	// Especificaciones del lenguaje
	static final int id_max_length = 15;
	static final int cte_uinteger_min = 0;
	static final int cte_uinteger_max = (int)(Math.pow(2.0,16.0) - 1);	// Rango: [0, 65535]
	static private String[] reserved = {"if","then","else","begin","end","end_if","outf","typedef","fun","ret","uinteger","single","repeat","until","pair","goto"};	// La TdeS lo convierte todo a mayúscula
	
	// Variables de comunicación entre el A.L. y las acciones semánticas
	static protected String lexema 		= "";
	static protected String lexema_type = ""; 		//ID', 'CTE', 'CHARCH',
	static protected String lexema_subtype = "";	//'uinteger', 'single', 'reserved'
	static protected String next_char;

	//debugging
	static private boolean aldebug = false;

	static public boolean isdebug(){ return aldebug; }

	public static void compile(String program_file_path) {
		AnalizadorLexico.main(new String[0]);
		try { AnalizadorLexico.program_file_reader = new BufferedReader(new FileReader(program_file_path)); } 
		catch (FileNotFoundException e) { System.out.println("[!] PATH ERROR: no existe el programa solicitado."); e.printStackTrace(); }
	}

	public static void display() {
		if (aldebug) {
			System.out.println("Lista de lexemas:");
			for (String lex : lex_list) {
				System.out.print(lex);
			}
		}
		AnalizadorLexico.t_simbolos.display();
	}
	

	public static int yylex() {

		acciones[0].execute();		// Resetea el estado de las variables estáticas.
		automata.reset();				// Resetea el autómata: estado inicial (0).

		// Lee el próximo token
		int next_char, next_accion_semantica;
		while (!automata.estadoFinal()) {
			try {
				program_file_reader.mark(100);
				next_char = program_file_reader.read();		// Lectura del siguiente carac
				// Casos especiales
				if (aldebug) System.out.println("\nSIG CHAR: \'"+next_char+"\'");	
				if (next_char == 10) line_number++;			// Presencia de un salto de línea
				if (next_char == -1) AnalizadorLexico.next_char = "?";	// Fin del programa
				else AnalizadorLexico.next_char = ""+(char)next_char;

				if (aldebug) System.out.println("SIG CHAR: \'"+AnalizadorLexico.next_char+"\'");	// se imprime rarisimo
				next_accion_semantica = automata.getNext(AnalizadorLexico.next_char);
				if (aldebug) System.out.println("Acción semántica: "+next_accion_semantica);
				if (next_accion_semantica>=0) acciones[next_accion_semantica].execute();
				if (next_char == -1) automata.finalize();
			} catch (IOException e) { e.printStackTrace(); }
		}
		int token =lexToToken(lexema);
		if (aldebug) System.out.println(">>>  ["+AnalizadorLexico.lexema+"] -> {"+AnalizadorLexico.lexema_type+"|"+AnalizadorLexico.lexema_subtype+"}");
		if (aldebug == false) System.out.println(">>> Lexema: '"+AnalizadorLexico.lexema+"' -> Token: "+token);
		AnalizadorLexico.lex_list.add("["+new String(AnalizadorLexico.lexema)+"|"+lexToToken(AnalizadorLexico.lexema)+"]");
		//ParserVal yylval = new ParserVal(lexema);
		// Retorna el token asociado (implementar función de mapeo) -> lo que hay en el lexema como entrada
		return token;
		
	}

	static public int lexToToken(String lexeme){
		int id = 0;
		// como mierda sabemos si es id, cte, o cadena multilinea?}
		lexeme = lexeme.toUpperCase();
		switch (lexeme) { // estos de 2 char empiezan en 257
			case ":=": id = Parser.ASSIGN; break;
			case "!=": id = Parser.NEQ; break;
			case "<": id = (int)'<' ; break;	// castear un char a un int lo transforma en ascii
			case ">": id = (int)'>' ; break;
			case "<=": id = Parser.LEQ; break;
			case ">=": id = Parser.MEQ; break;
			case "+": id = (int)'+'; break;
			case "-": id = (int)'-'; break;
			case "*": id = (int)'*'; break;
			case "=": id = (int)'='; break;
			case "(": id = (int)'('; break;		
			case ")": id = (int)')'; break;
			case ",": id = (int)','; break;
			case ".": id = (int)'.'; break;
			case ";": id = (int)';'; break;
			default:  switch (AnalizadorLexico.lexema_type)  {
				case "IF": id = Parser.IF; break; 
				case "THEN": id = Parser.THEN; break;
				case "ELSE": id = Parser.ELSE; break;
				case "BEGIN": id = Parser.BEGIN; break;
				case "END": id = Parser.END; break;
				case "END_IF": id = Parser.END_IF; break;
				case "OUTF": id = Parser.OUTF; break;
				case "TYPEDEF": id = Parser.TYPEDEF; break;
				case "FUN": id = Parser.FUN; break;
				case "RET": id = Parser.RET; break;
				case "UINTEGER": id = Parser.UINTEGER; break;
				case "SINGLE": id = Parser.SINGLE; break; 
				case "REPEAT": id = Parser.REPEAT; break;
				case "UNTIL": id = Parser.UNTIL; break;
				case "PAIR": id = Parser.PAIR; break;
				case "GOTO": id = Parser.GOTO; break;
				case "ID": id = Parser.ID; break;
				case "CTE": id = Parser.CTE; break;
				case "CHARCH": id = Parser.CHARCH; break;
				case "TAG": id = Parser.TAG; break;
				// si lexema_type es reservada...
				default: id =-1; break;
			} 
		}
		return id;
	}


	public static void main(String[] args) {
			// Inicialización del autómata
			String matrizE_filePath = "compiler-from-scratch/src/PrimeraEtapa/Matrices/matrizEstados.csv";			// Quizás se pasa desde Main (parámetro)
			String matrizA_filePath = "compiler-from-scratch/src/PrimeraEtapa/Matrices/matrizAcciones.csv";		// Quizás se pasa desde Main (parámetro)
			AnalizadorLexico.automata = new Automata(matrizE_filePath,matrizA_filePath);	

			// Inicialización de la tabla de símbolos + precarga de palabras reservadas
			AnalizadorLexico.t_simbolos = new TablaDeSimbolos();
			//AnalizadorLexico.t_simbolos.clear();
			for (String p_reservada : reserved)
				t_simbolos.add_entry(p_reservada.toUpperCase(), p_reservada.toUpperCase(), "reserved");
		
			// Inicialización de las acciones semánticas
			AccionSemantica.main(new String[0]);
			acciones = AccionSemantica.all_actions;

	}
}
%{
	package parser;

	import lexer.*;
	import symbol_table.*;
%}

%token IF ELSE END_IF PRINT INT BEGIN END LONG FOREACH IN ID STRING_CONST GREATER_EQUAL LESS_EQUAL EQUAL DISTINCT ASSIGN INT_CONST LONG_CONST


%left '+' '-'
%left '*' '/'

%%

program	:		/* empty */ {System.out.println("Empty program.");}
		|		program_statements
		;

program_statements :		statements
		|		statements block_statements ';'
		|		statements block_statements error {yyerror(ERROR_SEMICOLON);}
		|		statements block_statements {yyerror(ERROR_SEMICOLON);}
		|		block_statements ';'
		|		block_statements {yyerror(ERROR_SEMICOLON);}
		|		block_statements error {yyerror(ERROR_SEMICOLON);}
		;

statements	:		declarative_statements
			|		statements 	declarative_statements
			;

declarative_statements	:		variable_declaration_statement  
						|		collection_declaration_statement
						;

variable_declaration_statement	:		type var_list ';'	{System.out.println("Variable declared at line " + $3.end_line + ".");}   
								|		error var_list ';' {yyerror(ERROR_TYPE);}
								|		type error ';' {yyerror(ERROR_IDENTIFIER);}    
								|		type var_list {yyerror(ERROR_SEMICOLON);}  
								;

collection_declaration_statement :		type ID '['index_list']'';' {System.out.println("Colection declared at line " + $6.end_line + ".");}
								|		error ID '['index_list']'';' {yyerror(ERROR_TYPE);}
								|		type error '['index_list']'';' {yyerror(ERROR_COLECTION_ID);}
								|		type ID index_list']'';' {yyerror(ERROR_SQUARE_BRACKET_OPEN);}
								|		type ID '['']'';' {yyerror(ERROR_INDEX);}
								|		type ID '['error']'';' {yyerror(ERROR_INDEX);}
								|		type ID '['index_list error ';' {yyerror(ERROR_SQUARE_BRACKET_CLOSE);}
								|		type ID index_list ';' {yyerror(ERROR_SQUARE_BRACKET);}
								|		type ID '['index_list']' error {yyerror(ERROR_SEMICOLON);}
								; 

var_list 	:		ID	 
				|		var_list ',' ID     
				|		var_list ID {yyerror(ERROR_COMMA, $1.begin_line);}
				|		',' ID {yyerror(ERROR_IDENTIFIER, $1.begin_line);}
				;

index_list 	: 		index
			|		index_list ',' index 
			|		index_list index {yyerror(ERROR_COMMA, $1.begin_line);}
			; 

index 	:		'_'
			|		INT_CONST  
			;  
			
block_statements 	:		BEGIN statement_block END 
					|		error statement_block END {yyerror(ERROR_BEGIN);}
					|		BEGIN error END {yyerror(ERROR_EXE_STATEMENT);}
					|		BEGIN statement_block error {yyerror(ERROR_END);} 
					|		error statement_block error {yyerror(ERROR_BEGIN); yyerror(ERROR_END);}
					|		error END {yyerror(ERROR_BEGIN, $1.end_line);}
					;

statement_block	:		executional_statements
				|		statement_block executional_statements
				;

executional_statements 	:		if_statement
						|		assign_statement
						|		foreach_in_statement
						|		print_statement
						; 

if_statement 	:		IF '(' condition ')' executional_block END_IF ';' {System.out.println("IF statement at line " + $1.end_line + ".");}
				|		IF '(' condition ')' executional_block else_statement END_IF ';' {System.out.println("IF-ELSE statement at line " + $1.end_line + ".");}
				|		IF error condition ')' executional_block END_IF {yyerror(ERROR_BRACKET_OPEN, $3.begin_line);}
				|		IF condition ')' executional_block END_IF {yyerror(ERROR_BRACKET_OPEN, $3.begin_line);}
				|		IF '('condition executional_block END_IF {yyerror(ERROR_BRACKET_CLOSE, $2.begin_line);}
				|		IF '('condition error executional_block END_IF {yyerror(ERROR_BRACKET_CLOSE, $2.begin_line);}
				|		IF condition executional_block END_IF {yyerror(ERROR_BRACKET, $2.begin_line);}
				|		IF '('error')' executional_block END_IF {yyerror(ERROR_CONDITION, $1.begin_line);}
				|		IF '('')' executional_block END_IF {yyerror(ERROR_CONDITION, $1.begin_line);}
				|		error '('condition')' executional_block END_IF {yyerror(ERROR_IF, $2.begin_line);}
				|		IF '('condition')' error END_IF  {yyerror(ERROR_EXE_STATEMENT, $4.begin_line);}
				|		IF '('condition')' END_IF  {yyerror(ERROR_EXE_STATEMENT, $4.begin_line);}    
				|		IF error condition ')' executional_block else_statement END_IF {yyerror(ERROR_BRACKET_OPEN, $3.begin_line);}
				|		IF condition ')' executional_block else_statement END_IF {yyerror(ERROR_BRACKET_OPEN, $3.begin_line);}
				|		IF '('condition executional_block else_statement END_IF {yyerror(ERROR_BRACKET_CLOSE, $2.begin_line);}
				|		IF '('condition error executional_block else_statement END_IF {yyerror(ERROR_BRACKET_CLOSE, $2.begin_line);}
				|		IF condition executional_block else_statement END_IF {yyerror(ERROR_BRACKET, $2.begin_line);}
				|		IF '('error')' executional_block else_statement END_IF {yyerror(ERROR_CONDITION, $1.begin_line);}
				|		IF '('')' executional_block else_statement END_IF {yyerror(ERROR_CONDITION, $1.begin_line);}
				|		error '('condition')' executional_block else_statement END_IF {yyerror(ERROR_IF, $2.begin_line);}
				|		IF '('condition')' error else_statement END_IF  {yyerror(ERROR_EXE_STATEMENT, $4.begin_line);}
				|		IF '('condition')' else_statement END_IF  {yyerror(ERROR_EXE_STATEMENT, $4.begin_line);}   
				|		IF '('condition')' executional_block error {yyerror(ERROR_END_IF, $4.begin_line);}
				|		IF '(' condition ')' executional_block END_IF error {yyerror(ERROR_SEMICOLON);}
				|		IF '(' condition ')' executional_block else_statement END_IF error {yyerror(ERROR_SEMICOLON);}
				;	

else_statement	: 		ELSE executional_block 
					|		ELSE error {yyerror(ERROR_EXE_STATEMENT, $1.begin_line);}
					|		ELSE {yyerror(ERROR_EXE_STATEMENT, $1.begin_line);}
					|		ELSE executional_block executional_block {yyerror(ERROR_BEGIN, $0.begin_line);}
					;

executional_block 	:		executional_statements
					| 		block_statements ';'
					|		block_statements {yyerror(ERROR_SEMICOLON);}
					;

assign_statement	:		ID ASSIGN expression ';' {System.out.println("Assign statement at line " + $1.end_line + ".");}
					|		error ASSIGN expression ';' {yyerror(ERROR_IDENTIFIER);}
					|		ID error expression ';' {yyerror(ERROR_ASSIGN);}
					|		ID ASSIGN error ';' {yyerror(ERROR_EXPRESSION);}
					|		ID ASSIGN expression {yyerror(ERROR_SEMICOLON);}
					;

foreach_in_statement	:		FOREACH ID IN ID executional_block {System.out.println("FOREACH statement at line " + $1.end_line  + ".");} 
						|		ID IN ID executional_block {yyerror(ERROR_FOR, $1.begin_line);}
						|		ID error IN ID executional_block {yyerror(ERROR_FOR, $1.begin_line);}
						|		FOREACH ID IN error executional_block {yyerror(ERROR_COLECTION_ID, $2.begin_line);}
						|		FOREACH IN ID executional_block {yyerror(ERROR_IDENTIFIER, $1.begin_line);}
						|		FOREACH ID error ID executional_block {yyerror(ERROR_IN, $2.begin_line);}
						;

print_statement	:		PRINT '('STRING_CONST')' ';' {System.out.println("Print statement at line " + lexer.getLine() + ".");}
				|		error '('STRING_CONST')' ';' {yyerror(ERROR_PRINT);}
				|		PRINT STRING_CONST')' ';' {yyerror(ERROR_BRACKET_OPEN);}
				|		PRINT '('STRING_CONST ';' {yyerror(ERROR_BRACKET_CLOSE);}
				|		PRINT STRING_CONST ';' {yyerror(ERROR_BRACKET);}
				|		PRINT '('')' ';' {yyerror(ERROR_STRING);}
				|		PRINT '('STRING_CONST')' {yyerror(ERROR_SEMICOLON);}
				;

condition 	:		expression '<' expression	 
			|		expression '>' expression 	 
			|		expression LESS_EQUAL expression 	 
			| 		expression GREATER_EQUAL expression	 
			| 		expression EQUAL expression			 
			|		expression DISTINCT expression	 
			;

expression	:		expression '+' term
			|		expression '-' term
			|		term
			;

term	:		term '*' factor
		|		term '/' factor
		| 		factor
		;

type	:		INT  
		| 		LONG   
		;

factor 	:		ID 
		|		INT_CONST {checkRange($$.sval, MAX_BITS_INT);}
		|		LONG_CONST {checkRange($$.sval, MAX_BITS_LONG);}
		|		'-' INT_CONST {checkRange($2.sval, MAX_BITS_INT); }
		|		'-' LONG_CONST {checkRange($2.sval, MAX_BITS_LONG);}
		|		ID'['INT_CONST']' {checkRange($3.sval, MAX_BITS_INT);}
		|		ID'[''-'INT_CONST']' {checkRange($4.sval, MAX_BITS_INT);}
		;
 
 %%

 	// Constants declared to print error messages
 	private static final String ERROR_IDENTIFIER = ": identifier expected."; 
 	private static final String ERROR_COLECTION_ID = ": collection identifier expected."; 
 	private static final String ERROR_INDEX = ": collection index expected.";
 	private static final String ERROR_TYPE = ": type expected.";
 	private static final String ERROR_SEMICOLON = ": ';' expected.";
 	private static final String ERROR_COMMA = ": ',' expected."; 
 	private static final String ERROR_SQUARE_BRACKET = " '['']' expected.";
 	private static final String ERROR_SQUARE_BRACKET_OPEN = " '[' expected.";
 	private static final String ERROR_SQUARE_BRACKET_CLOSE = " ']' expected.";
 	private static final String ERROR_BRACKET_OPEN = ": '(' expected.";
 	private static final String ERROR_BRACKET_CLOSE = ": ')' expected.";
 	private static final String ERROR_BRACKET = ": '('')' expected.";
 	private static final String ERROR_BEGIN = ": begin expected."; 
 	private static final String ERROR_END = ": end expected.";
 	private static final String ERROR_IF = ": if expected."; 
 	private static final String ERROR_ELSE = ": else expected."; 
 	private static final String ERROR_END_IF = ": end_if expected.";
 	private static final String ERROR_FOR = ": foreach expected.";
 	private static final String ERROR_IN = ": in expected."; 
 	private static final String ERROR_EXE_STATEMENT = ": executional statement was expected.";   
 	private static final String ERROR_CONDITION = ": condition expected."; 
 	private static final String ERROR_PRINT = ": print expected."; 
 	private static final String ERROR_STRING = ": string expected."; 
 	private static final String ERROR_ASSIGN = ": ':=' expected."; 
 	private static final String ERROR_EXPRESSION = ": expression expected."; 
 	private static final String ERROR_CONST = ": constant out of range.";

 	private int error_counter;

    private LexerAnalyzer lexer;


    // constants to define the numbers of bits to represent an integer const or a ulong const
    private static final int MAX_BITS_INT = 15;
    private static final int MAX_BITS_LONG = 31;

	public Parser(LexerAnalyzer lexer) {
        this.lexer = lexer;
        error_counter = 0;
        //yydebug=true; // uncomment this line to show debug en console
	}

    private int yylex() {
        while (lexer.notEOF()) {
            Token token = this.lexer.getToken();
            if (token != null) {
                this.yylval = new ParserVal(token.getLexeme());
                this.yylval.begin_line = lexer.getLine();
                this.yylval.end_line = lexer.getLine();
                return token.getID();
            }
        }
        return 0;
    } 
    
    private void yyerror(String parser_error) {
    	if (!parser_error.equalsIgnoreCase("syntax error") ) { // ignore default parser error
    		System.out.println("    Syntax error near line " + lexer.getLine() + parser_error);
    		error_counter++;
    	}
    }

    private void yyerror(String parser_error, int line) {
    	if (!parser_error.equalsIgnoreCase("syntax error") ) { // ignore default parser error
    		System.out.println("    Syntax error near line " + line + parser_error);
    		error_counter++;
    	}
    }

    public int getError() {
    	return error_counter;
    }

    // Check if a constants is out of range
    public void checkRange(String myConst, int rangeBits) { 
        long constant = Long.parseLong(myConst); 
        if (constant < -Math.pow(2, rangeBits) || constant > Math.pow(2, rangeBits) - 1) {
            yyerror(ERROR_CONST, lexer.getLine());

        }
        if (constant < 0) {  
            Token t = SymbolTable.getInstance().getSymbol(myConst.substring(1));
            SymbolTable.getInstance().remove(myConst.substring(1));
            SymbolTable.getInstance().addSymbol(new Token(t.getID(), myConst, "negative " + t.getDescription()));
        }
    }
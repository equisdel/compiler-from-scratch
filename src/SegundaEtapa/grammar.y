
%{
        import java.io.*;
        import PrimeraEtapa.AnalizadorLexico;
        

%}


/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales? ID es un token, CTE es otro*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO 

/*
%LEFT '+' '-'
%LEFT '*' '/'
*/

%start prog

%%
/* %start prog eto va aca o atnes del %% ? */
prog    : ID BEGIN statement_list END
        | error BEGIN statement_list END {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
        | error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); } 
        | ID statement_list END {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
        | ID BEGIN statement_list error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
        | ID statement_list {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
        | /* vacio */ {System.out.println("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
        ;


statement_list 
        : statement 
        | statement_list statement
        | statement_list error ';'{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de sentencia.") ;}
        ;
/* AGREGAR EN ALGUN LADO EL POSIBLE ERROR DE SENTENCIA, POR SI POR EJEMPLO DE LA NADA HAY UNA CONDICION*/

statement
        : executable_statement optional_semicolon
        | declare_pair optional_semicolon
        | declare_var
        | declare_fun
        | return_statement optional_semicolon
        /*| error {System.out.println("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia en ;}*/
        /* | return_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

optional_semicolon
    : ';'
    | /* empty*/
        {
            System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
    ;
/* al usar esto, puedo contemplar la falta de ';' sin el token error, y sin shift reduce conflicts */

executable_statement
        : if_statement 
        /* | if_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | assign_statement
        | outf_statement 
        /* | outf_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | repeat_statement
        | goto_statement 
        /* | goto_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | mult_assign_statement 
        /*| mult_assign_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; }*/
        | TAG /* asumimos esta sentencia no termina con ';' */
        ;

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_var
        : var_type var_list ';'
        | var_type ID ';'
        | var_type ID error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de declaracion incorrecta, asegurate haya ',' entre las variables si estas tratando de declarar varias.");}

declare_fun
        : var_type FUN ID '(' parametro ')' BEGIN fun_body END 
        | var_type FUN error '(' parametro ')' BEGIN fun_body END {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ; }
        | var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END  {System.out.println("Error en linea "+AnalizadorLexico.line_number+": solo se permite 1 parametro. "); }
        | var_type FUN ID '(' error ')' BEGIN fun_body END  {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro "); }
        ;

declare_pair
        : TYPEDEF PAIR '<' var_type '>' ID  /* en semantica, se agrega la variable a la TS*/
        | TYPEDEF error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
        | TYPEDEF PAIR error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '<' .") ; }
        | TYPEDEF PAIR '<' var_type '>' error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */
/* al definir un tipo pair, el tipo de variable es personalizado, como se chequea despues ese tipo? es semantica?*/


var_list
    : ID ',' ID
    | var_list ',' ID
    /* | error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de lista de variables. Asegurate haya ',' entre las variables, ; }*/
    ;
/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */

parametro
        : var_type ID
        | ID {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
        | var_type error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
        ;

return_statement
        : RET '(' expr ')'
        | RET expr {System.out.println("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
        ;
/* podria estar en una funcion (bien) o en una sentencia de control,*/
/* si esa sentencia de control esta adentro de una funcion, esta bien, sino no */
/* eso se evalua en el analizador sintatico o despues? es semantica? creo que el sintatico deberia dar el error*/

fun_body
        : statement_list
        ;
        /*si no va nada mas está al pedo la regla*/
        /* POSIBLE ERROR: NO HAY RETURN  (semantica)*/

var_type
        : UINTEGER
        | SINGLE
        | ID /* en semantica se verifica que el tipo haya sido definido por el usuario */
        ;

if_statement
        : IF '(' cond ')' THEN ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        | IF '(' cond ')' THEN ctrl_block_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
        | IF '(' cond ')' THEN error END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
        | IF cond THEN error ELSE ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia de ejecucicion en el IF ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE error END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }

        | IF '(' fun_invoc ')' THEN ctrl_block_statement END_IF
        | IF fun_invoc THEN ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        | IF '(' fun_invoc ')' THEN ctrl_block_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF "); }
        | IF '(' fun_invoc ')' THEN error END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF ") ; }
        | IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF fun_invoc THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. ") ; }
        | IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
        | IF fun_invoc THEN error ELSE ctrl_block_statement END_IF {System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia de ejecucicion en el IF ") ; }
        ; /*error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de sentencia de IF "); }*/
        /* creo que si hay error da error por regla statement */
        /* agregar más especificos: ej. si falta ')' */

ctrl_block_statement
        : executable_statement_list
        ;
        /* ¿? SI ES MAS DE 1 LINEA O SENTENCIA Y VA BEGIN Y END SE AGREGA:*/
        /*      BEGIN executable_statement_list END      */
        /* si no: borro ctrl_block_statement y uso executable_statement_list en el if*/

cond
        : expr cond_op expr
        | expr error expr {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
        /*| fun_invoc */
        ;

cond_op
        : '<'
        | '>'
        | '='
        | NEQ
        | LEQ
        | MEQ
        ;

assign_statement
        : ID ASSIGN expr
        | expr_pair ASSIGN expr 
        | var_type ID ASSIGN expr {System.out.println("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
        ;

expr    : expr '+' term
        | expr '-' term
        | term
       /*| error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando. ; }*/

        /* al tener la sentencia la regla error ';' el compilador tira lo que encuentra hasta un ';' y de ahi sigue compilando */
        /*| expr error{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta una expresion"); }*/
        /*| expr expr {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta una expresion"); }*/
        /*| error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": asegurate no falte alguna expresion u operando"); }
        | error '+' term {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion antes del '+' en ; }
        | error '-' term {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion antes del '-' en ; }
        | expr '+' error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion despues del '+' en ; }
        | expr '-' error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion despues del '-' en ; } */
        ;
        
/* FALTAN ERRORES DE SI FALTA UN EXPR O TERM ...*/

term    : term '*' fact
        | term '/' fact
        | fact
        /*| term error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta un termino"); }*/
        /*| error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": asegurate no falte alguna expresion u operando"); }
        | error '*' fact {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion antes de '*' en ; }
        | error '/' fact {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion antes de '/' en ; }
        | term '*' error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion despues de '*' en ; }
        | term '/' error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba expresion despues de '/* en ; } */
        ;
        /* no parsea nada si falta un * o un / ?? que hace?*/

fact    : ID
        | CTE
        | '-' CTE /* En semantica se chequea que CTE sea del tipo single*/
        | '-' ID /* En semantica se chequea que ID sea del tipo single */
        | fun_invoc
        | expr_pair
        | CHARCH
        ;

expr_pair
        : ID '{' CTE '}' /* en semantica: •Verificar CTE es 1 o 2     • control de tipo de ID */
        ;

fun_invoc
        : ID '(' expr ')'
        | ID '(' expr error ')' {System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
        ;

outf_statement
        : OUTF '(' expr ')'
        | OUTF error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro de OUTF "); }
        ;
        /* en semantica: tipo de parametro incorrecto */

repeat_statement
        : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'
        | REPEAT BEGIN executable_statement_list END UNTIL cond {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' "); }
        | REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')' 
        | REPEAT BEGIN executable_statement_list END UNTIL fun_invoc ';' {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' "); }
        | REPEAT BEGIN error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
        | REPEAT BEGIN executable_statement_list END error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
        ;       

mult_assign_statement
        : id_list ASSIGN expr_list
        | id_list ASSIGN error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
        /*si hay mas de 1 id al aizq y solo 1 expr a la der asumimos esta mal..*/
        ;

id_list
        : elem_list ',' elem_list
        | id_list ',' elem_list
        ;

elem_list
        : ID
        | expr_pair
        ;

/*
a, pint(2) := 124,20;

a,b,c := 1,2,3;
*/

expr_list
        : expr ',' expr
        /* | expr expr {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba una ',' entre las expresiones en ; }  DA ERROR PERO NECESITAMOS TENERLO EN CUENTA */
        | expr_list ',' expr
        /*| error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones sintacticamente incorrecta. Asegurate haya ',' entre las expresiones"); }*/
        ;

goto_statement
        : GOTO TAG 
        | GOTO error {System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
        ;
/* ejemplo de un tag:     tagsito@ */

%%

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */


/* recordar: $$ es el valor del lado izq de la regla. $n del n-ésimo del lado de la derecha */
/* con esto podemos verfiicar algunos errores en vez de reescribir reglas.. */



	public static void yyerror(String msg){
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){
                return AnalizadorLexico.yylex();
        }
        // valor a yylval (lexema)

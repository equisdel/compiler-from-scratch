%{
        import java.io.*;
        import PrimerEtapa.AnalizadorLexico;
%}


/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales?*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG /* los que son unicos como * se usa el ASCII */ IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO


%LEFT '+' '-'
%LEFT '*' '/'

%start prog

%%

prog    : ID BEGIN statement_list END
        | error BEGIN statement_list END {System.println.out("Error: Falta el nombre del programa en la primer linea")}
        | error {System.println.out("Error: Falta delimitador de programa")}
        ;
/*conjunto de sentencias declarativas o ejecutables*/
/*El programa comenzará con un nombre, seguido por un conjunto de sentencias delimitado por BEGIN y END*/

statement_list 
        : statement ';'
        | statement_list statement
        | statement {System.println.out("Error: se esperaba ';' en linea " + AnalizadorLexico.line_number )}
        ;
/* y el salto de linea?*/

statement
        : executable_statement
        | declare_statement
        | return_statement
        | error ';' {System.println.out("Error: sintaxis de sentencia incorrecta")}
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

executable_statement
        : if_statement
        | repeat_statement
        | assign_statement
        | outf_statement
        | repeat_statement
        | mult_assign_statement
        | goto_statement
        ;

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_statement
        : var_type var_list
        | var_type FUN ID '(' parametro ')' BEGIN fun_body END
        | var_type FUN error '(' parametro ')' BEGIN fun_body END {System.println.out("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number)}
        | TYPEDEF PAIR '<' var_type '>' ID
        | TYPEDEF error {System.println.out("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number)}
        | TYPEDEF PAIR error {System.println.out("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number)}
        | TYPEDEF PAIR '<' var_type '>' error {System.println.out("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number)}
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
/* al definir un tipo pair, el tipo de variable es personalizado, como se chequea despues ese tipo? es semantica?*/

parametro
        : var_type ID
        | ID {System.println.out("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number)}
        | var_type error {System.println.out("Error: se esperaba nombre de parametro")}
        ;

return_statement
        : RET '(' expr ')'
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
        ;

var_list
        : ID
        | var_list ',' ID
        | var_list ID {System.println.out("Error: se espera ',' luego de cada variable, linea "+AnalizadorLexico.line_number)}
        ;

if_statement
        : IF '(' cond ')' THEN ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
        | IF '(' cond ')' THEN ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
        | IF '(' cond ')' THEN error END_IF {System.println.out("Error: Se esperaba bloque de sentencias en linea "+AnalizadorLexico.line_number)}
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
        | IF cond THEN error ELSE ctrl_block_statement END_IF {Sysmtem.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
        | error END_IF {System.println.out("Error: sintaxis incorrecta de sentencia de IF en linea "+AnalizadorLexico.line_number)}
        ;
        /* sigo sin entender bien el token error.. revisar todos estos errores */

ctrl_block_statement
        : executable_statement_list
        | error {System.println.out("Error: se esperaba sentencia ejecutable en linea "+AnalizadorLexico.line_number)}
        ;
        /* ¿? SI ES MAS DE 1 LINEA O SENTENCIA Y VA BEGIN Y END SE AGREGA:*/
        /*      BEGIN executable_statement_list END      */
        /* si no: borro ctrl_block_statement y uso executable_statement_list en el if*/

cond
        : expr cond_op expr
        | expr expr {System.println.out("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number)}
        | fun_invoc
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
        ;

expr    : expr '+' term
        | expr '-' term
        | term
        | expr term {System.println.out("Error: se esperaba operador en linea "+AnalizadorLexico.line_number)}
        | error '+' term {System.println.out("Error: se esperaba expresion antes del '+' en linea "+AnalizadorLexico.line_number)}
        | error '-' term {System.println.out("Error: se esperaba expresion antes del '-' en linea "+AnalizadorLexico.line_number)}
        | expr '+' error {System.println.out("Error: se esperaba expresion despues del '+' en linea "+AnalizadorLexico.line_number)}
        | expr '-' error {System.println.out("Error: se esperaba expresion despues del '-' en linea "+AnalizadorLexico.line_number)}
        ;

term    : term '*' fact
        | term '/' fact
        | fact
        | term fact {System.println.out("Error: se esperaba operador en linea "+AnalizadorLexico.line_number)}
        | error '*' fact {System.println.out("Error: se esperaba expresion antes de '*' en linea "+AnalizadorLexico.line_number)}
        | error '/' fact {System.println.out("Error: se esperaba expresion antes de '/' en linea "+AnalizadorLexico.line_number)}
        | term '*' error {System.println.out("Error: se esperaba expresion despues de '*' en linea "+AnalizadorLexico.line_number)}
        | term '/' error {System.println.out("Error: se esperaba expresion despues de '/* en linea "+AnalizadorLexico.line_number)}
        ;

fact    : ID
        | CTE
        | fun_invoc
        ;

fun_invoc
        : ID '(' expr ')'
        ;

outf_statement
        : OUTF '(' expr ')'
        | OUTF '(' CHARCH ')'
        | OUTF error {System.println.out("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number)}
        | OUTF '(' error ')' {System.println.out("Error: se tipo de parametro incorrecto en linea "+AnalizadorLexico.line_number)}
        ;

repeat_statement
        : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'
        | REPEAT BEGIN executable_statement_list END UNTIL cond {System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
        | REPEAT BEGIN error {System.println.out("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number)}
        | REPEAT BEGIN executable_statement_list END error {System.println.out("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number)}
        | error {System.println.out("Error: error de sintaxis de repeat until en linea "+AnalizadorLexico.line_number)}
        ;       /* el ultimo error es inecesario al tener error en statement (creo) y ademas no hay token d sincronizacion*/

mult_assign_statement
        : id_list ASSIGN expr_list
        ;

id_list
        : ID
        | ID ',' id_list
        ;

expr_list
        : expr
        | expr ',' expr_list
        | expr expr_list {System.println.out("Error: se epseraba ',' luego de la expresion en linea "+AnalizadorLexico.line_number)}
        ;

goto_statement
        : GOTO TAG '@'
        | GOTO error {System.println.out("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number)}
        ;

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */

/* HACER COPIA, QUITAR ERRORES E IR AGREGANDOLOS 1 A 1, TRATANDO LOS CONFLITOS */
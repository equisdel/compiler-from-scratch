
%{
        import java.io.*;
        import PrimeraEtapa.AnalizadorLexico;
%}


/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales? ID es un token, CTE es otro*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG /* los que son unicos como * se usa el ASCII */ IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO 

/*
%LEFT '+' '-'
%LEFT '*' '/'
*/

%start prog

%%

prog    : ID BEGIN statement_list END
        | error BEGIN statement_list END {System.println.out("Error: Falta el nombre del programa en la primer linea");}
        | error {System.println.out("Error: Falta delimitador de programa");}
        ;


statement_list 
        : statement
        | statement_list statement
        ;

statement
        : executable_statement
        | declare_statement
        | return_statement ';'
        | return_statement error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | error ';' {System.println.out("Error: sintaxis de sentencia incorrecta");}
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

executable_statement
        : if_statement ';'
        | if_statement error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | assign_statement
        | outf_statement ';'
        | outf_statement error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | repeat_statement
        | goto_statement ';'
        | goto_statement error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | mult_assign_statement ';'
        | mult_assign_statement error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        ;
/* agregar mult_assign_statement cuando no de shift/reduce conflict */

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_statement
        : var_type var_list ';'
        | var_type var_list error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | var_type ID ';'
        | var_type ID error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | var_type FUN ID '(' parametro ')' BEGIN fun_body END ';'
        | var_type FUN ID '(' parametro ')' BEGIN fun_body END error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | var_type FUN error '(' parametro ')' BEGIN fun_body END {System.println.out("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number);}
        | var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END ';' {System.println.out("Error: ',' invalida en linea "+AnalizadorLexico.line_number);}
        | var_type FUN ID '(' error ')' BEGIN fun_body END ';' {System.println.out("Error:  linea "+AnalizadorLexico.line_number);}
        | TYPEDEF PAIR '<' var_type '>' ID ';'
        | TYPEDEF PAIR '<' var_type '>' ID error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | TYPEDEF error {System.println.out("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number);}
        | TYPEDEF PAIR error {System.println.out("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number);}
        | TYPEDEF PAIR '<' var_type '>' error {System.println.out("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number);}
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */
/* al definir un tipo pair, el tipo de variable es personalizado, como se chequea despues ese tipo? es semantica?*/


var_list
    : ID ',' ID
    | ID ',' var_list
    | ID ID {
        yyerror("Error: se espera ',' luego de cada variable, línea " + AnalizadorLexico.line_number);
        yyclearin;
      ;}
    ;
/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */

parametro
        : var_type ID
        | ID {System.println.out("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number);}
        | var_type error {System.println.out("Error: se esperaba nombre de parametro");}
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


if_statement
        : IF '(' cond ')' THEN ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
        | IF '(' cond ')' THEN ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
        | IF '(' cond ')' THEN error END_IF {System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number);}
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
        | IF cond THEN error ELSE ctrl_block_statement END_IF {System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number);}

        | IF '(' fun_invoc ')' THEN ctrl_block_statement END_IF
        | IF fun_invoc THEN ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
        | IF '(' fun_invoc ')' THEN ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
        | IF '(' fun_invoc ')' THEN error END_IF {System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number);}
        | IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF fun_invoc THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
        | IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
        | IF fun_invoc THEN error ELSE ctrl_block_statement END_IF {System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number);}
        ; /*error {System.println.out("Error: sintaxis incorrecta de sentencia de IF en linea "+AnalizadorLexico.line_number);}*/
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
        | expr expr {System.println.out("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number);}
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
        : ID ASSIGN expr ';'
        | ID ASSIGN expr error {System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number);}
        ;


expr    : expr '+' term
        | expr '-' term
        | term
        | error
        ;
        /*| expr error{System.println.out("Error: sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta una expresion");}*/
        /*| expr expr {System.println.out("Error: sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta una expresion");}*/
        /*| error {System.println.out("Error: asegurate no falte alguna expresion u operando");}
        | error '+' term {System.println.out("Error: se esperaba expresion antes del '+' en linea "+AnalizadorLexico.line_number);}
        | error '-' term {System.println.out("Error: se esperaba expresion antes del '-' en linea "+AnalizadorLexico.line_number);}
        | expr '+' error {System.println.out("Error: se esperaba expresion despues del '+' en linea "+AnalizadorLexico.line_number);}
        | expr '-' error {System.println.out("Error: se esperaba expresion despues del '-' en linea "+AnalizadorLexico.line_number);} */
        ;
        
/* FALTAN ERRORES DE SI FALTA UN EXPR O TERM ...*/

term    : term '*' fact
        | term '/' fact
        | fact
        /*| term error {System.println.out("Error: sintaxis de la expresión incorrecta. Chequeá si pusiste el operando, o si falta un termino");}*/
        /*| error {System.println.out("Error: asegurate no falte alguna expresion u operando");}
        | error '*' fact {System.println.out("Error: se esperaba expresion antes de '*' en linea "+AnalizadorLexico.line_number);}
        | error '/' fact {System.println.out("Error: se esperaba expresion antes de '/' en linea "+AnalizadorLexico.line_number);}
        | term '*' error {System.println.out("Error: se esperaba expresion despues de '*' en linea "+AnalizadorLexico.line_number);}
        | term '/' error {System.println.out("Error: se esperaba expresion despues de '/* en linea "+AnalizadorLexico.line_number);} */
        ;
        /* no parsea nada si falta un * o un / ?? que hace?*/

fact    : ID
        | CTE
        | fun_invoc
        ;

fun_invoc
        : ID '(' expr ')'
        | ID '(' expr ',' expr ')' {System.println.out ("Error: no se puede pasar mas de 1 parametro a una funcion, linea "+AnalizadorLexico.line_number);}
        ;

outf_statement
        : OUTF '(' expr ')'
        | OUTF '(' CHARCH ')'
        | OUTF error {System.println.out("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number);}
        ;
        /* en semantica: tipo de parametro incorrecto */

repeat_statement
        : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')' ';'
        | REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')' error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | REPEAT BEGIN executable_statement_list END UNTIL cond {System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number);}
        | REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')' ';'{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')' error {System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
        | REPEAT BEGIN executable_statement_list END UNTIL fun_invoc ';' {System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number);}
        | REPEAT BEGIN error {System.println.out("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number);}
        | REPEAT BEGIN executable_statement_list END error {System.println.out("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number);}
        ;       

mult_assign_statement
        : id_list ASSIGN expr_list
        /*si hay mas de 1 id al aizq y solo 1 expr a la der asumimos esta mal..*/
        ;

id_list
        : ID ',' ID
        | id_list ',' ID
        ;

expr_list
        : expr ',' expr
        | expr expr {System.println.out("Error: se esperaba una ',' entre las expresiones en linea "+AnalizadorLexico.line_number);}
        | expr_list ',' expr
        ;

goto_statement
        : GOTO TAG '@'
        | GOTO error {System.println.out("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number);}
        ;

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */

/* hacer sysout para marcela esta bien, no hace falta guardarlos. */


/* recordar: $$ es el valor del lado izq de la regla. $n del n-ésimo del lado de la derecha */
/* con esto podemos verfiicar algunos errores en vez de reescribir reglas.. */
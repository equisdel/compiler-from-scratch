/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales?*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIG /* los que son unicos como * se usa el ASCII */ IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO


%LEFT + -
%LEFT * /

%start prog

%%

prog    : ID BEGIN statement_list END
        ;
/*conjunto de sentencias declarativas o ejecutables*/
/*El programa comenzará con un nombre, seguido por un conjunto de sentencias delimitado por BEGIN y END*/

statement_list 
        : statement
        | statement_list statement
        ;
/* recursividad a izq */

statement
        : executable_statement
        | declare_statement
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

executable_statement
        : if_statement
        | until_statement
        | assign_statement
        | return_statement
        ;

executable_statement_list
        : executable_statement
        : executable_statement_list executable_statement

declare_statement
        : var_type var_list
        : var_type FUN ID '(' parametro ')' BEGIN fun_body END
        ;

parametro
        : var_type ID
        ;

fun_body
        : statement_list
        ;

return_statement
        : RET '(' expr ')'
        ;
/* podria estar en una funcion (bien) o en una sentencia de control,*/
/* si esa sentencia de control esta adentro de una funcion, esta bien, sino no */
/* eso se evalua en el analizador sintatico o despues? es semantica? creo que el sintatico deberia dar el error*/

var_type
        : UINTEGER
        | SINGLE
        ;

var_list
        : ID
        | var_list ',' ID
        ;

if_statement
        : IF '(' cond ')' THEN ctrl_block_statement END_IF
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        ;

ctrl_block_statement
        : executable_statement_list
        ;
        /* ¿? SI ES MAS DE 1 LINEA O SENTENCIA Y VA BEGIN Y END SE AGREGA:*/
        /*      BEGIN executable_statement_list END      */

cond
        : expr cond_op expr
        ;
        /* AGREGAR: LAS INVOCACIONES A FUNCION TAMBIEN PUEDEN SER CONDICION  */

cond_op
        : '<'
        | '>'
        | '='
        | NEQ
        | LEQ
        | MEQ
        ;

expr    : expr '+' term
        | expr '-' term
        | term
        ;

term    : term '*' fact
        | term '/' fact
        | fact
        ;

fact    : ID
        | CTE
        ;
/* donde mierda iria CHARCH*/
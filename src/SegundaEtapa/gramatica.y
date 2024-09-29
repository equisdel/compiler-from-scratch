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
        ;
/*conjunto de sentencias declarativas o ejecutables*/
/*El programa comenzará con un nombre, seguido por un conjunto de sentencias delimitado por BEGIN y END*/

statement_list 
        : statement ';'
        | statement_list statement
        ;
/* y el salto de linea?*/

statement
        : executable_statement
        | declare_statement
        | return_statement
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

executable_statement
        : if_statement
        | assign_statement
        | outf_statement
        | repeat_statement
        | goto_statement
        | mult_assign_statement
        ;
/* agregar mult_assign_statement cuando no de shift/reduce conflict */

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_statement
        : var_type var_list
        | var_type FUN ID '(' parametro ')' BEGIN fun_body END
        | TYPEDEF PAIR '<' var_type '>' ID
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
/* al definir un tipo pair, el tipo de variable es personalizado, como se chequea despues ese tipo? es semantica?*/

parametro
        : var_type ID
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
        ;
/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */

if_statement
        : IF '(' cond ')' THEN ctrl_block_statement END_IF
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        /* sigo sin entender bien el token error.. revisar todos estos errores */

ctrl_block_statement
        : executable_statement_list
        ;
        /* ¿? SI ES MAS DE 1 LINEA O SENTENCIA Y VA BEGIN Y END SE AGREGA:*/
        /*      BEGIN executable_statement_list END      */
        /* si no: borro ctrl_block_statement y uso executable_statement_list en el if*/

cond
        : expr cond_op expr
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
        ;

term    : term '*' fact
        | term '/' fact
        | fact
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
        ;

repeat_statement
        : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'
        ;       

mult_assign_statement
        : id_list ASSIGN expr_list
        ;



id_list
        : ID ',' ID
        | ID ',' id_list
        ;

expr_list
        : expr
        | expr ',' expr_list
        ;

goto_statement
        : GOTO TAG '@'
        ;

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */

/* HACER COPIA, QUITAR ERRORES E IR AGREGANDOLOS 1 A 1, TRATANDO LOS CONFLITOS */
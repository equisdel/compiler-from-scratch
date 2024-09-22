/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales?*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIG /* los que son unicos como * se usa el ASCII */ IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO


prog    :   /*conjunto de sentencias declarativas o ejecutables*/
            /*El programa comenzará con un nombre, seguido por un conjunto de sentencias delimitado por BEGIN y END*/

bloq    :   sent

s_dec       /*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los
                bloques de las sentencias de control.*/
            /*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/
s_eje

sent    :   /*Cada sentencia debe terminar con punto y coma ";".*/

expr    :   expr '+' term
        |   expr '-' term
        |   term
        ;

term    :   term '*' fact
        |   term '/' fact
        |   fact
        ;

fact    :   '(' fact ')'
        |   ID
        |   CTE
        |   
        
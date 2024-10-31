
%{
        import java.io.*;
        import PrimeraEtapa.*;
        import TercerEtapa.*;

%}


/*Puntos flotantes: ¿están bajo el mismo token que las uinteger y las hexadecimales? ID es un token, CTE es otro*/
%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO 


%start prog

%%

prog    : ID BEGIN statement_list END
        | error BEGIN statement_list END {yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
        | error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); } 
        | ID statement_list END {yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
        | ID BEGIN statement_list error {yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
        | ID statement_list {yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
        | /* vacio */ {yyerror("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
        ;

statement_list 
        : statement
        /*| error ';' {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de sentencia.") ;}*/
        | statement_list statement 
        ;
/* AGREGAR EN ALGUN LADO EL POSIBLE ERROR DE SENTENCIA, POR SI POR EJEMPLO DE LA NADA HAY UNA CONDICION*/

statement
        : executable_statement 
        | declare_pair optional_semicolon       {yyerror("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
        | declare_var   {yyerror("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
        | declare_fun   {yyerror("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
        | error ';' {yyerror("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
        /* | return_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' ; } */
        ;       
/* cada una debe terminar con ;*/
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los*/
/*bloques de las sentencias de control.*/
/*Los elementos declarados sólo serán visibles a partir de su declaración (esto será chequeado en etapas posteriores).*/

optional_semicolon
    : ';'
    | /* empty */
        {
            yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
    ;
/* al usar esto, puedo contemplar la falta de ';' sin el token error, y sin shift reduce conflicts */

optional_not_semicolon
        : ';' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
        | /* vacio */
        ;

executable_statement
        : if_statement optional_semicolon {yyerror("Sentencia de control IF en linea "+AnalizadorLexico.line_number);} 
        /* | if_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | assign_statement optional_semicolon{yyerror("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
        | outf_statement optional_semicolon{yyerror("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
        /* | outf_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | repeat_statement optional_semicolon{yyerror("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
        | goto_statement optional_semicolon{yyerror("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
        /* | goto_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; } */
        | mult_assign_statement optional_semicolon{yyerror("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
        /*| mult_assign_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' en ; }*/
        | return_statement optional_semicolon {yyerror("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
        | TAG optional_not_semicolon/* asumimos esta sentencia no termina con ';' */ {yyerror("Sentencia de TAG");}
        /*| error ';'*/
        ;

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_var
        : var_type var_list ';' {//agregar a cada variable (separada por ',') su tipo, scope,uso

}
        | var_type var_list error {yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                // agregar a cada variable (separar por ',') su tipo,scope,uso
                                }
        | var_type ID ';' {
                chkAndDeclareVar($1.sval, $2.sval);
        }
                /*//debuging:
                yyerror("Variable "+$2.sval+" de tipo "+$1.sval+" declarada en linea "+AnalizadorLexico.line_number+" en el scope "+actualScope);
                }*/
        | var_type ID error {
                yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");
                chkAndDeclareVar($1.sval, $2.sval);
                }
        ;     

declare_fun
        : declare_fun_header fun_body END {
                popScope();
        }
        ;

/* necesario para poder apilar y desapilar ambito */
declare_fun_header
        : var_type FUN ID '(' parametro ')' BEGIN {
                if (!AnalizadorSemantico.validID($1.sval,$3.sval)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // guardar el scope de la funcion, en la tabla
                pushScope($3.sval); 
        }
        | var_type FUN error '(' parametro ')' BEGIN {
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;}
        | var_type FUN ID '(' error ')' BEGIN {
                // guardar el scope de la funcion
                pushScope($3.sval); 
                yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID($1.sval,$3.sval)){
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
        }
        ;

declare_pair
        : TYPEDEF PAIR '<' var_type '>' ID  {
                // OJO ESTO ES UN TIPO, NO UN NOMBRE DE VARIABLE.. 
                if (!AnalizadorSemantico.validID($4.sval,$6.sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                //if (isRedeclared){error de redeclaracion?} si coincide el tipo con nombre de otra variable da error? o solo si coincide con otro tipo?
                AnalizadorLexico.t_simbolos.add_entry($6.sval+":"+actualScope,"ID","pair");
                AnalizadorLexico.t_simbolos.set_use($6.sval+":"+actualScope,"type_name");
                // falta agregar var_type
                // observar que el tipo definido por el usuario tambien tiene alcance.
        }
        | TYPEDEF '<' var_type '>' ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
        | TYPEDEF PAIR var_type ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
        | TYPEDEF PAIR '<' var_type '>' error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */


var_list        /* solo se usa en declaracion multiple. */
        : ID ',' ID
        | var_list ',' ID
       /* | ID error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de lista de variables. Asegurate haya ',' entre las variables ") ; }*/
        ;
/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */

parametro
        : var_type ID
        | ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
        | var_type error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
        ;

return_statement
        : RET '(' expr ')'      /* semanticamente q hacemos con esto? */
        | RET expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
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
/* 
        : IF '(' cond ')' THEN ctrl_block_statement END_IF {}
        | IF cond THEN ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
        | IF '(' cond THEN ctrl_block_statement END_IF {
                yyerror("$1: "+$1.sval+" $$: "+$$.sval+" $4: "+$4.sval); //$3 devuelve el primer lexema de la condicion
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' antes del "+$4.sval+"."); }
        | IF cond ')' THEN ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        | IF '(' cond ')' THEN ctrl_block_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
        | IF '(' cond ')' THEN END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
        | IF '(' cond ')' THEN error END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }


        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF '(' cond ')' THEN ELSE END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
        | IF '(' cond ')' THEN ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE error END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
        | IF '(' cond ')' THEN error ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; }
        | IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este dentro de parentesis. ") ; }
        | IF '(' cond  THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
        | IF  cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
        ;
*/
: if_cond then_statement END_IF {}
| if_cond then_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
| if_cond then_statement END_IF 

| if_cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
| if_cond THEN ELSE END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
| if_cond THEN ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }

| if_cond THEN ctrl_block_statement ELSE END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
| if_cond THEN ctrl_block_statement ELSE error END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
| if_cond THEN error ELSE ctrl_block_statement END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; } 
| if_cond THEN ctrl_block_statement ELSE ctrl_block_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
;

if_cond
        : IF '(' cond ')' {
                $$.sval = Terceto.addTerceto("BF",$3,null)
                Terceto.pushTerceto($$.sval) //apilo terceto incompleto.
        } 
        | IF cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
        | IF '(' cond {
                yyerror("$1: "+$1.sval+" $$: "+$$.sval+" $4: "+$4.sval); //$3 devuelve el primer lexema de la condicion
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' antes del "+$4.sval+"."); }
        | IF cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        ;

then_statement
        : THEN ctrl_block_statement {
                // completo el terceto
                aux = Terceto.popTerceto();
                Terceto.completeTerceto(aux,null,$$.sval);
                $$ = Terceto.addTercetoT("BI",null,null,null); //incompleto, segundo componente se completara despues.
        }
        | THEN  /* va a dar shift reduce, probar THEN error */ {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
        | THEN error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
        ;

ctrl_block_statement
        : executable_statement_list
        ;
        

cond
        : expr cond_op expr {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                $$.sval=Terceto.addTercetoT($2.sval,id1,id2, null);
                // compatiblidades de comparacion??
        }
        | error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
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
        : ID ASSIGN expr {
                //chequear id exista
                // chequear tipos sean el mismo? o compatiblidad y conversion implcita
                //crear terceto
        }
        | expr_pair ASSIGN expr {
                //chequear id exista, sea tipo pair
                // chequear tipos
                //crear terceto
                
        }
        | var_type ID ASSIGN expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
        ;

expr    : expr '+' term    {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                //deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)
                if (isCompatible(t_subtype1,t_subtype2)){
                        $$.sval= Terceto.addTercetoT("+",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
        | expr '-' term         {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                //deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)
                if (isCompatible(t_subtype1,t_subtype2)){
                        $$.sval= Terceto.addTercetoT("+",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }}
        | term 
        | error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
        ;
        // a := 5 + sing1 - 3 * 2 + 1;

term    : term '*' fact {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                //deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)
                if (isCompatible(t_subtype1,t_subtype2)){
                        $$.sval=Terceto.addTercetoT("*",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
                
}
        | term '/' fact {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                if (isCompatible(t_subtype1,t_subtype2)){
                        // CONVERSION IMPLICTA DENTRO DE isCompatible
                        $$.sval=Terceto.addTercetoT("*",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+ AnalizadorLexico.line_number+" :tipos incompatibles en division"); }
                }
        | fact 
        ;

/* toda regla por default, hace $$.sval = $1*/
fact    : ID 
        | CTE
        | '-' CTE  { 
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",strToTID($2.sval),AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | '-' ID {
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",strToTID($2.sval),AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | fun_invoc     //como lleva el tipo esto?? en tabla de funcion guardar el tipo que devuelve.. capaz para toda tabla implementar interfaz comun..
                        //pero igual hay q mandarlo a la tabla de funciones..
                        // podria tomarse el tipo de la funcion aca, o en la regla fun_invoc, y directamente se usa el subtipo del terceto que llega a aca o a term(o expr)
                        // en esta invocacion, guardar el terceto, el tipo de lo que se espera. (y chequear coincida el tipo del parametro esperado, el formal)
        | expr_pair     //este tipo se saca de manera disitinta.. o no?
        | CHARCH
        ;

expr_pair
        : ID '{' CTE '}' /* en semantica: •Verificar CTE es 1 o 2     • control de tipo de ID */{
                if (AnalizadorLexico.t_simbolos.get_subtype($1.sval) != "PAIR") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba variable de tipo pair. "); }
                else {
                        if ($3.sval == "1" || $3.sval == "2") {
                                //Terceto.addTercetoT("[]",strToTID($1.sval),$3.sval,AnalizadorLexico.t_simbolos.get_subtype($1.sval));
                        }
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante 1 o 2. "); }
                }
        }
        ;

fun_invoc
        : ID '(' expr ')'       /* agregamos terceto del llamado???  + verifico si existe la funcion? deberia estar declarada antes (Asumimos) */
        | ID '(' expr error ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
        ;

outf_statement
        : OUTF '(' expr ')'     // terceto mostrar?
        | OUTF '(' ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
        | OUTF error {yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
        ;
        /* en semantica: tipo de parametro incorrecto */



repeat_statement
        : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'
        | REPEAT BEGIN executable_statement_list END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
        | REPEAT BEGIN executable_statement_list END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
        | REPEAT BEGIN executable_statement_list END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }

        | REPEAT BEGIN END UNTIL '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
        | REPEAT BEGIN END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
        | REPEAT BEGIN END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
        | REPEAT BEGIN END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
        | REPEAT BEGIN executable_statement_list END '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
        | REPEAT BEGIN executable_statement_list END UNTIL '(' ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
        /*| REPEAT BEGIN executable_statement_list END UNTIL error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL");} */
        ;

mult_assign_statement
        : id_list ASSIGN expr_list /* chequear cantidades, tipos, crear tercetos */
        | id_list ASSIGN error {yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
        /*si hay mas de 1 id a la izq y solo 1 expr a la der asumimos esta mal..?? ver enunciado*/
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

expr_list       /* solo se usa en asignacion multiple */
        : expr ',' expr
        /* | expr expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba una ',' entre las expresiones en ; }  */
        | expr_list ',' expr
        /*| error {yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones sintacticamente incorrecta. Asegurate haya ',' entre las expresiones"); }*/
        ;

goto_statement
        : GOTO TAG /* debe existir tag (pero puede estar despues, entonces se chequea al final) supongo tambien se agrega terceto */
        | GOTO error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
        ;
/* ejemplo de un tag:     tagsito@ */

%%
        public ArrayList<String> errores = new ArrayList<String>();
        private String actualScope = "main";

	public static void yyerror(String msg){
                errores.add(msg);
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        public String strToTID(String id){      // agrega "<" ">" para indicar es id de terceto, y no clave de TS
                return ("<"+id+">");
        }

        public Boolean isTerceto(String id){
                return (id.charAt(0) == '<' && id.charAt(id.length()-1) == '>');
        }

        public void chkAndGetTerc(ParseVal varParser, String t_subtype1, String id1){
                if isTerceto(varParser.sval) {
                        t_subtype1 = varParser.getSubtipo(varParser.sval);
                        id1 = varParser.sval;
                        }
                else {
                        t_subtype1 = AnalizadorLexico.t_simbolos.get_subtype(varParser.sval);
                        id1 = strToTID(varParser.sval);
                        }
        }


        
        public void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
        }

        public void popScope(){
                // quita ultimo scope, q esta delimitado con ':'
                int index = actualScope.lastIndexOf(":");
                if (index != -1) {
                        actualScope = actualScope.substring(0, index);
                } // else actualScope queda igual
        }

        public boolean isRedeclared(String id, String tipo){
                // chequea si ya fue declarada en el scope actual u otro anterior ( va pregutnando con cada scope, sacando el ultimo)
                String scopeaux = actualScope;
                while (actualScope.lastIndexOf(":") != -1){
                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                actualScope = scopeaux;
                                return true;}
                        popScope();
                }
                actualScope = scopeaux;
                return false;
        }

        public chkAndDeclareVar(String tipo, String id){
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // chequear si la variable ya fue declarada en el scope actual:  si tiene scope asociado y es el actual.
                if (isRedeclared(id,tipo)) {yyerror("WARNING: La variable "+id+" ya fue declarada en el scope actual o uno anterior. ");}
                else {
                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo. (elimino y agrego con esto):
                AnalizadorLexico.t_simbolos.del_entry(id);      //no tiene sentido borrarlo, porque seguro despues vuelva a estar..
                // ej: declaracion de una variable 'var1': el lexico agrega var1 a la tabla de simbolos. luego el sintactico la agrega con su tipo y scope.
                // pero al usarse, ej: var1 := 10;  el lexico agrega var1 a la TS (porque no esta.. esta var1:main) y ahi queda esa suelta 
                // para mi no está mal, lo piden en tp1, pero si va a quedar no tiene sentido borrarla cuando se declara.
                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                }
        }


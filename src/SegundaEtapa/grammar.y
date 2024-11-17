
%{
        import java.util.*;
        import PrimeraEtapa.*;
        import TercerEtapa.*;

%}

%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO 


%start prog

%%

prog    : ID BEGIN statement_list END {
        //TODO: FIN DEL PROGRAMA, CHEQUEAR:
        //TODO: - si hubo goto, chequear tag estaba y era alcanzada
        // llamar a funcion de TablaEtiquetas para actualizar los tercetos de tag
        // llamar a la función de limpieza de la tabla de símbolos (todo lo que no tenga scope)
        AnalizadorLexico.t_simbolos.clean();
        TablaEtiquetas.end();
}
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
/*Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los
bloques de las sentencias de control.*/

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
        | tag_statement optional_not_semicolon/* asumimos esta sentencia no termina con ';' */ {yyerror("Sentencia de TAG");
                //chequear no exista otro tag igual en todo el programa
        }
        /*| error ';'*/
        ;

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_var
        : var_type var_list ';' {
                String[] idList = $2.sval.split(",");
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar($1.sval, idList[i]);
                }

}
        | var_type var_list error {yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                // agregar a cada variable (separar por ',') su tipo,scope,uso
                                }
        | var_type ID ';' {
                chkAndDeclareVar($1.sval, $2.sval);
        }
        | var_type ID error {
                yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");
                chkAndDeclareVar($1.sval, $2.sval);
                }
        ;     

var_list        /* solo se usa en declaracion multiple. */
        : ID ',' ID{
                $$.sval = $1.sval + "," + $3.sval;
        }
        | var_list ',' ID{
                $$.sval = $$.sval + "," + $3.sval;
        }
       /* | ID error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de lista de variables. Asegurate haya ',' entre las variables ") ; }*/
        ;

/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */
//--> DUDA: recursion??? alcance de la misma funcion dentro de la misma?
// TP4: nos toco chequear recursion en ejecución. (ademas de overflow en productos de enteros y resultados negativos en restas de uintegers)
// chequear quien invoca y a quien      ¿agregamos terceto de chequeoo? no creo. en assembler se hara el chequeo para toda funcion.
// FUNCIONES SE HACEN A PARTE, (POR EJ AL FINAL, DELIMITADOS POR TERCETO DELIMITADOR, PARA INICIO Y FIN) PARA PODER DESP LLAMARLAS (O EN LISTA APARTE)
// EN ASSEMBLER, PONER SIOSI RETURN (ASSEMBLER LO NECESITA)
declare_fun     //     FUN uinteger fun1 (uinteger x1) begin DA ' SYNTAX ERROR ' POR PONER PRIMERO FUN Y DEPSUES EL TIPO DE RETORNO
        : declare_fun_header fun_body END { 
                // Actualización del scope: fin de la función fuerza retorno al ámbito del padre
                        yyerror("Salgo del ambito: "+actualScope);
                        popScope();
                }
        ;
// --> DUDA: USO PILA PARA QUE HAYA RETURNS? no hace falta.
//  si hay mas de 1 return no puedo distinguir si es porq estaban seguidos o uno estaba en un if, caso que estaria bien. asiq ni lo chequeo
// se hara un ret 0 en ejecucion (assembler) por las dudas
// si puedo chequear (es facil) que exista al menos 1 return en la funcion.

// ERRORES en ejecucion abortan la ejec.
declare_fun_header

        : var_type FUN ID '(' parametro ')' BEGIN {

                if (!AnalizadorSemantico.validID($1.sval,$3.sval)) yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                else {
                        // Control de ID: debe ser único en el scope actual
                        if (isDeclaredLocal($3.sval))       yyerror("No se permite la redeclaración de variables: el nombre seleccionado no está disponible en el scope actual.");
                        else {
                                String param_name = $5.sval.split("-")[1]; 
                                String param_type = $5.sval.split("-")[0];
                        // Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)
                                AnalizadorLexico.t_simbolos.del_entry($3.sval);
                                AnalizadorLexico.t_simbolos.add_entry($3.sval+":"+actualScope,"ID",$1.sval,"FUN_NAME",param_type);

                                String param_lexem = getDeclared(param_name);

                        // Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función
                                pushScope($3.sval); 

                        // Actualización del ID del parámetro: se actualiza el scope al actual

                                AnalizadorLexico.t_simbolos.del_entry(param_lexem);      // param_name llega con el scope y todo (desde donde fue llamado)
                                AnalizadorLexico.t_simbolos.add_entry(param_name+":"+actualScope,"ID",$1.sval,"VARIABLE_NAME",param_type);

                        // Posible generación de terceto de tipo LABEL
                                $$.sval = Terceto.addTerceto("LABEL",$3.sval+":"+actualScope,null); //para saber donde llamarla en assembler
                        }
                }
        }

        | var_type FUN error '(' parametro ')' BEGIN {
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;
                }

        | var_type FUN ID '(' error ')' BEGIN {
                // guardar el scope de la funcion
                pushScope($3.sval); 
                yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID($1.sval,$3.sval))
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
        ;

declare_pair
        : TYPEDEF PAIR '<' var_type '>' ID  {
                // se le pone scope 'MAIN' 
                if (!AnalizadorSemantico.validID($4.sval,$6.sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                if ($4.sval == "UINTEGER" || $4.sval == "SINGLE" || $4.sval == "HEXA") {

                        if (AnalizadorLexico.t_simbolos.get_entry($6.sval+":MAIN") != null){
                                yyerror("Error: Se esta redeclarando "+$6.sval +" en linea "+AnalizadorLexico.line_number);
                        } else {
                                AnalizadorLexico.t_simbolos.add_entry($6.sval+":MAIN","ID","pair","TYPE_NAME");
                                TablaPair.addPair($6.sval,$4.sval); // necesario? para mi agregar value1 y value2 en TS y listo. facil. no importa la eficiencia.
                                // pero mejor seria que sea una variable mas. en el assembler pasarla como una variable mas y listo.
                                // para esto, ????
                        }
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipo invalido. Solo se permite primitivos: uinteger, single, hexadecimal."); }


        }
        | TYPEDEF '<' var_type '>' ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
        | TYPEDEF PAIR var_type ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
        | TYPEDEF PAIR '<' var_type '>' error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */


parametro
        : var_type ID {
                $$.sval = $1.sval+"-"+$2.sval;
        }
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

//--> DUDAS!!
var_type
        : UINTEGER
        | SINGLE
        | HEXA
        | ID
        ;



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
if_statement
: if_cond then_statement END_IF {       //pdoria poner end_if dentro de then_statement y hacer esto ahi.
        //completo terceto
        Terceto.completeTerceto(Terceto.popTerceto(), null,String.valueOf(Integer.parseInt($2.sval) + 1)); 
}
| if_cond then_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }

| if_cond then_statement else_statement {
        // completo el terceto
        Terceto.completeTerceto(Terceto.popTerceto(),String.valueOf(Integer.parseInt($3.sval) + 1),null); 

}
;

if_cond
        : IF '(' cond ')' {
                $$.sval = Terceto.addTerceto("BF",$3.sval,null);
                Terceto.pushTerceto($$.sval); //apilo terceto incompleto.
        } 
        | IF cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
        | IF '(' cond {
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
        | IF cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
        ;

// if_cond {terceto incompleto 1, salto a rama else o fin del if}
// then ctrl_statement {terceto incompleto 2, salto a fin del if y completo terceto 1}
// else ctrl_statement {completo terceto 2}

//si hay else:
// if_cond {terceto incompleto 1, salto a rama else}
// then ctrl_statement {terceto incompleto 2, salto a fin del if y completo terceto 1}
// else ctrl_statement {completo terceto 2}
//fin del ifelse (nada? podria hacer lo q viene desp de else ctrl_statement)
// si no hay else:
// if_cond {terceto incompleto, salto a fin del if}
// then ctrl_statement {completo}
// fin del ifcomun (nada?)

//compatiblidad entre ambas posibilidades:
// inf_cond {apilo terceto incompleto para salto si no se cumple cond}
// then ctrl_statement {NO COMPLETO NADA. } 
//FIN IF COMUN: completo terceto
//else: apilo terceto incompleto para salto a fin del if y completo terceto cond
// else ctrl_statement : NO HAGO NADA
// fin if else completo terceto incompleto de salto a fin del if
//hay 1 distincion en ambos casos, en el then. para lograrlo:
//

then_statement
        : THEN ctrl_block_statement {
                $$.sval = $2.sval;       //devuelve ultimo terceto
        }
        /*| THEN  {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }*/
        | THEN error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
        ;

else_statement
        : else_tk ctrl_block_statement END_IF {$$.sval = $2.sval;}      //devulve ultimo terceto
        | else_tk END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. "); }
        | else_tk error END_IF {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta "); }
        | else_tk ctrl_block_statement error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
        ;

else_tk
        : ELSE {
                $$.sval = Terceto.addTerceto("BI",null,null); //incompleto, primer operando se completara despues.
                Terceto.completeTerceto(Terceto.popTerceto(),null,String.valueOf(Integer.parseInt($$.sval) + 1));//creo seria $$.sval + 1 (pasar a int y luego volver a string)
                Terceto.pushTerceto($$.sval);
        }
        ;
ctrl_block_statement
        : executable_statement_list
        ;

cond
        : expr cond_op expr {
                String t_subtype1 = chkAndGetType($1.sval);
                String id1 = $1.sval;
                String t_subtype2 = chkAndGetType($3.sval);
                String id2 = $3.sval;
                // compatiblidades de comparacion?? igual que en operaciones
                // chequear y convertir si es necesario (se hace en varios lugares, pensar en modularizar)
                if (t_subtype1.equals(t_subtype2)){
                        $$.sval=Terceto.addTercetoT($2.sval,id1,id2, null);
                } else if( t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) { $$.sval = Terceto.addTercetoT("utos",id1,null, "SINGLE");
                        $$.sval=Terceto.addTercetoT($2.sval,id1,id2, null);
                } else if( t_subtype2.equals("UINTEGER") || t_subtype2.equals("HEXA")) { $$.sval = Terceto.addTercetoT("utos",id2,null, "SINGLE");
                        $$.sval=Terceto.addTercetoT($2.sval,id1,id2, null);
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en comparacion. "); }
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
                chkAndAssign($1.sval,$3.sval);
                }

        | expr_pair ASSIGN expr {
                chkAndAssign($1.sval,$3.sval);
        }
        | var_type ID ASSIGN expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
        ;

        // el terceto de operaciones es el mismo para signel o uinteger, ya que el tipo del terceto es
        // el tipo resultante de la operacion, por ende el tipo de los operadores
        // ya que antes de operar, se hara la conversion en caso de ser necesario.
expr    : expr '+' term    {
                String t_subtype1 = chkAndGetType($1.sval);
                yyerror("tipo1: "+t_subtype1);
                String id1 = $1.sval;
                String t_subtype2 = chkAndGetType($3.sval);
                yyerror("tipo2: "+t_subtype2);
                String id2 = $3.sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) { $$.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype1);}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (!t_subtype1.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (!t_subtype2.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en suma. "); }
}

//VER TEMA DE REDECLARACIONES DE STRINGS EN JAVA, SI DA O NO PROBLEMA Y  COMO HACER
        | expr '-' term         {
                String t_subtype1 = chkAndGetType($1.sval);
                String id1 = $1.sval;
                String t_subtype2 = chkAndGetType($3.sval);
                String id2 = $3.sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) { $$.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype1);}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        else if (t_subtype2.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en resta. "); }
        }
        | term 
        | error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
        ;
        // a := 5 + sing1 - 3 * 2 + 1;

term    : term '*' fact {
                String t_subtype1 = chkAndGetType($1.sval);
                String id1 = $1.sval;
                String t_subtype2 = chkAndGetType($3.sval);
                String id2 = $3.sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) { $$.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype1);}
                        else {yyerror("Tipo no valido..");}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (!t_subtype1.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (!t_subtype2.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
        | term '/' fact {
                String t_subtype1 = chkAndGetType($1.sval);
                String id1 = $1.sval;
                String t_subtype2 = chkAndGetType($3.sval);
                String id2 = $3.sval;
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) { $$.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype1);}
                                else {yyerror("Tipo no valido..");}
                        } else if (isCompatible(t_subtype1,t_subtype2)){        //son dif pero compatibles, <=> uno es SINGLE y el otro UINTEGER
                                if (!t_subtype1.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                                else if (t_subtype2.equals("SINGLE")) {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                                $$.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en division. "); }
        }
        | fact 
        ;

/* toda regla por default, hace $$.sval = $1*/
fact    : ID    {
                if (isDeclared($1.sval)) {
                        $$.sval = $1.sval;
                } else {
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+$1.sval+" no declarada o no esta al alcance.");
                }
}

        | CTE 
        | '-' CTE  {
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval).equals("SINGLE")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | '-' ID {      // uso tendria q ser "variable_name" o podria ser otra?
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval).equals("SINGLE")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | fun_invoc
        | expr_pair /* pairsito{1}  */ 
        | CHARCH
        ;

expr_pair
        : ID '{' CTE '}' {      // ID ES UN TIPO PERSONALIAZDO POR EL USUARIO, 
                if (isDeclared($1.sval)){
                        if (!AnalizadorLexico.t_simbolos.get_subtype($1.sval).equals("PAIR")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba variable de tipo pair. "); }
                        else {
                                if (!($3.sval.equals("1") || $3.sval.equals("2"))) {
                                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante 1 o 2. "); 
                                }
                        }
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable no declarada. "); }
                //TODO: EN $$.sval DEVOLVER EL LEXEMA QUE REPRESENTARIA A ESE PAIR EN ESA POSICION (POR EJEMPLO pairsito{1})
        }
        ;

fun_invoc
        : ID '(' expr ')' { 
                String lexema = getDeclared($1.sval);
                yyerror(lexema);
                if (lexema != null && AnalizadorLexico.t_simbolos.get_use(lexema).equals("FUN_NAME")) {
                        //chequear tipo de parametros
                        if (!AnalizadorLexico.t_simbolos.get_value(lexema).equals(chkAndGetType($3.sval))) {
                                yyerror("Error en linea "+AnalizadorLexico.line_number+": tipo de parametro incorrecto. ");
                        } else {
                        $$.sval = Terceto.addTercetoT("CALL", lexema, $3.sval,AnalizadorLexico.t_simbolos.get_subtype(lexema));}
                } else {
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": "+$1.sval+" no es una funcion o no esta al alcance. ");
                }
        }
        | ID '(' expr error ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
        ;

outf_statement
        : OUTF '(' expr ')' {
                // ya se chequea la validez de expr antes
                $$.sval = Terceto.addTerceto("OUTF",$3.sval,null);
        }
        | OUTF '(' ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
        | OUTF error {yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
        ;

//tercetos repeat:
//REPEAT BEGIN
// a := b + c
//.. (sentencias)
// END UNTIL (a < c*5)

//Since the condition is evaluated at the end of each iteration,
//a repeat/until loop will always be executed at least once, even if the condition is already true when execution arrives at the loop.
/*
5: (+,a,b)
6: (:=,a,[5])
..
8: (*,c,5)
9: (<, a, [8])
10: (BF, [9], [5])              si NO se cumple la condicion, salta a la sentencia 5 (igual que el BF del if)
*/
repeat_statement
        : repeat_begin executable_statement_list END UNTIL '(' cond ')'{
                $$.sval = Terceto.addTerceto("BF",$6.sval,$1.sval);
                // si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());
        }
        | repeat_begin executable_statement_list END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
        | repeat_begin executable_statement_list END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
        | repeat_begin executable_statement_list END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }

        | repeat_begin END UNTIL '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
        | repeat_begin END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
        | repeat_begin END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
        | repeat_begin END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
        | repeat_begin executable_statement_list END '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
        | repeat_begin executable_statement_list END UNTIL '(' ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
        /*| REPEAT BEGIN executable_statement_list END UNTIL error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL");} */
        ;

repeat_begin
        : REPEAT BEGIN {
                // opcion 1: pila:
                //UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)
                // opcion 2:
                $$.sval = strToTID(Terceto.getTercetoCount());  //paso id del proximo terceto
        }
        ;

mult_assign_statement
        : id_list ASSIGN expr_list /* chequear cantidades, tipos, crear tercetos */{
                String[] idList = $1.sval.split(",");
                String[] exprList = $3.sval.split(",");
                if (idList.length != exprList.length){
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. ");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
        | id_list ASSIGN error {yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
        /*si hay mas de 1 id a la izq y solo 1 expr a la der asumimos esta mal..?? ver enunciado*/
        ;

id_list
        : elem_list ',' elem_list {
                
                $$.sval = $1.sval + "," + $3.sval;

        }
        | id_list ',' elem_list{
                $$.sval = $$.sval + ',' + $3.sval;
        }
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
        : expr ',' expr{
                $$.sval = $1.sval + "," + $3.sval;
        }
        /* | expr expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba una ',' entre las expresiones en ; }  */
        | expr_list ',' expr{
                $$.sval = $$.sval + ',' + $3.sval;
                
        }
        /*| error {yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones sintacticamente incorrecta. Asegurate haya ',' entre las expresiones"); }*/
        ;

tag_statement
        : TAG {
                // buscar si no hay otra tag con el mismo nombre al alcance
                System.out.println("wtf");
                if (!isDeclaredLocal($1.sval)) {
                        // reinserción en la T. de S. con scope actual
                        AnalizadorLexico.t_simbolos.del_entry($1.sval);
                        AnalizadorLexico.t_simbolos.add_entry($1.sval+":"+actualScope,"TAG","","tag_name","");
                        // agregar a la tabla de etiquetas
                        TablaEtiquetas.add_tag($1.sval); 
                } else yyerror("ERROR: La etiqueta "+$1.sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                AnalizadorLexico.t_simbolos.display();
        }
        ;

// hacer lista de gotos pendientes, que al final chequee. evaluar todos los casos aver si esto cumple.
// no complicarsela.
// al encontrar tag podria sacarlo, o directamente guardar en algun lado y al final del programa chequear.

goto_statement
        : GOTO TAG /* debe existir tag (pero puede estar despues, entonces se chequea al final) supongo tambien se agrega terceto */{
                //if existe en TS {
                        yyerror("equisdel");
                        $$.sval= Terceto.addTerceto("JUMP_TAG",null,null);
                        TablaEtiquetas.add_goto($2.sval,Terceto.parseTercetoId($$.sval),AnalizadorLexico.line_number);     // donde puse 0 iría número de línea en lo posible
                //}
        }
        | GOTO error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
        ;
/* ejemplo de un tag:     tagsito@ */

%%
        public static ArrayList<String> errores = new ArrayList<>();
        public static String actualScope = "MAIN";
        //static Stack<Integer> UntilStack = new Stack<>();


	public static void yyerror(String msg){
                errores.add(msg);
	        System.out.println(msg);
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

/* ENCARGARSE DE PASAR DE HEXA A UN INTEGER, NI BIEN LLEGA UN HEXA (ESTE DODNE ESTE) ASI DESP ES LA MISMA LOGICA*/
        public String chkAndGetType(String valStr){  
                //no contempla si la variable es de un tipo personalizado(USO: TYPENAME)
                if (isTerceto(valStr)) {
                        return Terceto.getSubtipo(valStr);
                        }
                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        //cte: lo unico que no tiene scope
                        if (isCte(valStr)) {return AnalizadorLexico.t_simbolos.get_subtype(valStr);}
                        else {  // variable, invoc. a funcion o expr_pair
                                // entre variable e invoc a funcion distingo por el uso, pero ambas tienen el tipo (o tipo de retorno q es lo q me interesa) en el mismo campo.
                                
                                // si es variable o funcion:
                                String lexem = getDeclared(valStr);
                                if (lexem != null){
                                        if (!(AnalizadorLexico.t_simbolos.get_subtype(valStr).equals("SINGLE") || AnalizadorLexico.t_simbolos.get_subtype(valStr).equals("UINTEGER") || AnalizadorLexico.t_simbolos.get_subtype(valStr).equals("HEXA"))) { // es porq es uno definido x el usuario (podria ser hexa?)
                                                //pair no seria, xq si es pair, valStr es un TIPO definido por usuario, y no una variable, y eso se chequea antes de llegar a esta linea.
                                
                                                return TablaPair.getTipo(valStr);               
                                        }
                                        // si es tipo primitivo
                                        return AnalizadorLexico.t_simbolos.get_subtype(valStr);
                                } else yyerror("Error en linea "+AnalizadorLexico.line_number+": identificador "+valStr+" no declarado. ");
                                return "";      //o null?
                        }
                }
        }
        
       /*  public void set_var_scope() {

        }*/

        public Boolean isCte(String valStr){
                return (AnalizadorLexico.t_simbolos.get_entry(valStr) != null); //se pasa sin scope. Si está, es cte (el resto deberia tener como minimo scope :main)
        }

        public void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
                TablaEtiquetas.pushScope();
        }

        public void popScope(){
                actualScope = popScope(actualScope);
                System.out.println("Scope tras salir: "+actualScope);
        }

        public String popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                int index = scope.lastIndexOf(":");
                yyerror("scope: "+scope);
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                TablaEtiquetas.popScope();
                return scope;
        }


        public boolean isDeclared(String id){
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
                String scopeaux = actualScope;
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclaredLocal(id)) {return true;}
                else {
                        while (actualScope.lastIndexOf(":") != -1){
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {       
                                        actualScope = scopeaux;
                                        return true;}
                                popScope();
                        }
                }
                actualScope = scopeaux;
                return false;
        }

        /*
        public Boolean chkVar(String tipo, String id){  // se usa??????????????????????
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                else if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) == null) {
                        yyerror("WARNING: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual ");}
                        // WARNING O ERROR? PODRIA SER WARNING AL NO TENER ENCUENTA ESTA REDECLARACION. ACLARAR SI SE DESCARTA LA REDECLARACION.
                else { AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                        AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                        return true;
                        
                }
        } */

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public void chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                //chequear tipo sea valido
                if (tipo.equals("UINTEGER") || tipo.equals("HEXA") ||tipo.equals("SINGLE") ||(AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME"))) {
                         
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                        yyerror("Error: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                                else {
                                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo:
                                AnalizadorLexico.t_simbolos.del_entry(id);
                                //AnalizadorLexico.t_simbolos.display();
                                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                                }   
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipo de variable no valido. "); }
        }

        public Boolean isCompatible(String t_subtype1,String t_subtype2){  //medio al dope creo, al menos duvuelva el tipo resultante en caso de ser ocmpatible, y null si no.
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2);
        }

        public void chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida  
                yyerror("id: "+id);
                //AnalizadorLexico.t_simbolos.display();
                if (!isDeclared(id))
                {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+id+" no declarada. "); }
                else {
                        //  CTE NO VAN CON SCOPE!
                        // EXPR PUEDE SER :CTE, ID, EXPRPAIR, FUN_INVOC, O UN PUTO TERCETOOO
                        String lexemExpr;
                        String subtypeT;
                        if (isTerceto(expr)) {
                                subtypeT = chkAndGetType(expr);
                                lexemExpr = expr;}   // y lexem es el terceto (expr)
                        else{
                                if (!isCte(expr)){      // si es variable o funcion, o expr_pair
                                        lexemExpr = getDeclared(expr);      // el lexema en la TS ()
                                } else {
                                        lexemExpr = expr;}  // si es cte, la misma es comos e busca en la tabla de simbolos
                                yyerror("expr: "+expr);
                                yyerror("lexemExpr: "+lexemExpr);
                                yyerror("id: "+id);
                                subtypeT = chkAndGetType(lexemExpr);
                        }
                        //id es variable siosi
                        String lexemID = getDeclared(id);
                        String subtypeID = chkAndGetType(lexemID);
                        if (subtypeT.equals(subtypeID)){       
                                Terceto.addTercetoT(":=",id,lexemExpr,subtypeID);
                        }
                        // TODO: FALTA DEVOLVER EN $$ EL ID DEL TERCETO (NECESARIO PARA ESTRUCTURAS DE CONTROL..)
                        else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){    // otros casos?
                                Terceto.addTercetoT("utos",expr,null,"SINGLE");
                                Terceto.addTercetoT(":=",id,expr,"SINGLE");
                        } else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){
                                
                        }  
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }

                }
        }

        public String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR SI id ES ID O FUNCION
                String scopeaux = actualScope;
                //AnalizadorLexico.t_simbolos.display();

                if (isDeclaredLocal(id)) {return id+":"+actualScope;}
                else {
                        while (scopeaux.lastIndexOf(":") != -1){

                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null) {
                                        return id+":"+scopeaux;
                                }
                                scopeaux = popScope(scopeaux);     // lo hace con actualScope
                        }
                        return null;    //si no esta declarada..
                }

        }

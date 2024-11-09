
%{
        import java.io.*;
        import PrimeraEtapa.*;
        import TercerEtapa.*;

%}

%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL PAIR GOTO 


%start prog

%%

prog    : ID BEGIN statement_list END {
        //TODO: FIN DEL PROGRAMA, CHEQUEAR:
        //TODO: - si hubo goto, chequear tag estaba y era alcanzada
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

var_list        /* solo se usa en declaracion multiple. */
        : ID ',' ID
        | var_list ',' ID
       /* | ID error {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de lista de variables. Asegurate haya ',' entre las variables ") ; }*/
        ;

/* no hay problema de ambiguedad porque a diferencia de id_list, esta regla*/
/* puede reducirse solo despues que aparezcla var_type */
//--> DUDA: recursion??? alcance de la misma funcion dentro de la misma?
// TP4: nos toco chequear recursion en ejecución. (ademas de overflow en productos de enteros y resultados negativos en restas de uintegers)
// chequear quien invoca y a quien      ¿agregamos terceto de chequeoo? no creo. en assembler se hara el chequeo para toda funcion.
// FUNCIONES SE HACEN A PARTE, (POR EJ AL FINAL, DELIMITADOS POR TERCETO DELIMITADOR, PARA INICIO Y FIN) PARA PODER DESP LLAMARLAS (O EN LISTA APARTE)
// EN ASSEMBLER, PONER SIOSI RETURN (ASSEMBLER LO NECESITA)
declare_fun
        : declare_fun_header fun_body END { 
                // Actualización del scope: fin de la función fuerza retorno al ámbito del padre
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

                // Control de ID: debe ser único en el scope actual
                        if (isDeclared())                                       yyerror("No se permite la redeclaración de variables: el nombre seleccionado no está disponible en el scope actual.");
                        else { }// ¿La compilación debería seguir? ¿Cómo? 

                // Control de ID: se verifica la primera letra del nombre (algunas iniciales son reservadas)
                        if (!AnalizadorSemantico.validID($1.sval,$3.sval))      yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                        else {} // ¿La compilación debería seguir? ¿Cómo? 

                // Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)
                        AnalizadorLexico.t_simbolos.del_entry($3.sval);
                        AnalizadorLexico.t_simbolos.add_entry($3.sval+":"+actualScope,"ID",$5.sval,"fun_name",$1);
                        AnalizadorLexico.t_simbolos.set_use($3.sval+":"+actualScope,"variable_name");

                // Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función
                        pushScope($3.sval); 
                        
                // Actualización del ID del parámetro: se actualiza el scope al actual
                        AnalizadorLexico.t_simbolos.del_entry($5.sval);
                        AnalizadorLexico.t_simbolos.add_entry($5.sval+":"+actualScope,"ID",$5.sval,"fun_name",$1);
                        AnalizadorLexico.t_simbolos.set_use($5.sval+":"+actualScope,"variable_name");

                // Posible generación de terceto de tipo LABEL
                        // $$.sval = Terceto.addTercetoT("LABEL",ID,null);

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
                // OJO ESTO ES UN TIPO, NO UN NOMBRE DE VARIABLE.. 
                if (!AnalizadorSemantico.validID($4.sval,$6.sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // chequeo si está redeclarado
                if (AnalizadorLexico.t_simbolos.get_entry($6.sval) != null && AnalizadorLexico.t_simbolos.get_entry($6.sval).getUse() == "type_name") {
                        yyerror("Error: tipo ya declarado, linea "+AnalizadorLexico.line_number);
                } else {
                        AnalizadorLexico.t_simbolos.add_entry($6.sval+":"+actualScope,"ID","pair");
                        AnalizadorLexico.t_simbolos.set_use($6.sval+":"+actualScope,"type_name");
                }


        }
        | TYPEDEF '<' var_type '>' ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
        | TYPEDEF PAIR var_type ID {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
        | TYPEDEF PAIR '<' var_type '>' error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */




parametro
        : var_type ID {$$.sval = $1.sval+"-"+$2.sval}
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
        | ID{   // chequear: tipo no definido por el usuario (debe estar definido antes.)
                // chequear: tipo no puede ser igual a nombre de variable ??
                // chequear: tipo no puede ser igual a nombre de funcion ??
                // 
                }
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
                $$.sval = Terceto.addTerceto("BF",$3,null)
                Terceto.pushTerceto($$.sval) //apilo terceto incompleto.
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
                $$.sval = $2.sval       //devuelve ultimo terceto
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
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                $$.sval=Terceto.addTercetoT($2.sval,id1,id2, null);
                // compatiblidades de comparacion?? igual que en operaciones
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
                if (!isDeclared($1.sval))
                        {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+$1.sval+" no declarada. "); }
                else {

                        String subtypeT;
                        String id;
                        chkAndGetTerc($3,subtypeT,id);
                        String subtypeID = AnalizadorLexico.t_simbolos.get_subtype($1.sval);
                        if (subtypeT = subtypeID){
                                Terceto.addTerceto(":=",$1.sval,$3.sval);
                        }
                        else if (subtypeID = "SINGLE" && subtypeT = "UINTEGER"){
                                Terceto.addTercetoT("utos",$3.sval,null,"SINGLE");
                                Terceto.addTerceto(":=",$1.sval,$3.sval);
                        }
                        else if (subtypeID = "UINTEGER" && subtypeT = "SINGLE"){
                                Terceto.addTercetoT("utos",$3.sval,null,"SINGLE");
                                Terceto.addTerceto(":=",$1.sval,$3.sval);
                        }
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }

                        }
                        
                }

        | expr_pair ASSIGN expr {
                //chequear id exista, sea tipo pair
                // chequear tipos
                //crear terceto
                
        }
        | var_type ID ASSIGN expr {yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
        ;

        // el terceto de operaciones es el mismo para signel o uinteger, ya que el tipo del terceto es
        // el tipo resultante de la operacion, por ende el tipo de los operadores
        // ya que antes de operar, se hara la conversion en caso de ser necesario.
expr    : expr '+' term    {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { $$.sval= Terceto.addTercetoT("SUMA",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {$$.sval = Terceto.addTercetoT("SUMA",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("SUMA",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en suma. "); }
}

        | expr '-' term         {
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { $$.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {$$.sval = Terceto.addTercetoT("RESTA",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en resta. "); }
        }
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
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { $$.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {$$.sval = Terceto.addTercetoT("MUL",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        $$.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
        | term '/' fact {
                String t_subtype1;      //probar si anda, en varias reglas declaro estos Strings preo creo dara error de redeclaracion
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc($1,t_subtype1,id1);
                chkAndGetTerc($3,t_subtype2,id2);
                        if (t_subtype1 = t_subtype2){
                                if (t_subtype1 = "SINGLE") { $$.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype);}
                                else if (t_subtype1 = "UINTEGER") {$$.sval = Terceto.addTercetoT("DIV",id1,id2, t_subtype);}
                                else {yyerror("Tipo no valido..")}
                        } else if (isCompatible(t_subtype1,t_subtype2)){
                                if (t_subtype1 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                                else if (t_subtype2 != "SINGLE") {$$.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                                $$.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en division. "); }
        }
        | fact 
        ;

/* toda regla por default, hace $$.sval = $1*/
fact    : ID 
        | CTE
        | '-' CTE  { 
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | '-' ID {
                if (AnalizadorLexico.t_simbolos.get_subtype($2.sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        $$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                }
        | fun_invoc     
        | expr_pair     //este tipo se saca de manera disitinta.. o no?
        | CHARCH
        ;

expr_pair
        : ID '{' CTE '}' /* en semantica:     • control de tipo de ID */{
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
        : ID '(' expr ')' { /* agregamos terceto del llamado???  + verifico si existe la funcion? deberia estar declarada antes (Asumimos) */
                //chequear si existe la funcion
                //chequear si la cantidad de parametros es correcta
                //chequear si los tipos de parametros son correctos
                //crear terceto
        }      
        | ID '(' expr error ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
        ;

outf_statement
        : OUTF '(' expr ')'     {       //habria q hacer algun chequeo???
                $$.sval = Terceto.addTerceto("OUTF", $3.sval, null)} 
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
        : repeat_statement executable_statement_list END UNTIL '(' cond ')'{
                $$.sval = Terceto.addTerceto("BF",$6.sval,$1.sval);
                // si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());
        }
        | repeat_statement executable_statement_list END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
        | repeat_statement executable_statement_list END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
        | repeat_statement executable_statement_list END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }

        | repeat_statement END UNTIL '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
        | repeat_statement END UNTIL cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
        | repeat_statement END UNTIL '(' cond {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
        | repeat_statement END UNTIL cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
        | repeat_statement executable_statement_list END '(' cond ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
        | repeat_statement executable_statement_list END UNTIL '(' ')' {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
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
// DUDA --> ir concatenando strings, con ',' y aca separar y hacer los tercetos individuales.
mult_assign_statement
        : id_list ASSIGN expr_list /* chequear cantidades, tipos, crear tercetos */{
                int cantE = 0;
                int cantID = 0;
                // recorro el string de $1 y el de $3 , separando por ',' los id y expr, para chequear cantidades y luego individualiazr las asignaciones
                // chequeando lo mismo que de una asignacion simple, la compatibilidad de tipos, y creando los tercetos.
                // funcion:
        
                String[] idList = $1.sval.split(",");
                String[] exprList = $3.sval.split(",");
                if (idList.length != exprList.length){
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. ");
                }
                else {
                        for (int i = 0; i < idList.length; i++){
                                String subtypeT;
                                String idT;
                                String subtypeID;
                                String idID;
                                chkAndGetTerc(exprList[i],subtypeT,idT);
                                chkAndGetTerc(idList[i],subtypeID,idID);
                                if (subtypeT = subtypeID){
                                        Terceto.addTerceto(":=",idID,idT);
                                }
                                else if (subtypeID = "SINGLE" && subtypeT = "UINTEGER"){
                                        Terceto.addTercetoT("utos",idT,null,"SINGLE");
                                        Terceto.addTerceto(":=",idID,idT);
                                }
                                else if (subtypeID = "UINTEGER" && subtypeT = "SINGLE"){
                                        Terceto.addTercetoT("utos",idT,null,"SINGLE");
                                        Terceto.addTerceto(":=",idID,idT);
                                }
                                else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }
                        }
                }

                if (cantE != cantID) {yyerror("Error en linea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. "); }
                else { 

                }
                cantE = 0;
                cantID = 0;
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
        :TAG {
                //ponerle scope y chequear no este 'redeclarada'
                //si no está, agregar a tabla de etiquetas
        }
        ;

// hacer lista de gotos pendientes, que al final chequee. evaluar todos los casos aver si esto cumple.
// no complicarsela.
// al encontrar tag podria sacarlo, o directamente guardar en algun lado y al final del programa chequear.

goto_statement
        : GOTO TAG /* debe existir tag (pero puede estar despues, entonces se chequea al final) supongo tambien se agrega terceto */{
                //if existe en TS {

                //}
        }
        | GOTO error {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
        ;
/* ejemplo de un tag:     tagsito@ */

%%
        public ArrayList<String> errores = new ArrayList<String>();
        public String actualScope = "main";
        //static Stack<Integer> UntilStack = new Stack<>();


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

        // EVALUAR PASAR PRIMER PARAMETRO A STRING, ASI LO USO EN MAS LUGARESSSS
        public void chkAndGetTerc(ParseVal varParser, String t_subtype1, String id1){   //t_subtype1 y id1 vienen vacios, vuelven modificadosf
                if isTerceto(varParser.sval) {
                        t_subtype1 = varParser.getSubtipo(varParser.sval);
                        id1 = varParser.sval;
                        }
                else {
                        t_subtype1 = AnalizadorLexico.t_simbolos.get_subtype(varParser.sval);
                        id1 = varParser.sval;
                        }
        }
        
        public void set_var_scope() {

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

        public boolean isDeclared(String id){
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo)
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

        public Boolean chkVar(String tipo, String id){
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                else if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) == null) {
                        yyerror("WARNING: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual ");}
                        // WARNING O ERROR? PODRIA SER WARNING AL NO TENER ENCUENTA ESTA REDECLARACION. ACLARAR SI SE DESCARTA LA REDECLARACION.
                else { AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                        AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                        return true;
                        
                }
        }
// DUDA --> SOLUCIONAMOS ESOS LEXEMAS SUELTOS O NO JODEN? SE PEDIAN PARA TP1
        public chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // chequear si la variable ya fue declarada en el scope actual:  si tiene scope asociado y es el actual.
                
                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                        yyerror("WARNING: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                else {
                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo. (elimino y agrego con esto):
                AnalizadorLexico.t_simbolos.del_entry(id);      //no tiene sentido borrarlo, porque seguro despues vuelva a estar..
                // ej: declaracion de una variable 'var1': el lexico agrega var1 a la tabla de simbolos. luego el sintactico la agrega con su tipo y scope.
                // pero al usarse, ej: var1 := 10;  el lexico agrega var1 a la TS (porque no esta.. esta var1:main) y ahi queda esa suelta 
                // para mi no está mal, lo piden en tp1, pero si va a quedar no tiene sentido borrarla cuando se declara.
                //solucion: en accion semantica chequear (agregnado el socpe actual) si ya esta o no (isDeclared) 
                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                }
        }

        public bool isCompatible(String t_subtype1,String t_subtype2){
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2)
        }
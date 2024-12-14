
%{
        import java.util.*;
        import PrimeraEtapa.*;
        import TercerEtapa.*;
        import PrimeraEtapa.Error;

%}

%token ID CTE CHARCH NEQ LEQ MEQ ASSIGN TAG IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET UINTEGER SINGLE REPEAT UNTIL HEXA PAIR GOTO 


%start prog

%%

prog    : ID BEGIN statement_list END 
                {
                // FIN DEL PROGRAMA, CHEQUEAR:
                //AnalizadorLexico.t_simbolos.clean();    // Limpieza de T. de Simbolos: quita todo lo que no tenga scope
                Terceto.addTerceto("END",null,null);
                TablaEtiquetas.end();                   // Asocia sentencias GoTo con su correspondiente etiqueta
                }
        | error BEGIN statement_list END        {yyerror("Falta el nombre del programa en la primer linea. "); }
        | error                                 {yyerror("La sintaxis del programa es incorrecta." ); } 
        | ID statement_list END                 {yyerror("Falta delimitador del programa 'BEGIN'. "); }
        | ID BEGIN statement_list error         {yyerror("Falta delimitador del programa 'END'.") ; }
        | ID statement_list                     {yyerror("Falta delimitadores del programa. "); }
        | /* vacio */                           {yyerror("El programa está vacío"); }
        ;

statement_list 
        : statement
        | statement_list statement 
        ;
/* AGREGAR EN ALGUN LADO EL POSIBLE ERROR DE SENTENCIA, POR SI POR EJEMPLO DE LA NADA HAY UNA CONDICION*/

statement
        : executable_statement 
        | declare_pair optional_semicolon       {/*System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number+"\n");*/}
        | declare_var                           {/*System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);*/}
        | declare_fun                           {/*System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
        | error ';'                             {yyerror("Sintaxis incorrecta de sentencia.");}
        ;       
        // Terminan con ';'
        // Las sentencias declarativas pueden aparecer en cualquier lugar del código fuente, exceptuando los bloques de las sentencias de control.*/

optional_semicolon
        : ';'
        | /* empty */                           {yyerror("Se esperaba ';' al final de la sentencia.");}
        ;  // Al usar esto, se contempla la falta de ';' sin el token error, y sin shift reduce conflicts.

optional_not_semicolon
        : ';'                                   {yyerror("Se encontró ';' pero esa sentencia no lleva. Proba quitandoselo.");}
        | /* vacio */
        ;

executable_statement
        : if_statement optional_semicolon               {/*System.out.print("Sentencia de control IF en linea "+AnalizadorLexico.line_number+"\n");*/}
        | assign_statement optional_semicolon           {/*System.out.print("Sentencia de asignacion en linea "+AnalizadorLexico.line_number+"\n");*/}
        | outf_statement optional_semicolon             {/*System.out.print("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number+"\n");*/}
        | repeat_statement optional_semicolon           {/*System.out.print("Sentencia de repeat until en linea "+AnalizadorLexico.line_number+"\n");*/}
        | goto_statement optional_semicolon             {/*System.out.print("Sentencia de salto goto en linea "+AnalizadorLexico.line_number+"\n");*/}
        | mult_assign_statement optional_semicolon      {/*System.out.print("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number+"\n");*/}
        | return_statement optional_semicolon           {/*System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
        | tag_statement optional_not_semicolon          {/*System.out.println("Sentencia de TAG\n");*/}        // Asumimos que no lleva ';'
        ;

executable_statement_list
        : executable_statement
        | executable_statement_list executable_statement
        ;

declare_var
        : var_type var_list ';' 
                {
                String[] idList = $2.sval.split(",");                
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar($1.sval, idList[i]);
                }
                }
        | var_type var_list error       
                {
                yyerror("Falta el ';' al final de la sentencia.");
                String[] idList = $2.sval.split(",");
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar($1.sval, idList[i]);
                }
                }
        | var_type ID ';'
                {
                chkAndDeclareVar($1.sval,$2.sval);
                }
        | var_type ID error 
                {
                yyerror("Falta ';' al final de la sentencia o se intenta declarar varias variables sin separarlas con ','.");
                chkAndDeclareVar($1.sval, $2.sval);
                }
        ;     

var_list 
        : ID ',' ID             { $$.sval = $1.sval + "," + $3.sval; }
        | var_list ',' ID       { $$.sval = $$.sval + "," + $3.sval; }
        ;
        // Solo se usa en declaracion multiple.

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
                        $$.sval = Terceto.addTercetoT("END_FUN",scopeToFunction(actualScope),null,null);
                        //System.out.println("Salgo del ambito: "+actualScope);
                        TablaEtiquetas.popScope();
                        popScope();
                }
        ;

// --> DUDA: USO PILA PARA QUE HAYA RETURNS? no hace falta.
//  si hay mas de 1 return no puedo distinguir si es porq estaban seguidos o uno estaba en un if, caso que estaria bien. asiq ni lo chequeo
// se hara un ret 0 en ejecucion (assembler) por las dudas
// si puedo chequear (es facil) que exista al menos 1 return en la funcion.

// ERRORES en ejecucion abortan la ejec.
declare_fun_header

        : var_type FUN ID '(' parametro ')' BEGIN 
                {
                AnalizadorSemantico.validID($2.sval,$3.sval,"FUN_NAME");           // Chequeo de tipos embebidos
                        // Control de ID: debe ser único en el scope actual
                        if (isDeclaredLocal($3.sval))   {
                                String declared_type = chkAndGetType($3.sval);
                                if (declared_type.equals("I"))  // ?
                                yyerror("El nombre seleccionado para la funcion no está disponible en el scope actual.");yyerror("No se permite la redeclaración de funciones: el nombre seleccionado no está disponible en el scope actual.");
                        }
                        else {
                                String param_name = $5.sval.split("-")[1]; 
                                String param_type = $5.sval.split("-")[0];
                               //System.out.println("param_name, param_type == "+param_name+", "+param_type);
                                AnalizadorSemantico.validID(param_type,param_name,"PARAM_NAME");
                                // Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)
                                AnalizadorLexico.t_simbolos.del_entry($3.sval);
                                AnalizadorLexico.t_simbolos.add_entry($3.sval+":"+actualScope,"ID",$1.sval,"FUN_NAME",param_type);
                                //AnalizadorLexico.t_simbolos.display();
                                //String param_lexem = getDeclared(param_name);
                                //System.out.println("param_lexem == "+param_lexem);
                                
                                // Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función
                                String act_scope = new String(actualScope);
                                pushScope($3.sval); 

                                // Actualización del ID del parámetro: se actualiza el scope al actual
                                // AnalizadorLexico.t_simbolos.display();
                                AnalizadorLexico.t_simbolos.del_entry(param_name);      // param_name llega con el scope y todo (desde donde fue llamado)
                                AnalizadorLexico.t_simbolos.add_entry(param_name+":"+actualScope,"ID",param_type,"PARAM_NAME");

                        // Posible generación de terceto de tipo LABEL
                                //System.out.println("Parametro: "+param_name+" de tipo "+param_type);
                                //System.out.println("Lexema: "+param_name+":"+actualScope);
                                $$.sval = Terceto.addTercetoT("INIC_FUN",$3.sval+":"+act_scope,param_name+":"+actualScope,param_type); //para saber donde llamarla en assembler
                        }       // el parametro se 'define' DENTRO de la funcion. entonces tiene el scope de la funcion.
        }

        | var_type FUN error '(' parametro ')' BEGIN {
                yyerror("Se esperaba nombre de funcion.") ;
                }

        | var_type FUN ID '(' error ')' BEGIN {
                // guardar el scope de la funcion
                pushScope($3.sval); 
                yyerror("Parametro incorrecto. Verifica que solo haya 1 parametro. ");
                AnalizadorSemantico.validID($1.sval,$3.sval,"FUN_NAME");
                }
        ;

/* PROBAR: ASIGNAR UN PAR A OTRO PAR */

declare_pair
/* a := b{1} + b{2}
 * en assembler: si es acceso pair, tiene {}
 * guardar en TS b{1} o b{2} con tipo pairsito
 * buscar ese tipo en la TS y fijarse de que tipo primitio es (tipo base)
 *  en assembler: b$1 o b$2 
 *  en TS no hace falta tener 2, con tener variable 'p1' de tipo parsito
 * y el tipo pairsito con su tipo primitivo, ya está. no necesito los dos campos. pero al pasarlo a assembler los creos.
 */
        : TYPEDEF PAIR '<' var_type '>' ID  {
                // se le pone scope 'MAIN' 
                AnalizadorSemantico.validID($4.sval,$6.sval,"TYPE_NAME");
                        if ($4.sval.equals("UINTEGER") || $4.sval.equals("SINGLE") || $4.sval.equals("HEXA")) {

                                if (AnalizadorLexico.t_simbolos.get_entry($6.sval+":MAIN") != null){
                                        yyerror("Se está intentando redeclarar "+$6.sval+".");
                                } else {
                                        AnalizadorLexico.t_simbolos.add_entry($6.sval+":MAIN","ID",$4.sval,"TYPE_NAME");
                                }
                        } else {yyerror("Tipo invalido para pair. Solo se permite primitivos: uinteger, single, hexadecimal."); }
                }

        | TYPEDEF '<' var_type '>' ID {yyerror("Se esperaba 'pair'.") ; }
        | TYPEDEF PAIR var_type ID {yyerror("Se esperaba que el tipo este entre '<' y '>'.") ; }
        | TYPEDEF PAIR '<' var_type '>' error {yyerror("Se esperaba un ID al final de la declaracion."); }
        ;
        /* por lo que entiendo, si pongo error, cualquier otra cosa (por lo q no coincide con otra) matchea */
        /* si no pusiera error y simplemente no pongo lo q puede faltar, es muy especifica la regla de error */
        /* PONER EL ';' AL FINAL QUITA AMBIGUEDADES */


parametro
        : var_type ID {
                $$.sval = $1.sval+"-"+$2.sval;
        }
        | ID {yyerror("Se esperaba tipo del parametro de la funcion. "); }
        | var_type error {yyerror("Se esperaba nombre de parametro."); }
        ;

return_statement
        : RET '(' expr ')' {     /* semanticamente q hacemos con esto? 
        LOS CHEQUEOS SOBRE ESTO SON COMPLICADOS, POR LO Q ENTENDI SOLO HACE FALTA:
        -CHEQUEAR QUE LA FUNCION TENGA AL MENOS UN RETURN SI SE NOS HACE FACIL
        - EN ASSEMBLER PONER SIEMPRE UN RET POR DEFAULT (COMO RET 0) 
        - si hay mas de 1 return es complicado contemplarlo
        - hacer terceto de return?? seria pasar el valor de resultado a AX (entero) o EAX (single) y luego un ret
        
        */
        // CHEQUEO EL RET DEVUELVA ALGO DEL MISMO TIPO QUE DEVUELVE LA FUNCION
        if (AnalizadorLexico.t_simbolos.get_entry(scopeToFunction(actualScope)) != null){
                if (AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)).equals(chkAndGetType($3.sval))){
                       //System.out.println("El tipo de retorno coincide con el tipo de la funcion. ");
                       //System.out.println("tipo de retorno: "+chkAndGetType($3.sval));
                       //$$.sval = Terceto.addTercetoT(":=","@"+scopeToFunction(actualScope),getDeclared($3.sval),AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                       // PERO SI ES TERCETO NO FUNCIONAR
                       String param = (AnalizadorLexico.t_simbolos.get_entry($3.sval) != null && AnalizadorLexico.t_simbolos.get_entry($3.sval).getTipo().equals("ID")) ? getDeclared($3.sval) : $3.sval;
                       $$.sval = Terceto.addTercetoT(":=","@"+scopeToFunction(actualScope),param,AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                       // chequear getDeclared($3.sval) si es pair, sea acceso ( {} )
                        $$.sval = Terceto.addTercetoT("RET",getDeclared($3.sval),scopeToFunction(actualScope),AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                } else {
                        yyerror("El tipo de retorno no coincide con el tipo de la funcion. ");
                }
        } else {//System.out.println("algo anda mal en return_statement, no encuentra la funcion actual ");
               //System.out.println(" actual scope: "+actualScope);
               //System.out.println("no se encontro en la TS: "+scopeToFunction(actualScope));        
        }
        }
        | RET expr {yyerror("Faltan parentesis en sentencia de return. ") ;}
        ;
/* podria estar en una funcion (bien) o en una sentencia de control,*/
/* si esa sentencia de control esta adentro de una funcion, esta bien, sino no */
/* eso se evalua en el analizador sintatico o despues? es semantica? creo que el sintatico deberia dar el error*/

fun_body
        : statement_list
        ;
        /*si no va nada mas está al pedo la regla*/
        /* POSIBLE NO HAY RETURN  (semantica)*/

//--> DUDAS!!
var_type
        : UINTEGER
        | SINGLE
        | HEXA
        | ID 
        ;



/* 
        : IF '(' cond ')' THEN ctrl_block_statement END_IF {}
        | IF cond THEN ctrl_block_statement END_IF {yyerror("Se esperaba que la condicion este entre parentesis. "); }
        | IF '(' cond THEN ctrl_block_statement END_IF {
               //System.out.println("$1: "+$1.sval+" $$: "+$$.sval+" $4: "+$4.sval); //$3 devuelve el primer lexema de la condicion
                yyerror("Se esperaba ')' antes del "+$4.sval+"."); }
        | IF cond ')' THEN ctrl_block_statement END_IF {yyerror("Se esperaba '(' antes de la condicion. "); }
        | IF '(' cond ')' THEN ctrl_block_statement error {yyerror("Se esperaba END_IF.") ; }
        | IF '(' cond ')' THEN END_IF {yyerror("Se esperaba sentencia/s ejecutable/s dentro del IF."); }
        | IF '(' cond ')' THEN error END_IF {yyerror("Sintaxis de sentencia ejecutable dentro del IF, incorrecta. "); }


        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF
        | IF '(' cond ')' THEN ELSE END_IF {yyerror("Se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE. ") ; }
        | IF '(' cond ')' THEN ELSE ctrl_block_statement END_IF {yyerror("Se esperaba sentencia/s ejecutable/s luego del THEN. ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE END_IF {yyerror("Se esperaba sentencia ejecutable luego del else. ") ; }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE error END_IF {yyerror("Sintaxis de sentencia ejecutable luego del else, incorrecta. ") ; }
        | IF '(' cond ')' THEN error ELSE ctrl_block_statement END_IF {yyerror("Sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN. ") ; }
        | IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Se esperaba que la condicion este dentro de parentesis. ") ; }
        | IF '(' cond  THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Se esperaba ')' luego de la condicion. "); }
        | IF  cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF {yyerror("Se esperaba '(' antes de la condicion. "); }
        | IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error {yyerror("Se esperaba END_IF. ") ; }
        ;
*/
if_statement
: if_cond then_statement END_IF {       //pdoria poner end_if dentro de then_statement y hacer esto ahi.
        //completo terceto
        Terceto.print_all();
        //System.out.println("debugging if: "+extractNumber($2.sval));
        //Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt((extractNumber($2.sval)).substring(1,($2.sval).length()-1)+1))+">"); 
        Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt(extractNumber($2.sval))+1)+">"); 
}
| if_cond then_statement error {yyerror("Se esperaba END_IF.") ; }

| if_cond then_statement else_statement {
        // completo el terceto
        Terceto.completeTerceto(Terceto.popTerceto(),"<"+String.valueOf(Integer.parseInt(extractNumber($3.sval)) + 1)+">",null); 

}
;

if_cond
        : IF '(' cond ')' {
                $$.sval = Terceto.addTerceto("BF",$3.sval,null);
                Terceto.pushTerceto($$.sval); //apilo terceto incompleto.
        } 
        | IF cond {yyerror("Se esperaba que la condicion este entre parentesis. "); }
        | IF '(' cond {
                yyerror("Se esperaba ')' luego de la condicion"); }
        | IF cond ')' {yyerror("se esperaba '(' antes de la condicion. "); }
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
        /*| THEN  {yyerror("Se esperaba sentencia/s ejecutable/s dentro del IF. "); }*/
        | THEN error {yyerror("Sintaxis de sentencia ejecutable dentro del IF, incorrecta."); }
        ;

else_statement
        : else_tk ctrl_block_statement END_IF {$$.sval = $2.sval;}      //devulve ultimo terceto
        | else_tk END_IF {yyerror("Se esperaba sentencia ejecutable luego del else. "); }
        | else_tk error END_IF {yyerror("sintaxis de sentencia ejecutable luego del else, incorrecta "); }
        | else_tk ctrl_block_statement error {yyerror("se esperaba END_IF ") ; }
        ;

else_tk
        : ELSE {
                $$.sval = Terceto.addTerceto("BI",null,null); //incompleto, primer operando se completara despues.
                //$$.sval = Terceto.addTerceto("LABEL_CTRL",)
                Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt(extractNumber($$.sval)) + 1)+">");//creo seria $$.sval + 1 (pasar a int y luego volver a string)
                Terceto.pushTerceto($$.sval);
        }
        ;
ctrl_block_statement
        : executable_statement_list
        ;

cond
        : expr cond_op expr {   // NO CONTEMPLA USAR TIPOS DEFINIDOS POR USUARIOOOOOOO
                // ambas expr chequear si son pair, que sean acceso (O CHEQUAR EN GETDECLARED?)
                String t_subtype1 = chkAndGetType($1.sval);
                String id1 = $1.sval;
                if (AnalizadorLexico.t_simbolos.get_entry(id1)!=null && AnalizadorLexico.t_simbolos.get_entry(id1).getTipo().equals("ID")) id1 = getDeclared(id1);
                String t_subtype2 = chkAndGetType($3.sval);
                String id2 = $3.sval;
                if (AnalizadorLexico.t_simbolos.get_entry(id2)!=null && AnalizadorLexico.t_simbolos.get_entry(id2).getTipo().equals("ID")) id2 = getDeclared(id2);
                // compatibilidades y conversiones:
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                $$.sval=Terceto.addTercetoT($2.sval,id1,id2,t_subtype1);       // NO SE SI HACE FALTA TIPO PERO PORLASDUDAS POR AHORA LO PONGO
                        } else if (t_subtype1.equals("SINGLE")) {
                                $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);              // se pasa el tipo de id2 para saber si es uinteger o hexa
                                $$.sval= Terceto.addTercetoT($2.sval,id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")) {
                                $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                $$.sval= Terceto.addTercetoT($2.sval,id1,id2, "SINGLE");
                        }else if(t_subtype1.equals("HEXA") && t_subtype2.equals("UINTEGER")){
                                        $$.sval= Terceto.addTercetoT("utos",id1,null, "HEXA");
                                        $$.sval= Terceto.addTercetoT($2.sval,id1,id2, "UINTEGER");
                        }else if (t_subtype1.equals("UINTEGER") && t_subtype2.equals("HEXA")){
                                        $$.sval= Terceto.addTercetoT("utos",id2,null, "HEXA");
                                        $$.sval= Terceto.addTercetoT($2.sval,id1,id2, "UINTEGER");
                        }
                } else {/*System.out.println("Un tipo dio null en condicion");*/}
                
        }
        | error {yyerror("Se esperaba comparador.") ; }
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
                $$.sval = chkAndAssign($1.sval,$3.sval);
                }

        | expr_pair ASSIGN expr {
                $$.sval = chkAndAssign($1.sval,$3.sval);
        }
        | var_type ID ASSIGN expr {yyerror("No se permite asignacion en declaracion. Separa las sentencias. ") ;}
        ;

        // el terceto de operaciones es el mismo para signel o uinteger, ya que el tipo del terceto es
        // el tipo resultante de la operacion, por ende el tipo de los operadores
        // ya que antes de operar, se hara la conversion en caso de ser necesario.
expr    : expr '+' term    {            
                String id1 = $1.sval;
                String id2 = $3.sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType($1.sval);             
               //System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType($3.sval);
               //System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        $$.sval= Terceto.addTercetoT("SUMA",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        $$.sval= Terceto.addTercetoT("SUMA",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        $$.sval= Terceto.addTercetoT("SUMA",Terceto.getLast(),id2, "SINGLE");
                        } else {$$.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en suma");*/}
}

//VER TEMA DE REDECLARACIONES DE STRINGS EN JAVA, SI DA O NO PROBLEMA Y  COMO HACER
        | expr '-' term         {            //la expr o el term pueden ser: variable, funcion, expr_pair,cte,terceto.
                String id1 = $1.sval;
                String id2 = $3.sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType($1.sval);             
               //System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType($3.sval);
               //System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        $$.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        $$.sval= Terceto.addTercetoT("RESTA",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        $$.sval= Terceto.addTercetoT("RESTA",Terceto.getLast(),id2, "SINGLE");
                        } else {$$.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en resta");*/}

        }
        | term 
        | error {yyerror("Sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
        ;
        // a := 5 + sing1 - 3 * 2 + 1;

term    : term '*' fact {            
                String id1 = $1.sval;
                String id2 = $3.sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType($1.sval);             
               //System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType($3.sval);
               //System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        $$.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        $$.sval= Terceto.addTercetoT("MUL",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        $$.sval= Terceto.addTercetoT("MUL",Terceto.getLast(),id2, "SINGLE");
                        } else {$$.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en multiplicacion");*/}
        }
        | term '/' fact {            
                String id1 = $1.sval;
                String id2 = $3.sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType($1.sval);             
               //System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType($3.sval);
               //System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        $$.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        $$.sval= Terceto.addTercetoT("DIV",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        $$.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        $$.sval= Terceto.addTercetoT("DIV",Terceto.getLast(),id2, "SINGLE");            // HACER ESTO EN EL RESTO DE OP ARITMETICASSSSSSSSSSSSSSSSSSSSS
                        } else {$$.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en division");*/}
        }
        | fact 
        ;

/* toda regla por default, hace $$.sval = $1*/
fact    : ID    
        | CTE 
        | '-' CTE  {    // si cont de CTE es 1, reemplazo esa entrada de la TS por -CTE
                // si es mas de 1, creo nueva entrada con -CTE
        if (!AnalizadorLexico.t_simbolos.get_subtype($2.sval).equals("SINGLE")) {yyerror("Se esperaba constante de tipo single. "); }
        else {
                //$$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
                if (AnalizadorLexico.t_simbolos.getCont($2.sval) == 1){
                        AnalizadorLexico.t_simbolos.del_entry($2.sval);
                } 
                AnalizadorLexico.t_simbolos.add_entry("-"+$2.sval,"CTE","SINGLE");
        }
        $$.sval = "-"+$2.sval;
}
/*| '-' ID {      // uso tendria q ser "variable_name" o podria ser otra? ESTO NO HACE FALTA PERMITIRLO, AFUERAAA
        if (AnalizadorLexico.t_simbolos.get_subtype($2.sval).equals("SINGLE")) {yyerror("Se esperaba variable de tipo single. "); }
        else {
                $$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}
        }*/
        | fun_invoc     // ej: funcion1(x)
        | expr_pair /* pairsito{1}  */ 
        ;

expr_pair
        : ID '{' CTE '}' {       
                if (isDeclared($1.sval)){
                        // si ID es de un tipo definido (el tipo de ID esta en la tabla de simbolos)
                        String lexem = getDeclared($1.sval);
                        String baseType = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                       //System.out.println("lexem: "+lexem+" baseType: "+baseType);
                        if (!AnalizadorLexico.t_simbolos.get_use(baseType).equals("TYPE_NAME")) {
                                yyerror("Se esperaba variable de tipo pair. "); }
                        else {
                                if (!($3.sval.equals("1") || $3.sval.equals("2"))) {
                                yyerror("Se esperaba constante 1 o 2. "); 
                                }
                                else {$$.sval = $1.sval+"{"+$3.sval+"}";}
                        }
                } else {yyerror("Variable de tipo pair no declarada. "); }
        }
        ;

fun_invoc
        : ID '(' expr ')' { 
                //System.out.println("\n\nEl lexema a buscar: "+$1.sval);
                String lexema = getDeclared($1.sval);
                if (lexema != null && AnalizadorLexico.t_simbolos.get_use(lexema).equals("FUN_NAME")) {
                        // CHEQUEAR QUE SI EXPR ES PAIR, SEA CON ACCESO ({})
                        //chequear tipo de parametros
                        if (!AnalizadorLexico.t_simbolos.get_value(lexema).equals(chkAndGetType($3.sval))) {
                                yyerror("Tipo de parametro real no coincide con el del parametro formal. ");
                        } else {        // se va pasando el terceto del llamado a la funcion.
                        String param = (AnalizadorLexico.t_simbolos.get_entry($3.sval) != null && AnalizadorLexico.t_simbolos.get_entry($3.sval).getTipo().equals("ID")) ? getDeclared($3.sval) : $3.sval;
                        $$.sval = Terceto.addTercetoT("CALL_FUN", lexema, param, AnalizadorLexico.t_simbolos.get_subtype(lexema));}
                } else {
                        yyerror($1.sval+" no es una funcion o no esta al alcance. ");
                }
        }
        | ID '(' expr error ')' {yyerror("Sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion. ") ; }
        ;

outf_statement
        : OUTF '(' expr ')' {   //expr puede  VARIBLE, CTE, funcion, terceto(varaux),
                // si es ID o funcion o exprpair se pasa con scope
                // CHEQUEAR LA EXPR SEA VALIDA, ES DECIR SI ES VARIABLE O FUNCIOn, QUE ESTE DECLARADO
                // y si es pair pasarlo bien
               //System.out.println("scope actual: "+actualScope);
                String lexem = $3.sval;
                String pos = "";
                if (!isTerceto(lexem) && (!isCte(lexem)) && (!isCharch(lexem))){
                        // es variable o funcion
                        if (isPairAccess(lexem)) {
                                pos = lexem.substring(lexem.lastIndexOf("{"),lexem.lastIndexOf("}") + 1);
                               //System.out.println("pos: "+pos);
                                lexem = getDeclared(getPairName(lexem)) + pos;
                        } else {
                                // TODO chequear no sea una variable pair sin los {} !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                lexem = getDeclared(lexem);
                        }
                }
                $$.sval = Terceto.addTercetoT("OUTF",lexem,null,chkAndGetType($3.sval));
        }
        | OUTF '(' CHARCH ')'{
                $$.sval = Terceto.addTercetoT("OUTF",$3.sval,null,"CHARCH");
        }
        | OUTF '(' ')' {yyerror("Se esperaba parametro en OUTF. "); }
        | OUTF error {yyerror("Parametro incorrecto en OUTF. "); }
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
        | repeat_begin executable_statement_list END UNTIL cond {yyerror("Se esperaba que la condicion este entre parentesis. "); }
        | repeat_begin executable_statement_list END UNTIL '(' cond {yyerror("Se esperaba ')' luego de la condicion UNTIL. "); }
        | repeat_begin executable_statement_list END UNTIL cond ')' {yyerror("Se esperaba '(' antes de la condicion UNTIL. "); }

        | repeat_begin END UNTIL '(' cond ')' {yyerror("Se esperaba cuerpo de REPEAT-UNTIL."); }
        | repeat_begin END UNTIL cond {yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y que la condicion este entre parentesis. "); }
        | repeat_begin END UNTIL '(' cond {yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y ')' luego de la condicion. "); }
        | repeat_begin END UNTIL cond ')' {yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y'(' antes de la condicion. "); }
        | repeat_begin executable_statement_list END '(' cond ')' {yyerror("Se esperaba UNTIL luego de 'END'."); }
        | repeat_begin executable_statement_list END UNTIL '(' ')' {yyerror("Se esperaba condicion luego de UNTIL."); }
        /*| REPEAT BEGIN executable_statement_list END UNTIL error {yyerror("Se esperaba condicion luego de UNTIL.");} */
        ;

repeat_begin
        : REPEAT BEGIN {
                // opcion 1: pila:
                //UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)
                // opcion 2:
                $$.sval = strToTID(Terceto.getTercetoCount());  //paso id del proximo terceto
                Terceto.addTerceto("LABEL_TAG","labelt_"+Terceto.getTercetoCount(),null); // pone un label al inicio de la esctructura
        }
        ;

mult_assign_statement
        : id_list ASSIGN expr_list /* chequear cantidades, tipos, crear tercetos */{
                String[] idList = $1.sval.split(",");
                String[] exprList = $3.sval.split(",");
                // si hay mas expr que id, esas expr se descartan.
                // en ambos casos se informa warning
                if (idList.length > exprList.length){
                // si hay mas ids que expr, las ids sobrantes se las asigna 0.
                        for (int i = 0; i < exprList.length; i++){
                                $$.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                        // agrego a 0 a las TS
                        AnalizadorLexico.t_simbolos.add_entry("0","CTE","UINTEGER");
                        for (int i = exprList.length; i < idList.length; i++){
                                $$.sval = chkAndAssign(idList[i],"0");
                        }
                        new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es mayor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
                } else if (idList.length < exprList.length){
                        for (int i = 0; i < idList.length; i++){
                                $$.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                        new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es menor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                $$.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
        | ID ASSIGN expr_list {
                String[] exprList = $3.sval.split(",");
                $$.sval = chkAndAssign($1.sval,exprList[0]);
                new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es menor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
        }
        | id_list ASSIGN error {yyerror("La lista de expresiones es incorrecta, puede que falte ',' entre las expresiones. ") ; }
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
        /* | expr expr {yyerror("Se esperaba una ',' entre las expresiones en ; }  */
        | expr_list ',' expr{
                $$.sval = $$.sval + ',' + $3.sval;
                
        }
        /*| error {yyerror("Lista de expresiones sintacticamente incorrecta. Asegurate haya ',' entre las expresiones"); }*/
        ;

tag_statement               //chequear no exista otro tag igual en todo el programa
        : TAG {
                // buscar si no hay otra tag con el mismo nombre al alcance
               //System.out.println("wtf");
                if (!isDeclaredLocal($1.sval)) {
                        // que no empiece con s,u,v,w
                        if ($1.sval.charAt(0) == 's' || $1.sval.charAt(0) == 'u' || $1.sval.charAt(0) == 'v' || $1.sval.charAt(0) == 'w') {
                                yyerror("El nombre de la etiqueta no puede comenzar con 's', 'u', 'v' o 'w'. ");
                        } else {

                                // reinserción en la T. de S. con scope actual
                                AnalizadorLexico.t_simbolos.del_entry($1.sval);
                                AnalizadorLexico.t_simbolos.add_entry($1.sval+":"+actualScope,"TAG","","tag_name","");
                                // agregar a la tabla de etiquetas
                                TablaEtiquetas.add_tag($1.sval); 
                                Terceto.addTerceto("LABEL_TAG",$1.sval+":"+actualScope,null);
                        }
                } else yyerror("La etiqueta "+$1.sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                // AnalizadorLexico.t_simbolos.display();
        }
        ;

// hacer lista de gotos pendientes, que al final chequee. evaluar todos los casos aver si esto cumple.
// no complicarsela.
// al encontrar tag podria sacarlo, o directamente guardar en algun lado y al final del programa chequear.

goto_statement
        : GOTO TAG /* debe existir tag (pero puede estar despues, entonces se chequea al final) supongo tambien se agrega terceto */{
                //if existe en TS {
                        $$.sval= Terceto.addTerceto("JUMP_TAG",null,null);      //se pone terceto incompleto, se completara al final del programa
                        TablaEtiquetas.add_goto($2.sval,Terceto.parseTercetoId($$.sval),AnalizadorLexico.line_number);     // donde puse 0 iría número de línea en lo posible
                //}
        }
        | GOTO error {yyerror("Se esperaba TAG en la sentencia GOTO."); }
        ;
/* ejemplo de un tag:     tagsito@ */

%%
        public static ArrayList<String> errores = new ArrayList<>();
        public static String actualScope = "MAIN";
        //static Stack<Integer> UntilStack = new Stack<>();


	public static void yyerror(String msg){
                errores.add(msg);
                new Error(AnalizadorLexico.line_number,msg,true,"SEMANTICO");
	       //System.out.println(msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        public static String strToTID(String id){      // agrega "<" ">" para indicar es id de terceto, y no clave de TS
                return ("<"+id+">");
        }

        public static Boolean isTerceto(String id){
                return (id.charAt(0) == '<' && id.charAt(id.length()-1) == '>');
        }


        public static Boolean isFunction(String id){
                // es terceto y se llama CALL_FUN
                return (isTerceto(id) && Terceto.getOperacion(id).equals("CALL_FUN"));

        }

        public static String getFunctionID(String id){ // recibe terceto porq invoc_funcion arrastra el id del terceto
                // devuelve lexema (ID con scope)
                if (isFunction(id)) {
                        return (Terceto.getOp1(id));
                } else {/*System.out.println("CUIDADO SE ESTA PASANDO UN ID QUE NO ES DE FUNCION A getFunctionID");*/
                        return null;
                }
        }

        public static String chkAndGetType(String valStr){  
                // DEVUELVE TIPO PRIMITIVO (HEXA, UINTEGER, O SINGLE)
                // SI valStr es invoc_funcion, devuelve el tipo de retorno de la funcion
                // Recibe lexema SIN SCOPE 
                // si es pair, llega con {} 
                //AnalizadorLexico.t_simbolos.display();
                String lexem = "";

                // CASO TERCETO
                if (isTerceto(valStr)) { return Terceto.getSubtipo(valStr); }

                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        // CASO CONSTANTE
                        if (isCte(valStr)) {
                               //System.out.println(valStr+"ES cte");
                                return AnalizadorLexico.t_simbolos.get_subtype(valStr);
                        }
                        
                        
                        else {  // variable, invoc. a funcion o expr_pair

                                // CASO PAIR
                                if (isPairAccess(valStr)) {  
                                        lexem = valStr.substring(0,valStr.indexOf("{")); // me quedo con el id del pair
                                        lexem = getDeclared(lexem);
                                } 
                                //else {        // me fijo que si no es un acceso a pair, no sea un pair sin el acceso ( sin {})

                                //}
                                
                                else if (isFunction(valStr)){
                                        lexem = getFunctionID(valStr);  //vuelve con ambito
                                }

                                else { lexem = getDeclared(valStr); }
                                
                                String type = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                               
                                //System.out.println("se busco: "+lexem);
                                if (lexem != null){     // si está declarada
                                        if (!(type.equals("SINGLE") || type.equals("UINTEGER") || type.equals("HEXA"))) {
                                                //es tipo definido por usuario
                                               //System.out.println("El tipo es "+type+" y su uso es "+AnalizadorLexico.t_simbolos.get_use(type));
                                                if ((AnalizadorLexico.t_simbolos.get_use(type)).equals("TYPE_NAME")) {
                                                        return (AnalizadorLexico.t_simbolos.get_subtype(type)); //devuelve el primitivo
                                                }
                                        } else {return AnalizadorLexico.t_simbolos.get_subtype(lexem);}
                                        // si es tipo primitivo
                                } else yyerror("Identificador "+valStr+" no declarado. ");
                                
                                return null;
                        }
                }
        }

       /*  public void set_var_scope() {

        }*/

        public static Boolean isCte(String valStr){
                String valStr_to_upper = valStr.toUpperCase();  // caso HEXA
                if (AnalizadorLexico.t_simbolos.get_entry(valStr_to_upper) != null){
                        return (AnalizadorLexico.t_simbolos.get_entry(valStr_to_upper).getTipo().equals("CTE")); 
                } else return false;
                // si no esta, no es cte o no está
        }

        public static void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
                TablaEtiquetas.pushScope();
        }

        public static void popScope(){
                actualScope = popScope(actualScope);
               //System.out.println("Scope tras salir: "+actualScope);
        }

        public static String popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                int index = scope.lastIndexOf(":");
                //System.out.print("popScope scope: "+scope);
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                return scope;
        }


        public static boolean isDeclared(String id) {   // recibe id sin scoope
                // PROBELMA DE EMBEBIDOS: LLEGA CON :MAIN
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
                if (isCte(id)) {System.out.println("OJO ESTAS PASANDO UNA CTE A isDeclared:"+id);}
                String scopeaux = new String(actualScope);
                //System.out.println("a isDeclared llego: "+id);
                //System.out.println("EL SCOPEAUX: "+scopeaux);
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclaredLocal(id)) {return true;}
                else {
                        do {
                                scopeaux = (scopeaux.equals("MAIN")) ? "MAIN" : scopeaux.substring(0,scopeaux.lastIndexOf(":"));
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null)     
                                        return true;
                                //System.out.println("EL SCOPEAUX: "+scopeaux);
                        } while ((!scopeaux.equals("MAIN")));
                }
                return false;
        }

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public static void chkAndDeclareVar(String tipo, String id){
                
                AnalizadorSemantico.validID(tipo,id,"VARIABLE_NAME");
                //chequear tipo sea valido (uinteger,hexa,single o definido por usuario)
                // AnalizadorLexico.t_simbolos.display();
                if (AnalizadorLexico.t_simbolos.get_entry(tipo) == null) {yyerror("tipo de variable no valido. "); }
                else {//System.out.println("tipo de var declarada:" +AnalizadorLexico.t_simbolos.get_entry(tipo).getUse());
                }
                
                if (tipo.equals("UINTEGER") || tipo.equals("HEXA") ||tipo.equals("SINGLE") || (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME"))) {
                // pair ejemplo en TS: pairsito:main subtipo:uinteger use:typename  no hace falta aclarar es un pair xq es el unico tipo q puede definir  
                // y otra entrada: p1:main:f1 subtipo: pairsito use: variable_name
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                        yyerror("La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                                else if (id.substring(0,1).equals("S") || id.substring(0,1).equals("U") || id.substring(0,1).equals("V") || id.substring(0,1).equals("W")) {
                                        // si comienza con s,u,v,w, ya tiene tipo embebido, y aca da error de redeclaracion
                                        yyerror("Se intento redeclarar un tipo embebido.");
                                } 
                                else {
                                
                                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo:
                                AnalizadorLexico.t_simbolos.del_entry(id);
                                //AnalizadorLexico.t_simbolos.display();
                                if (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME")){
                                        // SI EL TIPO ES DEFINIDO POR USER, VA CON :MAIN
                                        AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo+":MAIN");
                                }else {AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);}
                                
                                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                                }   
                } else {yyerror("tipo de variable no valido. "); }

        }

        public static Boolean isCompatible(String t_subtype1,String t_subtype2){  //medio al dope creo, al menos devuelva el tipo resultante en caso de ser ocmpatible, y null si no.
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2);
        }

        public static String getPairName(String id){
                // devuelve el nombre del pair, sin la posicion de acceso
                return id.substring(0,id.lastIndexOf("{"));             // FIJARSE Q ANDE
        }

        public static String chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida.
                //AnalizadorLexico.t_simbolos.display();
                        //System.out.print("chkAndAssign: id: "+id);
                        String posid = "";
                        String posexpr = "";
                        //System.out.println("chkAndAssign: expr: "+expr);
                        //System.out.println("chkAndAssign: id: "+id);
                        Boolean idPair = isPairAccess(id,"assign");   // en una asignacion, dos pair pueden asignarse
                        Boolean exprPair = isPairAccess(expr,"assign");
                        // PAIR SIN ACCESO PUEDE SOLO SI AMBAS PARTES DE LA ASIGNACIÓN LO SON
                        if (idPair){
                                // posid es {1} o {2}
                                posid = id.substring(id.lastIndexOf("{"),id.lastIndexOf("}") + 1);
                        //System.out.println("assign: posid: "+posid);
                                id = getPairName(id);
                        //System.out.println("assign: id: "+id);
                        }
                        if (exprPair){
                                posexpr = expr.substring(expr.lastIndexOf("{"),expr.lastIndexOf("}") + 1);
                                expr = getPairName(expr);
                        }
                        if (!isDeclared(id))
                        {yyerror("variable "+id+" no declarada. "); }
                        else {
                                // EXPR PUEDE SER :CTE, ID, EXPRPAIR, FUN_INVOC, O UN TERCETO
                                String lexemExpr = "";
                                String subtypeT = "";
                                if (isTerceto(expr)) {
                                        subtypeT = chkAndGetType(expr);
                                        lexemExpr = expr;}   // y lexem es el terceto (expr)
                                else{
                                        expr = expr.toUpperCase();
                                        if (isCte(expr)){      // si es cte, la misma es como se busca en la tabla de simbolos
                                        //System.out.println(expr+" es cte");
                                                lexemExpr = expr;
                                                subtypeT = chkAndGetType(expr);
                                        } else if (isDeclared(expr)){ 
                                                        lexemExpr = getDeclared(expr);
                                                        subtypeT = chkAndGetType(expr+posexpr); // si no era pair, posexpr es vacio
                                                }
                                                else {yyerror(""+expr+" no declarada. ");}

                                        }
                                
                                String lexemID = getDeclared(id);
                                String subtypeID = chkAndGetType(id+posid);      // SI ES PAIR, DEVUELVE TIPO PRIMITIVO! :D
                                //System.out.println("subtypeID: "+subtypeID);
                                //System.out.println("subtypeT: "+subtypeT);
                                if (idPair){lexemID = lexemID+posid;}
                                if (exprPair){lexemExpr = lexemExpr+posexpr;}
                                if (subtypeT.equals(subtypeID)){       
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,subtypeID);
                                }
                                // TODO: FALTA DEVOLVER EN $$ EL ID DEL TERCETO (NECESARIO PARA ESTRUCTURAS DE CONTROL..)
                                else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){    
                                        Terceto.addTercetoT("utos",lexemExpr,null,"SINGLE");
                                        return Terceto.addTercetoT(":=",lexemID,Terceto.getLast(),"SINGLE");
                                }// agregar otro else por si uno es uinteger y el otro hexa
                                else if (subtypeID.equals("UINTEGER") && subtypeT.equals("HEXA")){
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,"UINTEGER");
                                }
                                else if (subtypeID.equals("HEXA") && subtypeT.equals("UINTEGER")){
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,"HEXA");
                                }
                                else if (!subtypeID.equals("") && !subtypeT.equals("")) {       // para que no de error de tipos incompatibles si enrealidad era otro error antes
                                        //System.out.println("subtypeID:"+subtypeID+" subtypeT:"+subtypeT);
                                        yyerror("tipos incompatibles en asignacion. "); 
                                }

                        }
                        return Terceto.getLast();
                }

        /*public String getType(String lexema){   // se le pasa sin scope
                // puede ser: variable, funcion, cte, expr_pair, terceto
                if (isTerceto(lexema)){return "terceto"}
                if (isCte(lexema)){return AnalizadorLexico.t_simbolos.get_subtype(lexema);}

        }*/


        public static String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR SI ES ID O FUNCION.  SI ES EXPR_PAIR, SE ASUME LLEGA SIN LA POSICION (SIN {})
                String scopeaux = new String(actualScope);
                //AnalizadorLexico.t_simbolos.display();
                /*if (id.substring(0,1).equals("S") || id.substring(0,1).equals("U") || id.substring(0,1).equals("V") || id.substring(0,1).equals("W")){ // si es embebida
                        return id + ":MAIN";
                } */
                 
                if (isDeclared(id)){

                       //System.out.println("getDeclared: id: "+id);
                        if (isDeclaredLocal(id)) {return id+":"+actualScope;}
                        else {
                                do {
                                        scopeaux = (scopeaux.equals("MAIN")) ? "MAIN" : scopeaux.substring(0,scopeaux.lastIndexOf(":"));
                                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null)     
                                                return id+":"+scopeaux;;
                                        //System.out.println("EL SCOPEAUX: "+scopeaux);
                                } while ((!scopeaux.equals("MAIN")));
                                return null;
                        }
                } else {/*System.out.println("NO ESTA DECLARADA");*/return null;}

        }

        public static Boolean isCharch(String id){     //charhc comienza  y termina con []
                return (id.charAt(0) == '[' && id.charAt(id.length()-1) == ']');
        }

        public static Boolean isPair(String id){
                return (!(AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("UINTEGER") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("HEXA") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("SINGLE"))) 
        }

        public static Boolean isPairAccess(String id){
                return isPairAccess(id,"");
        }

        public static Boolean isPairAccess (String id, String op){
                // un pair tiene la posicion de acceso entre {}; ej: pairsito{1}
                if (!isCte(id)){
                        if (AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)) != null) {
                                        if (!(AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("UINTEGER") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("HEXA") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("SINGLE"))) {
                                                if (!(id.charAt(id.length()-1) == '}') && !op.equals("assign")){
                                                        System.out.println("op: "+op);
                                                        yyerror("Se intento acceder a un pair sin especificar la posicion de acceso: "+id);
                                                }
                                        }
                        }
                }
                return (id.charAt(id.length()-1) == '}');
        }
        public static String scopeToFunction(String f) {
                // devuelve una funcion dado el scope.
                // el scope esta en formato MAIN:FUN1:FUN2:FUN3:FUN4
                // y la funcion a devolver seria FUN4:MAIN:FUN1:FUN2:FUN3
                String[] parts = f.split(":");
                StringBuilder result = new StringBuilder(parts[parts.length - 1]);
                for (int i = 0; i < parts.length - 1; i++) {
                    result.append(":").append(parts[i]);
                }
                return result.toString();
            }

        public static String extractNumber(String terceto) {
        if (terceto.startsWith("<") && terceto.endsWith(">")) {
            return terceto.substring(1, terceto.length() - 1);
        } else {
            return terceto; 
        }
    }
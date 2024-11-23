package CuartaEtapa;

import PrimeraEtapa.*;
import SegundaEtapa.*;
import TercerEtapa.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.text.html.HTMLDocument.Iterator;

//import javax.jws.soap.SOAPBinding.Use;

// GENERANDO EL .CODE TODAVIA PUEDO TENER TODA LA TS. LUEGO DE GENERAR .DATA AHI NOSE USA MAS
// tonces las aux las voy agregando a las TS y ahi hay logica q ya esta contemplada! :D
public class AsmGenerator {

    // En este punto ya no deberían haber errores sintácticos/semánticos.
    // Atributo FILE: el archivo en código assembler que se genera a partir del programa indicado por el usuario

    // Acá ya no existen errores
    private static String destPath;  // Guarda con el path, hay que ver si funciona según donde estemos parados
    private static final String main = "start";

    
    private static FileManager header;              // 1. encabezado: metadatos
    private static FileManager punto_data;          // 2. declaracion de datos
    private static FileManager punto_code;          // 3. sección de código
    
    private static FileManager assembler;           // archivo final: ensamble de las partes anteriores
    
    // Manejo de funciones
    private static Stack<FunFile> func_nesting = new Stack<>();
    private static ArrayList<FunFile> func_files = new ArrayList<>();
    public static class FunFile {
        FileManager ff_manager;
        String fun_name;
        Boolean fun_is_closed = false;
        FunFile(String name) {
            this.fun_name = name;
            this.fun_is_closed = false;
            this.ff_manager = new FileManager(destPath+fun_name);
        }
        public void appendLine(String line) {this.ff_manager.appendLine(line);}
        public void close() {
            // agrega la sentencia return a su archivo
            appendCode("ret",this.ff_manager);
            this.fun_is_closed = true;
        }
    }
            
    private static int contador_t = 0;
    private static ArrayList<Integer> tercetos_b = new ArrayList<>();
    private static String next_jump_type = "";

    private static class AsmData {
        
        String var_name,var_type,var_value;
        // Distinguir entre inicializadas y no inicializadas según var_value ? null.
        AsmData(String var_name, String var_type, String var_value) {
            this.var_name = var_name;
            this.var_type = var_type; 
            this.var_value = var_value; 
        }

        String getInstruction() { return var_name + " " + var_type + " " + var_value; }
        // metodo que devuelva un string con la instrucción literal: ejemplo, aux_2 DW 5.
    }

    // Inicialización de los archivos intermedios: se crea cada "componente" del archivo final por separado
    public static void init(String program_name) {
        
        // HEADER
        header = new FileManager(destPath+"header");
        header.appendLine(".386                  ; Enable 32-bit instructions");
        header.appendLine(".MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems");
        header.appendLine(".STACK 100h           ; Define stack size;");
        header.appendLine("option casemap :none");
        header.appendLine("include\\masm32\\include\\masm32rt.inc");
        header.appendLine("includelib\\masm32\\lib\\kernel32.lib");
        header.appendLine("includelib\\masm32\\lib\\masm32.lib");
        header.appendLine("dll_dllcrt0 PROTO C");
        header.appendLine("printf PROTO C : VARARG");

        // .DATA
        punto_data = new FileManager(destPath+"punto_data");
        punto_data.appendLine(".data");
        volcarTablaDeSimbolos();
        punto_data.appendLine("__new_line__ DB 13, 10, 0 ; CRLF");
        punto_data.appendLine("errorOverflowMul db \"ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits\", 0");
        punto_data.appendLine("errorOverflowSub db \"ERROR: Overflow detectado! Una resta de enteros da negativo\", 0");
        punto_data.appendLine("errorRecursiveAttempt db \"ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.\", 0");
        appendData(new AsmData("chk_rec", "BYTE", "0"));

        // .CODE
        punto_code = new FileManager(destPath+"punto_code");
        punto_code.appendLine(".code");
        punto_code.appendLine("OverflowMul:");
        punto_code.appendLine("invoke StdOut, addr errorOverflowMul");
        punto_code.appendLine("invoke ExitProcess, 1   ; tiene que terminar con la ejecucion");
        punto_code.appendLine("OverflowSub:");
        punto_code.appendLine("invoke StdOut, addr errorOverflowSub");
        punto_code.appendLine("invoke ExitProcess, 1   ; tiene que terminar con la ejecucion");
        punto_code.appendLine("RecursiveAttempt:");
        punto_code.appendLine("invoke StdOut, addr errorRecursiveAttempt");
        punto_code.appendLine("invoke ExitProcess, 1   ; tiene que terminar con la ejecucion");

        // Elemento 0: programa main
        FunFile main = new FunFile(AsmGenerator.main);
        main.ff_manager.appendLine(main.fun_name+":");    
        main.ff_manager.appendLine("");
        func_files.add(main);   // primer elemento
        
        // Borra el assembler anterior (si existe)
        File file = new File(destPath+program_name+".asm");
        if (file.exists()) file.delete();

        // Crea el nuevo assembler
        assembler = new FileManager(destPath+program_name+".asm",false);  // <nombre del programa>.asm

    }

    // Generación del archivo final a partir de los intermedios
    public static void finish() {
        
        try {   // Ensamblado ordenado de archivos intermedios
            
            FunFile main = func_files.remove(0);
            punto_code.appendFiles(func_files);
            main.appendLine("");
            main.appendLine("end "+main.fun_name);
            punto_code.appendFile(main.ff_manager);

            assembler.appendFile(header);           
            assembler.appendFile(punto_data);       
            assembler.appendFile(punto_code);       
            System.out.println("Existen "+func_files.size()+" funciones en el programa (sin contar main).");

        } catch (IOException e) {}
        
        // Borrado de archivos intermedios
        FileManager.delete_all();

    }

    private static String mapIDSubtypeToVarType(String subtype) {
        // subtipos: uinteger/hexa : DW, single : DD, "64B" : DQ
        switch (subtype) {
            case "64B" : return "DQ";       // 64 bits
            case "SINGLE" : return "REAL4";    // 32 bits
            default : return "DW";          // 16 bits
        }
    }

        
                // Hay que arreglar este método
        private String mapSingleToFloat(String single) {    //cambia la s por una e
            
            return single.replace("s","e");
        }
        
    // Queda implementar esto al archivo de datos, junto con otras cosas (auxiliares de tercetos, por ej)
    private static void volcarTablaDeSimbolos() {

        // Solo se vuelcan IDs : declaración con su subtipo, sin valores.
        Simbolo simbolo;
        for (String lexema : AnalizadorLexico.t_simbolos.getAllEntries()) {
            simbolo = AnalizadorLexico.t_simbolos.get_entry(lexema);
            //System.out.println(lexema);
            if (simbolo.getTipo().equals("ID")) {
                if (!simbolo.getUse().equals("FUN_NAME")){
                    // Caso instancia de tipo pair definido por el usuario
                    if (!(simbolo.getSubtipo().equals("UINTEGER") || simbolo.getSubtipo().equals("HEXA") || simbolo.getSubtipo().equals("SINGLE"))) {
                        appendData(new AsmData("_"+lexema.replace(":","@")+"_1",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                        appendData(new AsmData("_"+lexema.replace(":","@")+"_2",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                    }
                    // Cualquier otra variable se almacena de manera literal
                    else if (!simbolo.getUse().equals("TYPE_NAME") && !simbolo.getUse().equals("PARAM_NAME")) {   // los tipos definidos, ni los parametros de funciones, son variables en el assembler.
                        appendData(new AsmData(lexema.replace(":","@"),mapIDSubtypeToVarType(simbolo.getSubtipo()),"?"));
                    }
                        // pairsito1:main                 ID    UINTEGER    TYPE_NAME 
                        // pair_de_pairsito:main:fun1     ID    PAIRSITO1  var_name   
                } else {
                    if (simbolo.getSubtipo().equals("SINGLE")){
                        appendData(new AsmData("@"+lexema.replace(":", "@"), "DD", "?")); // en esta aux se guarda siempre el resultado de la funcion
                    } else {appendData(new AsmData("@"+lexema.replace(":", "@"), "DW", "?"));}
                }   
            }
        }
        
    }

    private static void appendData(AsmData data) {punto_data.appendLine(data.getInstruction());}

    private static void appendCode(String instruction, FileManager code_file)  {
        code_file.appendLine(instruction);
    }

    private static void appendCode(String instruction) {
        if (func_nesting.isEmpty())
            appendCode(instruction,func_files.get(0).ff_manager);      // En el elemento cero se guarda el main
        else
            appendCode(instruction,func_nesting.peek().ff_manager);
    }
        
        
    private static String ifHexaTrans(String hexValue) { // pasa de hexa 0x123 a hexa para assembler
        // EJEMPLO: 0x12 : 012h
        if (hexValue.startsWith("0x")) {
            hexValue = hexValue.substring(2);
            return 0 + hexValue + "h";
        } return hexValue;
    }

    private static String getOperador(String operador, String subtipo) { //subtipo del terceto
        // devuelve el operador en el formato correcto para el assembler
            // Si operador es terceto:  aux_[op_id_terceto]
            // Si operador es ID:       referencia a la variable (el operador mismo)
            // Si operador es CTE:      literal (inmediato, el operador mismo)
        Simbolo ref_op = AnalizadorLexico.t_simbolos.get_entry(operador.replace("@",":"));   // funciona porque a esta etapa llega el lexema con scope incluido
        if (ref_op != null) {                       // Es un ID  (cte no esta en TS porque hubo limpieza)
            System.out.println(operador+" es un ID o algo de la T. de S.");
            return operador;
        } else if (Parser.isTerceto(operador)) {    // Es un terceto (de expr, de funcion)
            if (Terceto.getOperacion(operador).equals("CALL_FUN")) {    // si es la invocacion a una funcion uso la variable que guarda el resultado de la misma
                return ("@"+Terceto.getOp1(operador).replace(":", "@"));

            } else {
                System.out.println(operador+" es un terceto");
                return "auxt_"+operador.substring(1,operador.length()-1);  // Ejemplo: terceto <2> : auxt_2
            }
        } else {
            if(operador.startsWith("@")) // Es una variable de resultado de función (solo en assigns antes del ret pasa esto)
                {return operador;}
            // Es una CONSTANTE literal
            // hacer el pasaje o traduccion en caso de single 
            // los tipo float no se pueden usar como inmediatos
            //System.out.println("cte: "+operador+": El subtipo es "+subtipo);
            if (Parser.isPair(operador)) {  //pairsito{1}
                return ("_"+operador.replace("{","_").replace("}",""));
            } else if (subtipo.equals("SINGLE")){
                // _cte1 real4 1.2     -1.2s-8 [0-9][a-z]'.''+/-' : ctes__1__2s_8  | ctes_152__21s3 | '+', '-':'_', '.':'__'
                //agregar a .data  varfloat real4 mapSingleToFloat(operador);
                appendData(new AsmData("aux_float_"+contador_t, "REAL4", operador.replace("s", "e")));
                System.out.println("SE AGREGO CTE FLOAT: "+"aux_float_"+contador_t+" REAL4 "+operador.replace("S", "e"));
                return ("aux_float_"+contador_t);} //a todo single devuelve varfloat ?
            else if (subtipo.equals("UINTEGER") || subtipo.equals("HEXA"))   {    // TAMBIEN CONTEMPLAR HEXA : intentarlos tratar igual a ver si funciona
                return operador;
            }
            else System.out.println("PROBLEMA: El subtipo no es ni SINGLE ni UINTEGER.");
        }
        return null;
    }
            
    public static void mapTercetoToAssembler(Terceto terceto) {
    
            final String scopeDelimitatorReplacement = "@";    // Reemplaza a ":"
            String op1 = terceto.op1 != null ? terceto.op1.replace(":",scopeDelimitatorReplacement) : null;
            String op2 = terceto.op2 != null ? terceto.op2.replace(":",scopeDelimitatorReplacement) : null;

            appendCode("");
            //appendCode("; Terceto <"+contador_t+">  "+terceto.toString().substring(8,terceto.toString().length()-1));

            java.util.Iterator<Integer> iterator = tercetos_b.iterator();
            while (iterator.hasNext()) {
                Integer t_bf = iterator.next();
                if (t_bf.equals(contador_t)) {  // Usa equals para comparar objetos
                    iterator.remove();          // Elimina el elemento de forma segura
                    appendCode("labelt_" + contador_t + ":");
                }
            }

            switch (terceto.operacion) {
                
                case "LABEL_TAG" :  
                    appendCode(op1+":");
                    break;

                case "JUMP_TAG" : 
                    appendCode("JMP "+op1);   // JMP es salto incondicional.
                    break;

                case "INIC_FUN" : {  
                    FunFile new_fun = new FunFile(op1);
                    func_nesting.push(new_fun);
                    func_files.add(new_fun);
                    // el terceto guarda: ¿nos sirve de algo esta data?
                    // operador:    LABEL_FUN
                    // op1:         nombre de la función (con scope y reemplazos)
                    // op2:         nombre del parámetro formal (con scope y reemplazos)
                    // subtipo:     tipo del parametro formal
                    // SI EL PARAMETRO ES ENTERO:
                    appendCode(op1+" PROC "+op2+":WORD");
                    //appendCode("XOR EAX, EAX");     // setea el registro EAX en todos 0s
                    //appendCode("MOV EAX, [ESP+4]"); // carga el parámetro en EAX (32 bits son 4 bytes, funciona para UINTEGER y para SINGLE)
                    // appendCode("POP ESP");          // quita el parámetro del tope de la pila?
                    // LO ULTIMO TIENE Q SER UN  'RET 0' POR DEFAULT
                }
                    break;

                case "END_FUN" : {             
                    func_nesting.peek().close();     // agrega el return y la marca como cerrada. (el ret es por las dudas, y antes del ret ponerle un 0 a la aux)
                    appendCode(op1+" ENDP");
                    func_nesting.pop();

        }
            break;

        case "CALL_FUN" :       // tiene que saber el tipo del retorno, porque luego lo va a asignar al del lado izq
            // TERCETO CALL FUN : ("CALL_FUN", id_funcion(conscope), idExpr (parametro real -> id,cte,expr,terceto), tipo_retorno)
                appendCode("MOV chk_rec, "+isRecursive(op1));   // Si devuelve 1: el llamado es recursivo
                appendCode("CMP chk_rec, 0"); // solo para setear flag ZF 
                appendCode("JNZ RecursiveAttempt"); // llama al error
                appendCode("invoke "+op1+", "+getOperador(op2, terceto.subtipo));  
                //appendData(new AsmData("auxt_"+contador_t,"DWORD","?"));    // esta aux sacarla creo, usar 1 por funcion
                // usamos una unica var de resultado para cada funcion.
                //appendData(new AsmData("@"+op1, op1, op2));   no hacemos nada aca, despues se usa la variable cuando se use

                //sobre recursion:
                //opcion 1: en assembler chequear dir de label actual con la que me llamo y ahi fijarse si es recursion o no, PERO NO LO PUEDO HACER ANDAR
                //opcion 2: antes de cada CALL agregar una instr que chequee que no sea la misma tag que la actual (chequeo de dirs?)
                // OPCION 3 ASQUEROSA PERO VALIDA (TP 2021): agregar para cada funcion, una varialbe como flag, esta flag esta en 0 o en 1 si estás en esa funcion
                // estas var que sean "_"+nombre_funcion y se setean en 1 al entrar y en 0 al salir
                //  sea cual sea, chequearia recursion directa, no indirecta, pero piden eso (osea si fun2 llama a fun1 que llama fun2.. no pasa nada)
                // CHEQUEAR NO SE ESTE LLAMANDO A LA MISMA DONDE ESTA (EN ASSEMBLER SE CHEQUEA, ACA NO. CREAR LAS INSTR)
                // funcion en la q estoy: ¿???????????
                // quizás reservar un registro para esto? EDX? o la pila directamente
                // dos registros/variables: el retorno y el parámetro.
                // como se maneja el tema del parámetro? habría que mover lo que hay en op2 (nombre de variable) al parámetro
                // podriamos incluir la aux donde quedara el resultado, del tipo del terceto (que es del tipo de retorno de la funcion)
                // entonces despues es mas facil usar el resutlado de lo que deuvelve en el resto de tercetos
    
                // cada CALL es un terceto unico. la funcion sabe donde volver con 'ret'
                // pero el valor? la funcion siempre lo guarda en el mismo lugar. si lo guardamos en una varaible (_nombrefuncion), la misma sera sobreescrita (no hay problema con eso)
                // cada vez se llame. Pero en cada call, podemos poner ese valor de la variable _nombrefuncion en la aux de este CALL
                // a menos podamos pasar por parametro alguna variable.. o el resultado quede en la pila y luego de cada call pasamos lo de la pila a la var del terceto
                break;
    
            case "RET" : { // tiene que saber el tipo del retorno
                // el terceto guarda: ¿nos sirve de algo esta data?
                // operador:    RET
                // op1:         expr
                // op2:         null
                // subtipo:     tipo de expr a retornar = tipo de retorno de funcion
                //appendCode("MOV EAX, "+getOperador(op1, terceto.subtipo));   //NO, MOVER A AUX. dijo marcela no usemos regs
                // seria hacer primero una asginacion y depsues poenr ret. se podria usar el terceto :=
                //appendCode("MOV "+"@"+op2+", "+getOperador(op1, terceto.subtipo));   
                appendCode("ret");
            }
            break;
    
            case "BI" : {       // podria usarse label y jumps?
                    // salto incondicional a op1 (terceto)
                    String t_bi = op1.substring(1,op1.length()-1);
                    tercetos_b.add(Integer.parseInt(t_bi));
                    appendCode("JMP labelt_"+op1.substring(1,op1.length()-1));   // op1 es terceto. pasarlo a label
                    next_jump_type = "[!]";
            }
            break;
    
            case "BF" :{     
                String t_bf = op2.substring(1,op2.length()-1);
                tercetos_b.add(Integer.parseInt(t_bf));
                appendCode(next_jump_type+" labelt_"+t_bf);
                next_jump_type = "[!]";
                    // podria usarse label y jumps ?
                        // si op1(terceto) NO se cumple (es 0) salta a la etiqueta op2
            }
            break;
    
            case "OUTF" : {
                //System.out.println("SWITCH CASE MATCH: OUTF");
                //op2 null, op1: ID, cte, terceto (resultado de arimeticas, de funcion) O CHARCH!
                appendCode(    "INVOKE printf, addr __new_line__");
                if (terceto.subtipo.equals("SINGLE")) { // sea id,cte,terceto
                    // CONVERTIR A 64 BITS Y LLAMAR A PRINTF
                    // la variable de 64 bits q nombre le ponemos? para asegurar no se repita nunca
                    // opcion: auxt_contador_t_64
                    appendData(new AsmData("auxt_"+contador_t+"_64","64B","?"));
                    //pasar el valor de auxt__contador_t a auxt_contador_t_64
                    appendCode("fld "+getOperador(op1, terceto.subtipo));
                    appendCode("fst "+"auxt_"+contador_t+"_64");
                    appendCode("invoke printf, cfm$(\"%.20Lf\\n\") auxt_"+contador_t+"_64");
                } else if (terceto.subtipo.equals("UINTEGER")|| terceto.subtipo.equals("HEXA")) {   // HEXA COMO DEBERIA MOSTRARLA?
                    appendCode("invoke printf, cfm$(\"%u\\n\"), "+getOperador(op1, terceto.subtipo));
                }  else if (Parser.isCharch(op1)) {
                    // varcte = crear cte ¿Q CONVENCION USAMOS?
                    appendData(new AsmData("aux_charch_"+contador_t," DB \""+ op1.substring(1, op1.length()-1) +" \", ","0"));
                    //errorOverflowMsg db "ERROR: Overflow detectado!", 0
                    appendCode("invoke printf, addr "+"aux_charch_"+contador_t);   // ? FUNCA? O PASO A VARIABLE DB Y LLAMO STDOUT
                }
            }   break;
    
            case "SUMA" : { // QUE ONDA LAS CTES NEGATIVAS!?
                System.out.println("SWITCH CASE MATCH: SUMA");
                // Declarar la variable "auxt_[id_terceto]".
                appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                if (terceto.subtipo.equals("SINGLE")){ 
                    appendCode("fld "+getOperador(op1, terceto.subtipo));
                    appendCode("fadd "+getOperador(op2, terceto.subtipo));
                    appendCode("fstp "+"auxt_"+contador_t);
                } else if (AnalizadorLexico.t_simbolos.get_entry(op2) != null && !Parser.isTerceto(op2)) {  // si es cte
                    appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                    appendCode("ADD auxt_"+contador_t+","+getOperador(op2, terceto.subtipo)); // ADD auxt_[id_terceto], final_op2
                } else {    // si no es cte, es decir si es var o terceto
                    appendCode("MOV AX, "+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                    appendCode("ADD AX, "+getOperador(op2, terceto.subtipo)); // ADD auxt_[id_terceto], final_op2
                    appendCode("MOV auxt_"+contador_t+", AX");   // MOV final_op1, auxt_[id_terceto]
                }
            }
            break;
    
            case "RESTA" : {
                /* Resultados negativos en restas de enteros sin signo:
    El código Assembler deberá controlar el resultado de la operación indicada. Este control se aplicará a
    operaciones entre enteros sin signo. En caso que una resta entre datos de este tipo arroje un resultado
    negativo, deberá emitir un mensaje de error y terminar */
                // Análogo a la SUMA
                System.out.println("SWITCH CASE MATCH: RESTA");
                appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                if (terceto.subtipo.equals("SINGLE")){
                    appendCode("fld "+getOperador(op1, terceto.subtipo));
                    appendCode("fsub "+getOperador(op2, terceto.subtipo));
                    appendCode("fstp "+"auxt_"+contador_t);
                } else {    // es entero
                appendCode("MOV AX, "+getOperador(op1, terceto.subtipo));
                appendCode("MOV BX, "+getOperador(op2, terceto.subtipo)); 
                appendCode("CMP AX, BX"); 
                appendCode("jb OverflowSub"); 
                // si no salto, la resta es segura.
                appendCode("SUB AX, BX");
                appendCode("MOV auxt_"+contador_t+", AX");   
    
                }
            }
            break;  
    
            case "MUL" : {
                /*Overflow en productos de enteros:
    EL CODIGO ASSEMBLER deberá controlar el resultado de la operación indicada, para los tipos de datos
    enteros asignados al grupo. Si el mismo excede el rango del tipo del resultado, deberá emitir un
    mensaje de error y terminar */
                System.out.println("SWITCH CASE MATCH: MUL");// Análogo a la SUMA
                appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                if (terceto.subtipo.equals("SINGLE")){
                    appendCode("fld "+getOperador(op1, terceto.subtipo));
                    appendCode("fmul "+getOperador(op2, terceto.subtipo));
                    appendCode("fstp "+"auxt_"+contador_t);
                } else {    // es uinteger o hexa CONTEMPLAR OVERLFLOW MUL -> corregir: los enteros son de 16 bits
                    
                appendCode("MOV AX ,"+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                appendCode("MOV CX ,"+getOperador(op2, terceto.subtipo));
                appendCode("MUL CX"); // MUL auxt_[id_terceto], final_op2
                appendCode("CMP DX, 0");   // SI DA 0 ES PORQ NO HUBO OVERFLOW
                appendCode("JNE OverflowMul");
                appendCode("MOV "+"auxt_"+contador_t+" ,AX");      
                }
            }
            break;
    
            case "DIV" : { // QUE ONDA LAS CTES NEGATIVAS!?
                System.out.println("SWITCH CASE MATCH: DIV");
                // Declarar la variable "auxt_[id_terceto]".
                appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                if (terceto.subtipo.equals("SINGLE")){ 
                    appendCode("fld "+getOperador(op1, terceto.subtipo));
                    appendCode("fdiv "+getOperador(op2, terceto.subtipo));
                    appendCode("fstp "+"auxt_"+contador_t);
                } else{ 
                    appendCode("MOV AX, "+getOperador(op1, terceto.subtipo));   // NUMERADOR
                    appendCode("MOV CX, "+getOperador(op2, terceto.subtipo));   // NUMERADOR
                    appendCode("DIV CX");       //divido
                    appendCode("MOV "+"auxt_"+contador_t+" ,AX");   // guardo. el resto (No nos interesa) queda en DX
                }
                /*  else if (AnalizadorLexico.t_simbolos.get_entry(op2) != null && !Parser.isTerceto(op2)) {  // si es cte
                    
                    appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                    appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                    appendCode("DIV auxt_"+contador_t+","+getOperador(op2, terceto.subtipo)); // DIV auxt_[id_terceto], final_op2
                } else {    // si no es cte, es decir si es var o terceto
                    appendCode("MOV AX, "+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                    appendCode("DIV AX, "+getOperador(op2, terceto.subtipo)); // DIV auxt_[id_terceto], final_op2
                    appendCode("MOV auxt_"+contador_t+", AX");   // MOV final_op1, auxt_[id_terceto]
                }*/
            }
            break;
    
            case ":=" : {
                //System.out.println("op1: "+op1+" op2: "+op2);
                // a la izq: siempre ID (o acceso a pair), a la der: ID, CTE, REF_PAIR, INV_FUN, terceto
                if ( AnalizadorLexico.t_simbolos.get_entry(op2) != null && !Parser.isTerceto(op2)) {    // si es cte
                    if (!terceto.subtipo.equals("SINGLE")){
                        appendCode("MOV "+getOperador(op1, terceto.subtipo)+","+getOperador(op2, terceto.subtipo)); //op2 es inmediato
                    } else {
                        // si es float, creo la variable (no puedo usar inmediatos float)
                        System.out.println("singletooon");
                        appendCode("fld " + getOperador(op2, "SINGLE"));
                        appendCode("fstp "+getOperador(op1, terceto.subtipo));
                    }
                } else {// tengo q pasar primero a un reg
                    if (terceto.subtipo.equals("SINGLE")) {
                        appendCode("fld "+getOperador(op2, terceto.subtipo));
                        appendCode("fstp "+getOperador(op1, terceto.subtipo));
                    } else {    //uinteger o hexa
                        appendCode("MOV AX, "+getOperador(op2, terceto.subtipo));
                        appendCode("MOV "+getOperador(op1, terceto.subtipo)+", AX");
                    }
                }
            }
            break;  
    
            case "utos" : {    // minuscula?
                
                op1 = ifHexaTrans(op1);
                appendData(new AsmData("auxt_"+contador_t,"SINGLE","?"));
                // sea uinteger o hexa no cambia nada lo q devuelve
                appendCode("fild "+getOperador(op1,"UINTEGER"));
                appendCode("fstp "+"auxt_"+contador_t);
                // PERO ESTO PASA DE 16 BITS A 32. PASA DE DW A REAL4 PERO NO ESTÁ EN FORMATO SINGLE..
        // PROBAR SI CON SOLO ESO ANDA. SINO, MIRAR FILMINAS.
            }
            break;

            case "<" : {
                appendCode("MOV BX, "+getOperador(op1,terceto.subtipo)+"                  ; Mueve op1 a reg. B");
                appendCode("CMP BX, "+getOperador(op2,terceto.subtipo)+"                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. ");  
                next_jump_type = "JB";
            }
            break;

            case ">": {
                appendCode("MOV BX, "+getOperador(op1,terceto.subtipo)+"                  ; Mueve op1 a reg. B");
                appendCode("CMP BX, "+getOperador(op2,terceto.subtipo)+"                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. ");  
                next_jump_type = "JG";
            }
                
                break;

            case "<=" : {
                appendCode("MOV BX, "+getOperador(op1,terceto.subtipo)+"                  ; Mueve op1 a reg. B");
                appendCode("CMP BX, "+getOperador(op2,terceto.subtipo)+"                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. ");  
                next_jump_type = "JBE";
            }
            break;

            case ">=": {
                appendCode("MOV BX, "+getOperador(op1,terceto.subtipo)+"                  ; Mueve op1 a reg. B");
                appendCode("CMP BX, "+getOperador(op2,terceto.subtipo)+"                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. ");  
                next_jump_type = "JGE";
            }
                
                break;

            case "=": {
                appendCode("MOV BX, "+getOperador(op1,terceto.subtipo)+"                  ; Mueve op1 a reg. B");
                appendCode("CMP BX, "+getOperador(op2,terceto.subtipo)+"                 ; Compara op1 con op2. R = op1 - op2. ZF = R == 0 ? 1 : 0. ");  
                next_jump_type = "JE";

            }
                
                break;

            case "!=": {
                appendCode("MOV BX, op1");               
                appendCode("CMP BX, op2");            
                next_jump_type = "JNE";  
            }
                
                break;

            // FALTAN TERCETOS NEQ LEQ MEQ

            default : System.out.println("PROBLEMA: El terceto no fue implementado.");
                    }
                        }
                                
        private static int isRecursive(String fun_to_call) {
            for (FunFile ff : func_nesting)
                if (ff.fun_name.equals(fun_to_call)) return 1;
            return 0;
        }
    /* 
    * 
    private static String mapSubtypeToSizeInBytes(String op2) {
        switch (mapIDSubtypeToVarType(op2)) {
            case "DW":  return ""+16/8;     // 4  B
            case "REAL4":  return ""+32/8;     // 8  B
            case "DQ":  return ""+64/8;     // 16 B
        }
    }
    */
    
    public static void generate(String executablePath, String program) {
        System.out.println("\n\n"+Terceto.TerList.size()+"\n");
        destPath = executablePath;
        init(program);
        for (Terceto terceto : Terceto.TerList) {
            System.out.println("\n"+terceto.toString());
            mapTercetoToAssembler(terceto);
            contador_t++;   // siempre coincidira con el numero del terceto
        }
        finish();
    }
}

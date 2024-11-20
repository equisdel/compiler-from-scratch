package CuartaEtapa;

import PrimeraEtapa.*;
import SegundaEtapa.*;
import TercerEtapa.*;
import java.io.IOException;

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
    private static FileManager punto_code_fun;      // 3. seccion de funciones
    private static FileManager punto_code_body;     // 4. codigo principal

    private static FileManager assembler;           // archivo final: ensamble de las partes anteriores


    private static int contador_t = 0;

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
        
        header = new FileManager(destPath+"header");
        header.appendLine(".386                  ; Enable 32-bit instructions");
        header.appendLine(".MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems");
        header.appendLine(".STACK 100h           ; Define stack size;");
        header.appendLine("option casemap :none");
        header.appendLine("include \\masm32\\include\\masm32rt.inc");
        header.appendLine("includelib\\masm32\\lib\\kernel32.lib");
        header.appendLine("includelib\\masm32\\lib\\masm32.lib");
        header.appendLine("dll_dllcrt0 PROTO C");
        header.appendLine("printf PROTO C : VARARG");

        punto_data = new FileManager(destPath+"punto_data");
        punto_data.appendLine(".data");
        volcarTablaDeSimbolos();
        punto_data.appendLine("__new_line__ DB 13, 10, 0 ; CRLF");
        punto_data.appendLine("errorOverflowMsg db \"ERROR: Overflow detectado!\", 0");


        punto_code_fun = new FileManager(destPath+"punto_code_fun");
        punto_code_fun.appendLine(".code");
        punto_code_fun.appendLine("OverflowHandler:");
        punto_code_fun.appendLine("invoke StdOut, addr errorOverflowMsg");
        punto_code_fun.appendLine("invoke ExitProcess, 1   ; tiene que terminar con la ejecucion");
    
        
        punto_code_body = new FileManager(destPath+"punto_code_body");
        punto_code_body.appendLine(main+":");

        assembler = new FileManager(destPath+program_name+".asm");  // <nombre del programa>.asm
    }

    // Generación del archivo final a partir de los intermedios
    public static void finish() {
      
        try {   // Ensamblado ordenado de archivos intermedios
            assembler.appendFile(header);           assembler.appendLine("");
            assembler.appendFile(punto_data);       assembler.appendLine("");
            assembler.appendFile(punto_code_fun);   assembler.appendLine("");
            assembler.appendFile(punto_code_body);  assembler.appendLine("");
            assembler.appendLine("end "+main);
        } catch (IOException e) {}
        
        // Borrado de archivos intermedios
        header.delete();
        punto_data.delete(); 
        punto_code_fun.delete();
        punto_code_body.delete();

    }

    private static String mapIDSubtypeToVarType(String subtype) {
        // subtipos: uinteger/hexa : DW, single : DD, "64B" : DQ
        switch (subtype) {
            case "64B" : return "DQ";       // 64 bits
            case "SINGLE" : return "REAL4";    // 32 bits
            default : return "DW";          // 16 bits
        }
    }

        /*     
                // Hay que arreglar este método
        private String mapSingleToFloat(String single) {    // Convención IEEE 754
    
            String signo, exponenteBin, mantisaBin;       // 1, 8 y 23 bits, respectivamente.
            
            // Determinar el signo
    
            switch (single.substring(0,0)) {    // signo del single
                case "+" : {
                    signo = "0"; single = single.substring(1,single.length());
            }
                case "-" : {
                    signo = "1"; single = single.substring(1,single.length());
            }
                default : signo = "0";        // positivo por defecto
            }
        // signo del single
    
            // Separar la base (mantisa) y el exponente
    
            String[] singlePorPartes = single.split("s");
            float base;
            int exponente;
    
            try {
                base = Float.parseFloat(singlePorPartes[0]);    // Puede no tener exponente (ausencia de "s")
                exponente = singlePorPartes.length == 2 ? Integer.parseInt(singlePorPartes[1]) : 0;
            } catch (Exception e) {
                throw new IllegalArgumentException("Formato inválido para el número single: " + single);
            }
    
            // Normalizar la base para la forma 1.mantisa
            int desplazamiento = 0;
            while (base >= 2.0) {  // Ajustar para que la base esté en el rango [1, 2)
                base /= 2.0;
                desplazamiento++;
            }
            while (base < 1.0) {  // Ajustar para que la base esté en el rango [1, 2)
                base *= 2.0;
                desplazamiento--;
            }
    
            int exponenteAjustado = exponente + desplazamiento + 127;  // Sesgo para precisión simple (127)
            if (exponenteAjustado < 0 || exponenteAjustado > 255) {
                throw new IllegalArgumentException("El exponente resultante está fuera del rango permitido por IEEE 754.");
            }
    
            // Convertir el exponente a binario de 8 bits
            exponenteBin = String.format("%8s", Integer.toBinaryString(exponenteAjustado)).replace(' ', '0');
            
            // Calcular la mantisa (23 bits, sin el 1 implícito)
            float fraccion = base - 1.0f;
            StringBuilder mantisa = new StringBuilder();
            for (int i = 0; i < 23; i++) {
                fraccion *= 2;
                if (fraccion >= 1.0) {
                    mantisa.append("1");
                    fraccion -= 1.0;
                } else {
                    mantisa.append("0");
                }
            }
            mantisaBin = mantisa.toString();
    
            String IEEE_754_representation = signo + exponenteBin + mantisaBin;
            if (IEEE_754_representation.length() == 32)
                return IEEE_754_representation;
            else System.out.println("PROBLEMA: la representación IEEE 754 es errónea, no tiene 32 bits.");
        /*
            
            // Conversión a formato IEEE 754
            float ieeeValue = parseSingle(single);
            String ieee754Binary = floatToIEEE754(ieeeValue);
    
            // Mostrar resultados
            System.out.println("Valor decimal: " + ieeeValue);
            System.out.println("Representación IEEE 754: " + ieee754Binary);
        }
    
    
        // Paso 1: Parsear y calcular el valor decimal
        public static float parseSingle(String singleFormat) {
            // Separar la base (mantisa) y el exponente
            String[] parts = singleFormat.split("s");
            float base = Float.parseFloat(parts[0]);
            int exponent = Integer.parseInt(parts[1]);
    
            // Calcular el valor final (base * 2^exponente)
            return (float) (base * Math.pow(2, exponent));
        }
        // Paso 2: Convertir a IEEE 754 en formato binario
        public static String floatToIEEE754(float value) {
            // Convertir a bits
            int bits = Float.floatToIntBits(value);
    
            // Extraer las partes de IEEE 754
            int sign = (bits >> 31) & 1;             // Bit de signo
            int exponent = (bits >> 23) & 0xFF;      // Exponente (8 bits)
            int mantissa = bits & 0x7FFFFF;          // Mantisa (23 bits)
    
            // Formatear en binario
            return String.format("%1d | %8s | %23s",
                    sign,
                    Integer.toBinaryString(exponent).replace(' ', '0'),
                    Integer.toBinaryString(mantissa).replace(' ', '0'));
        }
        0 |  1111111 |   110011001100110011010          2.4s-1  (1.2 en decimal)
        
            return "";
        }
        */
    
    // Queda implementar esto al archivo de datos, junto con otras cosas (auxiliares de tercetos, por ej)
    private static void volcarTablaDeSimbolos() {

        // Solo se vuelcan IDs : declaración con su subtipo, sin valores.
        Simbolo simbolo;
        for (String lexema : AnalizadorLexico.t_simbolos.getAllEntries()) {
            simbolo = AnalizadorLexico.t_simbolos.get_entry(lexema);
            //System.out.println(lexema);
            if (simbolo.getTipo().equals("ID")) {
                // Caso instancia de tipo pair definido por el usuario
                if (!(simbolo.getSubtipo().equals("UINTEGER") || simbolo.getSubtipo().equals("HEXA") || simbolo.getSubtipo().equals("SINGLE"))) {
                    appendData(new AsmData("_"+lexema.replace(":","@")+"_1",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                    appendData(new AsmData("_"+lexema.replace(":","@")+"_2",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                }
                // Cualquier otra variable se almacena de manera literal
                else if (!simbolo.getUse().equals("TYPE_NAME")) {
                    appendData(new AsmData(lexema.replace(":","@"),mapIDSubtypeToVarType(simbolo.getSubtipo()),"?"));
                }
                    // pairsito1:main                 ID    UINTEGER    TYPE_NAME 
                    // pair_de_pairsito:main:fun1     ID    PAIRSITO1  var_name   
            }
        }
        
    }

    private static void appendData(AsmData data)                                {punto_data.appendLine(data.getInstruction());}
    
    private static void appendCode(String instruction, FileManager punto_code)  {punto_code.appendLine(instruction);}
    private static void appendCodeFun(String instruction)                       {appendCode(instruction,punto_code_fun);}
    private static void appendCodeBody(String instruction)                      {appendCode(instruction,punto_code_body);}
        
        
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
            Simbolo ref_op = AnalizadorLexico.t_simbolos.get_entry(operador);   // funciona porque a esta etapa llega el lexema con scope incluido
            if (ref_op != null) {                       // Es un ID  (cte no esta en TS porque hubo limpieza)
                System.out.println(operador+" es un ID o algo de la T. de S.");
                return operador;
            } else if (Parser.isTerceto(operador)) {    // Es un terceto (de expr, de funcion)
                System.out.println(operador+" es un terceto");
                return "auxt_"+operador.substring(1,operador.length()-1);  // Ejemplo: terceto <2> : auxt_2
            } else {                                    // Es una constante literal
                // hacer el pasaje o traduccion en caso de single 
                // los tipo float no se pueden usar como inmediatos
                System.out.println("cte: "+operador+": El subtipo es "+subtipo);
                if (Parser.isPair(operador)) {  //pairsito{1}
                    return ("_"+operador.replace("{","_").replace("}",""));
                } else if (subtipo.equals("SINGLE")){
                    //agregar a .data  varfloat real4 mapSingleToFloat(operador);
                    // _cte1 real4 1.2     -1.2s-8 [0-9][a-z]'.''+/-' : ctes__1__2s_8  | ctes_152__21s3 | '+', '-':'_', '.':'__'
                    return "varfloat";} //a todo single devuelve varfloat ?
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
        
                switch (terceto.operacion) {
                    
                    case "LABEL_TAG" :  
                        appendCode(op1+":",punto_code_body);
                        break;

                    case "JUMP_TAG" : 
                        appendCodeBody("JMP "+op1+":");   // JMP es salto incondicional
                        break;

                    case "LABEL_FUN" : {        // tiene que saber el tipo del parámetro
                        // el terceto guarda: ¿nos sirve de algo esta data?
                        // operador:    LABEL_FUN
                        // op1:         nombre de la función (con scope y reemplazos)
                        // op2:         nombre del parámetro formal (con scope y reemplazos)
                        // subtipo:     tipo del parametro formal
                        appendCodeBody(op1+" PROC");
                        appendCodeBody("XOR EAX, EAX");     // setea el registro EAX en todos 0s
                        appendCodeBody("MOV EAX, [ESP+4]"); // carga el parámetro en EAX (32 bits son 4 bytes, funciona para UINTEGER y para SINGLE)
                        // appendCode("POP ESP");          // quita el parámetro del tope de la pila?
                        // LO ULTIMO TIENE Q SER UN  'RET 0' POR DEFAULT
                    }
                        break;

                    case "END_FUN" : {          // tiene que saber el tipo del retorno?     //PENSAR SI ES NECESARIA O NOR

                        appendCodeFun("ret");  // El retorno se guarda, según el tipo
                        appendCodeFun(op1+" ENDP");
                        // el terceto guarda: ¿nos sirve de algo esta data?
                        // operador:    LABEL_FUN
                        // op1:         nombre de la función (con scope y reemplazos)
                        // op2:         nombre del parámetro (con scope y reemplazos)
                        // subtipo:     tipo del retorno
                    }
                        break;

                    case "CALL_FUN" :       // tiene que saber el tipo del retorno, porque luego lo va a asignar al del lado izq
                        // TERCETO CALL FUN : ("CALL_FUN", id_funcion(conscope), idExpr (parametro real -> id,cte,expr,terceto), tipo_retorno)
                        appendCodeFun("CALL "+op1+":");   // JMP es salto incondicional.
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
                        // pero el valor? la funcion siempre lo guarda en el mismo lugar. si lo guardamos en una varaible (_nombrefuncion), la misma sera sobreescrita
                        // cada vez se llame. Pero en cada call, podemos poner ese valor de la variable _nombrefuncion en la aux de este CALL
                        // a menos podamos pasar por parametro alguna variable.. o el resultado quede en la pila y luego de cada call pasamos lo de la pila a la var del terceto
                        break;

                    case "OUTF" : {
                        System.out.println("SWITCH CASE MATCH: OUTF");
                        //op2 null, op1: ID, cte, terceto (resultado de arimeticas, de funcion) O CHARCH!
                        appendCodeBody(    "INVOKE printf, addr __new_line__");
                        if (terceto.subtipo.equals("SINGLE")) { // sea id,cte,terceto
                            // CONVERTIR A 64 BITS Y LLAMAR A PRINTF
                            // la variable de 64 bits q nombre le ponemos? para asegurar no se repita nunca
                            // opcion: auxt_contador_t_64
                            appendData(new AsmData("auxt_"+contador_t+"_64","64B","?"));
                            //pasar el valor de aux_t_contador_t a auxt_contador_t_64
                            appendCodeBody("fld "+getOperador(op2, terceto.subtipo));
                            appendCodeBody("fst "+"auxt_"+contador_t+"_64");
                            appendCodeBody("invoke printf, cfm$(\"%.20Lf\\n\") auxt_"+contador_t+"_64");
                        } else if (terceto.subtipo.equals("UINTEGER")|| terceto.subtipo.equals("HEXA")) {   // HEXA COMO DEBERIA MOSTRARLA?
                            appendCodeBody("invoke printf, cfm$(\"%u\\n\"), "+getOperador(op1, terceto.subtipo));
                        }  else if (Parser.isCharch(op1)) {
                            // varcte = crear cte ¿Q CONVENCION USAMOS?
                            appendData(new AsmData("aux_charch_"+contador_t," DB \""+ op1.substring(1, op1.length()-1) +" \", ","0"));
                            //errorOverflowMsg db "ERROR: Overflow detectado!", 0
                            appendCodeBody("invoke printf, addr "+"aux_charch_"+contador_t);   // ? FUNCA? O PASO A VARIABLE DB Y LLAMO STDOUT
                        }
                    }   break;
        
                    case "SUMA" : { // TODO QUE ONDA LAS CTES NEGATIVAS!?
                        System.out.println("SWITCH CASE MATCH: SUMA");
                        // Declarar la variable "auxt_[id_terceto]".
                        appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                        if (terceto.subtipo.equals("SINGLE")){
                            appendCodeBody("fld "+getOperador(op1, terceto.subtipo));
                            appendCodeBody("fadd "+getOperador(op2, terceto.subtipo));
                            appendCodeBody("fstp "+"aux_t"+contador_t);
                        } else {
                        appendCodeBody("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCodeBody("ADD auxt_"+contador_t+","+getOperador(op2, terceto.subtipo)); // ADD auxt_[id_terceto], final_op2
                        }
                    }
                    break;

                    case "RESTA" : {
                        // Análogo a la SUMA
                        System.out.println("SWITCH CASE MATCH: RESTA");
                        appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                        if (terceto.subtipo.equals("SINGLE")){
                            appendCodeBody("fld "+getOperador(op1, terceto.subtipo));
                            appendCodeBody("fsub "+getOperador(op2, terceto.subtipo));
                            appendCodeBody("fstp "+"aux_t"+contador_t);
                        } else {
                        appendCodeBody("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCodeBody("SUB auxt_"+contador_t+","+getOperador(op2, terceto.subtipo)); // ADD auxt_[id_terceto], final_op2
                        }
                    }
                    break;  

                    case "MULT" : {
                        /*Overflow en productos de enteros:
EL CODIGO ASSEMBLER deberá controlar el resultado de la operación indicada, para los tipos de datos
enteros asignados al grupo. Si el mismo excede el rango del tipo del resultado, deberá emitir un
mensaje de error y terminar */
                        System.out.println("SWITCH CASE MATCH: MULT");// Análogo a la SUMA
                        appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                        if (terceto.subtipo.equals("SINGLE")){
                            appendCodeBody("fld "+getOperador(op1, terceto.subtipo));
                            appendCodeBody("fmul "+getOperador(op2, terceto.subtipo));
                            appendCodeBody("fstp "+"aux_t"+contador_t);
                        } else {    // es uinteger o hexa CONTEMPLAR OVERLFLOW MUL
                            
                        appendCodeBody("MOV AX "+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCodeBody("MUL "+getOperador(op2, terceto.subtipo)); // MUL auxt_[id_terceto], final_op2
                        appendCodeBody("CMP DX, 0");   // SI DA 0 ES PORQ NO HUBO OVERFLOW
                        appendCodeBody("JNE OverflowHandler");
                        appendCodeBody("MOV "+"aux_t"+contador_t+" EAX");      
                        }
                    }
                    break;

                    case "DIV" : {
                        System.out.println("SWITCH CASE MATCH: DIV");// Análogo a la SUMA
                        appendData(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                        if (terceto.subtipo.equals("SINGLE")){
                            appendCodeBody("fld "+getOperador(op1, terceto.subtipo));
                            appendCodeBody("fdiv "+getOperador(op2, terceto.subtipo));
                            appendCodeBody("fstp "+"aux_t"+contador_t);
                        } else {
                        appendCodeBody("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCodeBody("DIV auxt_"+contador_t+","+getOperador(op2, terceto.subtipo)); // ADD auxt_[id_terceto], final_op2
                            
                        }
                    }
                    break;

                    case ":=" : {
                        System.out.println("SWITCH CASE MATCH: :=");
                        // a la izq: siempre ID (o acceso a pair), a la der: ID, CTE, REF_PAIR, INV_FUN, terceto
                        if ( AnalizadorLexico.t_simbolos.get_entry(op2) != null && !Parser.isTerceto(op2)) {    // si es cte
                            // si no es float
                            appendCodeBody("MOV "+getOperador(op1, terceto.subtipo)+","+getOperador(op2, terceto.subtipo)); //op2 es inmediato
                            // si es float, creo la variable (no puedo usar inmediatos float)
                            appendData(new AsmData("aux_float_"+contador_t, "REAL4",getOperador(op2, terceto.subtipo)));
                            appendCodeBody("fld "+"aux_float_"+contador_t);
                            appendCodeBody("fstp "+getOperador(op1, terceto.subtipo));
                        } else {// tengo q pasar primero a un reg
                            if (terceto.subtipo.equals("SINGLE")) {
                                appendCodeBody("fld "+getOperador(op2, terceto.subtipo));
                                appendCodeBody("fstp "+getOperador(op1, terceto.subtipo));
                            } else {    //uinteger o hexa
                                appendCodeBody("MOV AX, "+getOperador(op2, terceto.subtipo));
                                appendCodeBody("MOV "+getOperador(op1, terceto.subtipo)+", AX");
                            }
                        }
                    }
                    break;  

                    case "utos" : {    // minuscula?
                        System.out.println("SWITCH CASE MATCH: UTOS");
                        op1 = ifHexaTrans(op1);
                        appendData(new AsmData("auxt_"+contador_t,"SINGLE","?"));
                        // sea uinteger o hexa no cambia nada lo q devuelve
                        appendCodeBody("fild "+getOperador(op1,"UINTEGER"));
                        appendCodeBody("fstp "+"aux_t"+contador_t);
                        // PERO ESTO PASA DE 16 BITS A 32. PASA DE DW A REAL4 PERO NO ESTÁ EN FORMATO SINGLE..
                // PROBAR SI CON SOLO ESO ANDA. SINO, MIRAR FILMINAS.
                    }
                    break;
    
                    default : System.out.println("PROBLEMA: El terceto no fue implementado.");
                            }
                                }
                                
        private static String mapSubtypeToSizeInBytes(String op2) {
                switch (mapIDSubtypeToVarType(op2)) {
                    case "DW":  return ""+16/8;     // 4  B
                    case "REAL4":  return ""+32/8;     // 8  B
                    case "DQ":  return ""+64/8;     // 16 B
                }
                throw new UnsupportedOperationException("Unimplemented method 'mapSubtypeToSizeInBytes'");
            }
    
        public static void generate(String executablePath, String program) {
        System.out.println(Terceto.TerList.size());
        destPath = executablePath;
        init(program);
        assembler.delete();
        header.delete();
        punto_code_fun.delete();
        punto_code_body.delete();
        punto_data.delete();
        init(program);
        for (Terceto terceto : Terceto.TerList) {
            System.out.println(terceto.toString()+"\n");
            mapTercetoToAssembler(terceto);
            contador_t++;   // siempre coincidira con el numero del terceto
        }
        finish();
    }
}

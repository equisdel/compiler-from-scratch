package CuartaEtapa;

import PrimeraEtapa.*;
import SegundaEtapa.*;
import TercerEtapa.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.jws.soap.SOAPBinding.Use;

// GENERANDO EL .CODE TODAVIA PUEDO TENER TODA LA TS. LUEGO DE GENERAR .DATA AHI NOSE USA MAS
// tonces las aux las voy agregando a las TS y ahi hay logica q ya esta contemplada! :D
public class AsmGenerator {

    // En este punto ya no deberían haber errores sintácticos/semánticos.
    // Atributo FILE: el archivo en código assembler que se genera a partir del programa indicado por el usuario

    // Acá ya no existen errores
    private static String destPath;  // Guarda con el path, hay que ver si funciona según donde estemos parados
    
    private static FileManager header;
    private static FileManager punto_code;    // Guardamos las instrucciones
    private static FileManager punto_data;    // Guardamos todos los datos generados
    private static FileManager assembler;     // Ensamble de todo (incluye header, codigo y data)


    private static final ArrayList<AsmData> assembly_variables = new ArrayList<>();     // otra opcion: usar la TS
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

    
    public static void init() {
        // Se crea cada "componente" del archivo final por separado
        header = new FileManager(destPath+"header");
        header.appendLine(".386                  ; Enable 32-bit instructions");
        header.appendLine(".MODEL FLAT           ; Use FLAT memory model for 32-bit systems");
        header.appendLine(".STACK 100h           ; Define stack size;");
        
        punto_code = new FileManager(destPath+"punto_code");
        punto_code.appendLine(".code");
        
        punto_data = new FileManager(destPath+"punto_data");
        punto_data.appendLine(".data");
        // Falta alguna sección?
    }

    public static void finish() {
            // Ensamblado de las partes que hacen al archivo assembler (¿qué extensión llevan?)
            assembler = new FileManager(destPath+"assembler"); // Estaría bueno que el nombre incluya al del programa
            
            //assembler.appendLine(header);                               // Header
            try {
                assembler.appendFile(header);
                assembler.appendLine("");
                assembler.appendFile(punto_data);
                assembler.appendLine("");
                assembler.appendFile(punto_code);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            header.delete();
            punto_data.delete();    // Archivo temporal con .DATA
            punto_code.delete();    // Archivo temporal con .CODE
                                                                        // Footer?
        }
    
        private static String mapIDSubtypeToVarType(String subtype) {
            // subtipos: uinteger/hexa : DW, single : DD, "64B" : DQ
            switch (subtype) {
                case "64B" : return "DQ";       // 64 bits
                case "SINGLE" : return "DD";    // 32 bits
                default : return "DW";          // 16 bits
            }
        }
                
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
        */
            return "";
        }
        
        // Queda implementar esto al archivo de datos, junto con otras cosas (auxiliares de tercetos, por ej)
        private void volcarTablaDeSimbolos() {

            // Todas las entradas de la tabla de símbolos, excepto palabras reservadas, van a parar a la sección data como variables del assembly
            // Solo se vuelcan IDs : declaración con su subtipo, sin valores.
            Simbolo simbolo;
            for (String lexema : AnalizadorLexico.t_simbolos.getAllEntries()) {
                simbolo = AnalizadorLexico.t_simbolos.get_entry(lexema);
                //System.out.println(lexema);
                if (simbolo.getTipo().equals("ID")) {
                    // PARA PAIR EN ASSEMBLER: SI TENGO UNA VARIABLE VAR1 DE TIPO PAIRSITO (q ES UN PAIR DE UINTEGER)
                    // EN ASSEMBLER PONER _VAR1_1 Y _VAR1_2 PARA ACCEDER A LOS ELEMENTOS DEL PAR
                    // EL _ al principio ES PARA DIFERENCAIR Y Q SEAN PAR PORQ SINO NADA ASEGURA HAYA OTRA VARIABLE CON MISMO NOMBRE
                    // si es pair:
                    if (!(simbolo.getSubtipo().equals("UINTEGER") || simbolo.getSubtipo().equals("HEXA") || simbolo.getSubtipo().equals("SINGLE"))){
                        assembly_variables.add(new AsmData("_"+lexema.replace("{","_").replace("}","_")+"_1",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                        assembly_variables.add(new AsmData("_"+lexema.replace("{","_").replace("}","_")+"_2",mapIDSubtypeToVarType(AnalizadorLexico.t_simbolos.get_subtype(simbolo.getSubtipo())),"?"));
                    }
                     else {appendData(new AsmData(lexema,mapIDSubtypeToVarType(simbolo.getSubtipo()),"?"));}
                    
                }
            }
            
        }
    
        private static void appendCode(String instruction) {
            punto_code.appendLine(instruction);
                // agrega el string al final del código actual
                // hace el salto de línea dentro del assembler (manejo de archivos)
                // Assuming you have a StringBuilder to hold the assembler code
        }
        
        private void appendData(AsmData data) {
            punto_data.appendLine(data.getInstruction());   // Como distinguimos entre ".data" y ".data?" ? 
                // las aux pueden ir agregandose a la TS
                // Distinguir entre .data y .data? -> podemos usar solo .data -> no es necesario
                // Se hace al final, porque ya se sabe cuantas variables auxiliares se van a necesitar.
                // Tener una lista de auxs con su tipo e inicializacion (o no).
        }
        
        private static String ifHexaTrans(String hexValue) { // pasa de hexa 0x123 a hexa para assembler
            // EJEMPLO: 0x12 : 012h
            if (hexValue.startsWith("0x")) {
                hexValue = hexValue.substring(2);
                return 0 + hexValue + "h";
            } return hexValue;
        }
    
        private static String getOperador(String operador, String subtipo) { //operador puede ser ID,terceto, cte pairsito{1}
            // devuelve el operador en el formato correcto para el assembler
            Simbolo ref_op = AnalizadorLexico.t_simbolos.get_entry(operador);   // funciona porque a esta etapa llega el lexema con scope incluido
            if (ref_op != null) {                       // Es un ID  (cte no esta en TS porque hubo limpieza)
                System.out.println(operador+" es un ID o algo de la T. de S.");
                return operador;
            } else if (Parser.isTerceto(operador)) {    // Es un terceto
                System.out.println(operador+" es un terceto");
                return "auxt_"+operador.substring(1,operador.length()-1);  // Ejemplo: terceto <2> : auxt_2
            } else {                                    // Es una constante literal
                // hacer el pasaje o traduccion en caso de single 
                // los tipo float no se pueden usar como inmediatos
                System.out.println("cte: "+operador+": El subtipo es "+subtipo);
                if (Parser.isPair(operador)) {  //pairsito{1}
                    return ("_"+operador.replace("{","_").replace("}","_"));
                } else if (subtipo.equals("SINGLE")){
                    //agregar a .data  varfloat dd mapSingleToFloat(operador);
                    // _cte1 dd 1.2     -1.2s-8 [0-9][a-z]'.''+/-' : ctes__1__2s_8  | ctes_152__21s3 | '+', '-':'_', '.':'__'
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
                        appendCode(op1+":");
                        break;

                    case "JUMP_TAG" : 
                        appendCode("JMP "+op1+":");   // JMP es salto incondicional
                        break;

                    case "LABEL_FUN" : {        // tiene que saber el tipo del parámetro
                        // el terceto guarda: ¿nos sirve de algo esta data?
                        // operador:    LABEL_FUN
                        // op1:         nombre de la función (con scope y reemplazos)
                        // op2:         nombre del parámetro formal (con scope y reemplazos)
                        // subtipo:     tipo del parametro formal
                        appendCode(op1+" PROC");
                        appendCode("XOR EAX, EAX");     // setea el registro EAX en todos 0s
                        appendCode("MOV EAX, [ESP+4]"); // carga el parámetro en EAX (32 bits son 4 bytes, funciona para UINTEGER y para SINGLE)
                        // appendCode("POP ESP");          // quita el parámetro del tope de la pila?
                    }
                        break;

                    case "END_FUN" : {          // tiene que saber el tipo del retorno?
                        appendCode("ret");  // El retorno se guarda, según el tipo
                        appendCode(op1+" ENDP");
                        // el terceto guarda: ¿nos sirve de algo esta data?
                        // operador:    LABEL_FUN
                        // op1:         nombre de la función (con scope y reemplazos)
                        // op2:         nombre del parámetro (con scope y reemplazos)
                        // subtipo:     tipo del retorno
                    }
                        break;

                    case "CALL_FUN" :       // tiene que saber el tipo del retorno, porque luego lo va a asignar al del lado izq
                        appendCode("CALL "+op1+":");   // JMP es salto incondicional.
                        // quizás reservar un registro para esto? EDX? o la pila directamente
                        // dos registros/variables: el retorno y el parámetro.
                        // como se maneja el tema del parámetro? habría que mover lo que hay en op2 (nombre de variable) al parámetro
                        // podriamos incluir la aux donde quedara el resultado, del tipo del terceto (que es del tipo de retorno de la funcion)
                        // entonces despues es mas facil usar el resutlado de lo que deuvelve en el resto de tercetos
                        break;

                    case "OUTF" : {
                        System.out.println("SWITCH CASE MATCH: OUTF");
                        //op2 null, op1: ID, cte, terceto (resultado de arimeticas, de funcion) O CHARCH!
                        appendCode("INVOKE printf, addr __new_line__");
                        if (terceto.subtipo.equals("SINGLE")) { // sea id,cte,terceto
                            // CONVERTIR A 64 BITS Y LLAMAR A PRINTF
                            // la variable de 64 bits q nombre le ponemos? para asegurar no se repita nunca
                            // opcion: auxt_contador_t_64
                            assembly_variables.add(new AsmData("auxt_"+contador_t+"_64","64B","?"));
                            //pasar el valor de aux_t_contador_t a auxt_contador_t_64
                            appendCode("fld "+"auxt_"+contador_t);
                            appendCode("fst "+"auxt_"+contador_t+"_64");
                            appendCode("invoke printf, cfm$(\"%.20Lf\\n\") auxt_"+contador_t+"_64");
                        } else if (terceto.subtipo.equals("UINTEGER")|| terceto.subtipo.equals("HEXA")) {
                            appendCode("invoke printf, cfm$(\"%u\\n\"), "+getOperador(op1, terceto.subtipo));
                        }   // FALTA CASO PAIR y funcion
                    }   break;
        
                    case "SUMA" : {
                        System.out.println("SWITCH CASE MATCH: SUMA");
                        // El resultado de la suma se guarda en una variable auxiliar con prefijo "auxt_" y sufijo igual al id del terceto actual.
                        
                        // Declarar la variable "auxt_[id_terceto]".
                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(new String(terceto.subtipo)),"?"));
                        // Si operador es terceto:  aux_[op_id_terceto]
                        // Si operador es ID:       referencia a la variable (el operador mismo)
                        // Si operador es CTE:      literal (inmediato, el operador mismo)
                        String final_op1 = getOperador(op1, terceto.subtipo);
                        String final_op2 = getOperador(op2, terceto.subtipo);
                        appendCode("MOV auxt_"+contador_t+","+final_op1);   // MOV auxt_[id_terceto], final_op1
                        appendCode("ADD auxt_"+contador_t+","+final_op2); // ADD auxt_[id_terceto], final_op2
                // SI ES SINGLE (FLOAT) SE OPERA CON PILA, AGREGAR ESA OPCION (MIRANDO TERCETO.SUBTIPO)
                    }
                    break;

                    case "RESTA" : {
                        // Análogo a la SUMA
                        System.out.println("SWITCH CASE MATCH: RESTA");
                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                        appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCode("SUB auxt_"+contador_t+","+getOperador(op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                    }
                    break;  

                    case "MULT" : {
                        System.out.println("SWITCH CASE MATCH: MULT");// Análogo a la SUMA
                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                        appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCode("MUL auxt_"+contador_t+","+getOperador(op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                    }
                    break;

                    case "DIV" : {
                        System.out.println("SWITCH CASE MATCH: DIV");// Análogo a la SUMA
                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                        appendCode("MOV auxt_"+contador_t+","+getOperador(op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                        appendCode("DIV auxt_"+contador_t+","+getOperador(op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                    }
                    break;

                    case ":=" : {
                        System.out.println("SWITCH CASE MATCH: :=");
                        // a la izq: siempre ID (o acceso a pair), a la der: ID, CTE, REF_PAIR, INV_FUN, terceto
                        appendCode("MOV "+getOperador(op1, terceto.subtipo)+","+getOperador(op2, terceto.subtipo));
                
                    }
                    break;  

                    case "utos" : {    // minuscula?
                        System.out.println("SWITCH CASE MATCH: UTOS");
                        // utos crea una auxiliar con el valor origen pero de tipo single
                        // puede ser CTE, ID , otro terceto (pero de tipo uinteger o hexa, sino a utos no se crea)
                        // contemplar uinteger a single o hexa a single : PROBAR SI SE PUEDEN TRATAR EXACTAMENTE IGUAL
                        op1 = ifHexaTrans(op1);
                        assembly_variables.add(new AsmData("auxt_"+contador_t,"SINGLE","?"));
                        // sea uinteger o hexa no cambia nada lo q devuelve
                        appendCode("fild "+getOperador(op1,"UINTEGER"));
                        appendCode("fstp "+"aux_t"+contador_t);
                // PROBAR SI CON SOLO ESO ANDA. SINO, MIRAR FILMINAS.
                    }
                    break;
    
                    default : System.out.println("PROBLEMA: El terceto no fue implementado.");
                            }
                                }
                                
        private static String mapSubtypeToSizeInBytes(String op2) {
                switch (mapIDSubtypeToVarType(op2)) {
                    case "DW":  return ""+16/8;     // 4  B
                    case "DD":  return ""+32/8;     // 8  B
                    case "DQ":  return ""+64/8;     // 16 B
                }
                throw new UnsupportedOperationException("Unimplemented method 'mapSubtypeToSizeInBytes'");
            }
    
        public static void generate(String executablePath) {
        System.out.println(Terceto.TerList.size());
        destPath = executablePath;
        init();
        header.delete();
        punto_code.delete();
        punto_data.delete();
        init();
        for (Terceto terceto : Terceto.TerList) {
            System.out.println(terceto.toString()+"\n");
            mapTercetoToAssembler(terceto);
            contador_t++;   // siempre coincidira con el numero del terceto
        }
        finish();
    }
}

package CuartaEtapa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import PrimeraEtapa.*;
import TercerEtapa.*;
import SegundaEtapa.*;

public class AsmGenerator {

    // En este punto ya no deberían haber errores sintácticos/semánticos.
    // Atributo FILE: el archivo en código assembler que se genera a partir del programa indicado por el usuario

    // Acá ya no existen errores
    private static String destPath;  // Guarda con el path, hay que ver si funciona según donde estemos parados
    private static final String header = "  .386\n.model flat, stdcall\noption casemap:none\nincludelib \\masm32\\lib\\kernel32.lib\nincludelib \\masm32\\lib\\user32.lib\ninclude \\masm32\\include\\masm32rt.inc\nincludelib \\masm32\\lib\\kernel32.lib\nincludelib \\masm32\\lib\\masm32.lib"; 
    private static final ArrayList<AsmData> assembly_variables = new ArrayList<>();
    private static int contador_t = 0;

    private static class AsmData {
        String var_name,var_type,var_value;
        // Distinguir entre inicializadas y no inicializadas según var_value ? null.
        // metodo que devuelva un string con la instrucción literal: ejemplo, aux_2 DW 5.
        AsmData(String var_name, String var_type, String var_value) {
            this.var_name = var_name;
            this.var_type = var_type; 
            this.var_value = var_value; 
        }
    }

    public void initialize() {
        // Inicializa el archivo en Null
        // Le agrega el header y todo lo que corresponde al formato del archivo
        // por ahora header ocupa 8 lineas asique empezar en linea 9

    }
    public void finalize() {
        // cierra el archivo y lo retorna.
        // guarda el assembly en el path especificado.
    }

    private static String mapIDSubtypeToVarType(String subtype) {
            // cte uinteger: 16 bits.   
            // cte single: 32 bits.
            // tipo personalizado?
            return "";
            }
            
            private String mapSingleToFloat(String single) {    // Convención IEEE 754
    
                String signo, exponenteBin, mantisaBin;       // 1, 8 y 23 bits, respectivamente.
                
                // Determinar el signo
    
                switch (single.substring(0,0)) {    // signo del single
                    case "+":   signo = "0"; single = single.substring(1,single.length());
                                break;
                    case "-":   signo = "1"; single = single.substring(1,single.length());
                                break;
                    default:    signo = "0";        // positivo por defecto
                                break;
                }
    
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
        private void volcarTablaDeSimbolos() {
            // Todas las entradas de la tabla de símbolos, excepto palabras reservadas, van a parar a la sección data como variables del assembly
            // Solo se vuelcan IDs -> declaración con su subtipo e ¿inicialización?
            /*  
                for (ID en tabla de símbolos) {
                    appendData(Id.nombre, mapIDSubtypeToVarType(ID.subtype));
                }
            */
        }
    
        private static void appendCode(String instruction) {
                // agrega el string al final del código actual
                // hace el salto de línea dentro del assembler (manejo de archivos)
                // Assuming you have a StringBuilder to hold the assembler code
            }
        
            private void appendData() {
                // Distinguir entre .data y .data?
                // Se hace al final, porque ya se sabe cuantas variables auxiliares se van a necesitar.
                // Tener una lista de auxs con su tipo e inicializacion (o no).
            }
        
            private static String getOperador(String operador, String subtipo) {
                        // devuelve el operador en el formato correcto para el assembler
                        Simbolo ref_op = AnalizadorLexico.t_simbolos.get_entry(operador);
                        if (ref_op != null) {                       // Es un ID
                            return operador;
                        } else if (Parser.isTerceto(operador)) {    // Es un terceto
                            return "auxt_"+operador.substring(1,operador.length()-1);  // Ejemplo: terceto <2> -> auxt_2
                        } else {                                    // Es una constante literal
                            // hacer el pasaje o traduccion en caso de single 
                            // los tipo float no se pueden usar como inmediatos
                            if (subtipo=="SINGLE"){
                                //agregar a .data  varfloat dd mapSingleToFloat(operador);
                                // _cte1 dd 1.2     -1.2s-8 [0-9][a-z]'.''+/-' -> ctes__1__2s_8  | ctes_152__21s3 | '+', '-':'_', '.':'__'
                                return "varfloat";}
                            else if (subtipo=="UINTEGER")       // TAMBIEN CONTEMPLAR HEXA (si pasamos el hexa a uinteger, no se donde hacerlo y que sea 'correcto')
                                return operador;
                            else System.out.println("PROBLEMA: El subtipo no es ni SINGLE ni UINTEGER.");
                        }
                        return null;
                    }
                
                    public static void mapTercetoToAssembler(Terceto terceto) {
                    
                            final String scopeDelimitatorReplacement = "@";    // Reemplaza a ":"
                            String op1 = terceto.op1.replace(":",scopeDelimitatorReplacement);
                            String op2 = terceto.op2.replace(":",scopeDelimitatorReplacement);
                    
                            switch (terceto.operacion) {
                                case "LABEL":       appendCode(op1+":");    
                                                    break;
                    
                                case "JUMP_TAG":    appendCode("JMP "+op1+":");   // JMP es salto incondicional
                                                    break;
                                
                                case "JUMP_FUN":    appendCode("JMP "+op1+":");   // JMP es salto incondicional
                                                    break;
                    
                                case "OUTF":        
                                                    break;
                    
                                case "SUMA":        // El resultado de la suma se guarda en una variable auxiliar con prefijo "auxt_" y sufijo igual al id del terceto actual.
                                                    
                                                    // Declarar la variable "auxt_[id_terceto]".
                                                    assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                                                    
                                                    // Si operador es terceto:  aux_[op_id_terceto]
                                                    // Si operador es ID:       referencia a la variable (el operador mismo)
                                                    // Si operador es CTE:      literal (inmediato, el operador mismo)
                                                    String final_op1 = getOperador(terceto.op1, terceto.subtipo);
                                                    String final_op2 = getOperador(terceto.op2, terceto.subtipo);
                    
                                                    appendCode("MOV auxt_"+contador_t+","+final_op1);   // MOV auxt_[id_terceto], final_op1
                                                    appendCode("ADD auxt_"+contador_t+","+final_op2);   // ADD auxt_[id_terceto], final_op2
                                                    break;
                    
                                case "RESTA":       // Análogo a la SUMA
                                                    assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                                                    appendCode("MOV auxt_"+contador_t+","+getOperador(terceto.op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                                                    appendCode("SUB auxt_"+contador_t+","+getOperador(terceto.op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                                        break;
        
                    case "MULT":        // Análogo a la SUMA
                                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                                        appendCode("MOV auxt_"+contador_t+","+getOperador(terceto.op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                                        appendCode("MUL auxt_"+contador_t+","+getOperador(terceto.op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                                        break;
        
                    case "DIV":         // Análogo a la SUMA
                                        assembly_variables.add(new AsmData("auxt_"+contador_t,mapIDSubtypeToVarType(terceto.subtipo),"?"));
                                    appendCode("MOV auxt_"+contador_t+","+getOperador(terceto.op1, terceto.subtipo));   // MOV auxt_[id_terceto], final_op1
                                    appendCode("DIV auxt_"+contador_t+","+getOperador(terceto.op2, terceto.subtipo));   // SUB auxt_[id_terceto], final_op2
                                    break;
                    
                case ":=":          // a la izq: siempre ID (o acceso a pair), a la der: ID, CTE, REF_PAIR, INV_FUN, CHARCH, terceto
                                    appendCode("MOV "+op1+","+getOperador(op2, terceto.subtipo));
                                    // CHEQUEAR LO QUE FALTE ANTES DE MOV ( EN CASO DE FLOAT )
                                break;
                case "utos":    // PUEDE SER UINTEGER O HEXA, OPCION 1 CONTEMPLAR LAS POSIBILIDADES ACA, OPCION 2 HACER OTRO TERCETO 'htos'
                break;
                
                
                default:    System.out.println("PROBLEMA: El terceto no fue implementado.");
                    break;
            }
        }
    
        public static void generate(String executablePath) {
            destPath = executablePath;
            for (Terceto terceto : Terceto.TerList) {
                mapTercetoToAssembler(terceto);
            contador_t++;
        }
    }

}

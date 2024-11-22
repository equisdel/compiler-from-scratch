package PrimeraEtapa;

import java.io.IOException;
import java.util.function.*;

public class AccionSemantica {

    protected static AccionSemantica[] all_actions = new AccionSemantica[200];

    private int id;
    private String descripcion;
    private Function<Void, Integer> action;
    static String error_msg = "Error"; // Variable compartida entre las acciones por si quieren elevar mensajes de
                                       // Warning

    public AccionSemantica(int id, String descripcion, Function<Void, Integer> action) {
        this.id = id;
        this.descripcion = descripcion;
        this.action = action;
        all_actions[id] = this; // Construcción: cada acción se agrega a la lista de todas las acciones
    }

    public void display() {
        System.out.println("Acción [" + id + "]: " + descripcion);
    }

    public Integer execute() {
        return this.action.apply(null);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        // Definición de las expresiones lambda que definen a cada instancia de Acción Semántica

        String desc_0 = "Resetea las variables del Analizador Léxico.";
        Function<Void,Integer> action_0 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema 		= "";
                AnalizadorLexico.lexema_type    = ""; 
                AnalizadorLexico.lexema_subtype = "";
                AnalizadorLexico.next_char      = "";	
                return 0;
            }
        };

        String desc_1 = "Anexa el último caracter leído al final de la cadena.";
        Function<Void,Integer> action_1 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + AnalizadorLexico.next_char;  // ¿Hay manera de que tenga estos datos más a mano?
                return 0;
            }
        };  

        String desc_2 = "Elimina el último caracter de la cadena.";
        Function<Void,Integer> action_2 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema.substring(0,AnalizadorLexico.lexema.length()-1);
                return 0;
            }
        };  

        String desc_3 = "Devuelve el último caracter leído a la entrada.";
        Function<Void,Integer> action_3 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // No agregarlo a la cadena es suficiente.
                try { AnalizadorLexico.program_file_reader.reset(); } catch (IOException e)  { if(AnalizadorLexico.isdebug()) System.out.println("no funca"); e.printStackTrace(); }
                return 0;
            }
        };

        String desc_4 = "Sobreescribe el último caracter anexado a la cadena con el último leído.";
        Function<Void,Integer> action_4 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                all_actions[2].execute();   // Elimina el último caracter anexado a la cadena.
                all_actions[1].execute();   // Lo reemplaza con el último caracter leído de la entrada.
                return 0;
            }
        };

        String desc_5 = "Discierne entre identificador y palabra reservada. Llama a las A.S. adecuadas para tratar a ambos tipos.";
        Function<Void,Integer> action_5 = new Function<Void,Integer>() {
            final int max_length = AnalizadorLexico.id_max_length;    // 15
            // Las palabras reservadas (PR) estan precargadas en la TS
            public Integer apply(Void t) {
                String key = AnalizadorLexico.lexema.toUpperCase();
                String key_under_15 = key.substring(0,Math.min(key.length(),max_length));       
                Simbolo value = AnalizadorLexico.t_simbolos.get_entry(key.substring(0,Math.min(key.length(),max_length)).toUpperCase());
                all_actions[3].execute();   // Devuelve el último caracter a la entrada
                if (value != null) {        // Existe una entrada en la TS asociada al lexema
                    //System.out.println(value.getSubtipo());
                    if (value.getSubtipo().equals("RESERVED"))  {   // Es una palabra reservada (PR)
                        AnalizadorLexico.lexema_subtype = "RESERVED";
                        all_actions[21].execute();          //   -> lo identifica como tal
                    }
                    else {          // Es una referencia a un identificador existente
                        if (key.length() > max_length)
                            all_actions[107].execute();
                        AnalizadorLexico.lexema_type = value.getTipo();
                        AnalizadorLexico.lexema_subtype = value.getSubtipo();
                    }                                 
                    // ¿Redeclaración? ¿Distinto scope? ¿Distinto subtipo? Cuestiones que pueden surgir más adelante
                                       // Es un nuevo identificador
                } else {
                        all_actions[22].execute();      // Identifica el tipo 'ID' (y subtipo si corresponde) del lexema
                        if (key.length() > max_length)  // El identificador supera el límite máximo de caracteres
                            all_actions[107].execute(); //   -> Trunca el identificador + Warning
                        all_actions[7].execute();       // Agrega el identificador a la tabla de simbolos
                    
                }
                return 0;
            }
        };
        
    
        String desc_7 = "Agrega una nueva entrada a la tabla de simbolos.";
        Function<Void,Integer> action_7 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.t_simbolos.add_entry(AnalizadorLexico.lexema, AnalizadorLexico.lexema_type, AnalizadorLexico.lexema_subtype);
                return 0;
            }
        };

        String desc_9 = "Devuelve el último caracter a la entrada + Inserción en la TS.";
        Function<Void,Integer> action_9 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                all_actions[3].execute();
                all_actions[7].execute();
                return 0;
            }
        };
        
        String desc_6 = "Control de rango: CTE UINTEGER.";
        Function<Void,Integer> action_6 = new Function<Void,Integer>() {
            int max = AnalizadorLexico.cte_uinteger_max;
            public Integer apply(Void t) {
                int cte = Integer.parseInt(AnalizadorLexico.lexema);
                if (cte > max) {
                    all_actions[111].execute();
                    AnalizadorLexico.lexema = max+"";
                }
                return 0;
            }
        };
        
        String desc_8 = "Control de rango: CTE HEXA.";
        Function<Void,Integer> action_8 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                String hexa = AnalizadorLexico.lexema.substring(2);
                AnalizadorLexico.lexema = Integer.parseInt(hexa, 16)+"";
                all_actions[6].execute();
                AnalizadorLexico.lexema = "0x"+Integer.toHexString(Integer.parseInt(AnalizadorLexico.lexema)).toUpperCase();
                all_actions[9].execute();
                return 0;
            }
        };

        String desc_10 = "Corrobora rango de variable single";
        Function<Void,Integer> action_10 = new Function<Void,Integer>() {
            public Integer apply(Void t) {  
                /*
                    String single = AnalizadorLexico.lexema;
                    String[] parts = single.split("S");
                    float base = Float.parseFloat(parts[0]);
                    int exponent = Integer.parseInt(parts[1]);
                    float value = (float) (base * Math.pow(10, exponent));
                    if (value > 3.4028235e+38) {
                        AnalizadorLexico.lexema = "3.4028235s+38";
                        all_actions[112].execute();
                    } else if (value < 1.17549435e-38) {
                        AnalizadorLexico.lexema = "1.17549435s-38";
                        all_actions[112].execute();
                    } 
                */
                    all_actions[9].execute();   // Devuelve el último char + Inserción en la TdeS
                    return 0;
            }
        };

        // A.S. ASOCIADAS A RECONOCIMIENTO DE (SUB)TIPOS

        String desc_20 = "Actualiza el tipo del lexema a CTE (Constante).";
        Function<Void,Integer> action_20 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = "CTE";   // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                AnalizadorLexico.lexema_subtype = "UINTEGER";
                all_actions[1].execute();
                return 0;
            }
        };

        String desc_21 = "Actualiza el tipo del lexema a PR (Palabra Reservada).";
        Function<Void,Integer> action_21 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = AnalizadorLexico.lexema.toUpperCase();   // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                //all_actions[1].execute();
                return 0;
            }
        };

        String desc_22 = "Actualiza el tipo (y subtipo) del lexema a ID (Identificador).";
        Function<Void,Integer> action_22 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = "ID";
                char inicial = Character.toLowerCase(AnalizadorLexico.lexema.charAt(0));
                switch(inicial) {
                    case 's':   AnalizadorLexico.lexema_subtype = "SINGLE";     break;   // Usar var subtype
                    case 'u':   AnalizadorLexico.lexema_subtype = "UINTEGER";   break;
                    case 'v':   AnalizadorLexico.lexema_subtype = "UINTEGER";   break;
                    case 'w':   AnalizadorLexico.lexema_subtype = "UINTEGER";   break;
                }
                return 0;
            }
        };

        String desc_23 = "Actualiza el subtipo del lexema a HEXA (Constante Hexadecimal).";
        Function<Void,Integer> action_23 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_subtype = "HEXA";
                all_actions[1].execute();
                return 0;
            }
        };

        String desc_24 = "Actualiza el subtipo del lexema a SINGLE (Constante PF-32).";
        Function<Void,Integer> action_24 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_subtype = "SINGLE";
                all_actions[1].execute();
                AnalizadorLexico.t_simbolos.addCont(AnalizadorLexico.lexema);
                return 0;
            }
        };

        String desc_25 = "Actualiza el subtipo del lexema a UINTEGER (Constante Entera Sin Signo).";
        Function<Void,Integer> action_25 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_subtype = "UINTEGER";
                all_actions[6].execute();   // Control de rangos
                all_actions[9].execute();   // Devuelve el último caracter + Inserción en la TdeS
                return 0;
            }
        };

        String desc_26 = "Actualiza el tipo del lexema a CHARCH (Cadena de Caracteres Multilínea).";
        Function<Void,Integer> action_26 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                all_actions[1].execute();   // Agrega el ']' de cierre al lexema
                AnalizadorLexico.lexema_type = "CHARCH";    // Indica el tipo
                all_actions[7].execute();   // Inserción en la TdeS
                return 0;
            }
        };

        String desc_27 = "Actualiza el tipo del lexema a TAG (Etiqueta de salto de control).";
        Function<Void,Integer> action_27 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = "TAG";
                all_actions[1].execute();
                all_actions[7].execute();   // Agrega el simbolo a la tabla
                return 0;
            }
        };

        // A.S. ASOCIADAS A ERRORES

        String ANSI_RED = "\u001B[31m";  
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RESET = "\u001B[0m"; 

        String desc_100 = "En caso de errores levanta un Warning con el mensaje de error actual.";
        Function<Void,Integer> action_100 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Implementar cola de Warnings
                System.out.println(ANSI_YELLOW+"WARNING - [LINE "+AnalizadorLexico.line_number+"]: "+ANSI_RESET+error_msg);
                error_msg = null;
                return 0;
            }
        };

        String desc_101 = "Error de escritura de operador '!=' o ':='. Corrección + Warning.";
        Function<Void,Integer> action_101 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "Escritura incompleta del operador '"+AnalizadorLexico.lexema+"='.";
                all_actions[100].execute();     // Levanta el warning
                all_actions[3].execute();       // Devuelve el último caracter leído a la entrada
                return 0;
            }
        };

        String desc_102 = "Error de comentario declarado con un único '#'. Desecho + Warning.";
        Function<Void,Integer> action_102 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                //AnalizadorLexico.lexema = AnalizadorLexico.lexema + "#"; 
                AccionSemantica.error_msg = "Los comentarios se declaran con doble '#'.";
                all_actions[100].execute();     // Levanta el warning
                all_actions[3].execute();       // Devuelve el último caracter leído a la entrada
                return 0;
            }
        };

        String desc_103 = "Error léxico en cte. de tipo HEXA: parte numérica ausente luego de '0x'. Corrección + Warning.";
        Function<Void,Integer> action_103 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "Ausencia de la parte numérica de la constante HEXA luego del '0x'.";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "0";
                all_actions[9].execute();       // Devuelve el último caracter leído a la entrada + Inserción en TS
                return 0;
            }
        };

        String desc_104 = "Error léxico en cte. de tipo SINGLE: parte decimal ausente luego del '.'. Corrección + Warning.";
        Function<Void,Integer> action_104 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La parte decimal de la constante SINGLE luego del '.' está ausente. Se toma como \"0\".";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "0";
                all_actions[9].execute();       // Devuelve el último caracter leído a la entrada + Inserción en TS
                return 0;
            }
        };

        String desc_105 = "Error léxico en cte. de tipo SINGLE: parte exponencial ausente luego de la 's'. Corrección + Warning.";
        Function<Void,Integer> action_105 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La parte exponencial de la constante SINGLE luego de la 's' está ausente. Se toma como \"1\"";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "1";
                all_actions[10].execute();      // Control de rangos
                return 0;
            }
        };

        String desc_106 = "Error léxico en cte. de tipo SINGLE: parte exponencial ausente luego de la 's'. Corrección + Warning.";
        Function<Void,Integer> action_106 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                all_actions[2].execute();       // Elimina el signo '+' o '-' al final de la cadena
                all_actions[105].execute();     // Invoca a la acción 105 y trata el caso como tal
                return 0;
            }
        };

        String desc_107 = "Error de longitud del identificador superior al límite establecido por el lenguaje.";
        Function<Void,Integer> action_107 = new Function<Void,Integer>() {
            int max_length = AnalizadorLexico.id_max_length;
            public Integer apply(Void t) {
                String lexema_truncado = AnalizadorLexico.lexema.substring(0, max_length);
                AccionSemantica.error_msg = "El identificador "+AnalizadorLexico.lexema+" supera la longitud máxima permitida ("+max_length+" caracteres). Se tomará como \""+lexema_truncado+"\".";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = lexema_truncado;
                return 0;
            }
        };

        String desc_108 = "Error de inserción fuera de contexto del simbolo '_'.";
        Function<Void,Integer> action_108 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El simbolo '_' solo puede usarse dentro de (1) el nombre de un identificador, (2) una cadena de caracteres o (3) un comentario.";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_109 = "Error de inserción fuera de contexto del simbolo ']'.";
        Function<Void,Integer> action_109 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El simbolo ']' solo puede usarse (1) como cierre de una cadena de caracteres previamente abierta por '[' o (2) dentro de un comentario.";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_110 = "Error de CHARCH abierta indefinidamente.";
        Function<Void,Integer> action_110 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La cadena de caracteres no ha sido correctamente cerrada mediante el simbolo ']'.";
                String aux = new String(AnalizadorLexico.next_char);
                AnalizadorLexico.next_char = "]";
                all_actions[26].execute();
                AnalizadorLexico.next_char = aux;
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_111 = "Error de incumplimiento de rangos en cte. de tipo UINTEGER/HEXA.";
        Function<Void,Integer> action_111 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La constante de tipo UINTEGER/HEXA se encuentra fuera del rango permitido. RANGO: [0, 65535].";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };
        
        String desc_112 = "Error de single fuera de rango";
        Function<Void,Integer> action_112 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La constante de tipo SINGLE se encuentra fuera del rango permitido. RANGO: [1.17549435e-38, 3.4028235e+38] [-3.4028235e+38, -1.17549435e-38].";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_113 = "Error de cte. SINGLE: parte decimal ausente luego del punto.";
        Function<Void,Integer> action_113 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La parte decimal de la constante de tipo SINGLE está ausente. Se toma como \"0\".";
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "0";
                all_actions[1].execute();
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_114 = "Error de cte. SINGLE: parte entera ausente antes del punto.";
        Function<Void,Integer> action_114 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "La parte entera de la constante de tipo SINGLE está ausente. Se toma como \"0\".";
                AnalizadorLexico.lexema = "0";
                AnalizadorLexico.lexema_type = "CTE";
                all_actions[24].execute();
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        }; 

        String desc_199 = "Error de simbolo no reconocido por la gramática del lenguaje.";
        Function<Void,Integer> action_199 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El simbolo "+AnalizadorLexico.next_char+" no pertenece al lenguaje."; // se imprime rarisimo
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        // Instanciación de acciones semánticas

        AccionSemantica as_0 = new AccionSemantica(0, desc_0, action_0);
        AccionSemantica as_1 = new AccionSemantica(1, desc_1, action_1);
        AccionSemantica as_2 = new AccionSemantica(2, desc_2, action_2);
        AccionSemantica as_3 = new AccionSemantica(3, desc_3, action_3);
        AccionSemantica as_4 = new AccionSemantica(4, desc_4, action_4);
        AccionSemantica as_5 = new AccionSemantica(5, desc_5, action_5);
        AccionSemantica as_6 = new AccionSemantica(6, desc_6, action_6);
        AccionSemantica as_7 = new AccionSemantica(7, desc_7, action_7);
        AccionSemantica as_8 = new AccionSemantica(8, desc_8, action_8);
        AccionSemantica as_9 = new AccionSemantica(9, desc_9, action_9);
        AccionSemantica as_10 = new AccionSemantica(10, desc_10, action_10);    

        AccionSemantica as_20 = new AccionSemantica(20, desc_20, action_20);
        AccionSemantica as_21 = new AccionSemantica(21, desc_21, action_21);
        AccionSemantica as_22 = new AccionSemantica(22, desc_22, action_22);
        AccionSemantica as_23 = new AccionSemantica(23, desc_23, action_23);
        AccionSemantica as_24 = new AccionSemantica(24, desc_24, action_24);
        AccionSemantica as_25 = new AccionSemantica(25, desc_25, action_25);
        AccionSemantica as_26 = new AccionSemantica(26, desc_26, action_26);
        AccionSemantica as_27 = new AccionSemantica(27, desc_27, action_27);

        AccionSemantica as_100 = new AccionSemantica(100, desc_100, action_100);

        AccionSemantica as_101 = new AccionSemantica(101, desc_101, action_101);
        AccionSemantica as_102 = new AccionSemantica(102, desc_102, action_102);
        AccionSemantica as_103 = new AccionSemantica(103, desc_103, action_103);
        AccionSemantica as_104 = new AccionSemantica(104, desc_104, action_104);
        AccionSemantica as_105 = new AccionSemantica(105, desc_105, action_105);
        AccionSemantica as_106 = new AccionSemantica(106, desc_106, action_106);
        AccionSemantica as_107 = new AccionSemantica(107, desc_107, action_107);
        AccionSemantica as_108 = new AccionSemantica(108, desc_108, action_108);
        AccionSemantica as_109 = new AccionSemantica(109, desc_109, action_109);
        AccionSemantica as_110 = new AccionSemantica(110, desc_110, action_110);
        AccionSemantica as_111 = new AccionSemantica(111, desc_111, action_111);
        AccionSemantica as_112 = new AccionSemantica(112, desc_112, action_112);
        AccionSemantica as_113 = new AccionSemantica(113, desc_113, action_113);
        AccionSemantica as_114 = new AccionSemantica(114, desc_114, action_114);

        AccionSemantica as_199 = new AccionSemantica(199, desc_199, action_199);

    // prueba de accion 112
        /*
            AnalizadorLexico.lexema = "3s+4";
            AccionSemantica.all_actions[10].execute();
            System.out.println(AnalizadorLexico.lexema);
        */
    }
}
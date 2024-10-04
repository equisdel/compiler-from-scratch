package PrimeraEtapa;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.*;

public class AccionSemantica {

    protected static AccionSemantica[] all_actions = new AccionSemantica[200];
    
    private int id;
    private String descripcion;
    private Function<Void,Integer> action;
    static String error_msg = "Error";    // Variable compartida entre las acciones por si quieren elevar mensajes de Warning

    public AccionSemantica(int id, String descripcion, Function<Void,Integer> action) {
        this.id = id;
        this.descripcion = descripcion;
        this.action = action;
        all_actions[id] = this;      // Construcción: cada acción se agrega a la lista de todas las acciones
    }

    public Integer execute() {
        return this.action.apply(null);
    }

    public static void main(String[] args) {

        // Definición de las expresiones lambda que definen a cada instancia de Acción Semántica

        String desc_0 = "Resetea las variables del Analizador Léxico.";
        Function<Void,Integer> action_0 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.token 		    = -1;   // Sacar
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
        };  // Testeado

        String desc_2 = "Elimina el último caracter de la cadena.";
        Function<Void,Integer> action_2 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema = AnalizadorLexico.lexema.substring(0,AnalizadorLexico.lexema.length()-1);
                return 0;
            }
        };  // Testeado

        String desc_3 = "Devuelve el último caracter leído a la entrada.";
        Function<Void,Integer> action_3 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // No agregarlo a la cadena es suficiente.
                try { AnalizadorLexico.program_file_reader.reset(); } catch (IOException e) { System.out.println("no funca"); e.printStackTrace(); }
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
            int max_length = AnalizadorLexico.id_max_length;    // 15
            String key = AnalizadorLexico.lexema;
            Simbolo value = AnalizadorLexico.t_simbolos.get_entry(key.substring(0,Math.min(key.length(),max_length)));
            // Las palabras reservadas (PR) estan precargadas en la TS
            public Integer apply(Void t) {
                all_actions[3].execute();   // Devuelve el último caracter a la entrada
                if (value != null) {        // Existe una entrada en la TS asociada al lexema
                    if (value.getSubtipo()=="reserved")     // Es una palabra reservada (PR)
                        all_actions[21].execute();          //   -> lo identifica como tal
                    else {}                                 // Es una referencia a un identificador existente
                        // ¿Redeclaración? ¿Distinto scope? ¿Distinto subtipo? Cuestiones que pueden surgir más adelante
                } else {                                    // Es un nuevo identificador
                    all_actions[22].execute();      // Identifica el tipo 'ID' (y subtipo si corresponde) del lexema
                    if (key.length() > max_length)  // El identificador supera el límite máximo de caracteres
                        all_actions[107].execute(); //   -> Trunca el identificador + Warning
                    all_actions[7].execute();       // Agrega el identificador a la tabla de símbolos
                }
                return 0;
            }
        };

        String desc_7 = "Agrega una nueva entrada a la tabla de símbolos.";
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

        // A.S. ASOCIADAS A RECONOCIMIENTO DE (SUB)TIPOS

        String desc_20 = "Actualiza el tipo del lexema a PR (Palabra Reservada).";
        Function<Void,Integer> action_20 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = AnalizadorLexico.lexema.toUpperCase();   // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                return 0;
            }
        };

        String desc_21 = "Actualiza el tipo del lexema a CTE (Constante).";
        Function<Void,Integer> action_21 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = AnalizadorLexico.lexema.toUpperCase();   // Cada PR se identifica consigo misma: lexema "if" -> lexema_type "IF".
                all_actions[1].execute();
                return 0;
            }
        };

        String desc_22 = "Actualiza el tipo (y subtipo) del lexema a ID (Identificador).";
        Function<Void,Integer> action_22 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_type = "ID";
                char inicial = AnalizadorLexico.lexema.charAt(0);
                switch(inicial) {
                    case 's':   AnalizadorLexico.lexema_subtype = "SINGLE";     // Usar var subtype
                    case 'u':   AnalizadorLexico.lexema_subtype = "UINTEGER";
                    case 'v':   AnalizadorLexico.lexema_subtype = "UINTEGER";
                    case 'w':   AnalizadorLexico.lexema_subtype = "UINTEGER";
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
                return 0;
            }
        };

        String desc_25 = "Actualiza el subtipo del lexema a UINTEGER (Constante Entera Sin Signo).";
        Function<Void,Integer> action_25 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_subtype = "UNITEGER";
                all_actions[9].execute();
                return 0;
            }
        };

        String desc_26 = "Actualiza el tipo del lexema a CHARCH (Cadena de Caracteres Multilínea).";
        Function<Void,Integer> action_26 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AnalizadorLexico.lexema_subtype = "CHARCH";
                return 0;
            }
        };

        // A.S. ASOCIADAS A ERRORES

        String desc_100 = "En caso de errores levanta un Warning con el mensaje de error actual.";
        Function<Void,Integer> action_100 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                // Implementar cola de Warnings
                System.out.println("WARNING - [LINE "+AnalizadorLexico.line_number+"]: "+error_msg);
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
                AccionSemantica.error_msg = "Ausencia de la parte decimal de la constante SINGLE luego del '.'.";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "0";
                all_actions[9].execute();       // Devuelve el último caracter leído a la entrada + Inserción en TS
                return 0;
            }
        };

        String desc_105 = "Error léxico en cte. de tipo SINGLE: parte exponencial ausente luego de la 's'. Corrección + Warning.";
        Function<Void,Integer> action_105 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "Ausencia de la parte exponencial de la constante SINGLE luego de la 's'.";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema + "1";
                all_actions[9].execute();       // Devuelve el último caracter leído a la entrada + Inserción en TS
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
                AccionSemantica.error_msg = "El identificador "+AnalizadorLexico.lexema+" supera la longitud máxima permitida ("+max_length+" caracteres).";
                all_actions[100].execute();     // Levanta el warning
                AnalizadorLexico.lexema = AnalizadorLexico.lexema.substring(0, max_length);
                return 0;
            }
        };

        String desc_108 = "Error de inserción fuera de contexto del símbolo '_'.";
        Function<Void,Integer> action_108 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El símbolo '_' solo puede usarse dentro de (1) el nombre de un identificador, (2) una cadena de caracteres o (3) un comentario.";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_109 = "Error de inserción fuera de contexto del símbolo ']'.";
        Function<Void,Integer> action_109 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El símbolo ']' solo puede usarse (1) como cierre de una cadena de caracteres previamente abierta por '[' o (2) dentro de un comentario.";
                all_actions[100].execute();     // Levanta el warning
                return 0;
            }
        };

        String desc_199 = "Error de símbolo no reconocido por la gramática del lenguaje.";
        Function<Void,Integer> action_199 = new Function<Void,Integer>() {
            public Integer apply(Void t) {
                AccionSemantica.error_msg = "El símbolo "+AnalizadorLexico.next_char+" no pertenece al lenguaje.";
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
        //AccionSemantica as_6 = new AccionSemantica(6, desc_6, action_6);
        AccionSemantica as_7 = new AccionSemantica(7, desc_7, action_7);
        //AccionSemantica as_8 = new AccionSemantica(8, desc_8, action_8);
        AccionSemantica as_9 = new AccionSemantica(9, desc_9, action_9);

        AccionSemantica as_20 = new AccionSemantica(20, desc_20, action_20);
        AccionSemantica as_21 = new AccionSemantica(21, desc_21, action_21);
        AccionSemantica as_22 = new AccionSemantica(22, desc_22, action_22);
        AccionSemantica as_23 = new AccionSemantica(23, desc_23, action_23);
        AccionSemantica as_24 = new AccionSemantica(24, desc_24, action_24);
        AccionSemantica as_25 = new AccionSemantica(25, desc_25, action_25);
        AccionSemantica as_26 = new AccionSemantica(26, desc_26, action_26);

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
        //AccionSemantica as_110 = new AccionSemantica(10, desc_110, action_110);
        AccionSemantica as_199 = new AccionSemantica(199, desc_199, action_199);


        /*
        // TEST
        AnalizadorLexico.lexema = "Delfina";
        AnalizadorLexico.next_char = "F";
        as_4.execute();
        System.out.println(AnalizadorLexico.lexema);
        
        //as_1.execute();
        */
    }
}
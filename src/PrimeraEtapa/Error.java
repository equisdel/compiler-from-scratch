package PrimeraEtapa;
import java.util.ArrayList;

public class Error {

    final String ANSI_RED = "\u001B[31m";  
    final String ANSI_YELLOW = "\u001B[33m";
    final String ANSI_RESET = "\u001B[0m"; 
    
    static ArrayList<Error> all_Errors = new ArrayList<>();
    static boolean contains_fatal_error = false;
    
    int line_number;
    boolean is_fatal;
    String error_msg;
    String etapa;


    public Error(int line_number, String error_msg, boolean is_fatal, String etapa) {
        this.line_number = line_number;
        this.etapa = etapa;
        this.is_fatal = is_fatal;
        if (is_fatal) contains_fatal_error = true;
        this.error_msg = is_fatal   ?   ANSI_RED    +"Error  "+" [line "+line_number+"]:   "+ANSI_RESET+error_msg 
                                    :   ANSI_YELLOW +"WARNING"+" [line "+line_number+"]:   "+ANSI_RESET+error_msg
                                    ;
        all_Errors.add(this);
    }
    public Error(String error_msg, boolean is_fatal, String etapa)      {
        this(AnalizadorLexico.line_number, error_msg, is_fatal, etapa);
    }
    
    // Solo tres componentes
    public Error(int line_number, String error_msg, boolean is_fatal)   {this(line_number, error_msg, is_fatal, "");}
    public Error(int line_number, String error_msg, String etapa)       {this(line_number, error_msg, true, etapa);}

    // Solo dos componentes
    public Error(String error_msg, String etapa)            {this(AnalizadorLexico.line_number,error_msg,true,etapa);}
    public Error(String error_msg, boolean is_fatal)        {this(AnalizadorLexico.line_number, error_msg, is_fatal);}
    public Error(int line_number, String error_msg)         {this(line_number,error_msg,true);}

    // Solo un componente
    public Error(String error_msg)      {this(AnalizadorLexico.line_number, error_msg);}
    
    
    
    public static boolean isCompilable() {return !contains_fatal_error;}
    
    public void display() {
        System.out.println(this.error_msg);
    }

    public static void display_all() {
        for (Error e : all_Errors)
            e.display();
    }

    public static void display_some(boolean fatals) {
        for (Error e : all_Errors)
            if (e.is_fatal==fatals)
                e.display();
    }
}

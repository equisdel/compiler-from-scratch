//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
        import java.io.*;
        import PrimerEtapa.AnalizadorLexico;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CHARCH=259;
public final static short NEQ=260;
public final static short LEQ=261;
public final static short MEQ=262;
public final static short ASSIGN=263;
public final static short TAG=264;
public final static short IF=265;
public final static short THEN=266;
public final static short ELSE=267;
public final static short BEGIN=268;
public final static short END=269;
public final static short END_IF=270;
public final static short OUTF=271;
public final static short TYPEDEF=272;
public final static short FUN=273;
public final static short RET=274;
public final static short UINTEGER=275;
public final static short SINGLE=276;
public final static short REPEAT=277;
public final static short UNTIL=278;
public final static short PAIR=279;
public final static short GOTO=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    2,    2,    2,    2,    2,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
   12,   12,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,   14,   14,   14,   15,   15,   15,
    5,   16,   13,   13,    6,    6,    6,    6,    6,    6,
    6,    6,   19,   18,   18,   18,   20,   20,   20,   20,
   20,   20,    7,    7,   17,   17,   17,   22,   22,   22,
   23,   23,   23,   21,    8,    8,    8,    8,    9,    9,
    9,    9,   11,   11,   11,   11,   24,   24,   25,   25,
   25,   10,   10,
};
final static short yylen[] = {                            2,
    4,    4,    1,    1,    2,    1,    1,    2,    2,    1,
    2,    2,    1,    2,    2,    2,    2,    2,    2,    1,
    1,    2,    3,    3,    3,    3,   10,   10,    9,    7,
    7,    2,    3,    6,    3,    3,    2,    2,    1,    2,
    4,    1,    1,    1,    7,    5,    7,    7,    9,    7,
    9,    7,    1,    3,    2,    1,    1,    1,    1,    1,
    1,    1,    4,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    4,    4,    4,    2,    4,    8,    6,
    3,    5,    4,    4,    4,    3,    3,    3,    3,    3,
    2,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   10,    0,    0,    0,    0,
    0,   43,   44,    0,    0,    0,    4,    6,    7,    0,
    0,   13,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,   72,    0,    0,    0,    0,    0,   70,   77,
    0,   32,    0,    0,    0,   93,    0,    2,    5,    9,
    8,   12,   11,   15,   14,   17,   16,   19,   18,    0,
    0,    0,    0,    0,    1,    0,   73,   87,    0,    0,
   60,   61,   62,    0,    0,   57,   58,   59,    0,    0,
    0,    0,    0,    0,    0,    0,   33,    0,    0,   81,
   21,    0,   92,   26,   37,   25,    0,    0,    0,   24,
   23,   86,    0,    0,   88,   64,   63,    0,    0,    0,
    0,    0,    0,    0,    0,   68,   69,   78,   76,   75,
    0,   41,    0,   22,    0,   36,    0,    0,   85,    0,
    0,   91,   84,   83,   74,    0,    0,    0,   46,    0,
   82,    0,   39,    0,    0,    0,    0,   90,    0,    0,
    0,    0,   34,    0,    0,   80,   40,   38,    0,    0,
   48,   47,    0,   45,   52,   50,   31,   30,    0,    0,
    0,    0,   79,    0,    0,    0,   51,   49,   29,    0,
   28,   27,
};
final static short yydgoto[] = {                          3,
  174,   17,   18,   19,   20,   21,   22,   23,   24,   25,
   26,  114,   27,   62,  145,  175,   35,   36,  115,   80,
   67,   38,   39,   28,  132,
};
final static short yysindex[] = {                       -99,
 -244, -224,    0,  -75,  -75,    0,  -30,  -28,    2, -169,
   48,    0,    0, -165, -150, -129,    0,    0,    0,  -49,
  -42,    0,  -13,    6,    7,    0, -139,  -29, -103,  -54,
 -122,   99,    0,  -54,   24, -116,    0,   65,    0,    0,
   43,    0,  -19,  -54, -148,    0,   91,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   36,
  -47,   11,  -63,  -94,    0,   38,    0,    0,  -54,  134,
    0,    0,    0,  -54,  -54,    0,    0,    0,   -8,  -54,
   39,  -54,  -54,  143,  148,  135,    0,  -40,  142,    0,
    0,   46,    0,    0,    0,    0,  -51,  198,  202,    0,
    0,    0,   30,   12,    0,    0,    0,  264,   -7,   65,
   65,   -8,  -20,   57, -154,    0,    0,    0,    0,    0,
  214,    0, -145,    0,  -39,    0, -156, -156,    0,  -54,
    8,    0,    0,    0,    0,   41,   57,   57,    0,  -44,
    0,  -18,    0,   15,  223,  239,    8,    0,   50, -118,
   63,   66,    0,   13,  -54,    0,    0,    0,   40,   45,
    0,    0,   57,    0,    0,    0,    0,    0,  286,  -75,
  -75,  -91,    0,  -75,   69,   70,    0,    0,    0,   33,
    0,    0,
};
final static short yyrindex[] = {                         0,
  335,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,  -34,  -12,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -25,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -5,
   17,    4,    0, -100,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   71,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  320,   14,   23,    0,    0,    0,    0,    0,    0,    0,
    0,  296,   -6,  245,  215,  173,   61,  -11,  -38,    0,
   56,  255,  249,    0,   -4,
};
final static int YYTABLESIZE=344;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
   71,   71,   71,   71,   97,   71,   56,   73,   73,   51,
   73,   34,   73,   31,   64,   55,   53,   71,   71,   71,
   71,  155,   70,    4,   56,   73,   73,   73,   67,   49,
   67,   67,   67,   55,   74,   65,   75,   65,   65,   65,
   88,   41,   49,    5,   54,   55,   67,   67,   67,   67,
   74,  130,   75,   65,   65,   65,   65,   66,  104,   66,
   66,   66,   54,   37,   57,   59,   74,   91,   75,  101,
  134,  168,   74,  130,   75,   66,   66,   66,   66,   97,
   74,  121,   75,   76,   78,   77,   42,   44,  129,   37,
   66,  182,   35,   89,   96,   79,  107,  150,  151,  152,
  143,   86,   45,   91,   89,   46,   82,   90,    7,   43,
  141,   83,  138,   47,  124,  139,    8,   60,   12,   13,
  144,  144,    9,  103,  172,  148,    6,    7,   14,  108,
  156,   15,  142,   61,   68,    8,  124,  162,   69,   48,
  112,    9,   10,  169,   11,   12,   13,   14,  163,   81,
   15,  164,    6,    7,   93,   53,    1,    2,   91,   91,
   91,    8,  105,  131,  177,   65,   53,    9,   10,   53,
   11,   12,   13,   14,  109,  120,   15,   74,  178,   75,
    6,    7,  122,  118,   74,   91,   75,   49,  119,    8,
  147,  131,  102,   32,   33,    9,   10,   37,   11,   12,
   13,   14,   32,   33,   15,  125,   50,  131,   98,   99,
   37,  153,  154,   52,   71,   71,   71,   95,   71,   71,
   71,   56,   73,   73,   71,   73,   73,   73,   32,   33,
   55,   56,   30,   63,   12,   13,   87,  127,   32,   33,
   55,  128,   54,   67,   67,   67,  137,   67,   67,   67,
   65,   65,   65,   67,   65,   65,   65,   40,  136,   54,
   65,   56,   58,  159,   32,   33,  100,  133,  167,   54,
  157,  158,   66,   66,   66,  140,   66,   66,   66,  160,
   32,   33,   66,   71,   72,   73,   32,   33,  181,   35,
   89,   94,   95,  106,  113,    7,  149,    7,   84,   32,
   33,   85,    7,    8,  135,    8,   74,  170,   75,    9,
    8,    9,  171,    7,  123,   14,    9,   14,   15,  161,
   15,    8,   14,   16,   29,   15,  173,    9,  110,  111,
  116,  117,  165,   14,    3,  166,   15,  179,  180,   42,
   92,  126,  146,  176,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   44,   47,   41,   42,   43,   59,
   45,   40,   47,   44,   44,   41,   59,   59,   60,   61,
   62,   40,   34,  268,   59,   60,   61,   62,   41,   16,
   43,   44,   45,   59,   43,   41,   45,   43,   44,   45,
   60,   40,   29,  268,   41,   59,   59,   60,   61,   62,
   43,   44,   45,   59,   60,   61,   62,   41,   63,   43,
   44,   45,   59,    8,   59,   59,   43,   45,   45,   59,
   59,   59,   43,   44,   45,   59,   60,   61,   62,   44,
   43,   88,   45,   60,   61,   62,  256,   40,   59,   34,
   30,   59,   59,   59,   59,   35,   59,  136,  137,  138,
  257,   41,  268,   81,   44,  256,   42,  256,  257,  279,
  256,   47,  267,  264,   92,  270,  265,  257,  275,  276,
  127,  128,  271,   63,  163,  130,  256,  257,  277,   69,
  142,  280,  278,  273,  257,  265,  114,  256,   40,  269,
   80,  271,  272,  155,  274,  275,  276,  277,  267,  266,
  280,  270,  256,  257,   64,  256,  256,  257,  136,  137,
  138,  265,  257,  103,  256,  269,  267,  271,  272,  270,
  274,  275,  276,  277,   41,   41,  280,   43,  270,   45,
  256,  257,   41,   41,   43,  163,   45,  174,   41,  265,
  130,  131,  256,  257,  258,  271,  272,  142,  274,  275,
  276,  277,  257,  258,  280,  257,  256,  147,  256,  257,
  155,  256,  257,  256,  256,  257,  258,  257,  260,  261,
  262,  256,  257,  258,  266,  260,  261,  262,  257,  258,
  256,  266,  263,  263,  275,  276,  256,   40,  257,  258,
  266,   40,  256,  256,  257,  258,  267,  260,  261,  262,
  256,  257,  258,  266,  260,  261,  262,  256,  266,  256,
  266,  256,  256,   41,  257,  258,  256,  256,  256,  266,
  256,  257,  256,  257,  258,   62,  260,  261,  262,   41,
  257,  258,  266,  260,  261,  262,  257,  258,  256,  256,
  256,  256,  257,  256,  256,  257,  256,  257,  256,  257,
  258,  259,  257,  265,   41,  265,   43,  268,   45,  271,
  265,  271,  268,  257,  269,  277,  271,  277,  280,  270,
  280,  265,  277,    4,    5,  280,   41,  271,   74,   75,
   82,   83,  270,  277,    0,  270,  280,  269,  269,  269,
   45,   97,  128,  171,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ID","CTE","CHARCH","NEQ","LEQ","MEQ","ASSIGN",
"TAG","IF","THEN","ELSE","BEGIN","END","END_IF","OUTF","TYPEDEF","FUN","RET",
"UINTEGER","SINGLE","REPEAT","UNTIL","PAIR","GOTO",
};
final static String yyrule[] = {
"$accept : prog",
"prog : ID BEGIN statement_list END",
"prog : error BEGIN statement_list END",
"prog : error",
"statement_list : statement",
"statement_list : statement_list statement",
"statement : executable_statement",
"statement : declare_statement",
"statement : return_statement ';'",
"statement : return_statement error",
"statement : error",
"executable_statement : if_statement ';'",
"executable_statement : if_statement error",
"executable_statement : assign_statement",
"executable_statement : outf_statement ';'",
"executable_statement : outf_statement error",
"executable_statement : repeat_statement ';'",
"executable_statement : repeat_statement error",
"executable_statement : goto_statement ';'",
"executable_statement : goto_statement error",
"executable_statement : mult_assign_statement",
"executable_statement_list : executable_statement",
"executable_statement_list : executable_statement_list executable_statement",
"declare_statement : var_type var_list ';'",
"declare_statement : var_type var_list error",
"declare_statement : var_type ID ';'",
"declare_statement : var_type ID error",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END ';'",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END error",
"declare_statement : var_type FUN error '(' parametro ')' BEGIN fun_body END",
"declare_statement : TYPEDEF PAIR '<' var_type '>' ID ';'",
"declare_statement : TYPEDEF PAIR '<' var_type '>' ID error",
"declare_statement : TYPEDEF error",
"declare_statement : TYPEDEF PAIR error",
"declare_statement : TYPEDEF PAIR '<' var_type '>' error",
"var_list : ID ',' ID",
"var_list : ID ',' var_list",
"var_list : ID ID",
"parametro : var_type ID",
"parametro : ID",
"parametro : var_type error",
"return_statement : RET '(' expr ')'",
"fun_body : statement_list",
"var_type : UINTEGER",
"var_type : SINGLE",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement END_IF",
"if_statement : IF cond THEN ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement error",
"if_statement : IF '(' cond ')' THEN error END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error",
"if_statement : IF cond THEN error ELSE ctrl_block_statement END_IF",
"ctrl_block_statement : executable_statement_list",
"cond : expr cond_op expr",
"cond : expr expr",
"cond : fun_invoc",
"cond_op : '<'",
"cond_op : '>'",
"cond_op : '='",
"cond_op : NEQ",
"cond_op : LEQ",
"cond_op : MEQ",
"assign_statement : ID ASSIGN expr ';'",
"assign_statement : ID ASSIGN expr error",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : term",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"fact : ID",
"fact : CTE",
"fact : fun_invoc",
"fun_invoc : ID '(' expr ')'",
"outf_statement : OUTF '(' expr ')'",
"outf_statement : OUTF '(' CHARCH ')'",
"outf_statement : OUTF error",
"outf_statement : OUTF '(' error ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL cond",
"repeat_statement : REPEAT BEGIN error",
"repeat_statement : REPEAT BEGIN executable_statement_list END error",
"mult_assign_statement : id_list ASSIGN expr_list ';'",
"mult_assign_statement : id_list ASSIGN expr_list error",
"mult_assign_statement : id_list ASSIGN expr ';'",
"mult_assign_statement : id_list ASSIGN error",
"id_list : ID ',' ID",
"id_list : id_list ',' ID",
"expr_list : expr ',' expr",
"expr_list : expr ',' expr_list",
"expr_list : expr expr_list",
"goto_statement : GOTO TAG '@'",
"goto_statement : GOTO error",
};

//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 19 "gramatica.y"
{System.println.out("Error: Falta el nombre del programa en la primer linea")}
break;
case 3:
//#line 20 "gramatica.y"
{System.println.out("Error: Falta delimitador de programa")}
break;
case 9:
//#line 34 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 10:
//#line 35 "gramatica.y"
{System.println.out("Error: sintaxis de sentencia incorrecta")}
break;
case 12:
//#line 44 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 15:
//#line 47 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 17:
//#line 49 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 19:
//#line 51 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 24:
//#line 63 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 26:
//#line 65 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 28:
//#line 67 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 29:
//#line 68 "gramatica.y"
{System.println.out("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number)}
break;
case 31:
//#line 70 "gramatica.y"
{Sysmtem.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 32:
//#line 71 "gramatica.y"
{System.println.out("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number)}
break;
case 33:
//#line 72 "gramatica.y"
{System.println.out("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number)}
break;
case 34:
//#line 73 "gramatica.y"
{System.println.out("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number)}
break;
case 37:
//#line 84 "gramatica.y"
{
        yyerror("Error: se espera ',' luego de cada variable, línea " + AnalizadorLexico.line_number);
        yyclearin;
      }
break;
case 39:
//#line 94 "gramatica.y"
{System.println.out("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number)}
break;
case 40:
//#line 95 "gramatica.y"
{System.println.out("Error: se esperaba nombre de parametro")}
break;
case 46:
//#line 119 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 47:
//#line 120 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 48:
//#line 121 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number)}
break;
case 50:
//#line 123 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 51:
//#line 124 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 52:
//#line 125 "gramatica.y"
{Sysmtem.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
break;
case 55:
//#line 139 "gramatica.y"
{System.println.out("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number)}
break;
case 64:
//#line 154 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number)}
break;
case 77:
//#line 198 "gramatica.y"
{System.println.out("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number)}
break;
case 78:
//#line 199 "gramatica.y"
{System.println.out("Error: se tipo de parametro incorrecto en linea "+AnalizadorLexico.line_number)}
break;
case 80:
//#line 204 "gramatica.y"
{System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
break;
case 81:
//#line 205 "gramatica.y"
{System.println.out("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number)}
break;
case 82:
//#line 206 "gramatica.y"
{System.println.out("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number)}
break;
case 84:
//#line 211 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number)}
break;
case 86:
//#line 213 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number)}
break;
case 91:
//#line 224 "gramatica.y"
{System.println.out("Error: se epseraba ',' luego de la expresion en linea "+AnalizadorLexico.line_number)}
break;
case 93:
//#line 229 "gramatica.y"
{System.println.out("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number)}
break;
//#line 709 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

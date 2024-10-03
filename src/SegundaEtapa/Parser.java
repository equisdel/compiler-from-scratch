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
    4,    4,    4,    4,    4,    4,   14,   14,   14,   15,
   15,   15,    5,   16,   13,   13,    6,    6,    6,    6,
    6,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    6,    6,   19,   18,   18,   21,   21,   21,   21,   21,
   21,    7,    7,   17,   17,   17,   17,   22,   22,   22,
   23,   23,   23,   20,   20,    8,    8,    8,    9,    9,
    9,    9,    9,    9,    9,    9,   11,   24,   24,   25,
   25,   25,   10,   10,
};
final static short yylen[] = {                            2,
    4,    4,    1,    1,    2,    1,    1,    2,    2,    2,
    2,    2,    1,    2,    2,    1,    2,    2,    2,    2,
    1,    2,    3,    3,    3,    3,   10,   10,    9,   11,
   10,    7,    7,    2,    3,    6,    3,    3,    2,    2,
    1,    2,    4,    1,    1,    1,    7,    5,    7,    7,
    9,    7,    9,    7,    7,    5,    7,    7,    9,    7,
    9,    7,    1,    3,    2,    1,    1,    1,    1,    1,
    1,    4,    4,    3,    3,    1,    1,    3,    3,    1,
    1,    1,    1,    4,    6,    4,    4,    2,    9,    9,
    6,    9,    9,    7,    3,    5,    3,    3,    3,    3,
    2,    3,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   45,   46,    0,    0,    0,    4,    6,    7,    0,
    0,   13,    0,   16,    0,    0,    0,    0,    0,   10,
    0,    0,   77,    0,   82,    0,    0,    0,    0,    0,
   80,   88,    0,   34,    0,    0,    0,  104,    0,    2,
    5,    9,    8,   12,   11,   15,   14,   18,   17,   20,
   19,    0,    0,    0,    0,    0,    1,    0,   83,   98,
    0,    0,    0,   69,   70,   71,   66,   67,   68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
    0,    0,   95,   21,    0,  103,   26,   39,   25,    0,
    0,    0,   24,   23,    0,    0,   99,   73,   72,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   78,   79,   87,   86,    0,   43,    0,   22,    0,   38,
    0,    0,    0,    0,    0,   84,    0,    0,    0,    0,
    0,   48,    0,    0,   56,    0,   96,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   36,    0,    0,   91,    0,   42,
   40,    0,    0,    0,    0,   85,   50,   49,    0,   47,
   58,   57,    0,   55,   54,   52,   62,   60,   33,   32,
    0,    0,   94,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   53,   51,   61,   59,
   90,   89,   93,   92,   29,    0,    0,    0,   31,   28,
   27,    0,   30,
};
final static short yydgoto[] = {                          3,
  202,   17,   94,   19,   20,   21,   22,   23,   24,   25,
   26,  117,   27,   64,  151,  203,   37,   38,  118,   69,
   83,   40,   41,   28,  106,
};
final static short yysindex[] = {                      -168,
 -229, -194,    0,  139,  139,   23,  -36,   59,  -33, -241,
   58,    0,    0, -146, -207,  -90,    0,    0,    0,  -50,
  -47,    0,  -45,    0,  -43,  -42, -225,  -31,  117,    0,
   84, -153,    0,  107,    0,   84,  110, -135, -104,   68,
    0,    0,  -99,    0,  -13,   84,  -88,    0,  109,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   41,  100,  -18,   84,  -62,    0,  -19,    0,    0,
   84,  158,  163,    0,    0,    0,    0,    0,    0,  119,
  119,  -12,   84,  161,  168,  119,  119,  169,   89,    0,
  104,  135,    0,    0, -136,    0,    0,    0,    0,  -35,
  207,  222,    0,    0,   81,  224,    0,    0,    0,  153,
   16,   63,   68,   68,  -12,    5,  192, -127,   40, -116,
    0,    0,    0,    0,  262,    0, -219,    0,  -39,    0,
 -139, -148,   84,  -12,   84,    0,   84,  179,  190,  192,
  192,    0,  192,  192,    0,  142,    0,   62,    0,  144,
  305,  310,  147,  -12,  -12,  162,   92, -184,  108, -183,
  113,  115,  120,  132,    0,   -4,   84,    0,  328,    0,
    0,  152,  159,  160,  371,    0,    0,    0,  192,    0,
    0,    0,  192,    0,    0,    0,    0,    0,    0,    0,
  388,  389,    0,  139,  139,  139,  166, -214, -189,    1,
    7,  139,  171,  173,  182,  139,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  372,   31,  183,    0,    0,
    0,  378,    0,
};
final static short yyrindex[] = {                         0,
  453,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,  103,  -16,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  103,    0,    0,    0,    0,    0,    0,    0,
    0,   56,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,    9,   34,   78,    0, -164,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   46,    0,
    0,    0,    0,   -8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   17,   21,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  103,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  103,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  185,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  401,   91,    6,    0,    0,    0,    0,    0,    0,    0,
    0,  411,  -68,  360,  330,  -57,  338,   -6,  -27,   26,
    0,  327,  336,    0,    0,
};
final static int YYTABLESIZE=475;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         81,
   81,   81,   81,   81,  100,   81,   43,   32,   53,   18,
   18,   55,   66,   57,   44,   59,   61,   81,   81,   81,
   81,   18,  125,   80,   76,   81,   76,   76,   76,   72,
   80,   62,   81,   39,   18,  101,  147,   45,    4,  109,
  104,  207,   76,   76,   76,   76,   91,   63,   48,   74,
  101,   74,   74,   74,  190,  208,   49,  120,  148,  212,
  100,   73,  150,  150,  102,  214,  209,   74,   74,   74,
   74,  178,  182,    5,   75,  100,   75,   75,   75,  102,
  210,   30,  179,  183,  100,  180,  184,    1,    2,  221,
   97,   63,   75,   75,   75,   75,   65,   46,   36,   99,
  128,  167,   63,   70,   37,   63,   51,  152,  149,   86,
  158,  160,  161,  162,   87,  163,  164,  149,   64,   51,
    7,   47,  128,   80,  133,   81,   12,   13,    8,  124,
   84,   80,  127,   81,    9,   12,   13,  204,  205,  141,
   14,  168,  142,   15,   83,   83,   71,   83,  218,   83,
  144,  198,   80,  145,   81,  199,   33,   34,   35,   88,
  191,   85,   83,   83,   83,    6,    7,   93,    7,   77,
   79,   78,   96,  169,    8,  126,    8,   80,   50,   81,
    9,   10,    9,   11,   12,   13,   14,  174,   14,   15,
  175,   15,  192,  136,  107,   80,  137,   81,  111,   18,
   18,   18,  176,  112,   80,   52,   81,   18,   54,  123,
   56,   18,   58,   60,   81,   81,   81,   98,   81,   81,
   81,  129,   42,   81,   81,   81,   31,   81,   81,   81,
   81,   65,   81,   81,   81,   81,  108,  103,   81,   76,
   76,   76,   90,   76,   76,   76,  131,  101,   76,   76,
   76,  189,   76,   76,   76,   76,  211,   76,   76,   76,
   76,  132,  213,   76,   74,   74,   74,  135,   74,   74,
   74,  140,  100,   74,   74,   74,  102,   74,   74,   74,
   74,  138,   74,   74,   74,   74,  220,   97,   74,   75,
   75,   75,   51,   75,   75,   75,   97,   98,   75,   75,
   75,   37,   75,   75,   75,   75,  143,   75,   75,   75,
   75,   65,   65,   75,   33,   34,   35,   33,   34,   35,
   65,   65,   65,  146,   65,   65,   65,   65,  139,   65,
   65,   65,   65,   64,   64,   65,   33,   34,   35,   33,
   34,   35,   64,   64,   64,  172,   64,   64,   64,   64,
  173,   64,   64,   64,   64,  101,  102,   64,   83,   83,
   83,  177,   83,   83,   83,   33,   34,   35,   68,   74,
   75,   76,    6,    7,   82,   34,   35,  181,   12,   13,
   89,    8,  185,   92,  186,   67,  193,    9,   10,  187,
   11,   12,   13,   14,    6,    7,   15,  165,  166,  170,
  171,  188,  105,    8,   16,   29,  113,  114,  110,    9,
   10,  197,   11,   12,   13,   14,  116,    7,   15,  194,
  115,  121,  122,  119,    7,    8,  195,  196,  200,  201,
  219,    9,    8,  206,  157,    7,  223,   14,    9,  215,
   15,  216,  134,    8,   14,  159,    7,   15,    7,    9,
  217,  222,    3,   44,    8,   14,    8,   95,   15,  130,
    9,  153,    9,    0,    0,    0,   14,    0,   14,   15,
  154,   15,  155,    0,  156,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   44,   47,   40,   44,   59,    4,
    5,   59,   44,   59,  256,   59,   59,   59,   60,   61,
   62,   16,   91,   43,   41,   45,   43,   44,   45,   36,
   43,  257,   45,    8,   29,   44,  256,  279,  268,   59,
   59,  256,   59,   60,   61,   62,   60,  273,  256,   41,
   59,   43,   44,   45,   59,  270,  264,   85,  278,   59,
   44,   36,  131,  132,   44,   59,  256,   59,   60,   61,
   62,  256,  256,  268,   41,   59,   43,   44,   45,   59,
  270,   59,  267,  267,   44,  270,  270,  256,  257,   59,
   59,  256,   59,   60,   61,   62,   41,   40,   40,   59,
   95,   40,  267,  257,   59,  270,   16,  256,  257,   42,
  138,  139,  140,  141,   47,  143,  144,  257,   41,   29,
  257,  268,  117,   43,   44,   45,  275,  276,  265,   41,
  266,   43,  269,   45,  271,  275,  276,  195,  196,  267,
  277,  148,  270,  280,   42,   43,   40,   45,  206,   47,
  267,  179,   43,  270,   45,  183,  256,  257,  258,  259,
  167,  266,   60,   61,   62,  256,  257,  256,  257,   60,
   61,   62,   64,  148,  265,   41,  265,   43,  269,   45,
  271,  272,  271,  274,  275,  276,  277,   41,  277,  280,
   44,  280,  167,   41,  257,   43,   44,   45,   41,  194,
  195,  196,   41,   41,   43,  256,   45,  202,  256,   41,
  256,  206,  256,  256,  256,  257,  258,  257,  260,  261,
  262,  257,  256,  265,  266,  267,  263,  269,  270,  271,
  272,  263,  274,  275,  276,  277,  256,  256,  280,  256,
  257,  258,  256,  260,  261,  262,   40,  256,  265,  266,
  267,  256,  269,  270,  271,  272,  256,  274,  275,  276,
  277,   40,  256,  280,  256,  257,  258,   44,  260,  261,
  262,  267,  256,  265,  266,  267,  256,  269,  270,  271,
  272,  266,  274,  275,  276,  277,  256,  256,  280,  256,
  257,  258,  202,  260,  261,  262,  256,  257,  265,  266,
  267,  256,  269,  270,  271,  272,  267,  274,  275,  276,
  277,  256,  257,  280,  256,  257,  258,  256,  257,  258,
  265,  266,  267,   62,  269,  270,  271,  272,  266,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  256,
  257,  258,  265,  266,  267,   41,  269,  270,  271,  272,
   41,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  270,  260,  261,  262,  256,  257,  258,   31,  260,
  261,  262,  256,  257,   37,  257,  258,  270,  275,  276,
   43,  265,  270,   46,  270,  269,   59,  271,  272,  270,
  274,  275,  276,  277,  256,  257,  280,  256,  257,  256,
  257,  270,   65,  265,    4,    5,   80,   81,   71,  271,
  272,   41,  274,  275,  276,  277,  256,  257,  280,  268,
   83,   86,   87,  256,  257,  265,  268,  268,   41,   41,
   59,  271,  265,  268,  256,  257,   59,  277,  271,  269,
  280,  269,  105,  265,  277,  256,  257,  280,  257,  271,
  269,  269,    0,  269,  265,  277,  265,   47,  280,  100,
  271,  132,  271,   -1,   -1,   -1,  277,   -1,  277,  280,
  133,  280,  135,   -1,  137,
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
"statement : error ';'",
"executable_statement : if_statement ';'",
"executable_statement : if_statement error",
"executable_statement : assign_statement",
"executable_statement : outf_statement ';'",
"executable_statement : outf_statement error",
"executable_statement : repeat_statement",
"executable_statement : goto_statement ';'",
"executable_statement : goto_statement error",
"executable_statement : mult_assign_statement ';'",
"executable_statement : mult_assign_statement error",
"executable_statement_list : executable_statement",
"executable_statement_list : executable_statement_list executable_statement",
"declare_statement : var_type var_list ';'",
"declare_statement : var_type var_list error",
"declare_statement : var_type ID ';'",
"declare_statement : var_type ID error",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END ';'",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END error",
"declare_statement : var_type FUN error '(' parametro ')' BEGIN fun_body END",
"declare_statement : var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END ';'",
"declare_statement : var_type FUN ID '(' error ')' BEGIN fun_body END ';'",
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
"if_statement : IF '(' fun_invoc ')' THEN ctrl_block_statement END_IF",
"if_statement : IF fun_invoc THEN ctrl_block_statement END_IF",
"if_statement : IF '(' fun_invoc ')' THEN ctrl_block_statement error",
"if_statement : IF '(' fun_invoc ')' THEN error END_IF",
"if_statement : IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF fun_invoc THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' fun_invoc ')' THEN ctrl_block_statement ELSE ctrl_block_statement error",
"if_statement : IF fun_invoc THEN error ELSE ctrl_block_statement END_IF",
"ctrl_block_statement : executable_statement_list",
"cond : expr cond_op expr",
"cond : expr expr",
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
"expr : error",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"fact : ID",
"fact : CTE",
"fact : fun_invoc",
"fun_invoc : ID '(' expr ')'",
"fun_invoc : ID '(' expr ',' expr ')'",
"outf_statement : OUTF '(' expr ')'",
"outf_statement : OUTF '(' CHARCH ')'",
"outf_statement : OUTF error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')' ';'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')' error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL cond",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')' ';'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')' error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL fun_invoc ';'",
"repeat_statement : REPEAT BEGIN error",
"repeat_statement : REPEAT BEGIN executable_statement_list END error",
"mult_assign_statement : id_list ASSIGN expr_list",
"id_list : ID ',' ID",
"id_list : id_list ',' ID",
"expr_list : expr ',' expr",
"expr_list : expr expr",
"expr_list : expr_list ',' expr",
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
//#line 20 "gramatica.y"
{System.println.out("Error: Falta el nombre del programa en la primer linea")}
break;
case 3:
//#line 21 "gramatica.y"
{System.println.out("Error: Falta delimitador de programa")}
break;
case 9:
//#line 34 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 10:
//#line 35 "gramatica.y"
{System.println.out("Error: sintaxis de sentencia incorrecta")}
break;
case 12:
//#line 44 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 15:
//#line 47 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 18:
//#line 50 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 20:
//#line 52 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 24:
//#line 63 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 26:
//#line 65 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 28:
//#line 67 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 29:
//#line 68 "gramatica.y"
{System.println.out("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number)}
break;
case 30:
//#line 69 "gramatica.y"
{System.println.out("Error: ',' invalida en linea "+AnalizadorLexico.line_number)}
break;
case 31:
//#line 70 "gramatica.y"
{System.println.out("Error:  linea "+AnalizadorLexico.line_number)}
break;
case 33:
//#line 72 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 34:
//#line 73 "gramatica.y"
{System.println.out("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number)}
break;
case 35:
//#line 74 "gramatica.y"
{System.println.out("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number)}
break;
case 36:
//#line 75 "gramatica.y"
{System.println.out("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number)}
break;
case 39:
//#line 86 "gramatica.y"
{
        yyerror("Error: se espera ',' luego de cada variable, línea " + AnalizadorLexico.line_number);
        yyclearin;
      }
break;
case 41:
//#line 96 "gramatica.y"
{System.println.out("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number)}
break;
case 42:
//#line 97 "gramatica.y"
{System.println.out("Error: se esperaba nombre de parametro")}
break;
case 48:
//#line 121 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 49:
//#line 122 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 50:
//#line 123 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number)}
break;
case 52:
//#line 125 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 53:
//#line 126 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 54:
//#line 127 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
break;
case 56:
//#line 130 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 57:
//#line 131 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 58:
//#line 132 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number)}
break;
case 60:
//#line 134 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 61:
//#line 135 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 62:
//#line 136 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
break;
case 65:
//#line 150 "gramatica.y"
{System.println.out("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number)}
break;
case 73:
//#line 165 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number)}
break;
case 85:
//#line 204 "gramatica.y"
{System.println.out ("Error: no se puede pasar mas de 1 parametro a una funcion, linea "+AnalizadorLexico.line_number)}
break;
case 88:
//#line 210 "gramatica.y"
{System.println.out("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number)}
break;
case 90:
//#line 216 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 91:
//#line 217 "gramatica.y"
{System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
break;
case 92:
//#line 218 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 93:
//#line 219 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 94:
//#line 220 "gramatica.y"
{System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
break;
case 95:
//#line 221 "gramatica.y"
{System.println.out("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number)}
break;
case 96:
//#line 222 "gramatica.y"
{System.println.out("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number)}
break;
case 101:
//#line 237 "gramatica.y"
{System.println.out("Error: se esperaba una ',' entre las expresiones en linea "+AnalizadorLexico.line_number)}
break;
case 104:
//#line 243 "gramatica.y"
{System.println.out("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number)}
break;
//#line 800 "Parser.java"
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

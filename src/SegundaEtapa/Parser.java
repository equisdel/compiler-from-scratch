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



package SegundaEtapa;



//#line 3 "gramatica.y"
        import java.io.*;
        import PrimeraEtapa.AnalizadorLexico;
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
    3,   12,   12,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,    4,    4,   14,   14,   14,
   15,   15,   15,    5,   16,   13,   13,   13,    6,    6,
    6,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    6,    6,    6,    6,   19,   18,   18,   21,   21,   21,
   21,   21,   21,    7,    7,    7,    7,   17,   17,   17,
   17,   23,   23,   23,   24,   24,   24,   24,   24,   22,
   20,   20,    8,    8,    8,    9,    9,    9,    9,    9,
    9,    9,    9,   11,   11,   25,   25,   27,   27,   26,
   26,   10,   10,
};
final static short yylen[] = {                            2,
    4,    4,    1,    1,    2,    1,    1,    2,    2,    2,
    2,    2,    1,    2,    2,    1,    2,    2,    2,    2,
    1,    1,    2,    3,    3,    3,    3,   10,   10,    9,
   11,   10,    7,    7,    2,    3,    6,    3,    3,    2,
    2,    1,    2,    4,    1,    1,    1,    1,    7,    5,
    7,    7,    9,    7,    9,    7,    7,    5,    7,    7,
    9,    7,    9,    7,    1,    3,    3,    1,    1,    1,
    1,    1,    1,    4,    4,    4,    4,    3,    3,    1,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    4,
    4,    6,    4,    4,    2,    9,    9,    6,    9,    9,
    7,    3,    5,    3,    3,    3,    3,    1,    1,    3,
    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   21,    0,    0,
    0,    0,   46,   47,    0,    0,    0,    4,    6,    7,
    0,    0,   13,    0,   16,    0,    0,    0,    0,    0,
    0,    0,   10,    0,    0,   81,    0,   86,    0,    0,
    0,    0,    0,   89,    0,   84,   95,    0,   35,    0,
    0,    0,  113,  112,    2,    5,    9,    8,   12,   11,
   15,   14,   18,   17,   20,   19,    0,    0,    0,    0,
    0,    0,    0,    1,    0,   88,    0,    0,    0,    0,
   87,    0,   71,   72,   73,   68,   69,   70,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   36,    0,    0,
  102,    0,   22,    0,   27,   40,   26,    0,    0,    0,
   25,   24,    0,    0,    0,    0,    0,  109,  107,  106,
   76,   74,   90,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   82,   83,   94,   93,   48,
    0,   44,    0,   23,    0,   39,    0,    0,   77,   75,
    0,    0,   91,    0,    0,    0,    0,    0,   50,    0,
    0,   58,    0,  103,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   37,    0,    0,   98,    0,   43,   41,    0,    0,
    0,    0,   92,   52,   51,    0,   49,   60,   59,    0,
   57,   56,   54,   64,   62,   34,   33,    0,    0,  101,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   55,   53,   63,   61,   97,   96,  100,
   99,   30,    0,    0,    0,   32,   29,   28,    0,   31,
};
final static short yydgoto[] = {                          3,
  219,   18,  103,   20,   21,   22,   23,   24,   25,   26,
   27,  132,   28,   69,  168,  220,   41,   42,  133,   76,
   91,   29,   45,   46,   30,  116,   31,
};
final static short yysindex[] = {                      -128,
 -236, -229,    0, -150, -150,  -10,  -93,    0,   50,  -12,
 -225,   21,    0,    0, -213,  -67,  -81,    0,    0,    0,
  -52,  -49,    0,  -48,    0,  -45,  -44, -223, -196,  -36,
   49,  197,    0,  112, -123,    0,  -23,    0,  112, -117,
   94, -118, -106,    0,   29,    0,    0,  105,    0,  109,
  112, -160,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  115,  -70,  -43,  112,
  117,  -84,  -84,    0,   15,    0,   52,  112,  151,  162,
    0,  112,    0,    0,    0,    0,    0,    0,  -40,  -40,
  112,  222,  247,  -40,  -40,  165,  344,    0, -108,  352,
    0,  -93,    0,  119,    0,    0,    0,  -17,  226,  244,
    0,    0,   32,    0,  367,  218,  225,    0,    0,    0,
    0,    0,    0,  357,  113,  120,  337,   29,   29,  337,
  125,  -99, -168,  137, -158,    0,    0,    0,    0,    0,
  362,    0, -227,    0,  -35,    0,  -75, -112,    0,    0,
  112,  112,    0,  112,  251,  285,  -99,  -99,    0,  -99,
  -99,    0,  -47,    0,  102,    0,  110,  365,  387,  161,
  337,  337,  364,  160, -211,  163, -178,  166,  167,  168,
  180,    0,   -2,  112,    0,  372,    0,    0,  164,  184,
  187,  394,    0,    0,    0,  -99,    0,    0,    0,  -99,
    0,    0,    0,    0,    0,    0,    0,  415,  424,    0,
 -150, -150, -150,  199, -162,  -85,   20,   42, -150,  201,
  206,  207, -150,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  421,   64,  212,    0,    0,    0,  423,    0,
};
final static short yyrindex[] = {                         0,
  483,    0,    0,    0,    0,    0,   36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  441,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,   91,    0,  -19,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  445,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -7,    0,   72,  -31,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   47,    3,   25,   69,
    0, -124,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   75,    0,    0,    0,    0,
   54,   59,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   91,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   91,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  221,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  411,   -5,  325,    0,    0,    0,    0,    0,    0,    0,
    0,  439,  -66,  384,  348, -140,  343,   -4,  288,   14,
    0,  369,  329,  328,    0,    0,  354,
};
final static int YYTABLESIZE=565;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         85,
   85,   85,   85,   85,   40,   85,   58,   72,  108,   60,
   62,   56,  108,   64,   66,  112,   78,   85,   85,   85,
   85,   80,   43,   80,   80,   80,   56,   48,  164,   35,
   49,    4,  141,   67,   79,   81,   81,   81,    5,   80,
   80,   80,   80,   78,  195,   78,   78,   78,   33,   68,
  165,  105,   80,   50,   52,  196,  207,   89,  197,   90,
   51,   78,   78,   78,   78,   79,   70,   79,   79,   79,
   94,  221,  222,  122,   89,   95,   90,  199,  229,  108,
  167,  167,  235,   79,   79,   79,   79,   67,  200,   39,
  150,  201,   73,  224,   40,  101,  102,  110,  158,   35,
  231,  159,  111,    8,    9,    6,    7,  225,  161,   66,
   10,  162,  110,    8,    9,   42,   15,  111,   42,   16,
   10,   11,  238,   12,   13,   14,   15,    1,    2,   16,
  104,   65,   88,   88,   77,   88,   89,   88,   90,   38,
   81,  184,   65,  169,  166,   65,   40,   92,  140,   40,
   88,   88,   88,   86,   88,   87,   40,  102,  108,   93,
  185,   40,   13,   14,    8,    9,   13,   14,   99,   34,
  226,   10,  117,  107,    6,    7,  123,   15,  186,  208,
   16,  166,    8,    9,  227,  109,  110,   55,   53,   10,
   11,  125,   12,   13,   14,   15,   54,  209,   16,   13,
   14,  191,  126,   57,  192,  138,   59,   61,  182,  183,
   63,   65,  111,   56,   85,   85,   37,   38,   85,   85,
   85,  106,   85,   85,   85,   85,   71,   85,   85,   85,
   85,  108,   85,   85,   85,   85,   80,   80,   85,  145,
   80,   80,   80,   47,   80,   80,   80,   80,  105,   80,
   80,   80,   80,  206,   80,   80,   80,   80,   78,   78,
   80,  152,   78,   78,   78,  147,   78,   78,   78,   78,
  121,   78,   78,   78,   78,  228,   78,   78,   78,   78,
   79,   79,   78,  148,   79,   79,   79,  149,   79,   79,
   79,   79,   48,   79,   79,   79,   79,  230,   79,   79,
   79,   79,   67,   67,   79,   36,   37,   38,   48,  110,
   67,   67,   67,   67,  111,   67,   67,   67,   67,  237,
   67,   67,   67,   67,   66,   66,   67,  104,   19,   19,
   48,   48,   66,   66,   66,   66,   38,   66,   66,   66,
   66,   19,   66,   66,   66,   66,   88,   35,   66,   82,
   88,   88,   88,   83,   84,   85,   19,   36,   37,   38,
   36,   37,   38,   96,   98,  187,  188,   36,   37,   38,
  105,  106,  114,   37,   38,  102,   75,   44,  155,   89,
  135,   90,    8,    9,  139,  156,   89,  143,   90,   10,
   97,  157,  142,  100,   89,   15,   90,  153,   16,   89,
  154,   90,   44,  160,  193,  189,   89,   44,   90,   89,
  151,   90,  113,  115,   17,   32,   44,  128,  129,   44,
  124,  136,  137,  163,  127,  119,  120,  190,  144,  194,
  210,  211,  198,  130,  214,  202,  203,  204,   44,   44,
  118,  118,  175,  177,  178,  179,   44,  180,  181,  205,
   44,  212,    6,    7,  213,  217,  144,   44,   44,   44,
    8,    9,   44,   44,  218,   74,  223,   10,   11,  232,
   12,   13,   14,   15,  233,  234,   16,  131,  102,  236,
  239,  240,    3,  215,  109,    8,    9,  216,  108,   45,
  104,  146,   10,  171,  172,  170,  173,    0,   15,    0,
    0,   16,  134,  102,    0,    0,  174,  102,    0,    0,
    8,    9,    0,    0,    8,    9,    0,   10,    0,   44,
   44,   10,   44,   15,    0,    0,   16,   15,    0,    0,
   16,    0,    0,   44,    0,   19,   19,   19,    0,    0,
  176,  102,    0,   19,    0,    0,    0,   19,    8,    9,
    0,    0,   44,    0,    0,   10,    0,    0,    0,    0,
    0,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   45,   47,   59,   44,   44,   59,
   59,   17,   44,   59,   59,   59,   40,   59,   60,   61,
   62,   41,    9,   43,   44,   45,   32,   40,  256,  123,
  256,  268,   99,  257,   39,   43,   44,   45,  268,   59,
   60,   61,   62,   41,  256,   43,   44,   45,   59,  273,
  278,   59,   39,  279,  268,  267,   59,   43,  270,   45,
   40,   59,   60,   61,   62,   41,  263,   43,   44,   45,
   42,  212,  213,   59,   43,   47,   45,  256,   59,   44,
  147,  148,  223,   59,   60,   61,   62,   41,  267,   40,
   59,  270,   44,  256,   45,  256,  257,   44,  267,  123,
   59,  270,   44,  264,  265,  256,  257,  270,  267,   41,
  271,  270,   59,  264,  265,   41,  277,   59,   44,  280,
  271,  272,   59,  274,  275,  276,  277,  256,  257,  280,
   59,  256,   42,   43,  258,   45,   43,   47,   45,   59,
  258,   40,  267,  256,  257,  270,   45,  266,  257,   45,
   60,   61,   62,   60,   61,   62,   45,  257,   44,  266,
  165,   45,  275,  276,  264,  265,  275,  276,   60,  263,
  256,  271,  257,   59,  256,  257,  125,  277,  165,  184,
  280,  257,  264,  265,  270,  256,  257,  269,  256,  271,
  272,   41,  274,  275,  276,  277,  264,  184,  280,  275,
  276,   41,   41,  256,   44,   41,  256,  256,  256,  257,
  256,  256,  256,  219,  256,  257,  257,  258,  260,  261,
  262,  257,  264,  265,  266,  267,  263,  269,  270,  271,
  272,  263,  274,  275,  276,  277,  256,  257,  280,  257,
  260,  261,  262,  256,  264,  265,  266,  267,  256,  269,
  270,  271,  272,  256,  274,  275,  276,  277,  256,  257,
  280,   44,  260,  261,  262,   40,  264,  265,  266,  267,
  256,  269,  270,  271,  272,  256,  274,  275,  276,  277,
  256,  257,  280,   40,  260,  261,  262,  256,  264,  265,
  266,  267,  257,  269,  270,  271,  272,  256,  274,  275,
  276,  277,  256,  257,  280,  256,  257,  258,  273,  256,
  264,  265,  266,  267,  256,  269,  270,  271,  272,  256,
  274,  275,  276,  277,  256,  257,  280,  256,    4,    5,
  256,  257,  264,  265,  266,  267,  256,  269,  270,  271,
  272,   17,  274,  275,  276,  277,  256,  123,  280,  256,
  260,  261,  262,  260,  261,  262,   32,  256,  257,  258,
  256,  257,  258,  259,  256,  256,  257,  256,  257,  258,
  256,  257,  256,  257,  258,  257,   34,    9,  266,   43,
   93,   45,  264,  265,   41,  266,   43,  269,   45,  271,
   48,  267,   41,   51,   43,  277,   45,   41,  280,   43,
   44,   45,   34,  267,   41,   41,   43,   39,   45,   43,
   44,   45,   70,   71,    4,    5,   48,   89,   90,   51,
   78,   94,   95,   62,   82,   72,   73,   41,  104,  270,
   59,  268,  270,   91,   41,  270,  270,  270,   70,   71,
   72,   73,  155,  156,  157,  158,   78,  160,  161,  270,
   82,  268,  256,  257,  268,   41,  132,   89,   90,   91,
  264,  265,   94,   95,   41,  269,  268,  271,  272,  269,
  274,  275,  276,  277,  269,  269,  280,  256,  257,   59,
  269,   59,    0,  196,   44,  264,  265,  200,   44,  269,
   52,  108,  271,  151,  152,  148,  154,   -1,  277,   -1,
   -1,  280,  256,  257,   -1,   -1,  256,  257,   -1,   -1,
  264,  265,   -1,   -1,  264,  265,   -1,  271,   -1,  151,
  152,  271,  154,  277,   -1,   -1,  280,  277,   -1,   -1,
  280,   -1,   -1,  165,   -1,  211,  212,  213,   -1,   -1,
  256,  257,   -1,  219,   -1,   -1,   -1,  223,  264,  265,
   -1,   -1,  184,   -1,   -1,  271,   -1,   -1,   -1,   -1,
   -1,  277,   -1,   -1,  280,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CHARCH","NEQ","LEQ","MEQ",
"ASSIGN","TAG","IF","THEN","ELSE","BEGIN","END","END_IF","OUTF","TYPEDEF","FUN",
"RET","UINTEGER","SINGLE","REPEAT","UNTIL","PAIR","GOTO",
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
"executable_statement : TAG",
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
"var_type : ID",
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
"cond : expr error expr",
"cond_op : '<'",
"cond_op : '>'",
"cond_op : '='",
"cond_op : NEQ",
"cond_op : LEQ",
"cond_op : MEQ",
"assign_statement : ID ASSIGN expr ';'",
"assign_statement : expr_pair ASSIGN expr ';'",
"assign_statement : ID ASSIGN expr error",
"assign_statement : expr_pair ASSIGN expr error",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : term",
"expr : error",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"fact : ID",
"fact : CTE",
"fact : '-' CTE",
"fact : fun_invoc",
"fact : expr_pair",
"expr_pair : ID '{' CTE '}'",
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
"mult_assign_statement : id_list ASSIGN error",
"id_list : elem_list ',' elem_list",
"id_list : id_list ',' elem_list",
"elem_list : ID",
"elem_list : expr_pair",
"expr_list : expr ',' expr",
"expr_list : expr_list ',' expr",
"goto_statement : GOTO TAG",
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
        yychar = AnalizadorLexico.yylex();
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
//#line 21 "gramatica.y"
{System.out.println("Error: Falta el nombre del programa en la primer linea");}
break;
case 3:
//#line 22 "gramatica.y"
{System.out.println("Error: Falta delimitador de programa");}
break;
case 9:
//#line 35 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 10:
//#line 36 "gramatica.y"
{System.out.println("Error: sintaxis de sentencia incorrecta");}
break;
case 12:
//#line 45 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 15:
//#line 48 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 18:
//#line 51 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 20:
//#line 53 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 25:
//#line 65 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 27:
//#line 67 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 29:
//#line 69 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 30:
//#line 70 "gramatica.y"
{System.out.println("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 31:
//#line 71 "gramatica.y"
{System.out.println("Error: ',' invalida en linea "+AnalizadorLexico.line_number);}
break;
case 32:
//#line 72 "gramatica.y"
{System.out.println("Error:  linea "+AnalizadorLexico.line_number);}
break;
case 34:
//#line 74 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 35:
//#line 75 "gramatica.y"
{System.out.println("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number);}
break;
case 36:
//#line 76 "gramatica.y"
{System.out.println("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number);}
break;
case 37:
//#line 77 "gramatica.y"
{System.out.println("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number);}
break;
case 40:
//#line 88 "gramatica.y"
{
        yyerror("Error: se espera ',' luego de cada variable, línea " + AnalizadorLexico.line_number);
        yyclearin;
      ;}
break;
case 42:
//#line 98 "gramatica.y"
{System.out.println("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number);}
break;
case 43:
//#line 99 "gramatica.y"
{System.out.println("Error: se esperaba nombre de parametro");}
break;
case 50:
//#line 123 "gramatica.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
break;
case 51:
//#line 124 "gramatica.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
break;
case 52:
//#line 125 "gramatica.y"
{System.out.println("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number);}
break;
case 54:
//#line 127 "gramatica.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
break;
case 55:
//#line 128 "gramatica.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
break;
case 56:
//#line 129 "gramatica.y"
{System.out.println("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number);}
break;
case 58:
//#line 132 "gramatica.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
break;
case 59:
//#line 133 "gramatica.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
break;
case 60:
//#line 134 "gramatica.y"
{System.out.println("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number);}
break;
case 62:
//#line 136 "gramatica.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number);}
break;
case 63:
//#line 137 "gramatica.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number);}
break;
case 64:
//#line 138 "gramatica.y"
{System.out.println("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number);}
break;
case 67:
//#line 152 "gramatica.y"
{System.out.println("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number);}
break;
case 76:
//#line 168 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number);}
break;
case 77:
//#line 169 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number);}
break;
case 92:
//#line 215 "gramatica.y"
{System.out.println ("Error: no se puede pasar mas de 1 parametro a una funcion, linea "+AnalizadorLexico.line_number);}
break;
case 95:
//#line 221 "gramatica.y"
{System.out.println("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number);}
break;
case 97:
//#line 227 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 98:
//#line 228 "gramatica.y"
{System.out.println("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number);}
break;
case 99:
//#line 229 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 100:
//#line 230 "gramatica.y"
{System.out.println("Error: se esperaba ';' en linea "AnalizadorLexico.line_number);}
break;
case 101:
//#line 231 "gramatica.y"
{System.out.println("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number);}
break;
case 102:
//#line 232 "gramatica.y"
{System.out.println("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 103:
//#line 233 "gramatica.y"
{System.out.println("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number);}
break;
case 105:
//#line 238 "gramatica.y"
{System.out.println("Error: lista de expresiones incorrecta, puede que falte ',' entre las expresiones, linea "+AnalizadorLexico.line_number)}
break;
case 113:
//#line 267 "gramatica.y"
{System.out.println("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number);}
break;
//#line 836 "Parser.java"
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

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



//#line 3 "grammar.y"
        import java.io.*;
        import PrimeraEtapa.AnalizadorLexico;
        

//#line 22 "Parser.java"




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
    0,    0,    0,    0,    0,    0,    1,    1,    1,    2,
    2,    2,    4,    4,    3,    3,    3,    3,    3,    3,
    3,   13,   13,    5,    5,    5,    5,    5,    5,    5,
    5,    5,    5,   15,   15,   15,   16,   16,   16,    6,
   17,   14,   14,   14,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,   20,   19,   19,   22,   22,   22,   22,   22,   22,
    8,    8,   18,   18,   18,   24,   24,   24,   25,   25,
   25,   25,   25,   25,   25,   23,   21,   21,    9,    9,
   10,   10,   10,   10,   10,   10,   12,   12,   26,   26,
   28,   28,   27,   27,   11,   11,
};
final static short yylen[] = {                            2,
    4,    4,    4,    4,    2,    0,    1,    2,    2,    2,
    2,    2,    1,    0,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    9,    9,   10,    9,    6,
    2,    3,    6,    3,    3,    1,    2,    1,    2,    4,
    1,    1,    1,    1,    7,    5,    7,    7,    9,    7,
    9,    7,    9,    7,    5,    7,    7,    9,    7,    9,
    7,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    2,    1,    1,    1,    4,    4,    5,    4,    2,
    8,    6,    8,    7,    3,    5,    3,    3,    3,    3,
    1,    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   21,    0,    0,    0,
    0,    0,   42,   43,    0,    0,    0,    7,    0,    0,
    0,   15,   16,   17,   18,   19,   20,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   80,   85,    0,    0,
    0,    0,    0,   84,    0,   78,    0,   90,    0,   31,
    0,    0,    0,  106,  105,    9,    8,   13,   10,   11,
   12,   36,    0,    0,    0,    0,    0,    0,    0,    2,
    3,    0,   83,    0,    0,    0,    0,   82,   81,    0,
   68,   69,   70,   65,   66,   67,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,   32,    0,    0,   95,
    0,   22,    0,    0,    0,    0,    0,    0,   98,    0,
    0,    0,  102,  100,   99,   86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   76,   77,
   89,   44,    0,   40,    0,   23,   34,    0,    0,   35,
    0,    0,    0,   87,    0,    0,    0,    0,   46,    0,
    0,   55,    0,   96,    0,    0,    0,    0,    0,    0,
    0,    0,   88,    0,    0,    0,    0,    0,    0,    0,
    0,   33,   30,    0,   92,    0,   39,   37,    0,    0,
    0,    0,   48,   47,    0,   45,   57,   56,    0,   54,
   52,   50,   61,   59,    0,    0,   94,    0,    0,    0,
    0,    0,    0,    0,   91,   93,    0,    0,    0,    0,
    0,   53,   51,   49,   60,   58,   27,   29,   26,    0,
   28,
};
final static short yydgoto[] = {                          3,
  207,   18,   19,   59,   20,   21,   22,   23,   24,   25,
   26,   27,  125,   28,   65,  158,  208,   41,   42,  126,
   73,   89,   29,   45,   46,   30,  111,   31,
};
final static short yysindex[] = {                       -76,
 -239,  -82,    0,  565,  565, -113,    0,  -23,  565,  -28,
 -228,   12,    0,    0, -198, -217,  511,    0,   19,   19,
   19,    0,    0,    0,    0,    0,    0, -236, -143,  -41,
   91,  422,  455,   -4, -111,  -25,    0,    0,   -4,  -61,
  -29, -115, -112,    0,   44,    0,  489,    0,   -4,    0,
  -52,   -4, -176,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  120,  -48,  127,   -4,  -18,  -79,  -79,    0,
    0,   54,    0,   63,   -4,  150,  164,    0,    0,   -4,
    0,    0,    0,    0,    0,    0,   -4,   -4,   -4,  293,
  412,   -4,   -4,    0,    0,   76,    0, -200,   83,    0,
 -113,    0,  586,  -54,  170,  174,  -38,   54,    0,  157,
  172,  107,    0,    0,    0,    0,   -5,  -16,   -6,   54,
   44,   44,   54,  -15, -119, -193,  -11, -126,    0,    0,
    0,    0,  202,    0, -222,    0,    0, -170, -203,    0,
   -4,   -4,  228,    0,  533,  537, -119, -119,    0, -119,
 -119,    0,  -14,    0,  -10,    0,  -12,  241,  245,  112,
   54,   54,    0,   34, -131,   42, -107,   60,   64,   69,
   79,    0,    0,   -4,    0,  299,    0,    0,  101,  109,
  111,  318,    0,    0,  555,    0,    0,    0, -119,    0,
    0,    0,    0,    0,  323,  337,    0,  565,  565,  565,
  123,  116, -201, -191,    0,    0,  511,  130,  131,  132,
  565,    0,    0,    0,    0,    0,    0,    0,    0,  133,
    0,
};
final static short yyrindex[] = {                       380,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  403,    0,  259,  259,
  259,    0,    0,    0,    0,    0,    0,    0,  362,    0,
    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,  -36,    0,   23,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  291,    0,  322,    0,    0,    0,    0,    0,
    0,  187,    0,    0,    0,    0,  -36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  344,    0,    0,    0,    0,    0,    0,
  364,    0,    0,    0,    0,    0,    0,  215,    0,    0,
  237,  -40,    0,    0,    0,    0,    0,    0,    0,   96,
   49,   71,  118,    0,  -91,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -39,    0,    0,    0,    0,
  140,  162,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -36,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -36,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  144,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   98,  110,  485,  281,    0,    0,    0,    0,    0,    0,
    0,    0,  368,  -80,    0,  283,  -77,  615,  -26,   22,
   11,    0,  573,  220,  262,    0,    0,  288,
};
final static int YYTABLESIZE=866;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        101,
   79,   38,   68,  101,   38,   83,   83,   98,   83,   35,
   83,   49,   76,   87,   75,   88,   39,  133,   43,   62,
   63,   40,   75,   83,   83,   83,   40,   50,    4,  174,
   84,   86,   85,  154,   40,  144,   64,   87,   54,   88,
   40,   79,   79,   79,   79,   79,   55,   79,   73,   77,
   51,   52,  159,  156,  213,  155,  132,  157,  157,   79,
   79,   79,   79,   75,  215,   75,   75,   75,  214,   53,
   74,   13,   14,  148,   13,   14,  149,   58,  216,  100,
  101,   75,   75,   75,   75,   92,  156,    7,    8,   73,
   93,   73,   73,   73,   10,   64,   87,   35,   88,   17,
   15,   32,   33,   16,   13,   14,   47,   73,   73,   73,
   73,   74,  128,   74,   74,   74,  131,   63,   87,   66,
   88,  209,  210,  134,  184,   87,   57,   88,  175,   74,
   74,   74,   74,  220,   69,  185,   64,  101,  186,  103,
  151,   57,   57,  152,    7,    8,   74,  195,  188,   34,
   90,   10,  181,   91,   64,  182,   57,   15,   63,  189,
   16,  104,  190,  104,   62,  176,  165,  167,  168,  169,
  107,  170,  171,    5,    6,   62,   63,  112,   62,    1,
    2,    7,    8,  103,  196,    9,   71,  116,   10,   11,
  118,   12,   13,   14,   15,   78,   79,   16,  103,   87,
  141,   88,  137,   97,  119,  104,  203,  105,  106,  138,
  204,   44,   44,  139,   72,  142,   44,   44,  140,   83,
  104,   67,  101,   83,   83,   83,   80,   48,   44,   35,
   81,   82,   83,   36,   37,   38,   97,  109,   36,   37,
   38,  172,  173,  177,  178,   71,   36,   37,   38,  145,
  143,  147,   36,   37,   38,  150,   79,   79,   14,  146,
   79,   79,   79,  153,   79,   79,   79,   79,  163,   79,
   79,   79,   79,   72,   79,   79,   79,   79,   75,   75,
   79,  179,   75,   75,   75,  180,   75,   75,   75,   75,
   25,   75,   75,   75,   75,   97,   75,   75,   75,   75,
   60,   61,   75,  183,   73,   73,  121,  122,   73,   73,
   73,  187,   73,   73,   73,   73,   57,   73,   73,   73,
   73,   24,   73,   73,   73,   73,   74,   74,   73,  191,
   74,   74,   74,  192,   74,   74,   74,   74,  193,   74,
   74,   74,   74,    4,   74,   74,   74,   74,  194,   25,
   74,   64,   64,  129,  130,  114,  115,  197,  201,   64,
   64,   64,   64,  205,   64,   64,   64,   64,  198,   64,
   64,   64,   64,   63,   63,   64,  199,  206,  200,    6,
   24,   63,   63,   63,   63,  212,   63,   63,   63,   63,
  211,   63,   63,   63,   63,  103,  103,   63,  217,  218,
  219,  221,    5,  103,  103,  102,  103,  101,  103,  103,
  103,  103,   41,  103,  103,  103,  103,  104,  104,  103,
  103,  160,    0,    0,    0,  104,  104,    0,  104,    0,
  104,  104,  104,  104,    0,  104,  104,  104,  104,    0,
    0,  104,   71,   71,    0,    0,    0,    0,    0,    0,
   71,   71,    0,   71,    0,   71,   71,   71,   71,    0,
   71,   71,   71,   71,    0,    0,   71,    0,    0,    0,
   72,   72,    0,    0,    0,    0,    0,    0,   72,   72,
    0,   72,    0,   72,   72,   72,   72,    0,   72,   72,
   72,   72,   97,   97,   72,    0,    0,    0,    0,    0,
   97,   97,    0,   97,    0,   97,   97,   97,   97,    0,
   97,   97,   97,   97,   14,   14,   97,    0,    0,    0,
    0,    0,   14,   14,    0,    0,    0,   14,    0,   14,
   14,    0,   14,   14,   14,   14,    0,  102,   14,    0,
    0,    0,    0,    0,    0,    0,   25,   25,  124,  101,
    0,    0,    0,    0,   25,   25,    7,    8,    0,   25,
    0,   25,   25,   10,   25,   25,   25,   25,    0,   15,
   25,    0,   16,    0,  102,  102,    0,   24,   24,    0,
   44,    0,    0,    0,    0,   24,   24,  136,    0,    0,
   24,    0,   24,   24,    0,   24,   24,   24,   24,    9,
    9,   24,    0,    0,    0,    0,   44,    9,    9,  136,
    0,   44,    9,    0,    9,    9,    0,    9,    9,    9,
    9,   44,    0,    9,   44,    0,    0,    0,    0,  102,
  102,  102,  102,    0,  102,  102,    0,    0,   44,   44,
  113,  113,    0,    0,    0,    0,    0,   44,   72,    0,
    0,    0,   44,    0,    0,    0,    0,    0,    0,   44,
   44,   44,    0,   96,   44,   44,   99,  127,  101,  102,
    0,    0,    0,  102,    0,    7,    8,   56,    6,    0,
  108,  110,   10,    0,    0,    7,    8,    0,   15,  117,
   70,   16,   10,   11,  120,   12,   13,   14,   15,    0,
    0,   16,    0,  123,    0,    0,    0,    0,    0,    0,
   56,    6,    0,   44,   44,    0,    0,    0,    7,    8,
    0,    0,    0,   71,    0,   10,   11,   44,   12,   13,
   14,   15,    0,    0,   16,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   94,    6,   44,    0,    0,    0,
    0,    0,    7,    8,    0,  161,  162,   95,    0,   10,
   11,    0,   12,   13,   14,   15,   56,    6,   16,    0,
    0,    0,    0,    0,    7,    8,    0,    0,    0,    0,
    0,   10,   11,    0,   12,   13,   14,   15,  164,  101,
   16,    0,  166,  101,    0,    0,    7,    8,    0,    0,
    7,    8,    0,   10,    0,    0,    0,   10,    0,   15,
  202,  101,   16,   15,    0,    0,   16,    0,    7,    8,
    0,    6,    0,    0,    0,   10,    0,    0,    7,    8,
    0,   15,    0,    0,   16,   10,   11,    0,   12,   13,
   14,   15,  101,    0,   16,    0,    0,    0,    0,    7,
    8,    0,    0,    0,  135,    0,   10,    0,    0,    0,
    0,    0,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   41,   44,   44,   44,   42,   43,   60,   45,  123,
   47,   40,   39,   43,   40,   45,   40,   98,    8,  256,
  257,   45,    0,   60,   61,   62,   45,  256,  268,   40,
   60,   61,   62,  256,   45,   41,  273,   43,  256,   45,
   45,   41,   42,   43,   44,   45,  264,   47,    0,   39,
  279,   40,  256,  257,  256,  278,  257,  138,  139,   59,
   60,   61,   62,   41,  256,   43,   44,   45,  270,  268,
    0,  275,  276,  267,  275,  276,  270,   59,  270,  256,
  257,   59,   60,   61,   62,   42,  257,  264,  265,   41,
   47,   43,   44,   45,  271,    0,   43,  123,   45,    2,
  277,    4,    5,  280,  275,  276,    9,   59,   60,   61,
   62,   41,   91,   43,   44,   45,   41,    0,   43,  263,
   45,  199,  200,   41,  256,   43,   17,   45,  155,   59,
   60,   61,   62,  211,   44,  267,   41,  257,  270,    0,
  267,   32,   33,  270,  264,  265,  258,  174,  256,  263,
  266,  271,   41,  266,   59,   44,   47,  277,   41,  267,
  280,    0,  270,   44,  256,  155,  145,  146,  147,  148,
   44,  150,  151,  256,  257,  267,   59,  257,  270,  256,
  257,  264,  265,   44,  174,  268,    0,  125,  271,  272,
   41,  274,  275,  276,  277,  257,  258,  280,   59,   43,
   44,   45,  257,  256,   41,   44,  185,  256,  257,   40,
  189,  256,  257,   40,    0,   44,  256,  257,  257,  256,
   59,  263,  263,  260,  261,  262,  256,  256,  273,  123,
  260,  261,  262,  257,  258,  259,    0,  256,  257,  258,
  259,  256,  257,  256,  257,   59,  257,  258,  259,  266,
  256,  267,  257,  258,  259,  267,  256,  257,    0,  266,
  260,  261,  262,   62,  264,  265,  266,  267,   41,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   41,  260,  261,  262,   41,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
   20,   21,  280,  270,  256,  257,   87,   88,  260,  261,
  262,  270,  264,  265,  266,  267,  207,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,  270,
  260,  261,  262,  270,  264,  265,  266,  267,  270,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  270,   59,
  280,  256,  257,   92,   93,   68,   69,   59,   41,  264,
  265,  266,  267,   41,  269,  270,  271,  272,  268,  274,
  275,  276,  277,  256,  257,  280,  268,   41,  268,    0,
   59,  264,  265,  266,  267,  270,  269,  270,  271,  272,
  268,  274,  275,  276,  277,  256,  257,  280,  269,  269,
  269,  269,    0,  264,  265,   44,  267,   44,  269,  270,
  271,  272,  269,  274,  275,  276,  277,  256,  257,  280,
   53,  139,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,   -1,
   -1,  280,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,   -1,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,   53,  280,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  256,  257,
   -1,   -1,   -1,   -1,  264,  265,  264,  265,   -1,  269,
   -1,  271,  272,  271,  274,  275,  276,  277,   -1,  277,
  280,   -1,  280,   -1,   90,   91,   -1,  256,  257,   -1,
    8,   -1,   -1,   -1,   -1,  264,  265,  103,   -1,   -1,
  269,   -1,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   34,  264,  265,  125,
   -1,   39,  269,   -1,  271,  272,   -1,  274,  275,  276,
  277,   49,   -1,  280,   52,   -1,   -1,   -1,   -1,  145,
  146,  147,  148,   -1,  150,  151,   -1,   -1,   66,   67,
   68,   69,   -1,   -1,   -1,   -1,   -1,   75,   34,   -1,
   -1,   -1,   80,   -1,   -1,   -1,   -1,   -1,   -1,   87,
   88,   89,   -1,   49,   92,   93,   52,  256,  257,  185,
   -1,   -1,   -1,  189,   -1,  264,  265,  256,  257,   -1,
   66,   67,  271,   -1,   -1,  264,  265,   -1,  277,   75,
  269,  280,  271,  272,   80,  274,  275,  276,  277,   -1,
   -1,  280,   -1,   89,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,   -1,  141,  142,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,  271,  272,  155,  274,  275,
  276,  277,   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  174,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  141,  142,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,  256,  257,  280,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,
   -1,  271,  272,   -1,  274,  275,  276,  277,  256,  257,
  280,   -1,  256,  257,   -1,   -1,  264,  265,   -1,   -1,
  264,  265,   -1,  271,   -1,   -1,   -1,  271,   -1,  277,
  256,  257,  280,  277,   -1,   -1,  280,   -1,  264,  265,
   -1,  257,   -1,   -1,   -1,  271,   -1,   -1,  264,  265,
   -1,  277,   -1,   -1,  280,  271,  272,   -1,  274,  275,
  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,   -1,
   -1,   -1,  277,   -1,   -1,  280,
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
"prog : ID error statement_list END",
"prog : ID BEGIN statement_list error",
"prog : ID statement_list",
"prog :",
"statement_list : statement",
"statement_list : statement_list statement",
"statement_list : statement_list error",
"statement : executable_statement optional_semicolon",
"statement : declare_statement optional_semicolon",
"statement : return_statement optional_semicolon",
"optional_semicolon : ';'",
"optional_semicolon :",
"executable_statement : if_statement",
"executable_statement : assign_statement",
"executable_statement : outf_statement",
"executable_statement : repeat_statement",
"executable_statement : goto_statement",
"executable_statement : mult_assign_statement",
"executable_statement : TAG",
"executable_statement_list : executable_statement",
"executable_statement_list : executable_statement_list executable_statement",
"declare_statement : var_type var_list",
"declare_statement : var_type ID",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END",
"declare_statement : var_type FUN error '(' parametro ')' BEGIN fun_body END",
"declare_statement : var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END",
"declare_statement : var_type FUN ID '(' error ')' BEGIN fun_body END",
"declare_statement : TYPEDEF PAIR '<' var_type '>' ID",
"declare_statement : TYPEDEF error",
"declare_statement : TYPEDEF PAIR error",
"declare_statement : TYPEDEF PAIR '<' var_type '>' error",
"var_list : ID ',' ID",
"var_list : var_list ',' ID",
"var_list : error",
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
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE error END_IF",
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
"assign_statement : ID ASSIGN expr",
"assign_statement : expr_pair ASSIGN expr",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : term",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"fact : ID",
"fact : CTE",
"fact : '-' CTE",
"fact : '-' ID",
"fact : fun_invoc",
"fact : expr_pair",
"fact : CHARCH",
"expr_pair : ID '{' CTE '}'",
"fun_invoc : ID '(' expr ')'",
"fun_invoc : ID '(' expr error ')'",
"outf_statement : OUTF '(' expr ')'",
"outf_statement : OUTF error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL cond",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' fun_invoc ')'",
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

//#line 278 "grammar.y"

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */


/* recordar: $$ es el valor del lado izq de la regla. $n del n-ésimo del lado de la derecha */
/* con esto podemos verfiicar algunos errores en vez de reescribir reglas.. */



	public static void yyerror(String msg){
	        System.out.println("Error en la línea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){
                return AnalizadorLexico.yylex();
        }
        // valor a yylval (lexema)
//#line 567 "Parser.java"
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
//#line 23 "grammar.y"
{System.out.println("Error: Falta el nombre del programa en la primer linea"); }
break;
case 3:
//#line 25 "grammar.y"
{System.out.println("Error: Falta delimitador del programa3, linea "+AnalizadorLexico.line_number); }
break;
case 4:
//#line 26 "grammar.y"
{System.out.println("Error: Falta delimitador del programa2, linea "+AnalizadorLexico.line_number); }
break;
case 5:
//#line 27 "grammar.y"
{System.out.println("Error: Falta delimitador del programa1, linea "+AnalizadorLexico.line_number); }
break;
case 6:
//#line 28 "grammar.y"
{System.out.println("Error: programa vacio... "); }
break;
case 9:
//#line 35 "grammar.y"
{System.out.println("Error: sintaxis incorrecta de sentencia en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 54 "grammar.y"
{
            System.out.println("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number);
        }
break;
case 27:
//#line 83 "grammar.y"
{System.out.println("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number); }
break;
case 28:
//#line 84 "grammar.y"
{System.out.println("Error: ',' invalida en linea "+AnalizadorLexico.line_number); }
break;
case 29:
//#line 85 "grammar.y"
{System.out.println("Error:  linea "+AnalizadorLexico.line_number); }
break;
case 31:
//#line 87 "grammar.y"
{System.out.println("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number); }
break;
case 32:
//#line 88 "grammar.y"
{System.out.println("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number); }
break;
case 33:
//#line 89 "grammar.y"
{System.out.println("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number); }
break;
case 36:
//#line 100 "grammar.y"
{System.out.println("Error: sintaxis incorrecta de lista de variables. Asegurate haya ',' entre las variables, linea "+AnalizadorLexico.line_number); }
break;
case 38:
//#line 107 "grammar.y"
{System.out.println("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number); }
break;
case 39:
//#line 108 "grammar.y"
{System.out.println("Error: se esperaba nombre de parametro"); }
break;
case 46:
//#line 132 "grammar.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number); }
break;
case 47:
//#line 133 "grammar.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number); }
break;
case 48:
//#line 134 "grammar.y"
{System.out.println("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number); }
break;
case 50:
//#line 136 "grammar.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number); }
break;
case 51:
//#line 137 "grammar.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number); }
break;
case 52:
//#line 138 "grammar.y"
{System.out.println("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number); }
break;
case 53:
//#line 139 "grammar.y"
{System.out.println("Error: Se esperaba sentencia ejecutable luego del else, linea "+AnalizadorLexico.line_number); }
break;
case 55:
//#line 142 "grammar.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number); }
break;
case 56:
//#line 143 "grammar.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number); }
break;
case 57:
//#line 144 "grammar.y"
{System.out.println("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number); }
break;
case 59:
//#line 146 "grammar.y"
{System.out.println("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number); }
break;
case 60:
//#line 147 "grammar.y"
{System.out.println("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number); }
break;
case 61:
//#line 148 "grammar.y"
{System.out.println("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number); }
break;
case 64:
//#line 162 "grammar.y"
{System.out.println("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number); }
break;
case 88:
//#line 224 "grammar.y"
{System.out.println("Error: sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion, linea "+AnalizadorLexico.line_number); }
break;
case 90:
//#line 229 "grammar.y"
{System.out.println("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number); }
break;
case 92:
//#line 235 "grammar.y"
{System.out.println("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number); }
break;
case 94:
//#line 237 "grammar.y"
{System.out.println("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number); }
break;
case 95:
//#line 238 "grammar.y"
{System.out.println("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number); }
break;
case 96:
//#line 239 "grammar.y"
{System.out.println("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number); }
break;
case 98:
//#line 244 "grammar.y"
{System.out.println("Error: lista de expresiones incorrecta, puede que falte ',' entre las expresiones, linea "+AnalizadorLexico.line_number); }
break;
case 106:
//#line 273 "grammar.y"
{System.out.println("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number); }
break;
//#line 870 "Parser.java"
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

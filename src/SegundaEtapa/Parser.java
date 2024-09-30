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
    3,    3,    3,    3,    3,    3,    3,    3,    3,   12,
   12,    4,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,   14,   14,   14,   15,   15,
   15,    5,   16,   13,   13,    6,    6,    6,    6,    6,
    6,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    6,   19,   18,   18,   21,   21,   21,   21,   21,   21,
    7,    7,   17,   17,   17,   17,   17,   17,   17,   22,
   22,   22,   22,   22,   22,   22,   22,   23,   23,   23,
   20,   20,    8,    8,    8,    9,    9,    9,    9,    9,
    9,    9,    9,   11,   24,   24,   25,   25,   25,   25,
   10,   10,
};
final static short yylen[] = {                            2,
    4,    4,    1,    1,    2,    1,    1,    2,    2,    1,
    2,    2,    1,    2,    2,    1,    2,    2,    1,    1,
    2,    3,    3,    3,    3,   10,   10,    9,   11,   12,
    7,    7,    2,    3,    6,    3,    3,    2,    2,    1,
    2,    4,    1,    1,    1,    7,    5,    7,    7,    9,
    7,    9,    7,    7,    5,    7,    7,    9,    7,    9,
    7,    1,    3,    2,    1,    1,    1,    1,    1,    1,
    4,    4,    3,    3,    1,    3,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    3,    1,    1,    1,
    4,    6,    4,    4,    2,    9,    9,    6,    9,    9,
    7,    3,    5,    3,    3,    3,    4,    4,    3,    2,
    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   10,    0,    0,    0,    0,
    0,   44,   45,    0,    0,    0,    4,    6,    7,    0,
    0,   13,    0,   16,    0,   19,    0,    0,    0,    0,
    0,    0,    0,   89,    0,    0,    0,    0,    0,   82,
   95,    0,   33,    0,    0,    0,  112,    0,    2,    5,
    9,    8,   12,   11,   15,   14,   18,   17,    0,    0,
    0,    0,    0,    1,    0,   90,  105,    0,    0,    0,
    0,    0,    0,    0,   68,   69,   70,   65,   66,   67,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   34,    0,    0,  102,   20,    0,  111,   25,   38,
   24,    0,    0,    0,   23,   22,    0,  104,  106,   72,
   71,    0,    0,    0,   84,   85,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   83,
   86,   80,   87,   81,   94,   93,    0,   42,    0,   21,
    0,   37,    0,    0,    0,  110,   91,    0,    0,    0,
    0,    0,   47,    0,    0,   55,    0,  103,    0,   40,
    0,    0,    0,    0,  109,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,    0,    0,   98,    0,   41,
   39,    0,    0,    0,    0,  107,   92,   49,   48,    0,
   46,   57,   56,    0,   54,   53,   51,   61,   59,   32,
   31,    0,    0,  101,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   52,   50,   60,
   58,   97,   96,  100,   99,   28,    0,    0,    0,   27,
   26,    0,    0,   29,    0,   30,
};
final static short yydgoto[] = {                          3,
  213,   17,   96,   19,   20,   21,   22,   23,   24,   25,
   26,  126,   27,   61,  162,  214,   36,   37,  127,   66,
   84,   39,   40,   28,  146,
};
final static short yysindex[] = {                      -193,
 -217, -183,    0, -142, -142,    0,  -36,  178,  -33, -221,
   73,    0,    0, -131, -144,  245,    0,    0,    0,  -54,
  -48,    0,  -47,    0,  -45,    0, -169,  -31,  267,  295,
 -208,  464,  140,    0,  295,  416,  -82,  -79,   -8,    0,
    0,  -53,    0,  -13,  295,  -94,    0,  136,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16, -105,
  -44,  295,  -64,    0,  -19,    0,    0,  305,  305,  -97,
  -97,  295,  169,  216,    0,    0,    0,    0,    0,    0,
  328,  343,  102,  295,  289,  293,  -97,  346,  351,  227,
  485,    0, -107,  486,    0,    0,  318,    0,    0,    0,
    0,   20,  242,  252,    0,    0,  429,    0,    0,    0,
    0,   42,   -8,   -8,    0,    0,  440,   35,   50,   42,
   -8,   42,   -8,  102,   39, -174, -159,   53, -139,    0,
    0,    0,    0,    0,    0,    0,  259,    0, -219,    0,
  -35,    0, -195, -195,  295,    0,    0,  295,  300,  311,
 -174, -174,    0, -174, -174,    0,   31,    0,  207,    0,
   40,  284,   95,  423,    0,  492,   60, -160,   65,  190,
   66,   70,   74,   75,    0,  -43,  295,    0,  290,    0,
    0,   86,   91,  135,  464,    0,    0,    0,    0, -174,
    0,    0,    0, -174,    0,    0,    0,    0,    0,    0,
    0,  319,  323,    0, -142, -142,  100,  332, -239, -214,
  -18,   -4, -142,  109,  114, -142,  101,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,  115, -142,    0,
    0,  329,  124,    0,  338,    0,
};
final static short yyrindex[] = {                         0,
  402,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,  409,   33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  409,    0,    0,    0,    0,    0,    0,
    0,    0,  151,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,   81,    0,    0,    0,    0,    0,  -16,
  105,    9,  129,  173,    0,  219,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    7,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  409,    0,
    0,    0,    0,    0,  223,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  409,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  145,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  307,   -6,  408,    0,    0,    0,    0,    0,    0,    0,
    0,  369,  -57,  317, -112, -168,   37,   -2,    4,   22,
    0,  524,  108,    0,  -40,
};
final static int YYTABLESIZE=687;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         88,
   88,   88,   88,   88,   52,   88,   42,   31,  102,   50,
   54,   56,   63,   58,  106,  201,  218,   88,   88,   88,
   88,  108,   50,   81,   78,   82,   78,   78,   78,   38,
  219,  163,   73,   88,   43,  137,  158,  215,   89,  111,
  223,  220,   78,   78,   78,   78,   93,  228,   67,   79,
    4,   79,   79,   79,  225,  221,   74,   44,  159,  102,
  233,  160,    1,    2,  231,   36,   65,   79,   79,   79,
   79,  208,   83,   75,  101,   75,   75,   75,   91,   12,
   13,   94,    7,   70,    5,  161,  161,   59,   71,  129,
    8,   75,   75,   75,   75,  189,    9,   76,  107,   76,
   76,   76,   14,   60,  165,   15,  190,  152,  117,  191,
  153,   47,   45,    6,    7,   76,   76,   76,   76,   48,
  124,   77,    8,   77,   77,   77,  161,  155,    9,   10,
  156,   11,   12,   13,   14,  183,   46,   15,  184,   77,
   77,   77,   77,  107,   81,   73,   82,   73,   73,   73,
  103,  104,  168,  170,  171,  172,  178,  173,  174,   33,
   34,   95,    7,   73,   73,   73,   73,   12,   13,   74,
    8,   74,   74,   74,  202,  207,    9,  115,  116,   72,
  179,  164,   14,   85,  166,   15,   86,   74,   74,   74,
   74,   64,  109,  209,  130,  132,  134,  210,  203,   98,
  107,   51,   32,   33,   34,   90,   50,   53,   55,  118,
   57,  105,  200,   63,   88,   88,   88,   35,   88,   88,
   88,  100,   41,   88,   88,   88,   30,   88,   88,   88,
   88,   62,   88,   88,   88,   88,  110,  222,   88,   78,
   78,   78,   92,   78,   78,   78,  177,   87,   78,   78,
   78,  224,   78,   78,   78,   78,  119,   78,   78,   78,
   78,  230,   36,   78,   79,   79,   79,  135,   79,   79,
   79,   99,  100,   79,   79,   79,  141,   79,   79,   79,
   79,  143,   79,   79,   79,   79,  175,  176,   79,   75,
   75,  144,   75,   75,   75,  180,  181,   75,   75,   75,
  149,   75,   75,   75,   75,  151,   75,   75,   75,   75,
   16,   29,   75,   76,   76,  150,   76,   76,   76,  154,
  157,   76,   76,   76,  182,   76,   76,   76,   76,  188,
   76,   76,   76,   76,  192,  196,   76,   77,   77,  197,
   77,   77,   77,  198,  199,   77,   77,   77,  204,   77,
   77,   77,   77,  205,   77,   77,   77,   77,  206,  211,
   77,   73,   73,  212,   73,   73,   73,  216,  229,   73,
   73,   73,  217,   73,   73,   73,   73,  226,   73,   73,
   73,   73,  227,  232,   73,   74,   74,  234,   74,   74,
   74,  160,  235,   74,   74,   74,  236,   74,   74,   74,
   74,    3,   74,   74,   74,   74,   64,   64,   74,   12,
   13,   18,   18,   43,   97,   64,   64,   64,  142,   64,
   64,   64,   64,   18,   64,   64,   64,   64,   63,   63,
   64,    0,    0,   32,   33,   34,   18,   63,   63,   63,
    0,   63,   63,   63,   63,  193,   63,   63,   63,   63,
   90,   90,   63,   90,    0,   90,  194,    0,   81,  195,
   82,    0,   32,   33,   34,   81,  145,   82,   90,   90,
   90,   81,  145,   82,   62,   78,   80,   79,  108,  108,
  147,  186,   81,  148,   82,   62,    0,  108,   62,  108,
    0,  108,  108,  108,  108,    0,  108,  108,  108,  108,
    6,    7,  108,    0,  140,   70,   68,    0,   69,    8,
   71,    0,    0,   49,    0,    9,   10,    0,   11,   12,
   13,   14,    6,    7,   15,  136,  138,   81,   81,   82,
   82,    8,  187,  140,   81,   64,   82,    9,   10,    0,
   11,   12,   13,   14,  125,    7,   15,    0,  128,    7,
   32,   33,   34,    8,    0,  167,    7,    8,    0,    9,
  112,   33,   34,    9,    8,   14,  169,    7,   15,   14,
    9,    0,   15,    0,    7,    8,   14,    0,    0,   15,
    0,    9,    8,  120,   33,   34,  139,   14,    9,    0,
   15,  113,  114,    0,   14,    0,    0,   15,  122,   33,
   34,  131,   33,   34,  121,  123,  133,   33,   34,    0,
    0,    0,   18,   18,    0,    0,    0,    0,    0,    0,
   18,    0,    0,   18,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   18,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   90,   90,   90,    0,   90,   90,
   90,   32,   33,   34,    0,   75,   76,   77,  185,   33,
   34,    0,    0,    0,   32,   33,   34,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   59,   47,   40,   44,   44,   16,
   59,   59,   44,   59,   59,   59,  256,   59,   60,   61,
   62,   62,   29,   43,   41,   45,   43,   44,   45,    8,
  270,  144,   35,   42,  256,   93,  256,  206,   47,   59,
   59,  256,   59,   60,   61,   62,   60,  216,  257,   41,
  268,   43,   44,   45,   59,  270,   35,  279,  278,   44,
  229,  257,  256,  257,   59,   59,   30,   59,   60,   61,
   62,  184,   36,   41,   59,   43,   44,   45,   42,  275,
  276,   45,  257,   42,  268,  143,  144,  257,   47,   86,
  265,   59,   60,   61,   62,  256,  271,   41,   62,   43,
   44,   45,  277,  273,  145,  280,  267,  267,   72,  270,
  270,  256,   40,  256,  257,   59,   60,   61,   62,  264,
   84,   41,  265,   43,   44,   45,  184,  267,  271,  272,
  270,  274,  275,  276,  277,   41,  268,  280,   44,   59,
   60,   61,   62,  107,   43,   41,   45,   43,   44,   45,
  256,  257,  149,  150,  151,  152,  159,  154,  155,  257,
  258,  256,  257,   59,   60,   61,   62,  275,  276,   41,
  265,   43,   44,   45,  177,   41,  271,   70,   71,   40,
  159,  145,  277,  266,  148,  280,  266,   59,   60,   61,
   62,   41,  257,  190,   87,   88,   89,  194,  177,   64,
  164,  256,  256,  257,  258,  259,  213,  256,  256,   41,
  256,  256,  256,   41,  256,  257,  258,   40,  260,  261,
  262,  257,  256,  265,  266,  267,  263,  269,  270,  271,
  272,  263,  274,  275,  276,  277,  256,  256,  280,  256,
  257,  258,  256,  260,  261,  262,   40,  256,  265,  266,
  267,  256,  269,  270,  271,  272,   41,  274,  275,  276,
  277,  256,  256,  280,  256,  257,  258,   41,  260,  261,
  262,  256,  257,  265,  266,  267,  257,  269,  270,  271,
  272,   40,  274,  275,  276,  277,  256,  257,  280,  257,
  258,   40,  260,  261,  262,  256,  257,  265,  266,  267,
  266,  269,  270,  271,  272,  267,  274,  275,  276,  277,
    4,    5,  280,  257,  258,  266,  260,  261,  262,  267,
   62,  265,  266,  267,   41,  269,  270,  271,  272,  270,
  274,  275,  276,  277,  270,  270,  280,  257,  258,  270,
  260,  261,  262,  270,  270,  265,  266,  267,   59,  269,
  270,  271,  272,  268,  274,  275,  276,  277,  268,   41,
  280,  257,  258,   41,  260,  261,  262,  268,  268,  265,
  266,  267,   41,  269,  270,  271,  272,  269,  274,  275,
  276,  277,  269,  269,  280,  257,  258,   59,  260,  261,
  262,  257,  269,  265,  266,  267,   59,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,  275,
  276,    4,    5,  269,   46,  265,  266,  267,  102,  269,
  270,  271,  272,   16,  274,  275,  276,  277,  256,  257,
  280,   -1,   -1,  256,  257,  258,   29,  265,  266,  267,
   -1,  269,  270,  271,  272,  256,  274,  275,  276,  277,
   42,   43,  280,   45,   -1,   47,  267,   -1,   43,  270,
   45,   -1,  256,  257,  258,   43,   44,   45,   60,   61,
   62,   43,   44,   45,  256,   60,   61,   62,  256,  257,
   41,   59,   43,   44,   45,  267,   -1,  265,  270,  267,
   -1,  269,  270,  271,  272,   -1,  274,  275,  276,  277,
  256,  257,  280,   -1,   97,   42,   43,   -1,   45,  265,
   47,   -1,   -1,  269,   -1,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,   41,   41,   43,   43,   45,
   45,  265,   41,  126,   43,  269,   45,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,  256,  257,
  256,  257,  258,  265,   -1,  256,  257,  265,   -1,  271,
  256,  257,  258,  271,  265,  277,  256,  257,  280,  277,
  271,   -1,  280,   -1,  257,  265,  277,   -1,   -1,  280,
   -1,  271,  265,  256,  257,  258,  269,  277,  271,   -1,
  280,   68,   69,   -1,  277,   -1,   -1,  280,  256,  257,
  258,  256,  257,  258,   81,   82,  256,  257,  258,   -1,
   -1,   -1,  205,  206,   -1,   -1,   -1,   -1,   -1,   -1,
  213,   -1,   -1,  216,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  229,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  256,  257,  258,   -1,  260,  261,  262,  256,  257,
  258,   -1,   -1,   -1,  256,  257,  258,
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
"executable_statement : repeat_statement",
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
"declare_statement : var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END ';'",
"declare_statement : var_type FUN ID '(' parametro ',' parametro ')' BEGIN fun_body END ';'",
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
"expr : error '+' term",
"expr : error '-' term",
"expr : expr '+' error",
"expr : expr '-' error",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"term : term error fact",
"term : error '*' fact",
"term : error '/' fact",
"term : term '*' error",
"term : term '/' error",
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
"expr_list : expr ',' expr ';'",
"expr_list : expr ',' expr error",
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
//#line 20 "gramatica.y"
{System.println.out("Error: Falta el nombre del programa en la primer linea")}
break;
case 3:
//#line 21 "gramatica.y"
{System.println.out("Error: Falta delimitador de programa")}
break;
case 9:
//#line 35 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 10:
//#line 36 "gramatica.y"
{System.println.out("Error: sintaxis de sentencia incorrecta")}
break;
case 12:
//#line 45 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 15:
//#line 48 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 18:
//#line 51 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 23:
//#line 63 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 25:
//#line 65 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 27:
//#line 67 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 28:
//#line 68 "gramatica.y"
{System.println.out("Error: se esperaba nombre de funcion en linea "+AnalizadorLexico.line_number)}
break;
case 29:
//#line 69 "gramatica.y"
{System.println.out("Error: ',' invalida en linea "+AnalizadorLexico.line_number)}
break;
case 30:
//#line 70 "gramatica.y"
{System.println.out("Error: no se puede pasar mas de 1 parametro a la funcion, linea "+AnalizadorLexico.line_number)}
break;
case 32:
//#line 72 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 33:
//#line 73 "gramatica.y"
{System.println.out("Error: se esperaba 'pair' en linea "+AnalizadorLexico.line_number)}
break;
case 34:
//#line 74 "gramatica.y"
{System.println.out("Error: se esperaba '<' en linea "+AnalizadorLexico.line_number)}
break;
case 35:
//#line 75 "gramatica.y"
{System.println.out("Error: se esperaba un ID al final de la linea "+AnalizadorLexico.line_number)}
break;
case 38:
//#line 86 "gramatica.y"
{
        yyerror("Error: se espera ',' luego de cada variable, línea " + AnalizadorLexico.line_number);
        yyclearin;
      }
break;
case 40:
//#line 96 "gramatica.y"
{System.println.out("Error: se esperaba tipo del parametro de la funcion en linea "+AnalizadorLexico.line_number)}
break;
case 41:
//#line 97 "gramatica.y"
{System.println.out("Error: se esperaba nombre de parametro")}
break;
case 47:
//#line 121 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 48:
//#line 122 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 49:
//#line 123 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number)}
break;
case 51:
//#line 125 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 52:
//#line 126 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 53:
//#line 127 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
break;
case 55:
//#line 130 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 56:
//#line 131 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 57:
//#line 132 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia/s ejecutable/s dentro del IF en linea "+AnalizadorLexico.line_number)}
break;
case 59:
//#line 134 "gramatica.y"
{System.println.out("Error: se esperaba '(' antes de la condicion en linea "+AnalizadorLexico.line_number)}
break;
case 60:
//#line 135 "gramatica.y"
{System.println.out("Error: se esperaba END_IF al final de la linea "+AnalizadorLexico.line_number)}
break;
case 61:
//#line 136 "gramatica.y"
{System.println.out("Error: Se esperaba sentencia de ejecucicion en el IF, linea "+AnalizadorLexico.line_number)}
break;
case 64:
//#line 150 "gramatica.y"
{System.println.out("Error: se esperaba comparador en linea "+AnalizadorLexico.line_number)}
break;
case 72:
//#line 165 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "+AnalizadorLexico.line_number)}
break;
case 76:
//#line 176 "gramatica.y"
{System.println.out("Error: se esperaba expresion antes del '+' en linea "+AnalizadorLexico.line_number)}
break;
case 77:
//#line 177 "gramatica.y"
{System.println.out("Error: se esperaba expresion antes del '-' en linea "+AnalizadorLexico.line_number)}
break;
case 78:
//#line 178 "gramatica.y"
{System.println.out("Error: se esperaba expresion despues del '+' en linea "+AnalizadorLexico.line_number)}
break;
case 79:
//#line 179 "gramatica.y"
{System.println.out("Error: se esperaba expresion despues del '-' en linea "+AnalizadorLexico.line_number)}
break;
case 84:
//#line 189 "gramatica.y"
{System.println.out("Error: se esperaba expresion antes de '*' en linea "+AnalizadorLexico.line_number)}
break;
case 85:
//#line 190 "gramatica.y"
{System.println.out("Error: se esperaba expresion antes de '/' en linea "+AnalizadorLexico.line_number)}
break;
case 86:
//#line 191 "gramatica.y"
{System.println.out("Error: se esperaba expresion despues de '*' en linea "+AnalizadorLexico.line_number)}
break;
case 87:
//#line 192 "gramatica.y"
{System.println.out("Error: se esperaba expresion despues de '/* en linea "+AnalizadorLexico.line_number)}
break;
case 92:
//#line 203 "gramatica.y"
{System.println.out ("Error: no se puede pasar mas de 1 parametro a una funcion, linea "+AnalizadorLexico.line_number)}
break;
case 95:
//#line 209 "gramatica.y"
{System.println.out("Error: se esperaba parametro de OUTF en linea "+AnalizadorLexico.line_number)}
break;
case 97:
//#line 215 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 98:
//#line 216 "gramatica.y"
{System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
break;
case 99:
//#line 217 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 100:
//#line 218 "gramatica.y"
{System.println.out("Error: se esperaba ';' en linea "AnalizadorLexico.line_number)}
break;
case 101:
//#line 219 "gramatica.y"
{System.println.out("Error: se esperaba '(' en linea "+AnalizadorLexico.line_number)}
break;
case 102:
//#line 220 "gramatica.y"
{System.println.out("Error: se esperaba cuerpo de repeat until en linea "+AnalizadorLexico.line_number)}
break;
case 103:
//#line 221 "gramatica.y"
{System.println.out("Error: se esperaba UNTIL luego de 'END' en linea "+AnalizadorLexico.line_number)}
break;
case 108:
//#line 239 "gramatica.y"
{System.println.out("falta ';'")}
break;
case 110:
//#line 241 "gramatica.y"
{System.println.out("Error: se epseraba ',' luego de la expresion en linea "+AnalizadorLexico.line_number)}
break;
case 112:
//#line 246 "gramatica.y"
{System.println.out("Error: se esperaba TAG en linea "+AnalizadorLexico.line_number)}
break;
//#line 887 "Parser.java"
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

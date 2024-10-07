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
    0,    0,    0,    0,    0,    0,    0,    1,    1,    1,
    2,    2,    2,    2,    2,    4,    4,    3,    3,    3,
    3,    3,    3,    3,   15,   15,    6,    6,    6,    7,
    7,    7,    7,    5,    5,    5,    5,   17,   17,   18,
   18,   18,    8,    8,   19,   16,   16,   16,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,   22,   21,   21,   24,   24,
   24,   24,   24,   24,   10,   10,   10,   20,   20,   20,
   26,   26,   26,   27,   27,   27,   27,   27,   27,   27,
   25,   23,   23,   11,   11,   12,   12,   12,   12,   12,
   12,   14,   14,   28,   28,   30,   30,   29,   29,   13,
   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    3,
    2,    2,    1,    1,    2,    1,    0,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    3,    3,    3,    9,
    9,   10,    9,    6,    2,    3,    6,    3,    3,    2,
    1,    2,    4,    2,    1,    1,    1,    1,    7,    5,
    7,    7,    9,    7,    9,    7,    9,    7,    5,    7,
    7,    9,    7,    9,    7,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    3,    3,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    2,    1,    1,    1,
    4,    4,    5,    4,    2,    8,    6,    8,    7,    3,
    5,    3,    3,    3,    3,    1,    1,    3,    3,    2,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   24,    0,    0,    0,    0,
    0,   46,   47,    0,    0,    0,    8,    0,    0,   13,
   14,    0,   18,   19,   20,   21,   22,   23,    0,    0,
    0,    0,    0,    0,    0,    0,   85,   90,    0,    0,
    0,    0,    0,   89,    0,   83,    0,   95,    0,   35,
    0,    0,    0,   88,    0,  111,  110,    0,    4,    9,
   16,   11,   12,   15,    0,    0,    0,    0,    0,    0,
    0,    2,    0,    0,    0,    0,    0,   87,   86,    0,
   72,   73,   74,   69,   70,   71,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,   36,    0,    0,  100,
   25,    0,    0,   10,   29,    0,   28,    0,    0,    0,
   27,    0,    0,  103,    0,    0,    0,  107,  105,  104,
   91,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   81,   82,   94,   48,    0,   43,    0,
   26,    0,    0,   38,    0,    0,   39,    0,    0,    0,
   92,    0,    0,    0,    0,   50,    0,    0,   59,    0,
  101,    0,    0,    0,    0,    0,    0,    0,    0,   93,
    0,    0,    0,    0,    0,    0,    0,    0,   37,   34,
    0,   97,    0,   42,   40,    0,    0,    0,    0,   52,
   51,    0,   49,   61,   60,    0,   58,   56,   54,   65,
   63,    0,    0,   99,    0,    0,    0,    0,    0,    0,
    0,   96,   98,    0,    0,    0,    0,    0,   57,   55,
   53,   64,   62,   31,   33,   30,    0,   32,
};
final static short yydgoto[] = {                          3,
  214,   17,   18,   62,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,  130,  103,   67,  165,  215,   41,
   42,  131,   54,   89,   30,   45,   46,   31,  116,   32,
};
final static short yysindex[] = {                      -216,
 -258,  595,    0,  616,  -96,    0,  -23,  616,  -28, -243,
  -10,    0,    0, -209, -186,  391,    0,   38,   38,    0,
    0,   38,    0,    0,    0,    0,    0,    0, -204, -160,
  -41,   28,  432,   97, -145,   -3,    0,    0,   97, -122,
  -29,  -75,  -71,    0,   87,    0,  457,    0,   97,    0,
  -52,   97,  130,    0, -176,    0,    0,   62,    0,    0,
    0,    0,    0,    0,  -40,  -76,   63,   97,  -16, -114,
 -114,    0,  130,   76,   97,  171,  173,    0,    0,   97,
    0,    0,    0,    0,    0,    0,   97,   97,   97,  510,
  527,   97,   97,   62,    0,  133,    0, -202,  153,    0,
    0,  637,  -27,    0,    0,   97,    0,  -19,  210,  220,
    0,    7,  130,    0,  211,  238,  163,    0,    0,    0,
    0,   13,   41,   42,  130,   87,   87,  130,   37, -119,
 -123,   45,  -70,    0,    0,    0,    0,  255,    0, -228,
    0,   67,  130,    0, -200, -151,    0,   97,   97,  293,
    0,  544,  561, -119, -119,    0, -119, -119,    0,  -47,
    0,   -6,    0,  -12,  298,  303,  161,  130,  130,    0,
   79,  -85,   89,  -84,   94,   99,  109,  110,    0,    0,
   97,    0,  327,    0,    0,  123,  131,  132,  360,    0,
    0,  578,    0,    0,    0, -119,    0,    0,    0,    0,
    0,  361,  362,    0,  616,  616,  616,  138,  143, -191,
 -179,    0,    0,  488,  139,  152,  154,  616,    0,    0,
    0,    0,    0,    0,    0,    0,  155,    0,
};
final static short yyrindex[] = {                       422,
  425,    0,    0,    0,  -44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  428,    0,  259,  259,    0,
    0,  259,    0,    0,    0,    0,    0,    0,    0,  386,
    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,  -36,    0,   23,    0,    0,    0,    0,    0,
    0,    0,  322,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  187,    0,    0,    0,  -36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  435,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  215,    0,    0,  237,  -24,    0,    0,    0,
    0,    0,    0,    0,   96,   49,   71,  118,    0,  -77,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  291,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,  140,  162,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  172,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  115,    5,   -4,  189,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  385,  536,    0,  299,  -79,  570,
  -21,   11,    8,    0,  543,  214,  265,    0,    0,  307,
};
final static int YYTABLESIZE=917;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        106,
   84,   41,   70,  108,   41,   88,   88,   98,   88,    4,
   88,   49,   50,   87,   43,   88,   39,   76,  107,  106,
   60,   40,   80,   88,   88,   88,   35,  161,   40,   52,
   84,   86,   85,  181,   40,   51,   75,   60,   40,    1,
    2,   84,   84,   84,   84,   84,   77,   84,   78,  162,
  101,   60,   65,  151,  137,   87,  163,   88,   55,   84,
   84,   84,   84,   80,  220,   80,   80,   80,   66,   56,
   79,   71,   12,   13,   12,   13,  222,   57,  221,  100,
    5,   80,   80,   80,   80,  101,  101,    6,    7,   78,
  223,   78,   78,   78,    9,   68,   61,  141,   12,   13,
   14,  133,   68,   15,  166,  163,  112,   78,   78,   78,
   78,   79,   74,   79,   79,   79,   16,   67,   33,   35,
  104,  111,   47,   12,   13,  141,  216,  217,   92,   79,
   79,   79,   79,   93,   78,   79,   68,    5,  227,  108,
  182,   40,  117,  155,    6,    7,  156,  101,  101,  101,
  101,    9,  101,  101,   68,   12,   13,   14,   67,  202,
   15,  109,  172,  174,  175,  176,   34,  177,  178,  183,
  191,  195,   87,  136,   88,   87,   67,   88,   66,  109,
  110,  192,  196,  108,  193,  197,   75,  101,  203,   66,
   90,  101,   66,  139,   91,   87,  158,   88,  108,  159,
  121,  188,  210,   97,  189,  109,  211,   63,  179,  180,
   64,  123,   48,  124,   76,  105,   48,   48,   60,   88,
  109,   69,  106,   88,   88,   88,   80,   48,   48,  142,
   81,   82,   83,   36,   37,   38,  102,  144,  106,  114,
   36,   37,   38,  184,  185,   75,   36,   37,   38,  145,
   36,   37,   38,   87,  148,   88,   84,   84,   17,  146,
   84,   84,   84,  147,   84,   84,   84,   84,  150,   84,
   84,   84,   84,   76,   84,   84,   84,   84,   80,   80,
   84,  149,   80,   80,   80,   35,   80,   80,   80,   80,
   77,   80,   80,   80,   80,  102,   80,   80,   80,   80,
  126,  127,   80,  154,   78,   78,  152,  153,   78,   78,
   78,  157,   78,   78,   78,   78,  160,   78,   78,   78,
   78,   44,   78,   78,   78,   78,   79,   79,   78,  106,
   79,   79,   79,  170,   79,   79,   79,   79,  186,   79,
   79,   79,   79,  187,   79,   79,   79,   79,  190,   77,
   79,   68,   68,   36,   37,   38,  134,  135,  194,   68,
   68,   68,   68,  198,   68,   68,   68,   68,  199,   68,
   68,   68,   68,   67,   67,   68,  119,  120,  200,  201,
   44,   67,   67,   67,   67,  204,   67,   67,   67,   67,
  205,   67,   67,   67,   67,  108,  108,   67,  206,  207,
  208,  212,  213,  108,  108,  218,  108,  224,  108,  108,
  108,  108,  219,  108,  108,  108,  108,  109,  109,  108,
  225,    7,  226,  228,    3,  109,  109,    6,  109,  107,
  109,  109,  109,  109,    5,  109,  109,  109,  109,  102,
   45,  109,   75,   75,  167,    0,    0,    0,    0,    0,
   75,   75,    0,   75,    0,   75,   75,   75,   75,    0,
   75,   75,   75,   75,    0,    0,   75,    0,    0,    0,
   76,   76,    0,    0,    0,    0,    0,    0,   76,   76,
    0,   76,    0,   76,   76,   76,   76,    0,   76,   76,
   76,   76,  102,  102,   76,    0,    0,    0,    0,    0,
  102,  102,    0,  102,    0,  102,  102,  102,  102,    0,
  102,  102,  102,  102,   17,   17,  102,    0,    0,    0,
    0,    0,   17,   17,    0,    0,    0,   17,    0,   17,
   17,    0,   17,   17,   17,   17,    0,   29,   17,   29,
    0,    0,    0,   29,    0,    0,   77,   77,    0,   44,
    0,   29,    0,   44,   77,   77,    0,   77,    0,   77,
   77,   77,   77,    0,   77,   77,   77,   77,   29,    0,
   77,    0,    0,    0,    0,    0,   44,   44,   44,    0,
   53,   44,   29,    0,    0,   44,   44,    0,    0,    0,
   44,   44,   44,   44,   44,   44,   44,   44,   44,    0,
    0,   44,    0,   73,    0,    0,    0,    0,    0,    0,
   44,   44,  118,  118,    0,    0,    0,   44,   96,    0,
    0,   99,   44,    0,    0,    0,    0,    0,    0,   44,
   44,   44,    0,  138,   44,   44,    0,  113,  115,    0,
    0,    0,    0,    0,  122,    0,   58,    5,   44,  125,
    0,    0,    0,    0,    6,    7,    0,    0,  128,   59,
    0,    9,   10,    0,   11,   12,   13,   14,    0,    0,
   15,    0,    0,    0,    0,  143,    0,    0,    0,    0,
  164,  164,    0,    0,    0,    0,    0,   58,    5,    0,
   44,   44,    0,    0,    0,    6,    7,    0,    0,    0,
   72,    0,    9,   10,   44,   11,   12,   13,   14,    0,
    0,   15,   94,    5,    0,    0,    0,  168,  169,    0,
    6,    7,    0,   44,    0,   95,    0,    9,   10,    0,
   11,   12,   13,   14,    0,    0,   15,    0,    0,    0,
   29,   29,   29,   58,    5,    0,    0,    0,    0,   29,
    0,    6,    7,   29,    0,    0,    0,    0,    9,   10,
    0,   11,   12,   13,   14,  129,    5,   15,    0,    0,
    0,    0,    0,    6,    7,    0,    0,    0,    0,    0,
    9,    0,  132,    5,   12,   13,   14,    0,    0,   15,
    6,    7,    0,    0,    0,    0,    0,    9,    0,  171,
    5,   12,   13,   14,    0,    0,   15,    6,    7,    0,
    0,    0,    0,    0,    9,    0,  173,    5,   12,   13,
   14,    0,    0,   15,    6,    7,    0,    0,    0,    0,
    0,    9,    0,  209,    5,   12,   13,   14,    0,    0,
   15,    6,    7,    0,    0,    0,    0,    0,    9,    0,
    0,    5,   12,   13,   14,    0,    0,   15,    6,    7,
    0,    0,    8,    0,    0,    9,   10,    0,   11,   12,
   13,   14,    5,    0,   15,    0,    0,    0,    0,    6,
    7,    0,    0,    0,    0,    0,    9,   10,    0,   11,
   12,   13,   14,    5,    0,   15,    0,    0,    0,    0,
    6,    7,    0,    0,    0,  140,    0,    9,    0,    0,
    0,   12,   13,   14,    0,    0,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   41,   44,   44,   44,   42,   43,   60,   45,  268,
   47,   40,  256,   43,    7,   45,   40,   39,   59,   44,
   16,   45,    0,   60,   61,   62,  123,  256,   45,   40,
   60,   61,   62,   40,   45,  279,   40,   33,   45,  256,
  257,   41,   42,   43,   44,   45,   39,   47,    0,  278,
   55,   47,  257,   41,  257,   43,  257,   45,  268,   59,
   60,   61,   62,   41,  256,   43,   44,   45,  273,  256,
    0,   44,  275,  276,  275,  276,  256,  264,  270,  256,
  257,   59,   60,   61,   62,   90,   91,  264,  265,   41,
  270,   43,   44,   45,  271,    0,   59,  102,  275,  276,
  277,   91,  263,  280,  256,  257,   44,   59,   60,   61,
   62,   41,  258,   43,   44,   45,    2,    0,    4,  123,
   59,   59,    8,  275,  276,  130,  206,  207,   42,   59,
   60,   61,   62,   47,  257,  258,   41,  257,  218,    0,
  162,   45,  257,  267,  264,  265,  270,  152,  153,  154,
  155,  271,  157,  158,   59,  275,  276,  277,   41,  181,
  280,    0,  152,  153,  154,  155,  263,  157,  158,  162,
  256,  256,   43,   41,   45,   43,   59,   45,  256,  256,
  257,  267,  267,   44,  270,  270,    0,  192,  181,  267,
  266,  196,  270,   41,  266,   43,  267,   45,   59,  270,
  125,   41,  192,  256,   44,   44,  196,   19,  256,  257,
   22,   41,  257,   41,    0,  256,  256,  257,  214,  256,
   59,  263,  263,  260,  261,  262,  256,  256,  273,  257,
  260,  261,  262,  257,  258,  259,    0,  257,  263,  256,
  257,  258,  259,  256,  257,   59,  257,  258,  259,   40,
  257,  258,  259,   43,   44,   45,  256,  257,    0,   40,
  260,  261,  262,  257,  264,  265,  266,  267,  256,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   44,  260,  261,  262,  123,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
   87,   88,  280,  267,  256,  257,  266,  266,  260,  261,
  262,  267,  264,  265,  266,  267,   62,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,  263,
  260,  261,  262,   41,  264,  265,  266,  267,   41,  269,
  270,  271,  272,   41,  274,  275,  276,  277,  270,   59,
  280,  256,  257,  257,  258,  259,   92,   93,  270,  264,
  265,  266,  267,  270,  269,  270,  271,  272,  270,  274,
  275,  276,  277,  256,  257,  280,   70,   71,  270,  270,
   59,  264,  265,  266,  267,   59,  269,  270,  271,  272,
  268,  274,  275,  276,  277,  256,  257,  280,  268,  268,
   41,   41,   41,  264,  265,  268,  267,  269,  269,  270,
  271,  272,  270,  274,  275,  276,  277,  256,  257,  280,
  269,    0,  269,  269,    0,  264,  265,    0,  267,   44,
  269,  270,  271,  272,    0,  274,  275,  276,  277,   55,
  269,  280,  256,  257,  146,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,   -1,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,    2,  280,    4,
   -1,   -1,   -1,    8,   -1,   -1,  256,  257,   -1,    7,
   -1,   16,   -1,   11,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   33,   -1,
  280,   -1,   -1,   -1,   -1,   -1,   34,  256,  257,   -1,
   11,   39,   47,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   49,  271,  272,   52,  274,  275,  276,  277,   -1,
   -1,  280,   -1,   34,   -1,   -1,   -1,   -1,   -1,   -1,
   68,   69,   70,   71,   -1,   -1,   -1,   75,   49,   -1,
   -1,   52,   80,   -1,   -1,   -1,   -1,   -1,   -1,   87,
   88,   89,   -1,   98,   92,   93,   -1,   68,   69,   -1,
   -1,   -1,   -1,   -1,   75,   -1,  256,  257,  106,   80,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   89,  269,
   -1,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,   -1,   -1,  106,   -1,   -1,   -1,   -1,
  145,  146,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
  148,  149,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,  272,  162,  274,  275,  276,  277,   -1,
   -1,  280,  256,  257,   -1,   -1,   -1,  148,  149,   -1,
  264,  265,   -1,  181,   -1,  269,   -1,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,   -1,   -1,   -1,
  205,  206,  207,  256,  257,   -1,   -1,   -1,   -1,  214,
   -1,  264,  265,  218,   -1,   -1,   -1,   -1,  271,  272,
   -1,  274,  275,  276,  277,  256,  257,  280,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,
  271,   -1,  256,  257,  275,  276,  277,   -1,   -1,  280,
  264,  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,  256,
  257,  275,  276,  277,   -1,   -1,  280,  264,  265,   -1,
   -1,   -1,   -1,   -1,  271,   -1,  256,  257,  275,  276,
  277,   -1,   -1,  280,  264,  265,   -1,   -1,   -1,   -1,
   -1,  271,   -1,  256,  257,  275,  276,  277,   -1,   -1,
  280,  264,  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,
   -1,  257,  275,  276,  277,   -1,   -1,  280,  264,  265,
   -1,   -1,  268,   -1,   -1,  271,  272,   -1,  274,  275,
  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,   -1,  271,  272,   -1,  274,
  275,  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,
  264,  265,   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,
   -1,  275,  276,  277,   -1,   -1,  280,
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
"prog : ID statement_list END",
"prog : ID BEGIN statement_list error",
"prog : ID statement_list",
"prog :",
"statement_list : statement",
"statement_list : statement_list statement",
"statement_list : statement_list error ';'",
"statement : executable_statement optional_semicolon",
"statement : declare_pair optional_semicolon",
"statement : declare_var",
"statement : declare_fun",
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
"declare_var : var_type var_list ';'",
"declare_var : var_type ID ';'",
"declare_var : var_type ID error",
"declare_fun : var_type FUN ID '(' parametro ')' BEGIN fun_body END",
"declare_fun : var_type FUN error '(' parametro ')' BEGIN fun_body END",
"declare_fun : var_type FUN ID '(' parametro ',' ')' BEGIN fun_body END",
"declare_fun : var_type FUN ID '(' error ')' BEGIN fun_body END",
"declare_pair : TYPEDEF PAIR '<' var_type '>' ID",
"declare_pair : TYPEDEF error",
"declare_pair : TYPEDEF PAIR error",
"declare_pair : TYPEDEF PAIR '<' var_type '>' error",
"var_list : ID ',' ID",
"var_list : var_list ',' ID",
"parametro : var_type ID",
"parametro : ID",
"parametro : var_type error",
"return_statement : RET '(' expr ')'",
"return_statement : RET expr",
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
"assign_statement : var_type ID ASSIGN expr",
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

//#line 288 "grammar.y"

/* ERRORES PENDIENTES: */
/*    falta sentencia ret en funcion: semantica  */
/*    cantidad erronea de parametros: ni permiti mas de 1 parametro no me di cuenta, pero la cantidad es semantica  */


/* recordar: $$ es el valor del lado izq de la regla. $n del n-ésimo del lado de la derecha */
/* con esto podemos verfiicar algunos errores en vez de reescribir reglas.. */



	public static void yyerror(String msg){
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){
                return AnalizadorLexico.yylex();
        }
        // valor a yylval (lexema)
//#line 584 "Parser.java"
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
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 24 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); }
break;
case 4:
//#line 25 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 26 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 27 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
break;
case 7:
//#line 28 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
break;
case 10:
//#line 35 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de sentencia.") ;}
break;
case 17:
//#line 56 "grammar.y"
{
            System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
break;
case 29:
//#line 84 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de declaracion incorrecta, asegurate haya ',' entre las variables si estas tratando de declarar varias.");}
break;
case 31:
//#line 88 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ; }
break;
case 32:
//#line 89 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": solo se permite 1 parametro. "); }
break;
case 33:
//#line 90 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro "); }
break;
case 35:
//#line 95 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 36:
//#line 96 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '<' .") ; }
break;
case 37:
//#line 97 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 41:
//#line 115 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 42:
//#line 116 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 44:
//#line 121 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 50:
//#line 141 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 51:
//#line 142 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 52:
//#line 143 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
break;
case 54:
//#line 145 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion ") ; }
break;
case 55:
//#line 146 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 56:
//#line 147 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia de ejecucicion en el IF ") ; }
break;
case 57:
//#line 148 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 59:
//#line 151 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 60:
//#line 152 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF "); }
break;
case 61:
//#line 153 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF ") ; }
break;
case 63:
//#line 155 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. ") ; }
break;
case 64:
//#line 156 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 65:
//#line 157 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia de ejecucicion en el IF ") ; }
break;
case 68:
//#line 171 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 77:
//#line 187 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 93:
//#line 234 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 95:
//#line 239 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro de OUTF "); }
break;
case 97:
//#line 245 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' "); }
break;
case 99:
//#line 247 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' "); }
break;
case 100:
//#line 248 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 101:
//#line 249 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 103:
//#line 254 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 111:
//#line 283 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 899 "Parser.java"
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

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
    0,    0,    0,    0,    0,    0,    0,    1,    1,    2,
    2,    2,    2,    2,    5,    5,    8,    8,    3,    3,
    3,    3,    3,    3,    3,    3,   16,   16,    6,    6,
    6,    7,    7,    7,    4,    4,    4,    4,   18,   18,
   19,   19,   19,   15,   15,   20,   17,   17,   17,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,   23,   22,   22,   24,
   24,   24,   24,   24,   24,   10,   10,   10,   21,   21,
   21,   21,   26,   26,   26,   27,   27,   27,   27,   27,
   27,   27,   25,   28,   28,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   14,   14,
   29,   29,   31,   31,   30,   30,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    9,    9,    9,    6,    5,    4,    6,    3,    3,
    2,    1,    2,    4,    2,    1,    1,    1,    1,    7,
    5,    6,    6,    7,    6,    7,    9,    7,    8,    8,
    9,    9,    7,    8,    8,    9,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    4,    3,    3,
    1,    1,    3,    3,    1,    1,    1,    2,    2,    1,
    1,    1,    4,    4,    5,    4,    3,    2,    8,    6,
    7,    7,    7,    5,    6,    6,    7,    7,    3,    3,
    3,    3,    1,    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   47,   48,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   14,    0,    0,   17,   26,    0,
    0,   87,   92,    0,    0,    0,    0,   91,    0,   85,
   90,    0,   98,    0,    0,    0,   82,    0,    0,    0,
  118,  117,    4,    9,   15,   11,   19,   20,   21,   22,
   23,   24,   25,    0,    0,    0,    0,    0,    0,    0,
    2,    0,    0,    0,    0,   89,   88,   73,   74,   75,
   70,   71,   72,    0,    0,    0,    0,    0,    0,    0,
    0,    1,   97,    0,   49,    0,    0,    0,    0,    0,
   27,    0,    0,   31,    0,   30,    0,    0,    0,   29,
    0,    0,    0,    0,    0,    0,  114,  112,  111,   93,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   83,
   84,   96,    0,   37,    0,   44,    0,    0,   28,    0,
    0,   39,    0,    0,   40,    0,    0,    0,   94,    0,
    0,    0,   51,    0,    0,   36,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   95,    0,   52,
    0,    0,   55,    0,    0,    0,   53,   38,   35,    0,
  106,    0,    0,    0,   43,   41,    0,    0,    0,    0,
    0,   56,   58,    0,   54,    0,   50,   63,    0,  103,
  108,    0,  102,  107,    0,    0,    0,   64,    0,   59,
    0,   60,    0,   65,   99,    0,    0,    0,    0,   62,
   61,   66,   57,   33,   34,   32,
};
final static short yydgoto[] = {                          3,
  226,   18,  111,   20,   66,   21,   22,   39,   23,   24,
   25,   26,   27,   28,   29,  137,  113,   76,  173,  227,
   46,   47,  138,   96,   31,   49,   50,   51,   32,  125,
   33,
};
final static short yysindex[] = {                      -216,
 -199,  591,    0,  756,   39, -115,   54,  -16,  756,   -9,
  -53,   -8,    0,    0, -151, -239,  668,    0,    0,   76,
    0,    0,   76,   76,   76,   76,   76,   76,   76, -247,
 -213,  -35,  103,  690,    0,  121,  -94,    0,    0,    0,
  -28,    0,    0,  285, -177,   -7,  -39,    0,    9,    0,
    0,  712,    0,  241,  -55, -218,    0,  121,  -27,  -79,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -38, -169,  -31,  121,  294, -103, -103,
    0,  -27,   48,  121,  -37,    0,    0,    0,    0,    0,
    0,    0,    0,  393,  393,  121,  643,  -96,  393,  393,
   39,    0,    0,   82,    0, -218,  -81,  120,   93,  -89,
    0,  612,  -73,    0,  121,    0,  -66,  153,  154,    0,
  -54,  -27,    0,  106,  156,   81,    0,    0,    0,    0,
   56,  643,  -61,    9,    9,  -27,  643, -195,  643,    0,
    0,    0,  144,    0,  -49,    0,  143,  -40,    0,  -52,
  -27,    0, -198, -242,    0,  121,  121,  169,    0, -191,
  734,  643,    0, -163, -128,    0,  285,  172,  167,  285,
    0, -104,  173,  176,  178,  -27,  -27,    0,  643,    0,
 -124,  799,    0, -167,  -48,  643,    0,    0,    0,  182,
    0,  263,  195,  204,    0,    0,  -12,   -4,   66,  -18,
  643,    0,    0,  -10,    0,  778,    0,    0,   -1,    0,
    0,  266,    0,    0,  756,  756,  756,    0,   47,    0,
   79,    0, -150,    0,    0,  756,   85,   86,   90,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       364,
  384,    0,    0,    0,    0,  -41,  259,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  386,    0,    0,  496,
    0,    0,  468,  468,  468,  468,  468,  468,  468,    0,
  375,    0,    0,    0,    0,    0,    0,    0,    0,   96,
    1,    0,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  215,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  237,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  408,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  291,  118,    0,  322,  -33,    0,    0,    0,    0,
    0,    0,    0,   49,   71,  140, -165,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  344,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  369,    0,    0,
   45,    0,    0,    0,    0,  165,  187,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  391,
    0,    0,  418,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  446,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  158,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  117,   13,  695,    0, 1057,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  353,   18,    0,  277,  -57,
  738,  -25,  602,    0,  746,   74,   75,    0,    0,    0,
  100,
};
final static int YYTABLESIZE=1086;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        170,
   86,   98,  113,  133,  106,  117,   56,   37,   79,   74,
  113,   84,  121,  174,  171,   94,   61,   95,   85,   30,
  116,   30,   81,   44,   62,   75,   30,  120,   45,   64,
   54,   58,   13,   14,   30,   94,   45,   95,  105,    1,
    2,   86,   86,   86,   86,   86,   64,   86,   79,   77,
   99,   30,   91,   93,   92,  100,   13,   14,  171,   86,
   86,   86,   86,   81,   64,   81,   81,   81,    4,   30,
   80,  162,  107,  108,  163,  179,   13,   14,  180,   86,
   87,   81,   81,   81,   81,   42,  118,  119,  205,   79,
   67,   79,   79,   79,   37,   69,  159,   35,   94,  206,
   95,   67,  207,  186,   67,  232,  187,   79,   79,   79,
   79,   80,   38,   80,   80,   80,   60,  110,   17,  233,
   34,  168,  142,  143,   94,   52,   95,  188,  189,   80,
   80,   80,   80,  146,   65,   94,   69,   95,   82,   68,
   82,  190,  201,  193,  194,  202,   80,   36,   94,  156,
   95,  195,  196,  126,   69,   82,   82,   82,  228,  229,
   82,   82,   82,   83,  115,   45,  212,  134,  135,  139,
  172,  172,  130,  140,  141,  144,  110,    6,  128,  129,
   68,  145,  167,  150,    7,    8,  116,   45,  147,  110,
  152,   10,  153,  154,   12,   13,   14,   15,   68,  157,
   16,  105,  155,   37,  161,  165,  192,  166,  115,  178,
  115,   45,  191,  197,   45,   49,  198,  114,  199,   13,
   14,  208,  210,  115,  115,   55,   97,   78,  132,  113,
  116,   49,   30,   30,   30,  213,   76,  169,   64,   40,
   41,   42,   43,   30,  214,  116,   53,   57,   41,   42,
   43,  218,   88,   89,   90,  215,   86,   86,   18,  220,
   86,   86,   86,  216,   86,   86,   86,   86,  224,   86,
   86,   86,   86,   45,   86,   86,   86,   86,   81,   81,
   86,  103,   81,   81,   81,   45,   81,   81,   81,   81,
   77,   81,   81,   81,   81,   76,   81,   81,   81,   81,
   49,   49,   81,  211,   79,   79,  225,   45,   79,   79,
   79,  158,   79,   79,   79,   79,  230,   79,   79,   79,
   79,  109,   79,   79,   79,   79,   80,   80,   79,   45,
   80,   80,   80,  217,   80,   80,   80,   80,   45,   80,
   80,   80,   80,   78,   80,   80,   80,   80,  231,   77,
   80,   69,   69,  234,  235,   82,   82,   82,  236,   69,
   69,   69,   69,    7,   69,   69,   69,   69,  104,   69,
   69,   69,   69,  110,  110,   69,   57,   41,   42,   43,
  109,  110,  110,    3,  110,    6,  110,  110,  110,  110,
  105,  110,  110,  110,  110,   68,   68,  110,   40,   41,
   42,   43,   78,   68,   68,   68,   68,    5,   68,   68,
   68,   68,  112,   68,   68,   68,   68,  100,  114,   68,
  115,  115,   40,   41,   42,   43,   46,  104,  115,  115,
  175,  115,    0,  115,  115,  115,  115,   45,  115,  115,
  115,  115,  116,  116,  115,  101,    0,    0,    0,  105,
  116,  116,    0,  116,    0,  116,  116,  116,  116,    0,
  116,  116,  116,  116,    0,    0,  116,   16,    0,    0,
   45,   45,    0,    0,    0,    0,  100,    0,   45,   45,
    0,   45,    0,   45,   45,   45,   45,    0,   45,   45,
   45,   45,   76,   76,   45,   16,   57,   41,   42,   43,
   76,   76,    0,   76,  101,   76,   76,   76,   76,    0,
   76,   76,   76,   76,   18,   18,   76,    0,   40,   41,
   42,   43,   18,   18,    0,   18,    0,   18,   18,   18,
   18,    0,   18,   18,   18,   18,    0,    0,   18,    0,
   40,   41,   42,   43,    0,    0,   77,   77,    0,  123,
   41,   42,   43,    0,   77,   77,    0,   77,    0,   77,
   77,   77,   77,    0,   77,   77,   77,   77,    0,    0,
   77,    0,    0,    0,    0,    0,    0,  109,  109,    0,
    0,    0,    0,    0,    0,  109,  109,    0,  109,    0,
  109,  109,  109,  109,    0,  109,  109,  109,  109,   78,
   78,  109,    0,    0,    0,    0,    0,   78,   78,    0,
   78,    0,   78,   78,   78,   78,    0,   78,   78,   78,
   78,    0,    0,   78,  104,  104,    0,    0,    0,    0,
    0,    0,  104,  104,    0,  104,    0,  104,  104,  104,
  104,    0,  104,  104,  104,  104,  105,  105,  104,   41,
   42,   43,    0,    0,  105,  105,    0,  105,    0,  105,
  105,  105,  105,    0,  105,  105,  105,  105,    0,    0,
  105,    0,    0,  100,  100,    0,    0,    0,    0,    0,
    0,  100,  100,    0,  100,    0,  100,  100,  100,  100,
    0,  100,  100,  100,  100,    0,   19,  100,   19,    0,
    0,  101,  101,   19,    0,    0,    0,    0,    0,  101,
  101,   19,  101,    0,  101,  101,  101,  101,    0,  101,
  101,  101,  101,   16,   16,  101,    0,    0,   19,    0,
    0,   16,   16,  160,   16,    0,   16,   16,   16,   16,
  164,   16,   16,   16,   16,    0,   19,   16,    0,   59,
    0,   16,   16,   48,    0,    0,    0,   48,    0,   16,
   16,    0,  184,  185,   16,    0,   16,   16,    0,   16,
   16,   16,   16,   82,    0,   16,    0,    0,    0,    0,
  200,   48,    0,  204,    0,    0,    0,  209,    0,   48,
    0,  104,    0,    0,    0,  109,    0,    0,    0,   48,
    0,    0,  219,   48,    0,    0,  149,  223,    0,    0,
    0,    0,    0,    0,  122,  124,    0,    0,    0,    0,
    0,  131,   48,   48,  127,  127,    0,    0,    0,   48,
    0,  149,    0,  136,    0,    0,    0,    0,    0,   48,
   48,   48,    0,    0,   48,   48,    5,    6,    0,    0,
    0,    0,  151,    0,    7,    8,    0,    0,    9,    0,
   48,   10,   11,    0,   12,   13,   14,   15,    6,    0,
   16,    0,    0,    0,    0,    7,    8,    0,    0,    0,
  148,    0,   10,    0,    0,   12,   13,   14,   15,    0,
    0,   16,   48,  176,  177,    0,    0,    0,    0,    6,
    0,   48,   48,    0,    0,    0,    7,    8,    0,   19,
   19,   19,   48,   10,   48,   48,   12,   13,   14,   15,
   19,    0,   16,    5,    6,    0,    0,    0,    0,    0,
    0,    7,    8,    0,    0,    0,   63,   48,   10,   11,
    0,   12,   13,   14,   15,    5,    6,   16,    0,    0,
    0,    0,    0,    7,    8,    0,    0,    0,   81,    0,
   10,   11,    0,   12,   13,   14,   15,  101,    6,   16,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
  102,    0,   10,   11,    0,   12,   13,   14,   15,  181,
    6,   16,    0,    0,    0,    0,    0,    7,    8,    0,
  182,    0,    0,  183,   10,    0,    0,   12,   13,   14,
   15,    5,    6,   16,    0,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,    0,   10,   11,    0,   12,
   13,   14,   15,  221,    6,   16,    0,    0,    0,    0,
    0,    7,    8,    0,    0,    0,    0,  222,   10,    0,
    0,   12,   13,   14,   15,    6,    0,   16,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,  203,   10,
    0,    0,   12,   13,   14,   15,    0,    0,   16,   67,
   68,   69,   70,   71,   72,   73,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   44,   41,   60,   44,   60,  123,   44,  257,
   44,   40,   44,  256,  257,   43,  256,   45,   44,    2,
   59,    4,    0,   40,  264,  273,    9,   59,   45,   17,
   40,   40,  275,  276,   17,   43,   45,   45,  257,  256,
  257,   41,   42,   43,   44,   45,   34,   47,    0,  263,
   42,   34,   60,   61,   62,   47,  275,  276,  257,   59,
   60,   61,   62,   41,   52,   43,   44,   45,  268,   52,
    0,  267,   55,   56,  270,  267,  275,  276,  270,  257,
  258,   59,   60,   61,   62,   41,  256,  257,  256,   41,
  256,   43,   44,   45,  123,    0,   41,   59,   43,  267,
   45,  267,  270,  267,  270,  256,  270,   59,   60,   61,
   62,   41,   59,   43,   44,   45,  268,    0,    2,  270,
    4,  147,   41,  106,   43,    9,   45,  256,  257,   59,
   60,   61,   62,   41,   59,   43,   41,   45,   43,    0,
   45,  167,  267,  169,  170,  270,   44,  263,   43,   44,
   45,  256,  257,  257,   59,   60,   61,   62,  216,  217,
   43,   44,   45,  258,    0,   45,  192,   94,   95,  266,
  153,  154,  125,   99,  100,  257,   59,  257,   79,   80,
   41,   62,   40,  257,  264,  265,    0,   45,  278,  269,
  257,  271,   40,   40,  274,  275,  276,  277,   59,   44,
  280,  257,  257,  123,  266,   62,   40,  257,   44,   41,
  263,   45,   41,   41,    0,  257,   41,  256,   41,  275,
  276,  270,   41,   59,  263,  279,  266,  263,  266,  263,
   44,  273,  215,  216,  217,   41,    0,  278,  226,  256,
  257,  258,  259,  226,   41,   59,  256,  256,  257,  258,
  259,  270,  260,  261,  262,  268,  256,  257,    0,  270,
  260,  261,  262,  268,  264,  265,  266,  267,  270,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   41,  260,  261,  262,   45,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  256,  257,  280,   41,  256,  257,   41,   45,  260,  261,
  262,  256,  264,  265,  266,  267,  270,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,   45,
  260,  261,  262,  268,  264,  265,  266,  267,   45,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  270,   59,
  280,  256,  257,  269,  269,  260,  261,  262,  269,  264,
  265,  266,  267,    0,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,    0,  267,    0,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,    0,  269,  270,
  271,  272,   60,  274,  275,  276,  277,    0,   44,  280,
  256,  257,  256,  257,  258,  259,  269,   59,  264,  265,
  154,  267,   -1,  269,  270,  271,  272,   45,  274,  275,
  276,  277,  256,  257,  280,    0,   -1,   -1,   -1,   59,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,    0,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   59,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,  256,  257,  258,  259,
  264,  265,   -1,  267,   59,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,  256,  257,
  258,  259,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
  256,  257,  258,  259,   -1,   -1,  256,  257,   -1,  256,
  257,  258,  259,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,   -1,  280,  256,  257,   -1,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,  256,  257,  280,  257,
  258,  259,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,    2,  280,    4,   -1,
   -1,  256,  257,    9,   -1,   -1,   -1,   -1,   -1,  264,
  265,   17,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   34,   -1,
   -1,  264,  265,  132,  267,   -1,  269,  270,  271,  272,
  139,  274,  275,  276,  277,   -1,   52,  280,   -1,   12,
   -1,  256,  257,    8,   -1,   -1,   -1,   12,   -1,  264,
  265,   -1,  161,  162,  269,   -1,  271,  272,   -1,  274,
  275,  276,  277,   36,   -1,  280,   -1,   -1,   -1,   -1,
  179,   36,   -1,  182,   -1,   -1,   -1,  186,   -1,   44,
   -1,   54,   -1,   -1,   -1,   58,   -1,   -1,   -1,   54,
   -1,   -1,  201,   58,   -1,   -1,  112,  206,   -1,   -1,
   -1,   -1,   -1,   -1,   77,   78,   -1,   -1,   -1,   -1,
   -1,   84,   77,   78,   79,   80,   -1,   -1,   -1,   84,
   -1,  137,   -1,   96,   -1,   -1,   -1,   -1,   -1,   94,
   95,   96,   -1,   -1,   99,  100,  256,  257,   -1,   -1,
   -1,   -1,  115,   -1,  264,  265,   -1,   -1,  268,   -1,
  115,  271,  272,   -1,  274,  275,  276,  277,  257,   -1,
  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,   -1,
   -1,  280,  147,  156,  157,   -1,   -1,   -1,   -1,  257,
   -1,  156,  157,   -1,   -1,   -1,  264,  265,   -1,  215,
  216,  217,  167,  271,  169,  170,  274,  275,  276,  277,
  226,   -1,  280,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,  269,  192,  271,  272,
   -1,  274,  275,  276,  277,  256,  257,  280,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,  269,   -1,
  271,  272,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
  267,   -1,   -1,  270,  271,   -1,   -1,  274,  275,  276,
  277,  256,  257,  280,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,   -1,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,   -1,  270,  271,   -1,
   -1,  274,  275,  276,  277,  257,   -1,  280,   -1,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,  270,  271,
   -1,   -1,  274,  275,  276,  277,   -1,   -1,  280,   23,
   24,   25,   26,   27,   28,   29,
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
"statement : executable_statement",
"statement : declare_pair optional_semicolon",
"statement : declare_var",
"statement : declare_fun",
"statement : error ';'",
"optional_semicolon : ';'",
"optional_semicolon :",
"optional_not_semicolon : ';'",
"optional_not_semicolon :",
"executable_statement : if_statement optional_semicolon",
"executable_statement : assign_statement optional_semicolon",
"executable_statement : outf_statement optional_semicolon",
"executable_statement : repeat_statement optional_semicolon",
"executable_statement : goto_statement optional_semicolon",
"executable_statement : mult_assign_statement optional_semicolon",
"executable_statement : return_statement optional_semicolon",
"executable_statement : TAG optional_not_semicolon",
"executable_statement_list : executable_statement",
"executable_statement_list : executable_statement_list executable_statement",
"declare_var : var_type var_list ';'",
"declare_var : var_type ID ';'",
"declare_var : var_type ID error",
"declare_fun : var_type FUN ID '(' parametro ')' BEGIN fun_body END",
"declare_fun : var_type FUN error '(' parametro ')' BEGIN fun_body END",
"declare_fun : var_type FUN ID '(' error ')' BEGIN fun_body END",
"declare_pair : TYPEDEF PAIR '<' var_type '>' ID",
"declare_pair : TYPEDEF '<' var_type '>' ID",
"declare_pair : TYPEDEF PAIR var_type ID",
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
"if_statement : IF '(' cond THEN ctrl_block_statement END_IF",
"if_statement : IF cond ')' THEN ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement error",
"if_statement : IF '(' cond ')' THEN END_IF",
"if_statement : IF '(' cond ')' THEN error END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ELSE END_IF",
"if_statement : IF '(' cond ')' THEN ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE error END_IF",
"if_statement : IF '(' cond ')' THEN error ELSE ctrl_block_statement END_IF",
"if_statement : IF cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' cond THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement error",
"ctrl_block_statement : executable_statement_list",
"cond : expr cond_op expr",
"cond : error",
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
"expr : error",
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
"outf_statement : OUTF '(' ')'",
"outf_statement : OUTF error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL cond",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL cond ')'",
"repeat_statement : REPEAT BEGIN END UNTIL '(' cond ')'",
"repeat_statement : REPEAT BEGIN END UNTIL cond",
"repeat_statement : REPEAT BEGIN END UNTIL '(' cond",
"repeat_statement : REPEAT BEGIN END UNTIL cond ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END '(' cond ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' ')'",
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

//#line 319 "grammar.y"

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
//#line 630 "Parser.java"
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
case 11:
//#line 40 "grammar.y"
{System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
break;
case 12:
//#line 41 "grammar.y"
{System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
break;
case 13:
//#line 42 "grammar.y"
{System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 43 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
break;
case 16:
//#line 54 "grammar.y"
{
            System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
break;
case 17:
//#line 61 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 66 "grammar.y"
{System.out.println("Sentencia de control IF en linea "+AnalizadorLexico.line_number);}
break;
case 20:
//#line 68 "grammar.y"
{System.out.println("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
break;
case 21:
//#line 69 "grammar.y"
{System.out.println("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
break;
case 22:
//#line 71 "grammar.y"
{System.out.println("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 23:
//#line 72 "grammar.y"
{System.out.println("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
break;
case 24:
//#line 74 "grammar.y"
{System.out.println("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
break;
case 25:
//#line 76 "grammar.y"
{System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 26:
//#line 77 "grammar.y"
{System.out.println("Sentencia de TAG");}
break;
case 31:
//#line 89 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de declaracion incorrecta, asegurate haya ',' entre las variables si estas tratando de declarar varias.");}
break;
case 33:
//#line 93 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ; }
break;
case 34:
//#line 94 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");}
break;
case 36:
//#line 99 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 37:
//#line 100 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 38:
//#line 101 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 42:
//#line 119 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 43:
//#line 120 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 45:
//#line 125 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 51:
//#line 146 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 52:
//#line 147 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 53:
//#line 148 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 54:
//#line 149 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 55:
//#line 150 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
break;
case 56:
//#line 151 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 58:
//#line 155 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
break;
case 59:
//#line 156 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }
break;
case 60:
//#line 157 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 61:
//#line 158 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
break;
case 62:
//#line 159 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; }
break;
case 63:
//#line 160 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este dentro de parentesis. ") ; }
break;
case 64:
//#line 161 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 65:
//#line 162 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 66:
//#line 163 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 69:
//#line 190 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 78:
//#line 206 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 82:
//#line 212 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 95:
//#line 253 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 97:
//#line 258 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 98:
//#line 259 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 100:
//#line 267 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 101:
//#line 268 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 102:
//#line 269 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 103:
//#line 275 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 104:
//#line 276 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 105:
//#line 277 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 106:
//#line 278 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 107:
//#line 279 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 108:
//#line 280 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 110:
//#line 285 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 118:
//#line 314 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1025 "Parser.java"
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

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
        import PrimeraEtapa.*;
        import TercerEtapa.*;

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
    6,    6,    7,   19,   19,   19,    4,    4,    4,    4,
   18,   18,   21,   21,   21,   15,   15,   20,   17,   17,
   17,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,   25,   24,
   23,   23,   26,   26,   26,   26,   26,   26,   10,   10,
   10,   22,   22,   22,   22,   28,   28,   28,   29,   29,
   29,   29,   29,   29,   29,   27,   30,   30,   11,   11,
   11,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   14,   14,   31,   31,   33,   33,   32,   32,   13,
   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    3,    7,    7,    7,    6,    5,    4,    6,
    3,    3,    2,    1,    2,    4,    2,    1,    1,    1,
    1,    7,    5,    6,    6,    7,    6,    7,    9,    7,
    8,    8,    9,    9,    7,    8,    8,    9,    4,    1,
    3,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    4,    3,    3,    1,    1,    3,    3,    1,    1,    1,
    2,    2,    1,    1,    1,    4,    4,    5,    4,    3,
    2,    8,    6,    7,    7,    7,    5,    6,    6,    7,
    7,    3,    3,    3,    3,    1,    1,    3,    3,    2,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   49,   50,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   14,    0,    0,   17,   26,
    0,    0,   90,   95,    0,    0,    0,    0,   94,    0,
   88,   93,    0,  101,    0,    0,    0,   85,    0,    0,
    0,  121,  120,    4,    9,   15,   11,   19,   20,   21,
   22,   23,   24,   25,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    2,    0,    0,    0,    0,   92,   91,
   76,   77,   78,   73,   74,   75,    0,    0,    0,    0,
    0,    0,    0,    0,    1,  100,    0,   51,    0,    0,
    0,    0,    0,   27,    0,    0,   32,    0,   31,    0,
    0,    0,   30,   29,    0,   33,    0,    0,    0,    0,
    0,  117,  115,  114,   96,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   86,   87,   99,    0,   39,    0,
   46,    0,    0,   28,    0,    0,   41,    0,    0,   42,
    0,    0,    0,   97,    0,    0,    0,   53,    0,    0,
   38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   98,    0,   54,    0,    0,   57,    0,    0,
    0,   55,   40,   37,    0,  109,    0,    0,    0,   45,
   43,    0,    0,    0,    0,    0,   58,   60,    0,   56,
    0,   52,   65,    0,  106,  111,    0,  105,  110,   35,
   36,   34,   66,    0,   61,    0,   62,    0,   67,  102,
   64,   63,   68,   59,
};
final static short yydgoto[] = {                          3,
   17,   18,  114,   20,   67,   21,   22,   40,   23,   24,
   25,   26,   27,   28,   29,  142,  116,   77,   31,   79,
  178,   47,   48,  143,    0,   99,   49,   50,   51,   52,
   33,  130,   34,
};
final static short yysindex[] = {                      -122,
 -249,  593,    0,  742,  -12, -115,   -8,  -16,  742,  -20,
  -53,   -9,    0,    0, -229, -224,  629,    0,    0,   -6,
    0,    0,   -6,   -6,   -6,   -6,   -6,   -6,   -6, -235,
  742, -204,  -35,   21,  672,    0,  121, -180,    0,    0,
    0,  -28,    0,    0,  262,  -89,  -27,  -39,    0,   27,
    0,    0,  698,    0,   -4,  -55, -150,    0,  121,  -17,
  -79,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -38,  -63,  -34,  742, -163,  121,
  285, -134, -134,    0,  -17,    2,  121,  -37,    0,    0,
    0,    0,    0,    0,    0,    0,  294,  294,  121,  530,
 -137,  294,  294,  -12,    0,    0,   46,    0, -150, -114,
   98,  131, -107,    0,  785,  -93,    0,  121,    0,  -82,
  142,  144,    0,    0,  -68,    0,  -17,    0,  160,  156,
   68,    0,    0,    0,    0,   13,  530,  -60,   27,   27,
  -17,  530, -253,  530,    0,    0,    0,  146,    0,  -18,
    0,  143,  -40,    0,  -44,  -17,    0, -129, -155,    0,
  121,  121,  176,    0, -195,  720,  530,    0, -117,  -46,
    0,  262,  182,  167,  262,    0,  -43,  210,  219,  223,
  -17,  -17,    0,  530,    0, -116,  806,    0, -118,  -14,
  530,    0,    0,    0,  263,    0,  241,  267,  271,    0,
    0,   66,   81,   91,   47,  530,    0,    0,   94,    0,
  764,    0,    0,  114,    0,    0,  345,    0,    0,    0,
    0,    0,    0,  138,    0,  149,    0, -243,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                       413,
  427,    0,    0,    0,    0,  -41,  259,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  431,    0,    0,  496,
    0,    0,  468,  468,  468,  468,  468,  468,  468,    0,
    0,  389,    0,    0,    0,    0,    0,    0,    0,    0,
   96,    1,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  215,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  169,    0,    0,
    0,    0,    0,    0,  237,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  449,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  291,  118,    0,  322,
  -33,    0,    0,    0,    0,    0,    0,    0,   49,   71,
  140,  -97,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  344,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  369,    0,    0,   45,    0,    0,    0,    0,
  165,  187,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  391,    0,    0,  418,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  446,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   72,   35,  782,    0,  795,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  392,  781,    0,    0,    0,
  296,   18,  -30,  -87,    0,    0,  747,  147,  252,    0,
    0,    0,  365,
};
final static int YYTABLESIZE=1086;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        175,
   89,  101,  116,  138,  109,  120,   57,   38,   82,  125,
  116,   87,  233,  167,   88,   97,  168,   98,    4,   55,
  119,   75,   84,   45,  124,   97,  234,   98,   46,   60,
   59,   62,   94,   96,   95,   46,  106,   76,   61,   63,
   46,   89,   89,   89,   89,   89,   36,   89,   82,  165,
   39,   65,   66,  164,   85,   97,  169,   98,   80,   89,
   89,   89,   89,   84,   83,   84,   84,   84,  102,   65,
   83,  184,  107,  103,  185,   35,  112,   86,  189,  190,
   53,   84,   84,   84,   84,   44,  147,   65,   97,   82,
   98,   82,   82,   82,   38,   72,  205,  127,  129,  209,
  179,  176,   78,  214,  136,  126,  108,   82,   82,   82,
   82,   83,   65,   83,   83,   83,  141,  113,  224,   13,
   14,  173,  131,  228,   13,   14,  135,  176,  144,   83,
   83,   83,   83,    1,    2,  156,   72,  210,   85,   71,
   85,  195,  149,  198,  199,   13,   14,   37,  211,  191,
  206,  212,  192,  207,   72,   85,   85,   85,   70,  150,
   85,   85,   85,  155,  118,   46,  217,   89,   90,   70,
  152,  151,   70,   97,  157,   98,  113,    6,  181,  182,
   71,  158,  172,  159,    7,    8,  119,   46,  160,  113,
   38,   10,  121,  122,   12,   13,   14,   15,   71,  162,
   16,  108,   97,  161,   98,  166,  197,  170,  118,  193,
  194,   46,  200,  201,   47,   51,  183,  117,  118,   13,
   14,  123,  196,  118,  118,   56,  100,   81,  137,  116,
  119,   51,   91,   92,   93,   54,   79,  174,  171,   41,
   42,   43,   44,  139,  140,  119,   58,   42,   43,   44,
  202,   58,   42,   43,   44,  213,   89,   89,   18,  203,
   89,   89,   89,  204,   89,   89,   89,   89,  163,   89,
   89,   89,   89,   47,   89,   89,   89,   89,   84,   84,
   89,  216,   84,   84,   84,   46,   84,   84,   84,   84,
   80,   84,   84,   84,   84,   79,   84,   84,   84,   84,
   51,   51,   84,  215,   82,   82,   46,  218,   82,   82,
   82,  219,   82,   82,   82,   82,  223,   82,   82,   82,
   82,  112,   82,   82,   82,   82,   83,   83,   82,   46,
   83,   83,   83,  220,   83,   83,   83,   83,   46,   83,
   83,   83,   83,   81,   83,   83,   83,   83,  221,   80,
   83,   72,   72,  145,  146,   85,   85,   85,  222,   72,
   72,   72,   72,  225,   72,   72,   72,   72,  107,   72,
   72,   72,   72,  113,  113,   72,   58,   42,   43,   44,
  112,  113,  113,  229,  113,  230,  113,  113,  113,  113,
  108,  113,  113,  113,  113,   71,   71,  113,   41,   42,
   43,   44,   81,   71,   71,   71,   71,  231,   71,   71,
   71,   71,    7,   71,   71,   71,   71,  103,  232,   71,
  118,  118,   41,   42,   43,   44,    3,  107,  118,  118,
    6,  118,  117,  118,  118,  118,  118,   48,  118,  118,
  118,  118,  119,  119,  118,  104,  133,  134,    5,  108,
  119,  119,  115,  119,  180,  119,  119,  119,  119,    0,
  119,  119,  119,  119,    0,    0,  119,   16,    0,    0,
   47,   47,    0,    0,    0,    0,  103,    0,   47,   47,
    0,   47,    0,   47,   47,   47,   47,    0,   47,   47,
   47,   47,   79,   79,   47,   16,   41,   42,   43,   44,
   79,   79,    0,   79,  104,   79,   79,   79,   79,    0,
   79,   79,   79,   79,   18,   18,   79,   41,   42,   43,
   44,    0,   18,   18,    0,   18,    0,   18,   18,   18,
   18,    0,   18,   18,   18,   18,    0,    0,   18,    0,
  128,   42,   43,   44,    0,    0,   80,   80,    0,    0,
   42,   43,   44,    0,   80,   80,    0,   80,    0,   80,
   80,   80,   80,    0,   80,   80,   80,   80,    0,    0,
   80,    0,    0,    0,    0,    0,    0,  112,  112,    0,
    0,    0,    0,    0,    0,  112,  112,    0,  112,    0,
  112,  112,  112,  112,    0,  112,  112,  112,  112,   81,
   81,  112,    0,    0,    0,    0,    0,   81,   81,    0,
   81,    0,   81,   81,   81,   81,    0,   81,   81,   81,
   81,    0,    0,   81,  107,  107,    0,    0,    0,    0,
    0,    0,  107,  107,    0,  107,    0,  107,  107,  107,
  107,    0,  107,  107,  107,  107,  108,  108,  107,    0,
    0,    0,    0,    0,  108,  108,    0,  108,    0,  108,
  108,  108,  108,    0,  108,  108,  108,  108,    0,    0,
  108,    0,    0,  103,  103,    0,    0,    0,    0,    0,
    0,  103,  103,    0,  103,    0,  103,  103,  103,  103,
    0,  103,  103,  103,  103,    0,    0,  103,    0,    0,
    0,  104,  104,    0,    0,    0,    0,    0,    0,  104,
  104,    0,  104,    0,  104,  104,  104,  104,    0,  104,
  104,  104,  104,   16,   16,  104,    0,    0,    0,    0,
    0,   16,   16,    0,   16,    0,   16,   16,   16,   16,
    0,   16,   16,   16,   16,    0,    0,   16,   32,    0,
   32,   16,   16,    0,    0,   32,    0,    0,    0,   16,
   16,    0,    0,   32,   16,    0,   16,   16,    0,   16,
   16,   16,   16,    0,    0,   16,    0,   32,    0,    0,
    0,   32,   30,   19,   30,   19,    6,    0,    0,   30,
   19,    0,    0,    7,    8,    0,    0,   30,   19,   32,
   10,    0,    0,   12,   13,   14,   15,   32,    0,   16,
    0,   30,   19,    0,    0,   30,   19,   68,   69,   70,
   71,   72,   73,   74,   32,    0,    0,    0,  132,  132,
    0,    0,    0,   30,   19,    0,  110,  111,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    5,    6,
    0,    0,    0,    0,    0,    0,    7,    8,   30,   19,
    9,   32,    0,   10,   11,    0,   12,   13,   14,   15,
    0,    0,   16,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   32,    5,    6,    0,    0,   32,  148,
   32,    0,    7,    8,    0,    0,  154,   64,    0,   10,
   11,    0,   12,   13,   14,   15,    0,    0,   16,    0,
    0,    0,   32,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  154,    0,    0,    0,    5,    6,    0,
   32,    0,    0,   32,    0,    7,    8,   32,  177,  177,
   84,    0,   10,   11,    0,   12,   13,   14,   15,    0,
    0,   16,   32,  104,    6,    0,    0,   32,    0,    0,
    0,    7,    8,    0,    0,    0,  105,    0,   10,   11,
    0,   12,   13,   14,   15,  186,    6,   16,    0,    0,
    0,    0,    0,    7,    8,    0,  187,    0,    0,  188,
   10,    0,    0,   12,   13,   14,   15,    5,    6,   16,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
    0,    0,   10,   11,    0,   12,   13,   14,   15,  226,
    6,   16,    0,    0,    0,    0,    0,    7,    8,    0,
    0,    0,    0,  227,   10,    0,    0,   12,   13,   14,
   15,    6,    0,   16,    0,    0,    0,    0,    7,    8,
    0,    0,    0,  153,    0,   10,    0,    0,   12,   13,
   14,   15,    6,    0,   16,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,  208,   10,    0,    0,   12,
   13,   14,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   44,   41,   60,   44,   60,  123,   44,   44,
   44,   40,  256,  267,   45,   43,  270,   45,  268,   40,
   59,  257,    0,   40,   59,   43,  270,   45,   45,   12,
   40,  256,   60,   61,   62,   45,   41,  273,  268,  264,
   45,   41,   42,   43,   44,   45,   59,   47,    0,  137,
   59,   17,   59,   41,   37,   43,  144,   45,  263,   59,
   60,   61,   62,   41,   44,   43,   44,   45,   42,   35,
    0,  267,   55,   47,  270,    4,   59,  258,  166,  167,
    9,   59,   60,   61,   62,   41,   41,   53,   43,   41,
   45,   43,   44,   45,  123,    0,  184,   80,   81,  187,
  256,  257,   31,  191,   87,  269,  257,   59,   60,   61,
   62,   41,   78,   43,   44,   45,   99,    0,  206,  275,
  276,  152,  257,  211,  275,  276,  125,  257,  266,   59,
   60,   61,   62,  256,  257,  118,   41,  256,   43,    0,
   45,  172,  257,  174,  175,  275,  276,  263,  267,  267,
  267,  270,  270,  270,   59,   60,   61,   62,  256,   62,
   43,   44,   45,  257,    0,   45,  197,  257,  258,  267,
  278,   41,  270,   43,  257,   45,   59,  257,  161,  162,
   41,   40,   40,   40,  264,  265,    0,   45,  257,  269,
  123,  271,  256,  257,  274,  275,  276,  277,   59,   44,
  280,  257,   43,   44,   45,  266,   40,   62,   44,  256,
  257,   45,  256,  257,    0,  257,   41,  256,  263,  275,
  276,  256,   41,   59,  263,  279,  266,  263,  266,  263,
   44,  273,  260,  261,  262,  256,    0,  278,  257,  256,
  257,  258,  259,   97,   98,   59,  256,  257,  258,  259,
   41,  256,  257,  258,  259,  270,  256,  257,    0,   41,
  260,  261,  262,   41,  264,  265,  266,  267,  256,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   41,  260,  261,  262,   45,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  256,  257,  280,   41,  256,  257,   45,   41,  260,  261,
  262,   41,  264,  265,  266,  267,  270,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,   45,
  260,  261,  262,  268,  264,  265,  266,  267,   45,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  268,   59,
  280,  256,  257,  102,  103,  260,  261,  262,  268,  264,
  265,  266,  267,  270,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,  270,  267,   41,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,  270,  269,  270,
  271,  272,    0,  274,  275,  276,  277,    0,  270,  280,
  256,  257,  256,  257,  258,  259,    0,   59,  264,  265,
    0,  267,   44,  269,  270,  271,  272,  269,  274,  275,
  276,  277,  256,  257,  280,    0,   82,   83,    0,   59,
  264,  265,   61,  267,  159,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,    0,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   59,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,  256,  257,  258,  259,
  264,  265,   -1,  267,   59,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,  256,  257,  258,
  259,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
  256,  257,  258,  259,   -1,   -1,  256,  257,   -1,   -1,
  257,  258,  259,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,   -1,  280,  256,  257,   -1,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,  256,  257,  280,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,   -1,
   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,    2,   -1,
    4,  256,  257,   -1,   -1,    9,   -1,   -1,   -1,  264,
  265,   -1,   -1,   17,  269,   -1,  271,  272,   -1,  274,
  275,  276,  277,   -1,   -1,  280,   -1,   31,   -1,   -1,
   -1,   35,    2,    2,    4,    4,  257,   -1,   -1,    9,
    9,   -1,   -1,  264,  265,   -1,   -1,   17,   17,   53,
  271,   -1,   -1,  274,  275,  276,  277,   61,   -1,  280,
   -1,   31,   31,   -1,   -1,   35,   35,   23,   24,   25,
   26,   27,   28,   29,   78,   -1,   -1,   -1,   82,   83,
   -1,   -1,   -1,   53,   53,   -1,   56,   57,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  100,   -1,  256,  257,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   78,   78,
  268,  115,   -1,  271,  272,   -1,  274,  275,  276,  277,
   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  137,  256,  257,   -1,   -1,  142,  109,
  144,   -1,  264,  265,   -1,   -1,  115,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
   -1,   -1,  166,  167,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  142,   -1,   -1,   -1,  256,  257,   -1,
  184,   -1,   -1,  187,   -1,  264,  265,  191,  158,  159,
  269,   -1,  271,  272,   -1,  274,  275,  276,  277,   -1,
   -1,  280,  206,  256,  257,   -1,   -1,  211,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,  272,
   -1,  274,  275,  276,  277,  256,  257,  280,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,  267,   -1,   -1,  270,
  271,   -1,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
   -1,   -1,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,   -1,  270,  271,   -1,   -1,  274,  275,  276,
  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,
  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,  270,  271,   -1,   -1,  274,
  275,  276,  277,   -1,   -1,  280,
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
"declare_var : var_type var_list error",
"declare_var : var_type ID ';'",
"declare_var : var_type ID error",
"declare_fun : declare_fun_header fun_body END",
"declare_fun_header : var_type FUN ID '(' parametro ')' BEGIN",
"declare_fun_header : var_type FUN error '(' parametro ')' BEGIN",
"declare_fun_header : var_type FUN ID '(' error ')' BEGIN",
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
"if_cond : IF '(' cond ')'",
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

//#line 363 "grammar.y"

        private String actualScope = "main";

	public static void yyerror(String msg){
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        public String strToTID(String id){
                return ("<"+id+">");
        }
        
        public void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
        }

        public void popScope(){
                // quita ultimo scope, q esta delimitado con ':'
                int index = actualScope.lastIndexOf(":");
                if (index != -1) {
                        actualScope = actualScope.substring(0, index);
                } // else actualScope queda igual
        }

        public boolean isRedeclared(String id, String tipo){
                // chequea si ya fue declarada en el scope actual u otro anterior ( va pregutnando con cada scope, sacando el ultimo)
                String scopeaux = actualScope;
                while (actualScope.lastIndexOf(":") != -1){
                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                actualScope = scopeaux;
                                return true;}
                        popScope();
                }
                actualScope = scopeaux;
                return false;
        }

        public chkAndDeclareVar(String tipo, String id){
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // chequear si la variable ya fue declarada en el scope actual:  si tiene scope asociado y es el actual.
                if (isRedeclared(id,tipo)) {yyerror("WARNING: La variable "+id+" ya fue declarada en el scope actual o uno anterior. ");}
                else {
                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo. (elimino y agrego con esto):
                AnalizadorLexico.t_simbolos.del_entry(id);      //no tiene sentido borrarlo, porque seguro despues vuelva a estar..
                // ej: declaracion de una variable 'var1': el lexico agrega var1 a la tabla de simbolos. luego el sintactico la agrega con su tipo y scope.
                // pero al usarse, ej: var1 := 10;  el lexico agrega var1 a la TS (porque no esta.. esta var1:main) y ahi queda esa suelta 
                // para mi no está mal, lo piden en tp1, pero si va a quedar no tiene sentido borrarla cuando se declara.
                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                }
        }

//#line 672 "Parser.java"
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
//#line 19 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 20 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); }
break;
case 4:
//#line 21 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 22 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 23 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
break;
case 7:
//#line 24 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
break;
case 11:
//#line 36 "grammar.y"
{yyerror("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
break;
case 12:
//#line 37 "grammar.y"
{yyerror("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
break;
case 13:
//#line 38 "grammar.y"
{yyerror("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 39 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
break;
case 16:
//#line 50 "grammar.y"
{
            yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
break;
case 17:
//#line 57 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 62 "grammar.y"
{yyerror("Sentencia de control IF en linea "+AnalizadorLexico.line_number);}
break;
case 20:
//#line 64 "grammar.y"
{yyerror("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
break;
case 21:
//#line 65 "grammar.y"
{yyerror("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
break;
case 22:
//#line 67 "grammar.y"
{yyerror("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 23:
//#line 68 "grammar.y"
{yyerror("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
break;
case 24:
//#line 70 "grammar.y"
{yyerror("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
break;
case 25:
//#line 72 "grammar.y"
{yyerror("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 26:
//#line 73 "grammar.y"
{yyerror("Sentencia de TAG");}
break;
case 30:
//#line 84 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                /* agregar a cada variable (separar por ',') su tipo,scope,uso*/
                                }
break;
case 31:
//#line 87 "grammar.y"
{
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
        }
break;
case 32:
//#line 93 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
                }
break;
case 33:
//#line 100 "grammar.y"
{
                popScope();
        }
break;
case 34:
//#line 107 "grammar.y"
{
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
        }
break;
case 35:
//#line 113 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;}
break;
case 36:
//#line 115 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval)){
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
        }
break;
case 37:
//#line 126 "grammar.y"
{
                /* OJO ESTO ES UN TIPO, NO UN NOMBRE DE VARIABLE.. */
                if (!AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                /*if (isRedeclared){error de redeclaracion?} si coincide el tipo con nombre de otra variable da error? o solo si coincide con otro tipo?*/
                AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"ID","pair");
                AnalizadorLexico.t_simbolos.set_use(val_peek(0).sval+":"+actualScope,"type_name");
                /* observar que el tipo definido por el usuario tambien tiene alcance.*/
        }
break;
case 38:
//#line 134 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 39:
//#line 135 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 40:
//#line 136 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 44:
//#line 153 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 154 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 47:
//#line 159 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 52:
//#line 203 "grammar.y"
{}
break;
case 53:
//#line 204 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 54:
//#line 205 "grammar.y"
{
        yyerror("$1: "+val_peek(5).sval+" $$: "+yyval.sval+" $4: "+val_peek(2).sval); /*$3 devuelve el primer lexema de la condicion*/
        yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' antes del "+val_peek(2).sval+"."); }
break;
case 55:
//#line 208 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 56:
//#line 209 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 57:
//#line 210 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
break;
case 58:
//#line 211 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 60:
//#line 215 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
break;
case 61:
//#line 216 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }
break;
case 62:
//#line 217 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 63:
//#line 218 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
break;
case 64:
//#line 219 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; }
break;
case 65:
//#line 220 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este dentro de parentesis. ") ; }
break;
case 66:
//#line 221 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 67:
//#line 222 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 68:
//#line 223 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 71:
//#line 236 "grammar.y"
{
                yyval.sval = Terceto.addTerceto(val_peek(1).sval,strToTID(val_peek(2).sval),strToTID(val_peek(0).sval));}
break;
case 72:
//#line 238 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 79:
//#line 252 "grammar.y"
{yyval.sval = Terceto.addTerceto(val_peek(1).sval,strToTID(val_peek(2).sval),strToTID(val_peek(0).sval));}
break;
case 81:
//#line 254 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 83:
//#line 268 "grammar.y"
{yyval.sval = Terceto.addTerceto("-",strToTID(val_peek(2).sval),strToTID(val_peek(0).sval));}
break;
case 85:
//#line 270 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 86:
//#line 274 "grammar.y"
{
        String t_subtype;
        if 
        Terceto.addTercetoT("*",strToTID(val_peek(2).sval),strToTID(val_peek(0).sval), t_subtype);
        }
break;
case 87:
//#line 280 "grammar.y"
{Terceto.addTercetoT("/",strToTID(val_peek(2).sval),strToTID(val_peek(0).sval));}
break;
case 91:
//#line 287 "grammar.y"
{Terceto.addTercetoT("-","0",strToTID(val_peek(0).sval),AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
break;
case 92:
//#line 288 "grammar.y"
{Terceto.addTercetoT("-","0",strToTID(val_peek(0).sval),AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
break;
case 98:
//#line 300 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 100:
//#line 305 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 101:
//#line 306 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 103:
//#line 314 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 104:
//#line 315 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 105:
//#line 316 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 106:
//#line 318 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 107:
//#line 319 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 108:
//#line 320 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 109:
//#line 321 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 110:
//#line 322 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 111:
//#line 323 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 113:
//#line 329 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 121:
//#line 358 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1155 "Parser.java"
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

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
    3,    3,    3,    3,    3,    3,   17,   17,    6,    6,
    6,    6,    7,   20,   20,   20,    4,    4,    4,    4,
   19,   19,   22,   22,   22,   15,   15,   21,   18,   18,
   18,    9,    9,    9,   24,   24,   24,   24,   25,   25,
   26,   26,   26,   26,   29,   28,   27,   27,   30,   30,
   30,   30,   30,   30,   10,   10,   10,   23,   23,   23,
   23,   32,   32,   32,   33,   33,   33,   33,   33,   33,
   33,   31,   34,   34,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   14,   14,   35,
   35,   37,   37,   36,   36,   16,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    3,    7,    7,    7,    6,    5,    4,    6,
    3,    3,    2,    1,    2,    4,    2,    1,    1,    1,
    1,    3,    3,    3,    4,    2,    3,    3,    2,    2,
    3,    2,    3,    3,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    4,    3,    3,    1,
    1,    3,    3,    1,    1,    1,    2,    2,    1,    1,
    1,    4,    4,    5,    4,    3,    2,    8,    6,    7,
    7,    7,    5,    6,    6,    7,    7,    3,    3,    3,
    3,    1,    1,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  116,    0,    0,    0,
    0,    0,   49,   50,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   14,    0,    0,
    0,    0,   86,   91,    0,    0,    0,    0,   90,    0,
   84,   89,    0,   97,    0,    0,    0,   81,    0,    0,
    0,  118,  117,    4,    9,   15,   11,   19,   20,   21,
   22,   23,   24,   25,   17,   26,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,   88,   87,   72,   73,   74,   69,   70,   71,
    0,    0,    0,   58,    0,    0,    0,    1,   96,    0,
   51,    0,    0,    0,    0,    0,   27,    0,    0,   32,
    0,   31,    0,    0,    0,   30,   29,    0,   33,   60,
    0,   59,   53,   65,   52,   54,    0,    0,    0,    0,
    0,    0,  113,  111,  110,   92,    0,   55,    0,    0,
    0,   82,   83,   95,    0,   39,    0,   46,    0,    0,
   28,    0,    0,   41,    0,    0,   42,    0,   62,    0,
    0,    0,    0,   93,    0,   38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   63,   64,   61,    0,    0,
   94,   40,   37,    0,  105,    0,    0,    0,   45,   43,
    0,    0,    0,  102,  107,    0,  101,  106,   35,   36,
   34,   98,
};
final static short yydgoto[] = {                          3,
   17,   18,   19,   20,   67,   21,   22,   76,   23,   24,
   25,   26,   27,   28,   29,   30,  131,   31,   79,   32,
   81,  183,   47,   33,   83,  136,   48,  132,  137,  103,
   49,   50,   51,   52,   35,  141,   36,
};
final static short yysindex[] = {                      -176,
 -259,  -74,    0,  605,  -21, -118,    0,  -38,  605,  -34,
  -56,  -23,    0,    0, -164, -199,  531,    0,    0,   10,
    0,    0,   10,   10,   10,   10,   10,   10,   10,   48,
 -245,  605, -140, -135,  -33,   94,  553,    0,  121, -115,
    0,  -22,    0,    0,  167, -123,  -35,  107,    0,  -26,
    0,    0,  579,    0,   -4,  -47, -178,    0,  121,   74,
  673,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -12, -105,   61,  605,
 -101,  652, -179,  121,  241,  -93,  -93,    0,   74,   53,
  121,  138,    0,    0,    0,    0,    0,    0,    0,    0,
  262,  262,  121,    0,  262,  262,  -21,    0,    0,   84,
    0, -178,  -77,  123,  101,  -89,    0,  694,  -65,    0,
  121,    0,  -64,  155,  156,    0,    0,  -53,    0,    0,
  715,    0,    0,    0,    0,    0,  630,   74,    0,   29,
  161,   85,    0,    0,    0,    0,   13,    0,  -26,  -26,
   74,    0,    0,    0,  145,    0,  -46,    0,  -16,  -40,
    0,  -50,   74,    0, -154, -223,    0,  -25,    0, -157,
  121,  121,  173,    0, -103,    0,  167,  176,   -9,  167,
    0,  -97,  219,  223,  263,    0,    0,    0,   74,   74,
    0,    0,    0,  267,    0,  143,  271,  289,    0,    0,
   14,   66,   81,    0,    0,  298,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                       256,
  354,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  355,    0,    0,  496,
    0,    0,  259,  259,  259,  259,  259,  259,  259,  468,
    0,    0,    0,  315,    0,    0,    0,    0,    0,    0,
   96,    1,    0,    0,    0,    0,    0,   98,    0,   23,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  215,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  115,
    0,    0,    0,    0,    0,    0,    0,    0,  237,    0,
    0,  120,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  408,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -120,    0,    0,    0,    0,    0,    0,  291,  118,    0,
  322,  -24,    0,    0,    0,    0,    0,    0,   49,   71,
  140,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  344,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  369,    0,    0,
   45,    0,    0,    0,    0,    0,    0,    0,  165,  187,
    0,    0,    0,  391,    0,    0,  418,    0,    0,    0,
    0,    0,    0,    0,    0,  446,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
   46,   22,  -31,    0,  517,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  352,  -42,    0,    0,
    0,  253,  695,    0,    0,    0,  -10,  290,    0,    0,
  760,   70,   68,    0,    0,    0,   89,
};
final static int YYTABLESIZE=995;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        180,
   85,   45,  112,   57,   40,   55,   46,  101,    4,  102,
   86,   77,  112,  113,  114,  105,   59,   91,  119,  112,
  106,   46,   80,  177,   98,  100,   99,   78,   46,  117,
  196,  123,  184,  181,   92,   46,  109,   38,   65,  119,
   46,   85,   85,   85,   85,   85,  122,   85,   78,   37,
  117,   13,   14,  174,   53,  101,   62,  102,   65,   85,
   85,   85,   85,   80,   63,   80,   80,   80,   66,  155,
   79,  101,  171,  102,   65,  119,  133,   80,  111,    1,
    2,   80,   80,   80,   80,   44,  161,  134,  119,   78,
  135,   78,   78,   78,  119,   68,   13,   14,  187,  161,
   40,   65,  181,   61,  128,  117,   75,   78,   78,   78,
   78,   79,  188,   79,   79,   79,  101,  109,  102,  127,
   13,   14,  182,  182,  154,   82,  101,   84,  102,   79,
   79,   79,   79,   93,   94,   66,   68,   87,   81,   67,
   81,  158,   90,  101,   39,  102,   66,  104,  178,   66,
  124,  125,  192,  193,   68,   81,   81,   81,  199,  200,
   81,   81,   81,  142,  114,   46,  194,  129,  197,  198,
  149,  150,  152,  153,  144,  145,  109,  146,  148,  156,
   67,    5,    6,  205,  157,  206,  115,   46,  159,    7,
    8,  162,  164,    9,  165,  166,   10,   11,   67,   12,
   13,   14,   15,  167,  172,   16,  175,   40,  114,  111,
  176,   46,  121,  191,   47,   51,  195,   41,   42,   43,
   44,   54,   56,  114,   95,   96,   97,   13,   14,   85,
  115,   51,   58,   42,   43,   44,   75,  179,  112,   41,
   42,   43,   44,  120,  186,  115,   41,   42,   43,   44,
  121,   58,   42,   43,   44,    7,   85,   85,   16,  201,
   85,   85,   85,  202,   85,   85,   85,   85,  173,   85,
   85,   85,   85,   47,   85,   85,   85,   85,   80,   80,
   85,  209,   80,   80,   80,   46,   80,   80,   80,   80,
   76,   80,   80,   80,   80,   75,   80,   80,   80,   80,
   51,   51,   80,  203,   78,   78,   46,  204,   78,   78,
   78,  207,   78,   78,   78,   78,  126,   78,   78,   78,
   78,  108,   78,   78,   78,   78,   79,   79,   78,  208,
   79,   79,   79,  210,   79,   79,   79,   79,  212,   79,
   79,   79,   79,   77,   79,   79,   79,   79,  211,   76,
   79,   68,   68,    3,    6,   81,   81,   81,  113,   68,
   68,   68,   68,   56,   68,   68,   68,   68,  103,   68,
   68,   68,   68,  109,  109,   68,   58,   42,   43,   44,
  108,  109,  109,   48,  109,   57,  109,  109,  109,  109,
  104,  109,  109,  109,  109,   67,   67,  109,   41,   42,
   43,   44,   77,   67,   67,   67,   67,    5,   67,   67,
   67,   67,  118,   67,   67,   67,   67,   99,  185,   67,
  114,  114,   41,   42,   43,   44,  170,  103,  114,  114,
    0,  114,    0,  114,  114,  114,  114,    0,  114,  114,
  114,  114,  115,  115,  114,  100,    0,    0,    0,  104,
  115,  115,    0,  115,    0,  115,  115,  115,  115,    0,
  115,  115,  115,  115,    0,    0,  115,   18,    0,    0,
   47,   47,    0,    0,    0,    0,   99,    0,   47,   47,
    0,   47,    0,   47,   47,   47,   47,    0,   47,   47,
   47,   47,   75,   75,   47,   16,  139,   42,   43,   44,
   75,   75,    0,   75,  100,   75,   75,   75,   75,    0,
   75,   75,   75,   75,   16,   16,   75,    0,   42,   43,
   44,    0,   16,   16,    0,   16,    0,   16,   16,   16,
   16,    0,   16,   16,   16,   16,    0,    0,   16,   68,
   69,   70,   71,   72,   73,   74,   76,   76,    0,    0,
    0,    0,    0,    0,   76,   76,    0,   76,    0,   76,
   76,   76,   76,    0,   76,   76,   76,   76,    0,    0,
   76,    0,    0,    0,    0,    0,    0,  108,  108,    0,
    0,    0,    0,    0,    0,  108,  108,    0,  108,    0,
  108,  108,  108,  108,    0,  108,  108,  108,  108,   77,
   77,  108,    0,    0,    0,    0,    0,   77,   77,    0,
   77,    0,   77,   77,   77,   77,    0,   77,   77,   77,
   77,    0,    0,   77,  103,  103,    0,    0,    0,    0,
    0,    0,  103,  103,    0,  103,    0,  103,  103,  103,
  103,    0,  103,  103,  103,  103,  104,  104,  103,    0,
    0,    0,    0,    0,  104,  104,    0,  104,    0,  104,
  104,  104,  104,    0,  104,  104,  104,  104,    0,    0,
  104,    0,    0,   99,   99,    0,    0,    0,    0,    0,
    0,   99,   99,    0,   99,    0,   99,   99,   99,   99,
    0,   99,   99,   99,   99,    0,    0,   99,    0,    0,
    0,  100,  100,    0,    0,    0,   60,    0,    0,  100,
  100,    0,  100,    0,  100,  100,  100,  100,    0,  100,
  100,  100,  100,   18,   18,  100,    0,    0,    0,    0,
    0,   18,   18,   89,   18,    0,   18,   18,   18,   18,
    0,   18,   18,   18,   18,    0,    0,   18,    0,  110,
    0,   16,   16,  115,    0,    0,    0,    0,    0,   16,
   16,   34,    0,   34,   16,    0,   16,   16,   34,   16,
   16,   16,   16,    0,    0,   16,   34,    0,  138,  140,
    0,    0,    0,    0,    0,  147,    5,    6,    0,    0,
    0,   34,    0,    0,    7,    8,   34,  151,    0,   64,
    0,   10,   11,    0,   12,   13,   14,   15,    5,    6,
   16,    0,   34,    0,    0,  163,    7,    8,    0,    0,
   34,   88,    0,   10,   11,    0,   12,   13,   14,   15,
    0,    0,   16,    0,  107,    6,    0,    0,    0,   34,
    0,   34,    7,    8,    0,  143,  143,  108,    0,   10,
   11,    0,   12,   13,   14,   15,    0,    0,   16,    0,
    5,    6,    0,    0,    0,  189,  190,    0,    7,    8,
    0,    0,    0,    0,    0,   10,   11,   34,   12,   13,
   14,   15,    0,    0,   16,  168,    6,    0,    0,    0,
   34,    0,    0,    7,    8,    0,   34,    0,    0,  169,
   10,    0,    0,   12,   13,   14,   15,  130,    6,   16,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
    0,    0,   10,    0,    0,   12,   13,   14,   15,    6,
    0,   16,    0,    0,    0,    0,    7,    8,    0,    0,
    0,  116,    0,   10,    0,    0,   12,   13,   14,   15,
    6,    0,   16,    0,    0,    0,    0,    7,    8,    0,
    0,    0,  160,    0,   10,    0,    0,   12,   13,   14,
   15,    6,    0,   16,    0,    0,    0,    0,    7,    8,
    0,    0,    0,    0,    0,   10,    0,    0,   12,   13,
   14,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,   60,  123,   40,   45,   43,  268,   45,
   44,  257,   60,   56,   57,   42,   40,   40,   61,   44,
   47,   45,    0,   40,   60,   61,   62,  273,   45,   61,
   40,   44,  256,  257,   45,   45,   41,   59,   17,   82,
   45,   41,   42,   43,   44,   45,   59,   47,    0,    4,
   82,  275,  276,   41,    9,   43,  256,   45,   37,   59,
   60,   61,   62,   41,  264,   43,   44,   45,   59,  112,
    0,   43,   44,   45,   53,  118,  256,   32,  257,  256,
  257,   59,   60,   61,   62,   41,  118,  267,  131,   41,
  270,   43,   44,   45,  137,    0,  275,  276,  256,  131,
  123,   80,  257,  268,   44,  137,   59,   59,   60,   61,
   62,   41,  270,   43,   44,   45,   43,    0,   45,   59,
  275,  276,  165,  166,   41,  266,   43,  263,   45,   59,
   60,   61,   62,  257,  258,  256,   41,   44,   43,    0,
   45,   41,  258,   43,  263,   45,  267,   41,  159,  270,
  256,  257,  256,  257,   59,   60,   61,   62,  256,  257,
   43,   44,   45,  257,    0,   45,  177,  269,  179,  180,
  101,  102,  105,  106,   86,   87,   59,  125,   41,  257,
   41,  256,  257,   41,   62,  196,    0,   45,  278,  264,
  265,  257,  257,  268,   40,   40,  271,  272,   59,  274,
  275,  276,  277,  257,   44,  280,   62,  123,   44,  257,
  257,   45,  263,   41,    0,  257,   41,  256,  257,  258,
  259,  256,  279,   59,  260,  261,  262,  275,  276,  263,
   44,  273,  256,  257,  258,  259,    0,  278,  263,  256,
  257,  258,  259,  256,  270,   59,  256,  257,  258,  259,
  263,  256,  257,  258,  259,    0,  256,  257,    0,   41,
  260,  261,  262,   41,  264,  265,  266,  267,  256,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,  268,  260,  261,  262,   45,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  256,  257,  280,   41,  256,  257,   45,   41,  260,  261,
  262,   41,  264,  265,  266,  267,  256,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,   41,
  260,  261,  262,  268,  264,  265,  266,  267,   41,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  268,   59,
  280,  256,  257,    0,    0,  260,  261,  262,   44,  264,
  265,  266,  267,  266,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,  269,  267,  266,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,    0,  269,  270,
  271,  272,   61,  274,  275,  276,  277,    0,  166,  280,
  256,  257,  256,  257,  258,  259,  137,   59,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,   -1,   -1,   -1,   59,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,    0,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   59,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,  256,  257,  258,  259,
  264,  265,   -1,  267,   59,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,  257,  258,
  259,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   23,
   24,   25,   26,   27,   28,   29,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
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
   -1,  256,  257,   -1,   -1,   -1,   12,   -1,   -1,  264,
  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   39,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,   55,
   -1,  256,  257,   59,   -1,   -1,   -1,   -1,   -1,  264,
  265,    2,   -1,    4,  269,   -1,  271,  272,    9,  274,
  275,  276,  277,   -1,   -1,  280,   17,   -1,   84,   85,
   -1,   -1,   -1,   -1,   -1,   91,  256,  257,   -1,   -1,
   -1,   32,   -1,   -1,  264,  265,   37,  103,   -1,  269,
   -1,  271,  272,   -1,  274,  275,  276,  277,  256,  257,
  280,   -1,   53,   -1,   -1,  121,  264,  265,   -1,   -1,
   61,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
   -1,   -1,  280,   -1,  256,  257,   -1,   -1,   -1,   80,
   -1,   82,  264,  265,   -1,   86,   87,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
  256,  257,   -1,   -1,   -1,  171,  172,   -1,  264,  265,
   -1,   -1,   -1,   -1,   -1,  271,  272,  118,  274,  275,
  276,  277,   -1,   -1,  280,  256,  257,   -1,   -1,   -1,
  131,   -1,   -1,  264,  265,   -1,  137,   -1,   -1,  270,
  271,   -1,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
   -1,   -1,  271,   -1,   -1,  274,  275,  276,  277,  257,
   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,
  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,
  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,  274,  275,
  276,  277,   -1,   -1,  280,
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
"executable_statement : tag_statement optional_not_semicolon",
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
"if_statement : if_cond then_statement END_IF",
"if_statement : if_cond then_statement error",
"if_statement : if_cond then_statement else_statement",
"if_cond : IF '(' cond ')'",
"if_cond : IF cond",
"if_cond : IF '(' cond",
"if_cond : IF cond ')'",
"then_statement : THEN ctrl_block_statement",
"then_statement : THEN error",
"else_statement : else_tk ctrl_block_statement END_IF",
"else_statement : else_tk END_IF",
"else_statement : else_tk error END_IF",
"else_statement : else_tk ctrl_block_statement error",
"else_tk : ELSE",
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
"tag_statement : TAG",
"goto_statement : GOTO TAG",
"goto_statement : GOTO error",
};

//#line 564 "grammar.y"
        public ArrayList<String> errores = new ArrayList<String>();
        public String actualScope = "main";
        private int CantE = 0;
        private int CantID = 0;


	public static void yyerror(String msg){
                errores.add(msg);
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        /*public String strToTID(String id){      // agrega "<" ">" para indicar es id de terceto, y no clave de TS
                return ("<"+id+">");
        }*/

        public Boolean isTerceto(String id){
                return (id.charAt(0) == '<' && id.charAt(id.length()-1) == '>');
        }

        public void chkAndGetTerc(ParseVal varParser, String t_subtype1, String id1){   //t_subtype1 y id1 vienen vacios, vuelven modificadosf
                if isTerceto(varParser.sval) {
                        t_subtype1 = varParser.getSubtipo(varParser.sval);
                        id1 = varParser.sval;
                        }
                else {
                        t_subtype1 = AnalizadorLexico.t_simbolos.get_subtype(varParser.sval);
                        id1 = strToTID(varParser.sval); // para que ??
                        }
        }
        
        public void set_var_scope() {

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

        public boolean isDeclared(String id){
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

        public Boolean chkVar(String tipo, String id){
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                else if (isDeclared(id)) {yyerror("WARNING: La variable "+id+" ya fue declarada en el scope actual o uno anterior. ");}
                else { AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                        AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                        return true;
                        
                }
        }

        public chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                // chequear si la variable ya fue declarada en el scope actual:  si tiene scope asociado y es el actual.
                
                if (isDeclared(id)) {yyerror("WARNING: La variable "+id+" ya fue declarada en el scope actual o uno anterior. ");}
                else {
                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo. (elimino y agrego con esto):
                AnalizadorLexico.t_simbolos.del_entry(id);      //no tiene sentido borrarlo, porque seguro despues vuelva a estar..
                // ej: declaracion de una variable 'var1': el lexico agrega var1 a la tabla de simbolos. luego el sintactico la agrega con su tipo y scope.
                // pero al usarse, ej: var1 := 10;  el lexico agrega var1 a la TS (porque no esta.. esta var1:main) y ahi queda esa suelta 
                // para mi no está mal, lo piden en tp1, pero si va a quedar no tiene sentido borrarla cuando se declara.
                //solucion: en accion semantica chequear (agregnado el socpe actual) si ya esta o no (isDeclared) 
                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"variable_name");
                }
        }

        public bool isCompatible(String t_subtype1,String t_subtype2){
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2)
        }
//#line 682 "Parser.java"
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
case 1:
//#line 16 "grammar.y"
{
        /*TODO: FIN DEL PROGRAMA, CHEQUEAR:*/
        /*TODO: - si hubo goto, chequear tag estaba y era alcanzada*/
}
break;
case 2:
//#line 20 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 21 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); }
break;
case 4:
//#line 22 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 23 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 24 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
break;
case 7:
//#line 25 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
break;
case 11:
//#line 37 "grammar.y"
{yyerror("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
break;
case 12:
//#line 38 "grammar.y"
{yyerror("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
break;
case 13:
//#line 39 "grammar.y"
{yyerror("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 40 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
break;
case 16:
//#line 51 "grammar.y"
{
            yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
break;
case 17:
//#line 58 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 63 "grammar.y"
{yyerror("Sentencia de control IF en linea "+AnalizadorLexico.line_number);}
break;
case 20:
//#line 65 "grammar.y"
{yyerror("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
break;
case 21:
//#line 66 "grammar.y"
{yyerror("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
break;
case 22:
//#line 68 "grammar.y"
{yyerror("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 23:
//#line 69 "grammar.y"
{yyerror("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
break;
case 24:
//#line 71 "grammar.y"
{yyerror("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
break;
case 25:
//#line 73 "grammar.y"
{yyerror("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 26:
//#line 74 "grammar.y"
{yyerror("Sentencia de TAG");
                /*chequear no exista otro tag igual en todo el programa*/
        }
break;
case 29:
//#line 86 "grammar.y"
{/*agregar a cada variable (separada por ',') su tipo, scope,uso*/

}
break;
case 30:
//#line 89 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                /* agregar a cada variable (separar por ',') su tipo,scope,uso*/
                                }
break;
case 31:
//#line 92 "grammar.y"
{
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
        }
break;
case 32:
//#line 98 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
                }
break;
case 33:
//#line 105 "grammar.y"
{
                /* Actualización del scope: fin de la función fuerza retorno al ámbito del padre*/
                        popScope();
                }
break;
case 34:
//#line 114 "grammar.y"
{

                /* Control de ID: debe ser único en el scope actual*/
                        if (isDeclared())                                       yyerror("No se permite la redeclaración de variables: el nombre seleccionado no está disponible en el scope actual.");
                        else { }/* ¿La compilación debería seguir? ¿Cómo? */

                /* Control de ID: se verifica la primera letra del nombre (algunas iniciales son reservadas)*/
                        if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))      yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                        else {} /* ¿La compilación debería seguir? ¿Cómo? */

                /* Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(4).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(4).sval+":"+actualScope,"ID",val_peek(2).sval,"fun_name",val_peek(6));
                        AnalizadorLexico.t_simbolos.set_use(val_peek(4).sval+":"+actualScope,"variable_name");

                /* Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función*/
                        pushScope(val_peek(4).sval); 
                        
                /* Actualización del ID del parámetro: se actualiza el scope al actual*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(2).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(2).sval+":"+actualScope,"ID",val_peek(2).sval,"fun_name",val_peek(6));
                        AnalizadorLexico.t_simbolos.set_use(val_peek(2).sval+":"+actualScope,"variable_name");

                /* Posible generación de terceto de tipo LABEL*/
                        /* $$.sval = Terceto.addTercetoT("LABEL",ID,null);*/

                }
break;
case 35:
//#line 142 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;
                }
break;
case 36:
//#line 146 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
break;
case 37:
//#line 156 "grammar.y"
{
                /* OJO ESTO ES UN TIPO, NO UN NOMBRE DE VARIABLE.. */
                if (!AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                if (isDeclared){yyerror("Error: variable ya declarada, linea "+AnalizadorLexico.line_number);} 
                AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"ID","pair");
                AnalizadorLexico.t_simbolos.set_use(val_peek(0).sval+":"+actualScope,"type_name");


        }
break;
case 38:
//#line 165 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 39:
//#line 166 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 40:
//#line 167 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 43:
//#line 183 "grammar.y"
{yyval.sval = val_peek(1).sval+"-"+val_peek(0).sval}
break;
case 44:
//#line 184 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 185 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 47:
//#line 190 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 52:
//#line 235 "grammar.y"
{       /*pdoria poner end_if dentro de then_Statement y hacer esto ahi.*/
        /*completo terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(), null,String.valueOf(Integer.parseInt(val_peek(1).sval) + 1)); 
}
break;
case 53:
//#line 239 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 54:
//#line 241 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),String.valueOf(Integer.parseInt(val_peek(0).sval) + 1),null); 

}
break;
case 55:
//#line 249 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1),null)
                Terceto.pushTerceto(yyval.sval) /*apilo terceto incompleto.*/
        }
break;
case 56:
//#line 253 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 57:
//#line 254 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
break;
case 58:
//#line 256 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 59:
//#line 284 "grammar.y"
{
                yyval.sval = val_peek(0).sval       /*devuelve ultimo terceto*/
        }
break;
case 60:
//#line 288 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 61:
//#line 292 "grammar.y"
{yyval.sval = val_peek(1).sval;}
break;
case 62:
//#line 293 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. "); }
break;
case 63:
//#line 294 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta "); }
break;
case 64:
//#line 295 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 65:
//#line 299 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BI",null,null); /*incompleto, primer operando se completara despues.*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,String.valueOf(Integer.parseInt(yyval.sval) + 1));/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval);
        }
break;
case 67:
//#line 310 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, null);
                /* compatiblidades de comparacion?? igual que en operaciones*/
        }
break;
case 68:
//#line 320 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 75:
//#line 333 "grammar.y"
{
                /*chequear id exista*/
                if (!isDeclared(val_peek(2).sval))
                        {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+val_peek(2).sval+" no declarada. "); }
                else {

                        String subtypeT;
                        String id;
                        chkAndGetTerc(val_peek(0),subtypeT,id);
                        String subtypeID = AnalizadorLexico.t_simbolos.get_subtype(val_peek(2).sval);
                        if (subtypeT = subtypeID){
                                Terceto.addTerceto(":=",val_peek(2).sval,val_peek(0).sval);
                        }
                        else if (subtypeID = "SINGLE" && subtypeT = "UINTEGER"){
                                Terceto.addTercetoT("utos",val_peek(0).sval,null,"SINGLE");
                                Terceto.addTerceto(":=",val_peek(2).sval,val_peek(0).sval);
                        }
                        else if (subtypeID = "UINTEGER" && subtypeT = "SINGLE"){
                                Terceto.addTercetoT("utos",val_peek(0).sval,null,"SINGLE");
                                Terceto.addTerceto(":=",val_peek(2).sval,val_peek(0).sval);
                        }
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }

                        }
                        
                }
break;
case 76:
//#line 360 "grammar.y"
{
                /*chequear id exista, sea tipo pair*/
                /* chequear tipos*/
                /*crear terceto*/
                
        }
break;
case 77:
//#line 366 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 78:
//#line 369 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { yyval.sval= Terceto.addTercetoT("SUMA_S",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {yyval.sval = Terceto.addTercetoT("SUMA_U",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("SUMA_S",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en suma. "); }
}
break;
case 79:
//#line 386 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { yyval.sval= Terceto.addTercetoT("RESTA_S",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {yyval.sval = Terceto.addTercetoT("RESTA_U",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("RESTA_S",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en resta. "); }
        }
break;
case 81:
//#line 404 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 82:
//#line 408 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                if (t_subtype1 = t_subtype2){
                        if (t_subtype1 = "SINGLE") { yyval.sval= Terceto.addTercetoT("MUL_S",id1,id2, t_subtype);}
                        else if (t_subtype1 = "UINTEGER") {yyval.sval = Terceto.addTercetoT("MUL_U",id1,id2, t_subtype);}
                        else {yyerror("Tipo no valido..")}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("MUL_S",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
break;
case 83:
//#line 425 "grammar.y"
{
                String t_subtype1;      /*probar si anda, en varias reglas declaro estos Strings preo creo dara error de redeclaracion*/
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                        if (t_subtype1 = t_subtype2){
                                if (t_subtype1 = "SINGLE") { yyval.sval= Terceto.addTercetoT("DIV_S",id1,id2, t_subtype);}
                                else if (t_subtype1 = "UINTEGER") {yyval.sval = Terceto.addTercetoT("DIV_U",id1,id2, t_subtype);}
                                else {yyerror("Tipo no valido..")}
                        } else if (isCompatible(t_subtype1,t_subtype2)){
                                if (t_subtype1 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                                else if (t_subtype2 != "SINGLE") {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                                yyval.sval= Terceto.addTercetoT("DIV_S",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en division. "); }
        }
break;
case 87:
//#line 448 "grammar.y"
{ 
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 88:
//#line 453 "grammar.y"
{
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 92:
//#line 464 "grammar.y"
{
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(3).sval) != "PAIR") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba variable de tipo pair. "); }
                else {
                        if (val_peek(1).sval == "1" || val_peek(1).sval == "2") {
                                /*Terceto.addTercetoT("[]",strToTID($1.sval),$3.sval,AnalizadorLexico.t_simbolos.get_subtype($1.sval));*/
                        }
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante 1 o 2. "); }
                }
        }
break;
case 94:
//#line 477 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 95:
//#line 481 "grammar.y"
{yyval.sval = Terceto.addTerceto("OUTF", val_peek(1).sval, null)}
break;
case 96:
//#line 482 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 97:
//#line 483 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 99:
//#line 489 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 100:
//#line 490 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 101:
//#line 491 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 102:
//#line 493 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 103:
//#line 494 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 104:
//#line 495 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 105:
//#line 496 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 106:
//#line 497 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 107:
//#line 498 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 108:
//#line 503 "grammar.y"
{
                if (cantE != cantID) {yyerror("Error en linea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. "); }
                else { /* opcion1: recorrer y separar por ',' opcion2: ir l*/

                }
                cantE = 0;
                cantID = 0;
        }
break;
case 109:
//#line 511 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 110:
//#line 516 "grammar.y"
{
                
                cantID = cantID + 2;

        }
break;
case 111:
//#line 521 "grammar.y"
{
                cantID = cantID + 1;
        }
break;
case 114:
//#line 535 "grammar.y"
{
                cantE = cantE + 2;
        }
break;
case 115:
//#line 539 "grammar.y"
{
                cantE = cantE + 1;
                
        }
break;
case 116:
//#line 547 "grammar.y"
{
                /*ponerle scope y chequear no este 'redeclarada'*/
                /*si no está, agregar a tabla de etiquetas*/
        }
break;
case 117:
//#line 554 "grammar.y"
{
                /*if existe en TS {*/

                /*}*/
        }
break;
case 118:
//#line 559 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1397 "Parser.java"
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

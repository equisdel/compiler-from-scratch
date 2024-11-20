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
        import java.util.*;
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
public final static short HEXA=279;
public final static short PAIR=280;
public final static short GOTO=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    0,    1,    1,    2,
    2,    2,    2,    2,    5,    5,    8,    8,    3,    3,
    3,    3,    3,    3,    3,    3,   17,   17,    6,    6,
    6,    6,   19,   19,    7,   20,   20,   20,    4,    4,
    4,    4,   22,   22,   22,   15,   15,   21,   18,   18,
   18,   18,    9,    9,    9,   24,   24,   24,   24,   25,
   25,   26,   26,   26,   26,   29,   28,   27,   27,   30,
   30,   30,   30,   30,   30,   10,   10,   10,   23,   23,
   23,   23,   32,   32,   32,   33,   33,   33,   33,   33,
   33,   31,   34,   34,   11,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   35,   14,
   14,   36,   36,   38,   38,   37,   37,   16,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    3,    3,    3,    7,    7,    7,    6,    5,
    4,    6,    2,    1,    2,    4,    2,    1,    1,    1,
    1,    1,    3,    3,    3,    4,    2,    3,    3,    2,
    2,    3,    2,    3,    3,    1,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    4,    3,    3,
    1,    1,    3,    3,    1,    1,    1,    2,    2,    1,
    1,    4,    4,    5,    4,    4,    3,    2,    7,    5,
    6,    6,    6,    4,    5,    5,    6,    6,    2,    3,
    3,    3,    3,    1,    1,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  118,    0,    0,    0,
    0,    0,   49,   50,    0,   51,    0,    0,    8,   10,
    0,   12,   13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   14,
    0,    0,    0,    0,   87,    0,    0,    0,    0,   91,
    0,   85,   90,    0,   98,    0,    0,    0,   82,    0,
    0,  109,  120,  119,    4,    9,   15,   11,   19,   20,
   21,   22,   23,   24,   25,   17,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,   89,   88,   73,
   74,   75,   70,   71,   72,    0,    0,    0,   59,    0,
    0,    0,    1,    0,   97,    0,   52,    0,    0,    0,
    0,   32,    0,   31,    0,    0,    0,   30,   29,    0,
   35,   61,    0,   60,   54,   66,   53,   55,    0,    0,
    0,    0,   28,    0,    0,    0,    0,    0,  115,  113,
  112,   92,    0,   56,    0,    0,    0,   83,   84,   96,
   95,    0,   41,    0,   46,    0,   33,    0,    0,   34,
    0,   63,    0,    0,    0,    0,    0,    0,    0,    0,
   93,    0,   40,    0,    0,    0,    0,    0,   64,   65,
   62,    0,  106,    0,    0,    0,    0,    0,   94,   42,
   39,   45,   43,    0,    0,    0,  103,  108,    0,  102,
  107,   37,   38,   36,   99,
};
final static short yydgoto[] = {                          3,
   18,   19,   20,   21,   68,   22,   23,   77,   24,   25,
   26,   27,   28,   29,   30,   31,  133,   32,   80,   33,
   82,  186,   48,   34,   84,  138,   49,  134,  139,  108,
   50,   51,   52,   53,   36,   37,  147,   38,
};
final static short yysindex[] = {                      -238,
 -262,  574,    0,  700,  -47, -112,    0,  -17,  700,  -19,
  -57,  -11,    0,    0, -193,    0, -217,  619,    0,    0,
   -9,    0,    0,   -9,   -9,   -9,   -9,   -9,   -9,   -9,
   24, -240,  700, -236, -171,  773,  -31,   51,  645,    0,
  133, -157,    0,  -30,    0,  161, -167,  -36,   68,    0,
    9,    0,    0,  674,    0,  -37,  -46, -158,    0,  133,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -28, -153,  -39,
  700, -143,  752, -218,  133, -162,    0,  794, -129,  214,
 -122, -122,    0,   39,   17,  133,  103,    0,    0,    0,
    0,    0,    0,    0,    0,   28,   28,  133,    0,   28,
   28,  -47,    0,  118,    0,   91,    0, -158,  -87,  109,
  102,    0,  133,    0,  -78,  140,  146,    0,    0,  -68,
    0,    0,  815,    0,    0,    0,    0,    0,  726,   39,
   -8,  -40,    0,  -70,    0,   14,  150,   73,    0,    0,
    0,    0,   65,    0,    9,    9,   39,    0,    0,    0,
    0,  135,    0,  -58,    0,   39,    0, -146,  -84,    0,
  -69,    0, -190,  161,  162,   -5,  161,  133,  133,  163,
    0, -103,    0,    0, -101,  164,  166,  167,    0,    0,
    0,  168,    0,  136,  171,  172,   39,   39,    0,    0,
    0,    0,    0,  -54,  -50,  -25,    0,    0,  203,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       255,
  256,    0,    0,    0,    0,  -42,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  260,    0,    0,
  540,    0,    0,  488,  488,  488,  488,  488,  488,  488,
  514,    0,    0,    0,  220,    0,    0,    0,    0,    0,
    0,    0,  105,    1,    0,    0,    0,    0,   13,    0,
   27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  236,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  274,    0,    0,   41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  290,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -189,    0,    0,    0,    0,    0,    0,  300,
    0,    0,    0,    0,  131,    0,  326,  -29,    0,    0,
    0,    0,    0,    0,   53,   79,  157,    0,    0,    0,
    0,    0,    0,    0,    0,  352,    0,    0,    0,    0,
    0,    0,    0,    0,  378,    0,    0,    0,    0,    0,
    0,    0,    0,  159,    0,    0,    0,    0,    0,    0,
    0,  404,    0,    0,  435,    0,  183,  210,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  461,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   32,   46,  -14,    0,  497,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  269,   19,    0,    0,
    0,  142,  762,    0,    0,    0,    8,  173,    0,    0,
  790,   54,   52,    0,    0,    0,    0,   77,
};
final static int YYTABLESIZE=1096;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        177,
   86,  114,   58,  115,  130,    4,  106,   47,  107,   96,
   42,   40,   91,  118,  114,  125,   78,    1,    2,  129,
   56,   87,   46,  103,  105,  104,   81,   47,   60,   83,
  124,  174,   79,   47,  194,   39,   47,  135,   63,   47,
   54,   86,   86,   86,   86,   86,   64,   86,  136,   67,
  110,  137,   79,   97,   89,  111,  106,  178,  107,   86,
   86,   86,   86,   66,   81,  190,   67,   81,   87,   81,
   81,   81,   47,  143,   62,  119,  120,   67,   80,  191,
   67,  106,   76,  107,   66,   81,   81,   81,   81,   98,
   99,   85,   42,   79,   92,   79,   79,   79,  117,   66,
   95,   89,  126,  127,   69,  181,   89,  106,  109,  107,
  184,   79,   79,   79,   79,  141,   13,   14,  143,   80,
   16,   80,   80,   80,   87,  131,   66,  144,   13,   14,
  111,  161,   16,  106,  148,  107,  162,   80,   80,   80,
   80,  152,  165,  154,  106,   69,  107,   82,  175,   82,
   41,   89,  200,  201,  202,  203,   68,   89,  160,  155,
  156,  158,  159,   69,   82,   82,   82,  150,  151,  163,
  164,  187,  184,   82,   82,   82,  208,   47,  167,  168,
   47,  192,  116,  195,  196,  169,  185,  185,  170,  111,
   13,   14,  123,  179,   16,   42,  182,   68,  183,   44,
  189,  209,  193,  199,  204,   47,  205,  206,  207,  117,
  117,  210,  211,  212,   52,   68,  128,  213,   59,   44,
   45,  114,   57,  100,  101,  102,  116,  122,   13,   14,
   52,   90,   16,  114,  123,   47,   55,  176,   43,   44,
   45,  116,  214,  215,   59,   44,   45,   43,   44,   45,
   43,   44,   45,  117,    7,    3,   86,   86,   47,    6,
   86,   86,   86,  115,   86,   86,   86,   86,  117,   86,
   86,   86,   86,   76,   86,   86,   86,   86,   57,   86,
   48,   86,   81,   81,   44,   45,   81,   81,   81,    5,
   81,   81,   81,   81,   47,   81,   81,   81,   81,   77,
   81,   81,   81,   81,   88,   81,   58,   81,   79,   79,
  188,  173,   79,   79,   79,    0,   79,   79,   79,   79,
  180,   79,   79,   79,   79,  110,   79,   79,   79,   79,
    0,   79,   76,   79,   80,   80,    0,    0,   80,   80,
   80,    0,   80,   80,   80,   80,    0,   80,   80,   80,
   80,   78,   80,   80,   80,   80,    0,   80,   77,   80,
   69,   69,    0,    0,   82,   82,   82,    0,   69,   69,
   69,   69,    0,   69,   69,   69,   69,  104,   69,   69,
   69,   69,    0,   69,  110,   69,  111,  111,   59,   44,
   45,   43,   44,   45,  111,  111,    0,  111,    0,  111,
  111,  111,  111,  105,  111,  111,  111,  111,    0,  111,
   78,  111,   68,   68,   52,   52,   43,   44,   45,    0,
   68,   68,   68,   68,    0,   68,   68,   68,   68,    0,
   68,   68,   68,   68,  100,   68,  104,   68,  116,  116,
    0,    0,    0,    0,    0,    0,  116,  116,    0,  116,
    0,  116,  116,  116,  116,    0,  116,  116,  116,  116,
  101,  116,  105,  116,    0,  117,  117,    0,    0,  145,
   44,   45,    0,  117,  117,    0,  117,    0,  117,  117,
  117,  117,    0,  117,  117,  117,  117,   16,  117,    0,
  117,   47,   47,  100,    0,    0,    0,    0,    0,   47,
   47,    0,   47,    0,   47,   47,   47,   47,    0,   47,
   47,   47,   47,   18,   47,    0,   47,    0,    0,  101,
   69,   70,   71,   72,   73,   74,   75,    0,    0,   76,
   76,    0,    0,    0,    0,    0,    0,   76,   76,   16,
   76,    0,   76,   76,   76,   76,    0,   76,   76,   76,
   76,    0,   76,    0,   76,   77,   77,    0,    0,    0,
    0,    0,    0,   77,   77,    0,   77,    0,   77,   77,
   77,   77,    0,   77,   77,   77,   77,    0,   77,    0,
   77,  110,  110,    0,    0,    0,    0,    0,    0,  110,
  110,    0,  110,    0,  110,  110,  110,  110,    0,  110,
  110,  110,  110,    0,  110,    0,  110,   78,   78,    0,
    0,    0,    0,    0,    0,   78,   78,    0,   78,    0,
   78,   78,   78,   78,    0,   78,   78,   78,   78,    0,
   78,    0,   78,  104,  104,    0,    0,    0,    0,    0,
    0,  104,  104,    0,  104,    0,  104,  104,  104,  104,
    0,  104,  104,  104,  104,    0,  104,    0,  104,  105,
  105,    0,    0,    0,    0,    0,    0,  105,  105,    0,
  105,    0,  105,  105,  105,  105,    0,  105,  105,  105,
  105,    0,  105,    0,  105,    0,    0,    0,    0,    0,
  100,  100,    0,    0,    0,    0,    0,    0,  100,  100,
    0,  100,    0,  100,  100,  100,  100,    0,  100,  100,
  100,  100,    0,  100,    0,  100,  101,  101,    0,    0,
    0,    0,    0,    0,  101,  101,    0,  101,    0,  101,
  101,  101,  101,    0,  101,  101,  101,  101,    0,  101,
    0,  101,    0,   16,   16,    0,    0,    0,    0,    0,
    0,   16,   16,    0,   16,    0,   16,   16,   16,   16,
    0,   16,   16,   16,   16,    0,   16,    0,   16,   18,
   18,    0,    0,   61,    0,    0,    0,   18,   18,    0,
   18,    0,   18,   18,   18,   18,    0,   18,   18,   18,
   18,   35,   18,   35,   18,   16,   16,    0,   35,    0,
    0,    0,   94,   16,   16,    0,    0,   35,   16,    0,
   16,   16,    0,   16,   16,   16,   16,  116,   16,    0,
   16,  121,   35,    0,    0,   35,    0,    0,   35,    5,
    6,    0,    0,    0,    0,    0,    0,    7,    8,    0,
    0,    9,    0,   35,   10,   11,  140,   12,   13,   14,
   15,  146,   16,    0,   17,    0,    0,  153,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  157,
   35,    0,   35,    0,    5,    6,    0,   35,    0,    0,
  149,  149,    7,    8,  166,    0,    0,   65,    0,   10,
   11,    0,   12,   13,   14,   15,    0,   16,    0,   17,
    5,    6,    0,    0,    0,    0,    0,    0,    7,    8,
    0,    0,    0,   93,    0,   10,   11,    0,   12,   13,
   14,   15,   35,   16,    0,   17,    0,    0,   35,  112,
    6,    0,    0,    0,    0,    0,    0,    7,    8,  197,
  198,    0,  113,    0,   10,   11,    0,   12,   13,   14,
   15,    0,   16,    0,   17,    5,    6,    0,    0,    0,
    0,    0,    0,    7,    8,    0,    0,    0,    0,    0,
   10,   11,    0,   12,   13,   14,   15,    0,   16,    0,
   17,  171,    6,    0,    0,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,  172,   10,    0,    0,   12,
   13,   14,   15,    0,   16,    0,   17,  132,    6,    0,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
    0,    0,   10,    0,    0,   12,   13,   14,   15,    6,
   16,    0,   17,    0,    0,    0,    7,    8,    0,    0,
    0,   86,    0,   10,    0,    0,   12,   13,   14,   15,
    6,   16,    0,   17,    0,    0,    0,    7,    8,    0,
    0,    0,  142,    0,   10,    0,    0,   12,   13,   14,
   15,    6,   16,    0,   17,    0,    0,    0,    7,    8,
    0,    0,    0,    0,    0,   10,    0,    0,   12,   13,
   14,   15,    0,   16,    0,   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   44,   60,   41,   44,  268,   43,   45,   45,   40,
  123,   59,   44,   60,   44,   44,  257,  256,  257,   59,
   40,   36,   40,   60,   61,   62,    0,   45,   40,  266,
   59,   40,  273,   45,   40,    4,   45,  256,  256,   45,
    9,   41,   42,   43,   44,   45,  264,   47,  267,   59,
   42,  270,    0,   46,   36,   47,   43,   44,   45,   59,
   60,   61,   62,   18,   33,  256,  256,   41,   83,   43,
   44,   45,   45,   88,  268,   57,   58,  267,    0,  270,
  270,   43,   59,   45,   39,   59,   60,   61,   62,  257,
  258,  263,  123,   41,   44,   43,   44,   45,  257,   54,
  258,   83,  256,  257,    0,   41,   88,   43,   41,   45,
  257,   59,   60,   61,   62,  278,  275,  276,  133,   41,
  279,   43,   44,   45,  139,  269,   81,  257,  275,  276,
    0,   41,  279,   43,  257,   45,  118,   59,   60,   61,
   62,  125,   41,   41,   43,   41,   45,   43,  141,   45,
  263,  133,  256,  257,  256,  257,    0,  139,   41,  106,
  107,  110,  111,   59,   60,   61,   62,   91,   92,  257,
   62,  256,  257,   43,   44,   45,   41,   45,  257,   40,
   45,  174,    0,  176,  177,   40,  168,  169,  257,   59,
  275,  276,  263,   44,  279,  123,   62,   41,  257,   41,
  270,  194,   41,   41,   41,   45,   41,   41,   41,    0,
  257,   41,   41,  268,  257,   59,  256,  268,  256,  257,
  258,  259,  280,  260,  261,  262,   44,  256,  275,  276,
  273,  263,  279,  263,  263,    0,  256,  278,  256,  257,
  258,   59,  268,   41,  256,  257,  258,  256,  257,  258,
  256,  257,  258,   44,    0,    0,  256,  257,   45,    0,
  260,  261,  262,   44,  264,  265,  266,  267,   59,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  266,  279,
  269,  281,  256,  257,  257,  258,  260,  261,  262,    0,
  264,  265,  266,  267,   59,  269,  270,  271,  272,    0,
  274,  275,  276,  277,   36,  279,  266,  281,  256,  257,
  169,  139,  260,  261,  262,   -1,  264,  265,  266,  267,
  256,  269,  270,  271,  272,    0,  274,  275,  276,  277,
   -1,  279,   59,  281,  256,  257,   -1,   -1,  260,  261,
  262,   -1,  264,  265,  266,  267,   -1,  269,  270,  271,
  272,    0,  274,  275,  276,  277,   -1,  279,   59,  281,
  256,  257,   -1,   -1,  260,  261,  262,   -1,  264,  265,
  266,  267,   -1,  269,  270,  271,  272,    0,  274,  275,
  276,  277,   -1,  279,   59,  281,  256,  257,  256,  257,
  258,  256,  257,  258,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,    0,  274,  275,  276,  277,   -1,  279,
   59,  281,  256,  257,  256,  257,  256,  257,  258,   -1,
  264,  265,  266,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,    0,  279,   59,  281,  256,  257,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,
   -1,  269,  270,  271,  272,   -1,  274,  275,  276,  277,
    0,  279,   59,  281,   -1,  256,  257,   -1,   -1,  256,
  257,  258,   -1,  264,  265,   -1,  267,   -1,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,    0,  279,   -1,
  281,  256,  257,   59,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,    0,  279,   -1,  281,   -1,   -1,   59,
   24,   25,   26,   27,   28,   29,   30,   -1,   -1,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,    0,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,   -1,  279,   -1,
  281,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,   -1,  279,   -1,  281,  256,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,   -1,
  279,   -1,  281,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,  279,   -1,  281,   -1,   -1,   -1,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,  279,
   -1,  281,   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,  256,
  257,   -1,   -1,   12,   -1,   -1,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,    2,  279,    4,  281,  256,  257,   -1,    9,   -1,
   -1,   -1,   41,  264,  265,   -1,   -1,   18,  269,   -1,
  271,  272,   -1,  274,  275,  276,  277,   56,  279,   -1,
  281,   60,   33,   -1,   -1,   36,   -1,   -1,   39,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,  268,   -1,   54,  271,  272,   85,  274,  275,  276,
  277,   90,  279,   -1,  281,   -1,   -1,   96,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  108,
   81,   -1,   83,   -1,  256,  257,   -1,   88,   -1,   -1,
   91,   92,  264,  265,  123,   -1,   -1,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,  271,  272,   -1,  274,  275,
  276,  277,  133,  279,   -1,  281,   -1,   -1,  139,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,  178,
  179,   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,
  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,
  271,  272,   -1,  274,  275,  276,  277,   -1,  279,   -1,
  281,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,  270,  271,   -1,   -1,  274,
  275,  276,  277,   -1,  279,   -1,  281,  256,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
   -1,   -1,  271,   -1,   -1,  274,  275,  276,  277,  257,
  279,   -1,  281,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,
  257,  279,   -1,  281,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,
  277,  257,  279,   -1,  281,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,  274,  275,
  276,  277,   -1,  279,   -1,  281,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
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
"RET","UINTEGER","SINGLE","REPEAT","UNTIL","HEXA","PAIR","GOTO",
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
"var_list : ID ',' ID",
"var_list : var_list ',' ID",
"declare_fun : declare_fun_header fun_body END",
"declare_fun_header : var_type FUN ID '(' parametro ')' BEGIN",
"declare_fun_header : var_type FUN error '(' parametro ')' BEGIN",
"declare_fun_header : var_type FUN ID '(' error ')' BEGIN",
"declare_pair : TYPEDEF PAIR '<' var_type '>' ID",
"declare_pair : TYPEDEF '<' var_type '>' ID",
"declare_pair : TYPEDEF PAIR var_type ID",
"declare_pair : TYPEDEF PAIR '<' var_type '>' error",
"parametro : var_type ID",
"parametro : ID",
"parametro : var_type error",
"return_statement : RET '(' expr ')'",
"return_statement : RET expr",
"fun_body : statement_list",
"var_type : UINTEGER",
"var_type : SINGLE",
"var_type : HEXA",
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
"expr_pair : ID '{' CTE '}'",
"fun_invoc : ID '(' expr ')'",
"fun_invoc : ID '(' expr error ')'",
"outf_statement : OUTF '(' expr ')'",
"outf_statement : OUTF '(' CHARCH ')'",
"outf_statement : OUTF '(' ')'",
"outf_statement : OUTF error",
"repeat_statement : repeat_begin executable_statement_list END UNTIL '(' cond ')'",
"repeat_statement : repeat_begin executable_statement_list END UNTIL cond",
"repeat_statement : repeat_begin executable_statement_list END UNTIL '(' cond",
"repeat_statement : repeat_begin executable_statement_list END UNTIL cond ')'",
"repeat_statement : repeat_begin END UNTIL '(' cond ')'",
"repeat_statement : repeat_begin END UNTIL cond",
"repeat_statement : repeat_begin END UNTIL '(' cond",
"repeat_statement : repeat_begin END UNTIL cond ')'",
"repeat_statement : repeat_begin executable_statement_list END '(' cond ')'",
"repeat_statement : repeat_begin executable_statement_list END UNTIL '(' ')'",
"repeat_begin : REPEAT BEGIN",
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

//#line 713 "grammar.y"
        public static ArrayList<String> errores = new ArrayList<>();
        public static String actualScope = "MAIN";
        //static Stack<Integer> UntilStack = new Stack<>();


	public static void yyerror(String msg){
                errores.add(msg);
	        System.out.println(msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        public String strToTID(String id){      // agrega "<" ">" para indicar es id de terceto, y no clave de TS
                return ("<"+id+">");
        }

        public static Boolean isTerceto(String id){
                return (id.charAt(0) == '<' && id.charAt(id.length()-1) == '>');
        }


        public static Boolean isFunction(String id){
                // es terceto y se llama CALL_FUN
                return (isTerceto(id) && Terceto.getOperacion(id).equals("CALL_FUN"));

        }

        public static String getFunctionID(String id){ // recibe terceto porq invoc_funcion arrastra el id del terceto
                // devuelve lexema (ID con scope)
                if (isFunction(id)) {
                        return (Terceto.getOp1(id));
                } else {System.out.println("CUIDADO SE ESTA PASANDO UN ID QUE NO ES DE FUNCION A getFunctionID");
                        return null;
                }
        }

        public String chkAndGetType(String valStr){  //DEVUELVE TIPO PRIMITIVO (HEXA, UINTEGER, O SINGLE)
                // SI valStr es invoc_funcion, devuelve el tipo de retorno de la funcion
        // recibe lexema SIN SCOPE 
                //AnalizadorLexico.t_simbolos.display();
                String lexem = "";
                if (isTerceto(valStr)) {
                        System.out.println(valStr+"ES terceto");
                        return Terceto.getSubtipo(valStr);
                        }
                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        if (isCte(valStr)) {
                                System.out.println(valStr+"ES cte");
                                return AnalizadorLexico.t_simbolos.get_subtype(valStr);}
                        else {  // variable, invoc. a funcion o expr_pair
                                if (isPair(valStr)) {
                                        lexem = valStr.substring(0,valStr.indexOf("{")); // me quedo con el id del pair
                                        lexem = getDeclared(lexem);
                                } else if (isFunction(valStr)){
                                        lexem = getFunctionID(valStr);  //vuelve con ambito
                                } else {lexem = getDeclared(valStr);}
                                System.out.println("NO ES CTE -> "+lexem);
                                String type = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                                AnalizadorLexico.t_simbolos.display();
                                System.out.println("se busco: "+lexem);
                                if (lexem != null){     // si está declarada
                                        if (!(type.equals("SINGLE") || type.equals("UINTEGER") || type.equals("HEXA"))) {
                                                //es tipo definido por usuario
                                                System.out.println("El tipo es "+type+" y su uso es "+AnalizadorLexico.t_simbolos.get_use(type));
                                                if ((AnalizadorLexico.t_simbolos.get_use(type)).equals("TYPE_NAME")) {
                                                        return (AnalizadorLexico.t_simbolos.get_subtype(type)); //devuelve el primitivo
                                                }
                                        } else {return AnalizadorLexico.t_simbolos.get_subtype(lexem);}
                                        // si es tipo primitivo
                                } else yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": identificador "+valStr+" no declarado. ");
                                
                                return null;
                        }
                }
        }

       /*  public void set_var_scope() {

        }*/

        public Boolean isCte(String valStr){
                System.out.println("aaaa"+valStr);
                if (AnalizadorLexico.t_simbolos.get_entry(valStr) != null){
                        return (AnalizadorLexico.t_simbolos.get_entry(valStr).getTipo().equals("CTE")); 
                } else return false;
                // si no esta, no es cte o no está
        }

        public void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
                TablaEtiquetas.pushScope();
        }

        public void popScope(){
                actualScope = popScope(actualScope);
                System.out.println("Scope tras salir: "+actualScope);
        }

        public String popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                int index = scope.lastIndexOf(":");
                System.out.print("scope: "+scope);
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                TablaEtiquetas.popScope();
                return scope;
        }


        public boolean isDeclared(String id){   // recibe id sin scoope
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
                if (isCte(id)){System.out.println("OJO ESTAS PASANDO UNA CTE A isDeclared");}
                String scopeaux = actualScope;
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclaredLocal(id)) {return true;}
                else {
                        while (actualScope.lastIndexOf(":") != -1){
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {       
                                        actualScope = scopeaux;
                                        return true;}
                                popScope();
                        }
                }
                actualScope = scopeaux;
                return false;
        }

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public void chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                //chequear tipo sea valido (uinteger,hexa,single o definido por usuario)
                AnalizadorLexico.t_simbolos.display();
                if (AnalizadorLexico.t_simbolos.get_entry(tipo) == null) {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipo de variable no valido. "); }
                else {System.out.println("tipo de var declarada:" +AnalizadorLexico.t_simbolos.get_entry(tipo).getUse());}
                
                if (tipo.equals("UINTEGER") || tipo.equals("HEXA") ||tipo.equals("SINGLE") || (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME"))) {
                // pair ejemplo en TS: pairsito:main subtipo:uinteger use:typename  no hace falta aclarar es un pair xq es el unico tipo q puede definir  
                // y otra entrada: p1:main:f1 subtipo: pairsito use: variable_name
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                        yyerror("Error: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                                else {
                                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo:
                                AnalizadorLexico.t_simbolos.del_entry(id);
                                //AnalizadorLexico.t_simbolos.display();
                                if (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME")){
                                        // SI EL TIPO ES DEFINIDO X USER, VA CON :MAIN
                                        AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo+":MAIN");
                                }else {AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);}
                                
                                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                                }   
                } else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipo de variable no valido. "); }
        }

        public Boolean isCompatible(String t_subtype1,String t_subtype2){  //medio al dope creo, al menos devuelva el tipo resultante en caso de ser ocmpatible, y null si no.
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2);
        }

        public String getPairName(String id){
                // devuelve el nombre del pair, sin la posicion de acceso
                return id.substring(0,id.lastIndexOf("{"));             // FIJARSE Q ANDE
        }

        public void chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida  
                AnalizadorLexico.t_simbolos.display();

                System.out.print("chkAndAssign: id: "+id);
                String posid = "";
                String posexpr = "";
                Boolean idPair = isPair(id);
                Boolean exprPair = isPair(expr);
                //AnalizadorLexico.t_simbolos.display();
                if (idPair){
                        // posid es {1} o {2}
                        posid = id.substring(id.lastIndexOf("{"),id.lastIndexOf("}") + 1);
                        System.out.println("assign: posid: "+posid);
                        id = getPairName(id);
                        System.out.println("assign: id: "+id);
                }
                if (exprPair){
                        posexpr = expr.substring(expr.lastIndexOf("{"),expr.lastIndexOf("}") + 1);
                        expr = getPairName(expr);
                }
                if (!isDeclared(id))
                {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": variable "+id+" no declarada. "); }
                else {
                        // EXPR PUEDE SER :CTE, ID, EXPRPAIR, FUN_INVOC, O UN PUTO TERCETOOO
                        String lexemExpr = "";
                        String subtypeT = "";
                        if (isTerceto(expr)) {
                                subtypeT = chkAndGetType(expr);
                                lexemExpr = expr;}   // y lexem es el terceto (expr)
                        else{
                                if (isCte(expr)){      // si es cte, la misma es como se busca en la tabla de simbolos
                                        System.out.println(expr+" es cte");
                                        lexemExpr = expr;
                                        subtypeT = chkAndGetType(expr);
                                } else if (isDeclared(expr)){ 
                                                lexemExpr = getDeclared(expr);
                                                subtypeT = chkAndGetType(expr);
                                        }
                                        else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": "+expr+" no declarada. ");}

                                }
                        
                        String lexemID = getDeclared(id);
                        String subtypeID = chkAndGetType(id);      // SI ES PAIR, DEVUELVE TIPO PRIMITIVO! :D
                        System.out.println("subtypeID: "+subtypeID);
                        System.out.println("subtypeT: "+subtypeT);
                        if (idPair){lexemID = lexemID+posid;}
                        if (exprPair){lexemExpr = lexemExpr+posexpr;}

                        if (subtypeT.equals(subtypeID)){       
                                Terceto.addTercetoT(":=",lexemID,lexemExpr,subtypeID);
                        }
                        // TODO: FALTA DEVOLVER EN $$ EL ID DEL TERCETO (NECESARIO PARA ESTRUCTURAS DE CONTROL..)
                        else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){    
                                Terceto.addTercetoT("utos",lexemExpr,null,"SINGLE");
                                Terceto.addTercetoT(":=",lexemID,lexemExpr,"SINGLE");
                        }// agregar otro else por si uno es uinteger y el otro hexa
                        else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }

                }
        }

        /*public String getType(String lexema){   // se le pasa sin scope
                // puede ser: variable, funcion, cte, expr_pair, terceto
                if (isTerceto(lexema)){return "terceto"}
                if (isCte(lexema)){return AnalizadorLexico.t_simbolos.get_subtype(lexema);}

        }*/


        public String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR ES ID O FUNCION.  SI ES EXPR_PAIR, SE ASUME LLEGA SIN LA POSICION (SIN {})
                String scopeaux = actualScope;
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclared(id)){

                        System.out.println("getDeclared: id: "+id);
                        if (isDeclaredLocal(id)) {return id+":"+actualScope;}
                        else {
                                while (scopeaux.lastIndexOf(":") != -1){

                                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null) {
                                                return id+":"+scopeaux;
                                        }
                                        scopeaux = popScope(scopeaux);     // lo hace con actualScope
                                }
                                return null;    //si no esta declarada..
                        }
                } else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": variable "+id+" no declarada. "); return null;}

        }

        public static Boolean isCharch(String id){     //charhc comienza  y termina con []
                return (id.charAt(0) == '[' && id.charAt(id.length()-1) == ']');
        }

        public static Boolean isPair (String id){
                // un pair tiene la posicion de acceso entre {}; ej: pairsito{1}
                return (id.charAt(id.length()-1) == '}');
        }
//#line 884 "Parser.java"
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
//#line 17 "grammar.y"
{
                /* FIN DEL PROGRAMA, CHEQUEAR:*/
                AnalizadorLexico.t_simbolos.clean();    /* Limpieza de T. de Simbolos: quita todo lo que no tenga scope*/
                TablaEtiquetas.end();                   /* Asocia sentencias GoTo con su correspondiente etiqueta*/
                }
break;
case 2:
//#line 22 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 23 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": La sintaxis del programa es incorrecta." ); }
break;
case 4:
//#line 24 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 25 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 26 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
break;
case 7:
//#line 27 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": El programa está vacío"); }
break;
case 11:
//#line 38 "grammar.y"
{System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
break;
case 12:
//#line 39 "grammar.y"
{System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
break;
case 13:
//#line 40 "grammar.y"
{System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 41 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
break;
case 16:
//#line 48 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");}
break;
case 17:
//#line 52 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 57 "grammar.y"
{System.out.print("Sentencia de control IF en linea "+AnalizadorLexico.line_number);}
break;
case 20:
//#line 58 "grammar.y"
{System.out.print("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
break;
case 21:
//#line 59 "grammar.y"
{System.out.print("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
break;
case 22:
//#line 60 "grammar.y"
{System.out.print("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 23:
//#line 61 "grammar.y"
{System.out.print("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
break;
case 24:
//#line 62 "grammar.y"
{System.out.print("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
break;
case 25:
//#line 63 "grammar.y"
{System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 26:
//#line 64 "grammar.y"
{System.out.println("Sentencia de TAG");}
break;
case 29:
//#line 74 "grammar.y"
{
                String[] idList = val_peek(1).sval.split(",");                
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar(val_peek(2).sval, idList[i]);
                }
                }
break;
case 30:
//#line 81 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta el ';' al final de la sentencia.");
                String[] idList = val_peek(1).sval.split(",");
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar(val_peek(2).sval, idList[i]);
                }
                }
break;
case 31:
//#line 89 "grammar.y"
{
                chkAndDeclareVar(val_peek(2).sval,val_peek(1).sval);
                }
break;
case 32:
//#line 93 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Falta ';' al final de la sentencia o se intenta declarar varias variables sin separarlas con ','.");
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
                }
break;
case 33:
//#line 100 "grammar.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 34:
//#line 101 "grammar.y"
{ yyval.sval = yyval.sval + "," + val_peek(0).sval; }
break;
case 35:
//#line 114 "grammar.y"
{ 
                /* Actualización del scope: fin de la función fuerza retorno al ámbito del padre*/
                        System.out.println("Salgo del ambito: "+actualScope);
                        popScope();
                }
break;
case 36:
//#line 130 "grammar.y"
{
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval)) 
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                else {
                        /* Control de ID: debe ser único en el scope actual*/
                        if (isDeclaredLocal(val_peek(4).sval))       
                                yyerror("No se permite la redeclaración de variables: el nombre seleccionado no está disponible en el scope actual.");
                        else {
                                String param_name = val_peek(2).sval.split("-")[1]; 
                                String param_type = val_peek(2).sval.split("-")[0];
                                System.out.println("param_name, param_type == "+param_name+", "+param_type);
                                
                                /* Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)*/
                                AnalizadorLexico.t_simbolos.del_entry(val_peek(4).sval);
                                AnalizadorLexico.t_simbolos.add_entry(val_peek(4).sval+":"+actualScope,"ID",val_peek(6).sval,"FUN_NAME",param_type);

                                /*String param_lexem = getDeclared(param_name);*/
                                /*System.out.println("param_lexem == "+param_lexem);*/
                                
                                /* Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función*/
                                String act_scope = new String(actualScope);
                                pushScope(val_peek(4).sval); 

                                /* Actualización del ID del parámetro: se actualiza el scope al actual*/
                                AnalizadorLexico.t_simbolos.display();
                                AnalizadorLexico.t_simbolos.del_entry(param_name);      /* param_name llega con el scope y todo (desde donde fue llamado)*/
                                AnalizadorLexico.t_simbolos.add_entry(param_name+":"+actualScope,"ID",val_peek(6).sval,"VARIABLE_NAME",param_type);

                        /* Posible generación de terceto de tipo LABEL*/
                                yyval.sval = Terceto.addTercetoT("LABEL_FUN",val_peek(4).sval+":"+act_scope,param_name+":"+act_scope,param_type); /*para saber donde llamarla en assembler*/
                        }
                }
        }
break;
case 37:
//#line 164 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;
                }
break;
case 38:
//#line 168 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))
                        yyerror("Error: Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
break;
case 39:
//#line 188 "grammar.y"
{
                /* se le pone scope 'MAIN' */
                if (!AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                else {
                        if (val_peek(2).sval.equals("UINTEGER") || val_peek(2).sval.equals("SINGLE") || val_peek(2).sval.equals("HEXA")) {

                                if (AnalizadorLexico.t_simbolos.get_entry(val_peek(0).sval+":MAIN") != null){
                                        yyerror("Error: Se esta redeclarando "+val_peek(0).sval +" en linea "+AnalizadorLexico.line_number);
                                } else {
                                        AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":MAIN","ID",val_peek(2).sval,"TYPE_NAME");
                                }
                        } else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipo invalido para pair. Solo se permite primitivos: uinteger, single, hexadecimal."); }
                }

        }
break;
case 40:
//#line 203 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 41:
//#line 204 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 42:
//#line 205 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 43:
//#line 213 "grammar.y"
{
                yyval.sval = val_peek(1).sval+"-"+val_peek(0).sval;
        }
break;
case 44:
//#line 216 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 217 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 47:
//#line 222 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 53:
//#line 269 "grammar.y"
{       /*pdoria poner end_if dentro de then_statement y hacer esto ahi.*/
        /*completo terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),null,String.valueOf(Integer.parseInt((val_peek(1).sval).substring(1,(val_peek(1).sval).length()-1)+1))); 
}
break;
case 54:
//#line 273 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 55:
//#line 275 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),String.valueOf(Integer.parseInt(val_peek(0).sval) + 1),null); 

}
break;
case 56:
//#line 283 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,null);
                Terceto.pushTerceto(yyval.sval); /*apilo terceto incompleto.*/
        }
break;
case 57:
//#line 287 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 58:
//#line 288 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
break;
case 59:
//#line 290 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 60:
//#line 318 "grammar.y"
{
                yyval.sval = val_peek(0).sval;       /*devuelve ultimo terceto*/
        }
break;
case 61:
//#line 322 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 62:
//#line 326 "grammar.y"
{yyval.sval = val_peek(1).sval;}
break;
case 63:
//#line 327 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. "); }
break;
case 64:
//#line 328 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta "); }
break;
case 65:
//#line 329 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 66:
//#line 333 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BI",null,null); /*incompleto, primer operando se completara despues.*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,String.valueOf(Integer.parseInt(yyval.sval) + 1));/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval);
        }
break;
case 68:
//#line 344 "grammar.y"
{   /* NO CONTEMPLA USAR TIPOS DEFINIDOS POR USUARIOOOOOOO*/
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                /* compatibilidades y conversiones:*/
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, t_subtype1);       /* NO SE SI HACE FALTA TIPO PERO PORLASDUDAS POR AHORA LO PONGO*/
                        } else if (t_subtype1.equals("SINGLE")) {
                                yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);              /* se pasa el tipo de id2 para saber si es uinteger o hexa*/
                                yyval.sval= Terceto.addTercetoT(val_peek(1).sval,id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")) {
                                yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                yyval.sval= Terceto.addTercetoT(val_peek(1).sval,id1,id2, "SINGLE");
                        }else if(t_subtype1.equals("HEXA") && t_subtype2.equals("UINTEGER")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, "HEXA");
                                        yyval.sval= Terceto.addTercetoT(val_peek(1).sval,id1,id2, "UINTEGER");
                        }else if (t_subtype1.equals("UINTEGER") && t_subtype2.equals("HEXA")){
                                        yyval.sval= Terceto.addTercetoT("utos",id2,null, "HEXA");
                                        yyval.sval= Terceto.addTercetoT(val_peek(1).sval,id1,id2, "UINTEGER");
                        }
                } else {System.out.println("Un tipo dio null en condicion");}
                
        }
break;
case 69:
//#line 369 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 76:
//#line 382 "grammar.y"
{
                chkAndAssign(val_peek(2).sval,val_peek(0).sval);
                }
break;
case 77:
//#line 386 "grammar.y"
{
                chkAndAssign(val_peek(2).sval,val_peek(0).sval);
        }
break;
case 78:
//#line 389 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 79:
//#line 395 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
                System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");}
                } else {System.out.println("Un tipo dio null en suma");}
}
break;
case 80:
//#line 421 "grammar.y"
{            /*la expr o el term pueden ser: variable, funcion, expr_pair,cte,terceto.*/
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
                System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");}
                } else {System.out.println("Un tipo dio null en resta");}

        }
break;
case 82:
//#line 447 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 83:
//#line 451 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
                System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        yyval.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        yyval.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");}
                } else {System.out.println("Un tipo dio null en multiplicacion");}
        }
break;
case 84:
//#line 475 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
                System.out.println("tipo1: "+t_subtype1);
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                System.out.println("tipo2: "+t_subtype2);
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER") || t_subtype1.equals("HEXA")) {
                                        yyval.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype1);}
                        } else if (t_subtype1.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id2,null, t_subtype2);
                                        yyval.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");}
                } else {System.out.println("Un tipo dio null en division");}
        }
break;
case 88:
//#line 513 "grammar.y"
{    /* SOLUCIONAR ESTOO, ¿CONTADOR DE REFENRCIAS? PROBAR CASOS DIF Y VER QUÉ Y COMO DETECTA LA CTE NEGATIVA*/
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 89:
//#line 518 "grammar.y"
{      /* uso tendria q ser "variable_name" o podria ser otra?*/
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba variable de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 92:
//#line 528 "grammar.y"
{       
                if (isDeclared(val_peek(3).sval)){
                        /* si ID es de un tipo definido (el tipo de ID esta en la tabla de simbolos)*/
                        String lexem = getDeclared(val_peek(3).sval);
                        String baseType = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                        System.out.println("lexem: "+lexem+" baseType: "+baseType);
                        if (!AnalizadorLexico.t_simbolos.get_use(baseType).equals("TYPE_NAME")) {
                                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba variable de tipo pair. "); }
                        else {
                                if (!(val_peek(1).sval.equals("1") || val_peek(1).sval.equals("2"))) {
                                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba constante 1 o 2. "); 
                                }
                                else {yyval.sval = val_peek(3).sval+"{"+val_peek(1).sval+"}";}
                        }
                } else {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": variable no declarada. "); }
                /*TODO: EN $$.sval DEVOLVER EL LEXEMA QUE REPRESENTARIA A ESE PAIR EN ESA POSICION (POR EJEMPLO pairsito{1})*/
        }
break;
case 93:
//#line 548 "grammar.y"
{ 
                String lexema = getDeclared(val_peek(3).sval);
                System.out.println(lexema);
                if (lexema != null && AnalizadorLexico.t_simbolos.get_use(lexema).equals("FUN_NAME")) {
                        /*chequear tipo de parametros*/
                        if (!AnalizadorLexico.t_simbolos.get_value(lexema).equals(chkAndGetType(val_peek(1).sval))) {
                                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipo de parametro incorrecto. ");
                        } else {        /* se va pasando el terceto del llamado a la funcion.*/
                        yyval.sval = Terceto.addTercetoT("CALL_FUN", lexema, val_peek(1).sval, AnalizadorLexico.t_simbolos.get_subtype(lexema));}
                } else {
                        yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": "+val_peek(3).sval+" no es una funcion o no esta al alcance. ");
                }
        }
break;
case 94:
//#line 561 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 95:
//#line 565 "grammar.y"
{   /*expr puede  VARIBLE, CTE, funcion, terceto(varaux),*/
                /* si es ID o funcion o exprpair se pasa con scope*/
                /* CHEQUEAR LA EXPR SEA VALIDA, ES DECIR SI ES VARIABLE O FUNCIOn, QUE ESTE DECLARADO*/
                /* y si es pair pasarlo bien*/
                String lexem = val_peek(1).sval;
                String pos = "";
                if (!isTerceto(lexem) && (!isCte(lexem)) && (!isCharch(lexem))){
                        /* es variable o funcion*/
                        if (isPair(lexem)) {
                                pos = lexem.substring(lexem.lastIndexOf("{"),lexem.lastIndexOf("}") + 1);
                                System.out.println("pos: "+pos);
                                lexem = getDeclared(getPairName(lexem)) + pos;
                        } else {
                                lexem = getDeclared(lexem);
                        }
                }
                yyval.sval = Terceto.addTercetoT("OUTF",lexem,null,chkAndGetType(val_peek(1).sval));
        }
break;
case 96:
//#line 583 "grammar.y"
{
                yyval.sval = Terceto.addTercetoT("OUTF",val_peek(1).sval,null,"CHARCH");
        }
break;
case 97:
//#line 586 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 98:
//#line 587 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 99:
//#line 607 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,val_peek(6).sval);
                /* si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());*/
        }
break;
case 100:
//#line 611 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 101:
//#line 612 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 102:
//#line 613 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 103:
//#line 615 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 104:
//#line 616 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 105:
//#line 617 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 106:
//#line 618 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 107:
//#line 619 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 108:
//#line 620 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 109:
//#line 625 "grammar.y"
{
                /* opcion 1: pila:*/
                /*UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)*/
                /* opcion 2:*/
                yyval.sval = strToTID(Terceto.getTercetoCount());  /*paso id del proximo terceto*/
        }
break;
case 110:
//#line 634 "grammar.y"
{
                String[] idList = val_peek(2).sval.split(",");
                String[] exprList = val_peek(0).sval.split(",");
                if (idList.length != exprList.length){
                        yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. ");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
break;
case 111:
//#line 646 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 112:
//#line 651 "grammar.y"
{
                
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;

        }
break;
case 113:
//#line 656 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
        }
break;
case 116:
//#line 670 "grammar.y"
{
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
        }
break;
case 117:
//#line 674 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
                
        }
break;
case 118:
//#line 682 "grammar.y"
{
                /* buscar si no hay otra tag con el mismo nombre al alcance*/
                System.out.println("wtf");
                if (!isDeclaredLocal(val_peek(0).sval)) {
                        /* reinserción en la T. de S. con scope actual*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(0).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"TAG","","tag_name","");
                        /* agregar a la tabla de etiquetas*/
                        TablaEtiquetas.add_tag(val_peek(0).sval); 
                        Terceto.addTerceto("LABEL_TAG",val_peek(0).sval+":"+actualScope,null);
                } else yyerror("ERROR: La etiqueta "+val_peek(0).sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                AnalizadorLexico.t_simbolos.display();
        }
break;
case 119:
//#line 702 "grammar.y"
{
                /*if existe en TS {*/
                        yyval.sval= Terceto.addTerceto("JUMP_TAG",null,null);      /*se pone terceto incompleto, se completara al final del programa*/
                        TablaEtiquetas.add_goto(val_peek(0).sval,Terceto.parseTercetoId(yyval.sval),AnalizadorLexico.line_number);     /* donde puse 0 iría número de línea en lo posible*/
                /*}*/
        }
break;
case 120:
//#line 708 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1721 "Parser.java"
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

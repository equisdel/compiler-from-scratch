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
   31,   34,   34,   11,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   35,   14,   14,
   36,   36,   38,   38,   37,   37,   16,   13,   13,
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
    1,    1,    3,    3,    1,    1,    1,    2,    1,    1,
    4,    4,    5,    4,    4,    3,    2,    7,    5,    6,
    6,    6,    4,    5,    5,    6,    6,    2,    3,    3,
    3,    3,    1,    1,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  117,    0,    0,    0,
    0,    0,   49,   50,    0,   51,    0,    0,    8,   10,
    0,   12,   13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   14,
    0,    0,    0,    0,   87,    0,    0,    0,    0,   90,
    0,   85,   89,    0,   97,    0,    0,    0,   82,    0,
    0,  108,  119,  118,    4,    9,   15,   11,   19,   20,
   21,   22,   23,   24,   25,   17,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,   88,   73,   74,
   75,   70,   71,   72,    0,    0,    0,   59,    0,    0,
    0,    1,    0,   96,    0,   52,    0,    0,    0,    0,
   32,    0,   31,    0,    0,    0,   30,   29,    0,   35,
   61,    0,   60,   54,   66,   53,   55,    0,    0,    0,
    0,   28,    0,    0,    0,    0,    0,  114,  112,  111,
   91,    0,   56,    0,    0,    0,   83,   84,   95,   94,
    0,   41,    0,   46,    0,   33,    0,    0,   34,    0,
   63,    0,    0,    0,    0,    0,    0,    0,    0,   92,
    0,   40,    0,    0,    0,    0,    0,   64,   65,   62,
    0,  105,    0,    0,    0,    0,    0,   93,   42,   39,
   45,   43,    0,    0,    0,  102,  107,    0,  101,  106,
   37,   38,   36,   98,
};
final static short yydgoto[] = {                          3,
   18,   19,   20,   21,   68,   22,   23,   77,   24,   25,
   26,   27,   28,   29,   30,   31,  132,   32,   80,   33,
   82,  185,   48,   34,   84,  137,   49,  133,  138,  107,
   50,   51,   52,   53,   36,   37,  146,   38,
};
final static short yysindex[] = {                      -207,
 -262,  574,    0,  707,  -20, -105,    0,  -17,  707,  -19,
  -57,  -11,    0,    0, -184,    0, -245,  623,    0,    0,
   32,    0,    0,   32,   32,   32,   32,   32,   32,   32,
   41, -190,  707, -148, -138,  754,  -31,   86,  655,    0,
  133, -116,    0,  -30,    0,  161, -114,  -36,  119,    0,
   57,    0,    0,  681,    0,  -37,  -46, -221,    0,  133,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -28, -200,  -39,
  707, -106,  -72, -192,  133, -110,    0,  775,  -84,  214,
  -75,  -75,    0,   64,   61,  133,  146,    0,    0,    0,
    0,    0,    0,    0,   28,   28,  133,    0,   28,   28,
  -20,    0,  147,    0,   76,    0, -221,  -68,  129,  102,
    0,  133,    0,  -63,  155,  156,    0,    0,  -60,    0,
    0,  796,    0,    0,    0,    0,    0,  733,   64,   -8,
  -40,    0,  -62,    0,  127,  164,   89,    0,    0,    0,
    0,   65,    0,   57,   57,   64,    0,    0,    0,    0,
  151,    0,  -43,    0,   64,    0, -210, -120,    0,  -52,
    0, -180,  161,  202,   -5,  161,  133,  133,  203,    0,
 -154,    0,    0, -129,  215,  219,  223,    0,    0,    0,
  238,    0,  136,  240,  249,   64,   64,    0,    0,    0,
    0,    0,  -13,   37,   39,    0,    0,  270,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                       312,
  316,    0,    0,    0,    0,  -42,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  331,    0,    0,
  540,    0,    0,  488,  488,  488,  488,  488,  488,  488,
  514,    0,    0,    0,  293,    0,    0,    0,    0,    0,
    0,    0,  105,    1,    0,    0,    0,    0,   72,    0,
   27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  236,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   73,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  274,    0,    0,   81,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  357,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -175,    0,    0,    0,    0,    0,    0,  300,    0,
    0,    0,    0,  131,    0,  326,  -29,    0,    0,    0,
    0,    0,    0,   53,   79,  157,    0,    0,    0,    0,
    0,    0,    0,    0,  352,    0,    0,    0,    0,    0,
    0,    0,    0,  378,    0,    0,    0,    0,    0,    0,
    0,    0,  159,    0,    0,    0,    0,    0,    0,    0,
  404,    0,    0,  435,    0,  183,  210,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  461,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    8,   20,   -3,    0,  497,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  327,   -6,    0,    0,
    0,  196,  762,    0,    0,    0,  -24,  230,    0,    0,
  790,   48,   24,    0,    0,    0,    0,   88,
};
final static int YYTABLESIZE=1077;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        176,
   86,  113,   58,  114,  129,    4,  105,   47,  106,   96,
   63,   39,   91,  117,  113,  124,   54,   42,   64,  128,
   56,   97,   46,  102,  104,  103,   81,   47,   60,   89,
  123,  173,   87,   47,  193,  116,   47,   66,   40,   47,
   81,   86,   86,   86,   86,   86,  183,   86,    1,    2,
  118,  119,   79,   13,   14,  125,  126,   16,   66,   86,
   86,   86,   86,  134,   13,   14,   78,   81,   16,   81,
   81,   81,   47,   66,  135,  189,   89,  136,   80,   87,
   67,   89,   79,   62,  142,   81,   81,   81,   81,  190,
   67,   67,   42,   79,   67,   79,   79,   79,  109,   76,
   66,  199,  200,  110,   69,  180,  105,  105,  106,  106,
  161,   79,   79,   79,   79,  174,  160,   83,  105,   80,
  106,   80,   80,   80,   85,   89,  201,  202,  142,   92,
  110,   89,  157,  158,   87,  186,  183,   80,   80,   80,
   80,   95,  164,   98,  105,   69,  106,   82,  191,   82,
  194,  195,  154,  155,   13,   14,   68,   41,   16,  108,
  184,  184,  130,   69,   82,   82,   82,  140,  208,  105,
  177,  106,  143,   82,   82,   82,  207,   47,  149,  150,
   47,  147,  115,  131,    6,  151,  153,  159,  162,  110,
  163,    7,    8,  166,  167,  168,  169,   68,   10,   44,
  122,   12,   13,   14,   15,   47,   16,  178,   17,  116,
  116,   42,  181,  182,   52,   68,  127,  188,   59,   44,
   45,  113,   57,   99,  100,  101,  115,  121,   13,   14,
   52,   90,   16,  113,  122,   47,   55,  175,   43,   44,
   45,  115,  192,  198,   59,   44,   45,   43,   44,   45,
   43,   44,   45,  116,  211,  203,   86,   86,   47,  204,
   86,   86,   86,  205,   86,   86,   86,   86,  116,   86,
   86,   86,   86,   76,   86,   86,   86,   86,  206,   86,
  209,   86,   81,   81,   44,   45,   81,   81,   81,  210,
   81,   81,   81,   81,   47,   81,   81,   81,   81,   77,
   81,   81,   81,   81,  212,   81,  213,   81,   79,   79,
  214,    7,   79,   79,   79,    3,   79,   79,   79,   79,
  179,   79,   79,   79,   79,  109,   79,   79,   79,   79,
    6,   79,   76,   79,   80,   80,  114,   57,   80,   80,
   80,   48,   80,   80,   80,   80,   58,   80,   80,   80,
   80,   78,   80,   80,   80,   80,    5,   80,   77,   80,
   69,   69,   88,  187,   82,   82,   82,  172,   69,   69,
   69,   69,    0,   69,   69,   69,   69,  103,   69,   69,
   69,   69,    0,   69,  109,   69,  110,  110,   59,   44,
   45,   43,   44,   45,  110,  110,    0,  110,    0,  110,
  110,  110,  110,  104,  110,  110,  110,  110,    0,  110,
   78,  110,   68,   68,   52,   52,   43,   44,   45,    0,
   68,   68,   68,   68,    0,   68,   68,   68,   68,    0,
   68,   68,   68,   68,   99,   68,  103,   68,  115,  115,
    0,    0,    0,    0,    0,    0,  115,  115,    0,  115,
    0,  115,  115,  115,  115,    0,  115,  115,  115,  115,
  100,  115,  104,  115,    0,  116,  116,    0,    0,  144,
   44,   45,    0,  116,  116,    0,  116,    0,  116,  116,
  116,  116,    0,  116,  116,  116,  116,   16,  116,    0,
  116,   47,   47,   99,    0,    0,    0,    0,    0,   47,
   47,    0,   47,    0,   47,   47,   47,   47,    0,   47,
   47,   47,   47,   18,   47,    0,   47,    0,    0,  100,
   69,   70,   71,   72,   73,   74,   75,    0,    0,   76,
   76,    0,    0,    0,    0,    0,    0,   76,   76,   16,
   76,    0,   76,   76,   76,   76,    0,   76,   76,   76,
   76,    0,   76,    0,   76,   77,   77,    0,    0,    0,
    0,    0,    0,   77,   77,    0,   77,    0,   77,   77,
   77,   77,    0,   77,   77,   77,   77,    0,   77,    0,
   77,  109,  109,    0,    0,    0,    0,    0,    0,  109,
  109,    0,  109,    0,  109,  109,  109,  109,    0,  109,
  109,  109,  109,    0,  109,    0,  109,   78,   78,    0,
    0,    0,    0,    0,    0,   78,   78,    0,   78,    0,
   78,   78,   78,   78,    0,   78,   78,   78,   78,    0,
   78,    0,   78,  103,  103,    0,    0,    0,    0,    0,
    0,  103,  103,    0,  103,    0,  103,  103,  103,  103,
    0,  103,  103,  103,  103,    0,  103,    0,  103,  104,
  104,    0,    0,    0,    0,    0,    0,  104,  104,    0,
  104,    0,  104,  104,  104,  104,    0,  104,  104,  104,
  104,    0,  104,    0,  104,    0,    0,    0,    0,    0,
   99,   99,    0,    0,    0,    0,    0,    0,   99,   99,
    0,   99,    0,   99,   99,   99,   99,    0,   99,   99,
   99,   99,    0,   99,    0,   99,  100,  100,    0,    0,
    0,    0,    0,    0,  100,  100,    0,  100,    0,  100,
  100,  100,  100,    0,  100,  100,  100,  100,    0,  100,
    0,  100,    0,   16,   16,    0,    0,    0,    0,    0,
    0,   16,   16,    0,   16,    0,   16,   16,   16,   16,
    0,   16,   16,   16,   16,    0,   16,    0,   16,   18,
   18,    0,    0,   61,    0,    0,    0,   18,   18,    0,
   18,    0,   18,   18,   18,   18,    0,   18,   18,   18,
   18,   35,   18,   35,   18,   16,   16,    0,   35,    0,
    0,    0,   94,   16,   16,    0,    0,   35,   16,    0,
   16,   16,    0,   16,   16,   16,   16,  115,   16,    0,
   16,  120,   35,    0,    0,   35,    0,    0,   35,    5,
    6,    0,    0,    0,    0,    0,    0,    7,    8,    0,
    0,    9,    0,   35,   10,   11,  139,   12,   13,   14,
   15,  145,   16,    0,   17,    0,    0,  152,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  156,    0,
   35,    0,   35,    0,    0,    0,    0,   35,    5,    6,
  148,  148,    0,  165,    0,    0,    7,    8,    0,    0,
    0,   65,    0,   10,   11,    0,   12,   13,   14,   15,
    0,   16,    0,   17,    0,    0,    0,    0,    0,    0,
    5,    6,    0,    0,    0,    0,    0,    0,    7,    8,
    0,   35,    0,   93,    0,   10,   11,   35,   12,   13,
   14,   15,    0,   16,    0,   17,  111,    6,  196,  197,
    0,    0,    0,    0,    7,    8,    0,    0,    0,  112,
    0,   10,   11,    0,   12,   13,   14,   15,    0,   16,
    0,   17,    5,    6,    0,    0,    0,    0,    0,    0,
    7,    8,    0,    0,    0,    0,    0,   10,   11,    0,
   12,   13,   14,   15,    0,   16,    0,   17,  170,    6,
    0,    0,    0,    0,    0,    0,    7,    8,    0,    0,
    0,    0,  171,   10,    0,    0,   12,   13,   14,   15,
    6,   16,    0,   17,    0,    0,    0,    7,    8,    0,
    0,    0,   86,    0,   10,    0,    0,   12,   13,   14,
   15,    6,   16,    0,   17,    0,    0,    0,    7,    8,
    0,    0,    0,  141,    0,   10,    0,    0,   12,   13,
   14,   15,    6,   16,    0,   17,    0,    0,    0,    7,
    8,    0,    0,    0,    0,    0,   10,    0,    0,   12,
   13,   14,   15,    0,   16,    0,   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   44,   60,   41,   44,  268,   43,   45,   45,   40,
  256,    4,   44,   60,   44,   44,    9,  123,  264,   59,
   40,   46,   40,   60,   61,   62,    0,   45,   40,   36,
   59,   40,   36,   45,   40,  257,   45,   18,   59,   45,
   33,   41,   42,   43,   44,   45,  257,   47,  256,  257,
   57,   58,    0,  275,  276,  256,  257,  279,   39,   59,
   60,   61,   62,  256,  275,  276,  257,   41,  279,   43,
   44,   45,   45,   54,  267,  256,   83,  270,    0,   83,
  256,   88,  273,  268,   88,   59,   60,   61,   62,  270,
   59,  267,  123,   41,  270,   43,   44,   45,   42,   59,
   81,  256,  257,   47,    0,   41,   43,   43,   45,   45,
  117,   59,   60,   61,   62,  140,   41,  266,   43,   41,
   45,   43,   44,   45,  263,  132,  256,  257,  132,   44,
    0,  138,  109,  110,  138,  256,  257,   59,   60,   61,
   62,  258,   41,  258,   43,   41,   45,   43,  173,   45,
  175,  176,  105,  106,  275,  276,    0,  263,  279,   41,
  167,  168,  269,   59,   60,   61,   62,  278,  193,   43,
   44,   45,  257,   43,   44,   45,   41,   45,   91,   92,
   45,  257,    0,  256,  257,  125,   41,   41,  257,   59,
   62,  264,  265,  257,   40,   40,  257,   41,  271,   41,
  263,  274,  275,  276,  277,   45,  279,   44,  281,    0,
  257,  123,   62,  257,  257,   59,  256,  270,  256,  257,
  258,  259,  280,  260,  261,  262,   44,  256,  275,  276,
  273,  263,  279,  263,  263,    0,  256,  278,  256,  257,
  258,   59,   41,   41,  256,  257,  258,  256,  257,  258,
  256,  257,  258,   44,  268,   41,  256,  257,   45,   41,
  260,  261,  262,   41,  264,  265,  266,  267,   59,  269,
  270,  271,  272,    0,  274,  275,  276,  277,   41,  279,
   41,  281,  256,  257,  257,  258,  260,  261,  262,   41,
  264,  265,  266,  267,   59,  269,  270,  271,  272,    0,
  274,  275,  276,  277,  268,  279,  268,  281,  256,  257,
   41,    0,  260,  261,  262,    0,  264,  265,  266,  267,
  256,  269,  270,  271,  272,    0,  274,  275,  276,  277,
    0,  279,   59,  281,  256,  257,   44,  266,  260,  261,
  262,  269,  264,  265,  266,  267,  266,  269,  270,  271,
  272,    0,  274,  275,  276,  277,    0,  279,   59,  281,
  256,  257,   36,  168,  260,  261,  262,  138,  264,  265,
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
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  107,   -1,
   81,   -1,   83,   -1,   -1,   -1,   -1,   88,  256,  257,
   91,   92,   -1,  122,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
   -1,  279,   -1,  281,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,  132,   -1,  269,   -1,  271,  272,  138,  274,  275,
  276,  277,   -1,  279,   -1,  281,  256,  257,  177,  178,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,  269,
   -1,  271,  272,   -1,  274,  275,  276,  277,   -1,  279,
   -1,  281,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,   -1,   -1,   -1,   -1,  271,  272,   -1,
  274,  275,  276,  277,   -1,  279,   -1,  281,  256,  257,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,   -1,  270,  271,   -1,   -1,  274,  275,  276,  277,
  257,  279,   -1,  281,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,
  277,  257,  279,   -1,  281,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,
  276,  277,  257,  279,   -1,  281,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,  274,
  275,  276,  277,   -1,  279,   -1,  281,
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

//#line 747 "grammar.y"
        public static ArrayList<String> errores = new ArrayList<>();
        public static String actualScope = "MAIN";
        //static Stack<Integer> UntilStack = new Stack<>();


	public static void yyerror(String msg){
                errores.add(msg);
	       //System.out.println(msg);
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
                       //System.out.println(valStr+"ES terceto");
                        return Terceto.getSubtipo(valStr);
                        }
                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        if (isCte(valStr)) {
                               //System.out.println(valStr+"ES cte");
                                return AnalizadorLexico.t_simbolos.get_subtype(valStr);}
                        else {  // variable, invoc. a funcion o expr_pair
                                if (isPair(valStr)) {
                                        lexem = valStr.substring(0,valStr.indexOf("{")); // me quedo con el id del pair
                                        lexem = getDeclared(lexem);
                                } else if (isFunction(valStr)){
                                        lexem = getFunctionID(valStr);  //vuelve con ambito
                                } else {lexem = getDeclared(valStr);}
                               //System.out.println("NO ES CTE -> "+lexem);
                                String type = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                                // AnalizadorLexico.t_simbolos.display();
                               //System.out.println("se busco: "+lexem);
                                if (lexem != null){     // si está declarada
                                        if (!(type.equals("SINGLE") || type.equals("UINTEGER") || type.equals("HEXA"))) {
                                                //es tipo definido por usuario
                                               //System.out.println("El tipo es "+type+" y su uso es "+AnalizadorLexico.t_simbolos.get_use(type));
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
               //System.out.println("aaaa"+valStr);
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
               //System.out.println("Scope tras salir: "+actualScope);
        }

        public String popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                int index = scope.lastIndexOf(":");
                System.out.print("popScope scope: "+scope);
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                return scope;
        }


        public boolean isDeclared(String id){   // recibe id sin scoope
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
                if (isCte(id)) {System.out.println("OJO ESTAS PASANDO UNA CTE A isDeclared");}
                String scopeaux = new String(actualScope);
                //System.out.println("EL SCOPEAUX: "+scopeaux);
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclaredLocal(id)) {return true;}
                else {
                        do {
                                scopeaux = (scopeaux.equals("MAIN")) ? "MAIN" : scopeaux.substring(0,scopeaux.lastIndexOf(":"));
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null)     
                                        return true;
                                //System.out.println("EL SCOPEAUX: "+scopeaux);
                        } while ((!scopeaux.equals("MAIN")));
                }
                //System.out.println("no hubo suerte amiga");
                return false;
        }

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public void chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                //chequear tipo sea valido (uinteger,hexa,single o definido por usuario)
                // AnalizadorLexico.t_simbolos.display();
                if (AnalizadorLexico.t_simbolos.get_entry(tipo) == null) {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipo de variable no valido. "); }
                else {//System.out.println("tipo de var declarada:" +AnalizadorLexico.t_simbolos.get_entry(tipo).getUse());
                        }
                
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

        public String chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida  
                //AnalizadorLexico.t_simbolos.display();

                //System.out.print("chkAndAssign: id: "+id);
                String posid = "";
                String posexpr = "";
                Boolean idPair = isPair(id);
                Boolean exprPair = isPair(expr);
                //AnalizadorLexico.t_simbolos.display();
                if (idPair){
                        // posid es {1} o {2}
                        posid = id.substring(id.lastIndexOf("{"),id.lastIndexOf("}") + 1);
                       //System.out.println("assign: posid: "+posid);
                        id = getPairName(id);
                       //System.out.println("assign: id: "+id);
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
                                       //System.out.println(expr+" es cte");
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
                       //System.out.println("subtypeID: "+subtypeID);
                       //System.out.println("subtypeT: "+subtypeT);
                        if (idPair){lexemID = lexemID+posid;}
                        if (exprPair){lexemExpr = lexemExpr+posexpr;}

                        if (subtypeT.equals(subtypeID)){       
                                return Terceto.addTercetoT(":=",lexemID,lexemExpr,subtypeID);
                        }
                        // TODO: FALTA DEVOLVER EN $$ EL ID DEL TERCETO (NECESARIO PARA ESTRUCTURAS DE CONTROL..)
                        else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){    
                                Terceto.addTercetoT("utos",lexemExpr,null,"SINGLE");
                                return Terceto.addTercetoT(":=",lexemID,lexemExpr,"SINGLE");
                        }// agregar otro else por si uno es uinteger y el otro hexa
                        else if (subtypeID.equals("UINTEGER") && subtypeT.equals("HEXA")){
                                Terceto.addTercetoT("stou",lexemExpr,null,"UINTEGER");
                                return Terceto.addTercetoT(":=",lexemID,lexemExpr,"UINTEGER");
                        }
                        else if (subtypeID.equals("HEXA") && subtypeT.equals("UINTEGER")){
                        Terceto.addTercetoT("stoh",lexemExpr,null,"HEXA");
                                return Terceto.addTercetoT(":=",lexemID,lexemExpr,"HEXA");
                        }
                        else if (!subtypeID.equals("") && !subtypeT.equals("")) {       // para que no de error de tipos incompatibles si enrealidad era otro error antes
                                System.out.println("subtypeID:"+subtypeID+" subtypeT:"+subtypeT);
                                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); 
                        }

                }
                return Terceto.getLast();
        }

        /*public String getType(String lexema){   // se le pasa sin scope
                // puede ser: variable, funcion, cte, expr_pair, terceto
                if (isTerceto(lexema)){return "terceto"}
                if (isCte(lexema)){return AnalizadorLexico.t_simbolos.get_subtype(lexema);}

        }*/


        public String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR ES ID O FUNCION.  SI ES EXPR_PAIR, SE ASUME LLEGA SIN LA POSICION (SIN {})
                String scopeaux = new String(actualScope);;
                //AnalizadorLexico.t_simbolos.display();
                if (isDeclared(id)){

                       //System.out.println("getDeclared: id: "+id);
                        if (isDeclaredLocal(id)) {return id+":"+actualScope;}
                        else {
                                do {
                                        scopeaux = (scopeaux.equals("MAIN")) ? "MAIN" : scopeaux.substring(0,scopeaux.lastIndexOf(":"));
                                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null)     
                                                return id+":"+scopeaux;;
                                        System.out.println("EL SCOPEAUX: "+scopeaux);
                                } while ((!scopeaux.equals("MAIN")));
                                return null;
                        }
                } else {System.out.println("NO ESTA DECLARADA");return null;}

        }

        public static Boolean isCharch(String id){     //charhc comienza  y termina con []
                return (id.charAt(0) == '[' && id.charAt(id.length()-1) == ']');
        }

        public static Boolean isPair (String id){
                // un pair tiene la posicion de acceso entre {}; ej: pairsito{1}
                return (id.charAt(id.length()-1) == '}');
        }
        public static String scopeToFunction(String f) {
                // devuelve una funcion dado el scope.
                // el scope esta en formato MAIN:FUN1:FUN2:FUN3:FUN4
                // y la funcion a devolver seria FUN4:MAIN:FUN1:FUN2:FUN3
                String[] parts = f.split(":");
                StringBuilder result = new StringBuilder(parts[parts.length - 1]);
                for (int i = 0; i < parts.length - 1; i++) {
                    result.append(":").append(parts[i]);
                }
                return result.toString();
            }

        public static String extractNumber(String terceto) {
        if (terceto.startsWith("<") && terceto.endsWith(">")) {
            return terceto.substring(1, terceto.length() - 1);
        } else {
            return terceto; 
        }
    }
//#line 910 "Parser.java"
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
{/*System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 12:
//#line 39 "grammar.y"
{/*System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);*/}
break;
case 13:
//#line 40 "grammar.y"
{/*System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 14:
//#line 41 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia\n");}
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
{/*System.out.print("Sentencia de control IF en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 20:
//#line 58 "grammar.y"
{/*System.out.print("Sentencia de asignacion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 21:
//#line 59 "grammar.y"
{/*System.out.print("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 22:
//#line 60 "grammar.y"
{/*System.out.print("Sentencia de repeat until en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 23:
//#line 61 "grammar.y"
{/*System.out.print("Sentencia de salto goto en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 24:
//#line 62 "grammar.y"
{/*System.out.print("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 25:
//#line 63 "grammar.y"
{/*System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 26:
//#line 64 "grammar.y"
{/*System.out.println("Sentencia de TAG\n");*/}
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
                        yyval.sval = Terceto.addTercetoT("END_FUN",scopeToFunction(actualScope),null,null);
                        /*System.out.println("Salgo del ambito: "+actualScope);*/
                        TablaEtiquetas.popScope();
                        popScope();
                }
break;
case 36:
//#line 132 "grammar.y"
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
                               /*System.out.println("param_name, param_type == "+param_name+", "+param_type);*/
                                
                                /* Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)*/
                                AnalizadorLexico.t_simbolos.del_entry(val_peek(4).sval);
                                AnalizadorLexico.t_simbolos.add_entry(val_peek(4).sval+":"+actualScope,"ID",val_peek(6).sval,"FUN_NAME",param_type);
                                AnalizadorLexico.t_simbolos.display();
                                /*String param_lexem = getDeclared(param_name);*/
                                /*System.out.println("param_lexem == "+param_lexem);*/
                                
                                /* Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función*/
                                String act_scope = new String(actualScope);
                                pushScope(val_peek(4).sval); 

                                /* Actualización del ID del parámetro: se actualiza el scope al actual*/
                                /* AnalizadorLexico.t_simbolos.display();*/
                                AnalizadorLexico.t_simbolos.del_entry(param_name);      /* param_name llega con el scope y todo (desde donde fue llamado)*/
                                AnalizadorLexico.t_simbolos.add_entry(param_name+":"+actualScope,"ID",param_type,"VARIABLE_NAME");

                        /* Posible generación de terceto de tipo LABEL*/
                                yyval.sval = Terceto.addTercetoT("INIC_FUN",val_peek(4).sval+":"+act_scope,param_name+":"+act_scope,param_type); /*para saber donde llamarla en assembler*/
                        }
                }
        }
break;
case 37:
//#line 166 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;
                }
break;
case 38:
//#line 170 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))
                        yyerror("Error: Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
break;
case 39:
//#line 190 "grammar.y"
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
//#line 205 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 41:
//#line 206 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 42:
//#line 207 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 43:
//#line 215 "grammar.y"
{
                yyval.sval = val_peek(1).sval+"-"+val_peek(0).sval;
        }
break;
case 44:
//#line 218 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 219 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 46:
//#line 223 "grammar.y"
{     /* semanticamente q hacemos con esto? 
        LOS CHEQUEOS SOBRE ESTO SON COMPLICADOS, POR LO Q ENTENDI SOLO HACE FALTA:
        -CHEQUEAR QUE LA FUNCION TENGA AL MENOS UN RETURN SI SE NOS HACE FACIL
        - EN ASSEMBLER PONER SIEMPRE UN RET POR DEFAULT (COMO RET 0) 
        - si hay mas de 1 return es complicado contemplarlo
        - hacer terceto de return?? seria pasar el valor de resultado a AX (entero) o EAX (single) y luego un ret
        
        */
        /* CHEQUEO EL RET DEVUELVA ALGO DEL MISMO TIPO QUE DEVUELVE LA FUNCION*/
        if (AnalizadorLexico.t_simbolos.get_entry(scopeToFunction(actualScope)) != null){
                if (AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)).equals(chkAndGetType(val_peek(1).sval))){
                       /*System.out.println("El tipo de retorno coincide con el tipo de la funcion. ");*/
                       /*System.out.println("tipo de retorno: "+chkAndGetType($3.sval));*/
                       /*$$.sval = Terceto.addTercetoT(":=","@"+scopeToFunction(actualScope),getDeclared($3.sval),AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));*/
                       /* PERO SI ES TERCETO NO FUNCIONAR*/
                       yyval.sval = Terceto.addTercetoT(":=","@"+scopeToFunction(actualScope),val_peek(1).sval,AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                        yyval.sval = Terceto.addTercetoT("RET",getDeclared(val_peek(1).sval),scopeToFunction(actualScope),AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                } else {
                        yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": el tipo de retorno no coincide con el tipo de la funcion. ");
                }
        } else {System.out.println("algo anda mal en return_statement, no encuentra la funcion actual ");
               /*System.out.println(" actual scope: "+actualScope);*/
               /*System.out.println("no se encontro en la TS: "+scopeToFunction(actualScope));        */
        }
        }
break;
case 47:
//#line 248 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 53:
//#line 295 "grammar.y"
{       /*pdoria poner end_if dentro de then_statement y hacer esto ahi.*/
        /*completo terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt((extractNumber(val_peek(1).sval)).substring(1,(val_peek(1).sval).length()-1)+1))+">"); 
}
break;
case 54:
//#line 299 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 55:
//#line 301 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),"<"+String.valueOf(Integer.parseInt(extractNumber(val_peek(0).sval)) + 1)+">",null); 

}
break;
case 56:
//#line 309 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,null);
                Terceto.pushTerceto(yyval.sval); /*apilo terceto incompleto.*/
        }
break;
case 57:
//#line 313 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 58:
//#line 314 "grammar.y"
{
                yyerror("ERROR. Línea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
break;
case 59:
//#line 316 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 60:
//#line 344 "grammar.y"
{
                yyval.sval = val_peek(0).sval;       /*devuelve ultimo terceto*/
        }
break;
case 61:
//#line 348 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 62:
//#line 352 "grammar.y"
{yyval.sval = val_peek(1).sval;}
break;
case 63:
//#line 353 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. "); }
break;
case 64:
//#line 354 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta "); }
break;
case 65:
//#line 355 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 66:
//#line 359 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BI",null,null); /*incompleto, primer operando se completara despues.*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt(extractNumber(yyval.sval)) + 1)+">");/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval);
        }
break;
case 68:
//#line 370 "grammar.y"
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
//#line 395 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 76:
//#line 408 "grammar.y"
{
                yyval.sval = chkAndAssign(val_peek(2).sval,val_peek(0).sval);
                }
break;
case 77:
//#line 412 "grammar.y"
{
                yyval.sval = chkAndAssign(val_peek(2).sval,val_peek(0).sval);
        }
break;
case 78:
//#line 415 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 79:
//#line 421 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
               /*System.out.println("tipo1: "+t_subtype1);*/
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
               /*System.out.println("tipo2: "+t_subtype2);*/
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
//#line 447 "grammar.y"
{            /*la expr o el term pueden ser: variable, funcion, expr_pair,cte,terceto.*/
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
               /*System.out.println("tipo1: "+t_subtype1);*/
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
               /*System.out.println("tipo2: "+t_subtype2);*/
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
//#line 473 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 83:
//#line 477 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
               /*System.out.println("tipo1: "+t_subtype1);*/
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
               /*System.out.println("tipo2: "+t_subtype2);*/
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
//#line 501 "grammar.y"
{            
                String id1 = val_peek(2).sval;
                String id2 = val_peek(0).sval;
                if (!isTerceto(id1) && (!isCte(id1)) ){
                        id1 = getDeclared(id1);}
                if (!isTerceto(id2) && (!isCte(id2)) ){
                        id2 = getDeclared(id2);}
                String t_subtype1 = chkAndGetType(val_peek(2).sval);             
               /*System.out.println("tipo1: "+t_subtype1);*/
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
               /*System.out.println("tipo2: "+t_subtype2);*/
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
//#line 539 "grammar.y"
{    /* si cont de CTE es 1, reemplazo esa entrada de la TS por -CTE*/
                /* si es mas de 1, creo nueva entrada con -CTE*/
        if (!AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
        else {
                /*$$.sval=Terceto.addTercetoT("-","0",$2.sval,AnalizadorLexico.t_simbolos.get_subtype($2.sval));}*/
                if (AnalizadorLexico.t_simbolos.getCont(val_peek(0).sval) == 1){
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(0).sval);
                } 
                AnalizadorLexico.t_simbolos.add_entry("-"+val_peek(0).sval,"CTE","SINGLE");
        }
        yyval.sval = "-"+val_peek(0).sval;
}
break;
case 91:
//#line 561 "grammar.y"
{       
                if (isDeclared(val_peek(3).sval)){
                        /* si ID es de un tipo definido (el tipo de ID esta en la tabla de simbolos)*/
                        String lexem = getDeclared(val_peek(3).sval);
                        String baseType = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                       /*System.out.println("lexem: "+lexem+" baseType: "+baseType);*/
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
case 92:
//#line 581 "grammar.y"
{ 
                System.out.println("\n\nEl lexema a buscar: "+val_peek(3).sval);
                String lexema = getDeclared(val_peek(3).sval);
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
case 93:
//#line 594 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 94:
//#line 598 "grammar.y"
{   /*expr puede  VARIBLE, CTE, funcion, terceto(varaux),*/
                /* si es ID o funcion o exprpair se pasa con scope*/
                /* CHEQUEAR LA EXPR SEA VALIDA, ES DECIR SI ES VARIABLE O FUNCIOn, QUE ESTE DECLARADO*/
                /* y si es pair pasarlo bien*/
               /*System.out.println("scope actual: "+actualScope);*/
                String lexem = val_peek(1).sval;
                String pos = "";
                if (!isTerceto(lexem) && (!isCte(lexem)) && (!isCharch(lexem))){
                        /* es variable o funcion*/
                        if (isPair(lexem)) {
                                pos = lexem.substring(lexem.lastIndexOf("{"),lexem.lastIndexOf("}") + 1);
                               /*System.out.println("pos: "+pos);*/
                                lexem = getDeclared(getPairName(lexem)) + pos;
                        } else {
                                lexem = getDeclared(lexem);
                        }
                }
                yyval.sval = Terceto.addTercetoT("OUTF",lexem,null,chkAndGetType(val_peek(1).sval));
        }
break;
case 95:
//#line 617 "grammar.y"
{
                yyval.sval = Terceto.addTercetoT("OUTF",val_peek(1).sval,null,"CHARCH");
        }
break;
case 96:
//#line 620 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 97:
//#line 621 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 98:
//#line 641 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,val_peek(6).sval);
                /* si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());*/
        }
break;
case 99:
//#line 645 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 100:
//#line 646 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 101:
//#line 647 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 102:
//#line 649 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 103:
//#line 650 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 104:
//#line 651 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 105:
//#line 652 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 106:
//#line 653 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 107:
//#line 654 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 108:
//#line 659 "grammar.y"
{
                /* opcion 1: pila:*/
                /*UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)*/
                /* opcion 2:*/
                yyval.sval = strToTID(Terceto.getTercetoCount());  /*paso id del proximo terceto*/
        }
break;
case 109:
//#line 668 "grammar.y"
{
                String[] idList = val_peek(2).sval.split(",");
                String[] exprList = val_peek(0).sval.split(",");
                if (idList.length != exprList.length){
                        yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. ");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                yyval.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
break;
case 110:
//#line 680 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 111:
//#line 685 "grammar.y"
{
                
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;

        }
break;
case 112:
//#line 690 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
        }
break;
case 115:
//#line 704 "grammar.y"
{
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
        }
break;
case 116:
//#line 708 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
                
        }
break;
case 117:
//#line 716 "grammar.y"
{
                /* buscar si no hay otra tag con el mismo nombre al alcance*/
               /*System.out.println("wtf");*/
                if (!isDeclaredLocal(val_peek(0).sval)) {
                        /* reinserción en la T. de S. con scope actual*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(0).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"TAG","","tag_name","");
                        /* agregar a la tabla de etiquetas*/
                        TablaEtiquetas.add_tag(val_peek(0).sval); 
                        Terceto.addTerceto("LABEL_TAG",val_peek(0).sval+":"+actualScope,null);
                } else yyerror("ERROR: La etiqueta "+val_peek(0).sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                /* AnalizadorLexico.t_simbolos.display();*/
        }
break;
case 118:
//#line 736 "grammar.y"
{
                /*if existe en TS {*/
                        yyval.sval= Terceto.addTerceto("JUMP_TAG",null,null);      /*se pone terceto incompleto, se completara al final del programa*/
                        TablaEtiquetas.add_goto(val_peek(0).sval,Terceto.parseTercetoId(yyval.sval),AnalizadorLexico.line_number);     /* donde puse 0 iría número de línea en lo posible*/
                /*}*/
        }
break;
case 119:
//#line 742 "grammar.y"
{yyerror("ERROR. Línea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1777 "Parser.java"
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

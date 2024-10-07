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
    2,    2,    2,    2,    5,    5,    8,    8,    3,    3,
    3,    3,    3,    3,    3,    3,   16,   16,    6,    6,
    6,    7,    7,    7,    4,    4,    4,    4,   18,   18,
   19,   19,   19,   15,   15,   20,   17,   17,   17,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   23,   22,   22,
   24,   24,   24,   24,   24,   24,   10,   10,   10,   21,
   21,   21,   26,   26,   26,   27,   27,   27,   27,   27,
   27,   27,   25,   28,   28,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   14,   14,
   29,   29,   31,   31,   30,   30,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    2,
    1,    2,    1,    1,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    9,    9,    9,    6,    2,    3,    6,    3,    3,
    2,    1,    2,    4,    2,    1,    1,    1,    1,    7,
    5,    6,    6,    7,    6,    7,    9,    7,    8,    8,
    9,    9,    7,    8,    8,    9,    2,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    3,    3,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    2,    2,    1,
    1,    1,    4,    4,    5,    4,    3,    2,    8,    6,
    7,    7,    7,    5,    6,    6,    5,    6,    3,    3,
    3,    3,    1,    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   47,   48,    0,    0,    0,    8,   11,    0,
   13,   14,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    9,    0,    0,   17,   26,    0,
   87,   92,   67,    0,    0,    0,    0,   91,    0,   85,
   90,    0,   98,    0,   36,    0,    0,    0,    0,  118,
  117,    4,   10,   15,   12,   19,   20,   21,   22,   23,
   24,   25,    0,    0,    0,    0,    0,    0,    0,    2,
    0,    0,    0,    0,   89,   88,    0,   74,   75,   76,
   71,   72,   73,    0,    0,    0,    0,    0,    0,    0,
    5,    1,   97,    0,   37,    0,    0,    0,   27,    0,
    0,   31,    0,   30,    0,    0,    0,   29,    0,    0,
  110,    0,    0,    0,  114,  112,  111,   93,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   83,   84,
   96,   49,    0,   44,    0,    0,   28,    0,    0,   39,
    0,    0,   40,    0,    0,    0,   94,    0,    0,    0,
   51,    0,    0,    0,    0,  107,    0,    0,    0,    0,
    0,    0,    0,    0,   95,    0,   52,    0,    0,   55,
    0,    0,    0,   53,   38,   35,    0,  106,  108,    0,
    0,   43,   41,    0,    0,    0,    0,    0,   56,   58,
    0,   54,    0,   50,   63,    0,  103,    0,  102,    0,
    0,    0,   64,    0,   59,    0,   60,    0,   65,   99,
    0,    0,    0,    0,   62,   61,   66,   57,   33,   34,
   32,
};
final static short yydgoto[] = {                          3,
  221,   18,  109,   20,   65,   21,   22,   39,   23,   24,
   25,   26,   27,   28,   29,  136,  111,   75,  170,  222,
   46,   47,  137,   96,   31,   49,   50,   51,   32,  123,
   33,
};
final static short yysindex[] = {                      -176,
 -249,  -82,    0,  614,  -31, -115,   48,  -27,  614,  -36,
 -240,   98,    0,    0, -230, -186,  662,    0,    0,   66,
    0,    0,   66,   66,   66,   66,   66,   66,   66, -246,
 -204,  -37,  116,  683,    0,  143,  -95,    0,    0,  -28,
    0,    0,    0,  143, -111,   -9,  -39,    0,    8,    0,
    0,  565,    0,  166,    0,  -54,  143,   29,  704,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -18, -107,  -34,  143,  -40,  -81,  -81,    0,
   29,   56,  143,  -38,    0,    0,  143,    0,    0,    0,
    0,    0,    0,  143,  143,  143,  788,  -75,  143,  143,
    0,    0,    0,   58,    0, -188,   59,  -93,    0,  725,
  -53,    0,  143,    0,  -52,  163,  168,    0,  -48,   29,
    0,  125,  170,   87,    0,    0,    0,    0,   -8,  788,
  -22,   29,    8,    8,   29,  788, -143,  788,    0,    0,
    0,    0,  150,    0,  121, -247,    0,  -41,   29,    0,
 -178, -199,    0,  143,  143,  198,    0, -141,  587,  788,
    0, -131, -104,  143,  208,    0,  -16,    0, -100,  209,
  213,  214,   29,   29,    0,  788,    0, -125,  746,    0,
 -181,  -14,  788,    0,    0,    0,  223,    0,    0,  143,
  228,    0,    0,   14,   18,   33,   32,  788,    0,    0,
   34,    0,  641,    0,    0,   37,    0,  267,    0,  614,
  614,  614,    0,   42,    0,   47,    0, -216,    0,    0,
  767,   61,   65,   70,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                       349,
  354,    0,    0,    0,    0,  -44,  259,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  358,    0,    0,  469,
    0,    0,  447,  447,  447,  447,  447,  447,  447,    0,
  315,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,    0,    0,    0,  187,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  215,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  237,
    0,    0,  291,  -30,    0,    0,    0,    0,    0,    0,
    0,   96,   49,   71,  118, -150,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  322,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  344,    0,    0,  -21,    0,    0,
    0,    0,  140,  162,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  369,    0,    0,    0,
  391,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  422,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   95,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  119,   39,  571,    0,  517,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  318,   13,    0,  234,  -32,
  693,  -23,  -25,    0,  720,   77,   97,    0,    0,    0,
  122,
};
final static int YYTABLESIZE=1068;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        113,
   86,   98,  131,   54,   45,  106,   78,   37,  166,  119,
   73,   83,   44,  113,   30,   55,   30,   45,    4,   42,
   84,   30,   82,  190,  118,  115,   74,   35,   45,   30,
  167,   43,  157,   94,   94,   95,   95,   59,   56,  227,
  114,   86,   86,   86,   86,   86,   30,   86,   80,   99,
   91,   93,   92,  228,  100,   63,  171,  168,   76,   86,
   86,   86,   86,   82,   30,   82,   82,   82,  142,   60,
   81,   94,   63,   95,  202,   13,   14,   61,  168,    1,
    2,   82,   82,   82,   82,  203,   13,   14,  204,   80,
   63,   80,   80,   80,   37,   70,   13,   14,  141,  144,
   94,   94,   95,   95,  158,   68,   38,   80,   80,   80,
   80,   81,  162,   81,   81,   81,   68,   69,  143,   68,
   17,  165,   34,  160,   64,  176,  161,   52,  177,   81,
   81,   81,   81,  181,  182,  183,   70,   57,  184,  115,
  187,  198,   45,  191,  199,   85,   86,   36,  116,  117,
  197,  185,  186,  201,   70,  192,  193,  206,   69,   79,
  164,  116,   82,  169,  169,   45,  208,   94,  154,   95,
  133,  134,  214,    5,    6,  124,   69,  218,  223,  224,
  128,    7,    8,  115,  145,    9,   45,   45,   10,   11,
  138,   12,   13,   14,   15,  139,  140,   16,  115,  126,
  127,  105,  151,  148,  150,  116,  103,  152,  153,   37,
   45,  163,   49,  155,   77,  121,   40,   41,   42,   53,
  116,  113,   30,   30,   30,   77,   97,  130,   49,   40,
   41,   42,  113,   30,   49,   49,   78,  112,  175,  189,
   40,   41,   42,  159,  113,   45,   87,  156,  188,  194,
   88,   89,   90,  195,  196,  205,   86,   86,   18,   63,
   86,   86,   86,  207,   86,   86,   86,   86,  209,   86,
   86,   86,   86,   77,   86,   86,   86,   86,   82,   82,
   86,  210,   82,   82,   82,  211,   82,   82,   82,   82,
  109,   82,   82,   82,   82,   78,   82,   82,   82,   82,
  212,  213,   82,  215,   80,   80,  219,  220,   80,   80,
   80,  225,   80,   80,   80,   80,  226,   80,   80,   80,
   80,   79,   80,   80,   80,   80,   81,   81,   80,  229,
   81,   81,   81,  230,   81,   81,   81,   81,  231,   81,
   81,   81,   81,  104,   81,   81,   81,   81,    7,  109,
   81,   70,   70,    3,   40,   41,   42,    6,  114,   70,
   70,   70,   70,   46,   70,   70,   70,   70,  105,   70,
   70,   70,   70,   69,   69,   70,  110,   40,   41,   42,
   79,   69,   69,   69,   69,  172,   69,   69,   69,   69,
  100,   69,   69,   69,   69,  115,  115,   69,    0,   40,
   41,   42,  104,  115,  115,    0,  115,    0,  115,  115,
  115,  115,    0,  115,  115,  115,  115,  116,  116,  115,
    0,  101,   40,   41,   42,  116,  116,  105,  116,    0,
  116,  116,  116,  116,    0,  116,  116,  116,  116,    0,
    0,  116,   45,   45,    0,    0,   16,    0,    0,  100,
   45,   45,    0,   45,    0,   45,   45,   45,   45,    0,
   45,   45,   45,   45,    0,    0,   45,    0,   16,    0,
   77,   77,    0,    0,    0,    0,    0,    0,   77,   77,
  101,   77,    0,   77,   77,   77,   77,    0,   77,   77,
   77,   77,   78,   78,   77,    0,    0,    0,    0,    0,
   78,   78,    0,   78,    0,   78,   78,   78,   78,    0,
   78,   78,   78,   78,   18,   18,   78,    0,    0,    0,
    0,    0,   18,   18,    0,   18,    0,   18,   18,   18,
   18,    0,   18,   18,   18,   18,    0,    0,   18,   66,
   67,   68,   69,   70,   71,   72,  109,  109,    0,    0,
    0,    0,    0,    0,  109,  109,    0,  109,    0,  109,
  109,  109,  109,    0,  109,  109,  109,  109,    0,    0,
  109,    0,   19,    0,   19,    0,    0,   79,   79,   19,
    0,    0,    0,    0,    0,   79,   79,   19,   79,    0,
   79,   79,   79,   79,    0,   79,   79,   79,   79,  104,
  104,   79,    0,    0,   19,    0,    0,  104,  104,    0,
  104,    0,  104,  104,  104,  104,    0,  104,  104,  104,
  104,    0,   19,  104,  105,  105,    0,    0,    0,    0,
    0,    0,  105,  105,    0,  105,    0,  105,  105,  105,
  105,    0,  105,  105,  105,  105,  100,  100,  105,    0,
    0,    0,    0,    0,  100,  100,    0,  100,    0,  100,
  100,  100,  100,    0,  100,  100,  100,  100,    0,    0,
  100,    0,    0,    0,    0,    0,    0,  101,  101,    0,
  147,    0,    0,    0,    0,  101,  101,    0,  101,    0,
  101,  101,  101,  101,    0,  101,  101,  101,  101,    0,
    0,  101,   16,   16,   58,    0,  147,    0,    0,    0,
   16,   16,    0,   16,    0,   16,   16,   16,   16,    0,
   16,   16,   16,   16,   16,   16,   16,   48,   81,    0,
    0,   48,   16,   16,    0,    0,    0,   16,    0,   16,
   16,    0,   16,   16,   16,   16,  104,    0,   16,  107,
    0,    0,    0,    0,    0,   48,    0,    0,    0,    0,
    0,    0,    0,   48,    0,    0,    0,    0,  120,  122,
    0,    0,    0,   48,    0,  129,   48,    0,    0,  132,
   19,   19,   19,    0,    0,    0,    0,    0,  135,    0,
    0,   19,    0,    0,    0,   48,   48,  125,  125,    0,
    0,    0,   48,    0,    0,  149,   48,    0,    0,    0,
    0,    0,    0,   48,   48,   48,    0,    0,   48,   48,
  101,    6,    0,    0,    0,    0,    0,    0,    7,    8,
    0,    0,   48,  102,    0,   10,   11,    0,   12,   13,
   14,   15,  178,    6,   16,    0,  173,  174,    0,    0,
    7,    8,    0,  179,    0,    0,  180,   10,    0,    0,
   12,   13,   14,   15,   48,    0,   16,    0,    0,    5,
    6,    0,    0,   48,   48,    0,    0,    7,    8,    0,
    0,    0,    0,   48,   10,   11,   48,   12,   13,   14,
   15,    0,    0,   16,    0,    0,  216,    6,    0,    0,
    0,    0,    0,    0,    7,    8,    0,    0,    0,   48,
  217,   10,    0,    0,   12,   13,   14,   15,    6,    0,
   16,    0,    0,    0,    0,    7,    8,    0,    0,    0,
   62,    0,   10,   11,    0,   12,   13,   14,   15,    6,
    0,   16,    0,    0,    0,    0,    7,    8,    0,    0,
    0,   80,    0,   10,   11,    0,   12,   13,   14,   15,
    6,    0,   16,    0,    0,    0,    0,    7,    8,    0,
    0,    0,  108,    0,   10,    0,    0,   12,   13,   14,
   15,    6,    0,   16,    0,    0,    0,    0,    7,    8,
    0,    0,    0,  146,    0,   10,    0,    0,   12,   13,
   14,   15,    6,    0,   16,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,  200,   10,    0,    0,   12,
   13,   14,   15,    6,    0,   16,    0,    0,    0,    0,
    7,    8,    0,    0,    0,    0,    0,   10,   11,    0,
   12,   13,   14,   15,    6,    0,   16,    0,    0,    0,
    0,    7,    8,    0,    0,    0,    0,    0,   10,    0,
    0,   12,   13,   14,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   41,   41,   40,   45,   60,   44,  123,  256,   44,
  257,   40,   40,   44,    2,  256,    4,   45,  268,   41,
   44,    9,    0,   40,   59,   44,  273,   59,   45,   17,
  278,   59,   41,   43,   43,   45,   45,  268,  279,  256,
   59,   41,   42,   43,   44,   45,   34,   47,    0,   42,
   60,   61,   62,  270,   47,   17,  256,  257,  263,   59,
   60,   61,   62,   41,   52,   43,   44,   45,  257,  256,
    0,   43,   34,   45,  256,  275,  276,  264,  257,  256,
  257,   59,   60,   61,   62,  267,  275,  276,  270,   41,
   52,   43,   44,   45,  123,    0,  275,  276,   41,   41,
   43,   43,   45,   45,  130,  256,   59,   59,   60,   61,
   62,   41,  138,   43,   44,   45,  267,    0,  106,  270,
    2,  145,    4,  267,   59,  267,  270,    9,  270,   59,
   60,   61,   62,  159,  160,  267,   41,   40,  270,    0,
  164,  267,   45,  167,  270,  257,  258,  263,  256,  257,
  176,  256,  257,  179,   59,  256,  257,  183,   41,   44,
   40,    0,  258,  151,  152,   45,  190,   43,   44,   45,
   94,   95,  198,  256,  257,  257,   59,  203,  211,  212,
  125,  264,  265,   44,  278,  268,    0,   45,  271,  272,
  266,  274,  275,  276,  277,   99,  100,  280,   59,   78,
   79,  256,   40,  257,  257,   44,   41,   40,  257,  123,
   45,   62,  257,   44,    0,  256,  257,  258,  259,  256,
   59,  263,  210,  211,  212,  263,  266,  266,  273,  257,
  258,  259,  263,  221,  256,  257,    0,  256,   41,  256,
  257,  258,  259,  266,  263,   59,  256,  256,   41,   41,
  260,  261,  262,   41,   41,  270,  256,  257,    0,  221,
  260,  261,  262,   41,  264,  265,  266,  267,   41,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,  268,  260,  261,  262,  268,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  268,  270,  280,  270,  256,  257,  270,   41,  260,  261,
  262,  270,  264,  265,  266,  267,  270,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,  269,
  260,  261,  262,  269,  264,  265,  266,  267,  269,  269,
  270,  271,  272,    0,  274,  275,  276,  277,    0,   59,
  280,  256,  257,    0,  257,  258,  259,    0,   44,  264,
  265,  266,  267,  269,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,   59,  257,  258,  259,
   59,  264,  265,  266,  267,  152,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,   -1,  257,
  258,  259,   59,  264,  265,   -1,  267,   -1,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,    0,  257,  258,  259,  264,  265,   59,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,   -1,
   -1,  280,  256,  257,   -1,   -1,    0,   -1,   -1,   59,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,   -1,    0,   -1,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   59,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   23,
   24,   25,   26,   27,   28,   29,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,    2,   -1,    4,   -1,   -1,  256,  257,    9,
   -1,   -1,   -1,   -1,   -1,  264,  265,   17,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   34,   -1,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,   52,  280,  256,  257,   -1,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,  256,  257,  280,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
  110,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,   -1,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,   -1,
   -1,  280,  256,  257,   12,   -1,  136,   -1,   -1,   -1,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,    8,   36,   -1,
   -1,   12,  264,  265,   -1,   -1,   -1,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   54,   -1,  280,   57,
   -1,   -1,   -1,   -1,   -1,   36,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   44,   -1,   -1,   -1,   -1,   76,   77,
   -1,   -1,   -1,   54,   -1,   83,   57,   -1,   -1,   87,
  210,  211,  212,   -1,   -1,   -1,   -1,   -1,   96,   -1,
   -1,  221,   -1,   -1,   -1,   76,   77,   78,   79,   -1,
   -1,   -1,   83,   -1,   -1,  113,   87,   -1,   -1,   -1,
   -1,   -1,   -1,   94,   95,   96,   -1,   -1,   99,  100,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,  113,  269,   -1,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,   -1,  154,  155,   -1,   -1,
  264,  265,   -1,  267,   -1,   -1,  270,  271,   -1,   -1,
  274,  275,  276,  277,  145,   -1,  280,   -1,   -1,  256,
  257,   -1,   -1,  154,  155,   -1,   -1,  264,  265,   -1,
   -1,   -1,   -1,  164,  271,  272,  167,  274,  275,  276,
  277,   -1,   -1,  280,   -1,   -1,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,  190,
  270,  271,   -1,   -1,  274,  275,  276,  277,  257,   -1,
  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,  272,   -1,  274,  275,  276,  277,  257,
   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,
  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,  274,  275,
  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,  270,  271,   -1,   -1,  274,
  275,  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,
  264,  265,   -1,   -1,   -1,   -1,   -1,  271,  272,   -1,
  274,  275,  276,  277,  257,   -1,  280,   -1,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,
   -1,  274,  275,  276,  277,   -1,   -1,  280,
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
"statement_list : error ';'",
"statement_list : statement_list statement",
"statement : executable_statement",
"statement : declare_pair optional_semicolon",
"statement : declare_var",
"statement : declare_fun",
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
"if_statement : IF ';'",
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
"repeat_statement : REPEAT BEGIN executable_statement_list END error",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL error",
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
//#line 626 "Parser.java"
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
case 9:
//#line 33 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de sentencia.") ;}
break;
case 12:
//#line 40 "grammar.y"
{System.out.println("Sentencia de declaracion de tipo");}
break;
case 13:
//#line 41 "grammar.y"
{System.out.println("Sentencia de declaracion de variable/s");}
break;
case 14:
//#line 42 "grammar.y"
{System.out.println("Sentencia de declaracion de funcion");}
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
{System.out.println("Sentencia de control IF");}
break;
case 20:
//#line 68 "grammar.y"
{System.out.println("Sentencia de asignacion");}
break;
case 21:
//#line 69 "grammar.y"
{System.out.println("Sentencia de impresion por pantalla");}
break;
case 22:
//#line 71 "grammar.y"
{System.out.println("Sentencia de repeat until");}
break;
case 23:
//#line 72 "grammar.y"
{System.out.println("Sentencia de salto goto");}
break;
case 24:
//#line 74 "grammar.y"
{System.out.println("Sentencia de asignacion multiple");}
break;
case 25:
//#line 76 "grammar.y"
{System.out.println("Sentencia de retorno de funcion");}
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
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '<' .") ; }
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
//#line 145 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 52:
//#line 146 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 53:
//#line 147 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 54:
//#line 148 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 55:
//#line 149 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
break;
case 56:
//#line 150 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 58:
//#line 154 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
break;
case 59:
//#line 155 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }
break;
case 60:
//#line 156 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 61:
//#line 157 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
break;
case 62:
//#line 158 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; }
break;
case 63:
//#line 159 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este dentro de parentesis. ") ; }
break;
case 64:
//#line 160 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 65:
//#line 161 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 66:
//#line 164 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 67:
//#line 165 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia IF incorrecta");}
break;
case 70:
//#line 191 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 79:
//#line 207 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 95:
//#line 254 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 97:
//#line 259 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 98:
//#line 260 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 100:
//#line 268 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 101:
//#line 269 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 102:
//#line 270 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 103:
//#line 276 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 104:
//#line 277 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 105:
//#line 278 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 106:
//#line 279 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 107:
//#line 280 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 108:
//#line 281 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL");}
break;
case 110:
//#line 285 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 118:
//#line 314 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1021 "Parser.java"
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

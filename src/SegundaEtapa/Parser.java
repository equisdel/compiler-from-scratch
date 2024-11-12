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
public final static short PAIR=279;
public final static short GOTO=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    0,    1,    1,    2,
    2,    2,    2,    2,    5,    5,    8,    8,    3,    3,
    3,    3,    3,    3,    3,    3,   17,   17,    6,    6,
    6,    6,   19,   19,    7,   20,   20,   20,    4,    4,
    4,    4,   22,   22,   22,   15,   15,   21,   18,   18,
   18,    9,    9,    9,   24,   24,   24,   24,   25,   25,
   26,   26,   26,   26,   29,   28,   27,   27,   30,   30,
   30,   30,   30,   30,   10,   10,   10,   23,   23,   23,
   23,   32,   32,   32,   33,   33,   33,   33,   33,   33,
   33,   31,   34,   34,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   35,   14,   14,
   36,   36,   38,   38,   37,   37,   16,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    3,    3,    3,    7,    7,    7,    6,    5,
    4,    6,    2,    1,    2,    4,    2,    1,    1,    1,
    1,    3,    3,    3,    4,    2,    3,    3,    2,    2,
    3,    2,    3,    3,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    4,    3,    3,    1,
    1,    3,    3,    1,    1,    1,    2,    2,    1,    1,
    1,    4,    4,    5,    4,    3,    2,    7,    5,    6,
    6,    6,    4,    5,    5,    6,    6,    2,    3,    3,
    3,    3,    1,    1,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  117,    0,    0,    0,
    0,    0,   49,   50,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
    0,    0,    0,   86,   91,    0,    0,    0,    0,   90,
    0,   84,   89,    0,   97,    0,    0,    0,   81,    0,
    0,  108,  119,  118,    4,    9,   15,   11,   19,   20,
   21,   22,   23,   24,   25,   17,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,   88,   87,   72,
   73,   74,   69,   70,   71,    0,    0,    0,   58,    0,
    0,    0,    1,   96,    0,   51,    0,    0,    0,    0,
   32,    0,   31,    0,    0,    0,   30,   29,    0,   35,
   60,    0,   59,   53,   65,   52,   54,    0,    0,    0,
    0,   28,    0,    0,    0,    0,    0,  114,  112,  111,
   92,    0,   55,    0,    0,    0,   82,   83,   95,    0,
   41,    0,   46,    0,   33,    0,    0,   34,    0,   62,
    0,    0,    0,    0,    0,    0,    0,    0,   93,    0,
   40,    0,    0,    0,    0,    0,   63,   64,   61,    0,
  105,    0,    0,    0,    0,    0,   94,   42,   39,   45,
   43,    0,    0,    0,  102,  107,    0,  101,  106,   37,
   38,   36,   98,
};
final static short yydgoto[] = {                          3,
   17,   18,   19,   20,   68,   21,   22,   77,   23,   24,
   25,   26,   27,   28,   29,   30,  132,   31,   80,   32,
   82,  184,   48,   33,   84,  137,   49,  133,  138,  108,
   50,   51,   52,   53,   35,   36,  146,   37,
};
final static short yysindex[] = {                      -156,
 -259,  -82,    0,  652,  -31, -118,    0,  -38,  652,  -34,
  -56,  -23,    0,    0, -250, -186,  548,    0,    0,   -6,
    0,    0,   -6,   -6,   -6,   -6,   -6,   -6,   -6,   10,
 -238,  652, -194, -182,  717,  -33,   69,  603,    0,  121,
 -161,    0,  -28,    0,    0,  167, -151,  -35,   76,    0,
   56,    0,    0,  630,    0,   -4,  -47, -236,    0,  121,
   44,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -12, -128,   61,
  652, -150,  696, -191,  121, -143,    0,  738, -111,  241,
 -107, -107,    0,   44,   39,  121,  139,    0,    0,    0,
    0,    0,    0,    0,    0,  262,  262,  121,    0,  262,
  262,  -31,    0,    0,   93,    0, -236,  -72,  129,  106,
    0,  121,    0,  -60,  160,  161,    0,    0,  -55,    0,
    0,  759,    0,    0,    0,    0,    0,  674,   44,  -16,
  -40,    0,  -59,    0,  124,  159,   82,    0,    0,    0,
    0,   13,    0,   56,   56,   44,    0,    0,    0,  144,
    0,  -50,    0,   44,    0, -202, -242,    0,  -62,    0,
 -240,  167,  170,   -9,  167,  121,  121,  172,    0, -114,
    0,    0, -104,  173,  176,  204,    0,    0,    0,  219,
    0,  143,  223,  263,   44,   44,    0,    0,    0,    0,
    0,   14,   40,   62,    0,    0,  271,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                       256,
  334,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  339,    0,    0,  496,
    0,    0,  259,  259,  259,  259,  259,  259,  259,  468,
    0,    0,    0,  305,    0,    0,    0,    0,    0,    0,
    0,   96,    1,    0,    0,    0,    0,    0,   88,    0,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  215,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  237,    0,    0,  120,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  359,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -179,    0,    0,    0,    0,    0,    0,  291,    0,
    0,    0,    0,  118,    0,  322,  -24,    0,    0,    0,
    0,    0,    0,   49,   71,  140,    0,    0,    0,    0,
    0,    0,    0,  344,    0,    0,    0,    0,    0,    0,
    0,    0,  369,    0,    0,    0,    0,    0,    0,    0,
    0,   45,    0,    0,    0,    0,    0,    0,    0,  391,
    0,    0,  418,    0,  165,  187,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  446,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
   48,   21,   16,    0,   98,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  329,  723,    0,    0,
    0,  217,  694,    0,    0,    0,    4,  270,    0,    0,
  760,   53,   60,    0,    0,    0,    0,   81,
};
final static int YYTABLESIZE=1039;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        175,
   85,   46,  113,   58,   41,   56,   47,  106,    4,  107,
   91,   96,  117,  185,  182,  188,   60,   62,   78,  113,
  116,   47,   80,  172,  103,  105,  104,   39,   47,  189,
  192,  124,   13,   14,   79,   47,  114,   66,   13,   14,
   47,   85,   85,   85,   85,   85,  123,   85,   78,   97,
   87,   38,   67,  179,  182,  106,   54,  107,   66,   85,
   85,   85,   85,   80,  134,   80,   80,   80,   76,   63,
   79,   83,   13,   14,   66,  135,   66,   64,  136,   81,
   85,   80,   80,   80,   80,   44,  106,   66,  107,   78,
   66,   78,   78,   78,   41,   68,   95,  110,   87,    1,
    2,   66,  111,  142,  129,   98,   99,   78,   78,   78,
   78,   79,   92,   79,   79,   79,  109,  110,  130,  128,
   69,   70,   71,   72,   73,   74,   75,  125,  126,   79,
   79,   79,   79,  159,  140,  106,   68,  107,   81,   67,
   81,  198,  199,  173,   40,  143,  163,  142,  106,  147,
  107,  200,  201,   87,   68,   81,   81,   81,  154,  155,
   81,   81,   81,  151,  115,   47,  106,  176,  107,  157,
  158,  149,  150,    5,    6,  190,  110,  193,  194,  153,
   67,    7,    8,  206,  161,    9,  116,   47,   10,   11,
  162,   12,   13,   14,   15,  207,  165,   16,   67,  166,
  167,  168,  177,  122,   41,  180,  181,  187,  115,  116,
  191,   47,  197,  202,   47,   51,  203,   42,   43,   44,
   45,   55,   57,  115,  100,  101,  102,   13,   14,   90,
  116,   51,   59,   43,   44,   45,   75,  174,  113,   42,
   43,   44,   45,  121,  204,  116,   42,   43,   44,   45,
  122,   59,   43,   44,   45,    7,   85,   85,   16,  205,
   85,   85,   85,  208,   85,   85,   85,   85,  178,   85,
   85,   85,   85,   47,   85,   85,   85,   85,   80,   80,
   85,  210,   80,   80,   80,   47,   80,   80,   80,   80,
   76,   80,   80,   80,   80,   75,   80,   80,   80,   80,
   51,   51,   80,  209,   78,   78,   47,  211,   78,   78,
   78,  213,   78,   78,   78,   78,  127,   78,   78,   78,
   78,  109,   78,   78,   78,   78,   79,   79,   78,  212,
   79,   79,   79,    3,   79,   79,   79,   79,    6,   79,
   79,   79,   79,   77,   79,   79,   79,   79,  114,   76,
   79,   68,   68,   56,   48,   81,   81,   81,    5,   68,
   68,   68,   68,   88,   68,   68,   68,   68,  103,   68,
   68,   68,   68,  110,  110,   68,   59,   43,   44,   45,
  109,  110,  110,  186,  110,   57,  110,  110,  110,  110,
  104,  110,  110,  110,  110,   67,   67,  110,   42,   43,
   44,   45,   77,   67,   67,   67,   67,  171,   67,   67,
   67,   67,    0,   67,   67,   67,   67,   99,    0,   67,
  115,  115,   42,   43,   44,   45,    0,  103,  115,  115,
    0,  115,    0,  115,  115,  115,  115,    0,  115,  115,
  115,  115,  116,  116,  115,  100,    0,    0,    0,  104,
  116,  116,    0,  116,    0,  116,  116,  116,  116,    0,
  116,  116,  116,  116,    0,    0,  116,   18,    0,    0,
   47,   47,    0,    0,    0,    0,   99,    0,   47,   47,
    0,   47,    0,   47,   47,   47,   47,    0,   47,   47,
   47,   47,   75,   75,   47,   16,  144,   43,   44,   45,
   75,   75,    0,   75,  100,   75,   75,   75,   75,    0,
   75,   75,   75,   75,   16,   16,   75,    0,   43,   44,
   45,    0,   16,   16,    0,   16,    0,   16,   16,   16,
   16,    0,   16,   16,   16,   16,    0,    0,   16,    0,
    0,    0,    0,    0,    0,    0,   76,   76,    0,    0,
    0,    0,    0,    0,   76,   76,    0,   76,    0,   76,
   76,   76,   76,    0,   76,   76,   76,   76,    0,    0,
   76,    0,    0,    0,    0,    0,    0,  109,  109,    0,
    0,    0,    0,    0,    0,  109,  109,    0,  109,    0,
  109,  109,  109,  109,    0,  109,  109,  109,  109,   77,
   77,  109,    0,    0,    0,    0,    0,   77,   77,    0,
   77,    0,   77,   77,   77,   77,    0,   77,   77,   77,
   77,    0,    0,   77,  103,  103,    0,    0,    0,    0,
    0,    0,  103,  103,    0,  103,    0,  103,  103,  103,
  103,    0,  103,  103,  103,  103,  104,  104,  103,    0,
    0,    0,    0,    0,  104,  104,    0,  104,    0,  104,
  104,  104,  104,    0,  104,  104,  104,  104,    0,    0,
  104,    0,    0,   99,   99,    0,    0,    0,    0,    0,
    0,   99,   99,    0,   99,    0,   99,   99,   99,   99,
    0,   99,   99,   99,   99,    0,    0,   99,    0,    0,
    0,  100,  100,    0,    0,   61,    0,    0,    0,  100,
  100,    0,  100,    0,  100,  100,  100,  100,    0,  100,
  100,  100,  100,   18,   18,  100,    0,    0,    0,    0,
    0,   18,   18,   94,   18,    0,   18,   18,   18,   18,
    0,   18,   18,   18,   18,    0,    0,   18,    0,  115,
    0,   16,   16,  120,    0,    0,    0,   89,    0,   16,
   16,   34,    0,   34,   16,    0,   16,   16,   34,   16,
   16,   16,   16,    0,    0,   16,   34,    0,  139,  118,
  119,    0,    0,  145,    0,    0,    0,    0,    0,  152,
    0,   34,    0,    0,   34,    0,    0,   34,    0,    0,
    0,  156,    0,    5,    6,   89,    0,    0,    0,    0,
   89,    7,    8,   34,    0,  164,   65,    0,   10,   11,
    0,   12,   13,   14,   15,    0,    0,   16,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  160,
   34,    0,   34,    0,    0,    0,    0,   34,    0,    0,
  148,  148,    0,    0,   89,    0,    0,    0,    5,    6,
   89,    0,    0,    0,    0,    0,    7,    8,    0,  195,
  196,   93,    0,   10,   11,    0,   12,   13,   14,   15,
    0,    0,   16,    0,    0,  112,    6,    0,  183,  183,
    0,   34,    0,    7,    8,    0,    0,   34,  113,    0,
   10,   11,    0,   12,   13,   14,   15,    5,    6,   16,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
    0,    0,   10,   11,    0,   12,   13,   14,   15,  169,
    6,   16,    0,    0,    0,    0,    0,    7,    8,    0,
    0,    0,    0,  170,   10,    0,    0,   12,   13,   14,
   15,  131,    6,   16,    0,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,    0,   10,    0,    0,   12,
   13,   14,   15,    6,    0,   16,    0,    0,    0,    0,
    7,    8,    0,    0,    0,   86,    0,   10,    0,    0,
   12,   13,   14,   15,    6,    0,   16,    0,    0,    0,
    0,    7,    8,    0,    0,    0,  141,    0,   10,    0,
    0,   12,   13,   14,   15,    6,    0,   16,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,    0,   10,
    0,    0,   12,   13,   14,   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,   60,  123,   40,   45,   43,  268,   45,
   44,   40,   60,  256,  257,  256,   40,  268,  257,   44,
  257,   45,    0,   40,   60,   61,   62,   59,   45,  270,
   40,   44,  275,  276,  273,   45,   41,   17,  275,  276,
   45,   41,   42,   43,   44,   45,   59,   47,    0,   46,
   35,    4,   59,   41,  257,   43,    9,   45,   38,   59,
   60,   61,   62,   41,  256,   43,   44,   45,   59,  256,
    0,  266,  275,  276,   54,  267,  256,  264,  270,   32,
  263,   59,   60,   61,   62,   41,   43,  267,   45,   41,
  270,   43,   44,   45,  123,    0,  258,   42,   83,  256,
  257,   81,   47,   88,   44,  257,  258,   59,   60,   61,
   62,   41,   44,   43,   44,   45,   41,    0,  269,   59,
   23,   24,   25,   26,   27,   28,   29,  256,  257,   59,
   60,   61,   62,   41,  278,   43,   41,   45,   43,    0,
   45,  256,  257,  140,  263,  257,   41,  132,   43,  257,
   45,  256,  257,  138,   59,   60,   61,   62,  106,  107,
   43,   44,   45,  125,    0,   45,   43,   44,   45,  110,
  111,   91,   92,  256,  257,  172,   59,  174,  175,   41,
   41,  264,  265,   41,  257,  268,    0,   45,  271,  272,
   62,  274,  275,  276,  277,  192,  257,  280,   59,   40,
   40,  257,   44,  263,  123,   62,  257,  270,   44,  257,
   41,   45,   41,   41,    0,  257,   41,  256,  257,  258,
  259,  256,  279,   59,  260,  261,  262,  275,  276,  263,
   44,  273,  256,  257,  258,  259,    0,  278,  263,  256,
  257,  258,  259,  256,   41,   59,  256,  257,  258,  259,
  263,  256,  257,  258,  259,    0,  256,  257,    0,   41,
  260,  261,  262,   41,  264,  265,  266,  267,  256,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,  268,  260,  261,  262,   45,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  256,  257,  280,   41,  256,  257,   45,  268,  260,  261,
  262,   41,  264,  265,  266,  267,  256,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,  268,
  260,  261,  262,    0,  264,  265,  266,  267,    0,  269,
  270,  271,  272,    0,  274,  275,  276,  277,   44,   59,
  280,  256,  257,  266,  269,  260,  261,  262,    0,  264,
  265,  266,  267,   35,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,  167,  267,  266,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,  138,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,    0,   -1,  280,
  256,  257,  256,  257,  258,  259,   -1,   59,  264,  265,
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
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
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
   -1,  256,  257,   -1,   -1,   12,   -1,   -1,   -1,  264,
  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   40,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,   56,
   -1,  256,  257,   60,   -1,   -1,   -1,   35,   -1,  264,
  265,    2,   -1,    4,  269,   -1,  271,  272,    9,  274,
  275,  276,  277,   -1,   -1,  280,   17,   -1,   85,   57,
   58,   -1,   -1,   90,   -1,   -1,   -1,   -1,   -1,   96,
   -1,   32,   -1,   -1,   35,   -1,   -1,   38,   -1,   -1,
   -1,  108,   -1,  256,  257,   83,   -1,   -1,   -1,   -1,
   88,  264,  265,   54,   -1,  122,  269,   -1,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  117,
   81,   -1,   83,   -1,   -1,   -1,   -1,   88,   -1,   -1,
   91,   92,   -1,   -1,  132,   -1,   -1,   -1,  256,  257,
  138,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  176,
  177,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
   -1,   -1,  280,   -1,   -1,  256,  257,   -1,  166,  167,
   -1,  132,   -1,  264,  265,   -1,   -1,  138,  269,   -1,
  271,  272,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
   -1,   -1,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,   -1,  270,  271,   -1,   -1,  274,  275,  276,
  277,  256,  257,  280,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,  274,
  275,  276,  277,  257,   -1,  280,   -1,   -1,   -1,   -1,
  264,  265,   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,
  274,  275,  276,  277,  257,   -1,  280,   -1,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,   -1,
   -1,  274,  275,  276,  277,  257,   -1,  280,   -1,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,  271,
   -1,   -1,  274,  275,  276,  277,   -1,   -1,  280,
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

//#line 621 "grammar.y"
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

        public Boolean isTerceto(String id){
                return (id.charAt(0) == '<' && id.charAt(id.length()-1) == '>');
        }

        public String chkAndGetType(String valStr){  
                if (isTerceto(valStr)) {
                        return Terceto.getSubtipo(valStr);
                        }
                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        //cte: lo unico que no tiene scope
                        if (isCte(valStr)) {return AnalizadorLexico.t_simbolos.get_subtype(valStr);}
                        else {  // variable, invoc. a funcion o expr_pair
                                // entre variable e invoc a funcion distingo por el uso, pero ambas tienen el tipo (o tipo de retorno q es lo q me interesa) en el mismo campo.
                                
                                // si es variable o funcion:
                                String lexem = getDeclared(valStr);
                                if (lexem != null){
                                        if (!(AnalizadorLexico.t_simbolos.get_subtype(valStr).equals("SINGLE") || AnalizadorLexico.t_simbolos.get_subtype(valStr).equals("UINTEGER"))) { // es porq es uno definido x el usuario (podria ser hexa?)
                                                //pair no seria, xq si es pair, valStr es un TIPO definido por usuario, y no una variable, y eso se chequea antes de llegar a esta linea.
                                
                                                return TablaPair.getTipo(valStr);               
                                        }
                                        // si es tipo primitivo
                                        return AnalizadorLexico.t_simbolos.get_subtype(valStr);
                                } else yyerror("Error en linea "+AnalizadorLexico.line_number+": identificador "+valStr+" no declarado. ");
                                return "";      //o null?
                        }
                }
        }
        
       /*  public void set_var_scope() {

        }*/

        public Boolean isCte(String valStr){
                return (AnalizadorLexico.t_simbolos.get_entry(valStr) != null); //se pasa sin scope. Si está, es cte (el resto deberia tener como minimo scope :main)
        }

        public void pushScope(String scope){
                scope = scope + ":" + scope;
                TablaEtiquetas.pushScope();
        }

        public void popScope(){
                popScope(actualScope);
        }

        public void popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                yyerror("salgo de scope: "+scope);
                int index = scope.lastIndexOf(":");
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                TablaEtiquetas.popScope();
        }

        public boolean isDeclared(String id){
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
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

        /*
        public Boolean chkVar(String tipo, String id){  // se usa??????????????????????
                if (!AnalizadorSemantico.validID(tipo,id)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                else if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) == null) {
                        yyerror("WARNING: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual ");}
                        // WARNING O ERROR? PODRIA SER WARNING AL NO TENER ENCUENTA ESTA REDECLARACION. ACLARAR SI SE DESCARTA LA REDECLARACION.
                else { AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                        AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                        return true;
                        
                }
        } */

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public void chkAndDeclareVar(String tipo, String id){
                
                if (!AnalizadorSemantico.validID(tipo,id)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                //chequear tipo sea valido
                if (tipo.equals("UINTEGER") || tipo.equals("SINGLE") ||(AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME"))) {
                         
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                        yyerror("Error: La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                                else {
                                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo:
                                AnalizadorLexico.t_simbolos.del_entry(id);
                                //AnalizadorLexico.t_simbolos.display();
                                AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);
                                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                                }   
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipo de variable no valido. "); }
        }

        public Boolean isCompatible(String t_subtype1,String t_subtype2){  //medio al dope creo, al menos duvuelva el tipo resultante en caso de ser ocmpatible, y null si no.
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2);
        }

        public void chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida
                yyerror("id: "+id);
               //AnalizadorLexico.t_simbolos.display();
                if (!isDeclared(id))
                {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+id+" no declarada. "); }
                else {
                        //  CTE NO VAN CON SCOPE!
                        // EXPR PUEDE SER :CTE, ID, EXPRPAIR, FUN_INVOC
                        String lexem;
                        if (!isCte(expr)){      // si es variable o funcion, o expr_pair
                                lexem = getDeclared(expr);
                        } else {
                                lexem = expr;}

                        String subtypeT = chkAndGetType(lexem);
                        String subtypeID = chkAndGetType(id);
                        if (subtypeT.equals(subtypeID)){
                                Terceto.addTerceto(":=",id,expr);
                        }
                        else if (subtypeID.equals("SINGLE") && subtypeT.equals("UINTEGER")){
                                Terceto.addTercetoT("utos",expr,null,"SINGLE");
                                Terceto.addTerceto(":=",id,expr);
                        }
                        else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en asignacion. "); }

                }
        }

        public String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR SI id ES ID O FUNCION
                String scopeaux = actualScope;
                //AnalizadorLexico.t_simbolos.display();

                if (isDeclaredLocal(id)) {return id+":"+scopeaux;}
                else {
                        while (scopeaux.lastIndexOf(":") != -1){

                                yyerror("aaaaaaa");
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null) {
                                        return id+":"+scopeaux;
                                }
                                popScope(scopeaux);     // lo hace con actualScope
                        }
                        return null;    //si no esta declarada..
                }

        }
//#line 778 "Parser.java"
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
{yyerror("Sentencia de TAG");
                /*chequear no exista otro tag igual en todo el programa*/
        }
break;
case 29:
//#line 85 "grammar.y"
{
                String[] idList = val_peek(1).sval.split(",");
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar(val_peek(2).sval, idList[i]);
                }

}
break;
case 30:
//#line 92 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                /* agregar a cada variable (separar por ',') su tipo,scope,uso*/
                                }
break;
case 31:
//#line 95 "grammar.y"
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
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
        }
break;
case 34:
//#line 108 "grammar.y"
{
                yyval.sval = yyval.sval + "," + val_peek(0).sval;
        }
break;
case 35:
//#line 122 "grammar.y"
{ 
                /* Actualización del scope: fin de la función fuerza retorno al ámbito del padre*/
                        popScope();
                }
break;
case 36:
//#line 135 "grammar.y"
{

                /* Control de ID: debe ser único en el scope actual*/
                        if (isDeclaredLocal(val_peek(4).sval))       yyerror("No se permite la redeclaración de variables: el nombre seleccionado no está disponible en el scope actual.");
                        else { }/* ¿La compilación debería seguir? ¿Cómo? */

                /* Control de ID: se verifica la primera letra del nombre (algunas iniciales son reservadas)*/
                        if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))      yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                        else {} /* ¿La compilación debería seguir? ¿Cómo? */

                /* Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(4).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(4).sval+":"+actualScope,"ID",val_peek(2).sval,"fun_name",val_peek(6).sval);
                        AnalizadorLexico.t_simbolos.set_use(val_peek(4).sval+":"+actualScope,"VARIABLE_NAME");

                /* Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función*/
                        pushScope(val_peek(4).sval); 
                        
                /* Actualización del ID del parámetro: se actualiza el scope al actual*/
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(2).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(2).sval+":"+actualScope,"ID",val_peek(2).sval,"fun_name",val_peek(6).sval);
                        AnalizadorLexico.t_simbolos.set_use(val_peek(2).sval+":"+actualScope,"VARIABLE_NAME");

                /* Posible generación de terceto de tipo LABEL*/
                        yyval.sval = Terceto.addTerceto("LABEL",ID+":"+actualScope,null);

                }
break;
case 37:
//#line 163 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;
                }
break;
case 38:
//#line 167 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval))
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");
                }
break;
case 39:
//#line 177 "grammar.y"
{
                /* se le pone scope 'MAIN' */
                if (!AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                
                if (val_peek(2).sval == "UINTEGER" || val_peek(2).sval == "SINGLE" ||(AnalizadorLexico.t_simbolos.get_entry(val_peek(2).sval+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(val_peek(2).sval+":MAIN").getUse() == "TYPE_NAME")) {

                        if (AnalizadorLexico.t_simbolos.get_entry(val_peek(0).sval+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(val_peek(0).sval+":MAIN").getUse() == "TYPE_NAME") {
                                yyerror("Error: ID de tipo ya utilizado, linea "+AnalizadorLexico.line_number);
                        } else {
                                AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":MAIN","ID","pair","TYPE_NAME");
                                TablaPair.addPair(val_peek(0).sval,val_peek(2).sval);
                        }
                }


        }
break;
case 40:
//#line 193 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 41:
//#line 194 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 42:
//#line 195 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 43:
//#line 205 "grammar.y"
{yyval.sval = val_peek(1).sval+"-"+val_peek(0).sval;}
break;
case 44:
//#line 206 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 207 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 47:
//#line 212 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 51:
//#line 228 "grammar.y"
{   /* se chequeara donde se use.*/
                }
break;
case 52:
//#line 259 "grammar.y"
{       /*pdoria poner end_if dentro de then_statement y hacer esto ahi.*/
        /*completo terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(), null,String.valueOf(Integer.parseInt(val_peek(1).sval) + 1)); 
}
break;
case 53:
//#line 263 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 54:
//#line 265 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),String.valueOf(Integer.parseInt(val_peek(0).sval) + 1),null); 

}
break;
case 55:
//#line 273 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,null);
                Terceto.pushTerceto(yyval.sval); /*apilo terceto incompleto.*/
        }
break;
case 56:
//#line 277 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 57:
//#line 278 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
break;
case 58:
//#line 280 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 59:
//#line 308 "grammar.y"
{
                yyval.sval = val_peek(0).sval;       /*devuelve ultimo terceto*/
        }
break;
case 60:
//#line 312 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 61:
//#line 316 "grammar.y"
{yyval.sval = val_peek(1).sval;}
break;
case 62:
//#line 317 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. "); }
break;
case 63:
//#line 318 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta "); }
break;
case 64:
//#line 319 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 65:
//#line 323 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BI",null,null); /*incompleto, primer operando se completara despues.*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,String.valueOf(Integer.parseInt(yyval.sval) + 1));/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval);
        }
break;
case 67:
//#line 334 "grammar.y"
{
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                /* compatiblidades de comparacion?? igual que en operaciones*/
                /* chequear y convertir si es necesario (se hace en varios lugares, pensar en modularizar)*/
                if (t_subtype1.equals(t_subtype2)){
                        yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, null);
                } else if( t_subtype1.equals("UINTEGER")) { yyval.sval = Terceto.addTercetoT("utos",id1,null, "SINGLE");
                        yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, null);
                } else if( t_subtype2.equals("UINTEGER")) { yyval.sval = Terceto.addTercetoT("utos",id2,null, "SINGLE");
                        yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, null);
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en comparacion. "); }
        }
break;
case 68:
//#line 349 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 75:
//#line 362 "grammar.y"
{
                chkAndAssign(val_peek(2).sval,val_peek(0).sval);
                }
break;
case 76:
//#line 366 "grammar.y"
{
                chkAndAssign(val_peek(2).sval,val_peek(0).sval);
        }
break;
case 77:
//#line 369 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 78:
//#line 375 "grammar.y"
{
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals ("UINTEGER")) { yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, t_subtype1);}
                        else {yyerror("Tipo no valido..");}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (!t_subtype1.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (!t_subtype2.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en suma. "); }
}
break;
case 79:
//#line 391 "grammar.y"
{
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER")) { yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, t_subtype1);}
                        else {yyerror("Tipo no valido..");}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (t_subtype1.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (t_subtype2.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en resta. "); }
        }
break;
case 81:
//#line 406 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 82:
//#line 410 "grammar.y"
{
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                if (t_subtype1.equals(t_subtype2)){
                        if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER")) { yyval.sval= Terceto.addTercetoT("MUL",id1,id2, t_subtype1);}
                        else {yyerror("Tipo no valido..");}
                } else if (isCompatible(t_subtype1,t_subtype2)){
                        if (!t_subtype1.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                        else if (!t_subtype2.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                        yyval.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
break;
case 83:
//#line 424 "grammar.y"
{
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                        if (t_subtype1.equals(t_subtype2)){
                                if (t_subtype1.equals("SINGLE") || t_subtype1.equals("UINTEGER")) { yyval.sval= Terceto.addTercetoT("DIV",id1,id2, t_subtype1);}
                                else {yyerror("Tipo no valido..");}
                        } else if (isCompatible(t_subtype1,t_subtype2)){        /*son dif pero compatibles, <=> uno es SINGLE y el otro UINTEGER*/
                                if (!t_subtype1.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id1,null, "SINGLE");}
                                else if (t_subtype2.equals("SINGLE")) {yyval.sval= Terceto.addTercetoT("utos",id2,null, "SINGLE");}
                                yyval.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en division. "); }
        }
break;
case 85:
//#line 442 "grammar.y"
{
                if (isDeclared(val_peek(0).sval)) {
                        yyval.sval = val_peek(0).sval;
                } else {
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": variable "+val_peek(0).sval+" no declarada o no esta al alcance.");
                }
}
break;
case 87:
//#line 451 "grammar.y"
{
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 88:
//#line 456 "grammar.y"
{      /* uso tendria q ser "variable_name" o podria ser otra?*/
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",val_peek(0).sval,AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 92:
//#line 467 "grammar.y"
{
                if (isDeclared(val_peek(3).sval)){
                        if (!AnalizadorLexico.t_simbolos.get_subtype(val_peek(3).sval).equals("PAIR")) {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba variable de tipo pair. "); }
                        else {
                                if (!(val_peek(1).sval.equals("1") || val_peek(1).sval.equals("2"))) {
                                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante 1 o 2. "); 
                                }
                        }
                } else {yyerror("Error en linea "+AnalizadorLexico.line_number+": variable no declarada. "); }
                /*TODO: EN $$.sval DEVOLVER EL LEXEMA QUE REPRESENTARIA A ESE PAIR EN ESA POSICION (POR EJEMPLO pairsito{1})*/
        }
break;
case 93:
//#line 481 "grammar.y"
{ /* agregamos terceto del llamado???  + verifico si existe la funcion? deberia estar declarada antes (Asumimos) */
                /*chequear si existe la funcion*/
                /*chequear si la cantidad de parametros es correcta*/
                /*chequear si los tipos de parametros son correctos*/
                /*crear terceto*/
        }
break;
case 94:
//#line 487 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 95:
//#line 491 "grammar.y"
{
                /* ya se chequea la validez de expr antes*/
                yyval.sval = Terceto.addTerceto("OUTF",val_peek(1).sval,null);
        }
break;
case 96:
//#line 495 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 97:
//#line 496 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 98:
//#line 516 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,val_peek(6).sval);
                /* si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());*/
        }
break;
case 99:
//#line 520 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 100:
//#line 521 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 101:
//#line 522 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 102:
//#line 524 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 103:
//#line 525 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 104:
//#line 526 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 105:
//#line 527 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 106:
//#line 528 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 107:
//#line 529 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 108:
//#line 534 "grammar.y"
{
                /* opcion 1: pila:*/
                /*UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)*/
                /* opcion 2:*/
                yyval.sval = strToTID(Terceto.getTercetoCount());  /*paso id del proximo terceto*/
        }
break;
case 109:
//#line 543 "grammar.y"
{
                String[] idList = val_peek(2).sval.split(",");
                String[] exprList = val_peek(0).sval.split(",");
                if (idList.length != exprList.length){
                        yyerror("Error en linea "+AnalizadorLexico.line_number+": cantidad de expresiones no coincide con cantidad de variables. ");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
break;
case 110:
//#line 555 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 111:
//#line 560 "grammar.y"
{
                
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;

        }
break;
case 112:
//#line 565 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
        }
break;
case 115:
//#line 579 "grammar.y"
{
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
        }
break;
case 116:
//#line 583 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
                
        }
break;
case 117:
//#line 591 "grammar.y"
{
                /* buscar si no hay otra tag con el mismo nombre al alcance*/
                if (!isDeclaredLocal(val_peek(0).sval)) {
                        /* reinserción en la T. de S. con scope actual*/
                        yyerror("Error: La variable "+val_peek(0).sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                        AnalizadorLexico.t_simbolos.del_entry(val_peek(0).sval);
                        AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"TAG","tag_name",null);
                        /* agregar a la tabla de etiquetas*/
                        TablaEtiquetas.add_tag(val_peek(0).sval); 
                }

        }
break;
case 118:
//#line 610 "grammar.y"
{
                /*if existe en TS {*/
                        String id = Terceto.addTerceto("JUMP_TAG",null,null);
                        TablaEtiquetas.add_goto(val_peek(0).sval,Integer.parseInt(id),0);     /* donde puse 0 iría número de línea en lo posible*/
                /*}*/
        }
break;
case 119:
//#line 616 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1541 "Parser.java"
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

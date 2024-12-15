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
        import PrimeraEtapa.Error;

//#line 23 "Parser.java"




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
    1,    1,    3,    3,    1,    1,    1,    2,    1,    1,
    4,    4,    5,    4,    4,    3,    2,    7,    5,    6,
    6,    6,    4,    5,    5,    6,    6,    2,    3,    3,
    3,    3,    3,    1,    1,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  118,    0,    0,    0,
    0,    0,   49,   50,    0,   51,    0,    0,    8,   10,
    0,   12,   13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   14,
    0,    0,    0,    0,   87,    0,    0,    0,    0,   90,
    0,   85,   89,    0,   97,    0,    0,    0,   82,    0,
    0,  108,  120,  119,    4,    9,   15,   11,   19,   20,
   21,   22,   23,   24,   25,   17,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,    0,   88,   73,
   74,   75,   70,   71,   72,    0,    0,    0,   59,    0,
    0,    0,    1,    0,   96,    0,   52,    0,    0,    0,
    0,   32,    0,   31,    0,    0,    0,   30,   29,    0,
   35,   61,    0,   60,   54,   66,   53,   55,    0,    0,
    0,    0,   28,    0,    0,    0,    0,    0,  115,  113,
  112,    0,    0,   91,    0,   56,    0,    0,    0,   83,
   84,   95,   94,    0,   41,    0,   46,    0,   33,    0,
    0,   34,    0,   63,    0,    0,    0,    0,    0,    0,
    0,    0,   92,    0,   40,    0,    0,    0,    0,    0,
   64,   65,   62,    0,  105,    0,    0,    0,   93,   42,
   39,   45,   43,    0,    0,    0,  102,  107,    0,  101,
  106,   37,   38,   36,   98,
};
final static short yydgoto[] = {                          3,
   18,   19,   20,   21,   68,   22,   23,   77,   24,   25,
   26,   27,   28,   29,   30,   31,  133,   32,   80,   33,
   82,  188,   48,   34,   84,  138,   49,  134,  139,  108,
   50,   51,   52,   53,   36,   37,   95,   38,
};
final static short yysindex[] = {                      -174,
 -262,  638,    0,  760,  -20, -112,    0,  -17,  760,  -19,
  -57,  -11,    0,    0, -212,    0, -226,  682,    0,    0,
    8,    0,    0,    8,    8,    8,    8,    8,    8,    8,
   67, -207,  760, -171, -204,  807,  -31,   84,  708,    0,
  133, -147,    0,  -30,    0,  161, -137,  -36,   93,    0,
   62,    0,    0,  734,    0,  -37,  -46, -224,    0,  133,
    4,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -28, -172,  -39,
  760, -133,  -72, -140,  133, -135,    0,  828, -110,  214,
 -108, -108,    0,  -26,  109,   30,  133,  122,    0,    0,
    0,    0,    0,    0,    0,   28,   28,  133,    0,   28,
   28,  -20,    0,  129,    0,   92,    0, -224,  -86,  110,
  111,    0,  133,    0,  -84,  139,  140,    0,    0,  -75,
    0,    0,  849,    0,    0,    0,    0,    0,  786,    4,
   -8,  -40,    0,  -77,    0,  -26,  109,   64,    0,    0,
    0,  133,  133,    0,   65,    0,   62,   62,    4,    0,
    0,    0,    0,  126,    0,  -68,    0,    4,    0, -176,
 -199,    0,  -79,    0, -192,  161,  153,   -5,  161,    4,
    4,  154,    0, -165,    0,    0, -155,  155,  156,  160,
    0,    0,    0,  167,    0,  136,  171,  172,    0,    0,
    0,    0,    0,  -54,  -50,  -25,    0,    0,  203,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       255,
  256,    0,    0,    0,    0,  -42,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  260,    0,    0,
  566,    0,    0,  514,  514,  514,  514,  514,  514,  514,
  540,    0,    0,    0,  220,    0,    0,    0,    0,    0,
    0,    0,  105,    1,    0,    0,    0,    0,   13,    0,
   27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  236,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  274,  300,    0,    0,   24,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  305,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -138,    0,    0,    0,    0,    0,    0,  326,
    0,    0,    0,    0,  131,    0,  352,  -29,    0,    0,
    0,    0,    0,    0,    0,    0,   53,   79,  157,    0,
    0,    0,    0,    0,    0,    0,    0,  378,    0,    0,
    0,    0,    0,    0,    0,    0,  404,    0,    0,  183,
  210,    0,    0,    0,    0,  159,    0,    0,    0,    0,
    0,    0,    0,  435,    0,    0,  461,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  488,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   32,   36,  -14,    0,  497,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  271,  718,    0,    0,
    0,  141,  788,    0,    0,    0,  -34,  177,    0,    0,
  816,   52,   50,    0,    0,    0,  221,   77,
};
final static int YYTABLESIZE=1130;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        179,
   86,  114,   58,  115,  130,    4,  106,   47,  107,   97,
   42,   98,   91,  118,  114,  125,  106,  152,  107,  129,
   56,   87,   46,  103,  105,  104,   81,   47,   60,   63,
  124,  176,  117,   47,  196,   39,   47,   64,   40,   47,
   54,   86,   86,   86,   86,   86,  106,   86,  107,   78,
   13,   14,   79,   66,   16,   62,  189,  186,   85,   86,
   86,   86,   86,  192,   81,   79,   67,   81,   87,   81,
   81,   81,   47,  143,   66,   13,   14,  193,   80,   16,
  186,    1,    2,  126,  127,   81,   81,   81,   81,   66,
  200,  201,   42,   79,   83,   79,   79,   79,   13,   14,
  202,  203,   16,  110,   69,  183,  177,  106,  111,  107,
   96,   79,   79,   79,   79,  135,   66,   67,  143,   80,
   99,   80,   80,   80,   87,   76,  136,   92,   67,  137,
  111,   67,  163,  109,  106,  131,  107,   80,   80,   80,
   80,  194,  141,  197,  198,   69,  144,   82,  148,   82,
   41,  167,  153,  106,  154,  107,   68,  157,  158,  160,
  161,  209,  156,   69,   82,   82,   82,  150,  151,  162,
  165,  166,  169,   82,   82,   82,  208,   47,  170,  171,
   47,  172,  116,  132,    6,  123,   42,  184,  185,  111,
  191,    7,    8,  195,  199,  204,  205,   68,   10,   44,
  206,   12,   13,   14,   15,   47,   16,  207,   17,  117,
  117,  210,  211,  212,   52,   68,  128,  213,   59,   44,
   45,  114,   57,  100,  101,  102,  116,  122,   13,   14,
   52,   90,   16,  114,  123,   47,   55,  178,   43,   44,
   45,  116,  214,  215,   59,   44,   45,   43,   44,   45,
   43,   44,   45,  117,    7,    3,   86,   86,   47,    6,
   86,   86,   86,  115,   86,   86,   86,   86,  117,   86,
   86,   86,   86,   76,   86,   86,   86,   86,   57,   86,
   48,   86,   81,   81,   44,   45,   81,   81,   81,   58,
   81,   81,   81,   81,   47,   81,   81,   81,   81,  110,
   81,   81,   81,   81,    5,   81,   88,   81,   79,   79,
  147,  190,   79,   79,   79,  175,   79,   79,   79,   79,
  182,   79,   79,   79,   79,   77,   79,   79,   79,   79,
    0,   79,   76,   79,   80,   80,    0,    0,   80,   80,
   80,    0,   80,   80,   80,   80,    0,   80,   80,   80,
   80,  109,   80,   80,   80,   80,    0,   80,  110,   80,
   69,   69,    0,    0,   82,   82,   82,    0,   69,   69,
   69,   69,    0,   69,   69,   69,   69,   78,   69,   69,
   69,   69,    0,   69,   77,   69,  111,  111,   59,   44,
   45,   43,   44,   45,  111,  111,    0,  111,    0,  111,
  111,  111,  111,  103,  111,  111,  111,  111,    0,  111,
  109,  111,   68,   68,   52,   52,   43,   44,   45,    0,
   68,   68,   68,   68,    0,   68,   68,   68,   68,    0,
   68,   68,   68,   68,  104,   68,   78,   68,  116,  116,
    0,    0,    0,    0,    0,    0,  116,  116,    0,  116,
    0,  116,  116,  116,  116,    0,  116,  116,  116,  116,
   99,  116,  103,  116,    0,  117,  117,    0,    0,  145,
   44,   45,    0,  117,  117,    0,  117,    0,  117,  117,
  117,  117,    0,  117,  117,  117,  117,  100,  117,    0,
  117,   47,   47,  104,    0,    0,    0,    0,    0,   47,
   47,    0,   47,    0,   47,   47,   47,   47,    0,   47,
   47,   47,   47,   16,   47,    0,   47,    0,    0,   99,
   69,   70,   71,   72,   73,   74,   75,    0,    0,   76,
   76,    0,    0,    0,    0,    0,    0,   76,   76,   18,
   76,    0,   76,   76,   76,   76,  100,   76,   76,   76,
   76,    0,   76,    0,   76,  110,  110,    0,    0,    0,
    0,    0,    0,  110,  110,   16,  110,    0,  110,  110,
  110,  110,    0,  110,  110,  110,  110,    0,  110,    0,
  110,   77,   77,    0,    0,    0,    0,    0,    0,   77,
   77,    0,   77,    0,   77,   77,   77,   77,    0,   77,
   77,   77,   77,    0,   77,    0,   77,  109,  109,    0,
    0,    0,    0,    0,    0,  109,  109,    0,  109,    0,
  109,  109,  109,  109,    0,  109,  109,  109,  109,    0,
  109,    0,  109,   78,   78,    0,    0,    0,    0,    0,
    0,   78,   78,    0,   78,    0,   78,   78,   78,   78,
    0,   78,   78,   78,   78,    0,   78,    0,   78,  103,
  103,    0,    0,    0,    0,    0,    0,  103,  103,    0,
  103,    0,  103,  103,  103,  103,    0,  103,  103,  103,
  103,    0,  103,    0,  103,    0,    0,    0,    0,    0,
  104,  104,    0,    0,    0,    0,    0,    0,  104,  104,
    0,  104,    0,  104,  104,  104,  104,    0,  104,  104,
  104,  104,    0,  104,    0,  104,   99,   99,    0,    0,
    0,    0,    0,    0,   99,   99,    0,   99,    0,   99,
   99,   99,   99,    0,   99,   99,   99,   99,    0,   99,
    0,   99,    0,  100,  100,    0,    0,    0,    0,    0,
    0,  100,  100,   89,  100,    0,  100,  100,  100,  100,
    0,  100,  100,  100,  100,    0,  100,    0,  100,   16,
   16,    0,    0,    0,  119,  120,    0,   16,   16,    0,
   16,    0,   16,   16,   16,   16,    0,   16,   16,   16,
   16,    0,   16,    0,   16,   18,   18,    0,    0,   61,
   89,    0,    0,   18,   18,   89,   18,    0,   18,   18,
   18,   18,    0,   18,   18,   18,   18,   35,   18,   35,
   18,   16,   16,    0,   35,    0,    0,    0,   94,   16,
   16,    0,    0,   35,   16,  164,   16,   16,    0,   16,
   16,   16,   16,  116,   16,    0,   16,  121,   35,    0,
   89,   35,    0,    0,   35,    0,   89,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
    0,    0,  140,    0,    0,    0,    0,  146,    0,    0,
    0,    0,    0,    0,  155,    0,    0,  187,  187,    0,
    0,    0,    0,    5,    6,  159,   35,    0,   35,    0,
    0,    7,    8,   35,    0,    9,  149,  149,   10,   11,
  168,   12,   13,   14,   15,    0,   16,    0,   17,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    6,  180,
  181,    0,    0,    0,    0,    7,    8,    0,   35,    0,
   65,    0,   10,   11,   35,   12,   13,   14,   15,    0,
   16,    0,   17,    5,    6,    0,    0,    0,    0,    0,
    0,    7,    8,    0,    0,    0,   93,    0,   10,   11,
    0,   12,   13,   14,   15,    0,   16,    0,   17,  112,
    6,    0,    0,    0,    0,    0,    0,    7,    8,    0,
    0,    0,  113,    0,   10,   11,    0,   12,   13,   14,
   15,    0,   16,    0,   17,    5,    6,    0,    0,    0,
    0,    0,    0,    7,    8,    0,    0,    0,    0,    0,
   10,   11,    0,   12,   13,   14,   15,    0,   16,    0,
   17,  173,    6,    0,    0,    0,    0,    0,    0,    7,
    8,    0,    0,    0,    0,  174,   10,    0,    0,   12,
   13,   14,   15,    6,   16,    0,   17,    0,    0,    0,
    7,    8,    0,    0,    0,   86,    0,   10,    0,    0,
   12,   13,   14,   15,    6,   16,    0,   17,    0,    0,
    0,    7,    8,    0,    0,    0,  142,    0,   10,    0,
    0,   12,   13,   14,   15,    6,   16,    0,   17,    0,
    0,    0,    7,    8,    0,    0,    0,    0,    0,   10,
    0,    0,   12,   13,   14,   15,    0,   16,    0,   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   44,   60,   41,   44,  268,   43,   45,   45,   40,
  123,   46,   44,   60,   44,   44,   43,   44,   45,   59,
   40,   36,   40,   60,   61,   62,    0,   45,   40,  256,
   59,   40,  257,   45,   40,    4,   45,  264,   59,   45,
    9,   41,   42,   43,   44,   45,   43,   47,   45,  257,
  275,  276,    0,   18,  279,  268,  256,  257,  263,   59,
   60,   61,   62,  256,   33,  273,   59,   41,   83,   43,
   44,   45,   45,   88,   39,  275,  276,  270,    0,  279,
  257,  256,  257,  256,  257,   59,   60,   61,   62,   54,
  256,  257,  123,   41,  266,   43,   44,   45,  275,  276,
  256,  257,  279,   42,    0,   41,  141,   43,   47,   45,
  258,   59,   60,   61,   62,  256,   81,  256,  133,   41,
  258,   43,   44,   45,  139,   59,  267,   44,  267,  270,
    0,  270,   41,   41,   43,  269,   45,   59,   60,   61,
   62,  176,  278,  178,  179,   41,  257,   43,  257,   45,
  263,   41,   44,   43,  125,   45,    0,  106,  107,  110,
  111,  196,   41,   59,   60,   61,   62,   91,   92,   41,
  257,   62,  257,   43,   44,   45,   41,   45,   40,   40,
   45,  257,    0,  256,  257,  263,  123,   62,  257,   59,
  270,  264,  265,   41,   41,   41,   41,   41,  271,   41,
   41,  274,  275,  276,  277,   45,  279,   41,  281,    0,
  257,   41,   41,  268,  257,   59,  256,  268,  256,  257,
  258,  259,  280,  260,  261,  262,   44,  256,  275,  276,
  273,  263,  279,  263,  263,    0,  256,  278,  256,  257,
  258,   59,  268,   41,  256,  257,  258,  256,  257,  258,
  256,  257,  258,   44,    0,    0,  256,  257,   45,    0,
  260,  261,  262,   44,  264,  265,  266,  267,   59,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  266,  279,
  269,  281,  256,  257,  257,  258,  260,  261,  262,  266,
  264,  265,  266,  267,   59,  269,  270,  271,  272,    0,
  274,  275,  276,  277,    0,  279,   36,  281,  256,  257,
   90,  171,  260,  261,  262,  139,  264,  265,  266,  267,
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
  267,   -1,  269,  270,  271,  272,   59,  274,  275,  276,
  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,   -1,
   -1,   -1,   -1,  264,  265,    0,  267,   -1,  269,  270,
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
   -1,  264,  265,   36,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,  256,
  257,   -1,   -1,   -1,   57,   58,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,   12,
   83,   -1,   -1,  264,  265,   88,  267,   -1,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,    2,  279,    4,
  281,  256,  257,   -1,    9,   -1,   -1,   -1,   41,  264,
  265,   -1,   -1,   18,  269,  118,  271,  272,   -1,  274,
  275,  276,  277,   56,  279,   -1,  281,   60,   33,   -1,
  133,   36,   -1,   -1,   39,   -1,  139,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   54,
   -1,   -1,   85,   -1,   -1,   -1,   -1,   90,   -1,   -1,
   -1,   -1,   -1,   -1,   97,   -1,   -1,  170,  171,   -1,
   -1,   -1,   -1,  256,  257,  108,   81,   -1,   83,   -1,
   -1,  264,  265,   88,   -1,  268,   91,   92,  271,  272,
  123,  274,  275,  276,  277,   -1,  279,   -1,  281,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  152,
  153,   -1,   -1,   -1,   -1,  264,  265,   -1,  133,   -1,
  269,   -1,  271,  272,  139,  274,  275,  276,  277,   -1,
  279,   -1,  281,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,  272,
   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,
  277,   -1,  279,   -1,  281,  256,  257,   -1,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,
  271,  272,   -1,  274,  275,  276,  277,   -1,  279,   -1,
  281,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,   -1,  270,  271,   -1,   -1,  274,
  275,  276,  277,  257,  279,   -1,  281,   -1,   -1,   -1,
  264,  265,   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,
  274,  275,  276,  277,  257,  279,   -1,  281,   -1,   -1,
   -1,  264,  265,   -1,   -1,   -1,  269,   -1,  271,   -1,
   -1,  274,  275,  276,  277,  257,  279,   -1,  281,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,  271,
   -1,   -1,  274,  275,  276,  277,   -1,  279,   -1,  281,
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
"mult_assign_statement : ID ASSIGN expr_list",
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

//#line 779 "grammar.y"
        public static ArrayList<String> errores = new ArrayList<>();
        public static String actualScope = "MAIN";
        //static Stack<Integer> UntilStack = new Stack<>();


	public static void yyerror(String msg){
                errores.add(msg);
                new Error(AnalizadorLexico.line_number,msg,true,"SEMANTICO");
	       //System.out.println(msg);
	}

        public int yylex(){ // tambien devuelve el yylval ...
                yylval = new ParserVal();       //se deberia modificar la variable de clase Parser 
                return AnalizadorLexico.yylex(yylval);
        }

        public static String strToTID(String id){      // agrega "<" ">" para indicar es id de terceto, y no clave de TS
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
                } else {/*System.out.println("CUIDADO SE ESTA PASANDO UN ID QUE NO ES DE FUNCION A getFunctionID");*/
                        return null;
                }
        }

        public static String chkAndGetType(String valStr){  
                // DEVUELVE TIPO PRIMITIVO (HEXA, UINTEGER, O SINGLE)
                // SI valStr es invoc_funcion, devuelve el tipo de retorno de la funcion
                // Recibe lexema SIN SCOPE 
                // si es pair, llega con {} 
                //AnalizadorLexico.t_simbolos.display();
                String lexem = "";

                // CASO TERCETO
                if (isTerceto(valStr)) { return Terceto.getSubtipo(valStr); }

                else {
                        // puede ser variable, o cte, o expr_pair, o invoc. a funcion
                        // CASO CONSTANTE
                        if (isCte(valStr)) {
                               //System.out.println(valStr+"ES cte");
                                return AnalizadorLexico.t_simbolos.get_subtype(valStr);
                        }
                        
                        
                        else {  // variable, invoc. a funcion o expr_pair

                                // CASO ACCESO A PAIR
                                if (isPairAccess(valStr)) {  
                                        lexem = valStr.substring(0,valStr.indexOf("{")); // me quedo con el id del pair
                                        lexem = getDeclared(lexem);
                                } 
                                //else {        // me fijo que si no es un acceso a pair, no sea un pair sin el acceso ( sin {})

                                //}
                                
                                else if (isFunction(valStr)){
                                        lexem = getFunctionID(valStr);  //vuelve con ambito
                                }

                                else { lexem = getDeclared(valStr); }
                                
                                String type = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                               
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
                                } else yyerror("Identificador "+valStr+" no declarado. ");
                                
                                return null;
                        }
                }
        }

       /*  public void set_var_scope() {

        }*/

        public static Boolean isCte(String valStr){
                String valStr_to_upper = valStr.toUpperCase();  // caso HEXA
                if (AnalizadorLexico.t_simbolos.get_entry(valStr_to_upper) != null){
                        return (AnalizadorLexico.t_simbolos.get_entry(valStr_to_upper).getTipo().equals("CTE")); 
                } else return false;
                // si no esta, no es cte o no está
        }

        public static void pushScope(String scope){
                actualScope = actualScope + ":" + scope;
                TablaEtiquetas.pushScope();
        }

        public static void popScope(){
                actualScope = popScope(actualScope);
               //System.out.println("Scope tras salir: "+actualScope);
        }

        public static String popScope(String scope){
                // quita ultimo scope, q esta delimitado con ':'
                int index = scope.lastIndexOf(":");
                //System.out.print("popScope scope: "+scope);
                if (index != -1) {
                        scope = scope.substring(0, index);
                } // else scope queda igual
                return scope;
        }


        public static boolean isDeclared(String id) {   // recibe id sin scoope
                // PROBELMA DE EMBEBIDOS: LLEGA CON :MAIN
                // chequea si ya fue declarada en el scope actual u otro global al mismo ( va pregutnando con cada scope, sacando el ultimo. comienza en el actual)
                if (isCte(id)) {System.out.println("OJO ESTAS PASANDO UNA CTE A isDeclared:"+id);}
                String scopeaux = new String(actualScope);
                //System.out.println("a isDeclared llego: "+id);
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
                return false;
        }

        public static Boolean isDeclaredLocal(String id){       //devuelve si fue declarada en el ambito actual.
                return (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null);
        }

        public static void chkAndDeclareVar(String tipo, String id){
                
                AnalizadorSemantico.validID(tipo,id,"VARIABLE_NAME");
                //chequear tipo sea valido (uinteger,hexa,single o definido por usuario)
                // AnalizadorLexico.t_simbolos.display();
                if (AnalizadorLexico.t_simbolos.get_entry(tipo) == null) {yyerror("tipo de variable no valido. "); }
                else {//System.out.println("tipo de var declarada:" +AnalizadorLexico.t_simbolos.get_entry(tipo).getUse());
                }
                
                if (tipo.equals("UINTEGER") || tipo.equals("HEXA") ||tipo.equals("SINGLE") || (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME"))) {
                // pair ejemplo en TS: pairsito:main subtipo:uinteger use:typename  no hace falta aclarar es un pair xq es el unico tipo q puede definir  
                // y otra entrada: p1:main:f1 subtipo: pairsito use: variable_name
                                if (AnalizadorLexico.t_simbolos.get_entry(id+":"+actualScope) != null) {
                                        yyerror("La variable "+id+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");}
                                else if (id.substring(0,1).equals("S") || id.substring(0,1).equals("U") || id.substring(0,1).equals("V") || id.substring(0,1).equals("W")) {
                                        // si comienza con s,u,v,w, ya tiene tipo embebido, y aca da error de redeclaracion
                                        yyerror("Se intento redeclarar un tipo embebido.");
                                } 
                                else {
                                
                                // si no fue declarada, agregar a la tabla de simbolos, el scope y el tipo:
                                AnalizadorLexico.t_simbolos.del_entry(id);
                                //AnalizadorLexico.t_simbolos.display();
                                if (AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN") != null && AnalizadorLexico.t_simbolos.get_entry(tipo+":MAIN").getUse().equals("TYPE_NAME")){
                                        // SI EL TIPO ES DEFINIDO POR USER, VA CON :MAIN
                                        AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo+":MAIN");
                                }else {AnalizadorLexico.t_simbolos.add_entry(id+":"+actualScope,"ID",tipo);}
                                
                                AnalizadorLexico.t_simbolos.set_use(id+":"+actualScope,"VARIABLE_NAME");
                                }   
                } else {yyerror("tipo de variable no valido. "); }

        }

        public static Boolean isCompatible(String t_subtype1,String t_subtype2){  //medio al dope creo, al menos devuelva el tipo resultante en caso de ser ocmpatible, y null si no.
                return AnalizadorSemantico.isCompatible(t_subtype1,t_subtype2);
        }

        public static String getPairName(String id){
                // devuelve el nombre del pair, sin la posicion de acceso
                return id.substring(0,id.lastIndexOf("{"));             // FIJARSE Q ANDE
        }

        public static String chkAndAssign(String id, String expr){       // chequea id este declarado y expr sea valida.
                //AnalizadorLexico.t_simbolos.display();
                        //System.out.print("chkAndAssign: id: "+id);
                        String pid = id;
                        String pexpr = expr;
                        String posid = "";
                        String posexpr = "";
                        //System.out.println("chkAndAssign: expr: "+expr);
                        //System.out.println("chkAndAssign: id: "+id);
                        Boolean idPair = isPairAccess(id,"assign");   // en una asignacion, dos pair pueden asignarse
                        Boolean exprPair = isPairAccess(expr,"assign");
                        // PAIR SIN ACCESO PUEDE SOLO SI AMBAS PARTES DE LA ASIGNACIÓN LO SON
                        if (idPair){
                                // posid es {1} o {2}
                                System.out.println(id+"id es acceso a pair");
                                posid = id.substring(id.lastIndexOf("{"),id.lastIndexOf("}") + 1);
                        //System.out.println("assign: posid: "+posid);
                                id = getPairName(id);
                        //System.out.println("assign: id: "+id);
                        }
                        if (exprPair){
                                System.out.println(expr+"expr es acceso a pair");
                                posexpr = expr.substring(expr.lastIndexOf("{"),expr.lastIndexOf("}") + 1);
                                expr = getPairName(expr);
                        }
                        if (!isDeclared(id))
                        {yyerror("variable "+id+" no declarada. "); }
                        else {
                                // EXPR PUEDE SER :CTE, ID, EXPRPAIR, FUN_INVOC, O UN TERCETO
                                String lexemExpr = "";
                                String subtypeT = "";
                                if (isTerceto(expr)) {
                                        subtypeT = chkAndGetType(expr);
                                        lexemExpr = expr;}   // y lexem es el terceto (expr)
                                else{
                                        expr = expr.toUpperCase();
                                        if (isCte(expr)){      // si es cte, la misma es como se busca en la tabla de simbolos
                                        //System.out.println(expr+" es cte");
                                                lexemExpr = expr;
                                                subtypeT = chkAndGetType(expr);
                                        } else if (isDeclared(expr)){ 
                                                         lexemExpr = getDeclared(expr);
                                                        if (!isPairAccess(pexpr,"assign") && isPair(pexpr)){      // caso asignacion de pair -> p1 := p2
                                                                subtypeT = AnalizadorLexico.t_simbolos.get_subtype(lexemExpr);
                                                                System.out.println("pair completo,"+ lexemExpr+" subtypeT: "+subtypeT);
                                                        } else {                // caso acceso a pair
                                                                subtypeT = chkAndGetType(expr+posexpr); // si no era pair, posexpr es vacio
                                                        }
                                                }
                                                else {yyerror(""+expr+" no declarada. ");}

                                        }
                                
                                String lexemID = getDeclared(id);
                                String subtypeID = "";
                                if (!isPairAccess(pid,"assign") && isPair(pid) ){      // caso asignacion de pair -> p1 := p2
                                        subtypeID = AnalizadorLexico.t_simbolos.get_subtype(lexemID);
                                        System.out.println("pair completo, "+lexemID+" subtypeID: "+subtypeID);
                                } else {
                                        subtypeID = chkAndGetType(id+posid);      // SI ES PAIR, DEVUELVE TIPO PRIMITIVO! :D
                                }
                                //System.out.println("subtypeID: "+subtypeID);
                                //System.out.println("subtypeT: "+subtypeT);
                                if (idPair){lexemID = lexemID+posid;}
                                if (exprPair){lexemExpr = lexemExpr+posexpr;}
                                Boolean pairs = false;
                                if (!isPairAccess(pid,"assign") && isPair(pid) && !isPairAccess(pexpr,"assign") && isPair(pexpr)){
                                        pairs = true;
                                        System.out.println(" ES ASIGNACION ENTRE PAIRS COMPLETOS!");
                                        subtypeID = AnalizadorLexico.t_simbolos.get_subtype(AnalizadorLexico.t_simbolos.get_subtype(lexemID));
                                        subtypeT = AnalizadorLexico.t_simbolos.get_subtype(AnalizadorLexico.t_simbolos.get_subtype(lexemExpr));
                                }           // si es asignacion entre pairs completos
                                if (subtypeT.equals(subtypeID)){
                                        if (pairs){
                                                Terceto.addTercetoT(":=",lexemID+"{1}",lexemExpr+"{1}",subtypeID);
                                                return Terceto.addTercetoT(":=",lexemID+"{2}",lexemExpr+"{2}",subtypeID);
                                        }
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,subtypeID);
                                }
                                
                                else if (subtypeID.equals("SINGLE") && (subtypeT.equals("UINTEGER") || subtypeT.equals("HEXA"))){    
                                        if (pairs){
                                                String taux1 = "";
                                                String taux2 = "";
                                               taux1 = Terceto.addTercetoT("utos",lexemExpr+"{1}",null,"SINGLE");
                                               taux2 = Terceto.addTercetoT("utos",lexemExpr+"{2}",null,"SINGLE");
                                               Terceto.addTercetoT(":=",lexemID+"{1}",taux1,"SINGLE");
                                               return Terceto.addTercetoT(":=",lexemID+"{2}",taux2,"SINGLE");
                                        }
                                        Terceto.addTercetoT("utos",lexemExpr,null,"SINGLE");
                                        return Terceto.addTercetoT(":=",lexemID,Terceto.getLast(),"SINGLE");
                                }// agregar otro else por si uno es uinteger y el otro hexa
                                else if (subtypeID.equals("UINTEGER") && subtypeT.equals("HEXA")){
                                        if (pairs){
                                                String taux1 = "";
                                                String taux2 = "";
                                               taux1 = Terceto.addTercetoT("utos",lexemExpr+"{1}",null,"UINTEGER");
                                               taux2 = Terceto.addTercetoT("utos",lexemExpr+"{2}",null,"UINTEGER");
                                               Terceto.addTercetoT(":=",lexemID+"{1}",taux1,"UINTEGER");
                                               return Terceto.addTercetoT(":=",lexemID+"{2}",taux2,"UINTEGER");
                                        }
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,"UINTEGER");
                                }
                                else if (subtypeID.equals("HEXA") && subtypeT.equals("UINTEGER")){
                                        if (pairs){
                                                String taux1 = "";
                                                String taux2 = "";
                                               taux1 = Terceto.addTercetoT("utos",lexemExpr+"{1}",null,"HEXA");
                                               taux2 = Terceto.addTercetoT("utos",lexemExpr+"{2}",null,"HEXA");
                                               Terceto.addTercetoT(":=",lexemID+"{1}",taux1,"HEXA");
                                               return Terceto.addTercetoT(":=",lexemID+"{2}",taux2,"HEXA");
                                        }
                                        return Terceto.addTercetoT(":=",lexemID,lexemExpr,"HEXA");
                                }
                                else if (!subtypeID.equals("") && !subtypeT.equals("")) {       // para que no de error de tipos incompatibles si enrealidad era otro error antes
                                        //System.out.println("subtypeID:"+subtypeID+" subtypeT:"+subtypeT);
                                        yyerror("tipos incompatibles en asignacion. "); 
                                        System.out.println("tipo id: "+subtypeID+" tipo expr: "+subtypeT);
                                        System.out.println("id: "+id+" expr: "+expr);
                                }

                        }
                        return Terceto.getLast();
                }

        /*public String getType(String lexema){   // se le pasa sin scope
                // puede ser: variable, funcion, cte, expr_pair, terceto
                if (isTerceto(lexema)){return "terceto"}
                if (isCte(lexema)){return AnalizadorLexico.t_simbolos.get_subtype(lexema);}

        }*/


        public static String getDeclared(String id){ //devuelve lexema completo con el cual buscar en TS.
                //LLAMAR SI ES ID O FUNCION.  SI ES EXPR_PAIR, SE ASUME LLEGA SIN LA POSICION (SIN {})
                String scopeaux = new String(actualScope);
                //AnalizadorLexico.t_simbolos.display();
                /*if (id.substring(0,1).equals("S") || id.substring(0,1).equals("U") || id.substring(0,1).equals("V") || id.substring(0,1).equals("W")){ // si es embebida
                        return id + ":MAIN";
                } */
                 
                if (isDeclared(id)){

                       //System.out.println("getDeclared: id: "+id);
                        if (isDeclaredLocal(id)) {return id+":"+actualScope;}
                        else {
                                do {
                                        scopeaux = (scopeaux.equals("MAIN")) ? "MAIN" : scopeaux.substring(0,scopeaux.lastIndexOf(":"));
                                        if (AnalizadorLexico.t_simbolos.get_entry(id+":"+scopeaux) != null)     
                                                return id+":"+scopeaux;;
                                        //System.out.println("EL SCOPEAUX: "+scopeaux);
                                } while ((!scopeaux.equals("MAIN")));
                                return null;
                        }
                } else {/*System.out.println("NO ESTA DECLARADA");*/return null;}

        }

        public static Boolean isCharch(String id){     //charhc comienza  y termina con []
                return (id.charAt(0) == '[' && id.charAt(id.length()-1) == ']');
        }

        public static Boolean isPair(String id){       
                return (!(AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("UINTEGER") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("HEXA") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("SINGLE"))); 
        }

        public static Boolean isPairAccess(String id){
                return isPairAccess(id,"");
        }

        public static Boolean isPairAccess (String id, String op){
                // un pair tiene la posicion de acceso entre {}; ej: pairsito{1}
                if (!isCte(id)){
                        if (AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)) != null) {
                                        if (!(AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("UINTEGER") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("HEXA") || AnalizadorLexico.t_simbolos.get_subtype(getDeclared(id)).equals("SINGLE"))) {
                                                if (!(id.charAt(id.length()-1) == '}') && !op.equals("assign")){
                                                        System.out.println("op: "+op);
                                                        yyerror("Se intento acceder a un pair sin especificar la posicion de acceso: "+id);
                                                }
                                        }
                        }
                }
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
//#line 1016 "Parser.java"
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
//#line 18 "grammar.y"
{
                /* FIN DEL PROGRAMA, CHEQUEAR:*/
                /*AnalizadorLexico.t_simbolos.clean();    // Limpieza de T. de Simbolos: quita todo lo que no tenga scope*/
                Terceto.addTerceto("END",null,null);
                TablaEtiquetas.end();                   /* Asocia sentencias GoTo con su correspondiente etiqueta*/
                }
break;
case 2:
//#line 24 "grammar.y"
{yyerror("Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 25 "grammar.y"
{yyerror("La sintaxis del programa es incorrecta." ); }
break;
case 4:
//#line 26 "grammar.y"
{yyerror("Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 27 "grammar.y"
{yyerror("Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 28 "grammar.y"
{yyerror("Falta delimitadores del programa. "); }
break;
case 7:
//#line 29 "grammar.y"
{yyerror("El programa está vacío"); }
break;
case 11:
//#line 40 "grammar.y"
{/*System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 12:
//#line 41 "grammar.y"
{/*System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);*/}
break;
case 13:
//#line 42 "grammar.y"
{/*System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 14:
//#line 43 "grammar.y"
{yyerror("Sintaxis incorrecta de sentencia.");}
break;
case 16:
//#line 50 "grammar.y"
{yyerror("Se esperaba ';' al final de la sentencia.");}
break;
case 17:
//#line 54 "grammar.y"
{yyerror("Se encontró ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 59 "grammar.y"
{/*System.out.print("Sentencia de control IF en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 20:
//#line 60 "grammar.y"
{/*System.out.print("Sentencia de asignacion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 21:
//#line 61 "grammar.y"
{/*System.out.print("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 22:
//#line 62 "grammar.y"
{/*System.out.print("Sentencia de repeat until en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 23:
//#line 63 "grammar.y"
{/*System.out.print("Sentencia de salto goto en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 24:
//#line 64 "grammar.y"
{/*System.out.print("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 25:
//#line 65 "grammar.y"
{/*System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number+"\n");*/}
break;
case 26:
//#line 66 "grammar.y"
{/*System.out.println("Sentencia de TAG\n");*/}
break;
case 29:
//#line 76 "grammar.y"
{
                String[] idList = val_peek(1).sval.split(",");                
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar(val_peek(2).sval, idList[i]);
                }
                }
break;
case 30:
//#line 83 "grammar.y"
{
                yyerror("Falta el ';' al final de la sentencia.");
                String[] idList = val_peek(1).sval.split(",");
                for (int i = 0; i < idList.length; i++) {
                        chkAndDeclareVar(val_peek(2).sval, idList[i]);
                }
                }
break;
case 31:
//#line 91 "grammar.y"
{
                chkAndDeclareVar(val_peek(2).sval,val_peek(1).sval);
                }
break;
case 32:
//#line 95 "grammar.y"
{
                yyerror("Falta ';' al final de la sentencia o se intenta declarar varias variables sin separarlas con ','.");
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
                }
break;
case 33:
//#line 102 "grammar.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 34:
//#line 103 "grammar.y"
{ yyval.sval = yyval.sval + "," + val_peek(0).sval; }
break;
case 35:
//#line 116 "grammar.y"
{ 
                /* Actualización del scope: fin de la función fuerza retorno al ámbito del padre*/
                        yyval.sval = Terceto.addTercetoT("END_FUN",scopeToFunction(actualScope),null,null);
                        /*System.out.println("Salgo del ambito: "+actualScope);*/
                        TablaEtiquetas.popScope();
                        popScope();
                }
break;
case 36:
//#line 134 "grammar.y"
{
                AnalizadorSemantico.validID(val_peek(5).sval,val_peek(4).sval,"FUN_NAME");           /* Chequeo de tipos embebidos*/
                        /* Control de ID: debe ser único en el scope actual*/
                        if (isDeclaredLocal(val_peek(4).sval))   {
                                String declared_type = chkAndGetType(val_peek(4).sval);
                                if (declared_type.equals("I"))  /* ?*/
                                yyerror("El nombre seleccionado para la funcion no está disponible en el scope actual.");yyerror("No se permite la redeclaración de funciones: el nombre seleccionado no está disponible en el scope actual.");
                        }
                        else {
                                String param_name = val_peek(2).sval.split("-")[1]; 
                                String param_type = val_peek(2).sval.split("-")[0];
                               /*System.out.println("param_name, param_type == "+param_name+", "+param_type);*/
                                AnalizadorSemantico.validID(param_type,param_name,"PARAM_NAME");
                                /* Actualización del ID: scope, uso, tipos de PARAMETRO y RETORNO (usamos los campos "SUBTIPO" y "VALOR" de la T. de S. respectivamente)*/
                                AnalizadorLexico.t_simbolos.del_entry(val_peek(4).sval);
                                AnalizadorLexico.t_simbolos.add_entry(val_peek(4).sval+":"+actualScope,"ID",val_peek(6).sval,"FUN_NAME",param_type);
                                /*AnalizadorLexico.t_simbolos.display();*/
                                /*String param_lexem = getDeclared(param_name);*/
                                /*System.out.println("param_lexem == "+param_lexem);*/
                                
                                /* Actualización del scope: las sentencias siguientes están dentro del cuerpo de la función*/
                                String act_scope = new String(actualScope);
                                pushScope(val_peek(4).sval); 

                                /* Actualización del ID del parámetro: se actualiza el scope al actual*/
                                /* AnalizadorLexico.t_simbolos.display();*/
                                AnalizadorLexico.t_simbolos.del_entry(param_name);      /* param_name llega con el scope y todo (desde donde fue llamado)*/
                                AnalizadorLexico.t_simbolos.add_entry(param_name+":"+actualScope,"ID",param_type,"PARAM_NAME");

                        /* Posible generación de terceto de tipo LABEL*/
                                /*System.out.println("Parametro: "+param_name+" de tipo "+param_type);*/
                                /*System.out.println("Lexema: "+param_name+":"+actualScope);*/
                                yyval.sval = Terceto.addTercetoT("INIC_FUN",val_peek(4).sval+":"+act_scope,param_name+":"+actualScope,param_type); /*para saber donde llamarla en assembler*/
                        }       /* el parametro se 'define' DENTRO de la funcion. entonces tiene el scope de la funcion.*/
        }
break;
case 37:
//#line 170 "grammar.y"
{
                yyerror("Se esperaba nombre de funcion.") ;
                }
break;
case 38:
//#line 174 "grammar.y"
{
                /* guardar el scope de la funcion*/
                pushScope(val_peek(4).sval); 
                yyerror("Parametro incorrecto. Verifica que solo haya 1 parametro. ");
                AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval,"FUN_NAME");
                }
break;
case 39:
//#line 193 "grammar.y"
{
                /* se le pone scope 'MAIN' */
                AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval,"TYPE_NAME");
                        if (val_peek(2).sval.equals("UINTEGER") || val_peek(2).sval.equals("SINGLE") || val_peek(2).sval.equals("HEXA")) {

                                if (AnalizadorLexico.t_simbolos.get_entry(val_peek(0).sval+":MAIN") != null){
                                        yyerror("Se está intentando redeclarar "+val_peek(0).sval+".");
                                } else {
                                        AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":MAIN","ID",val_peek(2).sval,"TYPE_NAME");
                                }
                        } else {yyerror("Tipo invalido para pair. Solo se permite primitivos: uinteger, single, hexadecimal."); }
                }
break;
case 40:
//#line 206 "grammar.y"
{yyerror("Se esperaba 'pair'.") ; }
break;
case 41:
//#line 207 "grammar.y"
{yyerror("Se esperaba que el tipo este entre '<' y '>'.") ; }
break;
case 42:
//#line 208 "grammar.y"
{yyerror("Se esperaba un ID al final de la declaracion."); }
break;
case 43:
//#line 216 "grammar.y"
{
                yyval.sval = val_peek(1).sval+"-"+val_peek(0).sval;
        }
break;
case 44:
//#line 219 "grammar.y"
{yyerror("Se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 220 "grammar.y"
{yyerror("Se esperaba nombre de parametro."); }
break;
case 46:
//#line 224 "grammar.y"
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
                       String param = (AnalizadorLexico.t_simbolos.get_entry(val_peek(1).sval) != null && AnalizadorLexico.t_simbolos.get_entry(val_peek(1).sval).getTipo().equals("ID")) ? getDeclared(val_peek(1).sval) : val_peek(1).sval;
                       yyval.sval = Terceto.addTercetoT(":=","@"+scopeToFunction(actualScope),param,AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                       /* chequear getDeclared($3.sval) si es pair, sea acceso ( {} )*/
                        yyval.sval = Terceto.addTercetoT("RET",getDeclared(val_peek(1).sval),scopeToFunction(actualScope),AnalizadorLexico.t_simbolos.get_subtype(scopeToFunction(actualScope)));
                } else {
                        yyerror("El tipo de retorno no coincide con el tipo de la funcion. ");
                }
        } else {/*System.out.println("algo anda mal en return_statement, no encuentra la funcion actual ");*/
               /*System.out.println(" actual scope: "+actualScope);*/
               /*System.out.println("no se encontro en la TS: "+scopeToFunction(actualScope));        */
        }
        }
break;
case 47:
//#line 251 "grammar.y"
{yyerror("Faltan parentesis en sentencia de return. ") ;}
break;
case 53:
//#line 298 "grammar.y"
{       /*pdoria poner end_if dentro de then_statement y hacer esto ahi.*/
        /*completo terceto*/
        Terceto.print_all();
        /*System.out.println("debugging if: "+extractNumber($2.sval));*/
        /*Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt((extractNumber($2.sval)).substring(1,($2.sval).length()-1)+1))+">"); */
        Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt(extractNumber(val_peek(1).sval))+1)+">"); 
}
break;
case 54:
//#line 305 "grammar.y"
{yyerror("Se esperaba END_IF.") ; }
break;
case 55:
//#line 307 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),"<"+String.valueOf(Integer.parseInt(extractNumber(val_peek(0).sval)) + 1)+">",null); 

}
break;
case 56:
//#line 315 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,null);
                Terceto.pushTerceto(yyval.sval); /*apilo terceto incompleto.*/
        }
break;
case 57:
//#line 319 "grammar.y"
{yyerror("Se esperaba que la condicion este entre parentesis. "); }
break;
case 58:
//#line 320 "grammar.y"
{
                yyerror("Se esperaba ')' luego de la condicion"); }
break;
case 59:
//#line 322 "grammar.y"
{yyerror("se esperaba '(' antes de la condicion. "); }
break;
case 60:
//#line 350 "grammar.y"
{
                yyval.sval = val_peek(0).sval;       /*devuelve ultimo terceto*/
        }
break;
case 61:
//#line 354 "grammar.y"
{yyerror("Sintaxis de sentencia ejecutable dentro del IF, incorrecta."); }
break;
case 62:
//#line 358 "grammar.y"
{yyval.sval = val_peek(1).sval;}
break;
case 63:
//#line 359 "grammar.y"
{yyerror("Se esperaba sentencia ejecutable luego del else. "); }
break;
case 64:
//#line 360 "grammar.y"
{yyerror("sintaxis de sentencia ejecutable luego del else, incorrecta "); }
break;
case 65:
//#line 361 "grammar.y"
{yyerror("se esperaba END_IF ") ; }
break;
case 66:
//#line 365 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BI",null,null); /*incompleto, primer operando se completara despues.*/
                /*$$.sval = Terceto.addTerceto("LABEL_CTRL",)*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,"<"+String.valueOf(Integer.parseInt(extractNumber(yyval.sval)) + 1)+">");/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval);
        }
break;
case 68:
//#line 377 "grammar.y"
{   /* NO CONTEMPLA USAR TIPOS DEFINIDOS POR USUARIOOOOOOO*/
                /* ambas expr chequear si son pair, que sean acceso (O CHEQUAR EN GETDECLARED?)*/
                String t_subtype1 = chkAndGetType(val_peek(2).sval);
                String id1 = val_peek(2).sval;
                if (AnalizadorLexico.t_simbolos.get_entry(id1)!=null && AnalizadorLexico.t_simbolos.get_entry(id1).getTipo().equals("ID")) id1 = getDeclared(id1);
                String t_subtype2 = chkAndGetType(val_peek(0).sval);
                String id2 = val_peek(0).sval;
                if (AnalizadorLexico.t_simbolos.get_entry(id2)!=null && AnalizadorLexico.t_simbolos.get_entry(id2).getTipo().equals("ID")) id2 = getDeclared(id2);
                /* compatibilidades y conversiones:*/
                if (t_subtype1 != null && t_subtype2 != null) {
                        if (t_subtype1.equals(t_subtype2)){
                                yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2,t_subtype1);       /* NO SE SI HACE FALTA TIPO PERO PORLASDUDAS POR AHORA LO PONGO*/
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
                } else {/*System.out.println("Un tipo dio null en condicion");*/}
                
        }
break;
case 69:
//#line 405 "grammar.y"
{yyerror("Se esperaba comparador.") ; }
break;
case 76:
//#line 418 "grammar.y"
{
                yyval.sval = chkAndAssign(val_peek(2).sval,val_peek(0).sval);
                }
break;
case 77:
//#line 422 "grammar.y"
{
                yyval.sval = chkAndAssign(val_peek(2).sval,val_peek(0).sval);
        }
break;
case 78:
//#line 425 "grammar.y"
{yyerror("No se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 79:
//#line 431 "grammar.y"
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
                                        yyval.sval= Terceto.addTercetoT("SUMA",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("SUMA",Terceto.getLast(),id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("SUMA",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en suma");*/}
}
break;
case 80:
//#line 457 "grammar.y"
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
                                        yyval.sval= Terceto.addTercetoT("RESTA",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("RESTA",Terceto.getLast(),id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("RESTA",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en resta");*/}

        }
break;
case 82:
//#line 483 "grammar.y"
{yyerror("Sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 83:
//#line 487 "grammar.y"
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
                                        yyval.sval= Terceto.addTercetoT("MUL",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("MUL",Terceto.getLast(),id2, "SINGLE");
                        } else {yyval.sval= Terceto.addTercetoT("MUL",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en multiplicacion");*/}
        }
break;
case 84:
//#line 511 "grammar.y"
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
                                        yyval.sval= Terceto.addTercetoT("DIV",id1,Terceto.getLast(), "SINGLE");
                        }else if (t_subtype2.equals("SINGLE")){
                                        yyval.sval= Terceto.addTercetoT("utos",id1,null, t_subtype1);
                                        yyval.sval= Terceto.addTercetoT("DIV",Terceto.getLast(),id2, "SINGLE");            /* HACER ESTO EN EL RESTO DE OP ARITMETICASSSSSSSSSSSSSSSSSSSSS*/
                        } else {yyval.sval= Terceto.addTercetoT("DIV",id1,id2, "SINGLE");}
                } else {/*System.out.println("Un tipo dio null en division");*/}
        }
break;
case 88:
//#line 541 "grammar.y"
{    /* si cont de CTE es 1, reemplazo esa entrada de la TS por -CTE*/
                /* si es mas de 1, creo nueva entrada con -CTE*/
        if (!AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval).equals("SINGLE")) {yyerror("Se esperaba constante de tipo single. "); }
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
//#line 563 "grammar.y"
{       
                if (isDeclared(val_peek(3).sval)){
                        /* si ID es de un tipo definido (el tipo de ID esta en la tabla de simbolos)*/
                        String lexem = getDeclared(val_peek(3).sval);
                        String baseType = (AnalizadorLexico.t_simbolos.get_subtype(lexem));
                       /*System.out.println("lexem: "+lexem+" baseType: "+baseType);*/
                        if (!AnalizadorLexico.t_simbolos.get_use(baseType).equals("TYPE_NAME")) {
                                yyerror("Se esperaba variable de tipo pair. "); }
                        else {
                                if (!(val_peek(1).sval.equals("1") || val_peek(1).sval.equals("2"))) {
                                yyerror("Se esperaba constante 1 o 2. "); 
                                }
                                else {yyval.sval = val_peek(3).sval+"{"+val_peek(1).sval+"}";}
                        }
                } else {yyerror("Variable de tipo pair no declarada. "); }
        }
break;
case 92:
//#line 582 "grammar.y"
{ 
                /*System.out.println("\n\nEl lexema a buscar: "+$1.sval);*/
                String lexema = getDeclared(val_peek(3).sval);
                if (lexema != null && AnalizadorLexico.t_simbolos.get_use(lexema).equals("FUN_NAME")) {
                        /* CHEQUEAR QUE SI EXPR ES PAIR, SEA CON ACCESO ({})*/
                        /*chequear tipo de parametros*/
                        if (!AnalizadorLexico.t_simbolos.get_value(lexema).equals(chkAndGetType(val_peek(1).sval))) {
                                yyerror("Tipo de parametro real no coincide con el del parametro formal. ");
                        } else {        /* se va pasando el terceto del llamado a la funcion.*/
                        String param = (AnalizadorLexico.t_simbolos.get_entry(val_peek(1).sval) != null && AnalizadorLexico.t_simbolos.get_entry(val_peek(1).sval).getTipo().equals("ID")) ? getDeclared(val_peek(1).sval) : val_peek(1).sval;
                        yyval.sval = Terceto.addTercetoT("CALL_FUN", lexema, param, AnalizadorLexico.t_simbolos.get_subtype(lexema));}
                } else {
                        yyerror(val_peek(3).sval+" no es una funcion o no esta al alcance. ");
                }
        }
break;
case 93:
//#line 597 "grammar.y"
{yyerror("Sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion. ") ; }
break;
case 94:
//#line 601 "grammar.y"
{   /*expr puede  VARIBLE, CTE, funcion, terceto(varaux),*/
                /* si es ID o funcion o exprpair se pasa con scope*/
                /* CHEQUEAR LA EXPR SEA VALIDA, ES DECIR SI ES VARIABLE O FUNCIOn, QUE ESTE DECLARADO*/
                /* y si es pair pasarlo bien*/
               /*System.out.println("scope actual: "+actualScope);*/
                String lexem = val_peek(1).sval;
                String pos = "";
                if (!isTerceto(lexem) && (!isCte(lexem)) && (!isCharch(lexem))){
                        /* es variable o funcion*/
                        if (isPairAccess(lexem)) {
                                pos = lexem.substring(lexem.lastIndexOf("{"),lexem.lastIndexOf("}") + 1);
                               /*System.out.println("pos: "+pos);*/
                                lexem = getDeclared(getPairName(lexem)) + pos;
                        } else {
                                /* TODO chequear no sea una variable pair sin los {} !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
                                lexem = getDeclared(lexem);
                        }
                }
                yyval.sval = Terceto.addTercetoT("OUTF",lexem,null,chkAndGetType(val_peek(1).sval));
        }
break;
case 95:
//#line 621 "grammar.y"
{
                yyval.sval = Terceto.addTercetoT("OUTF",val_peek(1).sval,null,"CHARCH");
        }
break;
case 96:
//#line 624 "grammar.y"
{yyerror("Se esperaba parametro en OUTF. "); }
break;
case 97:
//#line 625 "grammar.y"
{yyerror("Parametro incorrecto en OUTF. "); }
break;
case 98:
//#line 645 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1).sval,val_peek(6).sval);
                /* si use pila: $$.sval = Terceto.addTerceto("BF",$6.sval,UntilStack.pop());*/
        }
break;
case 99:
//#line 649 "grammar.y"
{yyerror("Se esperaba que la condicion este entre parentesis. "); }
break;
case 100:
//#line 650 "grammar.y"
{yyerror("Se esperaba ')' luego de la condicion UNTIL. "); }
break;
case 101:
//#line 651 "grammar.y"
{yyerror("Se esperaba '(' antes de la condicion UNTIL. "); }
break;
case 102:
//#line 653 "grammar.y"
{yyerror("Se esperaba cuerpo de REPEAT-UNTIL."); }
break;
case 103:
//#line 654 "grammar.y"
{yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y que la condicion este entre parentesis. "); }
break;
case 104:
//#line 655 "grammar.y"
{yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y ')' luego de la condicion. "); }
break;
case 105:
//#line 656 "grammar.y"
{yyerror("Se esperaba cuerpo de REPEAT-UNTIL, y'(' antes de la condicion. "); }
break;
case 106:
//#line 657 "grammar.y"
{yyerror("Se esperaba UNTIL luego de 'END'."); }
break;
case 107:
//#line 658 "grammar.y"
{yyerror("Se esperaba condicion luego de UNTIL."); }
break;
case 108:
//#line 663 "grammar.y"
{
                /* opcion 1: pila:*/
                /*UntilStack.push(Terceto.getTercetoCount());     //apilo prox terceto (porque empieza en 0 los id de lista.)*/
                /* opcion 2:*/
                yyval.sval = strToTID(Terceto.getTercetoCount());  /*paso id del proximo terceto*/
                Terceto.addTerceto("LABEL_TAG","labelt_"+Terceto.getTercetoCount(),null); /* pone un label al inicio de la esctructura*/
        }
break;
case 109:
//#line 673 "grammar.y"
{
                String[] idList = val_peek(2).sval.split(",");
                String[] exprList = val_peek(0).sval.split(",");
                /* si hay mas expr que id, esas expr se descartan.*/
                /* en ambos casos se informa warning*/
                if (idList.length > exprList.length){
                /* si hay mas ids que expr, las ids sobrantes se las asigna 0.*/
                        for (int i = 0; i < exprList.length; i++){
                                yyval.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                        /* agrego a 0 a las TS*/
                        AnalizadorLexico.t_simbolos.add_entry("0","CTE","UINTEGER");
                        for (int i = exprList.length; i < idList.length; i++){
                                yyval.sval = chkAndAssign(idList[i],"0");
                        }
                        new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es mayor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
                } else if (idList.length < exprList.length){
                        for (int i = 0; i < idList.length; i++){
                                yyval.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                        new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es menor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
                }
                else {  
                        for (int i = 0; i < idList.length; i++){
                                yyval.sval = chkAndAssign(idList[i],exprList[i]);
                        }
                }
        }
break;
case 110:
//#line 701 "grammar.y"
{
                String[] exprList = val_peek(0).sval.split(",");
                yyval.sval = chkAndAssign(val_peek(2).sval,exprList[0]);
                new Error(AnalizadorLexico.line_number,"La cantidad de elementos del lado izquierdo de la asignacion, es menor a la cantidad de elementos del lado derecho.",false,"SEMANTICO");
        }
break;
case 111:
//#line 706 "grammar.y"
{yyerror("La lista de expresiones es incorrecta, puede que falte ',' entre las expresiones. ") ; }
break;
case 112:
//#line 711 "grammar.y"
{
                
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;

        }
break;
case 113:
//#line 716 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
        }
break;
case 116:
//#line 730 "grammar.y"
{
                yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
        }
break;
case 117:
//#line 734 "grammar.y"
{
                yyval.sval = yyval.sval + ',' + val_peek(0).sval;
                
        }
break;
case 118:
//#line 742 "grammar.y"
{
                /* buscar si no hay otra tag con el mismo nombre al alcance*/
               /*System.out.println("wtf");*/
                if (!isDeclaredLocal(val_peek(0).sval)) {
                        /* que no empiece con s,u,v,w*/
                        if (val_peek(0).sval.charAt(0) == 's' || val_peek(0).sval.charAt(0) == 'u' || val_peek(0).sval.charAt(0) == 'v' || val_peek(0).sval.charAt(0) == 'w') {
                                yyerror("El nombre de la etiqueta no puede comenzar con 's', 'u', 'v' o 'w'. ");
                        } else {

                                /* reinserción en la T. de S. con scope actual*/
                                AnalizadorLexico.t_simbolos.del_entry(val_peek(0).sval);
                                AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"TAG","","tag_name","");
                                /* agregar a la tabla de etiquetas*/
                                TablaEtiquetas.add_tag(val_peek(0).sval); 
                                Terceto.addTerceto("LABEL_TAG",val_peek(0).sval+":"+actualScope,null);
                        }
                } else yyerror("La etiqueta "+val_peek(0).sval+" esta siendo redeclarada. Ya fue declarada en el scope actual. ");
                /* AnalizadorLexico.t_simbolos.display();*/
        }
break;
case 119:
//#line 768 "grammar.y"
{
                /*if existe en TS {*/
                        yyval.sval= Terceto.addTerceto("JUMP_TAG",null,null);      /*se pone terceto incompleto, se completara al final del programa*/
                        TablaEtiquetas.add_goto(val_peek(0).sval,Terceto.parseTercetoId(yyval.sval),AnalizadorLexico.line_number);     /* donde puse 0 iría número de línea en lo posible*/
                /*}*/
        }
break;
case 120:
//#line 774 "grammar.y"
{yyerror("Se esperaba TAG en la sentencia GOTO."); }
break;
//#line 1924 "Parser.java"
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

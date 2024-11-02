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
   17,    9,    9,    9,    9,    9,    9,   23,   23,   23,
   23,   24,   24,   25,   26,   26,   27,   27,   27,   27,
   27,   27,   10,   10,   10,   22,   22,   22,   22,   29,
   29,   29,   30,   30,   30,   30,   30,   30,   30,   28,
   31,   31,   11,   11,   11,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   14,   14,   32,   32,   34,
   34,   33,   33,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    3,    7,    7,    7,    6,    5,    4,    6,
    3,    3,    2,    1,    2,    4,    2,    1,    1,    1,
    1,    3,    3,    5,    4,    5,    5,    4,    2,    3,
    3,    2,    2,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    4,    3,    3,    1,    1,    3,
    3,    1,    1,    1,    2,    2,    1,    1,    1,    4,
    4,    5,    4,    3,    2,    8,    6,    7,    7,    7,
    5,    6,    6,    7,    7,    3,    3,    3,    3,    1,
    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   49,   50,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   14,    0,    0,   17,
   26,    0,    0,   84,   89,    0,    0,    0,    0,   88,
    0,   82,   87,    0,   95,    0,    0,    0,   79,    0,
    0,    0,  115,  114,    4,    9,   15,   11,   19,   20,
   21,   22,   23,   24,   25,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    2,    0,    0,    0,
    0,   86,   85,   70,   71,   72,   67,   68,   69,    0,
    0,    0,   61,    0,    0,    0,    1,   94,    0,   51,
    0,    0,    0,    0,    0,   27,    0,    0,   32,    0,
   31,    0,    0,    0,   30,   29,    0,   33,   63,    0,
   62,   53,    0,   52,    0,    0,    0,    0,    0,  111,
  109,  108,   90,    0,   58,    0,    0,    0,   80,   81,
   93,    0,   39,    0,   46,    0,    0,   28,    0,    0,
   41,    0,    0,   42,    0,   55,    0,    0,    0,    0,
   91,    0,   38,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,   57,   54,    0,    0,   92,   40,   37,
    0,  103,    0,    0,    0,   45,   43,    0,    0,    0,
  100,  105,    0,   99,  104,   35,   36,   34,   96,
};
final static short yydgoto[] = {                          3,
   17,   18,   19,   20,   68,   21,   22,   41,   23,   24,
   25,   26,   27,   28,   29,  130,   30,   78,   31,   80,
  180,   48,   32,   82,  131,   49,  102,   50,   51,   52,
   53,   34,  138,   35,
};
final static short yysindex[] = {                      -238,
 -259,  -74,    0,  617,  -44, -118,  -31,  -38,  617,  -34,
  -56,  -23,    0,    0, -247, -199,  523,    0,    0,  -25,
    0,    0,  -25,  -25,  -25,  -25,  -25,  -25,  -25, -243,
  617, -194, -187,  -33,   43,  565,    0,  121, -169,    0,
    0,    0,  -28,    0,    0,  167, -151,  -35,   50,    0,
   55,    0,    0,  591,    0,   -4,  -47, -206,    0,  121,
   -5,  682,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -12, -131,   61,  617, -152,
  661, -143,  121,  241, -138, -138,    0,   -5,   -2,  121,
   97,    0,    0,    0,    0,    0,    0,    0,    0,  262,
  262,  121,    0,  262,  262,  -44,    0,    0,  101,    0,
 -206, -128,   81,  433, -130,    0,  703, -103,    0,  121,
    0,  -98,  124,  128,    0,    0,  -79,    0,    0,  724,
    0,    0,  639,    0,   -5,    0,  108,  141,   66,    0,
    0,    0,    0,   13,    0,   55,   55,   -5,    0,    0,
    0,  142,    0,  -52,    0,  -16,  -40,    0,  -55,   -5,
    0, -154, -176,    0,  -63,    0, -197,  121,  121,  170,
    0, -121,    0,  167,  172,   -9,  167,    0,  -84,  173,
  176,  204,    0,    0,    0,   -5,   -5,    0,    0,    0,
  219,    0,  143,  223,  263,    0,    0,   14,   40,   44,
    0,    0,  289,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       256,
  334,    0,    0,    0,    0,  -41,  259,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  339,    0,    0,  496,
    0,    0,  468,  468,  468,  468,  468,  468,  468,    0,
    0,    0,  305,    0,    0,    0,    0,    0,    0,    0,
    0,   96,    1,    0,    0,    0,    0,    0,   89,    0,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  215,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   85,    0,
    0,    0,    0,    0,    0,    0,    0,  237,    0,    0,
   93,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  364,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -96,
    0,    0,    0,    0,  291,  118,    0,  322,  -24,    0,
    0,    0,    0,    0,    0,   49,   71,  140,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  344,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  369,    0,    0,   45,    0,    0,
    0,    0,    0,    0,    0,  165,  187,    0,    0,    0,
  391,    0,    0,  418,    0,    0,    0,    0,    0,    0,
    0,    0,  446,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   46,   -1,  -29,    0,  517,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  324,   17,    0,    0,    0,
  221,  594,    0,    0,  275,   -7,    0,  747,   75,   88,
    0,    0,    0,  110,
};
final static int YYTABLESIZE=1004;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        177,
   83,   46,  110,   58,   39,   56,   47,  100,    4,  101,
   85,   90,  111,   76,   37,   66,   60,    1,    2,  110,
   62,   47,   78,  174,   97,   99,   98,   40,   47,   77,
  193,  122,  116,   67,   66,   47,  108,  100,   91,  101,
   47,   83,   83,   83,   83,   83,  121,   83,   76,   36,
  110,  116,   66,  171,   54,  100,   63,  101,  184,   83,
   83,   83,   83,   78,   64,   78,   78,   78,   13,   14,
   77,   81,  185,  112,  113,   83,   79,   66,  118,  181,
  178,   78,   78,   78,   78,   44,   86,  158,   89,   76,
  103,   76,   76,   76,   39,   66,  104,  118,   13,   14,
  158,  105,  178,  116,  127,   92,   93,   76,   76,   76,
   76,   77,  132,   77,   77,   77,  128,  107,  139,  126,
   13,   14,  143,  133,  123,  124,  134,  152,  153,   77,
   77,   77,   77,  118,  189,  190,   66,  145,   79,   65,
   79,  151,  154,  100,   38,  101,  118,  156,  175,  118,
  100,  168,  101,  159,   66,   79,   79,   79,  161,   64,
   79,   79,   79,  162,  112,   47,  191,  163,  194,  195,
   64,  196,  197,   64,  146,  147,  107,  164,  179,  179,
   65,    5,    6,  202,  169,  203,  113,   47,   39,    7,
    8,  149,  150,    9,  141,  142,   10,   11,   65,   12,
   13,   14,   15,  172,  173,   16,  183,  120,  112,  110,
  188,   47,  192,  198,   47,   51,  199,   42,   43,   44,
   45,   55,   57,  112,   94,   95,   96,   13,   14,   84,
  113,   51,   59,   43,   44,   45,   73,  176,  110,   42,
   43,   44,   45,  119,  200,  113,   42,   43,   44,   45,
  120,   59,   43,   44,   45,    7,   83,   83,   18,  201,
   83,   83,   83,  204,   83,   83,   83,   83,  170,   83,
   83,   83,   83,   47,   83,   83,   83,   83,   78,   78,
   83,  206,   78,   78,   78,   47,   78,   78,   78,   78,
   74,   78,   78,   78,   78,   73,   78,   78,   78,   78,
   51,   51,   78,  205,   76,   76,   47,  207,   76,   76,
   76,  208,   76,   76,   76,   76,  125,   76,   76,   76,
   76,  106,   76,   76,   76,   76,   77,   77,   76,  209,
   77,   77,   77,    3,   77,   77,   77,   77,    6,   77,
   77,   77,   77,   75,   77,   77,   77,   77,  111,   74,
   77,   66,   66,   48,   59,   79,   79,   79,   60,   66,
   66,   66,   66,    5,   66,   66,   66,   66,  101,   66,
   66,   66,   66,  107,  107,   66,   59,   43,   44,   45,
  106,  107,  107,  182,  107,  117,  107,  107,  107,  107,
  102,  107,  107,  107,  107,   65,   65,  107,   42,   43,
   44,   45,   75,   65,   65,   65,   65,  167,   65,   65,
   65,   65,    0,   65,   65,   65,   65,   97,    0,   65,
  112,  112,   42,   43,   44,   45,    0,  101,  112,  112,
    0,  112,    0,  112,  112,  112,  112,    0,  112,  112,
  112,  112,  113,  113,  112,   98,    0,    0,    0,  102,
  113,  113,    0,  113,    0,  113,  113,  113,  113,    0,
  113,  113,  113,  113,    0,    0,  113,   16,    0,    0,
   47,   47,    0,  155,    0,  100,   97,  101,   47,   47,
    0,   47,    0,   47,   47,   47,   47,    0,   47,   47,
   47,   47,   73,   73,   47,   16,  136,   43,   44,   45,
   73,   73,    0,   73,   98,   73,   73,   73,   73,    0,
   73,   73,   73,   73,   18,   18,   73,    0,   43,   44,
   45,    0,   18,   18,    0,   18,    0,   18,   18,   18,
   18,    0,   18,   18,   18,   18,    0,    0,   18,   69,
   70,   71,   72,   73,   74,   75,   74,   74,    0,    0,
    0,    0,    0,    0,   74,   74,    0,   74,    0,   74,
   74,   74,   74,    0,   74,   74,   74,   74,    0,    0,
   74,    0,    0,    0,    0,    0,    0,  106,  106,    0,
    0,    0,    0,    0,    0,  106,  106,    0,  106,    0,
  106,  106,  106,  106,    0,  106,  106,  106,  106,   75,
   75,  106,    0,    0,    0,   61,    0,   75,   75,    0,
   75,    0,   75,   75,   75,   75,    0,   75,   75,   75,
   75,    0,    0,   75,  101,  101,    0,    0,    0,    0,
    0,   88,  101,  101,    0,  101,    0,  101,  101,  101,
  101,    0,  101,  101,  101,  101,  102,  102,  101,  109,
    0,    0,    0,  114,  102,  102,    0,  102,    0,  102,
  102,  102,  102,    0,  102,  102,  102,  102,    0,    0,
  102,    0,    0,   97,   97,    0,  135,  137,    0,    0,
    0,   97,   97,  144,   97,    0,   97,   97,   97,   97,
    0,   97,   97,   97,   97,  148,    0,   97,    0,    0,
    0,   98,   98,    0,    0,    0,    0,    0,    0,   98,
   98,    0,   98,  160,   98,   98,   98,   98,    0,   98,
   98,   98,   98,   16,   16,   98,    0,    0,    0,    0,
    0,   16,   16,    0,   16,    0,   16,   16,   16,   16,
    0,   16,   16,   16,   16,    0,    0,   16,   33,    0,
   33,   16,   16,    0,    0,   33,    0,    0,    0,   16,
   16,  186,  187,   33,   16,    0,   16,   16,    0,   16,
   16,   16,   16,    0,    0,   16,    0,   33,    5,    6,
    0,    0,   33,    0,    0,    0,    7,    8,    0,    0,
    0,   65,    0,   10,   11,    0,   12,   13,   14,   15,
   33,    0,   16,    0,    0,    0,    0,    0,   33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    5,    6,    0,    0,    0,   33,    0,   33,    7,    8,
    0,  140,  140,   87,    0,   10,   11,    0,   12,   13,
   14,   15,    0,    0,   16,    0,  106,    6,    0,    0,
    0,    0,    0,    0,    7,    8,    0,    0,    0,  107,
    0,   10,   11,   33,   12,   13,   14,   15,    0,    0,
   16,    0,    5,    6,    0,    0,   33,    0,    0,   33,
    7,    8,    0,    0,    0,    0,    0,   10,   11,    0,
   12,   13,   14,   15,  165,    6,   16,    0,    0,    0,
    0,    0,    7,    8,    0,    0,    0,    0,  166,   10,
    0,    0,   12,   13,   14,   15,  129,    6,   16,    0,
    0,    0,    0,    0,    7,    8,    0,    0,    0,    0,
    0,   10,    0,    0,   12,   13,   14,   15,    6,    0,
   16,    0,    0,    0,    0,    7,    8,    0,    0,    0,
  115,    0,   10,    0,    0,   12,   13,   14,   15,    6,
    0,   16,    0,    0,    0,    0,    7,    8,    0,    0,
    0,  157,    0,   10,    0,    0,   12,   13,   14,   15,
    6,    0,   16,    0,    0,    0,    0,    7,    8,    0,
    0,    0,    0,    0,   10,    0,    0,   12,   13,   14,
   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,   60,  123,   40,   45,   43,  268,   45,
   44,   40,   60,  257,   59,   17,   40,  256,  257,   44,
  268,   45,    0,   40,   60,   61,   62,   59,   45,  273,
   40,   44,   62,   59,   36,   45,   41,   43,   46,   45,
   45,   41,   42,   43,   44,   45,   59,   47,    0,    4,
  257,   81,   54,   41,    9,   43,  256,   45,  256,   59,
   60,   61,   62,   41,  264,   43,   44,   45,  275,  276,
    0,  266,  270,   57,   58,  263,   31,   79,   62,  256,
  257,   59,   60,   61,   62,   41,   44,  117,  258,   41,
   41,   43,   44,   45,  123,    0,   42,   81,  275,  276,
  130,   47,  257,  133,   44,  257,  258,   59,   60,   61,
   62,   41,  256,   43,   44,   45,  269,    0,  257,   59,
  275,  276,  125,  267,  256,  257,  270,  111,  257,   59,
   60,   61,   62,  117,  256,  257,   41,   41,   43,    0,
   45,   41,   62,   43,  263,   45,  130,  278,  156,  133,
   43,   44,   45,  257,   59,   60,   61,   62,  257,  256,
   43,   44,   45,   40,    0,   45,  174,   40,  176,  177,
  267,  256,  257,  270,  100,  101,   59,  257,  162,  163,
   41,  256,  257,   41,   44,  193,    0,   45,  123,  264,
  265,  104,  105,  268,   85,   86,  271,  272,   59,  274,
  275,  276,  277,   62,  257,  280,  270,  263,   44,  257,
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
  262,  268,  264,  265,  266,  267,  256,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,   41,
  260,  261,  262,    0,  264,  265,  266,  267,    0,  269,
  270,  271,  272,    0,  274,  275,  276,  277,   44,   59,
  280,  256,  257,  269,  266,  260,  261,  262,  266,  264,
  265,  266,  267,    0,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,  163,  267,   62,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,  133,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,    0,   -1,  280,
  256,  257,  256,  257,  258,  259,   -1,   59,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,   -1,   -1,   -1,   59,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   -1,   -1,  280,    0,   -1,   -1,
  256,  257,   -1,   41,   -1,   43,   59,   45,  264,  265,
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
  257,  280,   -1,   -1,   -1,   12,   -1,  264,  265,   -1,
  267,   -1,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,   -1,   -1,  280,  256,  257,   -1,   -1,   -1,   -1,
   -1,   38,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,  256,  257,  280,   56,
   -1,   -1,   -1,   60,  264,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,   -1,  256,  257,   -1,   83,   84,   -1,   -1,
   -1,  264,  265,   90,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,  102,   -1,  280,   -1,   -1,
   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,
  265,   -1,  267,  120,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,   -1,  280,    2,   -1,
    4,  256,  257,   -1,   -1,    9,   -1,   -1,   -1,  264,
  265,  168,  169,   17,  269,   -1,  271,  272,   -1,  274,
  275,  276,  277,   -1,   -1,  280,   -1,   31,  256,  257,
   -1,   -1,   36,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
   54,   -1,  280,   -1,   -1,   -1,   -1,   -1,   62,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,   -1,   -1,   -1,   79,   -1,   81,  264,  265,
   -1,   85,   86,  269,   -1,  271,  272,   -1,  274,  275,
  276,  277,   -1,   -1,  280,   -1,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,  269,
   -1,  271,  272,  117,  274,  275,  276,  277,   -1,   -1,
  280,   -1,  256,  257,   -1,   -1,  130,   -1,   -1,  133,
  264,  265,   -1,   -1,   -1,   -1,   -1,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,  270,  271,
   -1,   -1,  274,  275,  276,  277,  256,  257,  280,   -1,
   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,
   -1,  271,   -1,   -1,  274,  275,  276,  277,  257,   -1,
  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,  257,
   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,
  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,   -1,   -1,  271,   -1,   -1,  274,  275,  276,
  277,   -1,   -1,  280,
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
"if_statement : if_cond then_statement END_IF",
"if_statement : if_cond then_statement error",
"if_statement : if_cond then_statement ELSE ctrl_block_statement END_IF",
"if_statement : if_cond then_statement ELSE END_IF",
"if_statement : if_cond then_statement ELSE error END_IF",
"if_statement : if_cond then_statement ELSE ctrl_block_statement error",
"if_cond : IF '(' cond ')'",
"if_cond : IF cond",
"if_cond : IF '(' cond",
"if_cond : IF cond ')'",
"then_statement : THEN ctrl_block_statement",
"then_statement : THEN error",
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

//#line 439 "grammar.y"
        public ArrayList<String> errores = new ArrayList<String>();
        private String actualScope = "main";

	public static void yyerror(String msg){
                errores.add(msg);
	        System.out.println("Error en linea "+AnalizadorLexico.line_number+": "+msg);
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

        public void chkAndGetTerc(ParseVal varParser, String t_subtype1, String id1){
                if isTerceto(varParser.sval) {
                        t_subtype1 = varParser.getSubtipo(varParser.sval);
                        id1 = varParser.sval;
                        }
                else {
                        t_subtype1 = AnalizadorLexico.t_simbolos.get_subtype(varParser.sval);
                        id1 = strToTID(varParser.sval);
                        }
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

//#line 657 "Parser.java"
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
case 29:
//#line 83 "grammar.y"
{/*agregar a cada variable (separada por ',') su tipo, scope,uso*/

}
break;
case 30:
//#line 86 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");
                                /* agregar a cada variable (separar por ',') su tipo,scope,uso*/
                                }
break;
case 31:
//#line 89 "grammar.y"
{
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
        }
break;
case 32:
//#line 95 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");
                chkAndDeclareVar(val_peek(2).sval, val_peek(1).sval);
                }
break;
case 33:
//#line 102 "grammar.y"
{
                popScope();
        }
break;
case 34:
//#line 109 "grammar.y"
{
                if (!AnalizadorSemantico.validID(val_peek(6).sval,val_peek(4).sval)) {
                        yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                /* guardar el scope de la funcion, en la tabla*/
                pushScope(val_peek(4).sval); 
        }
break;
case 35:
//#line 115 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ;}
break;
case 36:
//#line 117 "grammar.y"
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
//#line 128 "grammar.y"
{
                /* OJO ESTO ES UN TIPO, NO UN NOMBRE DE VARIABLE.. */
                if (!AnalizadorSemantico.validID(val_peek(2).sval,val_peek(0).sval)) {yyerror("Los identificadores que comienzan con 's' se reservan para variables de tipo single. Los que comienzan con 'u','v','w' están reservados para variables de tipo uinteger. ");}
                /*if (isRedeclared){error de redeclaracion?} si coincide el tipo con nombre de otra variable da error? o solo si coincide con otro tipo?*/
                AnalizadorLexico.t_simbolos.add_entry(val_peek(0).sval+":"+actualScope,"ID","pair");
                AnalizadorLexico.t_simbolos.set_use(val_peek(0).sval+":"+actualScope,"type_name");
                /* falta agregar var_type*/
                /* observar que el tipo definido por el usuario tambien tiene alcance.*/
        }
break;
case 38:
//#line 137 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 39:
//#line 138 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 40:
//#line 139 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 44:
//#line 156 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 45:
//#line 157 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 47:
//#line 162 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 52:
//#line 206 "grammar.y"
{
        /* si no hay else, hay un terceto de salto menos.*/
        /* entonces aca no se hace nada, que el programa siga.*/
}
break;
case 53:
//#line 210 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 54:
//#line 212 "grammar.y"
{
        /* completo el terceto*/
        Terceto.completeTerceto(Terceto.popTerceto(),yyval.sval,null); /* creo seria $$.sval+1*/
}
break;
case 55:
//#line 216 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 56:
//#line 217 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
break;
case 57:
//#line 218 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 58:
//#line 222 "grammar.y"
{
                yyval.sval = Terceto.addTerceto("BF",val_peek(1),null)
                Terceto.pushTerceto(yyval.sval) /*apilo terceto incompleto.*/
        }
break;
case 59:
//#line 226 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 60:
//#line 227 "grammar.y"
{
                yyerror("Error en linea "+AnalizadorLexico.line_number +": se esperaba ')' luego de la condicion"); }
break;
case 61:
//#line 229 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 62:
//#line 233 "grammar.y"
{
                /* completo el terceto*/
                yyval.sval = Terceto.addTercetoT("BI",null,null,null); /*incompleto, segundo componente se completara despues.*/
                Terceto.completeTerceto(Terceto.popTerceto(),null,yyval.sval);/*creo seria $$.sval + 1 (pasar a int y luego volver a string)*/
                Terceto.pushTerceto(yyval.sval); 
        }
break;
case 63:
//#line 240 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 65:
//#line 248 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                yyval.sval=Terceto.addTercetoT(val_peek(1).sval,id1,id2, null);
                /* compatiblidades de comparacion??*/
        }
break;
case 66:
//#line 258 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 73:
//#line 271 "grammar.y"
{
                /*chequear id exista*/
                /* chequear tipos sean el mismo? o compatiblidad y conversion implcita*/
                /*crear terceto*/
        }
break;
case 74:
//#line 276 "grammar.y"
{
                /*chequear id exista, sea tipo pair*/
                /* chequear tipos*/
                /*crear terceto*/
                
        }
break;
case 75:
//#line 282 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 76:
//#line 285 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                /*deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)*/
                if (isCompatible(t_subtype1,t_subtype2)){
                        yyval.sval= Terceto.addTercetoT("+",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
}
break;
case 77:
//#line 297 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                /*deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)*/
                if (isCompatible(t_subtype1,t_subtype2)){
                        yyval.sval= Terceto.addTercetoT("+",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }}
break;
case 79:
//#line 309 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 80:
//#line 313 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                /*deducir tipo de terceto resultante (es implicito, usar logica de AnalizadorSemantico)*/
                if (isCompatible(t_subtype1,t_subtype2)){
                        yyval.sval=Terceto.addTercetoT("*",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+AnalizadorLexico.line_number+": tipos incompatibles en multiplicacion. "); }
                
}
break;
case 81:
//#line 326 "grammar.y"
{
                String t_subtype1;
                String id1;
                String t_subtype2;
                String id2;
                chkAndGetTerc(val_peek(2),t_subtype1,id1);
                chkAndGetTerc(val_peek(0),t_subtype2,id2);
                if (isCompatible(t_subtype1,t_subtype2)){
                        /* CONVERSION IMPLICTA DENTRO DE isCompatible*/
                        yyval.sval=Terceto.addTercetoT("*",id1,id2, t_subtype);}
                else{yyerror("Error en linea "+ AnalizadorLexico.line_number+" :tipos incompatibles en division"); }
                }
break;
case 85:
//#line 344 "grammar.y"
{ 
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",strToTID(val_peek(0).sval),AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 86:
//#line 349 "grammar.y"
{
                if (AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval) != "SINGLE") {yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba constante de tipo single. "); }
                else {
                        yyval.sval=Terceto.addTercetoT("-","0",strToTID(val_peek(0).sval),AnalizadorLexico.t_simbolos.get_subtype(val_peek(0).sval));}
                }
break;
case 90:
//#line 363 "grammar.y"
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
case 92:
//#line 376 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 94:
//#line 381 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 95:
//#line 382 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 97:
//#line 390 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 98:
//#line 391 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 99:
//#line 392 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 100:
//#line 394 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 101:
//#line 395 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 102:
//#line 396 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 103:
//#line 397 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 104:
//#line 398 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 105:
//#line 399 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 107:
//#line 405 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 115:
//#line 434 "grammar.y"
{yyerror("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1229 "Parser.java"
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

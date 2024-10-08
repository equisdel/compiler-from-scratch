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
    6,    6,    7,    7,    7,    4,    4,    4,    4,   18,
   18,   19,   19,   19,   15,   15,   20,   17,   17,   17,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   23,   22,   22,
   24,   24,   24,   24,   24,   24,   10,   10,   10,   21,
   21,   21,   21,   26,   26,   26,   27,   27,   27,   27,
   27,   27,   27,   25,   28,   28,   11,   11,   11,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   14,
   14,   29,   29,   31,   31,   30,   30,   13,   13,
};
final static short yylen[] = {                            2,
    4,    4,    1,    3,    4,    2,    0,    1,    2,    1,
    2,    1,    1,    2,    1,    0,    1,    0,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    2,    3,    3,
    3,    3,    9,    9,    9,    6,    5,    4,    6,    3,
    3,    2,    1,    2,    4,    2,    1,    1,    1,    1,
    7,    5,    6,    6,    7,    6,    7,    9,    7,    8,
    8,    9,    9,    7,    8,    8,    9,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    3,    3,    4,    3,
    3,    1,    1,    3,    3,    1,    1,    1,    2,    2,
    1,    1,    1,    4,    4,    5,    4,    3,    2,    8,
    6,    7,    7,    7,    5,    6,    6,    7,    7,    3,
    3,    3,    3,    1,    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   48,   49,    0,    0,    0,    8,   10,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   14,    0,    0,   17,   26,    0,
    0,   88,   93,    0,    0,    0,    0,   92,    0,   86,
   91,    0,   99,    0,    0,    0,   83,    0,    0,    0,
  119,  118,    4,    9,   15,   11,   19,   20,   21,   22,
   23,   24,   25,    0,    0,    0,    0,    0,    0,    0,
    2,    0,    0,    0,    0,   90,   89,   74,   75,   76,
   71,   72,   73,    0,    0,    0,    0,    0,    0,    0,
    0,    1,   98,    0,   50,    0,    0,    0,    0,    0,
   27,    0,    0,   32,    0,   31,    0,    0,    0,   30,
   29,    0,    0,    0,    0,    0,    0,  115,  113,  112,
   94,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   84,   85,   97,    0,   38,    0,   45,    0,    0,   28,
    0,    0,   40,    0,    0,   41,    0,    0,    0,   95,
    0,    0,    0,   52,    0,    0,   37,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   96,    0,
   53,    0,    0,   56,    0,    0,    0,   54,   39,   36,
    0,  107,    0,    0,    0,   44,   42,    0,    0,    0,
    0,    0,   57,   59,    0,   55,    0,   51,   64,    0,
  104,  109,    0,  103,  108,    0,    0,    0,   65,    0,
   60,    0,   61,    0,   66,  100,    0,    0,    0,    0,
   63,   62,   67,   58,   34,   35,   33,
};
final static short yydgoto[] = {                          3,
  227,   18,  111,   20,   66,   21,   22,   39,   23,   24,
   25,   26,   27,   28,   29,  138,  113,   76,  174,  228,
   46,   47,  139,   96,   31,   49,   50,   51,   32,  126,
   33,
};
final static short yysindex[] = {                      -202,
 -241,  599,    0,  740,    6, -115,   15,   -9,  740,  -30,
  -54,  143,    0,    0, -209, -239,  629,    0,    0,   32,
    0,    0,   32,   32,   32,   32,   32,   32,   32, -244,
 -162,  -35,   73,  674,    0,  121, -129,    0,    0,    0,
  -28,    0,    0,  294, -177,  -21,  -39,    0,  -27,    0,
    0,  696,    0,  241,  -55, -199,    0,  121,  -29,  -79,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   45, -170,   61,  121,  420, -122, -122,
    0,  -29,   21,  121,  -37,    0,    0,    0,    0,    0,
    0,    0,    0,  393,  393,  121,  554, -123,  393,  393,
    6,    0,    0,   93,    0, -199, -107,  102,  106, -108,
    0,  783,  -75,    0,  121,    0,  -73,  149,  151,    0,
    0,  -64,  -29,    0,  109,  150,   77,    0,    0,    0,
    0,  -13,  554,  -63,  -27,  -27,  -29,  554, -217,  554,
    0,    0,    0,  142,    0,  -52,    0,  167,  -40,    0,
  -57,  -29,    0, -178, -219,    0,  121,  121,  169,    0,
 -192,  718,  554,    0, -167, -150,    0,  294,  170,  262,
  294,    0,  -97,  172,  173,  176,  -29,  -29,    0,  554,
    0, -142,  804,    0, -168,  -62,  554,    0,    0,    0,
  177,    0,  499,  178,  195,    0,    0,  -17,  -16,  -15,
  -14,  554,    0,    0,  -10,    0,  762,    0,    0,   -6,
    0,    0,  201,    0,    0,  740,  740,  740,    0,   -1,
    0,   34,    0, -223,    0,    0,  740,   43,   65,   80,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       254,
  255,    0,    0,    0,    0,  -41,  259,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  330,    0,    0,  496,
    0,    0,  468,  468,  468,  468,  468,  468,  468,    0,
  310,    0,    0,    0,    0,    0,    0,    0,    0,   96,
    1,    0,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  215,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  237,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  355,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  291,  118,    0,  322,  -33,    0,    0,    0,
    0,    0,    0,    0,   49,   71,  140, -143,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  344,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  369,    0,
    0,  -34,    0,    0,    0,    0,  165,  187,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  391,    0,    0,  418,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  446,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   90,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  117,   18,  695,    0,  813,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  304,   17,    0,  229,  -49,
  739,  -26,  601,    0,  766,   79,   76,    0,    0,    0,
  100,
};
final static int YYTABLESIZE=1084;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        171,
   87,   98,  114,  134,  106,   56,   43,   37,   79,   54,
  114,   84,   74,   94,   99,   95,   61,   85,   30,  100,
   30,   94,   82,   95,   62,   30,    4,  160,   75,   94,
   44,   95,  233,   30,   64,   45,  175,  172,   91,   93,
   92,   87,   87,   87,   87,   87,  234,   87,   80,  163,
   30,   64,  164,    1,    2,   13,   14,  105,   60,   87,
   87,   87,   87,   82,   35,   82,   82,   82,   30,   64,
   81,  107,  108,   38,  180,   13,   14,  181,  172,   86,
   87,   82,   82,   82,   82,  118,  119,  206,  117,   80,
   65,   80,   80,   80,   37,   70,   13,   14,  207,  187,
   77,  208,  188,  116,  122,  189,  190,   80,   80,   80,
   80,   81,   68,   81,   81,   81,   80,  111,   17,  121,
   34,  169,  144,   68,  202,   52,   68,  203,   83,   81,
   81,   81,   81,  143,  127,   94,   70,   95,   83,   69,
   83,  191,  140,  194,  195,  131,  147,   36,   94,  145,
   95,   94,  157,   95,   70,   83,   83,   83,  196,  197,
   83,   83,   83,  146,  116,   45,  213,  229,  230,  148,
  173,  173,  135,  136,  141,  142,  111,    6,  129,  130,
   69,  151,   58,  153,    7,    8,  117,   45,  154,  110,
  155,   10,  156,  158,   12,   13,   14,   15,   69,   37,
   16,  105,  162,  166,  167,  115,  168,  209,  116,  179,
  192,   45,  198,  199,   46,   50,  200,  211,  214,   13,
   14,   50,   50,  116,   55,   53,   97,   78,  133,  114,
  117,   50,   30,   30,   30,  215,   77,  170,   88,   89,
   90,  226,  159,   30,   64,  117,   40,   41,   42,   43,
  216,  217,  218,    7,    3,  219,   87,   87,   18,  221,
   87,   87,   87,  225,   87,   87,   87,   87,  231,   87,
   87,   87,   87,   46,   87,   87,   87,   87,   82,   82,
   87,  103,   82,   82,   82,   45,   82,   82,   82,   82,
   78,   82,   82,   82,   82,   77,   82,   82,   82,   82,
  114,  193,   82,  232,   80,   80,   45,  115,   80,   80,
   80,  235,   80,   80,   80,   80,  120,   80,   80,   80,
   80,  110,   80,   80,   80,   80,   81,   81,   80,    6,
   81,   81,   81,  236,   81,   81,   81,   81,   45,   81,
   81,   81,   81,   79,   81,   81,   81,   81,  237,   78,
   81,   70,   70,  115,    5,   83,   83,   83,   47,   70,
   70,   70,   70,  112,   70,   70,   70,   70,  105,   70,
   70,   70,   70,  111,  111,   70,   57,   41,   42,   43,
  110,  111,  111,  176,  111,    0,  111,  111,  111,  111,
  106,  111,  111,  111,  111,   69,   69,  111,   57,   41,
   42,   43,   79,   69,   69,   69,   69,    0,   69,   69,
   69,   69,    0,   69,   69,   69,   69,  101,    0,   69,
  116,  116,   40,   41,   42,   43,    0,  105,  116,  116,
    0,  116,    0,  116,  116,  116,  116,   45,  116,  116,
  116,  116,  117,  117,  116,  102,    0,    0,    0,  106,
  117,  117,    0,  117,    0,  117,  117,  117,  117,    0,
  117,  117,  117,  117,   45,    0,  117,   16,    0,    0,
   46,   46,    0,    0,    0,    0,  101,    0,   46,   46,
    0,   46,    0,   46,   46,   46,   46,    0,   46,   46,
   46,   46,   77,   77,   46,   16,   57,   41,   42,   43,
   77,   77,    0,   77,  102,   77,   77,   77,   77,    0,
   77,   77,   77,   77,   18,   18,   77,   40,   41,   42,
   43,    0,   18,   18,    0,   18,    0,   18,   18,   18,
   18,    0,   18,   18,   18,   18,    0,    0,   18,  212,
    0,    0,    0,   45,    0,    0,   78,   78,    0,   40,
   41,   42,   43,    0,   78,   78,    0,   78,    0,   78,
   78,   78,   78,    0,   78,   78,   78,   78,    0,    0,
   78,    0,    0,    0,    0,    0,    0,  110,  110,    0,
    0,    0,    0,    0,    0,  110,  110,    0,  110,    0,
  110,  110,  110,  110,    0,  110,  110,  110,  110,   79,
   79,  110,    0,    0,    0,    0,    0,   79,   79,    0,
   79,    0,   79,   79,   79,   79,    0,   79,   79,   79,
   79,    0,    0,   79,  105,  105,    0,    0,    0,    0,
    0,    0,  105,  105,    0,  105,    0,  105,  105,  105,
  105,    0,  105,  105,  105,  105,  106,  106,  105,   41,
   42,   43,    0,    0,  106,  106,    0,  106,    0,  106,
  106,  106,  106,    0,  106,  106,  106,  106,    0,    0,
  106,    0,    0,  101,  101,  124,   41,   42,   43,    0,
    0,  101,  101,    0,  101,    0,  101,  101,  101,  101,
    0,  101,  101,  101,  101,    0,   19,  101,   19,    0,
    0,  102,  102,   19,    0,    0,    0,    0,    0,  102,
  102,   19,  102,    0,  102,  102,  102,  102,    0,  102,
  102,  102,  102,   16,   16,  102,    0,    0,   19,    0,
    0,   16,   16,  161,   16,    0,   16,   16,   16,   16,
  165,   16,   16,   16,   16,    0,   19,   16,    0,    0,
   59,   16,   16,    0,   40,   41,   42,   43,    0,   16,
   16,    0,  185,  186,   16,    0,   16,   16,    0,   16,
   16,   16,   16,   48,   82,   16,    0,   48,    0,    0,
  201,    0,    0,  205,    0,    0,    0,  210,    0,    0,
    0,    0,  104,    0,    0,    0,  109,    0,    0,    0,
    0,   48,  220,    0,    0,    0,  150,  224,    0,   48,
    6,    0,    0,    0,    0,  123,  125,    7,    8,   48,
    0,    0,  132,   48,   10,    0,    0,   12,   13,   14,
   15,    0,  150,   16,  137,   67,   68,   69,   70,   71,
   72,   73,   48,   48,  128,  128,    0,    0,    0,   48,
    0,    0,    0,  152,    5,    6,    0,    0,    0,   48,
   48,   48,    7,    8,   48,   48,    9,    0,    0,   10,
   11,    0,   12,   13,   14,   15,    0,    0,   16,    0,
   48,    0,    0,    0,    5,    6,    0,    0,    0,    0,
    0,    0,    7,    8,    0,  177,  178,   63,    0,   10,
   11,    0,   12,   13,   14,   15,    0,    0,   16,    0,
   19,   19,   19,   48,    0,    0,    0,    0,    0,    0,
    0,   19,   48,   48,    0,    0,    0,    0,    0,    5,
    6,    0,    0,   48,    0,   48,   48,    7,    8,    0,
    0,    0,   81,    0,   10,   11,    0,   12,   13,   14,
   15,  101,    6,   16,    0,    0,    0,    0,   48,    7,
    8,    0,    0,    0,  102,    0,   10,   11,    0,   12,
   13,   14,   15,  182,    6,   16,    0,    0,    0,    0,
    0,    7,    8,    0,  183,    0,    0,  184,   10,    0,
    0,   12,   13,   14,   15,    5,    6,   16,    0,    0,
    0,    0,    0,    7,    8,    0,    0,    0,    0,    0,
   10,   11,    0,   12,   13,   14,   15,  222,    6,   16,
    0,    0,    0,    0,    0,    7,    8,    0,    0,    0,
    0,  223,   10,    0,    0,   12,   13,   14,   15,    6,
    0,   16,    0,    0,    0,    0,    7,    8,    0,    0,
    0,  149,    0,   10,    0,    0,   12,   13,   14,   15,
    6,    0,   16,    0,    0,    0,    0,    7,    8,    0,
    0,    0,    0,  204,   10,    0,    0,   12,   13,   14,
   15,    0,    0,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   44,   41,   60,   60,   41,  123,   44,   40,
   44,   40,  257,   43,   42,   45,  256,   44,    2,   47,
    4,   43,    0,   45,  264,    9,  268,   41,  273,   43,
   40,   45,  256,   17,   17,   45,  256,  257,   60,   61,
   62,   41,   42,   43,   44,   45,  270,   47,    0,  267,
   34,   34,  270,  256,  257,  275,  276,  257,  268,   59,
   60,   61,   62,   41,   59,   43,   44,   45,   52,   52,
    0,   55,   56,   59,  267,  275,  276,  270,  257,  257,
  258,   59,   60,   61,   62,  256,  257,  256,   44,   41,
   59,   43,   44,   45,  123,    0,  275,  276,  267,  267,
  263,  270,  270,   59,   44,  256,  257,   59,   60,   61,
   62,   41,  256,   43,   44,   45,   44,    0,    2,   59,
    4,  148,  106,  267,  267,    9,  270,  270,  258,   59,
   60,   61,   62,   41,  257,   43,   41,   45,   43,    0,
   45,  168,  266,  170,  171,  125,   41,  263,   43,  257,
   45,   43,   44,   45,   59,   60,   61,   62,  256,  257,
   43,   44,   45,   62,    0,   45,  193,  217,  218,  278,
  154,  155,   94,   95,   99,  100,   59,  257,   79,   80,
   41,  257,   40,  257,  264,  265,    0,   45,   40,  269,
   40,  271,  257,   44,  274,  275,  276,  277,   59,  123,
  280,  257,  266,   62,  257,  263,   40,  270,   44,   41,
   41,   45,   41,   41,    0,  257,   41,   41,   41,  275,
  276,  256,  257,   59,  279,  256,  266,  263,  266,  263,
   44,  273,  216,  217,  218,   41,    0,  278,  260,  261,
  262,   41,  256,  227,  227,   59,  256,  257,  258,  259,
  268,  268,  268,    0,    0,  270,  256,  257,    0,  270,
  260,  261,  262,  270,  264,  265,  266,  267,  270,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   41,  260,  261,  262,   45,  264,  265,  266,  267,
    0,  269,  270,  271,  272,   59,  274,  275,  276,  277,
  256,   40,  280,  270,  256,  257,   45,  263,  260,  261,
  262,  269,  264,  265,  266,  267,  256,  269,  270,  271,
  272,    0,  274,  275,  276,  277,  256,  257,  280,    0,
  260,  261,  262,  269,  264,  265,  266,  267,   45,  269,
  270,  271,  272,    0,  274,  275,  276,  277,  269,   59,
  280,  256,  257,   44,    0,  260,  261,  262,  269,  264,
  265,  266,  267,   60,  269,  270,  271,  272,    0,  274,
  275,  276,  277,  256,  257,  280,  256,  257,  258,  259,
   59,  264,  265,  155,  267,   -1,  269,  270,  271,  272,
    0,  274,  275,  276,  277,  256,  257,  280,  256,  257,
  258,  259,   59,  264,  265,  266,  267,   -1,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,    0,   -1,  280,
  256,  257,  256,  257,  258,  259,   -1,   59,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   45,  274,  275,
  276,  277,  256,  257,  280,    0,   -1,   -1,   -1,   59,
  264,  265,   -1,  267,   -1,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,   45,   -1,  280,    0,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,   59,   -1,  264,  265,
   -1,  267,   -1,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,  256,  257,  280,    0,  256,  257,  258,  259,
  264,  265,   -1,  267,   59,  269,  270,  271,  272,   -1,
  274,  275,  276,  277,  256,  257,  280,  256,  257,  258,
  259,   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   41,
   -1,   -1,   -1,   45,   -1,   -1,  256,  257,   -1,  256,
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
  280,   -1,   -1,  256,  257,  256,  257,  258,  259,   -1,
   -1,  264,  265,   -1,  267,   -1,  269,  270,  271,  272,
   -1,  274,  275,  276,  277,   -1,    2,  280,    4,   -1,
   -1,  256,  257,    9,   -1,   -1,   -1,   -1,   -1,  264,
  265,   17,  267,   -1,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   34,   -1,
   -1,  264,  265,  133,  267,   -1,  269,  270,  271,  272,
  140,  274,  275,  276,  277,   -1,   52,  280,   -1,   -1,
   12,  256,  257,   -1,  256,  257,  258,  259,   -1,  264,
  265,   -1,  162,  163,  269,   -1,  271,  272,   -1,  274,
  275,  276,  277,    8,   36,  280,   -1,   12,   -1,   -1,
  180,   -1,   -1,  183,   -1,   -1,   -1,  187,   -1,   -1,
   -1,   -1,   54,   -1,   -1,   -1,   58,   -1,   -1,   -1,
   -1,   36,  202,   -1,   -1,   -1,  112,  207,   -1,   44,
  257,   -1,   -1,   -1,   -1,   77,   78,  264,  265,   54,
   -1,   -1,   84,   58,  271,   -1,   -1,  274,  275,  276,
  277,   -1,  138,  280,   96,   23,   24,   25,   26,   27,
   28,   29,   77,   78,   79,   80,   -1,   -1,   -1,   84,
   -1,   -1,   -1,  115,  256,  257,   -1,   -1,   -1,   94,
   95,   96,  264,  265,   99,  100,  268,   -1,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
  115,   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,   -1,
   -1,   -1,  264,  265,   -1,  157,  158,  269,   -1,  271,
  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,   -1,
  216,  217,  218,  148,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  227,  157,  158,   -1,   -1,   -1,   -1,   -1,  256,
  257,   -1,   -1,  168,   -1,  170,  171,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,  272,   -1,  274,  275,  276,
  277,  256,  257,  280,   -1,   -1,   -1,   -1,  193,  264,
  265,   -1,   -1,   -1,  269,   -1,  271,  272,   -1,  274,
  275,  276,  277,  256,  257,  280,   -1,   -1,   -1,   -1,
   -1,  264,  265,   -1,  267,   -1,   -1,  270,  271,   -1,
   -1,  274,  275,  276,  277,  256,  257,  280,   -1,   -1,
   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,   -1,   -1,
  271,  272,   -1,  274,  275,  276,  277,  256,  257,  280,
   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,
   -1,  270,  271,   -1,   -1,  274,  275,  276,  277,  257,
   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,  271,   -1,   -1,  274,  275,  276,  277,
  257,   -1,  280,   -1,   -1,   -1,   -1,  264,  265,   -1,
   -1,   -1,   -1,  270,  271,   -1,   -1,  274,  275,  276,
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

//#line 283 "grammar.y"

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
//#line 631 "Parser.java"
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
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta el nombre del programa en la primer linea. "); }
break;
case 3:
//#line 20 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta del programa." ); }
break;
case 4:
//#line 21 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'BEGIN'. "); }
break;
case 5:
//#line 22 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitador del programa 'END'.") ; }
break;
case 6:
//#line 23 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Falta delimitadores del programa. "); }
break;
case 7:
//#line 24 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": programa vacio... "); }
break;
case 11:
//#line 36 "grammar.y"
{System.out.println("Sentencia de declaracion de tipo en linea "+AnalizadorLexico.line_number);}
break;
case 12:
//#line 37 "grammar.y"
{System.out.println("Sentencia de declaracion de variable/s en linea "+AnalizadorLexico.line_number);}
break;
case 13:
//#line 38 "grammar.y"
{System.out.println("Sentencia de declaracion de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 14:
//#line 39 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+" : sintaxis incorrecta de sentencia ");}
break;
case 16:
//#line 50 "grammar.y"
{
            System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ';' al final de la sentencia.");
        }
break;
case 17:
//#line 57 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se encontro ';' pero esa sentencia no lleva. Proba quitandoselo.");}
break;
case 19:
//#line 62 "grammar.y"
{System.out.println("Sentencia de control IF en linea "+AnalizadorLexico.line_number);}
break;
case 20:
//#line 64 "grammar.y"
{System.out.println("Sentencia de asignacion en linea "+AnalizadorLexico.line_number);}
break;
case 21:
//#line 65 "grammar.y"
{System.out.println("Sentencia de impresion por pantalla en linea "+AnalizadorLexico.line_number);}
break;
case 22:
//#line 67 "grammar.y"
{System.out.println("Sentencia de repeat until en linea "+AnalizadorLexico.line_number);}
break;
case 23:
//#line 68 "grammar.y"
{System.out.println("Sentencia de salto goto en linea "+AnalizadorLexico.line_number);}
break;
case 24:
//#line 70 "grammar.y"
{System.out.println("Sentencia de asignacion multiple en linea "+AnalizadorLexico.line_number);}
break;
case 25:
//#line 72 "grammar.y"
{System.out.println("Sentencia de retorno de funcion en linea "+AnalizadorLexico.line_number);}
break;
case 26:
//#line 73 "grammar.y"
{System.out.println("Sentencia de TAG");}
break;
case 30:
//#line 84 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia ");}
break;
case 32:
//#line 86 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": falta ';' al final de la sentencia o estas tratando de declarar varias variables sin ','");}
break;
case 34:
//#line 91 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de funcion.") ; }
break;
case 35:
//#line 92 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto. Verifica solo haya 1 parametro ");}
break;
case 37:
//#line 97 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba 'pair'.") ; }
break;
case 38:
//#line 98 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que el tipo este entre <> .") ; }
break;
case 39:
//#line 99 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba un ID al final de la declaracion"); }
break;
case 43:
//#line 117 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba tipo del parametro de la funcion. "); }
break;
case 44:
//#line 118 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba nombre de parametro"); }
break;
case 46:
//#line 123 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": faltan parentesis en sentencia de return. ") ;}
break;
case 52:
//#line 144 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis. "); }
break;
case 53:
//#line 145 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 54:
//#line 146 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 55:
//#line 147 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF") ; }
break;
case 56:
//#line 148 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s dentro del IF "); }
break;
case 57:
//#line 149 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable dentro del IF, incorrecta "); }
break;
case 59:
//#line 153 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba sentencia/s ejecutable/s luego del THEN y luego del ELSE ") ; }
break;
case 60:
//#line 154 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia/s ejecutable/s luego del THEN ") ; }
break;
case 61:
//#line 155 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": Se esperaba sentencia ejecutable luego del else. ") ; }
break;
case 62:
//#line 156 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia ejecutable luego del else, incorrecta ") ; }
break;
case 63:
//#line 157 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de sentencia/s ejecutable/s incorrecta luego del THEN ") ; }
break;
case 64:
//#line 158 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este dentro de parentesis. ") ; }
break;
case 65:
//#line 159 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 66:
//#line 160 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 67:
//#line 161 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba END_IF ") ; }
break;
case 70:
//#line 173 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba comparador ") ; }
break;
case 79:
//#line 189 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": no se permite asignacion en declaracion. Separa las sentencias. ") ;}
break;
case 83:
//#line 195 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis de expresion incorrecta, asegurate no falte operador ni operando.") ; }
break;
case 96:
//#line 220 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": sintaxis incorrecta de invocacion a funcion. Asegurate de no pasar más de 1 parametro a una funcion ") ; }
break;
case 98:
//#line 225 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba parametro en OUTF "); }
break;
case 99:
//#line 226 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": parametro incorrecto en OUTF "); }
break;
case 101:
//#line 234 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba que la condicion este entre parentesis "); }
break;
case 102:
//#line 235 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba ')' luego de la condicion. "); }
break;
case 103:
//#line 236 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba '(' antes de la condicion. "); }
break;
case 104:
//#line 238 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until "); }
break;
case 105:
//#line 239 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y que la condicion este entre parentesis. "); }
break;
case 106:
//#line 240 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y ')' luego de la condicion. "); }
break;
case 107:
//#line 241 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba cuerpo de repeat until, y'(' antes de la condicion. "); }
break;
case 108:
//#line 242 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba UNTIL luego de 'END' "); }
break;
case 109:
//#line 243 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba condicion luego de UNTIL "); }
break;
case 111:
//#line 249 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": lista de expresiones incorrecta, puede que falte ',' entre las expresiones ") ; }
break;
case 119:
//#line 278 "grammar.y"
{System.out.println("Error en linea "+AnalizadorLexico.line_number+": se esperaba TAG "); }
break;
//#line 1030 "Parser.java"
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

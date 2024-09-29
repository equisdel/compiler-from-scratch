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






//#line 2 "gramatica.y"
        import java.io.*;
        import PrimerEtapa.AnalizadorLexico;
//#line 20 "Parser.java"




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
    0,    1,    1,    2,    2,    2,    3,    3,    3,    3,
    3,    3,   12,   12,    4,    4,    4,   15,    5,   16,
   13,   13,   14,   14,    6,    6,   19,   18,   18,   20,
   20,   20,   20,   20,   20,    7,   17,   17,   17,   22,
   22,   22,   23,   23,   23,   21,    8,    8,    9,   11,
   24,   24,   25,   25,   10,
};
final static short yylen[] = {                            2,
    4,    2,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    2,    9,    6,    2,    4,    1,
    1,    1,    1,    3,    7,    9,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    4,    4,    4,    8,    3,
    3,    3,    1,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   21,   22,
    0,    0,    0,    0,    4,    5,    6,    7,    8,    9,
   10,   11,   12,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    1,    3,    2,   23,    0,    0,    0,
    0,   44,    0,   45,    0,   42,    0,   52,    0,    0,
    0,    0,    0,    0,    0,   13,    0,   55,    0,    0,
    0,   50,    0,    0,    0,    0,    0,   33,   34,   35,
   30,   31,   32,    0,    0,   48,   47,    0,   19,    0,
   14,    0,   24,    0,    0,    0,    0,   40,   41,    0,
    0,    0,    0,    0,    0,   54,   46,    0,    0,   17,
    0,   18,    0,    0,   25,    0,    0,    0,   49,    0,
    0,   26,   16,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   98,   24,   39,   95,  111,   49,   50,   99,   74,
   44,   45,   46,   25,   62,
};
final static short yysindex[] = {                      -244,
 -251,    0, -122,  -36,  -17,   -6, -241,    5,    0,    0,
 -210, -214, -198,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -246, -188, -197, -176, -197, -202,   36,
 -197, -174,   40,    0,    0,    0,    0, -150,   64, -197,
   75,    0,    8,    0,  -32,    0,   74,    0,   99,   86,
    0,   88,  -29, -186,  -13,    0, -152,    0,   90, -126,
   49,    0, -197, -197, -197, -197, -197,    0,    0,    0,
    0,    0,    0, -197, -133,    0,    0,   72,    0, -142,
    0, -186,    0, -197,  -10,  -32,  -32,    0,    0,    8,
 -174, -120,   98, -112,  106,    0,    0, -174, -231,    0,
 -197,    0, -117, -174,    0,  107, -122, -114,    0, -122,
 -107,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   73,    0,
  -41,    0,   41,    0,  -19,    0, -100,    0,    0,    0,
   79,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   57,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    3,   25,    0,    0,  123,
    0,    0,    0,    0,    0,    0,    0, -218,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -104,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
   59,   -8,   -3,    0,    0,    0,    0,    0,    0,    0,
    0,  136,  -45,    0,    0,    0,   83,   68,   66,    0,
  -21,   34,   44,  144,   89,
};
final static int YYTABLESIZE=361;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   43,   43,   43,   43,   35,   43,   51,   27,   78,   66,
   37,   77,    1,   64,   67,   65,    3,   43,   43,   43,
   43,   39,   28,   39,   39,   39,   38,   79,   56,   64,
   97,   65,   64,   29,   65,  104,   94,   30,  105,   39,
   39,   39,   39,   37,   31,   37,   37,   37,   27,   33,
   64,   27,   65,   81,   41,   42,   52,   32,    4,   41,
   42,   37,   37,   37,   37,   38,    5,   38,   38,   38,
   34,   36,    6,    7,   40,    8,    9,   10,   11,   51,
   47,   12,    4,   38,   38,   38,   38,   56,    9,   10,
    5,   64,   84,   65,   81,   54,    6,   86,   87,   36,
   56,   35,   11,   58,    4,   12,   59,   60,   43,   88,
   89,   53,    5,   55,   63,   53,   80,   27,    6,   29,
   45,   45,   61,   45,   11,   45,   75,   12,   76,   82,
   83,   15,   91,   92,    4,   93,  100,  101,   45,   45,
   45,   64,    5,   65,  102,   85,  103,  109,    6,    7,
  107,    8,    9,   10,   11,  112,   90,   12,   71,   73,
   72,  113,   51,   28,   20,  110,   61,   57,  106,  108,
   48,    0,   96,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   43,    0,    0,   43,   43,
   43,    0,    0,   43,    0,   43,   26,   43,   43,   43,
   43,    0,   43,   43,   43,   43,    0,   39,   43,    0,
   39,   39,   39,    0,    0,   39,    0,   39,    0,   39,
   39,   39,   39,    0,   39,   39,   39,   39,    0,   37,
   39,    0,   37,   37,   37,    0,    0,   37,    0,   37,
    0,   37,   37,   37,   37,    0,   37,   37,   37,   37,
    0,   38,   37,    0,   38,   38,   38,    0,    0,   38,
    0,   38,    0,   38,   38,   38,   38,   36,   38,   38,
   38,   38,    0,    0,   38,   36,    0,   36,    0,   36,
   36,   36,   36,   53,   36,   36,   36,   36,    0,    0,
   36,   53,    0,   53,    0,   53,   53,   53,   53,   15,
   53,   53,   53,   53,    0,    0,   53,   15,   45,   45,
   45,   15,    0,   15,   15,    0,   15,   15,   15,   15,
    0,    0,   15,    0,    0,    0,    0,    0,   68,   69,
   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   13,   47,   28,   44,   54,   42,
  257,   41,  257,   43,   47,   45,  268,   59,   60,   61,
   62,   41,   40,   43,   44,   45,  273,   41,   32,   43,
   41,   45,   43,   40,   45,  267,   82,  279,  270,   59,
   60,   61,   62,   41,   40,   43,   44,   45,  267,  264,
   43,  270,   45,   57,  257,  258,  259,  268,  257,  257,
  258,   59,   60,   61,   62,   41,  265,   43,   44,   45,
  269,   59,  271,  272,  263,  274,  275,  276,  277,  101,
  257,  280,  257,   59,   60,   61,   62,   91,  275,  276,
  265,   43,   44,   45,   98,   60,  271,   64,   65,   59,
  104,  110,  277,   64,  257,  280,  257,   44,   26,   66,
   67,   29,  265,   31,   40,   59,  269,   44,  271,   41,
   42,   43,   40,   45,  277,   47,   41,  280,   41,   40,
  257,   59,  266,   62,  257,  278,  257,   40,   60,   61,
   62,   43,  265,   45,  257,   63,   41,   41,  271,  272,
  268,  274,  275,  276,  277,  270,   74,  280,   60,   61,
   62,  269,  263,   41,  269,  107,   84,   32,  101,  104,
   27,   -1,   84,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,  261,
  262,   -1,   -1,  265,   -1,  267,  263,  269,  270,  271,
  272,   -1,  274,  275,  276,  277,   -1,  257,  280,   -1,
  260,  261,  262,   -1,   -1,  265,   -1,  267,   -1,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,  257,
  280,   -1,  260,  261,  262,   -1,   -1,  265,   -1,  267,
   -1,  269,  270,  271,  272,   -1,  274,  275,  276,  277,
   -1,  257,  280,   -1,  260,  261,  262,   -1,   -1,  265,
   -1,  267,   -1,  269,  270,  271,  272,  257,  274,  275,
  276,  277,   -1,   -1,  280,  265,   -1,  267,   -1,  269,
  270,  271,  272,  257,  274,  275,  276,  277,   -1,   -1,
  280,  265,   -1,  267,   -1,  269,  270,  271,  272,  257,
  274,  275,  276,  277,   -1,   -1,  280,  265,  260,  261,
  262,  269,   -1,  271,  272,   -1,  274,  275,  276,  277,
   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,  260,  261,
  262,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ID","CTE","CHARCH","NEQ","LEQ","MEQ","ASSIGN",
"TAG","IF","THEN","ELSE","BEGIN","END","END_IF","OUTF","TYPEDEF","FUN","RET",
"UINTEGER","SINGLE","REPEAT","UNTIL","PAIR","GOTO",
};
final static String yyrule[] = {
"$accept : prog",
"prog : ID BEGIN statement_list END",
"statement_list : statement ';'",
"statement_list : statement_list statement",
"statement : executable_statement",
"statement : declare_statement",
"statement : return_statement",
"executable_statement : if_statement",
"executable_statement : assign_statement",
"executable_statement : outf_statement",
"executable_statement : repeat_statement",
"executable_statement : goto_statement",
"executable_statement : mult_assign_statement",
"executable_statement_list : executable_statement",
"executable_statement_list : executable_statement_list executable_statement",
"declare_statement : var_type var_list",
"declare_statement : var_type FUN ID '(' parametro ')' BEGIN fun_body END",
"declare_statement : TYPEDEF PAIR '<' var_type '>' ID",
"parametro : var_type ID",
"return_statement : RET '(' expr ')'",
"fun_body : statement_list",
"var_type : UINTEGER",
"var_type : SINGLE",
"var_list : ID",
"var_list : var_list ',' ID",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement END_IF",
"if_statement : IF '(' cond ')' THEN ctrl_block_statement ELSE ctrl_block_statement END_IF",
"ctrl_block_statement : executable_statement_list",
"cond : expr cond_op expr",
"cond : fun_invoc",
"cond_op : '<'",
"cond_op : '>'",
"cond_op : '='",
"cond_op : NEQ",
"cond_op : LEQ",
"cond_op : MEQ",
"assign_statement : ID ASSIGN expr",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : term",
"term : term '*' fact",
"term : term '/' fact",
"term : fact",
"fact : ID",
"fact : CTE",
"fact : fun_invoc",
"fun_invoc : ID '(' expr ')'",
"outf_statement : OUTF '(' expr ')'",
"outf_statement : OUTF '(' CHARCH ')'",
"repeat_statement : REPEAT BEGIN executable_statement_list END UNTIL '(' cond ')'",
"mult_assign_statement : id_list ASSIGN expr_list",
"id_list : ID ',' ID",
"id_list : ID ',' id_list",
"expr_list : expr",
"expr_list : expr ',' expr_list",
"goto_statement : GOTO TAG '@'",
};

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

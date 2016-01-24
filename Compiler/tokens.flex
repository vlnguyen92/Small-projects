import java_cup.runtime.Symbol;
%%
%cup
%eofval{
	return new Symbol(sym.EOF);
%eofval}
%{
//	The current line number

	public static int line_num = 1;
	
//	The lookup() function

	public Symbol lookup(String s)
	{
	//	Look for the string in the keyword section
	
		IdEntry p = SymbolTable.id_lookup(s, 1);
		
	//	If found, then return the keyword token to the parser
	
		if (p != null)
		{
		//	System.out.println("Token = " + p.name);
			return new Symbol(p.type);
		}
		
	//	If not found, then return the ID token to the parser
	
		else
		{
		//	System.out.println("Token = ID, value = '" + yytext() + "'");
			return new Symbol(sym.ID, s);
		}
	}
%}
digit=		[0-9]
letter=		[a-zA-Z]
num=		{digit}+
dbl=		{digit}*({digit}"."|"."{digit}){digit}*((E|e)[+-]?{digit}+)?
id=			{letter}({letter}|{digit}|_)*
str=		\"([^\n\\]|\\.)*\"
cmnt1=		"//".*
cmnt2=		\/\*([^\*]|\*+[^/])*\*+\/
ws=			[\ \t]+
%%
{id}		{return lookup(yytext());}
{num}		{/*System.out.println("Token = NUM, value = '" + yytext() + "'");*/ return new Symbol(sym.NUM, yytext());}
{dbl}		{/*System.out.println("Token = DBL, value = '" + yytext() + "'");*/ return new Symbol(sym.DBL, yytext());}
{str}		{/*System.out.println("Token = STR, value = " + yytext());*/ return new Symbol(sym.STR, yytext());}
{cmnt1}		{}
{cmnt2}		{}
{ws}		{}
"{"			{/*System.out.println("Token = LBRACE");*/ return new Symbol(sym.LBRACE);}
"}"			{/*System.out.println("Token = RBRACE");*/ return new Symbol(sym.RBRACE);}
"("			{/*System.out.println("Token = LPAREN");*/ return new Symbol(sym.LPAREN);}
")"			{/*System.out.println("Token = RPAREN");*/ return new Symbol(sym.RPAREN);}
"["			{/*System.out.println("Token = LBRACK");*/ return new Symbol(sym.LBRACK);}
"]"			{/*System.out.println("Token = RBRACK");*/ return new Symbol(sym.RBRACK);}
"+"			{/*System.out.println("Token = PLUS");*/ return new Symbol(sym.PLUS);}
"-"			{/*System.out.println("Token = MINUS");*/ return new Symbol(sym.MINUS);}
"*"			{/*System.out.println("Token = TIMES");*/ return new Symbol(sym.TIMES);}
"/"			{/*System.out.println("Token = DIVIDE");*/ return new Symbol(sym.DIVIDE);}
"%"			{/*System.out.println("Token = MOD");*/ return new Symbol(sym.MOD);}
"++"		{/*System.out.println("Token = INC");*/ return new Symbol(sym.INC);}
"--"		{/*System.out.println("Token = DEC");*/ return new Symbol(sym.DEC);}
"=="		{/*System.out.println("Token = EQ");*/ return new Symbol(sym.EQ);}
"!="		{/*System.out.println("Token = NE");*/ return new Symbol(sym.NE);}
"<"			{/*System.out.println("Token = LT");*/ return new Symbol(sym.LT);}
"<="		{/*System.out.println("Token = LE");*/ return new Symbol(sym.LE);}
">"			{/*System.out.println("Token = GT");*/ return new Symbol(sym.GT);}
">="		{/*System.out.println("Token = GE");*/ return new Symbol(sym.GE);}
"!"			{/*System.out.println("Token = NOT");*/ return new Symbol(sym.NOT);}
"&&"		{/*System.out.println("Token = AND");*/ return new Symbol(sym.AND);}
"||"		{/*System.out.println("Token = OR");*/ return new Symbol(sym.OR);}
"~"			{/*System.out.println("Token = COMP");*/ return new Symbol(sym.COMP);}
"<<"		{/*System.out.println("Token = SHL");*/ return new Symbol(sym.SHL);}
">>"		{/*System.out.println("Token = SHR");*/ return new Symbol(sym.SHR);}
"="			{/*System.out.println("Token = ASSIGN");*/ return new Symbol(sym.ASSIGN);}
","			{/*System.out.println("Token = COMMA");*/ return new Symbol(sym.COMMA);}
";"			{/*System.out.println("Token = SEMI");*/ return new Symbol(sym.SEMI);}
\r			{}
\n			{line_num++;}
.			{Err.display_msg(Err.ILLCHAR, "Illegal character (" + yytext() + ")");}

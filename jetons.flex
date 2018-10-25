import java.awt.*;
%%
%public
%class Lexer
%unicode
%type Token
%line
%column

%{
   // Fonctions
   
	public static Color stringHexaToRGB(String colorStr) {
	    return new Color(
            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
   
	

%}

%yylexthrow Exception

blank = [\n\r \t]
nombre = [0-9]+
hex = [0-9A-F]
couleur = #{hex}{hex}{hex}{hex}{hex}{hex}
op = "+"|"-"|"/"|"*"
keyword = [A-Z]+[a-zA-Z]*
identificateur = [a-z][a-zA-Z_]*

%%

 "("				{return new Token(Sym.LPAR, yyline, yycolumn);}
 ")"				{return new Token(Sym.RPAR, yyline, yycolumn);}
 ";"				{return new Token(Sym.SEMIC, yyline, yycolumn);}
 ","    		    {return new Token(Sym.COMMA, yyline, yycolumn);}
 "="				{return new Token(Sym.EQL, yyline, yycolumn);}
 {nombre}			{return new IntToken(Sym.NBR, Integer.parseInt(yytext()), yyline, yycolumn);}
 {hex}				{return new StringToken(Sym.HEX, yytext(), yyline, yycolumn);}
 {couleur}			{return new ColorToken(Sym.CLR, stringHexaToRGB(yytext()), yyline, yycolumn);}
 {keyword}			{return new StringToken(Sym.KWD, yytext(), yyline, yycolumn);}
 {op}				{return new OpToken(Sym.OP, yytext().charAt(0), yyline, yycolumn);}
 {identificateur}	{return new StringToken(Sym.ID, yytext(), yyline, yycolumn);}
 {blank}	{}
 
 <<EOF>>	{return new Token(Sym.EOF, yyline, yycolumn);}
 [^]		{throw new LexerException(yyline, yycolumn);}

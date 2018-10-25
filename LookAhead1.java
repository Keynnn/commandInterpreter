import java.io.*;
import java.awt.*;

class LookAhead1  {
    
    private static Token current;
    private Lexer lexer;

    public LookAhead1(Lexer l) throws IOException, Exception {
		lexer=l;
		current=lexer.yylex();
    }

    public boolean check(Sym s) throws Exception {
        return (current.symbol() == s); 
    }

    public void eat(Sym s) throws Exception {
		if (!check(s)) {
			posError();
		    throw new Exception("\nNe peut consommer "+s+", le token actuel étant : "+current);
		}
        current=lexer.yylex();
    }
    
    public boolean isEmpty(){
    	return current==null;
    }
    
    public int getIntValue() throws Exception {
    	if (current instanceof IntToken)
    		return ((IntToken)current).getValue();
    	current.posErreur();
    	throw new Exception("LookAhead erreur: méthode getIntValue");
    }

    public String getStringValue() throws Exception {
    	if (current instanceof StringToken)
    		return ((StringToken)current).getValue();
    	current.posErreur();
    	throw new Exception("LookAhead erreur: méthode getStringValue");
    }

	public char getOpValue() throws Exception {
		if (current instanceof OpToken)
			return ((OpToken)current).getValue();
		current.posErreur();
		throw new Exception("LookAhead erreur: méthode getOpValue");
	}
	
	public Color getColorValue() throws Exception {
		if (current instanceof ColorToken)
			return ((ColorToken)current).getValue();
		current.posErreur();
		throw new Exception("LookAheaed erreur: méthode getColorValue");
	}
		
	public void showToken(){
		if (current instanceof IntToken){
			System.out.println(((IntToken)current).toString());
		}
		else if (current instanceof StringToken){
			System.out.println(((StringToken)current).toString());
		}
		else if (current instanceof OpToken){
			System.out.println(((OpToken)current).toString());
		}
		else if (current instanceof ColorToken){
			System.out.println(((ColorToken)current).toString());
		}
		else {	System.out.println(current.toString());	}
	}
	
	public static void posError()	{	current.posErreur();	}
}

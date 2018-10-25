import java.awt.*;

class Token {

    protected Sym symbol;
    private int ligne, colonne;
    
    public Token(Sym s, int l, int c) {
		symbol = s;
		ligne = l+1;
		colonne = c+1;
    }
    
    public Sym symbol() {
		return symbol;
    }
    
    public String toString(){
		return "Symbol : "+symbol;
    }
    
    public void posErreur(){	System.out.println("\n Ligne :"+ligne+", colonne :"+colonne);	} 

}   


class IntToken extends Token {

    private int value;

    public IntToken(Sym s, int v, int l, int c){
        super(s, l, c);
        value = v;
    }
    
    public int getValue(){
    	return value;
    }
    
    public String toString(){
		return "Symbol : "+symbol+" | Value : "+value;
    }
}

class StringToken extends Token {

	private String value;
	
	public StringToken(Sym s, String v, int l, int c){
		super(s, l, c);
        value = v;
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		return "Symbol : "+symbol+" | Value : "+value;
    }	
}


class OpToken extends Token {

    private char value;

    public OpToken(Sym s, char v, int l, int c){
        super(s, l, c);
        value = v;
    }
    
    public char getValue(){
    	return value;
    }
    
    public String toString(){
		return "Symbol : "+symbol+" | Value : "+value;
    }
}

class ColorToken extends Token {

    private Color value;

    public ColorToken(Sym s, Color v, int l, int c){
        super(s, l, c);
        value = v;
    }
    
    public Color getValue(){
    	return value;
    }
    
    public String toString(){
		return "Symbol : "+symbol+" | Value : "+value;
    }
}

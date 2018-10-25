import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Parser {
	
    protected LookAhead1 reader;
    private static Constante liste;
    private static Variable lsVar;
    private static Stack<Begin> cs;
    private static int cptIF = 0 ;
    private static int cptELSE = 0 ;
    private static boolean choixLS = true;
    public Parser(LookAhead1 r) {
		reader = r;
		liste = new Constante();
		lsVar = new Variable();
		cs = new Stack<Begin>();
    }
    
    public Programme progNonTerm() throws Exception {
    	Programme ls = suiteInsNonTerm(new Programme());
		return ls;
	}
    
    public Programme suiteInsNonTerm(Programme ls) throws Exception {
    	if(reader.check(Sym.KWD) || reader.check(Sym.ID) ){
    		insNonTerm(ls);
    		if(reader.check(Sym.EOF)){
    			reader.eat(Sym.EOF);
    			return ls;
    		}
    		if(!reader.check(Sym.KWD)){
    			System.out.print("Suite | ");
    			reader.showToken();
    			reader.eat(Sym.SEMIC);
    		}
    		return suiteInsNonTerm(ls);
    	}
    	return ls;
    }
    
    public Programme insNonTerm(Programme ls) throws Exception {
    	if(reader.check(Sym.EOF)){
			return ls;
    	} 
    	String s = reader.getStringValue();
    	if(s.equals("Begin")){
    		beginTerm(ls);
    		suiteInsNonTerm(ls);
			return ls;
    	}
    	else if(s.equals("End")){
    		endTerm(ls);
    		return ls;
    	}
    	else if(s.equals("DrawCircle")){
    		reader.eat(Sym.KWD);
    		reader.eat(Sym.LPAR);
    		int a = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int b = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int c = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		Color clr = clrTerm();
    		reader.eat(Sym.RPAR);
    		ls.add(new DC (a, b, c, clr));
    		return ls;
    	}
    	else if(s.equals("FillCircle")){
    		reader.eat(Sym.KWD);
    		reader.eat(Sym.LPAR);
    		int a = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int b = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int c = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		Color clr = clrTerm();
    		reader.eat(Sym.RPAR);
    		ls.add(new FC(a, b, c, clr));
    		return ls;
    	}
    	else if(s.equals("DrawRect")){
    		reader.eat(Sym.KWD);
    		reader.eat(Sym.LPAR);
    		int a = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int b = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int c = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int d = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		Color clr = clrTerm();
    		reader.eat(Sym.RPAR);
    		ls.add(new DR(a, b, c, d, clr));
    		return ls;
    	}
    	else if(s.equals("FillRect")){
    		reader.eat(Sym.KWD);
    		reader.eat(Sym.LPAR);
    		int a = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int b = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int c = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		int d = exprNonTerm();
    		reader.eat(Sym.COMMA);
    		Color clr = clrTerm();
    		reader.eat(Sym.RPAR);
    		ls.add(new FR(a, b, c, d, clr));
    		return ls;
    	}
    	else if(s.equals("Const")){
    		choixLS = true;
    		reader.eat(Sym.KWD);
    		String id = idTerm();
    		reader.eat(Sym.EQL);
    		int v = exprNonTerm();
    		if(!cs.empty())
    			cs.peek().nouvelleConstante(id);    		
    		ls.add(new Constante(id, v));
    		return ls;
    	}
    	else if(s.equals("Var")){
    		choixLS = false;
    		reader.eat(Sym.KWD);
    		String idVar = idTerm();
    		reader.eat(Sym.EQL);
    		int val = exprNonTerm();
    		if(!cs.empty())
    			cs.peek().nouvelleVariable(idVar);
    		ls.add(new Variable(idVar, val));
    		return ls;
    	}
    	else if(reader.check(Sym.ID)){
    		choixLS = false;
    		String idNew = idTerm();
    		reader.eat(Sym.EQL);
    		int valNew = exprNonTerm();
    		if ( ls.add(new Variable(idNew, valNew, true)) ) System.out.println("Variable update avec succes."  + idNew + " , " + valNew );
    		return ls;
    	}
/*    	else if(s.equals("While")){
    		System.out.println("CONDITION WHILE");
    		reader.eat(Sym.KWD);
    		int cond = exprNonTerm();
    		//while(cond != 0){
    			
    		//}
    	}
*/    	else if(s.equals("If")){
    		reader.showToken();
    		cptIF++;
    		reader.eat(Sym.KWD);
    		int k = exprNonTerm();
    		System.out.println("Evaluation | "+k);
    		if(k!=0){
    			System.out.println("CONDITION IF");
    			reader.showToken();
    			reader.eat(Sym.KWD);
    			Boolean end = true;
    			while(end && !reader.check(Sym.EOF)){	 							// Partie executee
    				if(reader.check(Sym.KWD)){
    					if(reader.getStringValue().equals("Begin"))
    						beginTerm(ls);
    					else if(reader.getStringValue().equals("End"))
    						endTerm(ls);
    					else if(reader.getStringValue().equals("Else"))
    						end = false;
    					else{
    						if(end)
    							insNonTerm(ls);
    					}
    				}
    				else if ( reader.check(Sym.SEMIC) ) reader.eat(Sym.SEMIC);
    				else {
    					insNonTerm(ls);
    				}
    				
    			}
    			while(!reader.check(Sym.SEMIC) && !reader.check(Sym.EOF)){			// Partie non executee
    				term();
    			}
    			if(!reader.check(Sym.EOF))
    				reader.eat(Sym.SEMIC);
    			return ls;
    		}
    		else{
    			System.out.println("CONDITION ELSE");
    			termElse();															// Partie non executee
    			while(!reader.check(Sym.SEMIC)){									// Partie executee
    				if(reader.check(Sym.EOF))
    					break;
    				insNonTerm(ls);
    			}
    			return ls;
    		}
    	}
	   	else	{
    		reader.posError();
    		throw new Exception("Parser erreur: méthode insNonTerm, mauvais nom de fonction , s = " + s);
    	}
    }
    
    public int exprNonTerm() throws Exception {
    	if(reader.check(Sym.NBR)){
    		return nbrTerm();
    	}
    	else if(reader.check(Sym.ID) ){
    		String id = idTerm();
    		if(choixLS)
    			return liste.getValue(id);
    		else
    			return lsVar.getValue(id);
    	}
    	
    	else{
    		reader.showToken();
    		reader.eat(Sym.LPAR);
    		int n = exprNonTerm();
    		int c = opTerm();
    		if		(c == '+')	n += exprNonTerm();
    		else if	(c == '-')	n -= exprNonTerm();
    		else if	(c == '*')	n *= exprNonTerm();
    		else if	(c == '/')	{
    			try {
    				n /= exprNonTerm();
    			} catch (ArithmeticException e) {
    				reader.posError();
    				System.out.println("Division par zéro");
    			}
    		}
    		else	{
    			reader.posError();
    			throw new Exception("Parser erreur: méthode exprNonTerm, mauvaise opération");
    		}
    		reader.showToken();
    		reader.eat(Sym.RPAR);
    		return n;
    	}  		
    }
    
    
    public int nbrTerm() throws Exception {
    	reader.showToken();
    	int n = reader.getIntValue();
    	reader.eat(Sym.NBR);
    	return n;
    }
    
    public char opTerm() throws Exception {
    	reader.showToken();
    	char c = reader.getOpValue();
    	reader.eat(Sym.OP);
    	return c;
    }
    
    public Color clrTerm() throws Exception {
    	reader.showToken();
    	Color s = reader.getColorValue();
    	reader.eat(Sym.CLR);
    	return s;
    }
    
    public String idTerm() throws Exception {
    	reader.showToken();
    	String x = reader.getStringValue();
    	reader.eat(Sym.ID);
    	return x;
    }
    
    public void beginTerm(Programme ls) throws Exception {
    	reader.showToken();
    	Begin b = new Begin();
    	cs.push(b);
  		ls.add(b);
   		reader.eat(Sym.KWD);
   	}
   	
   	public void endTerm(Programme ls) throws Exception {
   		reader.showToken();
   		reader.eat(Sym.KWD);
    	if(cs.isEmpty()){
   			reader.posError();
   			throw new Exception("endTerm erreur: méthode insNonTerm, pile suppression impossible");
   		}
   		Begin d = cs.pop();
   		ls.add(new End(d, lsVar));
   		ls.add(new End(d, liste));
   }
    
    public void term() throws Exception{
    	System.out.print("Term | ");
    	reader.showToken();
    	if		(reader.check(Sym.LPAR))		reader.eat(Sym.LPAR);
    	else if (reader.check(Sym.RPAR))		reader.eat(Sym.RPAR);
    	else if (reader.check(Sym.COMMA))		reader.eat(Sym.COMMA);
    	else if (reader.check(Sym.EQL))			reader.eat(Sym.EQL);
    	else if (reader.check(Sym.NBR))			reader.eat(Sym.NBR);
    	else if (reader.check(Sym.OP))			reader.eat(Sym.OP);
    	else if (reader.check(Sym.HEX))			reader.eat(Sym.HEX);
    	else if (reader.check(Sym.CLR))			reader.eat(Sym.CLR);
    	else if (reader.check(Sym.ID))			reader.eat(Sym.ID);
    	else if	(reader.check(Sym.SEMIC))		reader.eat(Sym.SEMIC);
    	else if (reader.check(Sym.KWD)){
    		if(reader.getStringValue().equals("If"))
    			cptIF++;
    		else if(reader.getStringValue().equals("Else"))
    			cptELSE++;
    		reader.eat(Sym.KWD);
    	}
    	else if	(reader.check(Sym.EOF))			reader.eat(Sym.EOF);
    	else { }	
    }
    
    public void termElse() throws Exception {
    	
    	Boolean ok = true;
    	while(ok){
    		if(reader.check(Sym.KWD)){
    			if(reader.getStringValue().equals("Else") && cptIF==cptELSE+1){
    				ok = false;
    			}
    		}
    		System.out.print("Else | ");
    		term();
    	}
    }

    
    
    
}

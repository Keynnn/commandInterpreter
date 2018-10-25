import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;

abstract class Fonction{

	public abstract void exec(Graphics2D feuille);
		
}

class Programme extends ArrayList<Fonction>{
	public void exec(Graphics2D feuille){}
	
	public void run(Graphics2D feuille) throws Exception {
		for(Fonction fonc : this)
			fonc.exec(feuille);
		System.out.println("Ok.");
	}
}

class Begin extends Fonction {
	private ArrayList<String> c;
	private ArrayList<String> v;

	public Begin() {
		c = new ArrayList<String>();
		v = new ArrayList<String>();
	}
	
	public void nouvelleConstante(String n){
		c.add(n);
	}
	
	public void nouvelleVariable(String x){
		v.add(x);
	}
	
	public ArrayList<String> getConst(){
		return c;
	}
	
	public ArrayList<String> getVar(){
		return v;
	}
	
	public void exec(Graphics2D feuille){}
}

class End extends Fonction {

	public End(Begin str, Constante k) throws Exception{
		ArrayList<String> del = str.getConst();
		for(int i=0; i<=del.size()-1; i++) {
			if(k.getHMC().containsKey(del.get(i))){
				System.out.println("Fin constante : "+del.get(i));
				k.delValue(del.get(i));
			}
		}	
	}
	
	public End(Begin str, Variable k) throws Exception{
		ArrayList<String> del = str.getVar();
		System.out.println(del);
		if(del.size() == 1 && k.getHMV().containsKey(del.get(0))){
			k.delValeur(del.get(0));
		}
		else {
			for(int i=0; i<del.size()-1; i++) {
				if(k.getHMV().containsKey(del.get(i))){
					System.out.println("Fin variable : "+del.get(i));
					k.delValeur(del.get(i));
				}
			}
		}
	}
	
	public void exec(Graphics2D feuille){}
}

class Constante extends Fonction {
	
	static Map<String, ArrayList<Integer>> hm;
	
	public Constante() {
		hm = new HashMap<String, ArrayList<Integer>>();
	}
	
	public Constante(String n, int v){
		System.out.println("Debut constante : "+n);
		ajout(n, v);
	}
	
	public Map<String, ArrayList<Integer>> getHMC(){	return hm;	}

	public void ajout(String nom, int expression){
		if(!hm.containsKey(nom))
			hm.put(nom, new ArrayList<Integer>());
		hm.get(nom).add(expression);
	}
	
	public int getValue(String nom) throws Exception{
		if(!hm.containsKey(nom)){
			LookAhead1.posError();
			throw new Exception("Constante erreur: méthode getValue, l'identificateur n'existe pas");
		}
		else{
			List b = hm.get(nom);
			return (int)b.get(b.size()-1);
		}
	}
	
	public void delValue(String nom) throws Exception{
		if(hm.containsKey(nom)) {
			List d = hm.get(nom);
			if(d.size() == 1)
				hm.remove(nom);
			else
				d.remove(d.size()-1);
		}
		else{
			LookAhead1.posError();
			throw new Exception("Constante erreur: méthode delValue, l'identificateur n'existe pas");
		}
	}
	
	public void exec(Graphics2D feuille){}

}

class Variable extends Fonction{

	static Map<String, ArrayList<Integer>> variables;
	
	public Variable(){
		variables = new HashMap<String, ArrayList<Integer>>();
	}
	
	public Variable(String n, int v){
		System.out.println("Debut variable : "+n + " avec la valeur : " +v);
		ajout(n, v);
	}
	
	public Variable(String n, int v, Boolean x){
		System.out.println("Update variable :"+n+" avec la valeur : "+v);
		update(n, v);
	}
	
	public Map<String, ArrayList<Integer>> getHMV(){	return variables;	}

	public void ajout(String nom, int expression){
		if(!variables.containsKey(nom))
			variables.put(nom, new ArrayList<Integer>());
		variables.get(nom).add(expression);
	}
	
	public void update(String nom, int expression){
		if(!variables.containsKey(nom))
			variables.put(nom, new ArrayList<Integer>());
		List up = variables.get(nom);
		variables.get(nom).set(up.size()-1, expression);
	}
	
	public int getValue(String nom) throws Exception{
		if(!variables.containsKey(nom)){
			LookAhead1.posError();
			throw new Exception("Variable erreur: méthode getValue, l'identificateur n'existe pas");
		}
		else{
			List b = variables.get(nom);
			if(b.isEmpty()){
				LookAhead1.posError();
				throw new Exception ("Variable erreur: méthode getValue, la liste est vide");
			}
			return (int)b.get(b.size()-1);
		}
	}
	
	public void delValeur(String nom) throws Exception{
	System.out.println("VARIABLE DEL: ......");
		if(variables.containsKey(nom)) {
			List d = variables.get(nom);
			if(!d.isEmpty()){
				System.out.println("VARIABLE DEL:::: "+d.get(d.size()-1));
				d.remove(d.size()-1);
			}
		}
		else{
			LookAhead1.posError();
			throw new Exception("Variable erreur: méthode delValue, l'identificateur n'existe pas");
		}
	}
	
	public void exec(Graphics2D feuille){}
		

}

class DC extends Fonction {

	private int posX, posY, rayZ;
	private Color couleur;
	
	public DC (int x, int y, int z, Color col){
		posX = x;
		posY = y;
		rayZ = z;
		couleur = col;
	}
	
	public void exec(Graphics2D feuille){
		feuille.setColor(couleur);
		feuille.drawOval(posX, posY, 2*rayZ, 2*rayZ);
	}

}

class FC extends Fonction {

	private int posX, posY, rayZ;
	private Color couleur;
	
	public FC (int x, int y, int z, Color col){
		posX = x;
		posY = y;
		rayZ = z;
		couleur = col;
		
		
	}
	
	public void exec(Graphics2D feuille){
		feuille.setColor(couleur);
		feuille.fillOval(posX, posY, 2*rayZ, 2*rayZ);
	}

}

class DR extends Fonction {

	private int posX, posY, lgW, hauteur;
	private Color couleur;
	
	public DR (int x, int y, int w, int h, Color col){
		posX = x;
		posY = y;
		lgW = w;
		hauteur = h;
		couleur = col;
		
		
	}
	
	public void exec(Graphics2D feuille){
		feuille.setColor(couleur);
		feuille.drawRect(posX, posY, lgW, hauteur);
	}	
}

class FR extends Fonction {

	private int posX, posY, lgW, hauteur;
	private Color couleur;
	
	public FR (int x, int y, int w, int h, Color col){
		posX = x;
		posY = y;
		lgW = w;
		hauteur = h;
		couleur = col;
		
		
	}
	
	public void exec(Graphics2D feuille){
		feuille.setColor(couleur);
		feuille.fillRect(posX, posY, lgW, hauteur);
	}	
}


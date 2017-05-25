package core;

import base.*;
import java.util.*;

public class Sommet {
	
	/*
	 * attributes
	 */
	public int numero;
	public float longitude ;
	public float latitude ;
	public int nsuccesseurs;
	public ArrayList<Arc> tableauArc;
	
	/*
	 * constructeur
	 */
	public Sommet (int numero, float longitude, float latitude, int nsuccesseurs) {
		this.numero = numero;
		this.nsuccesseurs = nsuccesseurs;
		this.tableauArc = new ArrayList<Arc>();
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public Sommet () {}
	
	/*
	 * methodes
	 */
	
	/*
	 * vérifier dans la liste des successeurs 
	 * si il existe un arc lié ce sommet avec le sommet passé en paramètre
	 */
	public Arc trouveArc(Sommet dest_sommet){
		Arc res = new Arc();
		res = null;
		double longueur = (double) Integer.MAX_VALUE;
		for (Arc arc : this.tableauArc){
			if (arc.sommetArrive.numero == dest_sommet.numero && arc.longueur < longueur){
				res = arc;
			}
		}
		return res;
	}
	
	// retourner le descripteur de l'arc qui lie ce sommet 
	//avec le sommet passé en paramètre s'il existe
	public Descripteur trouveDescri (Sommet sommetArrive){
		Arc arc = new Arc();
		if( (arc = trouveArc(sommetArrive)) != null){
			return arc.descri;
		}
		return null;
	}
	
	public String toString(){
		String str = new String();	
		str += " numéro "+this.numero+
				" nombre de successeur "+this.nsuccesseurs+
				" longitude "+this.longitude+" latitude "+this.latitude+"\n";
		for (Arc arc : tableauArc){
			str += arc.toString()+"\n";
		}
		return str;
	}
	
	
}
package core;

import java.awt.Color;

import base.*;

public class Arc {
	/*
	 * Attributs
	 */

	public Sommet sommetDepart;
	public Sommet sommetArrive;
	public Descripteur descri;
	public int nb_segm;
	public float[] segLong;
	public float[] segLat;
	public double longueur;

	/*
	 * Constructeur
	 */

	public Arc(Sommet sommetDepart, Sommet sommetArrive, Descripteur descri, int nb_segm,double longueur){
		this.sommetDepart = sommetDepart ;
		this.sommetArrive = sommetArrive;
		this.descri = descri;
		this.nb_segm = nb_segm;
		this.longueur = longueur;
	}

	public Arc(){}

	/*
	 * Méthodes
	 */

	// calculer le cout d'un arc
	// cout arc est un valeur de temps en min
	public double coutArc(){
		double cout = ((double)this.longueur/((double)this.descri.vitesseMax() /3.6));
		return (cout/60.0);
	}

	/*
	 *  dessiner un arc en dessinant des segments de cet arc, 
	 */
	public void desssinArc(Dessin dessin){
		float current_long = this.sommetDepart.longitude;
		float current_lat = this.sommetDepart.latitude;
		float delta_long ;
		float delta_lat ;

		dessin.setWidth(2);
		for (int i = 0 ; i < nb_segm ; i++) {	
			dessin.drawPoint(current_long, current_lat, 5);
			delta_long = this.segLong[i];
			delta_lat = this.segLat[i];
			dessin.drawLine(current_long, current_lat, (current_long + delta_long), (current_lat + delta_lat)) ;

			current_long += delta_long ;
			current_lat  += delta_lat ;
		}
		dessin.drawLine(current_long, current_lat, this.sommetArrive.longitude,this.sommetArrive.latitude);	
	}

	public String toString(){
		return " Arc "+" sommet depart "+this.sommetDepart.numero + " sommet arrive "+this.sommetArrive.numero+" "+	this.descri.toString();
	}
}









































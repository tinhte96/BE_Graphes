package core;

import base.*;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * J'ai essayé de changer un peu le structure de Chemin.
 * Dans cette version, chemin est un liste de sommets
 */

import base.Utils;

public class Chemin {

	/*
	 * Attributs
	 */
	public Sommet[] listeSommets;
	public int nbSommets;
	public double coutChemin = 0;     


	/*
	 *Constructeur 
	 */

	public Chemin (DataInputStream dis, String nom_chemin, Sommet[] tableauSommets){
		try {

			// Verification du magic number et de la version du format du fichier .path
			int magic = dis.readInt () ;
			int version = dis.readInt () ;

			// Lecture de l'identifiant de carte
			int path_carte = dis.readInt () ;
			System.out.println(path_carte);

			int nb_noeuds = dis.readInt () ;
			this.nbSommets = nb_noeuds;

			// Origine du chemin
			int first_zone = dis.readUnsignedByte() ;
			int first_node = Utils.read24bits(dis) ;

			// Destination du chemin
			int last_zone  = dis.readUnsignedByte() ;
			int last_node = Utils.read24bits(dis) ;


			int current_zone = 0 ;
			int current_node = 0 ;

			this.listeSommets = new Sommet[nb_noeuds];
			// Tous les noeuds du chemin
			for (int i = 0 ; i < nb_noeuds ; i++) {
				current_zone = dis.readUnsignedByte() ;
				current_node = Utils.read24bits(dis) ;
				this.listeSommets[i] = tableauSommets[current_node];
			}

			this.coutChemin() ; 
			
		} catch (IOException e) {
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}
	
	//convertir un arrayList en Chemin
	public Chemin(ArrayList<Sommet> arrayList){
		this.nbSommets = arrayList.size();
		this.listeSommets = arrayList.toArray(new Sommet[this.nbSommets]);
	}
	
	public Chemin(){}

	/*
	 * Méthodes
	 */

	// calculer le cout de chemin
	public void coutChemin(){   
		Arc arc = new Arc();
		//System.out.println(this.nbSommets);
		for (int i = 0; i < this.nbSommets-1;i++){
			arc = this.listeSommets[i].trouveArc(this.listeSommets[i+1]);
			this.coutChemin += arc.coutArc();
		}

	} 

	/*
	 *  dessiner le chemin, c'est pour tester visuelement
	 *  les sommets en noir, les arcs en bleu
	 */
	public void dessineChemin(Dessin dessin){
		for (int i = 0; i < this.nbSommets-1; i++){
			dessin.drawPoint(this.listeSommets[i].longitude, this.listeSommets[i].latitude, 10);
			boolean inverse = (null == this.listeSommets[i].trouveArc(this.listeSommets[i+1]));
			Arc arc = (! inverse) ? this.listeSommets[i].trouveArc(this.listeSommets[i+1]) : this.listeSommets[i+1].trouveArc(this.listeSommets[i]);
			arc.desssinArc(dessin);
		}
		dessin.setColor(Color.black);
		dessin.drawPoint(this.listeSommets[this.nbSommets-1].longitude, this.listeSommets[this.nbSommets-1].latitude, 10);
	}

	// afficher la description d'un chemin
	public String toString(){
		String src = new String();
		for (int i= 0; i < this.nbSommets; i++){
			src += this.listeSommets[i].toString();
		}
		return src;
	}

}































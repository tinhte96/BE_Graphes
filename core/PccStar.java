package core ;

import base.*;
import java.io.* ;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import base.Readarg ;

public class PccStar extends Pcc {

	public PccStar(Graphe gr, PrintStream sortie, Readarg readarg,boolean temps) {
		super(gr, sortie, readarg,temps) ;
	}

	public void run() {
		
		long TpsCommence = System.currentTimeMillis();

		System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

		// extraire le label de l'origine 
		Label origineLabel = this.hmap.get(this.graphe.tableauSommets[this.origine]);
		Label destinationLabel = this.hmap.get(this.graphe.tableauSommets[this.destination]);

		Sommet destinationSommet = this.graphe.tableauSommets[destinationLabel.getSommet()];

		//mettre le cout d'origine 0 et ajouter au binaryHeap
		origineLabel.setCout(0);
		origineLabel.setSommetPere(origineLabel.getSommet());
		
		double trajetAVol;

		this.tas.insert(origineLabel);

		while(!this.tas.isEmpty() && !destinationLabel.isMarquage()){
			Label xLabel = this.tas.deleteMin();
			xLabel.setMarquage(true);

			//System.out.println("xlabel : " + xLabel.toString());

			Sommet xSommet = this.graphe.tableauSommets[xLabel.getSommet()];

			this.hmap.put(xSommet, xLabel);

			this.arrayLabel.add(xLabel);

			for (Arc arc : xSommet.tableauArc){

				Label yLabel = this.hmap.get(arc.sommetArrive);

				//System.out.println("ylabel : "+ yLabel.toString());

				double cout = 0;
				trajetAVol = this.graphe.distance(arc.sommetDepart.longitude, arc.sommetDepart.latitude, destinationSommet.longitude, destinationSommet.latitude);

				if (this.temps){
					cout = arc.coutArc() ; 
					trajetAVol = trajetAVol / this.graphe.getVitesse();
				} 
				else {
					cout = arc.longueur;
				}

				if (!yLabel.isMarquage()){
					if (yLabel.getCout() > cout + xLabel.getCout()){

						yLabel.setEstime(/*cout +*/ xLabel.getCout()+ trajetAVol);
						yLabel.setCout(cout + xLabel.getCout());
						yLabel.setSommetPere(xLabel.getSommet());

						//System.out.println("yLabel changé :" + yLabel.toString());

						if (this.tas.exist(yLabel)) this.tas.update(yLabel);
						else this.tas.insert(yLabel);

					}

				}
			}
		}

		this.plusCourtChemin();
		
		long TpsTermine = System.currentTimeMillis();
		
		//dessiner
		this.graphe.getDessin().setColor(Color.BLUE);
		this.plusCourt.dessineChemin(this.graphe.getDessin());

		//System.out.println(this.plusCourt.toString());
		//System.out.println(this.arrayLabel.toString());
		System.out.println("*****************************");
		System.out.print("cout final Dijkstra A* : "+coutFinal);
		if (this.temps){
			System.out.println(" min");
		}
		else {
			System.out.println(" m");
		}
		System.out.println("Temps d'exécution : " + (TpsTermine - TpsCommence)+" ms");
		System.out.println("*****************************");

	}
}

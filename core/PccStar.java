package core ;

import java.io.* ;
import java.awt.Color;

import base.Readarg ;

public class PccStar extends Pcc {

	public PccStar(Graphe gr, PrintStream sortie, Readarg readarg,boolean temps) throws NumeroSommetException {
		super(gr, sortie, readarg,temps) ;
	}

	public void run() throws SommetsConnectesException, NumeroSommetException {

		long TpsCommence = System.currentTimeMillis();

		if(!valide){
			throw new NumeroSommetException(" les sommets n'existent pas dans ce carte !!! peace");
		}
		
		this.Astar();
		
		if (this.arrayLabel.get(this.arrayLabel.size()-1).getSommet() != this.destination){
			throw new SommetsConnectesException (" le chemin des sommets donnés n'existe pas dans ce carte !!! peace");
		}
		
		this.coutFinal = this.hmap.get(this.graphe.tableauSommets[this.destination]).getCout();

		this.plusCourtChemin();

		long TpsTermine = System.currentTimeMillis();

		//dessiner
		this.graphe.getDessin().setColor(Color.BLUE);
		this.plusCourt.dessineChemin(this.graphe.getDessin());

		
		System.out.println("*****************************");
		System.out.print("cout final Dijkstra A* : "+ coutFinal);
		if (this.temps){
			System.out.println(" min");
		}
		else {
			System.out.println(" m");
		}
		System.out.println("Temps d'exécution : " + (TpsTermine - TpsCommence)+" ms");
		System.out.println("*****************************");

	}
	
	public void Astar (){
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

			Sommet xSommet = this.graphe.tableauSommets[xLabel.getSommet()];

			this.hmap.put(xSommet, xLabel);

			this.arrayLabel.add(xLabel);
			this.graphe.getDessin().setColor(Color.green);
			this.graphe.getDessin().drawPoint(xSommet.longitude,xSommet.latitude,5);

			for (Arc arc : xSommet.tableauArc){

				Label yLabel = this.hmap.get(arc.sommetArrive);

				double cout = 0;
				trajetAVol = Graphe.distance(arc.sommetArrive.longitude, arc.sommetArrive.latitude, destinationSommet.longitude, destinationSommet.latitude);

				if (this.temps){
					cout = arc.coutArc() ; 
					trajetAVol = (trajetAVol / this.graphe.getVitesseMaximum())/60.0;// trajet a vol est valeur de temps en minutes
				} 
				else {
					cout = arc.longueur;
				}

				if (!yLabel.isMarquage()){
					if (yLabel.getCout() > cout + xLabel.getCout()){
						yLabel.setEstime(cout + xLabel.getCout()+ trajetAVol);
						yLabel.setCout(cout + xLabel.getCout());
						yLabel.setSommetPere(xLabel.getSommet());

						if (this.tas.exist(yLabel)) this.tas.update(yLabel);
						else this.tas.insert(yLabel);

					}

				}
			}
		}
		
	}
}

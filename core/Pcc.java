package core ;

import java.awt.Color;
import java.io.* ;
import java.util.*;
import base.Readarg ;

public class Pcc extends Algo {

	// Numero des sommets origine et destination
	protected int zoneOrigine ;
	protected int origine ;

	protected int zoneDestination ;
	protected int destination ;

	protected ArrayList<Label> arrayLabel;
	protected BinaryHeap<Label> tas;
	protected HashMap<Sommet,Label> hmap;

	protected boolean temps ;
	protected int nbtas = 0;
	protected int tasmax = 0;

	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg,boolean temps) {
		super(gr, sortie, readarg) ;

		this.tas = new BinaryHeap<Label>();
		this.hmap = new HashMap<Sommet,Label>();
		this.arrayLabel = new ArrayList<Label>();

		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		
		if (0 > this.origine || this.origine >= gr.nbSommets) valide = false;
		if (0 > this.destination || this.destination >= gr.nbSommets) valide = false;
		
		this.temps = temps;

		//construire hmap
		for (int i = 0; i < this.graphe.nbSommets; i++){
			Label lab = new Label(false, Double.MAX_VALUE, 0, i);
			this.hmap.put(this.graphe.tableauSommets[i], lab);	
		}

	}

	public void run() throws SommetsConnectesException, NumeroSommetException {

		long TpsCommence = System.currentTimeMillis();
		
		if (!valide){
			throw new NumeroSommetException(" les sommets n'existent pas dans ce carte !!! peace");
		}

		this.Dijsktra();
		
		if (this.arrayLabel.get(this.arrayLabel.size()-1).getSommet() != this.destination){
			throw new SommetsConnectesException (" le chemin des sommets donnés n'existe pas dans ce carte !!! peace");
		}
		
		this.coutFinal = this.hmap.get(this.graphe.tableauSommets[this.destination]).getCout();

		this.plusCourtChemin();

		long TpsTermine = System.currentTimeMillis();

		//dessiner
		this.graphe.getDessin().setColor(Color.RED);
		this.plusCourt.dessineChemin(this.graphe.getDessin());

		System.out.println("*****************************");
		System.out.print("cout final Dijkstra standard : "+coutFinal);
		if (this.temps){
			System.out.println(" min");
		}
		else {
			System.out.println(" m");
		}
		System.out.println("Temps d'exécution : " + (TpsTermine - TpsCommence)+" ms");
		System.out.println("NbTas : " + nbtas);
		System.out.println("TasMax : " + tasmax);
		System.out.println("*****************************");

	}
	
	public void Dijsktra() {
		System.out.println("Run PCC de " + this.zoneOrigine + ":" + this.origine + " vers " + this.zoneDestination + ":" + this.destination) ;

		// extraire le label de l'origine 
		Label origineLabel = this.hmap.get(this.graphe.tableauSommets[this.origine]);
		Label destinationLabel = this.hmap.get(this.graphe.tableauSommets[this.destination]);

		//mettre le cout d'origine 0 et ajouter au binaryHeap
		origineLabel.setCout(0);
		origineLabel.setSommetPere(origineLabel.getSommet());

		this.tas.insert(origineLabel);
		nbtas += 1;
		tasmax += 1;

		while(!this.tas.isEmpty() && !destinationLabel.isMarquage()){
			Label xLabel = this.tas.deleteMin();
			tasmax -= 1;
			xLabel.setMarquage(true);

			Sommet xSommet = this.graphe.tableauSommets[xLabel.getSommet()];

			this.hmap.put(xSommet, xLabel);

			this.arrayLabel.add(xLabel);
			this.graphe.getDessin().setColor(Color.cyan);
			this.graphe.getDessin().drawPoint(xSommet.longitude,xSommet.latitude,5);

			for (Arc arc : xSommet.tableauArc){

				Label yLabel = this.hmap.get(arc.sommetArrive);

				double cout = 0;
				if (this.temps){
					cout = arc.coutArc();
				}
				else {
					cout = arc.longueur; 
				}

				if (!yLabel.isMarquage()){
					if (yLabel.getCout() > cout  + xLabel.getCout()){

						yLabel.setCout(cout  + xLabel.getCout());
						yLabel.setEstime(cout + xLabel.getCout());
						yLabel.setSommetPere(xLabel.getSommet());

						if (this.tas.exist(yLabel)) this.tas.update(yLabel);
						else {
							this.tas.insert(yLabel);
							nbtas += 1;
							tasmax +=1;
						}

					}			
				}		
			}
		}
	}

	public void plusCourtChemin() {
		// cree un liste du chemin
		int somInt = this.destination;
		Label label;
		ArrayList<Sommet> arrayChemin = new ArrayList<>();
		
		while (somInt != this.origine){
			Sommet som = this.graphe.tableauSommets[somInt];
			arrayChemin.add(som);
			label = this.hmap.get(som);
			somInt = label.getSommetPere();
		}
		
		arrayChemin.add(this.graphe.tableauSommets[this.origine]);
		Collections.reverse(arrayChemin);

		this.plusCourt =  new Chemin(arrayChemin);
	}

}


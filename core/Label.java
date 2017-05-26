package core;

public class Label implements Comparable<Label> {
	private boolean marquage = false;
	private double cout;
	private int sommetPere;
	private int sommet;
	private double estime; 	// estime = cout + trajet a vol 
	// pour dijsktra normal : trajet a vol est égal à 0
	// pour A star : trajet a vol est égal à distance entre les noeuds

	/*
	 * Getters et Setters
	 */

	public boolean isMarquage() {return marquage;}
	public void setMarquage(boolean marquage) {this.marquage = marquage;}

	public double getCout() {return cout;}
	public void setCout(double cout) {this.cout = cout;}

	public int getSommetPere() {return sommetPere;}
	public void setSommetPere(int sommetPere) {this.sommetPere = sommetPere;}

	public int getSommet() {return sommet;}
	public void setSommet(int sommet) {this.sommet = sommet;}

	public double getEstime(){return this.estime;}	
	public void setEstime(double valeur){this.estime = valeur;}

	/*
	 * Constructeur
	 */

	public Label(boolean marquage, double cout, int sommetPere, int sommet){
		this.marquage = marquage;
		this.cout = cout;
		this.sommetPere = sommetPere;
		this.sommet = sommet;
		this.estime = 0;
	}

	/*
	 * Méthodes
	 */

	public int compareTo(Label lab){

		if (this.estime > lab.estime){
			return 1;
		}
		else if (this.estime == lab.estime){
			return 0;
		}
		return -1;
	}

	public String toString(){
		return "Label du sommet " + this.getSommet() + 
				" / Marquage : " + this.isMarquage() + 
				" / Cout : " + this.getCout() +
				" / Sommet Pere : " + this.getSommetPere()+
				" / Cout estime : "+this.estime+ "\n";	
	}
}

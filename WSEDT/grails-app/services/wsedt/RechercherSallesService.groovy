package wsedt

import javax.jws.WebParam

import wsedt.Algo.Algo

class RechercherSallesService {
	
	static expose=['xfire']
	
	//utiliser les algo et renvoyer une string
	String rechercherSallesLight(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute).toString()
	}
	
	String rechercherSallesParCapacite(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, capacite).toString()
	}
	
	String rechercherSallesParBatiment(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment).toString()
	}
	
	String rechercherSallesFull(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment, capacite).toString()
	}
}

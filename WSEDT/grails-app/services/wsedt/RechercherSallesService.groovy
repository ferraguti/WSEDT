package wsedt

import javax.jws.WebParam
import wsedt.Algo.Algo

//Services de recherche de salles
class RechercherSallesService {
	
	static expose=['xfire']
	
	//Recherche de salles pour une date donnée
	String rechercherSallesLight(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute)
	}
	
	//Recherche de salles pour une date et une capacité minimale données
	String rechercherSallesParCapacite(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, capacite)
	}
	
	//Recherche de salles pour une date et un batiment donnés
	String rechercherSallesParBatiment(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment)
	}
	
	//Recherche de salles pour une date, une capacité minimale et un batiment donnés
	String rechercherSallesFull(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment, capacite)
	}
}

package wsedt

import javax.jws.WebParam
import wsedt.Algo.Algo

//Services de recherche de salles
class RechercherSallesService {
	
	static expose=['xfire']
	
	//Recherche de salles pour une date donn�e
	String rechercherSallesLight(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute)
	}
	
	//Recherche de salles pour une date et une capacit� minimale donn�es
	String rechercherSallesParCapacite(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, capacite)
	}
	
	//Recherche de salles pour une date et un batiment donn�s
	String rechercherSallesParBatiment(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment)
	}
	
	//Recherche de salles pour une date, une capacit� minimale et un batiment donn�s
	String rechercherSallesFull(@WebParam(name="annee", header=true) int annee, int mois, int jour, int heure, int minute, String batiment, int capacite) {
		return Algo.rechercherSalles(annee, mois, jour, heure, minute, batiment, capacite)
	}
}

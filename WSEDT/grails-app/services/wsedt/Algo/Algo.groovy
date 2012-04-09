package wsedt.Algo

import java.util.ArrayList;

import wsedt.Reservation;
import wsedt.Salle;

abstract class Algo {
	
	static ArrayList<Salle> rechercherSalles(int annee, int mois, int jour, int heure, int minute, String batiment, int capaciteMin){
		
	   Reservation arg = new Reservation(annee: annee, mois: mois, jour: jour, heure: heure, minute: minute)
	   ArrayList<Reservation> reservations = Reservation.getAll()
	   //System.out.println("Liste des reservations : " + reservations)
	   ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
	   
		//Trouver toutes les salles non disponibles
		for(Reservation debutReservation : reservations){
			Salle s = debutReservation.getSalle()
			
			if(!sallesNonLibres.contains(s)){
			   Reservation finCours = debutReservation.getFinReservation()
			  // System.out.println("Fin de la reservation : " + finCours)
			   
			   //Si la salle n'est pas libre (ie. en même temps ou entre le cours)
				if(debutReservation.enMemeTemps(arg) || (arg.estApres(debutReservation) && arg.estAvant(finCours))){
					//System.out.println("La salle " + s + " n'est pas libre")
					sallesNonLibres.add(s)
				}
			}
		}
	   
		//Les autres salles sont celles disponibles
		ArrayList<Salle> salles = Salle.getAll()
		ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
		
		for(Salle s : salles){
			if(!sallesNonLibres.contains(s))
				if(batiment.isEmpty() || s.getBatiment().equals(batiment)){ //On vérifie si le batiment de la salle est celui demandé
					if(capaciteMin == 0 || s.getCapacite() >= capaciteMin) //On vérifie si la capacité minimum de la salle est celle demandée
						sallesLibres.add(s)
				}
		}
		
		return sallesLibres
	}
	
	//Surcharge :
	
	static ArrayList<Salle> rechercherSalles(int annee, int mois, int jour, int heure, int minute){
		return rechercherSalles(annee, mois, jour, heure, minute, new String(""), 0)
	}
	
	
	 static ArrayList<Salle> rechercherSalles(int annee, int mois, int jour, int heure, int minute, String batiment){
		return rechercherSalles(annee, mois, jour, heure, minute, batiment, 0)
	}
	
	 static ArrayList<Salle> rechercherSalles(int annee, int mois, int jour, int heure, int minute, int capaciteMin){
		return rechercherSalles(annee, mois, jour, heure, minute, new String(""), capaciteMin)
	}
	
	
	 static ArrayList<Salle> rechercherSalles(int jour, int heure, int minute){
		 Date now = new Date()
		 
		return rechercherSalles(now.getYear(), now.getMonth(), jour, heure, minute, new String(""), 0)
	}
	 
	
	static ArrayList<Salle> rechercherSalles(int jour, int heure, int minute, String batiment){
		Date now = new Date()
		
		 return rechercherSalles(now.getYear(), now.getMonth(), jour, heure, minute, batiment, 0)
	 }
	 
	  static ArrayList<Salle> rechercherSalles(int jour, int heure, int minute, int capaciteMin){
		  Date now = new Date()
		  
		 return rechercherSalles(now.getYear(), now.getMonth(), jour, heure, minute, new String(""), capaciteMin)
	 }
	 


}

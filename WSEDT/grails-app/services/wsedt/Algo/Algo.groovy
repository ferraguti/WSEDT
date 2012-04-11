package wsedt.Algo

import java.util.ArrayList;

import wsedt.Reservation;
import wsedt.Salle;
import wsedt.Cours;

abstract class Algo {
	
	static String rechercherSalles(int annee, int mois, int jour, int heure, int minute, String batiment, int capaciteMin){
		
	   Reservation arg = new Reservation(annee: annee, mois: mois, jour: jour, heure: heure, minute: minute)
	   //System.out.println("Reservation pour le " + arg)
	   ArrayList<Reservation> reservations = Reservation.getAll()
	   //System.out.println("Liste des reservations : " + reservations)
	   ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
	   
		//Trouver toutes les salles non disponibles
		for(Reservation debutReservation : reservations){
			Salle s = debutReservation.getSalle()
			
			if(!sallesNonLibres.contains(s)){
			   Reservation finCours = debutReservation.getFinReservation()
			   //System.out.println("Fin de la reservation : " + finCours)
			   
			   //Si la salle n'est pas libre (ie. en même temps ou entre le cours)
			   //System.out.println("Reponse : " + arg.estApres(debutReservation))
				if(debutReservation.enMemeTemps(arg) || (arg.estApres(debutReservation) && arg.estAvant(finCours))){
					//System.out.println("La salle " + s + " n'est pas libre")
					sallesNonLibres.add(s)
				}
			}
		}
		
		//System.out.println("Liste des salles non libres : " + sallesNonLibres)
	   
		//Les autres salles sont celles disponibles
		ArrayList<Salle> salles = Salle.getAll()
		//System.out.println("Liste des salles : " + salles)
		ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
		
		for(Salle s : salles){
			//System.out.println(Algo.contient(sallesNonLibres, s))
			if(!Algo.contient(sallesNonLibres, s)) {
				if(batiment.isEmpty() || s.getBatiment().equals(batiment)){ //On vérifie si le batiment de la salle est celui demandé
					if(capaciteMin == 0 || s.getCapacite() >= capaciteMin) {//On vérifie si la capacité minimum de la salle est celle demandée
						sallesLibres.add(s)
						//System.out.println("La salle " + s + " est donc libre")
					}
				}
			}
		}
		
		return sallesLibres.toString()
	}
	
	//Pour une raison inconnue "contiens" ne marche pas ici
	 static boolean contient(ArrayList<Salle> sallesNonLibres, Salle salle){
		for(Salle s : sallesNonLibres){
			if(s.toString().equals(salle.toString()))
				return true
		}
		
		return false
	}
	
	 
	//Surcharge :
	
	static String rechercherSalles(int annee, int mois, int jour, int heure, int minute){
		return rechercherSalles(annee, mois, jour, heure, minute, new String(""), 0)
	}
	
	
	 static String rechercherSalles(int annee, int mois, int jour, int heure, int minute, String batiment){
		return rechercherSalles(annee, mois, jour, heure, minute, batiment, 0)
	}
	
	 static String rechercherSalles(int annee, int mois, int jour, int heure, int minute, int capaciteMin){
		return rechercherSalles(annee, mois, jour, heure, minute, new String(""), capaciteMin)
	}
	 
	 //Reservation à partir d'un cours
	 
	 static ArrayList<Reservation> listerReservations(Cours c){
		 ArrayList<Reservation> reservations = Reservation.getAll()
		 ArrayList<Reservation> reservationsRelated = new ArrayList<Reservation>() 
		 
		 for(r in reservations){
			 if (r.getCours().equals(c))
			 	reservationsRelated.add(r)
		 }
		 
		 return reservationsRelated
		  
	  }
	
}

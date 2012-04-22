package wsedt.Algo

import java.util.ArrayList;

import wsedt.Reservation;
import wsedt.Salle;
import wsedt.Cours;


//Classe qui contient les algorithmes utilisés pour les services
abstract class Algo {
	
	//Renvois la liste des salles disponibles pour une date, un batiment et une capacité donnés
	static String rechercherSalles(int annee, int mois, int jour, int heure, int minute, String batiment, int capaciteMin){
		
		//On calcul la reservation équivalente à la date donnée
	   Reservation arg = new Reservation(annee: annee, mois: mois, jour: jour, heure: heure, minute: minute)
	   
	   //On récupère toutes les reservations
	   ArrayList<Reservation> reservations = Reservation.getAll()
	   
	   ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
	   
		//Trouver toutes les salles non disponibles
		for(Reservation debutReservation : reservations){
			Salle s = debutReservation.getSalle()
			
			if(!sallesNonLibres.contains(s)){
			   Reservation finCours = debutReservation.getFinReservation()
			   
			   //Si la salle n'est pas libre (ie. en même temps ou entre le cours)
				if(debutReservation.enMemeTemps(arg) || (arg.estApres(debutReservation) && arg.estAvant(finCours)))
					sallesNonLibres.add(s)
			}
		}
	   
		//Les autres salles sont celles disponibles
		ArrayList<Salle> salles = Salle.getAll()
		ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
		
		for(Salle s : salles){
			if(!Algo.contient(sallesNonLibres, s)) {
				if(batiment.isEmpty() || s.getBatiment().equals(batiment)){ //On vérifie si le batiment de la salle est celui demandé
					if(capaciteMin == 0 || s.getCapacite() >= capaciteMin)//On vérifie si la capacité minimum de la salle est celle demandée
						sallesLibres.add(s)
				}
			}
		}
		
		return sallesLibres.toString()
	}
	
	//Pour une raison inconnue "List.contiens" ne marche pas ici
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
	 
	 //Renvois toutes les reservations concernant le cours donné en paramètre
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

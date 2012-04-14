package wsedt

import java.util.Date
import java.util.Calendar

class Reservation {
    Salle salle
    Cours cours
    int duree = 120 //en minute
	int annee = 2012
	int mois = 1
	int jour = 1
	int heure = 8
	int minute = 0
	String nom //utilisé pour avoir un nom identifiable dans list

    static constraints = {
        duree min: 15, max: 240
		annee min: 2012
		mois min: 1, max: 12
		jour min: 1, max: 31 // + de contrainte en vraie
		heure min: 8, max: 19
		minute min: 0, max: 59
		nom nullable: true
    }
	
	static mapping = {
		cours cascade: 'none'
	  }
	
	 Reservation(Reservation r) {
		this.salle = r.salle;
		this.cours = r.cours;
		this.duree = r.duree;
		this.annee = r.annee;
		this.mois = r.mois;
		this.jour = r.jour;
		this.heure = r.heure;
		this.minute = r.minute;
		this.nom = r.nom;
	}
	 
	Reservation(Salle salle, Cours cours, int annee, int mois, int jour, int heure, int minute){
		this.salle = salle;
		this.cours = cours;
		this.annee = annee;
		this.mois = mois;
		this.jour = jour;
		this.heure = heure;
		this.minute = minute;
		this.nom = this.toString()
	}
	
	Reservation(Salle salle, Cours cours, int annee, int mois, int jour, int heure, int minute, int duree){
		this.salle = salle;
		this.cours = cours;
		this.duree = duree;
		this.annee = annee;
		this.mois = mois;
		this.jour = jour;
		this.heure = heure;
		this.minute = minute;
		this.nom = this.toString()
	}
	
	boolean estApres(Reservation d){
		
		if(annee != d.annee)
			return (annee > d.annee)
		else if(mois != d.mois)
			return (mois > d.mois)
		else if(jour != d.jour)
			return (jour > d.jour)
		else if(heure != d.heure)
			return (heure > d.heure)
		else if(minute != d.minute)
			return (minute > d.minute)
		else
			return false
	}
	
	boolean  enMemeTemps(Reservation d){
		return(annee == d.annee && mois == d.mois && jour == d.jour && heure == d.heure && minute == d.minute)
	}
	
	boolean estAvant(Reservation d){
		return d.estApres(this)
	}
	
	String toString(){
		String fullnom = new String("Resarvation a " + heure + "h")
		
		if(minute < 10)
			fullnom += "0"
			
		fullnom += minute + ", le " + jour + "/" + mois + "/" + annee + " (" + duree + " minutes)" 
		
		return  fullnom
	}
	
	String toFullString(){
		String fullnom = new String("Reservation pour le cours " + cours + ", dans la salle " + salle + " a " + heure + "h")
		
		if(minute < 10)
			fullnom += "0"
		
		fullnom += minute + ", le " + jour + "/" + mois + "/" + annee + " (" + duree + " minutes)"
		
		return  fullnom
	}
   
    Reservation getFinReservation(){
        Reservation apresCours = new Reservation(this)
		int heure = apresCours.getHeure()
		int minute = apresCours.getMinute() + duree
		
		while(minute > 59){
			heure++
			minute -= 60
		}
		
        apresCours.setHeure(heure)
        apresCours.setMinute(minute)
        
        return apresCours;
    }

}

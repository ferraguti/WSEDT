package wsedt

class Reservation {
    Salle salle
    Cours cours
    int duree //en minute
	int annee, mois, jour, heure, minute = 0
	String nom

    static constraints = {
        duree min: 15, max: 240
		annee min: 2012
		mois min: 1, max: 12
		jour min:1, max: 31 // + de contrainte en vraie
		heure min:8, max: 19
		minute min:0, max: 59
		nom nullable: true
    }
	
	boolean estApres(Reservation d){
		if(annee > d.annee)
			return true
		else if(mois > d.mois)
			return true
		else if(jour > d.jour)
			return true
		else if(heure > d.heure)
			return true
		else if(minute > d.minute)
			return true
		else
			return false
	}
	
	boolean  enMemeTemps(Reservation d){
		return(annee == d.annee && mois == d.mois && jour == d.jour && heure == d.heure && minute == d.minute)
	}
	
	boolean estAvant(Reservation d){
		return !(estApres(d))
	}
	
	String toString(){
		return "Resarvation a " + heure + "h" + minute + ", le " + jour + "/" + mois + "/" + annee + " (" + duree + " minutes)" 
	}
   
    /*Reservation getFinReservation(){
        Reservation apresCours = new Reservation(this)
        apresCours.setHeure(heure + (duree / 60))
        apresCours.setMinute(minute + (duree % 60))
        
        return apresCours;
    }
   
   
    //a mettre dans quelle classe ?
    static ArrayList<Salle> rechercher(Date d){
        //+ filtrer les salles
        //+ int jour int heure
       
        ArrayList<Reservation> reservations //ICI RECUPERER LA LISTE DES RESERVATION
       
        ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
       
        //trouver toutes les salles non disponibles
        for(Reservation r : reservations){
            if(!sallesNonLibres.contains(r.salle)){
                Date debutCours = r.date
                Date apresCours = r.getFinCours()
               
                if(debutCours.equals(d) || (d.after(debutCours) && d.before(apresCours)))
                    sallesNonLibres.add(r.salle)
            }
        }
       
        //les autres salles sont celles disponibles
        ArrayList<Salle> salles //ICI RECUPERER LA LISTE DES SALLES
        ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
        for(Salle s : salles){
            if(!sallesNonLibres.contains(s))
                sallesLibres.add(s)
               
        }
       
        return sallesLibres
    }*/

}

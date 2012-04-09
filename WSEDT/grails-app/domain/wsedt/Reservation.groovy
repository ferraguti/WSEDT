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
   
   
    //a mettre dans quelle classe ?
    static String rechercherSalles(int annee, int mois, int jour, int heure, int minute){
        //+ filtrer les salles
        //+ int jour int heure
		
       Reservation arg = new Reservation(annee: annee, mois: mois, jour: jour, heure: heure, minute: minute)
       ArrayList<Reservation> reservations = Reservation.getAll()
       ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
       
        //trouver toutes les salles non disponibles
        for(Reservation r : reservations){
			Salle s = r.getSalle()
			
            if(!sallesNonLibres.contains(s)){
               Reservation finCours = r.getFinReservation()
               
                if(r.equals(arg) || (r.estApres(arg) && r.estAvant(finCours)))
                    sallesNonLibres.add(s)
            }
        }
       
        //les autres salles sont celles disponibles
        ArrayList<Salle> salles = Salle.getAll()
        ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
		
        for(Salle s : salles){
            if(!sallesNonLibres.contains(s))
                sallesLibres.add(s)
        }
       
        return sallesLibres.toString()
    }

}

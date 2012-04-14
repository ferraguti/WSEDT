package wsedt


import javax.jws.WebParam
import wsedt.Algo.Algo
import wsedt.Reservation

class ReservationParCoursService {
	
	static expose=['xfire']
	
	String listerReservations(@WebParam(name="coursNom", header=true)String coursNom){
		Cours c = Cours.findByNom(coursNom)
		
		//L'utilisation d'assert pour ce genre de chose n'est pas pratique. Nous voulons renvoyer une string quoi qu'il arrive et pas une exception
		try{
			c.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'existe pas"
		}

		ArrayList<Reservation> reservations = Algo.listerReservations(c)
			
		ArrayList<String> res = new ArrayList<String>()
			
		for(r in reservations)
			res.add(r.toFullString())
			
		return res.toString()
	}
	
	String supprimerReservation(@WebParam(name="coursNom", header=true)String coursNom){
		Cours c = Cours.findByNom(coursNom)
		
		try{
			c.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'existe pas"
		}
		
		try{
			c.getReservation().delete(flush: true)
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'a pas de reservation associe"
		}
		
		c.save(failOnError: true)
		
		return "La reservation associee au cours " + coursNom + " a ete supprimee avec succes"

	}
	
	String creerReservation(@WebParam(name="coursNom", header=true)String coursNom, @WebParam(name="salleNom", header=true)String salleNom, @WebParam(name="annee", header=true) int annee, @WebParam(name="mois", header=true) int mois, @WebParam(name="jour", header=true) int jour, @WebParam(name="heure", header=true) int heure, @WebParam(name="minute", header=true) int minute, @WebParam(name="duree", header=true)int duree){
		Cours c = Cours.findByNom(coursNom)
		
		try{
			c.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'existe pas"
		}
		
		try{
			c.getReservation().id
		}
		catch(NullPointerException e){
			Salle s = Salle.findByNom(salleNom)
			
			try{
				s.id
			}
			catch(NullPointerException ex){
				return "La salle " + s + " n'existe pas. Il faut donner le nom de la salle seulement, pas celui du batiment"
			}
			
			Reservation res = new Reservation(s, c, annee, mois, jour, heure, minute, duree).save(failOnError: true)
			return "La " + res + " a ete cree avec succes"
		}
		
		return "Le cours " + coursNom + " possede deja une reservation associe"
		
	}
	
	String modifierReservationValeur(@WebParam(name="coursNom", header=true)String coursNom, @WebParam(name="camp", header=true) String champ, @WebParam(name="valeur", header=true) int valeur){
		Cours c = Cours.findByNom(coursNom)
		
		try{
			c.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'existe pas"
		}
		
		Reservation r = c.getReservation()
		
		try{
			r.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'a pas de reservation associe"
		}
		
		Reservation r2 = new Reservation(r)
		
		if(champ.equals("annee"))
			r2.setAnnee(valeur)
		else if(champ.equals("mois"))
			r2.setMois(valeur)
		else if(champ.equals("jour"))
			r2.setJour(valeur)
		else if(champ.equals("heure"))
			r2.setHeure(valeur)
		else if(champ.equals("minute"))
			r2.setMinute(valeur)
		else if(champ.equals("duree"))
			r2.setDuree(valeur)
		else
			return ("Le champ " + champ + " est inconnu")
		
		//Seul façon que j'ai trouver de forcer la mise a jour de la valeur
		r.delete(flush: true)
		r2.save(failOnError: true)
		
		
		return "La reservation du cours " + coursNom + " a ete modifiee avec succes"
	}
	
	String modifierReservationSalle(@WebParam(name="coursNom", header=true)String coursNom, @WebParam(name="salleNom", header=true) String salleNom){
		Cours c = Cours.findByNom(coursNom)
		
		try{
			c.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'existe pas"
		}
		
		Reservation r = c.getReservation()
		
		try{
			r.id
		}
		catch(NullPointerException e){
			return "Le cours " + coursNom + " n'a pas de reservation associe"
		}
		
		Salle s = Salle.findByNom(salleNom)
		
		try{
			s.id
		}
		catch(NullPointerException ex){
			return "La salle " + salleNom + " n'existe pas. Il faut donner le nom de la salle seulement, pas celui du batiment"
		}
		
		Reservation r2 = new Reservation(r)
		r2.setSalle(s)
		r.delete(flush: true)
		r2.save(failOnError: true)
		
		return "La salle du cours " + coursNom + " a ete modifiee avec succes"
	}
	
}

package wsedt


import javax.jws.WebParam
import wsedt.Algo.Algo
import wsedt.Reservation

class ReservationParCoursService {
	
	static expose=['xfire']
	
	String listerReservations(@WebParam(name="coursNom", header=true)String coursNom){
		Cours c = Cours.findByNom(coursNom)
		
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
}

import wsedt.Cours

import wsedt.Reservation
import wsedt.Salle


class BootStrap {

    def init = { servletContext ->
		
		if(!Salle.count()){
			new Salle(nom: "200", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "201", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "202", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "203", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "204", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "205", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "206", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "207", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "208", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "209", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "210", batiment: "U3", capacite: 20).save(failOnError: true)
			new Salle(nom: "301", batiment: "U4", capacite: 40).save(failOnError: true)
			new Salle(nom: "302", batiment: "U4", capacite: 40).save(failOnError: true)
			new Salle(nom: "10", batiment: "1R1", capacite: 30).save(failOnError: true)
			new Salle(nom: "B7", batiment: "1TP1", capacite: 40).save(failOnError: true)
			new Salle(nom: "B8", batiment: "1TP1", capacite: 50).save(failOnError: true)
			new Salle(nom: "B17", batiment: "1TP1", capacite: 40).save(failOnError: true)
			new Salle(nom: "B18", batiment: "1TP1", capacite: 40).save(failOnError: true)
		}
		
		if(!Cours.count()){
			new Cours(nom: "Design Pattern MVC", nbrInscrits: 40).save(failOnError: true)
			new Cours(nom: "IAWS Projet, TP3", nbrInscrits: 20).save(failOnError: true)
			new Cours(nom: "IAWS Projet, TP4", nbrInscrits: 20).save(failOnError: true)
			new Cours(nom: "JEE Projet, TP6", nbrInscrits: 20).save(failOnError: true)
			new Cours(nom: "JEE Projet, TP7", nbrInscrits: 20).save(failOnError: true)
		}
		
		if(!Reservation.count()){
			new Reservation(salle: Salle.findByNom("10"), cours: Cours.findByNom("Design Pattern MVC"), duree: 120, annee: 2012, mois: 4, jour: 3, heure: 8, minute: 0).save(failOnError: true)
			new Reservation(salle: Salle.findByNom("203"), cours: Cours.findByNom("IAWS Projet, TP3"), duree: 120, annee: 2012, mois: 4, jour: 5, heure: 8, minute: 0).save(failOnError: true)
			//println(Reservation.rechercherSalles(2012, 4, 3, 8, 0))
			//println(Reservation.rechercherSalles(2012, 4, 3, 9, 19))
			//println(Reservation.rechercherSalles(2012, 4, 3, 10, 1))
		}
		
  

  }
    def destroy = {
    }
}


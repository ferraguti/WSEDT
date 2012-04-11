import wsedt.Cours

import wsedt.Reservation
import wsedt.Salle
import wsedt.Algo.Algo


class BootStrap {

    def init = { servletContext ->
		
		if(!Salle.count()){
			new Salle(nom: "10", batiment: "1R1", capacite: 30).save(failOnError: true)
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
			new Salle(nom: "211", batiment: "U4", capacite: 40).save(failOnError: true)
			new Salle(nom: "301", batiment: "U4", capacite: 40).save(failOnError: true)
			new Salle(nom: "302", batiment: "U4", capacite: 40).save(failOnError: true)
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
			new Cours(nom: "MA TD : Scrum BDD", nbrInscrits: 15).save(failOnError: true)
			new Cours(nom: "QCM JEE et IAWS", nbrInscrits: 20).save(failOnError: true)
		}
		
		if(!Reservation.count()){
			new Reservation(Salle.findByNom("10"), Cours.findByNom("Design Pattern MVC"), 2012,  4, 3, 8,  0).save(failOnError: true)
			new Reservation(Salle.findByNom("203"), Cours.findByNom("IAWS Projet, TP3"), 2012,  4, 5, 8,  0).save(failOnError: true)
			new Reservation(Salle.findByNom("211"), Cours.findByNom("MA TD : Scrum BDD"), 2012,  4, 3, 13,  30).save(failOnError: true)
			new Reservation(Salle.findByNom("204"), Cours.findByNom("IAWS Projet, TP4"), 2012,  4, 6, 10, 0).save(failOnError: true)
			new Reservation(Salle.findByNom("205"), Cours.findByNom("JEE Projet, TP6"), 2012,  4, 5, 10, 0).save(failOnError: true)
			new Reservation(Salle.findByNom("203"), Cours.findByNom("JEE Projet, TP7"), 2012,  4, 5, 13,  30).save(failOnError: true)
			new Reservation(Salle.findByNom("210"), Cours.findByNom("QCM JEE et IAWS"), 2012,  4, 6, 12, 0, 15).save(failOnError: true)
		}

  }
    def destroy = {
    }
}


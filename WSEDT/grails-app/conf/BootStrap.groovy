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
//			new Reservation(salle: Salle.findByNom("10"), cours: Cours.findByNom("Design Pattern MVC"), annee: 2012, mois: 4, jour: 3, heure: 8, minute: 0).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("203"), cours: Cours.findByNom("IAWS Projet, TP3"), annee: 2012, mois: 4, jour: 5, heure: 8, minute: 0).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("211"), cours: Cours.findByNom("MA TD : Scrum BDD"), annee: 2012, mois: 4, jour: 3, heure: 13, minute: 30).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("204"), cours: Cours.findByNom("IAWS Projet, TP4"), annee: 2012, mois: 4, jour: 6, heure: 10).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("205"), cours: Cours.findByNom("JEE Projet, TP6"), annee: 2012, mois: 4, jour: 5, heure: 10).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("203"), cours: Cours.findByNom("JEE Projet, TP7"), annee: 2012, mois: 4, jour: 5, heure: 13, minute: 30).save(failOnError: true)
//			new Reservation(salle: Salle.findByNom("210"), cours: Cours.findByNom("QCM JEE et IAWS"), annee: 2012, mois: 4, jour: 6, heure: 12, duree: 15).save(failOnError: true)
//			
			//System.out.println(Algo.rechercherSalles(2012, 4, 3, 9, 59, "1TP1", 41))
		}
		
  

  }
    def destroy = {
    }
}


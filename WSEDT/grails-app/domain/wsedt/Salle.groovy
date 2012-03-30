package wsedt

class Salle {
	String nom, batiment
	int capacite
	
	//boolean tp
	
	static belongsTo = [reservation : Reservation]

    static constraints = {
		nom blank: false, unique: true
		capacite min:1
    }
}

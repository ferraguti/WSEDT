package wsedt

class Cours {
	String nom
	int nbrInscrits
	
	static belongsTo = [reservation : Reservation]

    static constraints = {
		nom blank: false, unique: true
		nbrInscrits min: 1
    }
}

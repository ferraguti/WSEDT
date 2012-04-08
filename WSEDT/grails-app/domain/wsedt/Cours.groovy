package wsedt

class Cours {
	String nom
	int nbrInscrits
	
	static belongsTo = [reservation : Reservation]

    static constraints = {
		nom blank: false, unique: true
		nbrInscrits min: 1
		reservation nullable: true
    }
	
	String toString(){
		return nom
	}
}

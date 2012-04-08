package wsedt

class Salle {
	String nom, batiment
	int capacite
	
	//boolean tp

    static constraints = {
		nom blank: false, unique: true
		capacite min:1
		batiment blank:false
    }
	
	String toString(){
		return batiment + "-" + nom
	}
}

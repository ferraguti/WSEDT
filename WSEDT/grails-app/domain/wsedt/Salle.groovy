package wsedt

class Salle {
	String nom, batiment
	int capacite

    static constraints = {
		nom blank: false
		capacite min:1
		batiment blank:false
    }
	
	String toString(){
		return batiment + "-" + nom
	}
}

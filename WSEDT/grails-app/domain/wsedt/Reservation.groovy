package wsedt

class Reservation {
    Date date //calendar ?
    Salle salle
    Cours cours
    float duree //en heure

    static constraints = {
        duree min: 0.25f
    }
   
    /*Date getFinCours(){
        Date apresCours = new Date(date)
        apresCours.setHours(date.getHours() + duree)
        return apresCours;
    }
   
   
    //a mettre dans quelle classe ?
    static ArrayList<Salle> rechercher(Date d){
        //+ filtrer les salles
        //+ int jour int heure
       
        ArrayList<Reservation> reservations //ICI RECUPERER LA LISTE DES RESERVATION
       
        ArrayList<Salle> sallesNonLibres = new ArrayList<Salle>()
       
        //trouver toutes les salles non disponibles
        for(Reservation r : reservations){
            if(!sallesNonLibres.contains(r.salle)){
                Date debutCours = r.date
                Date apresCours = r.getFinCours()
               
                if(debutCours.equals(d) || (d.after(debutCours) && d.before(apresCours)))
                    sallesNonLibres.add(r.salle)
            }
        }
       
        //les autres salles sont celles disponibles
        ArrayList<Salle> salles //ICI RECUPERER LA LISTE DES SALLES
        ArrayList<Salle> sallesLibres = new ArrayList<Salle>()
        for(Salle s : salles){
            if(!sallesNonLibres.contains(s))
                sallesLibres.add(s)
               
        }
       
        return sallesLibres
    }*/

}
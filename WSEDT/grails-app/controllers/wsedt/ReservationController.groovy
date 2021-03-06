package wsedt

import org.springframework.dao.DataIntegrityViolationException

class ReservationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reservationInstanceList: Reservation.list(params), reservationInstanceTotal: Reservation.count()]
    }

    def create() {
        [reservationInstance: new Reservation(params)]
    }

    def save() {
        def reservationInstance = new Reservation(params)
		
		//V�rifie que la salle est asse grande pour le cours
		if(reservationInstance.getSalle().getCapacite() < reservationInstance.getCours().getNbrInscrits()){
			flash.message = "Erreur : la salle ne contient pas assez de place pour accueillir le cours"
			redirect(action: "create")
		}
		else{
	        if (!reservationInstance.save(flush: true)) {
	            render(view: "create", model: [reservationInstance: reservationInstance])
	            return
	        } 
				//Mise � jour du nom
				reservationInstance.setNom(reservationInstance.toString())
		
				flash.message = message(code: 'default.created.message', args: [message(code: 'reservation.label', default: 'Reservation'), reservationInstance.id])
		        redirect(action: "show", id: reservationInstance.id)
			}
    }

    def show() {
        def reservationInstance = Reservation.get(params.id)
        if (!reservationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "list")
            return
        }

        [reservationInstance: reservationInstance]
    }

    def edit() {
        def reservationInstance = Reservation.get(params.id)
        if (!reservationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "list")
            return
        }

        [reservationInstance: reservationInstance]
    }

    def update() {
        def reservationInstance = Reservation.get(params.id)
        if (!reservationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (reservationInstance.version > version) {
                reservationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'reservation.label', default: 'Reservation')] as Object[],
                          "Another user has updated this Reservation while you were editing")
                render(view: "edit", model: [reservationInstance: reservationInstance])
                return
            }
        }

        reservationInstance.properties = params

        if (!reservationInstance.save(flush: true)) {
            render(view: "edit", model: [reservationInstance: reservationInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'reservation.label', default: 'Reservation'), reservationInstance.id])
        redirect(action: "show", id: reservationInstance.id)
    }

    def delete() {
        def reservationInstance = Reservation.get(params.id)
        if (!reservationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "list")
            return
        }

        try {
            reservationInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'reservation.label', default: 'Reservation'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}

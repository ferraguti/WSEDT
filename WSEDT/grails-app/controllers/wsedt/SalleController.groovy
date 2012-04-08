package wsedt

import org.springframework.dao.DataIntegrityViolationException

class SalleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [salleInstanceList: Salle.list(params), salleInstanceTotal: Salle.count()]
    }

    def create() {
        [salleInstance: new Salle(params)]
    }

    def save() {
        def salleInstance = new Salle(params)
        if (!salleInstance.save(flush: true)) {
            render(view: "create", model: [salleInstance: salleInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'salle.label', default: 'Salle'), salleInstance.id])
        redirect(action: "show", id: salleInstance.id)
    }

    def show() {
        def salleInstance = Salle.get(params.id)
        if (!salleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "list")
            return
        }

        [salleInstance: salleInstance]
    }

    def edit() {
        def salleInstance = Salle.get(params.id)
        if (!salleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "list")
            return
        }

        [salleInstance: salleInstance]
    }

    def update() {
        def salleInstance = Salle.get(params.id)
        if (!salleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (salleInstance.version > version) {
                salleInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'salle.label', default: 'Salle')] as Object[],
                          "Another user has updated this Salle while you were editing")
                render(view: "edit", model: [salleInstance: salleInstance])
                return
            }
        }

        salleInstance.properties = params

        if (!salleInstance.save(flush: true)) {
            render(view: "edit", model: [salleInstance: salleInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'salle.label', default: 'Salle'), salleInstance.id])
        redirect(action: "show", id: salleInstance.id)
    }

    def delete() {
        def salleInstance = Salle.get(params.id)
        if (!salleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "list")
            return
        }

        try {
            salleInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'salle.label', default: 'Salle'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}

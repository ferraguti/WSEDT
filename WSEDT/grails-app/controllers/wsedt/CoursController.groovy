package wsedt

import org.springframework.dao.DataIntegrityViolationException

class CoursController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [coursInstanceList: Cours.list(params), coursInstanceTotal: Cours.count()]
    }

    def create() {
        [coursInstance: new Cours(params)]
    }

    def save() {
        def coursInstance = new Cours(params)
        if (!coursInstance.save(flush: true)) {
            render(view: "create", model: [coursInstance: coursInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'cours.label', default: 'Cours'), coursInstance.id])
        redirect(action: "show", id: coursInstance.id)
    }

    def show() {
        def coursInstance = Cours.get(params.id)
        if (!coursInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "list")
            return
        }

        [coursInstance: coursInstance]
    }

    def edit() {
        def coursInstance = Cours.get(params.id)
        if (!coursInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "list")
            return
        }

        [coursInstance: coursInstance]
    }

    def update() {
        def coursInstance = Cours.get(params.id)
        if (!coursInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (coursInstance.version > version) {
                coursInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'cours.label', default: 'Cours')] as Object[],
                          "Another user has updated this Cours while you were editing")
                render(view: "edit", model: [coursInstance: coursInstance])
                return
            }
        }

        coursInstance.properties = params

        if (!coursInstance.save(flush: true)) {
            render(view: "edit", model: [coursInstance: coursInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'cours.label', default: 'Cours'), coursInstance.id])
        redirect(action: "show", id: coursInstance.id)
    }

    def delete() {
        def coursInstance = Cours.get(params.id)
        if (!coursInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "list")
            return
        }

        try {
            coursInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'cours.label', default: 'Cours'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}

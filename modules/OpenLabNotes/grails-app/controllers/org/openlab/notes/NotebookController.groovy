package org.openlab.notes

import org.hibernate.criterion.CriteriaSpecification
import org.openlab.security.User

class NotebookController {

    def scaffold = true
    def springSecurityService
    def noteAccessService
    def notebookAccessService

    def create() {
        def notebookInstance = new Notebook(params)
        def otherUsers = User.list()
        otherUsers.remove(springSecurityService.currentUser)

        def accessibleNoteItems = noteAccessService.listOfNotesWithAccess()
        [notebookInstance: notebookInstance, otherUsers: otherUsers, accessibleNoteItems: accessibleNoteItems, bodyOnly: true]
    }

    def save() {
        params.accessLevel = "user"

        def notes
        if(params.notes != "")
            notes = params["notes"]?.split(",").collect{NoteItem.get(it as Long)}
        def shared
        if(params.shared != "")
            shared = params["shared"]?.split(",").collect{User.get(it as Long)}

        params.remove("notes")
        params.remove("shared")

        def notebookInstance = new Notebook(params)
        notebookInstance.notes = notes
        notebookInstance.shared = shared

        if (!notebookInstance.save(flush: true)) {
            render(view: "create", model: [notebookInstance: notebookInstance, bodyOnly: true])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), notebookInstance.id])
        redirect(action: "show", id: notebookInstance.id, params:[bodyOnly: true]) // , params:[bodyOnly: true]
    }

    def list(){
        User loggedInUser = springSecurityService.currentUser
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset?:0

        def notebookInstanceList = Notebook.withCriteria{
            createAlias("shared", "sh", CriteriaSpecification.LEFT_JOIN)
            createAlias("creator", "cr")
            or{
                eq 'sh.username', loggedInUser.username
                eq 'cr.username', loggedInUser.username
            }
            order("id", "asc")
        }

        def notebookInstanceListTotal = notebookInstanceList?.size()?:0
        if (params.int('offset') > notebookInstanceListTotal) params.offset = 0

        if(notebookInstanceListTotal > 0)
        {
            int rangeMin = Math.min(notebookInstanceListTotal, params.int('offset'))
            int rangeMax = Math.min(notebookInstanceListTotal, (params.int('offset') + params.int('max')))

            notebookInstanceList = notebookInstanceList.asList()
            notebookInstanceList.sort{ a,b -> a.id <=> b.id}
            if(params.order == "desc") notebookInstanceList = notebookInstanceList.reverse()

            notebookInstanceList = notebookInstanceList.asList().subList(rangeMin, rangeMax)
        }

        [notebookInstanceList: notebookInstanceList, notebookInstanceTotal: notebookInstanceListTotal, bodyOnly: true]	}


    def edit() {

        def notebookInstance = Notebook.get(params.id)
        if(!notebookAccessService.grantAccess(notebookInstance)){
            flash.message = "You do not have access to this particular notebook!"
            redirect(action: "list")
        }
        if (!notebookInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'notebook.label', default: 'Notebook'), id])
            redirect(action: "list")
            return
        }
        def otherUsers = User.list()
        if(notebookInstance.creator == springSecurityService.currentUser)
            otherUsers.remove(springSecurityService.currentUser)

        def accessibleNoteItems = noteAccessService.listOfNotesWithAccess()
        [notebookInstance: notebookInstance, otherUsers: otherUsers, accessibleNoteItems: accessibleNoteItems, bodyOnly: true]
    }

    def update() {

        def notes
        if(params.notes != "")
            notes = params["notes"]?.split(",").collect{NoteItem.get(it as Long)}
        def shared
        if(params.shared != "")
            shared = params["shared"]?.split(",").collect{User.get(it as Long)}

        params.remove("notes")
        params.remove("shared")

        def notebookInstance = Notebook.get(params.id)
        def version = params.version
        notebookInstance.notes = notes
        notebookInstance.shared = shared

        if(!notebookAccessService.grantAccess(notebookInstance)){
            flash.message = "You do not have access to this particular notebook!"
            redirect(action: "list")
        }
        if (!notebookInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (notebookInstance.version > version) {
                notebookInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'noteItem.label', default: 'NoteItem')] as Object[],
                        "Another user has updated this Notebook while you were editing")
                render(view: "edit", model: [notebookInstance: notebookInstance])
                return
            }
        }

        notebookInstance.properties = params
        if (!notebookInstance.save(flush: true)) {
            render(view: "edit", model: [notebookInstance: notebookInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), notebookInstance.id])
        redirect(action: "show", id: notebookInstance.id, params:[bodyOnly: true])
    }


    def show(){
        def notebookInstance = Notebook.get(params.id)

        if(!notebookAccessService.grantAccess(notebookInstance)){
            flash.message = "You do not have access to this particular note!"
            redirect(action: "list")
        }

        params.max = 1
        params.offset = params.offset?:0

        def noteItemInstanceList = notebookInstance.notes
        def noteItemInstanceListTotal = noteItemInstanceList?.size()?:0
        if (params.int('offset') > noteItemInstanceListTotal) params.offset = 0

        if(noteItemInstanceListTotal > 0)
        {
            int rangeMin = Math.min(noteItemInstanceListTotal, params.int('offset'))
            int rangeMax = Math.min(noteItemInstanceListTotal, (params.int('offset') + params.int('max')))

            noteItemInstanceList = noteItemInstanceList.asList()
            noteItemInstanceList.sort{ a,b -> a.id <=> b.id}
            if(params.order == "desc") noteItemInstanceList = noteItemInstanceList.reverse()

            noteItemInstanceList = noteItemInstanceList.asList().subList(rangeMin, rangeMax)
        }

        [notebookInstance: notebookInstance, noteItemInstanceList: noteItemInstanceList,
                        noteItemInstanceTotal: noteItemInstanceListTotal,
                        bodyOnly: true]
    }
}

package org.openlab.notes

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.docx4j.openpackaging.io.SaveToZipFile
import org.openlab.main.DataObject
import crypttools.PGPCryptoBC
import org.openlab.security.User
import org.springframework.dao.DataIntegrityViolationException
import org.hibernate.criterion.CriteriaSpecification
import org.openlab.main.Project
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings

class NoteItemController {

	def scaffold = true

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", finalize: "POST"]

	transient springSecurityService
	
    def noteAccessService
	def noteEncryptionService
	def noteExportService
	def notebookAccessService
	def exportService

    def index() {
        redirect(action: "list", params: params)
    }
	def list(){
		User loggedInUser = springSecurityService.currentUser
		
		params.max = Math.min(params.max?params.int('max'):10, 100)
		def noteItemInstanceList
		def noteItemInstanceTotal
		def noteItemInstanceCriteria

		if(!params.offset) params.offset = 0

		noteItemInstanceCriteria = NoteItem.createCriteria()

		def dateFilterOptions = ['Created': 'dateCreated', 'Updated':'lastUpdated', 'Finalized': 'finalizedDate', 'Signed': 'supervisorSignedDate']
		noteItemInstanceList = noteItemInstanceCriteria.list(max: params.format?NoteItem.count():params.max, offset: params.format?0:params.offset) {

			if(params.beforeDateFilter)
				le(dateFilterOptions[params.dateType], params.beforeDateFilter)
			if(params.afterDateFilter)
				ge(dateFilterOptions[params.dateType], params.afterDateFilter)

			if(params.type == "own") creator{
				eq('username', loggedInUser.username)
			}
			else if(params.type == "supervisor") supervisor {
				eq('username', loggedInUser.username)
			}
			if(params.creatorFilter) creator{
				eq('username', 'admin')//params.creatorFilter)
			}
			if(params.supervisorFilter) supervisor{
				eq('username', params.supervisorFilter)
			}
			if(params.type == "shared") shared {
				eq('username', loggedInUser.username)
			}
			if(params.status)
			{
				eq('status', params.status)
			}
			if(params.lastModifierFilter) lastModifier{
				eq('username', params.lastModifierFilter)
			}
			if(params.projectFilter) projects{
				eq('name', params.projectFilter)
			}
			if(params.sort) order(params.sort, params.order)
		}
		noteItemInstanceTotal = noteItemInstanceList.totalCount

		def exportParams = params.clone()
		exportParams.offset = 0
		exportParams.max = NoteItem.count()

		if(params?.format && params.format != "html"){
			noteItemInstanceList = NoteItem.list(exportParams)
			noteItemInstanceTotal = NoteItem.count()

			response.contentType = grailsApplication.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=noteItemInstance.${params.extension}")

			def notallowed = ["dbName", "springSecurityService", "typeLabel", "beforeInsert", "beforeUpdate", "_methods_"]
			def fields = new DefaultGrailsDomainClass(NoteItem.class).persistentProperties.findAll{ !notallowed.contains(it.name) }.collect{it.name}

			exportService.export(params.format, response.outputStream, noteItemInstanceList, fields?:[], [:], [:], [:])
		}

		[noteItemInstanceList: noteItemInstanceList, noteItemInstanceTotal: noteItemInstanceTotal, bodyOnly: true]
	}

	def listOwn(){
		params.type = "own"
		redirect(action: "list", params: params)
	}

	def listSharedNotes(){
		params.type = "shared"
		redirect(action: "list", params: params)
	}

	def listSignedAsSupervisor() {
		params.type = "supervisor"
		params.status = "signed"
		redirect(action: "list", params: params)
	}

	def listNotesToSign(){
		params.type = "supervisor"
		params.status = "final"
		redirect(action: "list", params: params)
	}

	/**
	 * export note to docx
	 */
	def exportToDocx(){

		def noteItemInstance = NoteItem.get(params.id)
		if (!noteItemInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'Note'), params.id])}"
			redirect(action: "show")
		}
		else {
			    def wp = noteExportService.convertNoteToDocx(noteItemInstance.note)

				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				response.setHeader("Content-disposition", "attachment;filename=${noteItemInstance.title}.docx")
				SaveToZipFile saver = new SaveToZipFile(wp)
				saver.save( response.getOutputStream() )
		}
	}

	/**
	 * export note to pdf
	 */
	def exportToPdf(){

		def noteItemInstance = NoteItem.get(params.id)
		if (!noteItemInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'Note'), params.id])}"
			redirect(action: "show")
		}
		else {
			def wp = noteExportService.convertNoteToDocx(noteItemInstance.note)

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename=${noteItemInstance.title}.pdf")

			def c = noteExportService.convertDocxToPdf(wp)
			c.output(response.getOutputStream(), new PdfSettings() );
		}
	}

	def makePublic(){
		def noteItemInstance = NoteItem.get(params.id as long)
		if(noteItemInstance.creator == springSecurityService.currentUser)
		{
			noteItemInstance.accessLevel = "open"
			if(noteItemInstance.save(flush: true)){
				flash.message = "Note can now be accessed by all users."
				redirect(action: "show")
			}
			else{
				flash.message = "Note could not be published."
				redirect(action: "show")
			}
		}
		else{
			flash.message = "Only the author of a note has the right to make it available to all users."
			redirect(action: "show")
		}
	}

	def addToSamples(){

		def noteItemInstance = NoteItem.get(params.id as long)
		def dataObj = DataObject.get(params.dataObject)

		if(!noteItemInstance.addToDataObjects(dataObj).save(flush:true))
			flash.message = "Could not add ${dataObj} to ${noteItemInstance}."

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}

	def removeFromSamples(){
		def noteItemInstance = NoteItem.get(params.id as long)
		def dataObj = DataObject.get(params.dataObject)

		if(!noteItemInstance.removeFromDataObjects(dataObj).save(flush:true))
			flash.message = "Could not remove ${dataObj} from ${noteItemInstance}"

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}

	def addToNotebooks(){

		def noteItemInstance = NoteItem.get(params.id as long)
		def notebook = Notebook.get(params.selectAddTo as long)

		if(!noteItemInstance.addToNotebooks(notebook).save(flush:true))
			flash.message = "Could not add ${notebook} to ${noteItemInstance}."

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}

	def removeFromNotebooks(){
		def noteItemInstance = NoteItem.get(params.id as long)
		def notebook = Notebook.get(params.notebookId)

		if(!noteItemInstance.removeFromNotebooks(notebook).save(flush:true))
			flash.message = "Could not remove ${notebook} from ${noteItemInstance}"

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}


	def addToShared(){

		def noteItemInstance = NoteItem.get(params.id as long)
		def user = User.get(params.user)

		if(!noteItemInstance.addToShared(user).save(flush:true))
			flash.message = "Could not add ${user} to ${noteItemInstance}."

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}

	def removeFromShared(){
		def noteItemInstance = NoteItem.get(params.id as long)
		def user = User.get(params.user)

		if(!noteItemInstance.removeFromShared(user).save(flush:true))
			flash.message = "Could not remove ${user} from ${noteItemInstance}"

		redirect(action: "show", id: params.id, params: [bodyOnly: params.bodyOnly?:false])
	}

    def create() {
		def noteItemInstance = new NoteItem(params)
		def notebooksWithAccess = notebookAccessService.listOfNotebooksWithAccess()
        [noteItemInstance: noteItemInstance, selectedNotebook: params.addToNotebook, notebooksWithAccess: notebooksWithAccess, bodyOnly: true]
    }

    def save() {
		params.accessLevel = "user"
        def noteItemInstance = new NoteItem(params)

        if (!noteItemInstance.save(flush: true)) {
            render(view: "create", model: [noteItemInstance: noteItemInstance, bodyOnly: true])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), noteItemInstance.id])
        redirect(action: "show", id: noteItemInstance.id, params:[bodyOnly: true]) // , params:[bodyOnly: true]
    }

    def show(Long id) {
        def noteItemInstance = NoteItem.get(id)
        if(!noteAccessService.grantAccess(noteItemInstance)){
            flash.message = "You do not have access to this particular note!"
            redirect(action: "list")
        }
        if (!noteItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list")
            return
        }
		def creator = false
		def supervisor = false
		if(springSecurityService.currentUser == noteItemInstance.creator){
			creator = true
		}else if(springSecurityService.currentUser == noteItemInstance.supervisor){
			supervisor = true
		}

		/* validate signatures */
		def validAuthorSignature
		def validSupervisorSignature

		if(noteItemInstance.authorSignature)
			validAuthorSignature = noteEncryptionService.validateNote(noteItemInstance.creator, noteItemInstance.authorSignature, noteItemInstance.finalizedDate.getDateString(), noteItemInstance.note)
		if(noteItemInstance.supervisorSignature)
			validSupervisorSignature = noteEncryptionService.validateNote(noteItemInstance.supervisor, noteItemInstance.supervisorSignature, noteItemInstance.finalizedDate.getDateString() +noteItemInstance.supervisorSignedDate.getDateString(), noteItemInstance.note)

		def users = User.list()
		users.remove(springSecurityService.currentUser)
		users.removeAll(noteItemInstance.shared)

		def notebooks = Notebook.list()
		notebooks.removeAll(noteItemInstance.notebooks)

		def username = springSecurityService.currentUser.username

		def dataObjects = DataObject.withCriteria{
			createAlias("shared", "sh", CriteriaSpecification.LEFT_JOIN)
			createAlias("creator", "cr", CriteriaSpecification.LEFT_JOIN)
			or{
				eq("cr.username", username)
				isNull("accessLevel")
				eq("accessLevel", "open")
				eq 'sh.username', username
			}
		}
		dataObjects.removeAll(noteItemInstance.dataObjects)
		dataObjects.removeAll(dataObjects.findAll{it.type == "n" +
			"oteItem"})

		def projects = Project.list()
		projects.removeAll(noteItemInstance.projects)

		[noteItemInstance: noteItemInstance, bodyOnly: true,
				validAuthorSignature: validAuthorSignature,
				validSupervisorSignature: validSupervisorSignature,
				users: users, dataObjects: dataObjects,
				notebooks: notebooks,
				currentUser: username, projects: projects,
		 		creator: creator, supervisor: supervisor]
    }

    def edit(Long id) {
        def noteItemInstance = NoteItem.get(id)
        if(!noteAccessService.grantAccess(noteItemInstance)){
            flash.message = "You do not have access to this particular note!"
            redirect(action: "list")
        }
        if (!noteItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list")
            return
        }
        [noteItemInstance: noteItemInstance, bodyOnly: true]
    }

    def update(Long id, Long version) {
        def noteItemInstance = NoteItem.get(id)
        if(!noteAccessService.grantAccess(noteItemInstance)){
            flash.message = "You do not have access to this particular note!"
            redirect(action: "list")
        }
        if (!noteItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (noteItemInstance.version > version) {
                noteItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'noteItem.label', default: 'NoteItem')] as Object[],
                          "Another user has updated this NoteItem while you were editing")
                render(view: "edit", model: [noteItemInstance: noteItemInstance])
                return
            }
        }

        noteItemInstance.properties = params
        if (!noteItemInstance.save(flush: true)) {
            render(view: "edit", model: [noteItemInstance: noteItemInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), noteItemInstance.id])
        redirect(action: "show", id: noteItemInstance.id, params:[bodyOnly: true])
    }

    def delete(Long id) {
        def noteItemInstance = NoteItem.get(id)
        if (!noteItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list")
            return
        }
        else if(!noteAccessService.grantAccess(noteItemInstance)){
            flash.message = "You do not have access to this particular note!"
            redirect(action: "list")
			return
        }
        else if(noteItemInstance.status != "draft"){
            flash.message = "You are not allowed to delete finalized notes"
            redirect(action: "show", id: id)
			return
        }

        try {
            noteItemInstance.delete(flush: true, failOnError:true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "list", params:[bodyOnly: true]) // , params:[bodyOnly: true]
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
            redirect(action: "show", id: id)
        }
    }

	def signNote(Long id){
		def noteItemInstance = NoteItem.get(id)
		if (!noteItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
			redirect(action: "list")
			return
		}else if(!noteAccessService.grantAccess(noteItemInstance)){
			flash.message = "You do not have access to this particular note!"
			redirect(action: "list")
			return
		}
		/* Note exists and user has access */
		User currentUser = springSecurityService.currentUser
		
		if(UserPGP.countByOwner(currentUser) == 0){
			flash.message = "User ${currentUser} does not have any keys!"
			redirect(action: "createKeys", id: id, params:[bodyOnly: true])
			return
		}
		def users = null
		User lastSupervisor = null
		if(noteItemInstance.creator == currentUser){
			/* currentUser is the author */
			users = User.findAllByUsernameNotEqual(currentUser.toString(), [sort:"userRealName"])
			def lastNote = NoteItem.findByCreatorAndSupervisorIsNotNull(currentUser, [sort: "id", order: "desc"])
			if(lastNote != null){
				lastSupervisor = lastNote.supervisor
			}
		}
		[noteItemInstance: noteItemInstance, users: users, lastSupervisor: lastSupervisor, bodyOnly: true]
	}
	
	def signNoteData(Long id){
		def noteItemInstance = NoteItem.get(id)
		if (!noteItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'noteItem.label', default: 'NoteItem'), id])
			redirect(action: "list")
			return
		}else if(!noteAccessService.grantAccess(noteItemInstance)){
			flash.message = "You do not have access to this particular note!"
			redirect(action: "list")
			return
		}
		
		/* Note exists and user has access */
		User currentUser = springSecurityService.currentUser
		String passphrase = params.password
		params.remove('password')
		String encodedPassword = springSecurityService.encodePassword(passphrase)
		/* Check if password is correct */
		if(!currentUser.password.equals(encodedPassword)){
			flash.message = "Incorrect password!"
			redirect(action: "signNote", id: id, params:[bodyOnly: true])
			return
		}

		Date currentDate = new Date()

		if(noteItemInstance.creator == currentUser){
			/* If the author is signing, a supervisor is added to the note */
			def supervisor = User.find{id == params.supervisor}
			params.remove('supervisor')
			params.remove('status')
			
			/* Add the supervisor to the noteItem */
			noteItemInstance.properties = params
			noteItemInstance.supervisor = supervisor
			noteItemInstance.status = 'final'

			noteItemInstance.finalizedDate = currentDate
			noteItemInstance.authorSignature = noteEncryptionService.signNote(currentUser, currentDate.getDateString(), noteItemInstance.note, passphrase)
		}else if(noteItemInstance.supervisor == currentUser){
			noteItemInstance.supervisorSignedDate = currentDate
			noteItemInstance.supervisorSignature = noteEncryptionService.signNote(currentUser, noteItemInstance.finalizedDate.getDateString() + currentDate.getDateString(), noteItemInstance.note, passphrase)
			noteItemInstance.status = 'signed'
		}
		if (!noteItemInstance.save(flush: true)) {
			flash.message = "Could not save!"
			redirect(action: "signNote", id: id, params:[bodyOnly: true])
			return
		}
		flash.message = message(code: 'Note was signed', args: [message(code: 'noteItem.label', default: 'NoteItem'), noteItemInstance.id])
		redirect(action: "show", id: id, params:[bodyOnly: true])
	}
	
	def createKeys(Long id){
		[UserPGPInstance: new UserPGP(params), noteId: id, bodyOnly: true]
	}
	
	def saveKeys(Long id){
		User currentUser = springSecurityService.currentUser
		
		String passphrase = params.password
		String username = currentUser.toString()
		params.remove('password')
		String encodedPassword = springSecurityService.encodePassword(passphrase)
		
		if(!currentUser.password.equals(encodedPassword)){
			flash.message = "Incorrect password!"
            redirect(action: "createKeys", id: id, params:[bodyOnly: true])
			return
		}
		
		/* Generate keys for user */
		PGPCryptoBC pgp = new PGPCryptoBC()
		pgp.generateKeys(username, passphrase)
		
		/* Store keys on a new UserPGP instance for the user */
		def UserPGPInstance = new UserPGP()
		UserPGPInstance.owner = currentUser
		
		UserPGPInstance.secretKey = pgp.getSecretKey()
		UserPGPInstance.publicKey = pgp.getPublicKey()
		
		if(!UserPGPInstance.save(flush: true, failOnError:true)) {
			flash.message = "Something went wrong when generating your keys!"
			redirect(action: "createKeys", params:[bodyOnly: true])
			return
		}
		flash.message = 'Public and private keys has been generated for your account'
		redirect(action: "signNote", id: id, params:[bodyOnly: true])
	}
}
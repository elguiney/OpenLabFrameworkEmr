package org.openlab.notes

import org.openlab.main.DataObject
import org.openlab.security.User
import org.openlab.main.Project

class NoteItem extends DataObject{
	String title
	String status
	String note
	String authorSignature
	String supervisorSignature
	Date finalizedDate
	Date supervisorSignedDate
	User supervisor

	static hasMany = [dataObjects: DataObject, notebooks: Notebook]
	static belongsTo = [Notebook, Project]

	String toString(){
		title
	}

	static searchable = true
	
    static constraints = {
		title blank: false
		note blank: true
		authorSignature nullable: true
		supervisorSignature nullable: true
		finalizedDate nullable: true
		supervisorSignedDate nullable: true
		supervisor nullable: true
		status inList:["draft", "final", "signed"]
    }
	static mapping = {
		note type: 'text'
		authorSignature type: 'text'
		supervisorSignature type: 'text'
		table 'olfNoteItem'
	}

    static String type = "noteItem"
    static String typeLabel = "NoteItem"
}

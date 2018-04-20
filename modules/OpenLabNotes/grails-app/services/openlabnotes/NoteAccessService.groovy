package openlabnotes

import grails.plugins.springsecurity.SpringSecurityService
import org.hibernate.criterion.CriteriaSpecification
import org.openlab.notes.NoteItem
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
class NoteAccessService {

    def springSecurityService

    def listOfNotesWithAccess(){
        def loggedInUser = springSecurityService.currentUser
        def listOfNotes = NoteItem.withCriteria{
            createAlias("shared", "sh", CriteriaSpecification.LEFT_JOIN)
            createAlias("notebooks", "nb", CriteriaSpecification.LEFT_JOIN)
            createAlias("nb.shared", "nbshared", CriteriaSpecification.LEFT_JOIN)
            createAlias("creator", "cr")
            createAlias("supervisor", "su")
            or{
                eq 'nbshared.username', loggedInUser.username
                eq 'sh.username', loggedInUser.username
                eq 'cr.username', loggedInUser.username
                eq 'su.username', loggedInUser.username
            }
            order("id", "asc")
        }
        return(listOfNotes)
    }

    def grantAccess(NoteItem noteItemInstance) {
        if(noteItemInstance.accessLevel == "open"){
            return true
        } else if (springSecurityService.currentUser == noteItemInstance.creator) {
            return true
        } else if (springSecurityService.currentUser == noteItemInstance.supervisor) {
            return true
        } else if(springSecurityService.currentUser in noteItemInstance.shared) {
            return true
        } //check if notes is in a notebook a this user has access to
          else if(springSecurityService.currentUser in noteItemInstance.notebooks.collect{it.shared}.unique()){
            return true
        } else if (SpringSecurityUtils.ifAllGranted("ROLE_ADMIN")){
            return true
        }
        else return false
    }
}

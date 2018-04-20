package openlabnotes

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.hibernate.criterion.CriteriaSpecification
import org.openlab.notes.Notebook

class NotebookAccessService {
    def springSecurityService

    def listOfNotebooksWithAccess(){
        def loggedInUser = springSecurityService.currentUser
        def listOfNotebooks = Notebook.withCriteria{
            createAlias("shared", "sh", CriteriaSpecification.LEFT_JOIN)
            createAlias("creator", "cr")
            or{
                eq 'sh.username', loggedInUser.username
                eq 'cr.username', loggedInUser.username
            }
            order("id", "asc")
        }
        return(listOfNotebooks)
    }

    def grantAccess(Notebook notebookInstance) {

        if (springSecurityService.currentUser == notebookInstance.creator) {
            return true
        } else if(springSecurityService.currentUser in notebookInstance.shared){
            return true
        } else if (SpringSecurityUtils.ifAllGranted("ROLE_ADMIN")){
            return true
        }
        else return false
    }
}
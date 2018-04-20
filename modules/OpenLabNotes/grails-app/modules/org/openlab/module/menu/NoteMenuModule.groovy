package org.openlab.module.menu

import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.openlab.module.MenuModule

class NoteMenuModule implements MenuModule{

    def getPriority()
    {
        6
    }

    def getMenu()
    {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def controller = "NoteItem"
        def isAdmin = SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')

        xml.root
                {
                    submenu(label: 'Notes')
                            {
                                menuitem(controller: "notebook", action: 'create', label: 'Create notebooks')
                                menuitem(controller: "notebook", action: 'list', label: 'List notebooks')
                                menuitem(controller: controller, action: 'create', label: 'Create new note')
                                if(isAdmin) menuitem(controller: controller, action: 'list', label: 'List all notes')
                                menuitem(controller: controller, action: 'listOwn', label: 'List own notes')
                                menuitem(controller: controller, action: 'listSharedNotes', label: 'List notes shared with me')
								menuitem(controller: controller, action: 'listSignedAsSupervisor', label: 'List notes signed as supervisor')
                                menuitem(controller: controller, action: 'listNotesToSign', label: 'List notes I need to sign')
                            }
                }

        return writer.toString()
    }
}

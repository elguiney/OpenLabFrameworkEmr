package org.openlab.notes

import org.openlab.security.User
import org.openlab.main.MainObject

class Notebook extends MainObject{
    String title

    static hasMany = [shared: User, notes: NoteItem]

    static constraints = {
    }

    String toString(){
        title
    }

    static mapping = {
        table 'olfNotebook'
    }
}

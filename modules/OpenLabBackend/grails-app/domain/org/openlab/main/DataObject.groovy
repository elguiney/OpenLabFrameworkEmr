/*
 * Copyright (C) 2013 
 * Center for Excellence in Nanomedicine (NanoCAN)
 * Molecular Oncology
 * University of Southern Denmark
 * ###############################################
 * Written by:	Markus List
 * Contact: 	mlist'at'health'.'sdu'.'dk
 * Web:		http://www.nanocan.org
 * ###########################################################################
 *	
 *	This file is part of OpenLabFramework.
 *
 *  OpenLabFramework is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with this program. It can be found at the root of the project page.
 *	If not, see <http://www.gnu.org/licenses/>.
 *
 * ############################################################################
 */
package org.openlab.main

import org.openlab.security.User

/**
 * DataObject is to be extended by all domain classes that are in some kind data that is not 
 * more or less static (contrary to master data).
 * @author markus.list
 *
 */
class DataObject extends MainObject {

    static String type = "dataObject"
    static String typeLabel = "dataObject"

    static belongsTo = Project

    static hasMany = [projects: Project, shared: User]

    String accessLevel

    static constraints = {
        accessLevel nullable:true, inList: ["user", "group", "shared", "open"]
    }
    static mapping = {
        //without this, all DataObjects would be stored within one single huge datatable.
        tablePerHierarchy false
        //alter the default relation name
        table 'olfDataObject'
    }

    String toBarcode()
    {
        toString()
    }
}

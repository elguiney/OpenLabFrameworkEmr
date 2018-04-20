/*
 * Copyright (C) 2014
 * Center for Excellence in Nanomedicine (NanoCAN)
 * Molecular Oncology
 * University of Southern Denmark
 * ###############################################
 * Written by:	Markus List
 * Contact: 	mlist'at'health'.'sdu'.'dk
 * Web:		    http://www.nanocan.org
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
package org.openlab.service

import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

/**
 * Created by markus on 5/6/14.
 */
class DomainObjectCloningService {
    def deepClone(domainInstanceToClone){

        //Our target instance for the instance we want to clone
        def newDomainInstance = domainInstanceToClone.getClass().newInstance()

        //Returns a DefaultGrailsDomainClass (as interface GrailsDomainClass) for inspecting properties
        def domainClass = new DefaultGrailsDomainClass(newDomainInstance.class)

        domainClass?.persistentProperties.each{prop ->
            if(prop.association){
                if(prop.owningSide){
                    //we have to deep clone owned associations
                    if(prop.oneToOne){
                        def newAssociationInstance = deepClone(domainInstanceToClone."${prop.name}")
                        newDomainInstance."${prop.name}" = newAssociationInstance
                    }
                    else{
                        domainInstanceToClone."${prop.name}".each{ associationInstance ->
                            def newAssociationInstance = deepClone(associationInstance)
                            newDomainInstance."addTo${StringUtils.capitalize(prop.name)}"(newAssociationInstance)
                        }
                    }
                }
                else{
                    if(!prop.otherSide && prop.oneToOne){
                        //If the association isn't owned or the owner, then we can just do a  shallow copy of the reference.
                        newDomainInstance."${prop.name}" = domainInstanceToClone."${prop.name}"
                    }
                }
            }
            else{
                //If the property isn't an association then simply copy the value
                newDomainInstance."${prop.name}" = domainInstanceToClone."${prop.name}"
            }
        }

        return newDomainInstance
    }
}

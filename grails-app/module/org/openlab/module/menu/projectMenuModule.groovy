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
package org.openlab.module.menu

import org.openlab.module.*;
import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


class projectMenuModule implements MenuModule{

	def springSecurityService
	def isAdmin = SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')
	
	def getPriority()
	{
		0
	}
	
	def getMenu()
	{
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
		
		xml.root
		{
			submenu(label: 'Projects')
			{
				menuitem(controller: 'project', action: 'create', label: 'Create Project')
				menuitem(controller: 'project', action: 'list', label: 'List Projects')
			}
		}
		
		return writer.toString()
	}
}

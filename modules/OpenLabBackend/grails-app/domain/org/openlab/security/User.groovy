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
package org.openlab.security

class User implements Serializable{

    transient springSecurityService
	transient hasBeforeUpdate = false
	transient hasBeforeInsert = false

	String username
	String userRealName
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	String email

	static constraints = {
		username blank: false, unique: true
		password blank: false
		userRealName blank: false
		email email:true
	}

	static mapping = {
		password column: '`password`'
		table 'olfUser'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	String toString()
	{
		username
	}

	def beforeInsert() {
		if (!hasBeforeInsert)
		{
			hasBeforeInsert = true
			encodePassword()
		}
	}

	def beforeUpdate() {
		if (isDirty('password') && !hasBeforeUpdate) {
			hasBeforeUpdate = true
			println password
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}

}

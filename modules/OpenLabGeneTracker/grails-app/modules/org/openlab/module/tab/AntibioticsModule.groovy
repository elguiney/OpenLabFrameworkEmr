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
package org.openlab.module.tab

import org.openlab.module.*;
import org.openlab.genetracker.*;

class AntibioticsModule implements Module{

	def getPluginName() {
		"gene-tracker"
	}

	def getTemplateForDomainClass(def domainClass) 
	{
		if(domainClass.startsWith("cellLineData")) return "antibioticsTab"
		
		else return null
	}

    @Override
    def getMobileTemplateForDomainClass(Object domainClass) {
        return null
    }

    def isInterestedIn(def domainClass, def type)
	{
		if((type == "tab") && domainClass.startsWith("cellLineData")) return true
		return false
	}
	
	def getModelForDomainClass(def domainClass, def id)
	{
		if(domainClass.startsWith("cellLineData"))
		{
			def cellLineData = CellLineData.get(id)
			def antibioticsWithConc = AntibioticsWithConcentration.findAllByCellLineData(cellLineData)
            def antibioticsList = Antibiotics.list()


            [cellLineData: cellLineData, antibioticsList: antibioticsList, antibioticsWithConc: antibioticsWithConc]
		}
	}

    @Override
    def isMobile() {
        return false
    }
}
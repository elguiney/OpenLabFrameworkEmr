
<%@ page import="org.openlab.genetracker.CellLineData" %>
<!doctype html>
<html>
	<head>
        <g:setProvider library="prototype"/>
        <meta name="layout" content="body" />
		<g:set var="entityName" value="${message(code: 'cellLineData.label', default: 'CellLineData')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:remoteLink params="${[bodyOnly: true]}" class="list" action="list" update="body"><g:message code="default.list.label" args="[entityName]" /></g:remoteLink></li>
				<li><g:remoteLink params="${[bodyOnly: true]}" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:remoteLink></li>
                <li><g:remoteLink params="${[bodyOnly: true]}" class="delete" action="delete" id="${cellLineDataInstance?.id}" update="body" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                    <g:message code='default.button.delete.label' default='Delete'/></g:remoteLink></li>
			</ul>
		</div>
        <g:render template="additionalBoxes" id="${cellLineDataInstance?.id}"/>
		<div id="show-cellLineData" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /> ${cellLineDataInstance}</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list cellLineData">
                
                

				<li class="fieldcontain">
                    <span id="cellLine-label" class="property-label"><g:message code="cellLineData.cellLine.label" default="Cell Line" /></span>
					
                    <span class="property-value" aria-labelledby="cellLine-label">
                        <div id="cellLineEditable">
                            <div onclick="${g.remoteFunction(action:'updateEditable',                                                       id: cellLineDataInstance?.id,
                                                       params:[thisClassName: 'CellLineData',
                                                       referencedClassName: 'org.openlab.genetracker.CellLine',
                                                       propertyName: 'cellLine'],
                                                       update:'cellLineEditable')}"
                        >
                            <g:if test="${cellLineDataInstance?.cellLine}">${cellLineDataInstance?.cellLine?.encodeAsHTML()}</g:if>
                            <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>

                            </div>
                        </div>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="acceptor-label" class="property-label"><g:message code="cellLineData.acceptor.label" default="Acceptor" /></span>
					
                    <span class="property-value" aria-labelledby="acceptor-label">
                        <div id="acceptorEditable">
                            <div onclick="${g.remoteFunction(action:'updateEditable',                                                       id: cellLineDataInstance?.id,
                                                       params:[thisClassName: 'CellLineData',
                                                       referencedClassName: 'org.openlab.genetracker.vector.Acceptor',
                                                       propertyName: 'acceptor'],
                                                       update:'acceptorEditable')}"
                        >
                            <g:if test="${cellLineDataInstance?.acceptor}">${cellLineDataInstance?.acceptor?.encodeAsHTML()}</g:if>
                            <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>

                            </div>
                        </div>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="firstRecombinant-label" class="property-label"><g:message code="cellLineData.firstRecombinant.label" default="First Recombinant" /></span>
					
                    <span class="property-value" aria-labelledby="firstRecombinant-label">
                        <div id="firstRecombinantEditable">
                            <div onclick="${g.remoteFunction(action:'updateEditable',                                                       id: cellLineDataInstance?.id,
                                                       params:[thisClassName: 'CellLineData',
                                                       referencedClassName: 'org.openlab.genetracker.Recombinant',
                                                       propertyName: 'firstRecombinant'],
                                                       update:'firstRecombinantEditable')}"
                        >
                            <g:if test="${cellLineDataInstance?.firstRecombinant}">${cellLineDataInstance?.firstRecombinant?.encodeAsHTML()}</g:if>
                            <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>

                            </div>
                        </div>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="secondRecombinant-label" class="property-label"><g:message code="cellLineData.secondRecombinant.label" default="Second Recombinant" /></span>
					
                    <span class="property-value" aria-labelledby="secondRecombinant-label">
                        <div id="secondRecombinantEditable">
                            <div onclick="${g.remoteFunction(action:'updateEditable',                                                       id: cellLineDataInstance?.id,
                                                       params:[thisClassName: 'CellLineData',
                                                       referencedClassName: 'org.openlab.genetracker.Recombinant',
                                                       propertyName: 'secondRecombinant'],
                                                       update:'secondRecombinantEditable')}"
                        >
                            <g:if test="${cellLineDataInstance?.secondRecombinant}">${cellLineDataInstance?.secondRecombinant?.encodeAsHTML()}</g:if>
                            <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>

                            </div>
                        </div>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="cultureMedia-label" class="property-label"><g:message code="cellLineData.cultureMedia.label" default="Culture Media" /></span>
					
                    <span class="property-value" aria-labelledby="cultureMedia-label">
                        <div id="cultureMediaEditable">
                            <div onclick="${g.remoteFunction(action:'updateEditable',                                                       id: cellLineDataInstance?.id,
                                                       params:[thisClassName: 'CellLineData',
                                                       referencedClassName: 'org.openlab.genetracker.CultureMedia',
                                                       propertyName: 'cultureMedia'],
                                                       update:'cultureMediaEditable')}"
                        >
                            <g:if test="${cellLineDataInstance?.cultureMedia}">${cellLineDataInstance?.cultureMedia?.encodeAsHTML()}</g:if>
                            <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>

                            </div>
                        </div>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="mediumAdditives-label" class="property-label"><g:message code="cellLineData.mediumAdditives.label" default="Medium Additives" /></span>
					
                    <span class="property-value" aria-labelledby="mediumAdditives-label">
                        <ul>
                            <g:if test="${cellLineDataInstance.mediumAdditives}">
                            <g:each in="${cellLineDataInstance.mediumAdditives}" var="m">
                                <g:form name="remove${m}Form">
                                    <li>
                                        <g:remoteLink controller="mediumAdditive" action="show" id="${m.id}" params="['bodyOnly':'true']" update="[success: 'body', failure: 'body']">${m?.encodeAsHTML()}</g:remoteLink>

                                        <g:hiddenField name="bodyOnly" value="${true}"/>
                                        <g:hiddenField name="id" value="${cellLineDataInstance?.id}"/>
                                        <g:hiddenField name="associatedId" value="${m?.id}"/>
                                        <g:hiddenField name="propertyName" value="mediumAdditives"/>
                                        <g:hiddenField name="referencedClassName" value="org.openlab.genetracker.MediumAdditive"/>
                                        <g:hiddenField name="thisClassName" value="CellLineData"/>
                                        <g:submitToRemote action="removeOneToMany" update="[success:'body',failure:'body']" value="Remove" />
                                    </li>
                                </g:form>
                            </g:each>
                            </g:if>
                            <g:else>
                                <i>None added</i>
                            </g:else>
                        </ul><br/><br/>
                            <g:form name="addOneToMany">
                                <g:hiddenField name="bodyOnly" value="${true}"/>
                                <g:hiddenField name="propertyName" value="mediumAdditives"  />
                                <g:hiddenField name="referencedClassName" value="org.openlab.genetracker.MediumAdditive"/>
                                <g:select from="${org.openlab.genetracker.MediumAdditive.list()}" name="selectAddTo" optionKey="id"/>
                                <g:hiddenField name="id" value="${cellLineDataInstance?.id}"/>
                                <g:submitToRemote action="addOneToMany" update="[success:'body',failure:'body']" value="Add" />
                            </g:form>
                    </span>
                    
				</li>

			

				<li class="fieldcontain">
                    <span id="plasmidNumber-label" class="property-label"><g:message code="cellLineData.plasmidNumber.label" default="Plasmid Number" /></span>
					
                    <span class="property-value" aria-labelledby="plasmidNumber-label">
                        <g:set var="myInList" value="${cellLineDataInstance?.constraints.plasmidNumber.inList}"/>
                        <g:if test="${myInList}">
                           <g:select from="${myInList}" name="plasmidNumberSelect" value="${fieldValue(bean: cellLineDataInstance, field: 'plasmidNumber')}"
                                     onChange="${g.remoteFunction(action:'editInList',id: cellLineDataInstance?.id, params: '"plasmidNumber=" + this.value', onFailure:'alert("Could not save property change");')}"/>
                        </g:if>
                        <g:else>
                            <g:editInPlace id="plasmidNumber"
                                           url="[action: 'editField', id:cellLineDataInstance.id]"
                                           rows="1"
                                           paramName="plasmidNumber">
                                <g:if test="${fieldValue(bean: cellLineDataInstance, field: 'plasmidNumber')}">
                                    ${fieldValue(bean: cellLineDataInstance, field: 'plasmidNumber')}
                                </g:if>
                                <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>
                            </g:editInPlace>
                        </g:else>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="colonyNumber-label" class="property-label"><g:message code="cellLineData.colonyNumber.label" default="Colony Number" /></span>
					
                    <span class="property-value" aria-labelledby="colonyNumber-label">
                        <g:set var="myInList" value="${cellLineDataInstance?.constraints.colonyNumber.inList}"/>
                        <g:if test="${myInList}">
                           <g:select from="${myInList}" name="colonyNumberSelect" value="${fieldValue(bean: cellLineDataInstance, field: 'colonyNumber')}"
                                     onChange="${g.remoteFunction(action:'editInList',id: cellLineDataInstance?.id, params: '"colonyNumber=" + this.value', onFailure:'alert("Could not save property change");')}"/>
                        </g:if>
                        <g:else>
                            <g:editInPlace id="colonyNumber"
                                           url="[action: 'editField', id:cellLineDataInstance.id]"
                                           rows="1"
                                           paramName="colonyNumber">
                                <g:if test="${fieldValue(bean: cellLineDataInstance, field: 'colonyNumber')}">
                                    ${fieldValue(bean: cellLineDataInstance, field: 'colonyNumber')}
                                </g:if>
                                <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>
                            </g:editInPlace>
                        </g:else>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="notes-label" class="property-label"><g:message code="cellLineData.notes.label" default="Notes" /></span>
					
                    <span class="property-value" aria-labelledby="notes-label">
                        <g:set var="myInList" value="${cellLineDataInstance?.constraints.notes.inList}"/>
                        <g:if test="${myInList}">
                           <g:select from="${myInList}" name="notesSelect" value="${fieldValue(bean: cellLineDataInstance, field: 'notes')}"
                                     onChange="${g.remoteFunction(action:'editInList',id: cellLineDataInstance?.id, params: '"notes=" + this.value', onFailure:'alert("Could not save property change");')}"/>
                        </g:if>
                        <g:else>
                            <g:editInPlace id="notes"
                                           url="[action: 'editField', id:cellLineDataInstance.id]"
                                           rows="1"
                                           paramName="notes">
                                <g:if test="${fieldValue(bean: cellLineDataInstance, field: 'notes')}">
                                    ${fieldValue(bean: cellLineDataInstance, field: 'notes')}
                                </g:if>
                                <g:else><img src="${resource(dir:'images/skin',file:'olf_tool_small.png')}"/></g:else>
                            </g:editInPlace>
                        </g:else>
                    </span>
					
				</li>

			

				<li class="fieldcontain">
                    <span id="projects-label" class="property-label"><g:message code="cellLineData.projects.label" default="Projects" /></span>
					
                    <span class="property-value" aria-labelledby="projects-label">
                        <ul>
                            <g:if test="${cellLineDataInstance?.projects?.size() > 0}">
                            <g:each in="${cellLineDataInstance?.projects}" var="p">
                                <g:form name="remove${p}Form">
                                <li>
                                    <g:remoteLink controller="project" action="show" id="${p.id}" params="['bodyOnly':'true']" update="[success: 'body', failure: 'body']">${p?.encodeAsHTML()}</g:remoteLink>

                                        <g:hiddenField name="bodyOnly" value="${true}"/>
                                        <g:hiddenField name="id" value="${cellLineDataInstance?.id}"/>
                                        <g:hiddenField name="associatedId" value="${p?.id}"/>
                                        <g:hiddenField name="propertyName" value="project"/>
                                        <g:hiddenField name="referencedClassName" value="org.openlab.main.Project"/>
                                        <g:hiddenField name="thisClassName" value="CellLineData"/>
                                        <g:submitToRemote action="removeManyToMany" update="[success:'body',failure:'body']" value="Remove" />
                                </li>
                                </g:form>
                            </g:each>
                            </g:if>
                            <g:else>
                                <i>None added</i>
                            </g:else>
                        </ul><br/><br/>
                        <g:form name="addToProject">
                            <g:hiddenField name="bodyOnly" value="${true}"/>
                            <g:hiddenField name="referencedClassName" value="org.openlab.main.Project"/>
                            <g:select from="${org.openlab.main.Project.list()}" name="selectAddTo" optionKey="id"/>
                            <g:hiddenField name="id" value="${cellLineDataInstance?.id}"/>
                            <g:submitToRemote action="addManyToMany" update="[success:'body',failure:'body']" value="Add to" />
                        </g:form>
                    </span>
                    
				</li>

			
			</ol>
		</div>

        <div id="tabs"/>
        <g:renderInterestedModules id='${cellLineDataInstance?.id}' domainClass='cellLineData'/>

        <script type="text/javascript">
            olfEvHandler.bodyContentChangedEvent.fire("${cellLineDataInstance?.toString()}", "${CellLineData}" ,"${cellLineDataInstance?.id}");
        </script>
	</body>
</html>

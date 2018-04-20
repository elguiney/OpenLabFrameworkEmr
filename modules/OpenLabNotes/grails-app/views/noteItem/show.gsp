
<%@ page import="org.openlab.security.User; org.openlab.main.DataObject; org.openlab.main.Project; org.openlab.notes.NoteItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="${params.bodyOnly?'body':bodyOnly?'body':'main'}" />
		<g:set var="entityName" value="${message(code: 'noteItem.label', default: 'NoteItem')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
			<script>
				tinymce.init({
					menubar:false,
					plugins: [
						"table textcolor code print"
					],
					width: 1000,
					height: 400,
					selector: "textarea",
					toolbar1: "print | copy | search"
				});
				tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
			</script>
			<script>
				//$('.mce-toolbar').hide();
			</script>
	<a href="#show-noteItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-noteItem" class="content scaffold-show" role="main">
			<h1>Show Note ${noteItemInstance.title}</h1>
<%--			<h1><g:message code="default.show.label" args="[entityName]" /></h1>--%>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<div style="padding:20px;">
				<g:form>
				<fieldset class="buttons">
					<g:remoteLink params="${[bodyOnly: true]}" update="body" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:remoteLink>
					<g:hiddenField name="id" value="${noteItemInstance?.id}" />
					<%-- if note is draft and the user is the author, show edit,save and delete options --%>
					<g:if test="${creator}">
						<g:if test="${noteItemInstance?.status == 'draft'}">
							<g:remoteLink params="${[bodyOnly: true]}" update="body" class="edit" action="edit" id="${noteItemInstance.id}"><g:message code="default.button.edit.label" default="Edit" /></g:remoteLink>
							<g:remoteLink params="${[bodyOnly: true]}" update="body" class="save" action="signNote" id="${noteItemInstance.id}"><g:message code="default.button.authorSign.label" default="Finalize" /></g:remoteLink>
							<g:submitToRemote params="${[bodyOnly: true]}" update="body" action="delete" name="delete" class="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" before="if(!confirm('Are you sure you want to delete this note?')) return false"/>
						</g:if>
						<g:else>
							<g:if test="${noteItemInstance?.accessLevel != 'open'}">
								<g:submitToRemote params="${[bodyOnly: true]}" update="body" action="makePublic" name="makePublic" class="create" value="${message(code: 'default.button.publish.label', default: 'Make note available to all users.')}" before="if(!confirm('Are you sure you want to make this note accessible to all users?')) return false"/>
							</g:if>
						</g:else>
					</g:if>
					<g:elseif test="${supervisor}">
						<g:if test="${noteItemInstance?.status == 'final'}">
							<g:remoteLink params="${[bodyOnly: true]}" update="body" class="save" action="signNote" id="${noteItemInstance.id}"><g:message code="default.button.sign.label" default="Sign note" /></g:remoteLink>
						</g:if>
					</g:elseif>

					<g:link class="save" action="exportToDocx" id="${noteItemInstance.id}"><g:message code="default.button.authorSign.label" default="Download note as docx" /></g:link>
					<g:link class="save" action="exportToPdf" id="${noteItemInstance.id}"><g:message code="default.button.authorSign.label" default="Download note as PDF" /></g:link>
				</fieldset>
			</g:form>
				</div>
			<g:if test="${noteItemInstance.accessLevel == "open"}">
				<div class="message" role="status">Note has been published and can be accessed by all users.</div>
			</g:if>
			<g:if test="${creator}">
				<g:if test="${noteItemInstance?.status == 'final'}">
				 	<div class="message">Pending supervisor signature, note can not be edited or deleted.</div>
				</g:if>
				<g:elseif test="${noteItemInstance?.status == 'signed'}">
					<div class="message">Signed by you and the supervisor, note can not be edited or deleted.</div>
				</g:elseif>
			</g:if>
			<g:elseif test="${supervisor}">
				<g:if test="${noteItemInstance?.status == 'signed'}">
					<div class="message">This note is signed by you and the author.</div>
				</g:if>
			</g:elseif>
			<g:elseif test="${noteItemInstance.accessLevel != "open"}">
				<div class="message">This note has been shared with you.</div>
			</g:elseif>

			<div style="width:260px; padding-right:20px; padding-top: 20px; position:absolute; right:0;">
				<gui:expandablePanel title="History" expanded="true" closable="true">
					<table style="border: 0;">
						<tbody>
						<tr class="prop">
							<td valign="top" class="name">Date created:</td>
							<td valign="top" class="value"><g:formatDate date="${noteItemInstance?.dateCreated}"/></td>
						</tr>

						<g:if test="${noteItemInstance?.status != 'draft'}">
							<tr class="prop">
								<td valign="top" class="name">Date finalized:</td>
								<td valign="top" class="value"><g:formatDate date="${noteItemInstance?.finalizedDate}"/></td>
							</tr>
						</g:if>

						<tr class="prop">
							<td valign="top" class="name">Author:</td>
							<td valign="top" class="value">${noteItemInstance.creator}</td>
						</tr>

						<g:if test="${noteItemInstance.status != 'draft'}">
							<tr class="prop">
								<td valign="top" class="name">Author signature:</td>
								<td valign="top" class="value"><span style='font-size: 24pt; font-wight:normal;'>${validAuthorSignature?"<span style='color:green;'>&check;</span>":"<span style='color:red;'>x</span>"}</span></td>
							</tr>
						</g:if>

						<g:if test="${noteItemInstance?.status == 'signed'}">
							<tr class="prop">
								<td valign="top" class="name">Date signed by supervisor:</td>
								<td valign="top" class="value"><g:formatDate date="${noteItemInstance?.supervisorSignedDate}"/></td>
							</tr>
						</g:if>

						<g:if test="${noteItemInstance?.supervisor}">
							<tr class="prop">
								<td valign="top" class="name">Supervisor:</td>
								<td valign="top" class="value">${noteItemInstance.supervisor}</td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name">Supervisor signature:</td>
								<td valign="top" class="value"><span style='font-size: 24pt; font-wight:normal;'>${validSupervisorSignature?"<span style='color:green;'>&check;</span>":"<span style='color:red;'>x</span>"}</span></td>
							</tr>
						</g:if>
						<tr class="prop">
							<td valign="top" class="name">Note status:</td>
							<td valign="top" class="value">${noteItemInstance?.status}</td>
						</tr>

						</tbody>
					</table>
				</gui:expandablePanel>
			 </div>
			<table>
				<tbody>

				<tr class="prop">
					<td valign="top" class="name">Title:</td>
					<td valign="top" class="value">${noteItemInstance?.title}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">Samples:</td>
					<td valign="top" class="value">
						<g:if test="${noteItemInstance.dataObjects.size() > 0}">
							<g:each in="${noteItemInstance.dataObjects}" var="dobj">
								<g:form name="removeDataObjectForm">
									<ul>
										<li>
											<g:remoteLink controller="dataObject" action="showSubClass" id="${dobj.id}" params="['bodyOnly':'true']" update="[success: 'body', failure: 'body']">${dobj?.encodeAsHTML()}</g:remoteLink>

											<g:hiddenField name="bodyOnly" value="true}"/>
											<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
											<g:hiddenField name="dataObject" value="${dobj?.id}"/>
											<g:submitToRemote action="removeFromSamples" update="[success:'body',failure:'body']" value="Remove" />
										</li>
									</ul>
								</g:form>
							</g:each>
						</g:if>
						<g:else>
							<i>None added</i>
						</g:else>
						<g:form name="addToDataObject">
							<g:hiddenField name="bodyOnly" value="true"/>
							<g:select class="select2" from="${dataObjects}" name="dataObject" optionKey="id"/>
							<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
							<g:submitToRemote action="addToSamples" update="[success:'body',failure:'body']" value="Add to" />
						</g:form>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">Upload attachment:</td>
					<td valign="top" class="value">
					<g:formRemote update="body" name="uploadFormBody" id="uploadFormBody" url="[action:'createInBody', controller:'dataObjectAttachment']">

					<div id="updateMeNow"><div class="message">Previously attached files are shown in a tab below the note.</div></div>

					<g:hiddenField name="suggestQuery" value="true"/>
						<g:hiddenField name="controllerToShow" value="noteItem"/>
					<g:hiddenField id="filesUploadedBody" name="filesUploadedBody" value=""/>
					<g:hiddenField name="attachTo" value="${noteItemInstance.id}"/>
					<div id="files">
					<!--<input type="file" name="attachment"/>-->
						<uploader:uploader id="noteItemAttachment" multiple="false" url="${[controller:'dataObjectAttachment', action:'uploadFile']}">

							<uploader:onComplete>
								document.getElementById('updateMeNow').innerHTML="<div class='message'>Upload successful</div>"

               var pathElt = document.createElement("input");
               pathElt.type = "hidden";
               pathElt.name = "filePath_" + id;
               pathElt.value = responseJSON.tempFile;

               var nameElt = document.createElement("input");
               nameElt.type = "hidden";
               nameElt.name = "fileName_" + id;
               nameElt.value = fileName;

               jQuery('#uploadFormBody').append(pathElt);
               jQuery('#uploadFormBody').append(nameElt);
               document.getElementById('filesUploadedBody').value = id;
							</uploader:onComplete>
						</uploader:uploader>

					</div>

					<g:submitButton name="Submit"/>
				</g:formRemote>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">Shared with:</td>
					<td valign="top" class="value">
						<g:if test="${noteItemInstance.shared.size() > 0}">
							<g:each in="${noteItemInstance.shared}" var="user">
								<g:form name="removeShareForm">
									<ul>
										<li>
											${user.userRealName} (${user.username})

											<g:hiddenField name="bodyOnly" value="true"/>
											<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
											<g:hiddenField name="user" value="${user?.id}"/>
											<g:if test="${user.username != currentUser}">
												<g:submitToRemote action="removeFromShared" update="[success:'body',failure:'body']" value="Remove" />
											</g:if>
										</li>
									</ul>
								</g:form>
							</g:each>
						</g:if>
						<g:else>
							<i>None added</i>
						</g:else>
						<g:if test="${noteItemInstance?.creator?.username == currentUser || noteItemInstance?.supervisor?.username == currentUser}">
						<g:form name="addToShared">
							<g:hiddenField name="bodyOnly" value="true"/>
							<g:select class="select2" from="${users}" name="user" optionKey="id"/>
							<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
							<g:submitToRemote action="addToShared" update="[success:'body',failure:'body']" value="Add to" />
						</g:form>
						</g:if>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">Projects:</td>
					<td valign="top" class="value">
						<g:if test="${noteItemInstance.projects.size() > 0}">
							<g:each in="${noteItemInstance.projects}" var="project">
								<g:form name="removeProjectsForm">
									<ul>
									<li>
										<g:remoteLink controller="project" action="show" id="${project.id}" params="['bodyOnly':'true']" update="[success: 'body', failure: 'body']">${project?.encodeAsHTML()}</g:remoteLink>

										<g:hiddenField name="bodyOnly" value="true"/>
										<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
										<g:hiddenField name="associatedId" value="${project?.id}"/>
										<g:hiddenField name="propertyName" value="project"/>
										<g:hiddenField name="referencedClassName" value="org.openlab.main.Project"/>
										<g:hiddenField name="thisClassName" value="org.openlab.notes.NoteItem"/>
										<g:submitToRemote action="removeManyToMany" update="[success:'body',failure:'body']" value="Remove" />
									</li>
									</ul>
								</g:form>
							</g:each>
						</g:if>
						<g:else>
							<i>None added</i>
						</g:else>
						<g:form name="addToProject">
							<g:hiddenField name="bodyOnly" value="true"/>
							<g:hiddenField name="referencedClassName" value="org.openlab.main.Project"/>
							<g:select class="select2" from="${projects}" name="selectAddTo" optionKey="id"/>
							<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
							<g:submitToRemote action="addManyToMany" update="[success:'body',failure:'body']" value="Add to" />
						</g:form>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">Notebook:</td>
					<td valign="top" class="value">
						<g:if test="${noteItemInstance.notebooks.size() > 0}">
							<g:each in="${noteItemInstance.notebooks}" var="notebook">
								<g:form name="removeNotebookForm">
									<ul>
										<li>
											<g:remoteLink controller="notebook" action="show" id="${notebook.id}" params="['bodyOnly':'true']" update="[success: 'body', failure: 'body']">${notebook?.encodeAsHTML()}</g:remoteLink>

											<g:hiddenField name="bodyOnly" value="true"/>
											<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
											<g:hiddenField name="notebookId" value="${notebook?.id}"/>
											<g:submitToRemote action="removeFromNotebooks" update="[success:'body',failure:'body']" value="Remove" />
										</li>
									</ul>
								</g:form>
							</g:each>
						</g:if>
						<g:else>
							<i>None added</i>
						</g:else>
						<g:form name="addToNotebook">
							<g:hiddenField name="bodyOnly" value="true"/>
							<g:select class="select2" from="${notebooks}" name="selectAddTo" optionKey="id"/>
							<g:hiddenField name="id" value="${noteItemInstance?.id}"/>
							<g:submitToRemote action="addToNotebooks" update="[success:'body',failure:'body']" value="Add to" />
						</g:form>
					</td>
				</tr>
				<tr class="prop">
					<td valign="top" class="name">Note:</td>
					<td valign="top" class="value">
						<div style="width:800px;margin-right:auto;margin-top:20px;">
							<textarea name="note">${noteItemInstance?.note}</textarea>
						</div>
					</td>
				</tr>
				</tbody>
				</table>
			<div id="tabs"/>
			<g:renderInterestedModules id='${noteItemInstance?.id}' domainClass='noteItem'/>

		</div>
	</body>
</html>

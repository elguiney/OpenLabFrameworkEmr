<%@ page import="org.openlab.notes.NoteItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="${params.bodyOnly?'body':bodyOnly?'body':'main'}" />
		<g:set var="entityName" value="${message(code: 'noteItem.label', default: 'NoteItem')}" />
		<title><g:message code="default.signNote.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-noteItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-noteItem" class="content scaffold-edit" role="main">
			<h1>Sign note</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${noteItemInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${noteItemInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" onsubmit="return false;">
				<g:hiddenField name="id" value="${noteItemInstance?.id}" />
				<g:hiddenField name="version" value="${noteItemInstance?.version}" />
				<g:hiddenField name="status" value="final"/>
				<g:hiddenField name="title" value="${noteItemInstance?.title}"/>
				<g:hiddenField name="note" value="${noteItemInstance?.note}"/>
				<fieldset class="form">
					Please confirm your password to sign this note.
					<table>
						<tbody>
							<tr>
								<td>
									<label for="password">Password</label>
								</td>
								<td>
									<g:passwordField name="password" value="" style="width:99%"/>
								</td>
							</tr>
							<g:if test="${users}">
								<tr>
									<td>
										<label for="supervisor">Supervisor</label>
									</td>
									<td>
									<g:if test="${lastSupervisor}">
										<g:select name="supervisor" from="${users}" value="${lastSupervisor?.id}" optionKey="id" optionValue="${{it.userRealName}}" />
									</g:if>
									<g:else>
										<g:select name="supervisor" from="${users}" value="${{users}}" optionKey="id" optionValue="${{it.userRealName}}" />
									</g:else>
									</td>
								</tr>
							</g:if>
						</tbody>
					</table>
				</fieldset>
				<fieldset class="buttons">
					<g:submitToRemote update="body" action="signNoteData" id="${noteItemInstance?.id}" class="save" value="${message(code: 'default.button.signNoteData.label', default: 'Sign note')}"/>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

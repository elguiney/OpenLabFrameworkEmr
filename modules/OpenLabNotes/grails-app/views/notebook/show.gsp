
<%@ page import="org.openlab.notes.NoteItem" %>
<!doctype html>
<html>
<head>
	<g:setProvider library="prototype"/>
	<meta name="layout" content="${params.bodyOnly?'body':bodyOnly?'body':'main'}" />
	<g:set var="entityName" value="${message(code: 'notebook.label', default: 'Notebook')}" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>
	<r:require module="export"/>
</head>
<body>
<script>
	tinymce.init({
		menubar:false,
		plugins: [
			"table textcolor code print"
		],
		width: 1000,
		height: 600,
		selector: "textarea",
		toolbar1: "print | copy | search"
	});
	tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
</script>
<a href="#list-noteItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
	<ul>
		<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
		<li><g:remoteLink params="${[bodyOnly: true, addToNotebook: notebookInstance.id]}" update="body" controller="noteItem" class="create" action="create">Add new note</g:remoteLink></li>
		<li><g:remoteLink params="${[bodyOnly: true]}" update="body" class="edit" action="edit" id="${notebookInstance.id}"><g:message code="default.edit.label" args="[entityName]" /></g:remoteLink></li>
		<li><g:submitToRemote params="${[bodyOnly: true]}" update="body" action="delete" name="delete" class="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" before="if(!confirm('Are you sure yu want to delete this notebook?')) return false"/></li>
	</ul>
</div>
<div id="list-noteItem" class="content scaffold-list" role="main">
	<h1>Notebook ${notebookInstance.title}</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
	</g:if>
</div>
<div style="width:260px; padding-right:20px; padding-top: 20px; position:absolute; right:0;">
	<gui:expandablePanel title="History" expanded="true" closable="true">
		<table style="border: 0;">
			<tbody>
			<tr class="prop">
				<td valign="top" class="name">Date created:</td>
				<td valign="top" class="value"><g:formatDate date="${notebookInstance?.dateCreated}"/></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name">Author:</td>
				<td valign="top" class="value">${notebookInstance.creator}</td>
			</tr>

			</tbody>
		</table>
	</gui:expandablePanel>
</div>
<div class="pagination">
	<g:remotePaginate total="${noteItemInstanceTotal?:0}" params="${params}" />
</div>

<table>
	<thead>
	<tr>

		<th><g:message code="noteItem.creator.label" default="Creator" /></th>

		<g:remoteSortableColumn property="lastUpdate" params="${params}" title="${message(code: 'noteItem.lastUpdate.label', default: 'Last Update')}" />

		<g:remoteSortableColumn property="dateCreated" params="${params}" title="${message(code: 'noteItem.dateCreated.label', default: 'Date Created')}" />

		<g:remoteSortableColumn property="finalizedDate" params="${params}" title="${message(code: 'noteItem.finalizedDate.label', default: 'Finalized Date')}" />

		<g:remoteSortableColumn property="supervisor" params="${params}" title="${message(code: 'noteItem.supervisor.label', default: 'Supervisor')}" />

		<g:remoteSortableColumn property="supervisorSignedDate" params="${params}" title="${message(code: 'noteItem.supervisorSignedDate.label', default: 'Supervisor Signed Date')}" />

		<g:remoteSortableColumn property="title" params="${params}" title="${message(code: 'noteItem.title.label', default: 'Title')}" />

		<th/>
	</tr>
	</thead>
	<tbody>
	<g:each in="${noteItemInstanceList}" status="i" var="noteItemInstance">
		<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

			<td>${fieldValue(bean: noteItemInstance, field: "creator")}</td>

			<td><g:formatDate type="date" date="${noteItemInstance.lastUpdate}" /></td>

			<td><g:formatDate type="date" date="${noteItemInstance.dateCreated}" /></td>

			<td><g:formatDate type="date" date="${noteItemInstance.finalizedDate}" /></td>

			<td>${fieldValue(bean: noteItemInstance, field: "supervisor")}</td>

			<td><g:formatDate type="date" date="${noteItemInstance.supervisorSignedDate}" /></td>

			<td><g:editInPlace id="title_${noteItemInstance.id}"
							   url="[action: 'editField', id: noteItemInstance.id]"
							   rows="1"
							   cols= "10"
							   paramName="title">${fieldValue(bean: noteItemInstance, field: "title")}
			</g:editInPlace>
			</td>

			<td><g:remoteLink params="${[bodyOnly: true]}" action="show" id="${noteItemInstance.id}" update="body">show details</g:remoteLink></td>
		</tr>
		<tr>
			<td colspan="8">
				<textarea name="note_${noteItemInstance.id}">${fieldValue(bean: noteItemInstance, field: "note")}</textarea>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:remotePaginate total="${noteItemInstanceTotal?:0}" params="${params}" />
</div>

</div>
<script type="text/javascript">
	olfEvHandler.bodyContentChangedEvent.fire("${noteItemInstance?.toString()}");
</script>
</body>
</html>

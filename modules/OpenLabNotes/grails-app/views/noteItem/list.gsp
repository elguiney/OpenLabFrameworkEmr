
<%@ page import="org.openlab.notes.NoteItem" %>
<!doctype html>
<html>
<head>
	<g:setProvider library="prototype"/>
	<meta name="layout" content="${params.bodyOnly?'body':bodyOnly?'body':'main'}" />
	<g:set var="entityName" value="${message(code: 'noteItem.label', default: 'NoteItem')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
	<r:require module="export"/>
</head>
<body>
<script>
	tinymce.init({
		menubar:false,
		statusbar: false,
		toolbar: false,
		width: 200,
		height: 100,
		selector: "textarea"
	});
	tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
</script>
<a href="#list-noteItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
	<ul>
		<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
		<li><g:remoteLink params="${[bodyOnly: true]}" update="body" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:remoteLink></li>
	</ul>
</div>
<div id="list-noteItem" class="content scaffold-list" role="main">
	<h1><g:message code="default.list.label" args="[entityName]" /></h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
	</g:if>

	<div id="filter" class="boxShadow">
		<h2>Filter options:</h2>
		<div style="padding:15px;"/>
		<g:formRemote update="body" name="filterList" url="[controller: 'noteItem', action:'list']">
			<g:hiddenField name="bodyOnly" value="${true}"/>
			Results per page: <g:select name="max" value="${params.max?:10}" from="${10..100}" class="range"/>

			Creator (Author): <g:select name="creatorFilter" from="${org.openlab.security.User.list().collect{it.username}}"
							   value="${params.creatorFilter?:''}" noSelection="['':'']" class="select2 many-to-many"/>

			Supervisor: <g:select name="supervisorFilter" from="${org.openlab.security.User.list().collect{it.username}}"
										value="${params.supervisorFilter?:''}" noSelection="['':'']" class="select2 many-to-many"/>


			Last Modifier: <g:select name="lastModifierFilter" from="${org.openlab.security.User.list().collect{it.username}}"
									 value="${params.lastModifierFilter?:''}" noSelection="['':'']" class="select2 many-to-many"/>


			<br/><br/>Project: <g:select name="projectFilter" from="${org.openlab.main.Project.list().collect{it.name}}"
							   value="${params.projectFilter?:''}" noSelection="['':'']" class="select2 many-to-many"/>

			Date: 	<g:select name="dateType" from="['Created', 'Updated', 'Finalized','Signed']" value="${params.dateType?:'Created'}"/>
					<g:datePicker name="afterDateFilter" value="${params.afterDateFilter?:new Date()}" noSelection="['':'-Choose-']" precision="day"/>
					- <g:datePicker name="beforeDateFilter" value="${params.beforeDateFilter?:new Date()}" noSelection="['':'-Choose-']" precision="day"/>

			<g:submitButton name="Filter"/>
		</g:formRemote>
	</div>
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

		<g:remoteSortableColumn property="note" params="${params}" title="${message(code: 'noteItem.note.label', default: 'Note Preview')}" />

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

			<td><g:remoteLink params="${[bodyOnly: true]}" action="show" id="${noteItemInstance.id}" update="body">show</g:remoteLink></td>

			<td>
				<textarea name="note_${noteItemInstance.id}">${fieldValue(bean: noteItemInstance, field: "note")}</textarea>
			</td>


		</tr>
	</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:remotePaginate total="${noteItemInstanceTotal?:0}" params="${params}" />
</div>
<export:formats params="${params}"/>
</div>
<script type="text/javascript">
	olfEvHandler.bodyContentChangedEvent.fire("${noteItemInstance?.toString()}");
</script>
</body>
</html>

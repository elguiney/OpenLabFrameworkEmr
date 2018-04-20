<%@ page import="org.openlab.notes.Notebook" %>

<div class="fieldcontain ${hasErrors(bean: notebookInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="notebook.title.label" default="Title" />

	</label>
	<g:textField name="title" value="${notebookInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: notebookInstance, field: 'notes', 'error')} ">
	<label for="notes">
		<g:message code="notebook.notes.label" default="Notes" />
		
	</label>
	<g:select name="notes" from="${accessibleNoteItems}" multiple="multiple" optionKey="id" size="5" value="${notebookInstance?.notes*.id}" class="select2 many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: notebookInstance, field: 'shared', 'error')} ">
	<label for="notes">
		<g:message code="notebook.shared.label" default="Shared" />

	</label>
	<g:select name="shared" from="${otherUsers}" multiple="multiple" optionKey="id" size="5" value="${notebookInstance?.shared*.id}" class="select2 many-to-many"/>
</div>



<%@ page import="org.openlab.notes.Notebook" %>
<!doctype html>
<html>
	<head>
        <g:setProvider library="prototype"/>
        <meta name="layout" content="${params.bodyOnly?'body':bodyOnly?'body':'main'}" />
		<g:set var="entityName" value="${message(code: 'notebook.label', default: 'Notebook')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-notebook" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:remoteLink update="body" params="${[bodyOnly: true]}" class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:remoteLink></li>
			</ul>
		</div>
		<div id="create-notebook" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${notebookInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${notebookInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form name="createnotebookInstanceForm">
				<g:hiddenField name="bodyOnly" value="${true}"/>
                <fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitToRemote update="body" action="save" name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
    <script type="text/javascript">
        olfEvHandler.bodyContentChangedEvent.fire("${notebookInstance?.toString()}");
    </script>
	</body>
</html>

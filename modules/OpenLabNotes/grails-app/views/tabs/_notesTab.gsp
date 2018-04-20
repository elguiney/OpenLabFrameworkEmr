	<div id="notesTab">
        <g:if test="${noteItemInstanceList}">
            <div class="list">
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

                            <td><g:remoteLink params="${[bodyOnly: true]}" controller="noteItem" action="show" id="${noteItemInstance.id}" update="body">show</g:remoteLink></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
                <div class="pagination">
                    <g:remotePaginate total="${noteItemInstanceTotal?:0}" params="${params}" />
                </div>

            </div>
        </g:if>
        <g:else>
            There are no notes attached to this object instance
        </g:else>
        </div>


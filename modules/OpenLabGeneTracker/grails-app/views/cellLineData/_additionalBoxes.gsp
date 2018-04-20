       	
		<%@ page import="org.openlab.genetracker.CellLineData" %>
        
    	
        <div style="padding-right:20px; padding-top: 20px; position:absolute; right:0;">
        	
        	<gui:expandablePanel title="History" expanded="true" closable="true">
                   <table style="border: 0;"><tbody>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="cellLineData.creator.label" default="Creator" /></td>
                            
                            <td valign="top" class="value">${cellLineDataInstance?.creator?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="cellLineData.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${cellLineDataInstance?.dateCreated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="cellLineData.lastModifier.label" default="Last Modifier" /></td>
                            
                            <td valign="top" class="value">${cellLineDataInstance?.lastModifier?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="cellLineData.lastUpdate.label" default="Last Update" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${cellLineDataInstance?.lastUpdate}" /></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
			</gui:expandablePanel>
			

			<div style="padding-top:10px;">
				<gui:expandablePanel title="Operations" expanded="true" closable="false">
				 <ul>
					<g:includeOperationsForType domainClass="cellLineData" id="${cellLineDataInstance.id}"/>
				 </ul>
				</gui:expandablePanel>
			</div>
		</div>
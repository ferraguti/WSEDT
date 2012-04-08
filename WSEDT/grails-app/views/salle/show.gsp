
<%@ page import="wsedt.Salle" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'salle.label', default: 'Salle')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-salle" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-salle" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list salle">
			
				<g:if test="${salleInstance?.nom}">
				<li class="fieldcontain">
					<span id="nom-label" class="property-label"><g:message code="salle.nom.label" default="Nom" /></span>
					
						<span class="property-value" aria-labelledby="nom-label"><g:fieldValue bean="${salleInstance}" field="nom"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${salleInstance?.batiment}">
				<li class="fieldcontain">
					<span id="batiment-label" class="property-label"><g:message code="salle.batiment.label" default="Batiment" /></span>
					
						<span class="property-value" aria-labelledby="batiment-label"><g:fieldValue bean="${salleInstance}" field="batiment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${salleInstance?.capacite}">
				<li class="fieldcontain">
					<span id="capacite-label" class="property-label"><g:message code="salle.capacite.label" default="Capacite" /></span>
					
						<span class="property-value" aria-labelledby="capacite-label"><g:fieldValue bean="${salleInstance}" field="capacite"/></span>
					
				</li>
				</g:if>
			
				
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${salleInstance?.id}" />
					<g:link class="edit" action="edit" id="${salleInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

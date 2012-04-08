
<%@ page import="wsedt.Reservation" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'reservation.label', default: 'Reservation')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-reservation" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-reservation" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list reservation">
			
				<g:if test="${reservationInstance?.salle}">
				<li class="fieldcontain">
					<span id="salle-label" class="property-label"><g:message code="reservation.salle.label" default="Salle" /></span>
					
						<span class="property-value" aria-labelledby="salle-label"><g:link controller="salle" action="show" id="${reservationInstance?.salle?.id}">${reservationInstance?.salle?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
				
				<g:if test="${reservationInstance?.cours}">
				<li class="fieldcontain">
					<span id="cours-label" class="property-label"><g:message code="reservation.cours.label" default="Cours" /></span>
					
						<span class="property-value" aria-labelledby="cours-label"><g:link controller="cours" action="show" id="${reservationInstance?.cours?.id}">${reservationInstance?.cours?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.duree}">
				<li class="fieldcontain">
					<span id="duree-label" class="property-label"><g:message code="reservation.duree.label" default="Duree" /></span>
					
						<span class="property-value" aria-labelledby="duree-label"><g:fieldValue bean="${reservationInstance}" field="duree"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.annee}">
				<li class="fieldcontain">
					<span id="annee-label" class="property-label"><g:message code="reservation.annee.label" default="Annee" /></span>
					
						<span class="property-value" aria-labelledby="annee-label"><g:fieldValue bean="${reservationInstance}" field="annee"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.mois}">
				<li class="fieldcontain">
					<span id="mois-label" class="property-label"><g:message code="reservation.mois.label" default="Mois" /></span>
					
						<span class="property-value" aria-labelledby="mois-label"><g:fieldValue bean="${reservationInstance}" field="mois"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.jour}">
				<li class="fieldcontain">
					<span id="jour-label" class="property-label"><g:message code="reservation.jour.label" default="Jour" /></span>
					
						<span class="property-value" aria-labelledby="jour-label"><g:fieldValue bean="${reservationInstance}" field="jour"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.heure}">
				<li class="fieldcontain">
					<span id="heure-label" class="property-label"><g:message code="reservation.heure.label" default="Heure" /></span>
					
						<span class="property-value" aria-labelledby="heure-label"><g:fieldValue bean="${reservationInstance}" field="heure"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reservationInstance?.minute}">
				<li class="fieldcontain">
					<span id="minute-label" class="property-label"><g:message code="reservation.minute.label" default="Minute" /></span>
					
						<span class="property-value" aria-labelledby="minute-label"><g:fieldValue bean="${reservationInstance}" field="minute"/></span>
					
				</li>
				</g:if>
			
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${reservationInstance?.id}" />
					<g:link class="edit" action="edit" id="${reservationInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

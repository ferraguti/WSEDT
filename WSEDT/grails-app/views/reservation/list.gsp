
<%@ page import="wsedt.Reservation" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'reservation.label', default: 'Reservation')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-reservation" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-reservation" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
					<th><g:message code="cours.reservation.salle" default="Salle" /></th>
					
					<th><g:message code="cours.reservation.cours" default="Cours" /></th>
				
					
						<g:sortableColumn property="duree" title="${message(code: 'reservation.duree.label', default: 'Duree')}" />
					
						<g:sortableColumn property="annee" title="${message(code: 'reservation.annee.label', default: 'Annee')}" />
					
						<g:sortableColumn property="mois" title="${message(code: 'reservation.mois.label', default: 'Mois')}" />
					
						<g:sortableColumn property="jour" title="${message(code: 'reservation.jour.label', default: 'Jour')}" />
					
						<g:sortableColumn property="heure" title="${message(code: 'reservation.heure.label', default: 'Heure')}" />
					
						<g:sortableColumn property="minute" title="${message(code: 'reservation.minute.label', default: 'Minute')}" />
						
						<th><g:message code="cours.reservation.nom" default="Nom" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${reservationInstanceList}" status="i" var="reservationInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
					<td><g:link controller="salle" action="show" id="${reservationInstance.salle.id}">${fieldValue(bean: reservationInstance, field: "salle")}</g:link></td>
					
					<td><g:link controller="cours" action="show" id="${reservationInstance.cours.id}">${fieldValue(bean: reservationInstance, field: "cours")}</g:link></td>
					
						<td>${fieldValue(bean: reservationInstance, field: "duree")}</td>
					
						<td>${fieldValue(bean: reservationInstance, field: "annee")}</td>
					
						<td>${fieldValue(bean: reservationInstance, field: "mois")}</td>
					
						<td>${fieldValue(bean: reservationInstance, field: "jour")}</td>
					
						<td>${fieldValue(bean: reservationInstance, field: "heure")}</td>
					
						<td>${fieldValue(bean: reservationInstance, field: "minute")}</td>
						
						<td><g:link action="show" id="${reservationInstance.id}">${fieldValue(bean: reservationInstance, field: "nom")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${reservationInstanceTotal}" />
			</div>
		</div>
	</body>
</html>


<%@ page import="wsedt.Salle" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'salle.label', default: 'Salle')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-salle" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-salle" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="nom" title="${message(code: 'salle.nom.label', default: 'Nom')}" />
						
						<g:sortableColumn property="batiment" title="${message(code: 'salle.batiment.label', default: 'Batiment')}" />
					
						<g:sortableColumn property="capacite" title="${message(code: 'salle.capacite.label', default: 'Capacite')}" />
					
						
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${salleInstanceList}" status="i" var="salleInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${salleInstance.id}">${fieldValue(bean: salleInstance, field: "nom")}</g:link></td>
					
						<td>${fieldValue(bean: salleInstance, field: "batiment")}</td>
						
						<td>${fieldValue(bean: salleInstance, field: "capacite")}</td>
					
						
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${salleInstanceTotal}" />
			</div>
		</div>
	</body>
</html>

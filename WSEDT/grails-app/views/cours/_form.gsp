<%@ page import="wsedt.Cours" %>



<div class="fieldcontain ${hasErrors(bean: coursInstance, field: 'nom', 'error')} required">
	<label for="nom">
		<g:message code="cours.nom.label" default="Nom" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nom" required="" value="${coursInstance?.nom}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: coursInstance, field: 'nbrInscrits', 'error')} required">
	<label for="nbrInscrits">
		<g:message code="cours.nbrInscrits.label" default="Nbr Inscrits" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="nbrInscrits" min="1" required="" value="${fieldValue(bean: coursInstance, field: 'nbrInscrits')}"/>
</div>


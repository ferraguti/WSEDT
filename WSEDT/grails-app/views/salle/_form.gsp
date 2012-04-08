<%@ page import="wsedt.Salle" %>



<div class="fieldcontain ${hasErrors(bean: salleInstance, field: 'nom', 'error')} required">
	<label for="nom">
		<g:message code="salle.nom.label" default="Nom" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nom" required="" value="${salleInstance?.nom}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: salleInstance, field: 'batiment', 'error')} required">
	<label for="batiment">
		<g:message code="salle.batiment.label" default="Batiment" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="batiment" required="" value="${salleInstance?.batiment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: salleInstance, field: 'capacite', 'error')} required">
	<label for="capacite">
		<g:message code="salle.capacite.label" default="Capacite" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="capacite" min="1" required="" value="${fieldValue(bean: salleInstance, field: 'capacite')}"/>
</div>




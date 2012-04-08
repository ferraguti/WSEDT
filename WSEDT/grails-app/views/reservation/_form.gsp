<%@ page import="wsedt.Reservation" %>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'salle', 'error')} required">
	<label for="salle">
		<g:message code="reservation.salle.label" default="Salle" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="salle" name="salle.id" from="${wsedt.Salle.list()}" optionKey="id" required="" value="${reservationInstance?.salle?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'cours', 'error')} required">
	<label for="cours">
		<g:message code="reservation.cours.label" default="Cours" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="cours" name="cours.id" from="${wsedt.Cours.list()}" optionKey="id" required="" value="${reservationInstance?.cours?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'duree', 'error')} required">
	<label for="duree">
		<g:message code="reservation.duree.label" default="Duree (en minute)" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="duree" min="15" max="240" required="" value="${fieldValue(bean: reservationInstance, field: 'duree')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'annee', 'error')} required">
	<label for="annee">
		<g:message code="reservation.annee.label" default="Annee" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="annee" min="2012" required="" value="${fieldValue(bean: reservationInstance, field: 'annee')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'mois', 'error')} required">
	<label for="mois">
		<g:message code="reservation.mois.label" default="Mois" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="mois" min="1" max="12" required="" value="${fieldValue(bean: reservationInstance, field: 'mois')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'jour', 'error')} required">
	<label for="jour">
		<g:message code="reservation.jour.label" default="Jour" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="jour" min="1" max="31" required="" value="${fieldValue(bean: reservationInstance, field: 'jour')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'heure', 'error')} required">
	<label for="heure">
		<g:message code="reservation.heure.label" default="Heure" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="heure" min="8" max="19" required="" value="${fieldValue(bean: reservationInstance, field: 'heure')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: reservationInstance, field: 'minute', 'error')} required">
	<label for="minute">
		<g:message code="reservation.minute.label" default="Minute" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="minute" min="0" max="59" required="" value="${fieldValue(bean: reservationInstance, field: 'minute')}"/>
</div>



<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="now" class="java.util.Date" />


<h3><spring:message code="player.squad" />: <jstl:out value="${player.squadNumber}-${player.squadName}"/></h3>

<h3><spring:message code="player.buyout" />: <jstl:out value="${player.buyoutClause }" /></h3>

<acme:display code="player.team" property="${player.team.name}" />

<acme:display code="player.name" property="${player.name }" />

<acme:display code="player.surnames" property="${player.surnames}" />

<spring:message code="player.photo"/>: <br> <img src="${player.photo }" width="10%" height="10%"/> <br>

<acme:display code="player.email" property="${player.email}" />

<acme:display code="player.phone" property="${player.phone}" />

<acme:display code="player.address" property="${player.address}" />

<jstl:if test="${language == 'en'}">
	<acme:display code="player.positionEnglish" property= "${player.positionEnglish}"/>
</jstl:if>
<jstl:if test="${language == 'es'}">
	<acme:display code="player.positionSpanish" property= "${player.positionSpanish}"/>
</jstl:if>

<spring:message code="player.injured" />: <spring:message code="player.${player.injured }" /><br>

<spring:message code="player.punished" />: <spring:message code="player.${player.punished }" /><br>

<!--<acme:button name="back" code="actor.back.team" onclick="javascript: relativeRedir('position/list.do');" />-->

<acme:button name="back" code="actor.back.finder" onclick="javascript: relativeRedir('finder/president/find.do');" />


<!-- 
<jstl:if test="${find}">
	<div>
		<a target="_blank" href="${targetSponsorship}"><img src="${bannerSponsorship }" alt="Banner" width="10%" height="10%" /></a>
	</div>
</jstl:if> -->
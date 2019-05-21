<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="hasRole('MANAGER')">
<display:table name="players" id="row" requestURI="${requestURI }" pagesize="${pagesize }">
	
	
	<acme:column property="name" titleKey="player.name" value= "${row.name}: "/>
	
	<acme:column property="surnames" titleKey="player.surnames" value= "${row.surnames}: "/>
	
	<jstl:if test="${language == 'en'}">
		<acme:column property="positionEnglish" titleKey="player.positionEnglish" value= "${row.positionEnglish} "/>
	</jstl:if>
	<jstl:if test="${language == 'es'}">
		<acme:column property="positionSpanish" titleKey="player.positionSpanish" value= "${row.positionSpanish} "/>
	</jstl:if>

	<display:column titleKey="player.injured"> 
				<spring:message code="player.${row.injured }" />
	</display:column>
	
	<display:column titleKey="player.punished"> 
				<spring:message code="player.${row.punished }" />
	</display:column>
	
	<acme:column property="team.name" titleKey="player.team.name" value= "${row.team.name}: "/>
	
	<security:authorize access="hasRole('MANAGER')">
	<acme:url href="training/manager/addPlayerPost.do?playerId=${row.id }&trainingId=${trainingId }" code="player.add" />
	</security:authorize>
	

</display:table>

<acme:button name="back" code="player.back" onclick="javascript: relativeRedir('training/manager/list.do');" />
</security:authorize> 
		


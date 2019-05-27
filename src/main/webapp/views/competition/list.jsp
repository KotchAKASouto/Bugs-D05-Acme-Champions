<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>



<display:table name="competitions" id="row" requestURI="${requestURI }" pagesize="5">
	
	<acme:column property="startDate" titleKey="competition.startDate" value= "${row.startDate}: "/>
	
	<acme:column property="nameTrophy" titleKey="competition.nameTrophy" value= "${row.nameTrophy}: "/>
	
	<display:column titleKey="competition.teams">
		<c:forEach items="${row.teams}" var="team">
    		${team.name}
    		<br>
		</c:forEach>
	</display:column>
	
	<acme:column property="format.type" titleKey="competition.format.type" value= "${row.format.type}: "/>
		
	<display:column>
		<jstl:if test="${!row.closed}">
		
			<a href="competition/federation/close.do?competitionId=${row.id}"><spring:message code="competition.close"/></a>
			<a href="competition/federation/listAddTeam.do?competitionId=${row.id}"><spring:message code="competition.addTeams"/></a>
			
		</jstl:if>
	</display:column>
	

	</display:table>
	
	<jstl:if test="${teamError }">
		<spring:message code="competition.error" />
		<br>
	</jstl:if>
	
	<a href="competition/federation/create.do"><spring:message code="competition.create"/></a>
		
	<acme:button name="back" code="competition.back" onclick="javascript: relativeRedir('welcome/index.do');" />





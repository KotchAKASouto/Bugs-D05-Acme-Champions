<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('REFEREE')">
<jstl:choose>
	<jstl:when test="${AmInMinutesFirstStep }">
		<h2><spring:message code="players.add.scored"/></h2>
	</jstl:when>
</jstl:choose>
<h3><spring:message code="players.home"/></h3>
</security:authorize>


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
	
	<security:authorize access="hasRole('REFEREE')">
	<jstl:choose>
		<jstl:when test="${AmInMinutesFirstStep}">
			<acme:url href="minutes/referee/addPlayerScored.do?playerId=${row.id}&minutesId=${minutesId}" code="player.add" />
		</jstl:when>
	</jstl:choose>
	</security:authorize>
	

</display:table>

<security:authorize access="hasRole('REFEREE')">
<jstl:if test="${AmInMinutesFirstStep }">
<h3><spring:message code="players.visitor"/></h3>

<display:table name="playersVisitor" id="row2" requestURI="${requestURI }" pagesize="${pagesize }">
	
	
	<acme:column property="name" titleKey="player.name" value= "${row2.name}: "/>
	
	<acme:column property="surnames" titleKey="player.surnames" value= "${row2.surnames}: "/>
	
	<jstl:if test="${language == 'en'}">
		<acme:column property="positionEnglish" titleKey="player.positionEnglish" value= "${row2.positionEnglish} "/>
	</jstl:if>
	<jstl:if test="${language == 'es'}">
		<acme:column property="positionSpanish" titleKey="player.positionSpanish" value= "${row2.positionSpanish} "/>
	</jstl:if>

	<display:column titleKey="player.injured"> 
				<spring:message code="player.${row2.injured }" />
	</display:column>
	
	<display:column titleKey="player.punished"> 
				<spring:message code="player.${row2.punished }" />
	</display:column>
	
	<acme:column property="team.name" titleKey="player.team.name" value= "${row2.team.name}: "/>
	
	<security:authorize access="hasRole('REFEREE')">
	<acme:url href="minutes/referee/addPlayerScored.do?playerId=${row.id}&minutesId=${minutesId}" code="player.add" />
	</security:authorize>	

</display:table>

</jstl:if>
</security:authorize>
<acme:button name="back" code="player.back" onclick="javascript: relativeRedir('training/manager/list.do');" />

<security:authorize access="hasRole('MANAGER')">	
<script type="text/javascript">
	var trTags = document.getElementsByTagName("tr");
	for (var i = 0; i < trTags.length; i++) {
	  var tdStatus = trTags[i].children[3];
	  if (tdStatus.innerText == "NO") {
		  trTags[i].style.backgroundColor = "#98FB98";
	  } else if (tdStatus.innerText == "YES") {
		  trTags[i].style.backgroundColor = "#FFA07A";
	  }
	}
</script>
</security:authorize>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>



<display:table name="games" id="row" requestURI="${requestURI }" pagesize="5">


	<display:column titleKey="game.gameDate"> 
		<fmt:formatDate value="${row.gameDate }" pattern="yyyy/MM/dd HH:mm" />
	</display:column>
	
	<acme:column property="place" titleKey="game.place" value= "${row.place}: "/>
	
	<display:column titleKey="game.friendly"> 
			<spring:message code="game.${row.friendly }" />
	</display:column>
	
	<acme:column property="homeTeam.name" titleKey="game.homeTeam" value= "${row.homeTeam.name}: "/>
	
	<acme:column property="visitorTeam.name" titleKey="game.visitorTeam" value= "${row.visitorTeam.name}: "/>
	
	<acme:column property="referee.name" titleKey="game.referee" value= "${row.referee.name}: "/>

	</display:table>
		
	<acme:button name="back" code="game.back" onclick="javascript: relativeRedir('welcome/index.do');" />





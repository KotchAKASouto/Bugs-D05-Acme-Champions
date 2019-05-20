<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>



<display:table name="trainings" id="row" requestURI="${requestURI }" pagesize="5">

	<spring:message code="dateFormat" var="format"/>
	<display:column titleKey="training.startDate"> 
		<fmt:formatDate value="${row.startDate }" pattern="${format}" />
	</display:column>
	
	<spring:message code="dateFormat" var="format"/>
	<display:column titleKey="training.endingDate"> 
		<fmt:formatDate value="${row.endingDate }" pattern="${format}" />
	</display:column>
	
	<acme:column property="place" titleKey="training.place" value= "${row.place}: "/>
	
	<acme:column property="description" titleKey="training.description" value= "${row.description}: "/>
	
	<display:column titleKey="training.players">
		<c:forEach items="${row.players}" var="item">
    		${item.name}
    		<br>
		</c:forEach>
	</display:column>
	
	
	<security:authorize access="hasRole('MANAGER')">
		<acme:url href="training/manager/display.do?trainingId=${row.id }" code="training.display" />
	</security:authorize>

	</display:table>
	
	<security:authorize access="hasRole('MANAGER')">
		<a href="training/manager/create.do"><spring:message code="training.create"/></a>
	</security:authorize>
		
	<acme:button name="back" code="training.back" onclick="javascript: relativeRedir('welcome/index.do');" />





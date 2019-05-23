<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${enlace}" modelAttribute="signing">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="playerId" />
	
	<div><spring:message code="signing.offeredClause.now" />:
	
		<fmt:formatNumber type="number" maxFractionDigits="5" value="${buyoutClause}" />
		
	</div>
	
	
	<jstl:if test="${signing.id == 0 }">
	
		<acme:textbox code="signing.price" path="price" obligatory="true"/>
		<acme:textbox code="signing.offeredClause" path="offeredClause" obligatory="true"/>
		
	</jstl:if>
	
	<jstl:if test="${signing.id != 0 }">
		
		<acme:textbox code="signing.mandatoryComment" path="mandatoryComment" obligatory="true"/>
		
	</jstl:if>
	
	<acme:submit name="save" code="signing.save" />
	
	<acme:cancel code="signing.cancel" url="signing/${autoridad}/list.do" />


</form:form>    
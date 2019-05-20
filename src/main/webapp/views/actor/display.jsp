<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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


<%-- <security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize> --%>

<jstl:if test="${admin}">
	<div>
		<jstl:if test="${!(actor eq principal)}">
			<a href="actor/administrator/profile/deleteProfile.do?actorId=${actor.id}"><spring:message code="actor.deleteProfile"/></a>
			<br/>
		</jstl:if>
	</div>
</jstl:if>

<acme:display code="actor.name" property="${actor.name }" />

<acme:display code="actor.surname" property="${actor.surnames }" />

<spring:message code="actor.photo"/>: <br> <img src="${actor.photo }" width="10%" height="10%"/> <br>

<acme:display code="actor.email" property="${actor.email }" />

<acme:display code="actor.phone" property="${actor.phone }" />

<acme:display code="actor.address" property="${actor.address }" />
	


<jstl:if test="${!admin}">
	<acme:button name="socialProfile" code="actor.socialProfile" onclick="javascript: relativeRedir('socialProfile/administrator,company,rookie/list.do');" />


	<security:authorize access="isAuthenticated()">

		<acme:button name="edit" code="actor.edit" onclick="javascript: relativeRedir('profile/edit.do');" />

	</security:authorize>
</jstl:if>
<jstl:if test="${!admin}">
	<acme:button name="back" code="actor.back" onclick="javascript: relativeRedir('welcome/index.do');" />
</jstl:if>
<jstl:if test="${admin}">
	<acme:button name="back" code="actor.back" onclick="javascript: relativeRedir('actor/administrator/profile/list.do');" />
</jstl:if>


	


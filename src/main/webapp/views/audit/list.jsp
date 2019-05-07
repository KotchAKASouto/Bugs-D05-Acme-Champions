<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>



<display:table name="audits" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:dateFormat titleKey="audit.moment" value="${row.moment }" pattern="yyyy/MM/dd" />
	
	<acme:column property="text" titleKey="audit.text" value= "${row.text}: "/>
	
	<acme:column property="score" titleKey="audit.score" value= "${row.score}: "/>
	
	<display:column titleKey="audit.finalMode"> 
				<spring:message code="audit.${row.finalMode }" />
	</display:column>
	
	<acme:column property="position.title" titleKey="audit.position" value= "${row.position.title}: "/>
	
	<security:authorize access="hasRole('AUDITOR')">
		<acme:url href="audit/auditor/display.do?auditId=${row.id }" code="audit.display" />
	</security:authorize>

	</display:table>
			
	<acme:button name="back" code="audit.back" onclick="javascript: relativeRedir('welcome/index.do');" />





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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<security:authorize access="hasRole('MANAGER')">

<div><spring:message code="training.startDate" />:
<fmt:formatDate value="${training.startDate}" pattern="yyyy/MM/dd HH:mm"/>
</div>

<div><spring:message code="training.endingDate" />:
<fmt:formatDate value="${training.endingDate}" pattern="yyyy/MM/dd HH:mm"/>
</div>

<acme:display code="training.place" property="${training.place }" />

<acme:display code="training.description" property="${training.description }" />


<spring:message code="training.players" />:  
	<c:forEach items="${training.players}" var="item">
    	${item.name} /
	</c:forEach>
<br>


<acme:button name="edit" code="training.edit" onclick="javascript: relativeRedir('training/manager/edit.do?trainingId=${training.id }');" />

<acme:button name="back" code="training.back" onclick="javascript: relativeRedir('training/manager/list.do');" />

</security:authorize>

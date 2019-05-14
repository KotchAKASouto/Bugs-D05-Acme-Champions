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

<br/>
<acme:display property="${curriculum.ticker}" code="curriculum.ticker" />
<br/>
<br/>

<fieldset>
<h3><spring:message code="curriculum.personalData" /></h3>

<acme:display property="${curriculum.personalData.fullName}" code="curriculum.fullName" />

<acme:display property="${curriculum.personalData.statement}" code="curriculum.statement" />

<acme:display property="${curriculum.personalData.phone}" code="curriculum.phone" />

<acme:display property="${curriculum.personalData.linkGitHubProfile}" code="curriculum.linkGitHubProfile" />

<acme:display property="${curriculum.personalData.linkLinkedInProfile}" code="curriculum.linkLinkedInProfile" />

<br/>

<security:authorize access="hasRole('ROOKIE')">
	<jstl:if test="${curriculum.noCopy}">
		<a href="personalData/rookie/edit.do?personalDataId=${curriculum.personalData.id }"><spring:message code="curriculum.editPersonal"/></a>
	</jstl:if>		
</security:authorize>	
</fieldset>
<br/>
<br/>

<fieldset>
<h3><spring:message code="curriculum.positionData" /></h3>

<display:table name="curriculum.positionDatas" pagesize="5" id="row1" requestURI="${requestUri}">

	<acme:column property="title" titleKey="curriculum.title" value= "${row1.title} "/>
	
	<acme:dateFormat titleKey="curriculum.startDate" pattern="yyyy/MM/dd" value="${row1.startDate}"/>
	
	<acme:dateFormat titleKey="curriculum.endDate" pattern="yyyy/MM/dd" value="${row1.endDate}"/>
	

	
	<security:authorize access="hasRole('ROOKIE')">
		<jstl:if test="${curriculum.noCopy}">
			<acme:url href="positionData/rookie/edit.do?positionRecordId=${row1.id }" code="curriculum.edit"/>
		</jstl:if>		
	</security:authorize>	
	
</display:table>

<br/>

<security:authorize access="hasRole('ROOKIE')">
	<jstl:if test="${curriculum.noCopy}">
		<a href="positionData/rookie/create.do?curriculumId=${curriculum.id }"><spring:message code="curriculum.create"/></a>
	</jstl:if>	
</security:authorize>	

</fieldset>

<br/>
<br/>
<fieldset>
<h3><spring:message code="curriculum.educationData" /></h3>

<display:table name="curriculum.educationDatas" pagesize="5" id="row2" requestURI="${requestUri}">

	<acme:column property="degree" titleKey="curriculum.degree" value= "${row2.degree} "/>
	
	<acme:column property="institution" titleKey="curriculum.institution" value= "${row2.institution} "/>
	
	<acme:column property="mark" titleKey="curriculum.mark" value= "${row2.mark} "/>
	
	<acme:dateFormat titleKey="curriculum.startDate" pattern="yyyy/MM/dd" value="${row2.startDate}"/>
	
	<acme:dateFormat titleKey="curriculum.endDate" pattern="yyyy/MM/dd" value="${row2.endDate}"/>
	
	<security:authorize access="hasRole('ROOKIE')">
		<jstl:if test="${curriculum.noCopy}">
			<acme:url href="educationData/rookie/edit.do?educationRecordId=${row2.id }" code="curriculum.edit"/>
		</jstl:if>
	</security:authorize>	
	
</display:table>

<br/>

<security:authorize access="hasRole('ROOKIE')">
	<jstl:if test="${curriculum.noCopy}">
		<a href="educationData/rookie/create.do?curriculumId=${curriculum.id }"><spring:message code="curriculum.create"/></a>
	</jstl:if>
</security:authorize>	

</fieldset>

<br/>
<br/>

<fieldset>
<h3><spring:message code="curriculum.miscellaneousData" /></h3>

<display:table name="curriculum.miscellaneousDatas" pagesize="5" id="row3" requestURI="${requestUri}">

	<acme:column property="text" titleKey="curriculum.text" value= "${row3.text} "/>

	<display:column titleKey="curriculum.attachments">
		<jstl:out value="${row3.attachments}"></jstl:out><br>

	</display:column>

	<security:authorize access="hasRole('ROOKIE')">
		<jstl:if test="${curriculum.noCopy}">
			<acme:url href="miscellaneousData/rookie/edit.do?miscellaneousRecordId=${row3.id }" code="curriculum.edit"/>
		</jstl:if>	
	</security:authorize>	
	
</display:table>
<br/>

<security:authorize access="hasRole('ROOKIE')">
	<jstl:if test="${curriculum.noCopy}">
		<a href="miscellaneousData/rookie/create.do?curriculumId=${curriculum.id }"><spring:message code="curriculum.create"/></a>
	</jstl:if>	
</security:authorize>	
</fieldset>
<br/>
<br/>
<security:authorize access="hasRole('ROOKIE')">
	<jstl:if test="${curriculum.noCopy}">
		<a href="curriculum/rookie/delete.do?curriculumId=${curriculum.id }"><spring:message code="curriculum.deleteall"/></a>
	</jstl:if>		
</security:authorize>	

<security:authorize access="hasRole('ROOKIE')">
	<acme:button name="back" code="curriculum.back" onclick="javascript: relativeRedir('curriculum/rookie/list.do');"/>
</security:authorize>	

<security:authorize access="hasRole('COMPANY')">
	<acme:button name="back" code="curriculum.back" onclick="javascript: relativeRedir('application/company/list.do');"/>
</security:authorize>	


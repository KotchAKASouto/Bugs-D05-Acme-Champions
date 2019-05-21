<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme Champions Co., Inc." width="489" height="297" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="configuration/administrator/edit.do"><spring:message code="master.page.configuration" /></a></li>
					<li><a href="broadcast/administrator/create.do"><spring:message code="master.page.broadcast" /></a></li>	
					<li><a href="actor/administrator/score/list.do"><spring:message code="master.page.score" /></a></li>
					<li><a href="actor/administrator/spammer/list.do"><spring:message code="master.page.spammer" /></a></li>
					<li><a href="actor/administrator/profile/list.do"><spring:message code="master.page.profiles" /></a></li>
					<li><a href="administrator/create.do"><spring:message code="master.page.signUpAdmin" /></a></li>
					<li><a href="administrator/createPresident.do"><spring:message code="master.page.signUpPresident" /></a></li>
					<li><a href="administrator/dashboard.do"><spring:message code="master.page.dashboard" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message	code="master.page.manager" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="training/manager/list.do"><spring:message code="master.page.list.trainings" /></a></li>		

				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('PRESIDENT')">
			<li><a class="fNiv"><spring:message	code="master.page.president" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="finder/president/find.do"><spring:message code="master.page.finder" /></a></li>		

				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('PLAYER')">
			<li><a class="fNiv"><spring:message	code="master.page.player" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="history/player/display.do"><spring:message code="master.page.history" /></a></li>		

				</ul>
			</li>
		</security:authorize>
		
	
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="register/createPlayer.do"><spring:message code="master.page.signup.player" /></a></li>
			<li><a class="fNiv" href="register/createManager.do"><spring:message code="master.page.signup.manager" /></a></li>
			<li><a class="fNiv" href="register/createReferee.do"><spring:message code="master.page.signup.referee" /></a></li>
			<li><a class="fNiv" href="register/createSponsor.do"><spring:message code="master.page.signup.sponsor" /></a></li>
			<li><a class="fNiv" href="register/createFederation.do"><spring:message code="master.page.signup.federation" /></a></li>
		</security:authorize>
		
		<security:authorize access="permitAll()">
	
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li><a href="profile/displayPrincipal.do"><spring:message code="master.page.profile" /></a></li>
					<li><a href="message/actor/list.do"><spring:message code="master.page.message" /> </a></li>
						
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" import="java.sql.*,java.io.*,java.net.*,org.owasp.encoder.Encode, dbProcs.*, utils.*" errorPage="" %>
<%@ include file="translation.jsp" %>
<%
/**
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 * @author Mark Denihan
 */
 
if (request.getSession() != null)
{
HttpSession ses = request.getSession();
Getter get = new Getter();
//Getting CSRF Token from client
Cookie tokenCookie = null;
try
{
	tokenCookie = Validate.getToken(request.getCookies());
}
catch(Exception htmlE)
{
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "DEBUG(getStarted.jsp): tokenCookie Error:" + htmlE.toString());
}
// validateSession ensures a valid session, and valid role credentials
// Also, if tokenCookie != null, then the page is good to continue loading
if (Validate.validateSession(ses) && tokenCookie != null)
{
	//Logging Username
	ShepherdLogManager.logEvent(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), "Accessed by: " + ses.getAttribute("userName").toString(), ses.getAttribute("userName"));
// Getting Session Variables
//The org.owasp.encoder.Encode class should be used to encode any softcoded data. This should be performed everywhere for safety

String csrfToken = Encode.forHtmlAttribute(tokenCookie.getValue());
String userName = Encode.forHtml(ses.getAttribute("userName").toString());
String userRole = Encode.forHtml(ses.getAttribute("userRole").toString());
String userId = Encode.forHtml(ses.getAttribute("userStamp").toString());
String ApplicationRoot = getServletContext().getRealPath("");
boolean isAdmin = userRole.equalsIgnoreCase("admin");
boolean changePassword = false;
if(ses.getAttribute("ChangePassword") != null)
{
	String tempPass = ses.getAttribute("ChangePassword").toString();
	changePassword = tempPass.equalsIgnoreCase("true");
}

int i = 0;

//IF Change Password is True, Stick up a form
if(!changePassword)
{
%>
	<div id="getStarted" style="display:none;">
	<div class="post">
		<h1 class="title"><fmt:message key="getStarted.text.lets_start" /></h1>
		<div class="entry">
			<p>
				<% if(ModulePlan.openFloor) { %>
					<fmt:message key="getStarted.text.moduleInfo.openFloor" />
				<% } else if (ModulePlan.incrementalFloor) { %>
					<fmt:message key="getStarted.text.moduleInfo.incrementalFloor" />
				<% } else {%>
					<fmt:message key="getStarted.text.moduleInfo" />
				<% } %>
			</p>
			<% if(isAdmin) {%>
			<h2 class="title"><fmt:message key="generic.text.configureShepherd" /></h2>
			<p>
			<fmt:message key="getStarted.text.configureShepherd.asAnAdmin" />
			<br/>
			<div id="scopeResultsDiv" class="informationBox" style="display: none;"></div>
			<br/>
			<div id="setScopeDiv">
				<a href="javascript:;" style="text-decoration: none;" id="allApplication"><div class="menuButton"><fmt:message key="getStarted.button.openLevels.all" /></div></a>
				<a href="javascript:;" style="text-decoration: none;" id="onlyWebApplication"><div class="menuButton"><fmt:message key="getStarted.button.openLevels.web" /></div></a>
				<a href="javascript:;" style="text-decoration: none;" id="onlyMobileApplication"><div class="menuButton"><fmt:message key="getStarted.button.openLevels.mobile" /></div></a>
				<a href="javascript:;" style="text-decoration: none;" id="noApplication"><div class="menuButton"><fmt:message key="getStarted.button.closeLevels" /></div></a>
			</div>
			<div id="scopeLoadingDiv" style="display: none;"><fmt:message key="generic.text.loading" /></div>
			</p>
			<% } %>
			<fmt:message key="getStarted.text.checkShepConfigMsg" /></a>.
		</div>
		<br/>
	<div id="cantSee">

	</div>
	</div>
	</div>
	<script>
	var theCsrfToken = "<%= csrfToken %>";
	var theRefreshError = "Could not Refresh Menu";

	$('#getStarted').slideDown("slow");
	$('#cantSee').html("<iframe class='levelIframe' frameborder='no' id='theStart' src='readyToPlay.jsp'></iframe>");
	$('#cantSee').html(function(){
		$("#theStart").load(function(){
			$("#contentDiv").slideDown("slow");
		});
	});
	<% if (isAdmin) { %>
	$("#allApplication").click(function(){
		$("#scopeResultsDiv").slideUp("slow");
		$("#scopeLoadingDiv").show("slow");
		$("#setScopeDiv").slideUp("fast", function(){
			var ajaxCall = $.ajax({
				type: "POST",
				url: "openEveryModules",
				data: {
					csrfToken: "<%= csrfToken %>"
				},
				async: false
			});
			if(ajaxCall.status == 200)
			{
				$('#scopeResultsDiv').html(ajaxCall.responseText);
			}
			else
			{
				$('#scopeResultsDiv').html("<br/><p> Config Failed!: " + ajaxCall.status + " " + ajaxCall.statusText + "</p><br/>");
			}
			$("#scopeLoadingDiv").hide("fast", function(){
				$("#setScopeDiv").slideDown("slow", function(){
					$("#scopeResultsDiv").show ("fast");
					//Refresh the Side Menu
					refreshSideMenu(theCsrfToken, theRefreshError);
				});
			});
			$("html, body").animate({ scrollTop: 0 }, "fast");
		});
	});
	
	$("#onlyWebApplication").click(function(){
		$("#scopeResultsDiv").slideUp("slow");
		$("#scopeLoadingDiv").show("slow");
		$("#setScopeDiv").slideUp("fast", function(){
			var ajaxCall = $.ajax({
				type: "POST",
				url: "openWebModules",
				data: {
					csrfToken: "<%= csrfToken %>"
				},
				async: false
			});
			if(ajaxCall.status == 200)
			{
				$('#scopeResultsDiv').html(ajaxCall.responseText);
			}
			else
			{
				$('#scopeResultsDiv').html("<br/><p> Config Failed!: " + ajaxCall.status + " " + ajaxCall.statusText + "</p><br/>");
			}
			$("#scopeLoadingDiv").hide("fast", function(){
				$("#setScopeDiv").slideDown("slow", function(){
					$("#scopeResultsDiv").show ("fast");
					//Refresh the Side Menu
					refreshSideMenu(theCsrfToken, theRefreshError);
				});
			});
			$("html, body").animate({ scrollTop: 0 }, "fast");
		});
	});
	
	$("#onlyMobileApplication").click(function(){
		$("#scopeResultsDiv").slideUp("slow");
		$("#scopeLoadingDiv").show("slow");
		$("#setScopeDiv").slideUp("fast", function(){
			var ajaxCall = $.ajax({
				type: "POST",
				url: "openMobileModules",
				data: {
					csrfToken: "<%= csrfToken %>"
				},
				async: false
			});
			if(ajaxCall.status == 200)
			{
				$('#scopeResultsDiv').html(ajaxCall.responseText);
			}
			else
			{
				$('#scopeResultsDiv').html("<br/><p> Config Failed!: " + ajaxCall.status + " " + ajaxCall.statusText + "</p><br/>");
			}
			$("#scopeLoadingDiv").hide("fast", function(){
				$("#setScopeDiv").slideDown("slow", function(){
					$("#scopeResultsDiv").show ("fast");
					//Refresh the Side Menu
					refreshSideMenu(theCsrfToken, theRefreshError);
				});
			});
			$("html, body").animate({ scrollTop: 0 }, "fast");
		});
	});
	
	$("#noApplication").click(function(){
		$("#scopeResultsDiv").slideUp("slow");
		$("#scopeLoadingDiv").show("slow");
		$("#setScopeDiv").slideUp("fast", function(){
			var ajaxCall = $.ajax({
				type: "POST",
				url: "closeEveryModules",
				data: {
					csrfToken: "<%= csrfToken %>"
				},
				async: false
			});
			if(ajaxCall.status == 200)
			{
				$('#scopeResultsDiv').html(ajaxCall.responseText);
			}
			else
			{
				$('#scopeResultsDiv').html("<br/><p> Config Failed!: " + ajaxCall.status + " " + ajaxCall.statusText + "</p><br/>");
			}
			$("#scopeLoadingDiv").hide("fast", function(){
				$("#setScopeDiv").slideDown("slow", function(){
					$("#scopeResultsDiv").show ("fast");
					//Refresh the Side Menu
					refreshSideMenu(theCsrfToken, theRefreshError);
				});
			});
			$("html, body").animate({ scrollTop: 0 }, "fast");
		});
	});
	<% } // End of Admin Only Script%>
	</script>
	<% if(Analytics.googleAnalyticsOn) { %><%= Analytics.googleAnalyticsScript %><% } %>
	<%
}
else	//IF the  user doesnt need to change their pass, just post up the get Started message
{
	%>
	<div class="errorWrapper">
		<fmt:message key="getStarted.text.info.changePassword" />
		<br /><br />
		<div class="errorMessage">
			<form id="changePassword" method="POST" action="passwordChange">
			<table align="center">
				<tr><td>Current Password:</td><td><input type="password" name="currentPassword" /></td></tr>
				<tr><td>New Password:</td><td><input type="password" name="newPassword" /></td></tr>
				<tr><td>Password Confirmation:</td><td><input type="password" name="passwordConfirmation" /></td></tr>
				<tr><td colspan="2"><center><input type="submit" id="changePasswordSubmit" value = "Change Password"/></center></td></tr>
			</table>
			<input type="hidden" name="csrfToken" value="<%=csrfToken%>" />
			</form>
		</div>
	</div>
	<%
}
}
else
{
response.sendRedirect("login.jsp");
}
}
else
{
response.sendRedirect("login.jsp");
}
%>
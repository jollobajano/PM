<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PM - Login</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <title>PM - <s:text name="pagetitle"/></title>
        <link href="favicon.ico" rel="icon" type="image/x-icon" />
        <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
        <link href="docinfo.css" rel="stylesheet" type="text/css" />
        <link href="tomcat.css" rel="stylesheet" type="text/css" />
        <decorator:head/>
    </head>

    <body>
        <div id="wrapper">
            <div id="navigation" class="curved container">
                <span id="nav-home"><a href="documents.action">Hem</a></span>
                <span id="nav-hosts"><a href="settings">Inställningar</a></span>
                <span id="nav-help"><a href="Logout.html">Logga ut</a></span>
                <br class="separator" />
            </div>
            <div id="upper" class="curved container">
                <div style="height:30px">
                </div>
                <div id="notice" style="min-height: 120px">
                    <img src="pms.gif" width="120px" />
                    <div id="tasks" style="text-align: right;">
                    	<% if(request.getUserPrincipal() != null) { %>
                    	  Inloggad som: <%= request.getUserPrincipal().getName() %>
                    	<% } else { %>
                     	  Inte inloggad
                    	<% } %>
                     </div>
                </div>
                <br class="separator" />
            </div>
	<center>
		<form method="POST" action="j_security_check">
			<table>
               	<% if(request.getParameter("failed") != null) { %>
				<tr>
					<td colspan="2" style="color: red; font-size: 10pt;">
						Inlogningen misslyckades. Försök igen.
					</td>
				</tr>
               	<% } %>
				<tr>
					<td colspan="2">Logga in för att se dina sidor</td>
				</tr>
				<tr>
					<td>Mailadress:</td>
					<td><input type="text" name="j_username" />
					</td>
				</tr>
				<tr>
					<td>Lösenord:</td>
					<td><input type="password" name="j_password"/ >
					</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Go" />
					</td>
				</tr>
			</table>
		</form>
	</center>
        </div>
    </body>
</html>

<!DOCTYPE html>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <title>PM</title>
        <link href="favicon.ico" rel="icon" type="image/x-icon" />
        <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
        <link href="docinfo.css" rel="stylesheet" type="text/css" />
        <link href="tomcat.css" rel="stylesheet" type="text/css" />
        <decorator:head/>
    </head>

    <body>
        <div id="wrapper">
            <div id="navigation" class="curved container">
                <span id="nav-home"><a href="<s:url action="documents"/>">Dokument</a></span>
                <span id="nav-hosts"><a href="<s:url action="settings"/>">Inställningar</a></span>
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
           	<decorator:body/>
        </div>
    </body>

</html>

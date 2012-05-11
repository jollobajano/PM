<!DOCTYPE html>
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<html>
<head>
<style>
body {
	color: #033;
}

.colHead {
	min-height: 50px;
}

table {
	padding: 2px;
	width: 100%;
}

table tr:FIRST-CHILD {
	border-top: 2px solid #033;
	border-bottom: 1px solid #033;
}

table tr:LAST-CHILD {
	border-bottom: 12px solid #033;
}

th {
	margin: 4px;
	font-size: 9pt;
	font-weight: normal;
}

td {
	font-weight: bold;
	font-size: 11pt;
}
</style>
</head>
<body>
	<h2>Dina inställningar</h2>

	<div>
		<div style="float: left; width: 45%;">

			<div class="colHead">
				<h3>Systemet tar emot PM inskickade från dessa adresser</h3>
			</div>


			<table>
				<tr>
					<th>Mailadress</th>
					<th />
				</tr>
				<s:iterator value="documentCollection.users" var="user">
					<tr>
						<td>${mail}</td>

						<td align="center"><s:if
								test="documentCollection.users.size() > 1">
								<a href="?delUser=${id}"><img src="trash.png" height="32px" />
								</a>&nbsp;
							</s:if>
						</td>
					</tr>
				</s:iterator>
				<s:form theme="simple">
					<tr>
						<td><s:textfield key="newUserAddress" /></td>
						<td align="center"><s:submit type="image" src="add.jpeg" height="24px" /></td>
					</tr>
				</s:form>
			</table>

		</div>
		<div style="float: right; width: 45%;">

			<div class="colHead">
				<h3>Dessa adresser meddelas av systemet</h3>
			</div>

			<table>
				<tr>
					<th>Mailadress</th>
					<s:iterator value="eventValues" var="event">
						<th>${text}</th>
					</s:iterator>
					<td />
				</tr>
				<s:iterator value="documentCollection.carbonCopies" var="cc">
					<tr>
						<s:form theme="simple">
							<td>${mail}</td>
							<s:iterator value="eventValues" var="event">
								<td align="center"><img src="check2.jpg" width="24px" />
								</td>
							</s:iterator>
						</s:form>
						<td align="center"><a href="?delCc=${id}"><img src="trash.png" width="32px" /></a>
						</td>
					</tr>
				</s:iterator>
				<tr>
					<s:form theme="simple">
						<td><s:textfield key="cc.mail" />
						</td>
						<s:iterator value="eventValues" var="event">
							<td align="center">
								<s:checkbox key="cc.events" value="false" fieldValue="%{event}" />
							</td>
						</s:iterator>
						<td align="center"><s:submit type="image" src="add.jpeg" height="24px" /></td>
						</td>
					</s:form>
					<td />
				</tr>
			</table>

		</div>
	</div>
</body>
</html>

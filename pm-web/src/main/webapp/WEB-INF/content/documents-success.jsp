<!DOCTYPE html>
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<html>
<head>
</head>
<body>
    <h2>Dina dokument</h2>
	<div id="doclist">
		<s:iterator value="documents" var="document">
			<div class="docinfo">

				<div class="row">
					<div class="cell">
						<div>Titel</div>
						<div class="cellcontents">
							${title}
						</div>
					</div>
					<div class="cell" style="width: 25px; vertical-align: bottom;">
						<div>&nbsp;</div>
						<div class="cellcontents">
							<s:if test="  #document.getState().ordinal() >= 4 ">
								<img src="red-alert.png" height="18px"/>
							</s:if>
							<s:elseif test=" #document.getState().ordinal() > 1 ">
								<img src="green-alert.png" height="18px"/>
							</s:elseif>
						</div>
					</div>
					<div class="cell">
						<div>Ursprungsdatum</div>
						<div class="cellcontents">${created}</div>
					</div>
					<div class="cell">
						<div>Gäller fr o m</div>
						<div class="cellcontents">${updated}</div>
					</div>
					<div class="cell">
						<div>Gäller t om</div>
						<div class="cellcontents">${expires}</div>
					</div>
					<div class="cell">
						<a
							href="DocumentRelay/${id}/doc/<s:property value="encoded(#document.title)"/>.doc">
							<img src="MSWord.gif" width="32px" /></a>
						<a
							href="DocumentRelay/${id}/pdf/<s:property value="encoded(#document.title)"/>.pdf">
							<img src="pdf.png" width="40px" /></a>
						<s:url var="trashcan">
							<s:param name="delete" value="id"/>
						</s:url>
						<a href="${trashcan}"> <img src="trash.png" width="32px" 
							onclick="return confirm('Vill du verkligen radera dokumentet\n${title}?')"/></a>
					</div>
					<br clear="all">
				</div>

				<%--
				<div class="row">
					<div class="cell">
						<div>Gäller för (målgrupp)</div>
						<div class="cellcontents">${audience}</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<div class="cell">
						<div>Dokumenttyp</div>
						<div class="cellcontents">${type}</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<br clear="all">
				</div>


				<div class="row">
					<div class="cell">
						<div>Författare/dokumentansvarig</div>
						<div class="cellcontents">${author}</div>
					</div>
					<div class="cell">
						<div>Godkännare</div>
						<div class="cellcontents">${supervisor}</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<div class="cell">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
					<br clear="all"> <br clear="all">
				</div>
				--%>
			</div>
		</s:iterator>
	</div>
</body>
</html>

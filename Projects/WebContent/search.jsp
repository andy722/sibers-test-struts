<!-- Contains search field -->

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tags/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tags/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tags/struts-html.tld" prefix="html" %>

<html:html>

	<head>
		<title>
			<bean:message key="application.title"/>
		</title>
	</head>

	<body>
		<html:form action="search.do">

			<table border=0 width="100%" align="center">
				<tr align="center">
					<td align="center">
						<bean:message key="search.title"/>
					</td>
				</tr>

				<tr>
					<td align="center">
						<html:text property="query" size="40"/>
					</td>
				</tr>

				<tr>
					<td align="center">
						<html:submit property="action">
							<bean:message key="search.searchButton"/>
						</html:submit>
						<html:submit property="action">
							<bean:message key="search.createButton"/>
						</html:submit>
					</td>
				</tr>
			</table>

		</html:form>
	</body>
</html:html>

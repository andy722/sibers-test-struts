<!-- Contains search form and search results, if any -->

<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tags/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tags/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tags/struts-html.tld" prefix="html"%>

<html:html>
	<head>
		<title>
			<bean:message key="application.title" />
		</title>
	</head>

	<body>
		<!-- Search form -->
		<jsp:include page="search.jsp" />

		<html:errors />

		<!-- If there is a query, show results -->
		<logic:notEmpty name="searchForm" property="query">
			<h3 align="left" style="background-color: gray">
				<bean:message key="search.results" />
			</h3>

			<!-- Have search results -->
			<logic:notEmpty name="searchForm" property="results">
				<table border="0" width="100%" align="left">
					<logic:iterate id="project" property="results" name="searchForm" scope="session">
						<tr>
							<td>
								<b>
									<bean:message key="project.name" />
								</b>
							</td>
							<td>
								<bean:write name="project" property="projectName" filter="true" />
							</td>
						</tr>

						<tr>
							<td>
								<b>
									<bean:message key="project.companyFrom" />
								</b>
							</td>
							<td>
								<bean:write name="project" property="companyFrom" filter="true" />
							</td>
						</tr>

						<tr>
							<td>
								<b>
									<bean:message key="project.companyTo" />
								</b>
							</td>
							<td>
								<bean:write name="project" property="companyTo" filter="true" />
							</td>
						</tr>

						<!-- Link to a page containing detail information about currently found Project -->
						<tr align="left">
							<td>
								<html:link action="edit.do" paramName="project" paramProperty="id" paramId="id">
									<bean:message key="search.detailsButton" />
								</html:link>
							</td>
							<td></td>
						</tr>

						<tr><td></td></tr>
					</logic:iterate>
				</table>
			</logic:notEmpty>

			<!-- If nothing found for current request -->
			<logic:empty name="searchForm" property="results">
				<h3 align="center">
					<bean:message key="search.nothing" />
				</h3>
			</logic:empty>

		</logic:notEmpty>

	</body>
</html:html>

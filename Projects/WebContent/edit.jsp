<!-- Represents detail project view with abilities to save or delete it -->

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
		<h3 style="background-color: gray">
			<bean:message key="edit.title" />
		</h3>

		<!-- Errors that cannot be mapped to input field -->
		<html:errors property="general" />

		<html:form action="edit.do">
			<table width="100%" border="0">
					<tr>
						<td>
							<bean:message key="project.name" />
						</td>
						<td>
							<html:text property="projectName" size="40"/> <html:errors property="projectName"/>
						</td>
					</tr>

					<tr>
						<td>
							<bean:message key="project.companyFrom" />
						</td>
						<td>
							<html:text property="companyFrom" size="40"/> <html:errors property="companyFrom"/>
						</td>
					</tr>

					<tr>
						<td>
							<bean:message key="project.companyTo" />
						</td>
						<td>
							<html:text property="companyTo" size="40" /> <html:errors property="companyTo" />
						</td>
					</tr>

					<tr>
						<td>
							<bean:message key="project.manager" />
						</td>
						<td>
							<html:text property="manager" size="40" /> <html:errors property="manager" />
						</td>
					</tr>

					<tr>
						<td>
							<bean:message key="project.workers" />
						</td>
						<td>
							<html:textarea property="workers" rows="7" cols="40"/>
						</td>
					</tr>

					<tr>
						<td>
							<bean:message key="project.comment" />
						</td>
						<td>
							<html:textarea property="comment" rows="10" cols="40"/>
						</td>
					</tr>

					<tr align="center">
						<td>
							<html:submit property="editAction">
								<bean:message key="edit.saveButton" />
							</html:submit>

							<!-- Only shown if an object is saved in database (otherwise, it's no need to delete it) -->
							<logic:notEmpty name="projectDetailForm" property="id">
								<html:submit property="editAction">
									<bean:message key="edit.deleteButton" />
								</html:submit>
							</logic:notEmpty>

							<html:submit property="editAction">
								<bean:message key="edit.backButton" />
							</html:submit>
						</td>
					</tr>
			</table>

		</html:form>
	</body>
</html:html>

package ru.sibers.belsky.Projects;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * Session bean representing Project
 *
 * @see ru.sibers.Projects.data.Project
 */
public class ProjectDetailForm extends ActionForm {
	private static final long serialVersionUID = 9159135049235871771L;

	/** Customer */
	private String companyFrom;

	/** Client */
	private String companyTo;

	private String projectName;

	/** Project manager */
	private String manager;

	private String comment;

	/** newline-separated list of persons */
	private String workers;

	/** Contains the title of pressed button */
	private String editAction;

	/** Unique identifier */
	private String id;

	public String getCompanyFrom() {
		return companyFrom;
	}

	public void setCompanyFrom(String companyFrom) {
		this.companyFrom = companyFrom;
	}

	public String getCompanyTo() {
		return companyTo;
	}

	public void setCompanyTo(String companyTo) {
		this.companyTo = companyTo;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEditAction() {
		return editAction;
	}

	public void setEditAction(String action) {
		this.editAction = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getWorkers() {
		return workers;
	}

	public void setWorkers(String workers) {
		this.workers = workers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void reset() {
		id = null;
		editAction = null;
		companyFrom = null;
		companyTo = null;
		manager = null;
		projectName = null;
	}

	public void resetAction() {
		editAction = null;
	}

	/**
	 * Assures that title, customer and client are set
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (projectName==null || projectName.trim().length()==0)
			errors.add("projectName", new ActionMessage("error.project.field"));
		if (companyFrom==null || companyFrom.trim().length()==0)
			errors.add("companyFrom", new ActionMessage("error.project.field"));
		if (companyTo==null || companyTo.trim().length()==0)
			errors.add("companyTo", new ActionMessage("error.project.field"));
		if (manager==null || manager.trim().length()==0)
			errors.add("manager", new ActionMessage("error.project.field"));

		return errors;
	}

}

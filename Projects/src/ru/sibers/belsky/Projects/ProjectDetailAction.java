package ru.sibers.belsky.Projects;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ru.sibers.belsky.Projects.data.DBOperator;
import ru.sibers.belsky.Projects.data.DataAccess;
import ru.sibers.belsky.Projects.data.Project;

/**
 * Controller, associated with <code>ProjectDetailForm</code> bean. Shows/edits/deletes <code>Project</code> entries
 */
public class ProjectDetailAction extends Action {

	private final Log log = LogFactory.getLog(ProjectDetailAction.class);

	/** Form bean, associated with corresponding page */
	private ProjectDetailForm detailForm = null;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		getServlet().log("Detail:");

		detailForm = (ProjectDetailForm) request.getSession().getAttribute("projectDetailForm");
		if (detailForm==null) {
			detailForm = new ProjectDetailForm();
			request.getSession().setAttribute("projectDetailForm", detailForm);
		}

		String action = detailForm.getEditAction();

		log.debug("Attributes = ");
		Enumeration attrs = request.getSession().getAttributeNames();
		while (attrs.hasMoreElements())
			log.debug(attrs.nextElement().toString());

		log.debug("id = " + detailForm.getId());
		log.debug("action = " + action);
		log.debug("FormId = " + request.getParameter("id"));

		/* Delete button pressed */
		if (getResources(request).getMessage("edit.deleteButton").equals(action))
			return onDelete(mapping, form, request, response);

		/* Save button pressed */
		if (getResources(request).getMessage("edit.saveButton").equals(action))
			return onSave(mapping, form, request, response);

		/* Cancel (back to search) button pressed */
		if (getResources(request).getMessage("edit.backButton").equals(action))
			return onCancel(mapping, form, request, response);

		/* Else - we are redirected from another page, or something unexpected happened */
		else
			return onDefault(mapping, form, request, response);
	}

	/**
	 * Fetches corresponding Project and fills ActionForm
	 * @param id String identifier of project in database
	 */
	private ProjectDetailForm fillForm(String id) throws Exception {
		log.debug("FillForm with id = " + id);
		try {
			Project project = new DBOperator().loadProjectById(Integer.parseInt(id));
			return DataTransform.projectToForm(project);
		} catch (java.lang.NumberFormatException e) {
			return new ProjectDetailForm();
		}
	}

	/**
	 * Converts given form bean to a Project and saves
	 * @param Form Bean
	 */
	private void saveProject(ProjectDetailForm form) throws Exception {
		Project p = DataTransform.formToProject(form);
		new DBOperator().saveProject(p);
		/* Saving obtained identifier on form - it enables "Delete" button */
		form.setId(Integer.valueOf(p.getId()).toString());
	}

	/**
	 * Deletes project by given String identifier
	 */
	private void deleteProjectById(String id) throws Exception {
		new DBOperator().deleteProjectById(Integer.parseInt(id));
	}

	/**
	 * Delete action
	 */
	private ActionForward onDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("Deleting id = " + detailForm.getId());

		try {
			deleteProjectById(detailForm.getId());
		} catch (Exception e) {
			servlet.log("Error in deleting entry: ", e);
			ActionErrors errors = new ActionErrors();
			errors.add("general", new ActionMessage("error", e.getLocalizedMessage()));
			saveErrors(request, errors);
		}

		log.debug(this.getClass().getName() + " onDelete");

		request.getSession().removeAttribute("projectDetailForm");
		DataAccess.closeSession();
		return mapping.findForward("onDelete");
	}

	/**
	 * Save action
	 */
	private ActionForward onSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DataAccess.closeSession();

		log.debug("Saving id = " + detailForm.getId());

		ActionErrors formErrors = detailForm.validate(mapping, request);
		if (formErrors!=null && !formErrors.isEmpty()) {
			saveErrors(request, formErrors);
			return mapping.findForward("onDefault");
		}

		try {
			saveProject(detailForm);
		} catch (Exception e) {
			log.error("Error in saving project entry", e);

			ActionErrors errors = new ActionErrors();
			errors.add("general", new ActionMessage("error", e.getLocalizedMessage()));
			saveErrors(request, errors);
		}
		detailForm.resetAction();
		request.getSession().setAttribute("projectDetailForm", detailForm);

		log.info(this.getClass().getName() + " onSave");

		DataAccess.closeSession();
		return mapping.findForward("onSave");
	}

	/**
	 * Cancel action - returning back.
	 */
	private ActionForward onCancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug(this.getClass().getName() + "onBack");

		/* The next time user reopens this URL - empty form is shown */
		request.getSession().removeAttribute("projectDetailForm");
		DataAccess.closeSession();
		return mapping.findForward("onBack");
	}

	/**
	 * Default action (if no other actions fit), usually called after redirect to this <code>Action</code>
	 */
	private ActionForward onDefault(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DataAccess.closeSession();
		try {
			request.getSession().removeAttribute("projectDetailForm");
			ProjectDetailForm f = fillForm(request.getParameter("id"));
			request.getSession().setAttribute("projectDetailForm", f);
		} catch (Exception e) {
			log.error(e);
			ActionErrors errors = new ActionErrors();
			errors.add("general", new ActionMessage("error", e.getLocalizedMessage()));
			saveErrors(request, errors);
		}
		log.debug(this.getClass().getName() + " onDefault");
		DataAccess.closeSession();
		return mapping.findForward("onDefault");
	}

}

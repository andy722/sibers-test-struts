package ru.sibers.belsky.Projects;

import java.util.LinkedList;
import java.util.List;

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
import ru.sibers.belsky.Projects.data.Project;

/**
 * Project search
 */
public class SearchAction extends Action {

	/** Submitted search query */
	protected String query = null;

	/** Contains identifier of last pressed button */
	protected String action = null;

	protected SearchForm searchForm = null;

	protected Log log = LogFactory.getLog(SearchAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		searchForm = (SearchForm) request.getSession().getAttribute("searchForm");
		if (searchForm==null) {
			searchForm = new SearchForm();
			request.getSession().setAttribute("searchForm", searchForm);
		}

		query = searchForm.getQuery();
		action = searchForm.getAction();

		log.debug("\nSearch");
		log.debug("Attributes = " + request.getSession().getAttributeNames().toString());
		log.debug("Query = " + query);
		log.debug("Action = " + searchForm.getAction());

		/* "Search" button pressed */
		if (getResources(request).getMessage("search.searchButton").equals(action)) {
			searchForm.resetAction();
			return onSearch(mapping, form, request, response);
		}

		/* "Create" button pressed */
		else if (getResources(request).getMessage("search.createButton").equals(action))
			return onCreate(mapping, form, request, response);

		/* Default action - we were redirected, probably */
		else
			return onSearch(mapping, form, request, response);
	}

	/**
	 * Performs a search on a given query, results are exported to <code>form</code>
	 * @param query May be null or empty
	 */
	private void fillResults(String query, SearchForm form) throws Exception {
		List<Project> results = new DBOperator().search(query);

		/* Filling result-form bean */
		form.setResults(new LinkedList<ProjectDetailForm>());
		for (Project p : results)
			form.getResults().add(DataTransform.projectToForm(p));
	}

	/**
	 * Just shows search page
	 */
	protected ActionForward onDefault(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("searchForm");
		log.info(this.getClass().getName() + " onDefault");
		return mapping.findForward("onDefault");
	}

	/**
	 * Searching with entered query
	 */
	protected ActionForward onSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			fillResults(query, searchForm);
			request.getSession().setAttribute("searchForm", searchForm);
		} catch (Exception e) {
			log.error("Search", e);

			/* Adding error indicator to the page */
			ActionErrors errors = new ActionErrors();
			errors.add("search", new ActionMessage("error", e.getMessage()));
			saveErrors(request, errors);
		}
		log.info(this.getClass().getName() + " onSearch");
		return mapping.findForward("onSearch");
	}

	/**
	 * Creating new entry
	 */
	protected ActionForward onCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("searchForm");
		request.getSession().removeAttribute("projectDetailsForm");
		log.info(this.getClass().getName() + " onCreate");
		return mapping.findForward("onCreate");
	}
}

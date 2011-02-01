package ru.sibers.belsky.Projects;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * This bean contains search query and search results (if any)
 */
public class SearchForm extends ActionForm {
	private static final long serialVersionUID = 5347874150095852000L;

	/** Search query. Cannot be empty */
	private String query;

	private List<ProjectDetailForm> results;

	/** Contains identifier of a button pressed during submit */
	private String action;

	public List<ProjectDetailForm> getResults() {
		return results;
	}

	public void setResults(List<ProjectDetailForm> results) {
		this.results = results;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void resetAction() {
		action = null;
	}

	public void reset() {
		query = null;
		results = null;
		action = null;
	}
}

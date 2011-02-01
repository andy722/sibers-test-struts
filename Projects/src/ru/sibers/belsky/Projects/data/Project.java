package ru.sibers.belsky.Projects.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent Project representation.
 */
public class Project implements Serializable {
	private static final long serialVersionUID = 1434059464892123470L;

	/** Unique identifier */
	private int id;

	/** Client */
	private String companyFrom;

	/** Executor */
	private String companyTo;

	/** Title of a project */
	private String projectName;

	private Worker manager;

	private Set<Worker> workers = new HashSet<Worker>();

	private String comment;

	public int getId() {
		return id;
	}

	public Project() {
		//manager = new Worker();
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Worker getManager() {
		return manager;
	}

	public void setManager(Worker manager) {
		this.manager = manager;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(Set<Worker> workers) {
		this.workers = workers;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (! (obj instanceof Project) )
			return false;

		Project p = (Project) obj;
			return (p.getCompanyFrom()==companyFrom) && (p.getCompanyTo()==companyTo) && (p.getProjectName()==projectName);
	}

}
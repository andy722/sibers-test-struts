package ru.sibers.belsky.Projects.data;

import java.io.Serializable;

/**
 * Persistent representation of worker. Can be a project manager.
 *
 * NB. Two different persons have different names AND different email addresses
 */
public class Worker implements Serializable {
	private static final long serialVersionUID = -6724351592883016782L;

	/** Hibernate identifier */
	private int id;

	/** Name of person. May contain spaces, but not the '@' sign */
	private String name;

	/** E-mail address */
	private String email;

	public Worker(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Worker() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "[" + name + ", " + email + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (! (o instanceof Worker) )
			return false;

		Worker w = (Worker) o;
		return (name==w.getName()) || (email==w.getEmail());
	}
}
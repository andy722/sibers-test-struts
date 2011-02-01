package ru.sibers.belsky.Projects;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.sibers.belsky.Projects.data.Project;
import ru.sibers.belsky.Projects.data.Worker;

/**
 * Converts persistent representation of data classes, e.g. Project or Worker, to Form beans and vice-versa.
 * It's some kind of a layer between business-logic and servlet representation
 */
public class DataTransform {
	private static Log log = LogFactory.getLog(DataTransform.class);

	/* Used for email validation */
	private static String emailName = "[a-z][a-z[0-9]\u005F\u002E\u002D]*[a-z||0-9]";
	private static String emailZone = "([a-z]){2,4}";
	private static Pattern emailPattern = Pattern.compile(emailName + "@" + emailName + "\u002E" + emailZone);

	/**
	 * Conversion between
	 * @param project
	 * @return ProjectDetailForm consisting of data specified in <code>project</code>
	 */
	public static ProjectDetailForm projectToForm(Project project) throws Exception {
		log.debug("DataTransform.projectToForm()");
		ProjectDetailForm result = new ProjectDetailForm();

		result.setProjectName(project.getProjectName());
		result.setCompanyFrom(project.getCompanyFrom());
		result.setCompanyTo(project.getCompanyTo());
		result.setComment(project.getComment());

		result.setId(Integer.valueOf(project.getId()).toString());

		Worker manager = project.getManager();
		result.setManager( workerToString(manager) );

		if (project.getWorkers()!=null) {
			String workers = new String();
			for (Worker w : project.getWorkers())
				workers += "\n" + workerToString(w);
			result.setWorkers(workers);
		}

		return result;
	}

	/**
	 * @param project
	 * @return ProjectDetailForm consisting of data specified in <code>project</code>
	 * @throws ExceptionInInitializerError If required fields are null or strictly formatted fields cannot be parsed
	 */
	public static Project formToProject(ProjectDetailForm form) throws RuntimeException {
		log.debug("DataTransform.formToProject()");

		Project result = new Project();

		try {
			result.setProjectName(form.getProjectName());
			result.setCompanyFrom(form.getCompanyFrom());
			result.setCompanyTo(form.getCompanyTo());
			result.setComment(form.getComment());

			if ( (form.getId()!=null) && (form.getId().length()!=0) ) {
				result.setId(Integer.valueOf(form.getId()));
				log.debug("ID = " + result.getId());
			}
			else
				log.debug("Found NULL identifier");

			if ( (form.getManager() != null) && (form.getManager().trim().length()!=0) ) {
				log.debug("Parsing manager: " + form.getManager());
				result.setManager( stringToWorker(form.getManager()) );
			} else {
				log.error("NULL manager in form bean");
			}

			result.setWorkers(new HashSet<Worker>());
			for (String workerStr : form.getWorkers().split("\n")) {
				log.debug("Parsing new worker: " + workerStr);
				if ( (workerStr != null) && (workerStr.trim().length()!=0) )
					result.getWorkers().add( stringToWorker(workerStr) );
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return result;
	}

	public static String workerToString(Worker worker) {
		return worker.getName() + " " + worker.getEmail();
	}

	/**
	 * @param string Combination of name and ONE email address
	 * @throws ExceptionInInitializerError If <code>string</code> cannot be parsed
	 * Assumes string isn't <code>null</null> or empty
	 */
	public static Worker stringToWorker(String string) throws RuntimeException {
		log.debug("DataTransform.stringToWorker(" + string + ")");

		if (string==null || string.trim().length()==0)
			return null;

		String name = new String();
		String email = null;

		String[] strings = string.split(" ");

		for (String word : strings) {
			String wordFinal = word.trim();

			/* It is email */
			if (wordFinal.contains("@")) {
				if (email!=null) {
					/* already have an email */
					throw new ExceptionInInitializerError("Only one email allowed");
				} else {
					email = new String(wordFinal);
				}
			}
			/* It's part of a name, probably */
			else
				name = name + " " + wordFinal;
		}

		if (email==null)
			throw new RuntimeException("Email is mandatory");
		if (!isValidEmail(email))
			throw new RuntimeException("Improper form of email address");
		if (name.length()==0)
			throw new RuntimeException("Name is mandatory");

		Worker worker = new Worker(name, email);
		log.debug("Parsed: " + worker);
		return worker;
	}

	/**
	 *
	 * @param mail
	 * @return True if <code>mail</code> is valid email address, false otherwise
	 * @see javax.mail.internet.InternetAddress#validate()
	 */
	private static boolean isValidEmail(String mail) {
		Matcher matcher = emailPattern.matcher(mail.toLowerCase());

		boolean result = matcher.matches();

		if (result)
			log.debug(mail + " is valid address");
		else
			log.debug(mail + " is invalid address");

		return result;
	}

}

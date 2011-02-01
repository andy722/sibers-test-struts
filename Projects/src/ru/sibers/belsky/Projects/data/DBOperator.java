package ru.sibers.belsky.Projects.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Interface between Hibernate database connection and top-level application classes.
 * Wraps all database queries (including thrown exceptions) so that application is completely abstracted from data source
 */
public class DBOperator {

	private static final Log log = LogFactory.getLog(DBOperator.class);

	/**
	 * Performs a search on all persistent <code>Project</code>s
	 * @param query Is register-sensitive
	 * @return List of projects that contain <code>query</code> in one or more fields (excluding staff)
	 * @throws RuntimeException
	 */
	public List<Project> search(String query) throws RuntimeException {
		/* Matching Projects */
		List<Project> result = new LinkedList<Project>();

		/* Don't perform a search on empty query */
		if (query==null || query.trim().length()==0)
			return result;

		final String mask = "%" + query + "%";

		Session session = DataAccess.openSession();
//		Transaction t = null;

		try {
	//		t = session.beginTransaction();
			/* Getting list of matching projects */
			List projects = session.createCriteria(Project.class).add(
					Restrictions.disjunction()
					.add(Restrictions.like("projectName", mask))
					.add(Restrictions.like("companyTo", mask))
					.add(Restrictions.like("companyFrom", mask))
					.add(Restrictions.like("comment", mask))
			).list();

			for (Object o : projects)
				result.add( (Project) o );

		//	t.commit();
		} catch (HibernateException e) {
			//if (t!=null)
				//t.rollback();
			throw new RuntimeException(e.getMessage());
		} finally {
			//DataAccess.closeSession();
		}

		return result;
	}

	/**
	 * @param id Unique object identifier
	 * @throws RuntimeException
	 */
	public void deleteProjectById(int id) throws RuntimeException {
		Session s = DataAccess.openSession();
		Transaction t = null;

		try {
			t = s.beginTransaction();
			Project project = (Project) s.get(Project.class, id);

			if (project==null)
				return;
			if (project!=null)
				s.delete(project);
			t.commit();
		} catch (HibernateException e) {
			if (t!=null)
				t.rollback();
			log.error("Cannot delete object", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DataAccess.closeSession();
		}
	}

	public Project loadProjectById(int id) throws RuntimeException {
			Session session = DataAccess.openSession();
			//Transaction t = null;
			Project result = null;
			try {
				//t = session.beginTransaction();
				result = (Project) session.load(Project.class, id);
				//t.commit();
			} catch (HibernateException e) {
				//if (t!=null)
					//t.rollback();
				throw new RuntimeException(e.getMessage());
			} finally {
				//DataAccess.closeSession();
			}
			return result;
	}

	public void saveProject(Project project) throws RuntimeException {
		if (project.getWorkers()==null)
			project.setWorkers(new HashSet<Worker>());

		Session session= DataAccess.openSession();
		Transaction t = null;

		try {
			t = session.beginTransaction();

			if (project.getId()!=0 && session.get(Project.class, project.getId())!=null) {
				t.rollback();
				DataAccess.closeSession();

				session = DataAccess.openSession();
				t = session.beginTransaction();

				session.saveOrUpdate(project.getManager());
				if (project.getWorkers()!=null) {
					for (Worker w : project.getWorkers())
						if (w!=null)
							session.saveOrUpdate(w);
				}

				session.update(project);
			}
			else {
				/* Firstly, saving workers of a project */
				session.saveOrUpdate(project.getManager());
				if (project.getWorkers()!=null) {
					for (Worker w : project.getWorkers())
						if (w!=null)
							session.saveOrUpdate(w);
				}
				session.save(project);
			}
			t.commit();
		} catch (HibernateException e) {
			if (t!=null)
				t.rollback();
			throw new RuntimeException(e.getMessage());
		} finally {
			/*
			 * Hibernate's lazy collection loading may cause problems if we close session right now and try to access fields of loaded object later.
			 * Because of using static DataAccess with it's ThreadLocal sessions, it's possible to close this session later
			 */
			//DataAccess.closeSession();
		}
	}

	/**
	 * Manually saves or updates <code>worker</code> according to it's <code>equals()</code> method
	 * @param worker
	 */
	public void saveWorker(Worker worker) {
		log.debug("ProjectDetailAction.saveWorker(" + worker.toString() + ")");

		Session session = DataAccess.openSession();

		/* Data is not written here, so it's possible to avoid using transaction */
		//Transaction t = null;

		try {
			Worker saved = (Worker) session.createCriteria(Worker.class).add(Restrictions.eq("email", worker.getEmail())).add(Restrictions.eq("name", worker.getName())).uniqueResult();

			/* Not saved */
			if (saved==null) {
				List names = session.createCriteria(Worker.class).add(Restrictions.eq("name", worker.getName())).list();
				List mails = session.createCriteria(Worker.class).add(Restrictions.eq("email", worker.getEmail())).list();

				/* There are no entries with duplicate name or email */
				if ( (names.size()==0) && (mails.size()==0) )
					session.save(worker);
				else {
					/* List of workers that have the same names or e-mail addresses */
					String alreadyHave = new String();

					for (Object o : names) {
						Worker w = (Worker) o;
						alreadyHave += w.toString() + " ";
					}
					for (Object o : mails) {
						Worker w = (Worker) o;
						alreadyHave += w.toString() + " ";
					}

					throw new RuntimeException("Names and e-mail addresses must be unique to all the workers. Already have: " + alreadyHave);
				}
			}
			/* Already saved */
			else
				worker.setId(saved.getId());
		} catch (HibernateException e) {
			//if (t!=null)
				//t.rollback();
			throw new RuntimeException(e.getMessage());
		} finally {
			//DataAccess.closeSession();
		}
	}
}
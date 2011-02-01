package ru.sibers.belsky.Projects.data;

import java.net.URL;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Plug-in for data access based on Hibernate. Provides methods for getting Session object as ThreadLocal variable. Consider using this: <br><br>
 * <code>
 * Session session = DataAccess.openSession();<br>
 * session.save(object);<br>
 * DataAccess.closeSession();<br>
 * </code><br>
 * instead of<br><br>
 * <code>
 * Session session = sessionFactory.openSession();<br>
 * session.save(object);<br>
 * session.close();<br>
 * </code> <br>
 *
 */
public class DataAccess implements PlugIn {
	private final static Log log = LogFactory.getLog(DataAccess.class);

	/** Location of Hibernate configuration file (usually hibernate.cfg.xml) */
	public static final String HIBERNATE_CONFIG = "hibernate.cfg.xml";

	private static SessionFactory factory = null;

	/** Stores Session object, one per thread */
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	/**
	 * Deinitializes database connection
	 */
	public void destroy() {
		try {
			if (factory!=null)
				factory.close();
			log.info("SessionFactory destroyed");
		} catch(HibernateException e) {
			log.error("Unable to close SessionFactory", e);
		}
	}

	/**
	 * Initializes database connection
	 */
	public synchronized void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
		log.info("Initilizing Hibernate connection");

		try {
			buildSF();
		} catch(HibernateException e) {
			log.fatal("Unable to initialize database connection", e);
		}
	}

	/**
	 * Configures Hibernate connection, saves SessionFactory
	 */
	private static void buildSF() {
		URL configFileURL =	DataAccess.class.getResource(HIBERNATE_CONFIG);
		log.debug("URL = " + configFileURL);

		factory = new Configuration().configure(configFileURL).buildSessionFactory();

		log.info("SessionFactory initialized");
	}

	/**
	 * Replaces usual <br><code>session = sessionFactory.openSession()</code>
	 * @return Session object, local for querying threads
	 * @throws HibernateException
	 */
	public static Session openSession() throws HibernateException {
		Session session = threadLocal.get();

		/* Invalid session is stored */
		if ( (session != null) && !session.isConnected())
			session = null;

		/* Nothing stored */
		if (session == null) {
			if (factory==null) {
				log.warn("Asking for session before initializing plugin");
				buildSF();
			}
			session = factory.openSession();
			threadLocal.set(session);
		}

		return session;
	}

	/**
	 * Should replace standard <code>session.close()</code> in DB operations
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		Session session = threadLocal.get();
		threadLocal.set(null);

		if (session != null)
			session.close();
	}

}

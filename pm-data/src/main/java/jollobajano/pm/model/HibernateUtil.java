package jollobajano.pm.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.addPackage("jollobajano.pm.model");
			cfg.addAnnotatedClass(CarbonCopy.class);
			cfg.addAnnotatedClass(DocumentCollection.class);
			cfg.addAnnotatedClass(DocumentInfo.class);
			cfg.addAnnotatedClass(Header.class);
			cfg.addAnnotatedClass(User.class);
			cfg.configure();

			sessionFactory = cfg.buildSessionFactory();

			Runtime.getRuntime().addShutdownHook(new Thread() {

				public void run() {
					try {
						sessionFactory.close();
					} catch (RuntimeException ex) {
						System.err.println("Error shutting down database." + ex);
					}
				}
			});

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}


	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}

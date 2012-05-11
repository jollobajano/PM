package jollobajano.pm.model;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class DAO {

	interface Worker {

		Object doWork( Session session ) throws Exception;
	}

	protected static DAO instance = new DAO();



	// Persistence.createEntityManagerFactory( "jollobajano.pm.model" );
	public synchronized static DAO getInstance() {
		return instance;
	}

	EntityManagerFactory entityManagerFactory = null;



	protected DAO() {
	}



	public synchronized List<DocumentInfo> checkForExpiringEntries( final Date triggerDate, final Date todayDate )
			throws Exception {
		final List<DocumentInfo> result = new ArrayList<DocumentInfo>();
		perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				Criteria criteria = session.createCriteria(DocumentInfo.class).add(
						Restrictions.or(Restrictions.and(Restrictions.eq("state", DocumentInfo.State.VALID),
								Restrictions.le("expires", triggerDate)), Restrictions.and(
								Restrictions.not(Restrictions.in("state", new DocumentInfo.State[] {
										DocumentInfo.State.EXPIRED, DocumentInfo.State.EXPIRED_NOTIFIED })),
								Restrictions.le("expires", todayDate))));
				result.addAll(new TreeSet<DocumentInfo>(criteria.list()));
				for (DocumentInfo info : result)
					info.getHeaders();
				return null;
			}
		});
		return result;
	}



	public synchronized void delete( final Class clazz, final Long id ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				Object found = session.get(clazz, id);
				if (found != null) {
					session.delete(found);
				}
				return null;
			}
		});
	}



	public synchronized void deleteDocumentInfo( final DocumentInfo info ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				session.delete(info);
				return null;
			}
		});
	}



	public synchronized void deleteDocumentInfo( final String title ) throws Exception {
		perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				List<DocumentInfo> result = new ArrayList<DocumentInfo>();
				Criteria criteria = session.createCriteria(DocumentInfo.class);
				criteria.add(Restrictions.eq("title", title));
				result.addAll(new TreeSet<DocumentInfo>(criteria.list()));
				for (DocumentInfo info : result)
					session.delete(info);
				return null;
			}
		});
	}



	public synchronized DocumentInfo findDocumentInfo( final String title ) throws Exception {
		final List<DocumentInfo> result = new ArrayList<DocumentInfo>();
		perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				Criteria criteria = session.createCriteria(DocumentInfo.class);
				criteria.add(Restrictions.eq("title", title));
				result.addAll(new TreeSet<DocumentInfo>(criteria.list()));
				for (DocumentInfo info : result)
					info.getHeaders().size();
				return null;
			}
		});
		return result.size() > 1 ? result.get(0) : null;
	}



	public synchronized byte[] getDoc( final DocumentInfo documentInfo ) throws Exception {
		return (byte[]) perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				DocumentInfo documentInfo2 = (DocumentInfo) session.get(DocumentInfo.class, documentInfo.getId());
				return documentInfo2.getDoc();
			}
		});
	}



	public synchronized DocumentCollection getDocumentCollectionByUserName( final String mail ) throws Exception {
		return (DocumentCollection) perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				Criteria criteria = session.createCriteria(User.class);
				criteria.add(Restrictions.eq("mail", mail));
				@SuppressWarnings("unchecked")
				List<User> users = criteria.list();
				if (users.size() > 0) {
					User user = users.get(0);
					DocumentCollection documentCollection = user.getDocumentCollection();
					documentCollection.getDocuments().size();
					documentCollection.getCarbonCopies().size();
					documentCollection.getUsers().size();
					return documentCollection;
				}
				return null;
			}
		});
	}



	public synchronized DocumentInfo getDocumentInfo( final Long id ) throws Exception {
		return getDocumentInfo(id, true);
	}



	public synchronized DocumentInfo getDocumentInfo( final Long id, final boolean headers ) throws Exception {
		final DocumentInfo[] result = new DocumentInfo[1];
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				result[0] = (DocumentInfo) session.get(DocumentInfo.class, id);
				if (result[0] != null)
					result[0].getHeaders().size();
				return null;
			}
		});
		return result[0];
	}



	public synchronized DocumentInfo getDocumentInfoByTitle( final String title ) throws Exception {
		final List<DocumentInfo> result = new ArrayList<DocumentInfo>();
		perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				Criteria criteria = session.createCriteria(DocumentInfo.class);
				criteria.add(Restrictions.eq("title", title));
				result.addAll(new TreeSet<DocumentInfo>(criteria.list()));
				for (DocumentInfo info : result)
					info.getHeaders().size();
				return null;
			}
		});
		return result.size() > 1 ? result.get(0) : null;
	}



	public synchronized List<Header> getHeaders( final DocumentInfo documentInfo ) throws Exception {
		final List<Header> result = new ArrayList<Header>();
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				DocumentInfo info = (DocumentInfo) session.get(DocumentInfo.class, documentInfo.getId());
				result.addAll(info.getHeaders());
				return null;
			}
		});
		return result;
	}



	public synchronized byte[] getPdf( final DocumentInfo documentInfo ) throws Exception {
		return (byte[]) perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				DocumentInfo documentInfo2 = (DocumentInfo) session.get(DocumentInfo.class, documentInfo.getId());
				return documentInfo2.getPdf();
			}
		});
	}



	public synchronized User getUser( final Long id ) throws Exception {
		return getUser(id, false);
	}



	public synchronized User getUser( final Long id, final boolean full ) throws Exception {
		return (User) perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				User user = (User) session.get(User.class, id);
				if (full && user != null) {
					user.getDocumentCollection().getDocuments().size();
					user.getDocumentCollection().getCarbonCopies().size();
					user.getDocumentCollection().getUsers().size();
				}
				return user;
			}
		});
	}



	public synchronized User getUser( final String userName ) throws Exception {
		return getUser(userName, false);
	}



	public synchronized User getUser( final String userName, final boolean full ) throws Exception {
		return (User) perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				List<User> users;
				Criteria criteria = session.createCriteria(User.class);
				criteria.add(Restrictions.eq("mail", userName));
				users = criteria.list();
				User user = users.size() > 0 ? users.get(0) : null;
				System.out.println("user: " + user);
				if (full && user != null) {
					user.getDocumentCollection().getDocuments().size();
					user.getDocumentCollection().getCarbonCopies().size();
					user.getDocumentCollection().getUsers().size();
				}
				System.out.println("user again: " + user);
				return user;
			}
		});
	}



	public List<DocumentInfo> listDocumentInfo( final User user ) throws Exception {
		return listDocumentInfo(user, false);
	}



	public List<DocumentInfo> listDocumentInfo( final User user, final boolean includeHeaders ) throws Exception {
		@SuppressWarnings("unchecked")
		List<DocumentInfo> result = (List<DocumentInfo>) perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				List<DocumentInfo> result = new ArrayList<DocumentInfo>();
				User user1 = (User) session.get(User.class, user.getId());
				DocumentCollection collection = user1.getDocumentCollection();
				result.addAll(collection.getDocuments());
				if (includeHeaders) {
					for (DocumentInfo info : result)
						info.getHeaders().size();
				}
				return result;
			}
		});
		Collections.sort(result, new Comparator<DocumentInfo>() {

			public int compare( DocumentInfo o1, DocumentInfo o2 ) {
				return o1.getTitle().compareTo(o2.getTitle());
			}
		});
		return result;
	}



	protected synchronized Object perform( Worker worker ) throws Exception {
		Object result = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			result = worker.doWork(session);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session.isOpen()) {
				// session.flush();
				session.close();
			}
		}
		return result;
	}



	public synchronized void saveDocumentInfo( final DocumentInfo info ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				session.save(info);
				return null;
			}
		});
	}



	public synchronized void saveOrUpdate( final Object object ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				session.saveOrUpdate(object);
				return null;
			}
		});
	}



	public synchronized void save( final Object object ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				session.save(object);
				return null;
			}
		});
	}



	public synchronized void update( final Object object ) throws Exception {
		perform(new Worker() {

			public Object doWork( Session session ) throws Exception {
				session.update(object);
				return null;
			}
		});
	}



	public synchronized void updateDocumentInfo( final DocumentInfo documentInfo ) throws Exception {
		perform(new Worker() {

			@SuppressWarnings("unchecked")
			public Object doWork( Session session ) throws Exception {
				session.update(documentInfo);
				return null;
			}
		});
	}



	protected synchronized void Xperform( Worker worker ) throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			System.out.println("EntityManager: " + entityManager);
			// worker.doWork( entityManager );
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction() != null && entityManager.getTransaction().isActive())
				entityManager.getTransaction().commit();
			e.printStackTrace();
			throw e;
		} finally {
			entityManager.close();
		}
	}
}

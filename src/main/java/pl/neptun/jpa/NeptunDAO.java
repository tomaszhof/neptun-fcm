package pl.neptun.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NeptunDAO {
	private static final String PERSISTENCE_UNIT = "UnitNeptunFCMTest";
	private static EntityManagerFactory emf;
	private static EntityManager em;

	private static void initializeHibernate() {
		System.out.println("Set hibernate connection...");
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		System.out.println("success.");
	}

	public static EntityManager em() {
		if (emf == null)
			initializeHibernate();
		if (em == null)
			em = emf.createEntityManager();
		return em;
	}
}

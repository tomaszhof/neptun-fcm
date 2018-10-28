package pl.neptun.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeptunJPA {
	private static final Logger log = LoggerFactory.getLogger(NeptunJPA.class);
	
	private static final String PERSISTENCE_UNIT = "UnitNeptunFCMTest";
	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	public static final int NO_TRANSACTION = 0;
    public static final int TRANSACTION_CARDINALITY_1 = -1;
    public static final int TRANSACTION_CARDINALITY = 10000;

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
	
	private static void transactionCommit() {
        if (em().getTransaction().isActive()) {
            em().getTransaction().commit();
        } else {
            log.debug("Illegal state. Transaction should be active (c)");
        }
    }


    public static void importBegin(int transactionCardinability) {
        if (transactionCardinability != NO_TRANSACTION) {
            em().getTransaction().begin();
        }
    }


    public static void importEnd(int transactionCardinability) {
        if (transactionCardinability != NO_TRANSACTION) {
            transactionCommit();
        }
    }
    
    public static void importRollback(int transactionCardinability) {
        if (transactionCardinability != NO_TRANSACTION) {
            if (em().getTransaction().isActive()) {
                em().getTransaction().rollback();
            } else {
                log.debug("Illegal state. Transaction should be active (r)");
            }
        }
    }
}

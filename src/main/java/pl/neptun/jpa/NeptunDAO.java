package pl.neptun.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/*
	JSON z QUESTION ITD

 */
@Deprecated
public class NeptunDAO {
	private static final Logger log = LoggerFactory.getLogger(NeptunDAO.class);

	protected static <T> List<Order> constructOrder(List<String> order, CriteriaBuilder cb, Root<T> r) {
		List<Order> result = new ArrayList<>();
		for (String o : order) {
			if (!o.startsWith("+") && !o.startsWith("-")) {
				result.add(cb.asc(r.get(o)));
			} else {
				Path<Object> path = r.get(o.substring(1));
				if (o.startsWith("+")) {
					result.add(cb.asc(path));
				} else { // "-"
					result.add(cb.desc(path));
				}
			}
		}
		return result;
	}

	public static <T> List<T> findAll(Class<T> clazz) {
		EntityManager em = NeptunJPA.em();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> qr = cq.from(clazz);

		List<String> order = new ArrayList<>();
		order.add("id");

		return em.createQuery(cq.select(qr).orderBy(constructOrder(order, cb, qr))).getResultList();
	}

	public static <T> List<Long> findAllIds(Class<T> clazz) {
		EntityManager em = NeptunJPA.em();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> qr = cq.from(clazz);
		cq.select(qr.get("id"));
		List<Long> ids = em.createQuery(cq).getResultList();
		return ids;
	}


	public static <T> T findById(Class<T> clazz, Long id) {
		T result = null;

		if (clazz != null) {
			result = (T) NeptunJPA.em().find(clazz, id);
		}
		return result;
	}

	public static <T> T findByCode(Class<T> clazz, String code) {

		CriteriaBuilder cb = NeptunJPA.em().getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);

		Root<T> root = q.from(clazz);

		Predicate srcEqPredicate = cb.equal(root.get("code"), code);
		q.select(root)
				.where(srcEqPredicate);
		TypedQuery<T> query = NeptunJPA.em().createQuery(q);

		T result = null;

		try {
			query.setMaxResults(1);
			result = query.getSingleResult();
			log.debug("Result: " + result);
		} catch (Exception e) {
			log.debug("Not found " + clazz.getName() + " where code=" + code);
			log.debug(e.getMessage());
		}

		return result;
	}

}

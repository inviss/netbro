package kr.co.d2net.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.MenuTbl_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class MenuManualDao {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<MenuTbl> findMenuById(String userId) {
		Query query = null;
		try {
			query = entityManager.createNamedQuery("Menu.findMenuById");
			query.setParameter("userId", userId);
			// query cache 설정
			query.setHint("org.hibernate.cacheable", true);
			return (List<MenuTbl>)query.getResultList();
		} catch (Exception e) {
			logger.error("findMenuById",e);
		}
		return Collections.EMPTY_LIST;
	}
	
	public MenuTbl getFirstMenuById(String userId) {
		Query query = null;
		try {
			query = entityManager.createNamedQuery("Menu.getFirstMenuById");
			query.setParameter("userId", userId);
			// query cache 설정
			query.setHint("org.hibernate.cacheable", true);
			Object o = query.getSingleResult();
			
			return (o != null) ? (MenuTbl)o : null;
		} catch (NoResultException e) {
			logger.info("No result value by id: "+userId);
		} catch (Exception e) {
			logger.error("getFirstMenuById", e);
		}
		return null;
	}
	

	public void save(MenuTbl menuTbl) {
		try {
			entityManager.getTransaction().begin();
			if(menuTbl.getMenuId() != null && menuTbl.getMenuId() > 0) {
				entityManager.merge(menuTbl);
			} else {
				entityManager.persist(menuTbl);
			}
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
		}
		entityManager.close();
	}
	
	/**
	 * 
	 * @param userId
	 * @return

	public List<Tuple> findMenuWithUserAuth(String userId) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> cq1 = cb.createTupleQuery();
		Root<MenuTbl> root1_1 = cq1.from(MenuTbl.class);
		Root<MenuTbl> root1_2 = cq1.from(MenuTbl.class);
		Root<MenuTbl> root1_3 = cq1.from(MenuTbl.class);

		Path<Integer> rmenuId = root1_1.get(MenuTbl_.menuId);
		Path<String> rmenuNm = root1_1.get(MenuTbl_.menuNm);
		//Path<String> rmenuEnNm = root1_1.get(MenuTbl_.regrId);

		cq1.where(cb.and(cb.between(root1_1.get(MenuTbl_.lft), root1_2.get(MenuTbl_.lft), root1_2.get(MenuTbl_.rgt)),
				cb.between(root1_1.get(MenuTbl_.lft), root1_3.get(MenuTbl_.lft), root1_3.get(MenuTbl_.rgt))));

		CriteriaQuery<Tuple> cq2 = cb.createTupleQuery();

		Root<MenuTbl> root2_1 = cq2.from(MenuTbl.class);
		Root<MenuTbl> root2_2 = cq2.from(MenuTbl.class);

		Path<Integer> menuId = root2_1.get(MenuTbl_.menuId);
		Path<String> menuNm = root2_1.get(MenuTbl_.menuNm);
		Path<Integer> lft = root2_1.get(MenuTbl_.lft);

		Expression<Long> menuCount = cb.count(root2_2.get(MenuTbl_.menuId));
		Expression<? extends Number> depth = cb.diff(menuCount, 1);

		cq2.multiselect(menuId.alias("menu_id"), menuNm.alias("menu_nm"), depth.alias("depth"));

		Predicate p1 = cb.equal(root2_1.get(MenuTbl_.menuId), 1);
		Predicate p2 = cb.equal(root2_1.get(MenuTbl_.useYn), "Y");
		Predicate p3 = cb.equal(root2_2.<String>get("useYn"), "Y");
		Predicate p4 = cb.between(root2_1.get(MenuTbl_.lft), root2_2.get(MenuTbl_.lft), root2_2.get(MenuTbl_.rgt));

		cq2.where(cb.and(p1, p2, p3, p4));

		cq2.groupBy(menuId, menuNm, lft);

		/*
		Expression<Long> pcount = cb.count(root1_2.get("menuId"));
		Expression<Long> sdepth = cb.count(root1_2.get("depth"));
		Expression<? extends Number> exp1 = cb.sum(sdepth, 1);
		Expression<Number> depth2 = cb.diff(pcount, exp1);
		
		//cq2.multiselect(rmenuId, rmenuNm);

		//cb.createQuery(cq2);

		return entityManager.createQuery(cq2).getResultList();
	}
	 */

}

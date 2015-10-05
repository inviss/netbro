package kr.co.d2net.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.AuthSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.AuthTbl;

import org.junit.BeforeClass;
import org.junit.Test;


public class AuthDaoTest {
	private static Repository repository;
	private static EntityManagerFactory factory;
	private static EntityManager em;
	private static EntityTransaction tx;

	private static Map<String, Object> props = new HashMap<String, Object>();

	protected EntityManager getEntityManager() {
		if (em == null) {
			throw new NullPointerException("entityManager must be set before executing any operations");
		}
		return em;
	}

	@BeforeClass
	public static void before() {
		factory = Persistence.createEntityManagerFactory("netbro_hsql");
		em = factory.createEntityManager();
		repository = new JpaRepository(em);
		tx = em.getTransaction();
		tx.begin();

		createSampleTest("000");
		createSampleTest("000");
		createSampleTest("000");
		createSampleTest("000");

		tx.commit();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	private static void createSampleTest(String workStatcd) {
		AuthTbl archiveTbl = new AuthTbl();
		repository.save(archiveTbl);
	}


	@Test
	public void test(){
	AuthTbl authTbl = null;
		
		try {
			authTbl = repository.find(AuthTbl.class,AuthSpecifications.getAuthMaxId()).single();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}


}

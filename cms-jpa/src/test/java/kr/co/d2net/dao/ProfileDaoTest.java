package kr.co.d2net.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.exception.DaoNonRollbackException;

import org.junit.BeforeClass;
import org.junit.Test;


public class ProfileDaoTest {
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

		createSampleTest("sbs");
		createSampleTest("kbs");
		createSampleTest("mbc");
		createSampleTest("jtbc");

		tx.commit();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	private static void createSampleTest(String profileNm) {
		ProFlTbl proFlTbl = new ProFlTbl();
		
		proFlTbl.setProFlnm(profileNm);
		repository.save(proFlTbl);
	}


	@Test
	public void getProfileInfo(){
		ProFlTbl proFlTbl = null;
		proFlTbl = repository.find(ProFlTbl.class,2L);
		
		System.out.println("proFlTbl.getProFlnm : " + proFlTbl.getProFlnm());
	}
	
}

package kr.co.d2net.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dao.support.SpecificationBuilder;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.exception.DaoNonRollbackException;

import org.junit.BeforeClass;
import org.junit.Test;


public class EquipmentDaoTest {
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
		EquipmentTbl equipmentTbl = null;

		try {
			Specification<EquipmentTbl> specification = SpecificationBuilder
					.forProperty("deviceId").equal("1").and()
					.forProperty("deviceNum").equal(2).build();
			
			equipmentTbl = repository.find(EquipmentTbl.class, specification).single();
			
			
		} catch (Exception e) {
			
		}
	}



}

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
import kr.co.d2net.dao.filter.UserSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.dto.UserTbl;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class UserDaoTest {
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

		createSampleTest("0");
		createSampleTest("1");
		createSampleTest("2");
		createSampleTest("3");

		tx.commit();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	private static void createSampleTest(String profileNm) {
		UserTbl tbl = new UserTbl();

		tbl.setUserId(profileNm);
		
		repository.save(tbl);
	}


	@Test
	@Ignore
	public void getStorageInfo(){
		UserTbl userTbl = null;
		//userTbl = repository.find(UserTbl.class,UserSpecifications.getLoginUserInfo("1", "2")).single();
		userTbl = repository.find(UserTbl.class,UserSpecifications.findUserAndAuthInfos("1")).single();
		System.out.println(userTbl);
	}
	
	@Test
	@Ignore
	public void finStorageList(){
		List<UserTbl> userTbls = null;
		
		String tmp = "1";

		try {
			SpecificationResult<UserTbl> result = repository.find(UserTbl.class,UserSpecifications.findUserAndAuthInfos(tmp));
			userTbls = result.list();
			System.out.println(userTbls);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	@Test
	public void test2(){
		List<UserTbl> userTbls = null;
		
		Map<String, Object> params = null;
		
		
		String paramUserId = "1";
		Integer paramMenuId = 1;

		try {
			SpecificationResult<UserTbl> result = repository.find(UserTbl.class,UserSpecifications.getUserControlAuth(paramUserId,paramMenuId));
			userTbls = result.list();
			System.out.println(userTbls);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

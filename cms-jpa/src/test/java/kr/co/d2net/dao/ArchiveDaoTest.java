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
import kr.co.d2net.dao.filter.ArchiveSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.exception.DaoNonRollbackException;

import org.junit.BeforeClass;
import org.junit.Test;


public class ArchiveDaoTest {
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
		createCategory("aaa", 0, "1");
		createCategory("bbb", 0, "2");
		createCategory("ccc", 0, "3");

		createSampleTest("000");
		createSampleTest("000");
		createSampleTest("000");
		createSampleTest("000");

		tx.commit();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	private static void createCategory(String cateNm, Integer depth, String node) {
		CategoryTbl categoryTbl = new CategoryTbl();
		categoryTbl.setCategoryNm(cateNm);
		categoryTbl.setDepth(depth);
		categoryTbl.setNodes(node);
		repository.save(categoryTbl);
	}

	private static void createSampleTest(String workStatcd) {
		ArchiveTbl archiveTbl = new ArchiveTbl();
		archiveTbl.setWorkStatCd(workStatcd);
		repository.save(archiveTbl);
	}


	@Test
	public void test(){
		ArchiveTbl archiveTbl = new ArchiveTbl();

		try {
			archiveTbl = repository.find(ArchiveTbl.class,ArchiveSpecifications.getArchiveInfoByCtiId(9L)).single();
			
			

		} catch (Exception e) {
		}
	}


}

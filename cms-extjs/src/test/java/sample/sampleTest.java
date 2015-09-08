package sample;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProBusiTbl.ProBusiId;
import kr.co.d2net.dto.ProBusiTbl_;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleTest extends BaseDaoConfig {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void test(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ProBusiTbl> root = cq.from(ProBusiTbl.class);

	//	Path<Long> proFlId = root.get(ProBusiTbl_.id).get(ProBusiTbl_.proFlId);
		Path<ProBusiId> proFlId = root.get(ProBusiTbl_.id);
		cq.select(proFlId);
		cq.where(cb.equal(root.get("id").get("busiPartnerId"),7));
		logger.debug("asdfasdfas");
		em.createQuery(cq).getResultList();
	}

}

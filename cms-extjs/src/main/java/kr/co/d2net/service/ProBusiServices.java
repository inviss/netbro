package kr.co.d2net.service;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.BusiPartnerDao;
import kr.co.d2net.dao.ProBusiDao;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProBusiTbl.ProBusiId;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.ProBusi;
import kr.co.d2net.exception.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class ProBusiServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProBusiDao proBusiDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProFlServices proFlServices;

	@Autowired
	private BusiPartnerDao busiPartnerDao;


	public Page<ProBusiTbl> findAllProBusi(Specification<ProBusiTbl> specification, Pageable pageable) {
		return proBusiDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<ProBusiTbl> probusies) {
		proBusiDao.save(probusies);
	}

	@Modifying
	@Transactional
	public void add(ProBusiTbl probusi) {
		proBusiDao.save(probusi);
	}


	public List<ProBusiTbl> findAll() {
		return proBusiDao.findAll();
	}

	/**
	 * 사업자별 프로파일 정보를 저장한다.
	 * @param partnerTbl
	 * @param proFlTbl
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveBusiInfo(BusiPartnerTbl partnerTbl) throws ServiceException{

		if(logger.isInfoEnabled()){
			logger.info("partnerTbl.partnerTbl() : " + partnerTbl.getTmpProflId());
		}
		
		if(StringUtils.isNotBlank(partnerTbl.getTmpProflId())){
			String[] tmp = partnerTbl.getTmpProflId().split(",");
			for(int i = 0; i<tmp.length;i++){

				ProBusiId proBusiId = new ProBusiId();
				ProBusiTbl probusi = new ProBusiTbl();

				long tmp_long = Long.parseLong(tmp[i]);

				proBusiId.setBusiPartnerId(partnerTbl.getBusiPartnerId());
				proBusiId.setProFlId(tmp_long);
				probusi.setId(proBusiId);
				proBusiDao.save(probusi);
			}
		}		
	}

	/**
	 * 사업자별 프로파일 정보를 삭제한다(update할때 기존정보 delete하고 save한다).
	 * @param partnerTbl
	 * @param proFlTbl
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void deleteBusiInfo(BusiPartnerTbl partnerTbl,ProFlTbl proFlTbl,Search search) throws ServiceException{

		BusiPartnerTbl busiPartnerTbl = busiPartnerDao.findOne(partnerTbl.getBusiPartnerId());
		Set<ProBusiTbl> proBusiTbls = busiPartnerTbl.getProBusi();

		proBusiDao.deleteInBatch(proBusiTbls);

	}



	/**
	 * 사업자별 프로파일(checkbox)의 정보를 가져온다.
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ProBusi> findProBusiInfo(Long id) throws ServiceException{

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ProBusiTbl> root = cq.from(ProBusiTbl.class);

		Path<List> proFlId = root.get("id").get("proFlId");

		cq.select(proFlId);
		cq.where(cb.equal(root.get("id").get("busiPartnerId"),id));

		return em.createQuery(cq).getResultList();

	}

}

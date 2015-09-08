package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.BusiPartnerDao;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.BusiPartnerTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.BusiPartner;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 비지니스 파트너 조회,저장을 담당하는 class.
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = true)
public class BusiPartnerServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BusiPartnerDao busiDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	public Page<BusiPartnerTbl> findAllBusiPartner(
			Specification<BusiPartnerTbl> specification, Pageable pageable) {
		return busiDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<BusiPartnerTbl> auths) {
		busiDao.save(auths);
	}

	/**
	 * 사업자 정보를 저장한다.
	 * 
	 * @param tbl
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveBusiPartner(BusiPartnerTbl tbl) throws ServiceException {
		busiDao.save(tbl);
	}

	/**
	 * 사업자 정보를 갱신한다.
	 * 
	 * @param tbl
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateBusiPartner(BusiPartnerTbl tbl) throws ServiceException {

		BusiPartnerTbl busiPartnerTbl = new BusiPartnerTbl();
		
		if(logger.isInfoEnabled()){
			logger.info("tbl.getBusiPartnerId() : " + tbl.getBusiPartnerId());
		}

		busiPartnerTbl = getBusiPartnerObj(tbl.getBusiPartnerId());
		busiPartnerTbl.setCompany(tbl.getCompany());
		busiPartnerTbl.setServYn(tbl.getServYn());
		busiPartnerTbl.setFtpServYn(tbl.getFtpServYn());
		busiPartnerTbl.setFtpId(tbl.getFtpId());
		busiPartnerTbl.setIp(tbl.getIp());
		busiPartnerTbl.setPort(tbl.getPort());
		busiPartnerTbl.setFtpId(tbl.getFtpId());
		busiPartnerTbl.setPassword(tbl.getPassword());
		busiPartnerTbl.setTransMethod(tbl.getTransMethod());
		busiPartnerTbl.setRemoteDir(tbl.getRemoteDir());

		busiDao.save(tbl);
	}

	/**
	 * 사업자 리스트 클릭시 해당되는 데이터값을 가져온다.
	 * 
	 * @param busiPartnerId
	 * @return
	 * @throws ServiceException
	 */
	public BusiPartnerTbl getBusiPartnerObj(Long busiPartnerId)
			throws ServiceException {
		return busiDao.findOne(busiPartnerId);
	}



	/**
	 * 비지니스파트너 list에서 정보 한건을 가져오는 method.
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<BusiPartner> findBusiPartnerInfo(Long id) throws ServiceException{

		String[] busiPartnerFields = { "busiPartnerId", "regrId", "modrId",
				"password", "company", "servYn", "ftpServYn", "ip", "port",
				"remoteDir", "transMethod", "ftpId", "srvUrl" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<BusiPartnerTbl> root = cq.from(BusiPartnerTbl.class);

		Path<Long> busiPartnerId = root.get(BusiPartnerTbl_.busiPartnerId);
		Path<String> regrId = root.get(BusiPartnerTbl_.regrId);
		Path<String> modrId = root.get(BusiPartnerTbl_.modrId);
		Path<String> password = root.get(BusiPartnerTbl_.password);
		Path<String> company = root.get(BusiPartnerTbl_.company);
		Path<String> servYn = root.get(BusiPartnerTbl_.servYn);
		Path<String> ftpServYn = root.get(BusiPartnerTbl_.ftpServYn);
		Path<String> ip = root.get(BusiPartnerTbl_.ip);
		Path<String> port = root.get(BusiPartnerTbl_.port);
		Path<String> remoteDir = root.get(BusiPartnerTbl_.remoteDir);
		Path<String> transMethod = root.get(BusiPartnerTbl_.transMethod);
		Path<String> ftpId = root.get(BusiPartnerTbl_.ftpId);
		Path<String> srvUrl = root.get(BusiPartnerTbl_.srvUrl);

		cq.multiselect(busiPartnerId, regrId, modrId, password, company,
				servYn, ftpServYn, ip, port, remoteDir, transMethod, ftpId,
				srvUrl);
		cq.where(cb.equal(busiPartnerId, id));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> busiPartnerList = typedQuery.getResultList();

		if (busiPartnerList != null) {
			List<BusiPartner> busiPartners = new ArrayList<BusiPartner>();

			for (Object[] list : busiPartnerList) {
				BusiPartner busiPartner = new BusiPartner();

				int i = 0;
				for (int j = 0; j < busiPartnerFields.length; j++) {
					ObjectUtils.setProperty(busiPartner, busiPartnerFields[j],
							list[i]);
					i++;
				}
				busiPartners.add(busiPartner);
			}
			return busiPartners;
		} else {
			return Collections.EMPTY_LIST;
		}
	}


	/**
	 * 사업자 리스트 정보를 가져온다.
	 * 
	 * @param busiPartnerTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public List<BusiPartnerTbl> findBusiPartnerInfos(
			BusiPartnerTbl busiPartnerTbl, Search search)
					throws ServiceException {

		PageRequest pageRequest = new PageRequest(search.getPageNo() - 1,
				SearchControls.CODE_LIST_COUNT, new Sort(new Order(
						Direction.DESC, "regDt")));

		Page<BusiPartnerTbl> proFlInfo;

		if (StringUtils.isBlank(search.getKeyword())) {
			proFlInfo = busiDao.findAll(pageRequest);
		} else {
			proFlInfo = busiDao.findAll(pageRequest);
		}

		return proFlInfo.getContent();

	}

	/**
	 * 사업자 전체의 카운터 갯수를 가져온다(페이지 이용).
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Long getAllBusiPartnerCount() throws ServiceException {
		return busiDao.count();
	}

	/**
	 * BusiPartner의 MaxId값을 가져온다.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long getBusiPartnerMaxId() throws ServiceException {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<BusiPartnerTbl> root = cq.from(BusiPartnerTbl.class);
		Path<Long> busiPartnerId = root.get(BusiPartnerTbl_.busiPartnerId);

		cq.select(cb.max(busiPartnerId));

		return (Long) em.createQuery(cq).getSingleResult();

	}
	
	
	/**
	 * 비지니스파트너 리스트를 조회하는 method.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BusiPartner> findBusiPartnerList() throws ServiceException {

		String[] busiPartnerFields = { "busiPartnerId", "regrId", "modrId",
				"password", "company", "servYn", "ftpServYn", "ip", "port",
				"remoteDir", "transMethod", "ftpId", "srvUrl" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<BusiPartnerTbl> root = cq.from(BusiPartnerTbl.class);

		Path<Long> busiPartnerId = root.get(BusiPartnerTbl_.busiPartnerId);
		Path<String> regrId = root.get(BusiPartnerTbl_.regrId);
		Path<String> modrId = root.get(BusiPartnerTbl_.modrId);
		Path<String> password = root.get(BusiPartnerTbl_.password);
		Path<String> company = root.get(BusiPartnerTbl_.company);
		Path<String> servYn = root.get(BusiPartnerTbl_.servYn);
		Path<String> ftpServYn = root.get(BusiPartnerTbl_.ftpServYn);
		Path<String> ip = root.get(BusiPartnerTbl_.ip);
		Path<String> port = root.get(BusiPartnerTbl_.port);
		Path<String> remoteDir = root.get(BusiPartnerTbl_.remoteDir);
		Path<String> transMethod = root.get(BusiPartnerTbl_.transMethod);
		Path<String> ftpId = root.get(BusiPartnerTbl_.ftpId);
		Path<String> srvUrl = root.get(BusiPartnerTbl_.srvUrl);

		cq.multiselect(busiPartnerId, regrId, modrId, password, company,
				servYn, ftpServYn, ip, port, remoteDir, transMethod, ftpId,
				srvUrl);

		cq.orderBy(cb.desc(busiPartnerId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> busiPartnerList = typedQuery.getResultList();

		if (busiPartnerList != null) {
			List<BusiPartner> busiPartners = new ArrayList<BusiPartner>();

			for (Object[] list : busiPartnerList) {
				BusiPartner busiPartner = new BusiPartner();

				int i = 0;
				for (int j = 0; j < busiPartnerFields.length; j++) {
					ObjectUtils.setProperty(busiPartner, busiPartnerFields[j],
							list[i]);
					i++;
				}
				busiPartners.add(busiPartner);
			}
			return busiPartners;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}

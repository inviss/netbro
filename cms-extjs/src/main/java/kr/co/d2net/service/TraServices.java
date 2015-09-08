package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Tra;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

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
@Transactional(readOnly = true)
public class TraServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TraDao traDao;

	@PersistenceContext
	private EntityManager em;

	/*
	 * 

	public Page<TraTbl> findAllTra(Specification<TraTbl> specification,
			Pageable pageable) {
		return traDao.findAll(specification, pageable);
	}
	 */

	/**
	 * 
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public TraTbl getTraObj(Long seq) throws ServiceException {
		return traDao.findOne(seq);
	}

	/**
	 * 
	 * @param traTbls
	 */
	@Modifying
	@Transactional
	public void addAll(Set<TraTbl> traTbls) {
		traDao.save(traTbls);
	}

	/**
	 * 
	 * @param traTbl
	 * @return
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public Long add(TraTbl traTbl) throws ServiceException {
		TraTbl traTbl2 = traDao.save(traTbl);
		return traTbl2.getSeq();
	}

	/**
	 * 
	 * @param traTbl
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void update(TraTbl traTbl) throws ServiceException {
		traDao.save(traTbl);
	}

	/**
	 * 사용자UI 페이징 관련 count.
	 * 
	 * @return

	public Long getAllTraCount() throws ServiceException {
		return traDao.count();
	}
	 */

	/**
	 * TC리스트 정보를 가져온다.
	 * 
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getTraCount(Search search) throws ServiceException {

		String[] traFields = { "deviceId", "seq", "ctNm", "reqDt", "regDt",
				"modDt", "errorCount", "jobStatus", "modrId", "prgrs", "ctId",
				"categoryNm", "categoryId" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<TraTbl> root = cq.from(TraTbl.class);
		Root<ContentsTbl> root1 = cq.from(ContentsTbl.class);
		Root<CategoryTbl> root2 = cq.from(CategoryTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);

		Path<String> deviceId = root.get(TraTbl_.deviceId);
		Path<Long> seq = root.get(TraTbl_.seq);
		Path<String> ctNm = root1.get(ContentsTbl_.ctNm);
		Path<Date> reqDt = root.get(TraTbl_.reqDt);
		Path<Date> regDt = root.get(TraTbl_.regDt);
		Path<Date> modDt = root.get(TraTbl_.modDt);
		Path<Integer> errorCount = root.get(TraTbl_.errorCount);
		Path<String> jobStatus = root.get(TraTbl_.jobStatus);
		Path<String> modrId = root.get(TraTbl_.modrId);
		Path<Integer> prgrs = root.get(TraTbl_.prgrs);
		Path<Long> ctId = root.get(TraTbl_.ctId);

		Path<String> categoryNm = root2.get(CategoryTbl_.categoryNm);
		Path<Integer> categoryId = root2.get(CategoryTbl_.categoryId);

		cq.multiselect(deviceId, seq, ctNm, reqDt, regDt, modDt, errorCount,
				jobStatus, modrId, prgrs, ctId, categoryNm, categoryId);
		cq.where(ObjectLikeSpecifications.traFilterSearch(cb, cq, search, root,
				root1, root2,root3));
		cq.orderBy(cb.desc(seq));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> traList = typedQuery.getResultList();

		if (traList != null) {
			List<Tra> tras = new ArrayList<Tra>();

			for (Object[] list : traList) {
				Tra tra = new Tra();

				int i = 0;
				for (int j = 0; j < traFields.length; j++) {
					ObjectUtils.setProperty(tra, traFields[j], list[i]);
					i++;
				}
				tras.add(tra);
			}
			return (Integer) tras.size();
		} else {
			return 0;
		}
	}

	/**
	 * TC리스트 정보를 가져온다.
	 * 
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tra> findTraInfos(Search search) throws ServiceException {

		String[] traFields = { "deviceId", "seq", "ctNm", "reqDt", "regDt",
				"modDt", "errorCount", "jobStatus", "modrId", "prgrs", "ctId",
				"categoryNm", "categoryId","proFlnm" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<TraTbl> root = cq.from(TraTbl.class);
		Root<ContentsTbl> root1 = cq.from(ContentsTbl.class);
		Root<CategoryTbl> root2 = cq.from(CategoryTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);

		Path<String> deviceId = root.get(TraTbl_.deviceId);
		Path<Long> seq = root.get(TraTbl_.seq);
		Path<String> ctNm = root1.get(ContentsTbl_.ctNm);
		Path<Date> reqDt = root.get(TraTbl_.reqDt);
		Path<Date> regDt = root.get(TraTbl_.regDt);
		Path<Date> modDt = root.get(TraTbl_.modDt);
		Path<Integer> errorCount = root.get(TraTbl_.errorCount);
		Path<String> jobStatus = root.get(TraTbl_.jobStatus);
		Path<String> modrId = root.get(TraTbl_.modrId);
		Path<Integer> prgrs = root.get(TraTbl_.prgrs);
		Path<Long> ctId = root.get(TraTbl_.ctId);
		Path<String> proFlnm = root3.get("proFlnm");

		Path<String> categoryNm = root2.get(CategoryTbl_.categoryNm);
		Path<Integer> categoryId = root2.get(CategoryTbl_.categoryId);

		cq.multiselect(deviceId, seq, ctNm, reqDt, regDt, modDt, errorCount,
				jobStatus, modrId, prgrs, ctId, categoryNm, categoryId,proFlnm);

		cq.where(ObjectLikeSpecifications.traFilterSearch(cb, cq, search, root,
				root1, root2,root3));
		cq.orderBy(cb.desc(seq));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		int startPage = 0;
		int endPage = 0;

		startPage = (search.getPageNo() - 1) * SearchControls.TRA_LIST_COUNT;
		endPage = startPage + SearchControls.TRA_LIST_COUNT;

		if (logger.isDebugEnabled()) {
			logger.debug("startPage :" + startPage);
			logger.debug("endPage :" + endPage);
		}

		typedQuery.setFirstResult(startPage);
		typedQuery.setMaxResults(endPage);

		List<Object[]> traList = typedQuery.getResultList();

		if (traList != null) {
			List<Tra> tras = new ArrayList<Tra>();

			for (Object[] list : traList) {
				Tra tra = new Tra();

				int i = 0;
				for (int j = 0; j < traFields.length; j++) {
					ObjectUtils.setProperty(tra, traFields[j], list[i]);
					i++;
				}
				tras.add(tra);
			}
			return tras;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * TraTbl의 JobStatus 상태를 update한다.
	 * 
	 * @param tra
	 */
	@Modifying
	@Transactional
	public void updateTraJobStatus(TraTbl tra) throws ServiceException {
		TraTbl traTbl = getTraObj(tra.getSeq());

		traTbl.setJobStatus("I");
		traTbl.setPrgrs(0);

		traDao.save(traTbl);

	}
}

package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.BusiPartnerCategoryDao;
import kr.co.d2net.dao.BusiPartnerDao;
import kr.co.d2net.dto.BusiPartnerCategoryTbl;
import kr.co.d2net.dto.BusiPartnerCategoryTbl.BusiPartnerCategoryId;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.ProFlTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.BusiPartnerCategory;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

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
import org.springframework.web.bind.annotation.ModelAttribute;



/**
 * 비지니스파트너의 프로파일 맵핑 관련된 class.
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class BusiPartnerCategoryServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BusiPartnerCategoryDao busiPartnerCategoryDao;

	@Autowired
	private BusiPartnerDao BusiPartnerDao;


	@PersistenceContext
	private EntityManager em;


	@Autowired
	private MessageSource messageSource;

	/**
	 * 
	 * @param specification
	 * @param pageable
	 * @return
	 */
	public Page<BusiPartnerCategoryTbl> findAllBusiPartnerCategory(Specification<BusiPartnerCategoryTbl> specification, Pageable pageable) {
		return busiPartnerCategoryDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<BusiPartnerCategoryTbl> busiPartners) {
		busiPartnerCategoryDao.save(busiPartners);
	}

	/**
	 * 
	 * @param busiPartner
	 */
	@Modifying
	@Transactional
	public void add(BusiPartnerCategoryTbl busiPartner) {
		busiPartnerCategoryDao.save(busiPartner);
	}



	/**
	 * 카테고리 맵핑 데이터를 저장한다.
	 * @param busiPartner
	 * @param categoryTbl
	 * @throws Exception 
	 */
	@Modifying
	@Transactional
	public void saveBusiCategoryPartner(BusiPartnerTbl busiPartner,CategoryTbl categoryTbl) throws Exception {

		//ex)tmpBusiPartnerId = "1,2,3,4,5" 이런식으로 옴

		logger.debug("busiPartner.getTmpBusiPartnerId() : " + busiPartner.getTmpBusiPartnerId());

		if(StringUtils.isNotBlank(busiPartner.getTmpBusiPartnerId())){
			String[] tmp =  busiPartner.getTmpBusiPartnerId().split(",");

			if(logger.isInfoEnabled()){
				logger.info("busiPartner.getBusiPartnerId() : " + busiPartner.getTmpBusiPartnerId());
			}

			if(StringUtils.isNotBlank(busiPartner.getTmpBusiPartnerId())){
				for(int i = 0; i<tmp.length;i++){

					BusiPartnerCategoryTbl busiPartnerCategory = new BusiPartnerCategoryTbl();
					BusiPartnerCategoryId busiPartnerCategoryId = new BusiPartnerCategoryId();

					busiPartnerCategoryId.setCategoryId(categoryTbl.getCategoryId());
					/*
					 *  save로직은 임시로직.
					 * setCtTyp("test" + i) 부분을 한것은 ctTyp 미구현.
					 * save할때 ctTyp를 test로 저장하게함.
					 */
					busiPartnerCategoryId.setCtTyp("test"+i);

					long tmp_long = Long.parseLong(tmp[i]);

					busiPartnerCategory.setId(busiPartnerCategoryId);
					busiPartnerCategory.setBusiPartnerId(tmp_long);

					busiPartnerCategoryDao.save(busiPartnerCategory);

				}
			}		
		}else{
			logger.error("eeeeeeeeeeeeeeeeeeeeeeee");
			throw new Exception("");

		}
	}


	/**
	 * 카테고리 맵핑에서 사업자 맵핑 데이터를 지움.
	 * delete 하고 insert하는 방식으로 하기 위해 구현.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void deleteBusiInfo(BusiPartnerTbl partnerTbl,CategoryTbl categoryTbl,Search search) throws ServiceException{

		if(logger.isInfoEnabled()){
			logger.info("categoryTbl.getCategoryId() : " + categoryTbl.getCategoryId());
		}

		BusiPartnerCategoryTbl tbl = new BusiPartnerCategoryTbl();
		BusiPartnerCategoryId id = new BusiPartnerCategoryId();
		try {
			//기존 데이터를 지우기 위해 categoryId 소속 count 갯수를 알아야함.
			Long count = getBusiPartnerCount(partnerTbl, categoryTbl, search);

			/*
			 * 밑에 delete로직은 임시로직.
			 * setCtTyp("test" + i) 부분을 한것은 ctTyp 미구현.
			 * save할때 ctTyp를 test로 저장하게함.
			 */
			for(int i = 0; i<count; i++){
				id.setCategoryId(categoryTbl.getCategoryId());
				id.setCtTyp("test" + i);
				tbl.setId(id);
				busiPartnerCategoryDao.delete(tbl);
			}
		} catch (Exception e) {
			logger.error("deleteBusiInfo",e);
		}

	}



	/**
	 * busiPartnerId 이용한 categoryList 데이터 구현.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BusiPartnerCategory> findBusiPartnerInfo(@ModelAttribute("busiPartnerTbl") BusiPartnerTbl partnerTbl,
			@ModelAttribute("categoryTbl") CategoryTbl categoryTbl,
			@ModelAttribute("search") Search search)  
					throws ServiceException {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<BusiPartnerCategoryTbl> root = cq.from(BusiPartnerCategoryTbl.class);
		Path<Integer> categoryId = root.get("busiPartnerId");

		cq.multiselect(categoryId);
		cq.where(cb.equal(root.get("id").get("categoryId"),categoryTbl.getCategoryId()));

		return  em.createQuery(cq).getResultList();
	}



	/**
	 * 비지니스파트너의 카운터를 조회하는 method.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long getBusiPartnerCount(@ModelAttribute("busiPartnerTbl") BusiPartnerTbl partnerTbl,
			@ModelAttribute("categoryTbl") CategoryTbl categoryTbl,
			@ModelAttribute("search") Search search)  
					throws ServiceException {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<BusiPartnerCategoryTbl> root = cq.from(BusiPartnerCategoryTbl.class);
		Path<Integer> categoryId = root.get("busiPartnerId");

		cq.select(cb.count(categoryId));
		cq.where(cb.equal(root.get("id").get("categoryId"),categoryTbl.getCategoryId()));

		return  (long) em.createQuery(cq).getSingleResult();

	}




	/**
	 * 카테고리 맵핑에서 사업자 정보 리스트(checkbox)를 가져온다.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BusiPartnerCategory> findBusiPartnerMappingProfileNm(@ModelAttribute("busiPartnerTbl") BusiPartnerTbl partnerTbl,
			@ModelAttribute("categoryTbl") CategoryTbl categoryTbl,
			@ModelAttribute("search") Search search)  
					throws ServiceException {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		String[] busiPartnerFields = {"busiPartnerId","proFlNm"};

		Root<BusiPartnerTbl> root = cq.from(BusiPartnerTbl.class);
		Root<ProFlTbl> root1 = cq.from(ProFlTbl.class);
		Root<ProBusiTbl> root2 = cq.from(ProBusiTbl.class);

		Path<Integer> busiPartnerId = root.get("busiPartnerId");
		Path<Long> proFlid = root1.get(ProFlTbl_.proFlId);
		Path<Integer> proFlnm = root1.get("proFlnm");
		Path<Integer> busiPartnerId1 = root2.get("id").get("busiPartnerId");
		Path<Integer> proFlid1 = root2.get("id").get("proFlId");

		cq.multiselect(busiPartnerId,proFlnm);
		cq.where(cb.and(cb.equal(busiPartnerId,busiPartnerId1),cb.equal(proFlid,proFlid1)));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> objects =  typedQuery.getResultList();
		List<BusiPartnerCategory> busiCategories = new ArrayList<BusiPartnerCategory>();

		for(Object[] list : objects) {
			BusiPartnerCategory monitoring = new BusiPartnerCategory();

			int i = 0;
			for(int j = 0; j<busiPartnerFields.length; j++){
				ObjectUtils.setProperty(monitoring, busiPartnerFields[j], list[i]);			
				i++;
			}
			busiCategories.add(monitoring);
		}
		return busiCategories;
	}

}

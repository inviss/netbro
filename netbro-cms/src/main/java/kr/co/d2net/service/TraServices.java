package kr.co.d2net.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Tra;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.PropertiesUtils;

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

@Service
@Transactional(readOnly=true)
public class TraServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TraDao traDao;

	@PersistenceContext
	private EntityManager em;

	public Page<TraTbl> findAllTra(Specification<TraTbl> specification, Pageable pageable) {
		return traDao.findAll(specification, pageable);
	}

	public TraTbl getTraObj(Long seq) throws ServiceException{

		String message = "";

		try {
			return traDao.findOne(seq);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}

	@Modifying
	@Transactional
	public void addAll(Set<TraTbl> traTbls) {
		traDao.save(traTbls);
	}

	@Modifying
	@Transactional
	public Long add(TraTbl traTbl) throws ServiceException{

		String message = "";

		try {
			TraTbl traTbl2 = traDao.save(traTbl);
			return traTbl2.getSeq();
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}

	@Modifying
	@Transactional
	public void update(TraTbl traTbl) throws ServiceException{

		String message = "";

		try {
			traDao.save(traTbl);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}


	/**
	 * 사용자UI 페이징 관련 count.
	 * @return
	 */
	public Long getAllTraCount() throws ServiceException{

		String message = "";

		try {
			return traDao.count();
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}


	/**
	 * TC리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long getTraCount(Search search) throws ServiceException{

		String message = "";

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<TraTbl> root = cq.from(TraTbl.class);
		Root<ContentsTbl> root1 = cq.from(ContentsTbl.class);
		Root<CategoryTbl> root2 = cq.from(CategoryTbl.class);

		Path<Long> seq = root.get(TraTbl_.seq);

		cq.select(cb.count(seq));


		try {
			//컨텐츠명 데이터 값이 입력되었을경우.
			if(StringUtils.isNotBlank(search.getSelectContentNm())){
				logger.debug("search.getSecondSelectBox() : " + search.getSecondSelectBox());
				//두번째 selectbox가 빈값일 때.
				//첫번째 selectbox가 선택하세요, 전체 일경우 두번째 selectbox가 보이지 않음.
				if(StringUtils.isBlank(search.getSecondSelectBox())){
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")));
				}else{
					//첫번째 selectbox가 상태일 경우.
					//if("S".equals(search.getSecondSelectBox().equals("S")) || "C".equals(search.getSecondSelectBox().equals("C")) ||  "E".equals(search.getSecondSelectBox().equals("E"))){
					if((search.getSecondSelectBox().equals("S")) || (search.getSecondSelectBox().equals("C")) || (search.getSecondSelectBox().equals("E"))){
						//첫번째 selectbox가 전체일 경우.
						//secondbox의 데이터 값 조회를 하지 않음.
						if((search.getFirstSelectBox().equals("all"))){
							cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")));
						}else{
							cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")),cb.equal(root.get(TraTbl_.jobStatus), search.getSecondSelectBox()));
						}
					}else{
						//첫번째 selectbox가 카테고리일 경우.
						if((search.getFirstSelectBox().equals("all"))){
							cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")));
						}else{
							cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")),cb.equal(root2.get(CategoryTbl_.categoryId), search.getSecondSelectBox()));
						}
					}	
				}
				//컨텐츠명 조회 데이터 값이 있는경우.
				//두번째 selectbox 데이터가 없는 경우, 첫번째 selectbox의 값이 전체일 경우.
			}else if(StringUtils.isBlank(search.getSecondSelectBox()) || search.getFirstSelectBox().equals("all") ){
				cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId))));
			}else{
				//상태 데이터 값은 숫자 : ex:) S,C,E
				//where 조건에서 jobstatus를 비교.	
				if((search.getSecondSelectBox().equals("S")) || (search.getSecondSelectBox().equals("C")) || (search.getSecondSelectBox().equals("E"))){
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.equal(root.get(TraTbl_.jobStatus), search.getSecondSelectBox())));
				}
				//카테고리 데이터 값은 숫자 : ex:) 1,2,3,4,5
				//where 조건에서 categoryId를 비교.	
				else{
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.equal(root2.get(CategoryTbl_.categoryId), search.getSecondSelectBox())));
				}
			}
			return (Long)em.createQuery(cq).getSingleResult();

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}


	/**
	 * TC리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tra> findTraInfos(Search search) throws ServiceException{

		String message = "";

		String[] traFields = {"deviceId","seq", "ctNm", "reqDt","regDt","modDt","errorCount","jobStatus","modrId","prgrs","ctId","categoryNm","categoryId"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<TraTbl> root = cq.from(TraTbl.class);
		Root<ContentsTbl> root1 = cq.from(ContentsTbl.class);
		Root<CategoryTbl> root2 = cq.from(CategoryTbl.class);

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

		cq.multiselect(deviceId,seq,ctNm,reqDt,regDt,modDt,errorCount,jobStatus,modrId,prgrs,ctId,categoryNm,categoryId);

		try {
			/*
			 * 처음 페이지가 로드되면 컨텐츠명 조회 데이터 값이 없음.
			 */

			//컨텐츠명 데이터 값이 입력되었을경우.
			if(StringUtils.isNotBlank(search.getSelectContentNm())){
				//두번째 selectbox가 빈값일 때.
				//첫번째 selectbox가 선택하세요, 전체 일경우 두번째 selectbox가 보이지 않음.
				if(StringUtils.isBlank(search.getSecondSelectBox())){
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")));
				}else{
					//첫번째 selectbox가 상태일 경우.
					if((search.getSecondSelectBox().equals("S")) || (search.getSecondSelectBox().equals("C")) || (search.getSecondSelectBox().equals("E"))){
						//첫번째 selectbox가 전체일 경우.
						//secondbox의 데이터 값 조회를 하지 않음.
						cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")),cb.equal(root.get(TraTbl_.jobStatus), search.getSecondSelectBox()));
					}else{
						//첫번째 selectbox가 카테고리일 경우.
						cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getSelectContentNm()+"%")),cb.equal(root2.get(CategoryTbl_.categoryId), search.getSecondSelectBox()));
					}	
				}
				//컨텐츠명 조회 데이터 값이 있는경우.
				//두번째 selectbox 데이터가 없는 경우, 첫번째 selectbox의 값이 전체일 경우.
			}else if(StringUtils.isBlank(search.getSecondSelectBox()) || search.getFirstSelectBox().equals("all") ){
				cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId))));
			}else{
				//상태 데이터 값은 숫자 : ex:) S,C,E
				//where 조건에서 jobstatus를 비교.
				if((search.getSecondSelectBox().equals("S")) || (search.getSecondSelectBox().equals("C")) || (search.getSecondSelectBox().equals("E"))){
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.equal(root.get(TraTbl_.jobStatus), search.getSecondSelectBox())));
				}
				//카테고리 데이터 값은 숫자 : ex:) 1,2,3,4,5
				//where 조건에서 categoryId를 비교.	
				else{
					cq.where(cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.equal(root2.get(CategoryTbl_.categoryId), search.getSecondSelectBox())));
				}
			}

			cq.orderBy(cb.desc(seq));

			TypedQuery<Object[]> typedQuery = em.createQuery(cq);

			int startPage = (search.getPageNo()-1) * SearchControls.USER_LIST_COUNT;
			int endPage = startPage+SearchControls.USER_LIST_COUNT;

			if(logger.isDebugEnabled()){
				logger.debug("startPage : " + startPage);
				logger.debug("endPage : " + endPage);
			}

			typedQuery.setFirstResult(startPage);
			typedQuery.setMaxResults(20);

			List<Object[]> traList =  typedQuery.getResultList();

			if(traList != null){
				List<Tra> tras = new ArrayList<Tra>();

				for(Object[] list : traList) {
					Tra tra = new Tra();

					int i = 0;
					for(int j = 0; j<traFields.length; j++){
						ObjectUtils.setProperty(tra, traFields[j], list[i]);			
						i++;
					}
					tras.add(tra);
				}
				return tras;
			}else{
				return Collections.EMPTY_LIST;
			}
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}


	/**
	 * TraTbl의 JobStatus 상태를 update한다.
	 * @param tra
	 */
	@Modifying
	@Transactional
	public void updateTraJobStatus(TraTbl tra) throws ServiceException{
		String message = "";
		try {
			TraTbl traTbl = getTraObj(tra.getSeq());

			traTbl.setJobStatus("I");
			traTbl.setPrgrs(0);

			traDao.save(traTbl);

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}
}

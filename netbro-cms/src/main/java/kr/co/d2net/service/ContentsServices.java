package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dao.ContentsInstDao;
import kr.co.d2net.dao.ContentsModDao;
import kr.co.d2net.dao.filter.ContentsLikeSpecifications;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.schedule.delection.BatchDeleteQueue;
import kr.co.d2net.utils.ObjectUtils;

import org.hibernate.Hibernate;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 컨텐츠 관련 정보를 조회하기위한 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class ContentsServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContentsDao contentsDao;

	@Autowired
	private ContentsModDao contentsModDao;

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ContentsInstDao contentsInstDao;
	@Autowired
	private MessageSource messageSource;

	public Page<ContentsTbl> findAllContents(Specification<ContentsTbl> specification, Pageable pageable) throws ServiceException{
		String message ="";
		try{
			return contentsDao.findAll(specification, pageable);
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
		}catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	public List<ContentsTbl> findFilterAll(Search search) throws ServiceException{
		String message ="";
		try{
			return contentsDao.findAll(ContentsLikeSpecifications.contentsFilterSearch(search));
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
		}catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		}  catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	public List<ContentsTbl> findAll(Search search) throws ServiceException{
		String message ="";
		try{
			return contentsDao.findAll(ContentsLikeSpecifications.deleteRangeSearch(search));
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
		}catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	@Modifying
	@Transactional
	public void addAll(Set<ContentsTbl> contentsTbls) throws ServiceException{
		String message ="";
		try{
			contentsDao.save(contentsTbls);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

	}

	/**
	 * 신규저장을 하거나 기존에 정보가 존재한다면 update 성공한다면 키값을 리턴한다.
	 * @param contentsTbl
	 * @return ct_id
	 */
	@Modifying
	@Transactional
	public Long add(ContentsTbl contentsTbl) throws ServiceException{
		String message ="";
		try{
			ContentsTbl tbl = contentsDao.save(contentsTbl);

			return tbl.getCtId();
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}

	/**
	 * 전체조회시 조회된 조회 갯수를 반환한다.(paging 용도)
	 * @return
	 */
	public Long countAllContents(Search search) throws ServiceException{
		String message ="";
		try {
			long count = contentsDao.count(ContentsLikeSpecifications.contentsFilterSearch(search));
			//Thread.sleep(60000l);

			return count;
		} catch (PersistenceException e) {
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
		} catch(DataAccessResourceFailureException | JDBCConnectionException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}



	/**
	 * contents_tbl 에서 ct_id 단일 건을 조회한다.
	 * @param ctId
	 * @return
	 */
	public ContentsTbl getContentObj(Long ctId) throws ServiceException{
		String message ="";
		try {
			ContentsTbl contentsTbl = contentsDao.findOne(ctId);
			//contentsTbl.setContentsInst(null);
			Hibernate.initialize(contentsTbl.getContentsInst());

			return contentsTbl;
		} catch (PersistenceException e) {
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
		}catch(TransactionSystemException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		}  catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	/**
	 * 컨텐츠 정보를 업데이트 한다.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void updateCotentObj(ContentsTbl contentsTbl) throws ServiceException{
		String message ="";
		try {

			ContentsTbl old = contentsDao.findOne(contentsTbl.getCtId());
			old.setRpimgKfrmSeq(contentsTbl.getRpimgKfrmSeq());
			contentsDao.save(old);
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		}  catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

	}

	/**
	 * contents_tbl에 값을 저장(수정)한다.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public ContentsTbl saveContentObj(ContentsTbl contentsTbl) throws ServiceException{
		String message ="";

		try{


			ContentsTbl result = contentsDao.save(contentsTbl);

			saveContentHistory(contentsTbl);

			return result;
		}catch (PersistenceException e) {
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch(JDBCConnectionException e){
			System.out.println("===========JDBCConnectionException=========================");
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			System.out.println("==============Exception======================");
			if(e instanceof CannotCreateTransactionException){
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else{
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}
	}



	/**
	 * where 조건에 따른 조회결과를 반환한다.
	 * 
	 * @param search
	 * @return List<SearchMeta>
	 */
	public List<Content> getAllContentsInfo(Search search)throws ServiceException{
		String message ="";
		String[] ctFields = {"ctId", "ctNm", "categoryId","brdDd","regDt","vdQlty","delDd","rpimgKfrmSeq","ctLeng"};
		String[] ctiFields = {"flPath", "wrkFileNm","vdHresol","vdVresol"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = from.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);


		criteriaQuery.where(ObjectLikeSpecifications.contentsFilterSearch(criteriaBuilder,criteriaQuery,from,inst,segment,episode,category ,search));

		try {

			Selection[] s = new Selection[ctFields.length + ctiFields.length];

			int i=0;

			for(int j=0; j<ctFields.length; j++) {

				s[i] = from.get(ctFields[j]);
				i++;

			}

			for(int j=0; j<ctiFields.length; j++) {	

				s[i] = inst.get(ctiFields[j]);				
				i++;

			}

			CriteriaQuery<Object[]> select = criteriaQuery.select(
					criteriaBuilder.array(s)
					).orderBy(criteriaBuilder.desc(from.get("ctId")));

			TypedQuery<Object[]> typedQuery = em.createQuery(select);

			/**
			 * 페이징 시작. 
			 * searchTyp가 imgage면 한페이지에 25개씩
			 * searchTyp가 list면 한페이지에 50개씩
			 */

			int startPage = 0;
			int endPage = 0;

			if(search.getSearchTyp().equals("image")){

				startPage = (search.getPageNo()-1) * SearchControls.CLIP_IMAGE_COUNT;
				endPage = startPage+SearchControls.CLIP_IMAGE_COUNT;

			}else{

				startPage = (search.getPageNo()-1) * SearchControls.CLIP_LIST_COUNT;
				endPage = startPage+SearchControls.CLIP_LIST_COUNT;

			}

			if(logger.isDebugEnabled()){

				logger.debug("startPage :"+startPage);
				logger.debug("endPage :"+endPage);

			}


			typedQuery.setFirstResult(startPage);
			typedQuery.setMaxResults(endPage);

			List<Object[]> list2 = typedQuery.getResultList();

			List<Content> contents = new ArrayList<Content>();

			for(Object[] list : list2) {

				Content content = new Content();

				i=0;
				for(int j=0; j<ctFields.length; j++) {

					ObjectUtils.setProperty(content, ctFields[j], list[i]);
					i++;

				}

				for(int j=0; j<ctiFields.length; j++) {

					ObjectUtils.setProperty(content, ctiFields[j], list[i]);
					i++;

				}

				contents.add(content);

			}

			return contents;
		} catch (Exception e) {
			return Collections.emptyList();
		}



	}

	/**
	 * 컨텐츠를 삭제요청을 한다.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void deleteContent(ContentsTbl contentsTbl) throws ServiceException{
		String message ="";
		try {

			contentsTbl.setUseYn("N");
			contentsDao.save(contentsTbl);

			List<ContentsInstTbl> contentsInstTbls = contentsInstDao.findAll(ContentsLikeSpecifications.findContentIntByCtId(contentsTbl.getCtId()));

			if(contentsInstTbls != null){
				for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
					// DB정보는 USE_YN = 'Y'로 변경
					contentsInstTbl.setUseYn("N");
					contentsInstDao.save(contentsInstTbl);

					// 물리적인 파일은 삭제 Queue에 등록하여 별도로 처리
					BatchDeleteQueue.putJob(contentsInstTbl);

				}
			}

		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

	}


	/**
	 * 데이터 정리상태의 값을 저장한다. contents_mod_tbl에 값을 저장.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void saveContentHistory(ContentsTbl contentsTbl) throws ServiceException{
		String message ="";
		ContentsModTbl contentsModTbl = new ContentsModTbl();
		contentsModTbl.setModDt(new Date());
		contentsModTbl.setModId(contentsTbl.getModrId());
		contentsModTbl.setDataStatcd(contentsTbl.getDataStatCd());
		contentsModTbl.setContentsTbl(contentsTbl);
		try{
			contentsModDao.save(contentsModTbl);

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

	}


	/**
	 * EPISODE_ID로 조회하여 등록된 컨텐츠가 존재하는지를 확인한다.
	 * @return
	 */
	public Long findContentsForEpisode(EpisodeId id) throws ServiceException{
		String message ="";
		try {
			long count =contentsDao.count(ContentsLikeSpecifications.equalEpisode(id));
			logger.debug("count :"+count);

			return count;
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
		} catch(DataAccessResourceFailureException e){
			message = messageSource.getMessage("error.015",null,null);
			throw new DaoNonRollbackException("015", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

}


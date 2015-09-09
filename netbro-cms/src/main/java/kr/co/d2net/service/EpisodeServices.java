package kr.co.d2net.service;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.EpisodeDao;
import kr.co.d2net.dao.filter.EpissodeLikeSpecifications;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.PropertiesUtils;

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
 * 에피소드 관련정보를 조회하기위해서 사용되는 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class EpisodeServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EpisodeDao episodeDao;

	@Autowired
	private SegmentServices segmentServices;

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MessageSource messageSource;


	public Page<EpisodeTbl> findAllEpisode(Specification<EpisodeTbl> specification, Pageable pageable) throws ServiceException {
		String message = "";
		try {
			
			return episodeDao.findAll(specification, pageable);
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	@Modifying
	@Transactional
	public void addAll(Set<EpisodeTbl> episodes) throws ServiceException{
		String message = "";
		try {
			episodeDao.save(episodes);
			
		} catch (PersistenceException e) {
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
	 * 에피소드를 신규로 추가한다.
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void add(EpisodeTbl episode) throws ServiceException {
		String message = "";
		try {
			episodeDao.save(episode);
			
		} catch (PersistenceException e) {
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
	@Modifying
	@Transactional
	public String addEpisodeInfo(EpisodeTbl episodeTbl, EpisodeId id) throws ServiceException {
		String message = "";
		
			Integer maxEpisodeId = MaxCount(id.getCategoryId());
			EpisodeTbl newEpisode = new EpisodeTbl();

			//카테고리 신규 추가할 때 maxEpisodeId의 값은 없다.
			//maxEpisodeId+1 일때 nullPointException
			if(maxEpisodeId != null){
				id.setEpisodeId(maxEpisodeId+1);
			}else{
				id.setEpisodeId(1);
			}

			newEpisode.setId(id);
			newEpisode.setEpisodeNm(episodeTbl.getEpisodeNm());
			newEpisode.setRegDt(new Date());
			newEpisode.setUseYn("Y");
			try {
				episodeDao.save(newEpisode);
				
			} catch (PersistenceException e) {
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

			//20131007 에피소드가 생성되면 세그먼트를 무조건 생성해준다.(임시)
			SegmentTbl segment = new SegmentTbl();
			SegmentId segid = new SegmentId();
			segid.setCategoryId(id.getCategoryId());
			segid.setEpisodeId(maxEpisodeId);
			//카테고리 신규 추가할 때 maxEpisodeId의 값은 없다.
			//maxEpisodeId+1 일때 nullPointException

			if(logger.isDebugEnabled()){
				logger.debug("segid.getEpisodeId()    "+segid.getEpisodeId());
			}

			if(segid.getEpisodeId() != null){
				segid.setEpisodeId(maxEpisodeId+1);
				segid.setSegmentId(1);
			}else{
				segid.setEpisodeId(1);
				segid.setSegmentId(1);
			}

			segment.setId(segid);
			segment.setSegmentNm("기본값");

			segmentServices.add(segment);

			return "Y";
		
	}

	/**
	 * 에피소드 정보를 삭제한다. (현재 ibatis로 삭제하고있으며, 추후 hibernate로 전환)
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void delete(EpisodeTbl episode)throws ServiceException {
		String message = "";
		try {
			episodeDao.delete(episode.getId());
			
		} catch (PersistenceException e) {
			// e = e.getCause();
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
	 * 에피소드 정보를 삭제한다. 세그먼트정보를 먼저 삭제후 에피소드 정보를 삭제한다.
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void deleteEpisodeInfo(EpisodeTbl episode)throws ServiceException{
		String message = "";
		try {
			/*2013.10.08 세그먼트를 무조건 삭제한다*/

			SegmentId segid = new SegmentId();
			segid.setCategoryId(episode.getId().getCategoryId());
			segid.setEpisodeId(episode.getId().getEpisodeId());
			/*2013.12.06 현재 세그먼트는 무조건 생성임으로 1이다.		 */
			segid.setSegmentId(1);
			segmentServices.delete(segid);

			episodeDao.delete(episode.getId());
		} catch (PersistenceException e) {
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
	 * 에피소드 정보를 조회한다.
	 * @param search
	 * @return
	 */
	public List<EpisodeTbl> episodeSearchList(Search search)throws ServiceException {
		String message = "";
		try {
			return episodeDao.findAll(EpissodeLikeSpecifications.episodeNmLike(search.getCategoryId(), search.getEpisodeNm()));
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
		
	}


	/**
	 * 카테고리에 소속된 에피소드id중 가장 높은 값을 조회하는 함수.
	 * @param categoryId
	 * @return
	 */
	public Integer MaxCount(Integer categoryId)throws ServiceException{
		String message = "";

		Integer result = 0;

		try {
			/**
			 * 서비스단에서는 트랜잭션 단위의 묶음으로 처리해야함.
			 * DB 핸들링은 Dao단에서 처리하도록 위임을 하는게 맞음.
			 */
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

			CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);

			Root<EpisodeTbl> from = criteriaQuery.from(EpisodeTbl.class);

			Path<Integer> episodeId = from.get("id").get("episodeId");
			criteriaQuery.select(criteriaBuilder.max(episodeId));

			criteriaQuery.where(criteriaBuilder.equal(from.get("id").get("categoryId"),categoryId));

			result = em.createQuery(criteriaQuery).getSingleResult();

			logger.debug("result :"+result);

			return result;

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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
		
	}

	/**
	 * category_id, episode_id를 가지고 에피소드 1건을 조회하는 함수.
	 * @param id
	 * @return
	 */
	public EpisodeTbl getEpisodeObj(EpisodeId id )throws ServiceException{
		String message = "";
		EpisodeTbl episodeTbl = null;

		try {
			episodeTbl = episodeDao.findOne(id);
			return episodeTbl;
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
		
		
	}

	/**
	 * category_id, episode_id를 가지고 에피소드 1건을 조회하는 함수.
	 * @param Id
	 * @return
	 */
	public EpisodeTbl episodeInfo(EpisodeId Id )throws ServiceException{
		String message = "";
		EpisodeTbl info=null;

		try {
			info= episodeDao.findOne(Id);
			return info;
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
		
	}

}

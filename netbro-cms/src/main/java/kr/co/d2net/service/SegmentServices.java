package kr.co.d2net.service;

import java.io.InputStream;
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

import kr.co.d2net.dao.SegmentDao;
import kr.co.d2net.dao.filter.SegmentLikeSpecifications;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.PropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 세그먼트관려 정보를 조회하기위한 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class SegmentServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SegmentDao segmentDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 
	 * @param segments
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void addAll(Set<SegmentTbl> segments) throws ServiceException{
		String message = "";
		try {
			segmentDao.save(segments);
		} catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				throw new ConnectionTimeOutException("000", null, e);
			} else {
				throw new DaoRollbackException("001", null, e);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("002", null, e);
		}
	}

	/**
	 * 
	 * @param segment
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(SegmentTbl segment) throws ServiceException{
		String message = "";
		try {
			segmentDao.save(segment);
		} catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				throw new ConnectionTimeOutException("000", null, e);
			} else {
				throw new DaoRollbackException("001", null, e);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("002", null, e);
		}
	}

	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void delete(SegmentId id) throws ServiceException{
		String message = "";
		try {
			segmentDao.delete(id);
		} catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				throw new ConnectionTimeOutException("000", null, e);
			} else {
				throw new DaoRollbackException("001", null, e);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("002", null, e);
		}

	}

	/**
	 * 세그먼트 정보를 조회한다. 
	 * category_id,episode_id는 필수이며, segment_nm은 공백이여도 조회할수있다.
	 * @param search
	 * @return
	 */
	public List<SegmentTbl> segmentSearchList(Search search) throws ServiceException{
		String message = "";
		try {
			List<SegmentTbl> segmentTbls=segmentDao.findAll(SegmentLikeSpecifications.SegmentNmLike(search.getCategoryId(),search.getEpisodeId(), search.getEpisodeNm()));

			for(SegmentTbl segmentTbl : segmentTbls){

				segmentTbl.setContentsTbls(null);

			}

			return segmentTbls;

		} catch (PersistenceException e) {

			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(throwable instanceof EntityExistsException || throwable instanceof EntityNotFoundException
					|| throwable instanceof NoResultException ) {
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

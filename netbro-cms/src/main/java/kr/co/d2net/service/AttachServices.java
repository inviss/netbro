package kr.co.d2net.service;

import java.io.InputStream;
import java.util.Collections;
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

import kr.co.d2net.dao.AttachDao;
import kr.co.d2net.dao.filter.AttachSpecifications;
import kr.co.d2net.dto.AttachTbl;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 첨부파일 관련정보를 조회하기위한 함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class AttachServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AttachDao attachDao;

	@PersistenceContext
	private EntityManager em;


	public Page<AttachTbl> findAllAttach(Specification<AttachTbl> specification, Pageable pageable) {
		return attachDao.findAll(specification, pageable);
	}

	@Autowired
	private MessageSource messageSource;

	
	@Modifying
	@Transactional
	public void add(AttachTbl attachTbl) throws ServiceException {
		String message ="";
		try{
			attachDao.save(attachTbl);
		}catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000", null, Locale.KOREA);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003", null, Locale.KOREA);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003", null, Locale.KOREA);
			throw new DaoRollbackException("003", message, e);
		}
	}


	@Modifying
	@Transactional
	public void delete(AttachTbl attachTbl) throws ServiceException  {
		String message ="";
		try{
			attachDao.delete(attachTbl);
		}catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000", null, Locale.KOREA);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003", null, Locale.KOREA);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003", null, Locale.KOREA);
			throw new DaoRollbackException("003", message, e);
		}
	}

	/**
	 * 첨부파일 정보를 조회한다.
	 * @param ctId
	 * @return
	 */
	public List<AttachTbl> getAttachObj(Long ctId) throws ServiceException {
		/* paging 처리할때 사용
		final PageRequest pageRequest = new PageRequest(
				  0, 20, new Sort(
				    new Order(Direction.ASC, "ct_id"), 
				    new Order(Direction.DESC, "other")
				  ) 
				);
		Page<AttachTbl> page = attachDao.findAll(AttachSpecifications.formatLike(ctId), pageRequest);
		 */

		// Order 안에 ','로 구분하여 여러개 지정가능
		String message ="";
		try {
			Sort sort = new Sort(new Order(Direction.DESC, "seq"));
			return attachDao.findAll(AttachSpecifications.formatLike(ctId), sort);
		} catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000", null, Locale.KOREA);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(throwable instanceof EntityExistsException || throwable instanceof EntityNotFoundException
					|| throwable instanceof NoResultException ) {
				message = messageSource.getMessage("error.001", null, Locale.KOREA);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004", null, Locale.KOREA);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004", null, Locale.KOREA);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	/**
	 * 첨부파일 정보 단일건을 조회한다.
	 * @param seq
	 * @return AttachTbl
	 */
	public AttachTbl getAttach(Long seq) throws ServiceException {
		String message ="";
		try {
			return attachDao.findOne(seq);
		}catch (PersistenceException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof LockTimeoutException || throwable instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000", null, Locale.KOREA);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(throwable instanceof EntityExistsException || throwable instanceof EntityNotFoundException
					|| throwable instanceof NoResultException ) {
				message = messageSource.getMessage("error.001", null, Locale.KOREA);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004", null, Locale.KOREA);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004", null, Locale.KOREA);
			throw new DaoNonRollbackException("004", message, e);
		}
	}
}

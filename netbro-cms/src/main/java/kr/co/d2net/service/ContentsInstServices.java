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
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.dao.ContentsInstDao;
import kr.co.d2net.dao.filter.ContentInstSpecifications;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
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
 * 컨텐츠 inst 관련정보를 조회하기위한 함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class ContentsInstServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContentsInstDao contentsInstDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	
	

	@Modifying
	@Transactional
	public void addAll(Set<ContentsInstTbl> contentsInstTbls)  throws ServiceException {
		String message ="";
		try{
		contentsInstDao.save(contentsInstTbls);
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

	@Modifying
	@Transactional
	public Long add(ContentsInstTbl contentsInstTbl)  throws ServiceException {
		String message ="";
		try{
		ContentsInstTbl contentsInstTbl2 = contentsInstDao.save(contentsInstTbl);

		return contentsInstTbl2.getCtiId();
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
	 * 고해상도 영상으로 등록되어잇는 정보를 한건 조회한다.
	 * @param ctId
	 * @return ContentsInstTbl
	 */
	public ContentsInstTbl getContentInstObj(Long ctId)  throws ServiceException {
		String message ="";
		try {
			return contentsInstDao.findOne(ContentInstSpecifications.formatLike(ctId, "%10%"));	
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
	 * 스토리보드와 관련된 정보를 조회한다.
	 * @param id
	 * @return
	
	@SuppressWarnings("unchecked")
	public List<ContentsInstTbl> getStroyboardInfo(Long id) {

		Query query = null;

		try {

			query = em.createNamedQuery("contentsinst.getStroryboard");
			query.setParameter("ctId", id);

			return (List<ContentsInstTbl>)query.getResultList();

		} catch (Exception e) {

			logger.error("getStroyboardInfo"+e);
		}

		return Collections.EMPTY_LIST;
	}	
	 */

}

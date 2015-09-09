package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.dao.AuthDao;
import kr.co.d2net.dao.RoleAuthDao;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class RoleAuthServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleAuthDao roleAuthDao;

	@Autowired
	private AuthDao authDao;

	@PersistenceContext
	private EntityManager em;


	@Autowired
	private MessageSource messageSource;
	

	@Modifying
	@Transactional
	public void addAll(Set<RoleAuthTbl> roleauths) throws ServiceException{
		String message = "";
		try {
			roleAuthDao.save(roleauths);
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
	public void add(RoleAuthTbl roleauth) throws ServiceException{

		String message = "";

		try {
			roleAuthDao.save(roleauth);
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
	 * authId를 이용해 RoleAuth정보를 가져온다.
	 * @param id
	 * @return
	 */
	public RoleAuthTbl getRoleAuthObj(RoleAuthId authId) throws ServiceException{
		if(logger.isDebugEnabled()){
			logger.debug("id.getAuthid() : "+authId.getAuthid());
			logger.debug("id.getMenuId() : "+authId.getMenuId());
		}

		String message = "";

		try {
			return roleAuthDao.findOne(authId);
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
	 * authId를 이용해 RoleAuthTbl의 데이터를 delete한다.
	 * @param authId
	 */
	public void deleteRoleAuth(Integer authId) throws ServiceException{

		String message = "";

		try {

			AuthTbl authTbl = authDao.findOne(authId);
			Set<RoleAuthTbl> authTbls = authTbl.getRoleAuth();

			//AuthTbl,RoleAuthTbl의 데이터를 일괄 삭제한다.
			roleAuthDao.deleteInBatch(authTbls);

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
	 * roleAuthTbl에 데이터가 있는지 여부를 파악한다
	 * @param id
	 * @return
	 */
	public long findRoleAuthCount() throws ServiceException{
	

		String message = "";

		try {
			return roleAuthDao.count();
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

}

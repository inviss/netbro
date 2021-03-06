package kr.co.d2net.service;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.UserAuthDao;
import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.AuthTbl_;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;

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
 * 유저권한(USER_AUTH_TBL) 관련 정보를 조회하기위한 class.
 * @author vayne
 *
 */
@Service
@Transactional(readOnly=true)
public class UserAuthServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserAuthDao userAuthDao;
	@Autowired
	private UserDao userDao;

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MessageSource messageSource;


	public Page<UserAuthTbl> findAllUserAuth(Specification<UserAuthTbl> specification, Pageable pageable) {
		return userAuthDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<UserAuthTbl> users) {
		userAuthDao.save(users);
	}

	@Modifying
	@Transactional
	public void add(UserAuthTbl user) throws ServiceException{
		String message = "";
		try {
			userAuthDao.save(user);
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
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<UserAuthTbl> findAll() throws ServiceException{

		String message = "";

		try {
			return userAuthDao.findAll();

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
	 * 사용여부를 체크.
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAuthUseYn(String id) throws ServiceException{

		String message = "";

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

			Search search = new Search();

			Root<UserTbl> root = cq.from(UserTbl.class);
			Root<AuthTbl> root1 = cq.from(AuthTbl.class);

			Path<String> userId = root.get(UserTbl_.userId);
			Path<String> useYn = root1.get(AuthTbl_.useYn);

			Join<UserTbl, UserAuthTbl> userAuth = root.join("userAuth", JoinType.INNER);
			Join<AuthTbl, UserAuthTbl> userAuth1 = root1.join("userAuth", JoinType.INNER);

			cq.multiselect(useYn);
			cq.where(cb.and(ObjectLikeSpecifications.userFilterSearch(cb,cq,userAuth,userAuth1, search),cb.equal(userId, id)));

			return (Object)em.createQuery(cq).getResultList();

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.001",null,null);
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
	 * userId를 이용해서 userAuthTbl의 각 메뉴 권한을 가져온다. 
	 * @param userId
	 * @return

	public List<UserAuthTbl> getUserAuthObj(String userId) {

		try {
			return userAuthDao.findAll(UserSpecifications.authLikeUserId(userId));
		} catch (Exception e) {
			logger.error("getUserAuthObj",e);
			return null;
		}
	}
	 */


	/**
	 * 유저권한 데이터를 delete.
	 * @param userId
	 */
	public void deleteUserAuth(String userId) throws ServiceException{

		String message = "";

		try {
			UserTbl userTbl = userDao.findOne(userId);
			Set<UserAuthTbl> authTbls = userTbl.getUserAuth();

			userAuthDao.deleteInBatch(authTbls);
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
	 * userAuthTbl에 데이터가 있는지 여부를 파악한다
	 * @return
	 * @throws ServiceException
	 */
	public long findUserAuthCount() throws ServiceException{

		String message = "";

		try {
			return userAuthDao.count();

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

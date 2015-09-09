package kr.co.d2net.service;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.AuthDao;
import kr.co.d2net.dao.RoleAuthDao;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.AuthTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 권한(AUTH_TBL) 관련 정보를 조회하기위한 class.
 * @author vayne
 *
 */
@Service
@Transactional(readOnly=true)
public class AuthServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	
	@Autowired
	private AuthDao authDao;

	@Autowired
	private RoleAuthDao roleAuthDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Modifying
	@Transactional
	public void addAll(Set<AuthTbl> auths) throws ServiceException{

		String message = "";

		try {
			authDao.save(auths);
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
	public void add(AuthTbl auth) throws ServiceException{

		String message = "";

		try {
			authDao.save(auth);
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


	public List<AuthTbl> findAll() throws ServiceException{

		String message = "";

		try {
			return authDao.findAll();
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
	 * authId를 AuthTbl정보를 가져온다.
	 * @param id
	 * @return
	 */
	public AuthTbl getAuthObj(Integer authId) throws ServiceException{

		String message = "";

		try {
			
			return authDao.findOne(authId);
		}catch (PersistenceException e) {
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
	 * authTbl에서 MaxId값을 가져온다.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer getAuthMaxId() throws ServiceException{
		
		String message = "";

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery cq = cb.createQuery();

			Root<AuthTbl> root = cq.from(AuthTbl.class);
			Path<Integer> authId = root.get(AuthTbl_.authId);

			cq.select(cb.max(authId));

			return (Integer) em.createQuery(cq).getSingleResult();

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
	 * AuthTbl,RoleAuthTbl 의 데이터를 일괄 insert한다.
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateRoleAuth(Search search) throws ServiceException{
		
		String message = "";

		if(logger.isDebugEnabled()){
			logger.debug("search.getControlGubun() : "+search.getControlGubun());
		}

		try {
			String controlGubun = search.getControlGubun();

			//ex) controlGubuns = R(읽기),RW(쓰기),L(제한)
			String[] controlGubuns = controlGubun.split(",");

			String clipSearch = controlGubuns[0];
			String content = controlGubuns[1];
			String disuse = controlGubuns[2];
			String stat = controlGubuns[3];
			String auth = controlGubuns[4];
			String category = controlGubuns[5];
			String part = controlGubuns[6];
			String user = controlGubuns[7];
			String code = controlGubuns[8];
			String monitor = controlGubuns[9];
			String equip = controlGubuns[10];
			String notice = controlGubuns[11];

			AuthTbl authTbl = getAuthObj(search.getAuthId());

			authTbl.setAuthNm(search.getAuthNm());
			authTbl.setAuthSubNm(search.getAuthSubNm());
			authTbl.setUseYn(search.getUseYn());
			authTbl.setModDt(new Date());

			//menuTbl에 menu id값은 2,3,4,5,6
			//ex)menuid =  2 클립검색, 3	컨텐츠관리, 4	작업관리, 5	통계 6 관리
			for(int i = 2; i <= 13; i++){

				RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
				RoleAuthId roleAuthId = new RoleAuthId();

				switch (i) {
				case 2:
					roleAuthTbl.setControlGubun(clipSearch);	
					break;
				case 3:
					roleAuthTbl.setControlGubun(content);
					break;
				case 4:
					roleAuthTbl.setControlGubun(disuse);
					break;
				case 5:
					roleAuthTbl.setControlGubun(stat);
					break;
				case 6:
					roleAuthTbl.setControlGubun(auth);
					break;
				case 7:
					roleAuthTbl.setControlGubun(category);
					break;
				case 8:
					roleAuthTbl.setControlGubun(part);
					break;
				case 9:
					roleAuthTbl.setControlGubun(user);
					break;
				case 10:
					roleAuthTbl.setControlGubun(code);
					break;
				case 11:
					roleAuthTbl.setControlGubun(monitor);
					break;
				case 12:
					roleAuthTbl.setControlGubun(equip);
					break;
				case 13:
					roleAuthTbl.setControlGubun(notice);
					break;
				}

				roleAuthId.setAuthid(search.getAuthId());
				roleAuthId.setMenuId(i);

				roleAuthTbl.setId(roleAuthId);
				authTbl.addRoleAuth(roleAuthTbl);
				authDao.save(authTbl);
			}
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
	public void saveRoleAuth(Search search) throws ServiceException{
		
		String message = "";

		AuthTbl authTbl = new AuthTbl();

		try {
			if(logger.isDebugEnabled()){
				logger.debug("search.getControlGubun() : "+search.getControlGubun());
			}

			String controlGubun = search.getControlGubun();
			String[] controlGubuns = controlGubun.split(",");

			//ex) controlGubuns = R(읽기),RW(쓰기),L(제한)

			String clipSearch = controlGubuns[0];
			String content = controlGubuns[1];
			String disuse = controlGubuns[2];
			String stat = controlGubuns[3];
			String auth = controlGubuns[4];			
			String category = controlGubuns[5];
			String part = controlGubuns[6];
			String user = controlGubuns[7];
			String code = controlGubuns[8];
			String monitor = controlGubuns[9];
			String equip = controlGubuns[10];
			String notice = controlGubuns[11];

			authTbl.setUseYn(search.getUseYn());
			authTbl.setAuthNm(search.getAuthNm());
			authTbl.setAuthSubNm(search.getAuthSubNm());
			authTbl.setRegDt(new Date());
			authTbl.setUseYn(search.getUseYn());

			add(authTbl);

			//authTbl에서 MaxId값을 가져온다.
			Integer authMaxId = getAuthMaxId();

			if(logger.isDebugEnabled()){
				logger.debug(controlGubun);
				logger.debug("authMaxId : "+authMaxId);
			}

			//menuTbl에 menu id값은 2,3,4,5,6
			//ex)menuid =  2 클립검색, 3	컨텐츠관리, 4	작업관리, 5	통계,6 관리 

			for(int i = 2; i <= 13; i++){

				RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
				RoleAuthId roleAuthId = new RoleAuthId();
				switch (i) {
				case 2:
					roleAuthTbl.setControlGubun(clipSearch);	
					break;
				case 3:
					roleAuthTbl.setControlGubun(content);
					break;
				case 4:
					roleAuthTbl.setControlGubun(disuse);
					break;
				case 5:
					roleAuthTbl.setControlGubun(stat);
					break;
				case 6:
					roleAuthTbl.setControlGubun(auth);
					break;
				case 7:
					roleAuthTbl.setControlGubun(category);
					break;
				case 8:
					roleAuthTbl.setControlGubun(part);
					break;
				case 9:
					roleAuthTbl.setControlGubun(user);
					break;
				case 10:
					roleAuthTbl.setControlGubun(code);
					break;
				case 11:
					roleAuthTbl.setControlGubun(monitor);
					break;
				case 12:
					roleAuthTbl.setControlGubun(equip);
					break;
				case 13:
					roleAuthTbl.setControlGubun(notice);
					break;
				}

				roleAuthId.setAuthid(authMaxId);
				roleAuthId.setMenuId(i);

				roleAuthTbl.setId(roleAuthId);

				roleAuthServices.add(roleAuthTbl);
			}
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
	 * auth테이블이 총 갯수를 얻어온다
	 * @param id
	 * @return
	 */
	public long findAuthCount() throws ServiceException{

		String message = "";

		try {
			
			return authDao.count();
		}catch (PersistenceException e) {
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

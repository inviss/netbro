package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.UserAuthDao;
import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dao.filter.UserSpecifications;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.AuthTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserAuthTbl.UserAuthId;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 유저(USER_TBL) 관련 정보를 조회하기위한 class.
 * @author vayne
 *
 */
@Service
@Transactional(readOnly=true)
public class UserServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserAuthDao userAuthDao;

	@Autowired
	private UserAuthServices userAuthServices;

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MessageSource messageSource;

	
	@Modifying
	@Transactional
	public void addAll(Set<UserTbl> users) {
		userDao.save(users);
	}

	@Modifying
	@Transactional
	public void add(UserTbl user) throws ServiceException{
		String message = "";
		try {
		userDao.save(user);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}catch (JDBCConnectionException e) {
			throw new ConnectionTimeOutException("000", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}

	@Modifying
	@Transactional
	public void remove(UserTbl user){
		userDao.delete(user);
	}

	public List<UserTbl> findAll() throws ServiceException{

		String message = "";

		try {
			Sort sort = new Sort(new Order(Direction.DESC, "regDt"));
			return userDao.findAll(sort);
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
	 * userId 이용해 User정보를 가져온다(Update할때 사용).
	 * @param userId
	 * @return
	 */
	public UserTbl getUserObj(String userId) throws ServiceException{

		String message = "";

		try {
			logger.debug("userId     "+userId);
			return userDao.findOne(userId);
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
	 * userId 이용해 User정보를 가져온다(select할때 사용).
	 * @param Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Users> findUserAndAuthObj(String id) throws ServiceException{

		String message = "";

		try {
			
			
			String[] userInfoFields = {"userNm", "userId", "userPhone","userPass","authNm","authId","useYn"};

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery cq = cb.createQuery();

			Search search = new Search();

			Root<UserTbl> root = cq.from(UserTbl.class);
			Root<AuthTbl> root1 = cq.from(AuthTbl.class);

			Path<String> userId = root.get(UserTbl_.userId);
			Path<String> userNm = root.get(UserTbl_.userNm);
			Path<String> userPasswd = root.get(UserTbl_.userPass);
			Path<String> userPhone = root.get(UserTbl_.userPhone);

			Path<String> authNm = root1.get(AuthTbl_.authNm);
			Path<Integer> authId = root1.get(AuthTbl_.authId);

			Path<String> useYn = root.get(UserTbl_.useYn);

			Join<UserTbl, UserAuthTbl> userAuth = root.join("userAuth", JoinType.INNER);
			Join<AuthTbl, UserAuthTbl> userAuth1 = root1.join("userAuth", JoinType.INNER);

			cq.multiselect(userNm,userId,userPhone,userPasswd,authNm,authId,useYn);
			cq.where(ObjectLikeSpecifications.userFilterSearch(cb,cq,userAuth,userAuth1, search),cb.equal(userId, id));

			List<Object[]> list2 = em.createQuery(cq).getResultList();

			List<Users> users = new ArrayList<Users>();

			for(Object[] list : list2) {
				Users user = new Users();
				for(int j = 0; j<userInfoFields.length; j++){
					ObjectUtils.setProperty(user, userInfoFields[j], list[j]);			
				}
				
				users.add(user);
			}

			return  users;

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
	 * 사용자 관리에서 유저 추가시 userId,phone 값을 이용해 중복체크한다.
	 * @param userId
	 * @param phone
	 * @return
	 */
	public UserTbl getUserInfoYn(String userId, String phone) throws ServiceException{

		String message = "";

		try {
			return userDao.findOne(UserSpecifications.validator(userId, phone));
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
	 * 로그인에서 userId, userPasswd를 이용해 데이터 유무 확인.
	 * @param userId
	 * @param userPasswd
	 * @return
	 */

	public UserTbl getLoginUserInfoYn(String userId, String userPasswd,String useYn) throws ServiceException{

		String message = "";

		try {
			UserTbl userTbl = userDao.findOne(UserSpecifications.loginValidator(userId, userPasswd,useYn));

			if(userTbl != null){
				Set<UserAuthTbl> userAuthTbls = userTbl.getUserAuth();

				List<Integer> authIds = new ArrayList<Integer>();

				for(UserAuthTbl userAuthTbl : userAuthTbls) {
					authIds.add(userAuthTbl.getId().getAuthId());
				}

				userTbl.setUserAuths(authIds.toArray(new Integer[0]));
				return userTbl;
			}else{
				return null;
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
	 * 
	 * @param userId
	 * @param userPasswd
	 * @return
	 */
	public UserTbl getLoginUserInfo(String userId, String userPasswd) throws ServiceException{

		String message = "";

		try {
			UserTbl userTbl = userDao.findOne(UserSpecifications.loginValidator1(userId, userPasswd));

			if(userTbl != null){
				Set<UserAuthTbl> userAuthTbls = userTbl.getUserAuth();

				List<Integer> authIds = new ArrayList<Integer>();

				for(UserAuthTbl userAuthTbl : userAuthTbls) {
					authIds.add(userAuthTbl.getId().getAuthId());
				}

				userTbl.setUserAuths(authIds.toArray(new Integer[0]));
				return userTbl;
			}else{
				return null;
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
	 * user정보를 갱신한다.
	 * userAuthTbl,UserTbl 의 데이터를 일괄 insert한다.
	 * @param search
	 * @param userTbl
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateUserInfo(Search search,UserTbl user) throws ServiceException {

		UserAuthId userAuthId = new UserAuthId();
		UserAuthTbl userAuthTbl = new UserAuthTbl();
		String message = "";

		try {

			UserTbl userTbl = getUserObj(user.getUserId());

			if(logger.isDebugEnabled()){
				logger.debug("userTbl2.getUserPass() : "+userTbl.getUserPass());
				logger.debug("search.getUserPass() : "+search.getUserPasswd());
				logger.debug("search.getAuthId() : "+search.getAuthId());
				logger.debug("search.getUserId() : "+search.getUserId());
			}

			//userTbl2.setXXXX 사용자 정보 변경
			userTbl.setUserNm(search.getUserNm());
			userTbl.setUserPhone(search.getUserPhone());
			userTbl.setUserPass(Utility.encodeMD5(search.getUserPasswd()));
			userTbl.setModDt(new Date());
			userTbl.setUseYn(search.getUseYn());

			userAuthId.setAuthId(search.getAuthId());
			userAuthId.setUserId(search.getUserId());

			userAuthTbl.setId(userAuthId);
			userAuthTbl.setModDt(new Date());

			userTbl.addUserAuth(userAuthTbl);  // 권한정보 변경
			userDao.save(userTbl); // 일괄변경

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}catch (JDBCConnectionException e) {
			throw new ConnectionTimeOutException("000", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}



	/**
	 * user정보를 저장한다.
	 * userAuthTbl,UserTbl 의 데이터를 일괄 insert한다.
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveUserInfo(Search search) throws ServiceException {

		UserTbl userTbl = new UserTbl();
		UserAuthTbl userAuthTbl = new UserAuthTbl();
		UserAuthId userAuthId = new UserAuthId();

		String message = "";

		if(logger.isDebugEnabled()){
			logger.debug("userTbl2.getUserPass() : "+userTbl.getUserPass());
			logger.debug("search.getUserPass() : "+search.getUserPasswd());
			logger.debug("search.getAuthId() : "+search.getAuthId());
			logger.debug("search.getUserId() : "+search.getUserId());
			logger.debug("search.getUserNm() : "+search.getUserNm());
			logger.debug("search.getUserPhone() : "+search.getUserPhone());
			logger.debug("search.getUserPasswd() : "+search.getUserPasswd());
			logger.debug("search.getUserId() : "+search.getUserId());
			logger.debug("search.getAuthId() : "+search.getAuthId());
		}

		try {
			//UserTbl 데이터 넣기
			userTbl.setUserId(search.getUserId());
			userTbl.setUserNm(search.getUserNm());
			userTbl.setRegrId(search.getUserId());
			userTbl.setUserPhone(search.getUserPhone());
			userTbl.setUserPass(Utility.encodeMD5(search.getUserPasswd()));
			userTbl.setRegDt(new Date());
			userTbl.setUseYn(search.getUseYn());

			//UserAuthTbl 데이터 넣기
			userAuthId.setAuthId(search.getAuthId());
			userAuthId.setUserId(search.getUserId());
			userAuthTbl.setId(userAuthId);

			userTbl.addUserAuth(userAuthTbl);
			userDao.save(userTbl);
			//userAuthServices.add(userAuthTbl);
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
	 * user정보를 갱신한다(useYn 데이터를 N으로 변경).
	 * @param search
	 * @param user
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateUserInfoYn(Search search,UserTbl user) throws ServiceException {

		String message = "";

		try {
			UserTbl userTbl = getUserObj(user.getUserId());

			//userTbl2.setXXXX 사용자 정보 변경
			userTbl.setUseYn("N");
			userTbl.setModrId(user.getUserId());
			userTbl.setModDt(new Date());

			userDao.save(userTbl);

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
	public Long getAllUserCount() throws ServiceException{

		String message = "";

		try {
			return userDao.count();
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
	 * 사용자리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Modifying
	@Transactional
	public List<Users> findUserInfo(Search search) throws ServiceException{

		String message = "";

		String[] userFields = {"userNm", "userId", "userPhone","regDt","authNm","authId","useYn"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<UserTbl> root = cq.from(UserTbl.class);
		Root<AuthTbl> root1 = cq.from(AuthTbl.class);

		Path<String> userId = root.get(UserTbl_.userId);
		Path<String> userNm = root.get(UserTbl_.userNm);
		Path<Date> regDt = root.get("regDt");
		Path<String> userPhone = root.get(UserTbl_.userPhone);

		Path<String> authNm = root1.get(AuthTbl_.authNm);
		Path<Integer> authId = root1.get(AuthTbl_.authId);

		Path<String> userYn = root.get(UserTbl_.useYn);

		Join<UserTbl, UserAuthTbl> userAuth = root.join("userAuth", JoinType.INNER);
		Join<AuthTbl, UserAuthTbl> userAuth1 = root1.join("userAuth", JoinType.INNER);

		cq.multiselect(userNm,userId,userPhone,regDt,authNm,authId,userYn);
		//cq.where(ObjectLikeSpecifications.userFilterSearch(cb,cq,userAuth,userAuth1, search));

		try {
			if(StringUtils.isBlank(search.getSearchUserObj())){
				cq.where(cb.and(cb.equal(userAuth, userAuth1)));
			}else{
				//if(search.getUserSelectBox().equals("id")){
				if("id".equals(search.getUserSelectBox())){	
					cq.where(cb.and(cb.equal(userAuth, userAuth1)),cb.like(root.<String>get(UserTbl_.userId), "%"+search.getSearchUserObj()+"%"));
				}else{
					cq.where(cb.and(cb.equal(userAuth, userAuth1)),cb.like(root.<String>get(UserTbl_.userNm), "%"+search.getSearchUserObj()+"%"));
				}

			}


			//cq.where(cb.and(cb.equal(userAuth, userAuth1)),cb.like(root.<String>get("userId"), "%"+search.getSearchUserObj()+"%"));
			cq.orderBy(cb.desc(userId));

			TypedQuery<Object[]> typedQuery = em.createQuery(cq);

			int startPage = (search.getPageNo()-1) * SearchControls.USER_LIST_COUNT;
			int endPage = startPage+SearchControls.USER_LIST_COUNT;

			logger.debug("startPage : " + startPage);
			logger.debug("endPage : " + endPage);


			typedQuery.setFirstResult(startPage);
			typedQuery.setMaxResults(20);

			List<Object[]> userList =  typedQuery.getResultList();

			logger.debug("userList.size() : " + userList.size());


			List<Users> users = new ArrayList<Users>();


			for(Object[] list : userList) {
				Users user = new Users();

				int i = 0;
				for(int j = 0; j<userFields.length; j++){
					ObjectUtils.setProperty(user, userFields[j], list[i]);			
					i++;
				}
				users.add(user);
			}
			return  users;
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
	 * 권한별 버튼 활성화/비활성화 체크
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getUserControlAuth(Map<String, Object> params) throws ServiceException{

		String message = "";

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery cq = cb.createQuery();

			String paramUserId = (String) params.get("userId");
			String paramMenuId = (String) params.get("menuId");

			Root<UserAuthTbl> rootUserAuth = cq.from(UserAuthTbl.class);
			Root<RoleAuthTbl> rootRoleAuth = cq.from(RoleAuthTbl.class);

			Path<String> controlGubun = rootRoleAuth.get("controlGubun");
			Path<String> userId = rootUserAuth.get("id").get("userId");
			Path<String> menuId = rootRoleAuth.get("id").get("menuId");

			Path<Integer> userAuthId = rootUserAuth.get("id").get("authId");
			Path<Integer> roleAuthId = rootRoleAuth.get("id").get("authId");

			cq.multiselect(controlGubun);
			cq.where(cb.and(cb.equal(userId, paramUserId)),cb.equal(menuId, paramMenuId),cb.equal(userAuthId, roleAuthId));

			return em.createQuery(cq).getResultList();
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
	 * 유저테이블에 데이터가 존재하는지 여부 파악
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long findUserCount() throws ServiceException{

		String message = "";

		try {
			
			return userDao.count();
			
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
	 * userId 이용해 User정보를 가져온다(Update할때 사용).
	 * @param userId
	 * @return
	 */
	public UserTbl getUserInfo(String userId) throws ServiceException{

		String message = "";

		return userDao.findOne(userId);
		
	}

	
	
}

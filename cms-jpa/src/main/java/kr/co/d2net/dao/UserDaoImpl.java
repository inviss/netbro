package kr.co.d2net.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.ContentSpecifications;
import kr.co.d2net.dao.filter.UserSpecifications;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}


	@Override
	public void save(UserTbl user) throws DaoRollbackException {
		try {
			if(StringUtils.isNotBlank(user.getUserId())){
				repository.update(user);
			}else{
				repository.save(user);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("", "User save Error", e);
		}

	}



	@Override
	public void remove(UserTbl user) throws DaoRollbackException {
		try {
			repository.remove(user);
		} catch (Exception e) {
			throw new DaoRollbackException("", "User remove Error", e);
		}

	}


	/**
	 * userId 이용해 User정보를 가져온다(select할때 사용).
	 * @param Id
	 * @return
	 */
	@Override
	public List<UserTbl> findUserAndAuthInfos(String userId) throws DaoNonRollbackException {

		List<UserTbl> userTbls = null;

		try {
			SpecificationResult<UserTbl> result = repository.find(UserTbl.class,UserSpecifications.findUserAndAuthInfos(userId));
			userTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findUserAndAuthInfos Error", e);
		}
		return userTbls;
	}
	
	
	/**
	 * 사용자 관리에서 유저 추가시 userId,phone 값을 이용해 중복체크한다.
	 * @param userId
	 * @param phone
	 * @return
	 */
	@Override
	public UserTbl getUserInfoYn(String userId, String userPhone) throws DaoNonRollbackException {

		UserTbl userTbl = null;

		try {
			userTbl = repository.find(UserTbl.class,UserSpecifications.getLoginUserInfo(userId, userPhone)).single();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getUserInfoYn Error", e);
		}
		return userTbl;
	}

	
	/**
	 * 로그인에서 userId, userPasswd를 이용해 데이터 유무 확인.
	 * @param userId
	 * @param userPasswd
	 * @param useYn
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public UserTbl getLoginUserInfoYn(String userId, String userPasswd,String useYn) throws DaoNonRollbackException {

		UserTbl userTbl = null;

		try {
			userTbl = repository.find(UserTbl.class,UserSpecifications.checkExistUserInfo(userId, userPasswd, useYn)).single();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getLoginUserInfoYn Error", e);
		}
		return userTbl;
	}


	/**
	 * login 한 유저정보를 가져오는 method.
	 * @param userId
	 * @param userPasswd
	 * @return
	 */
	@Override
	public UserTbl getLoginUserInfo(String userId, String userPasswd) throws DaoNonRollbackException {

		UserTbl userTbl = null;

		try {
			userTbl = repository.find(UserTbl.class,UserSpecifications.getLoginUserInfo(userId, userPasswd)).single();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getLoginUserInfo Error", e);
		}
		return userTbl;
	}

	
	/**
	 * userId 이용해 User정보를 가져온다(Update할때 사용).
	 * @param userId
	 * @return
	 */
	@Override
	public Long findUserCount() throws DaoNonRollbackException {

		Long userCount = 0L;

		try {
			userCount =	repository.count(UserTbl.class);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findUserCount Error", e);
		}
		return userCount;
	}


	@Override
	public List<String> getUserControlAuth(Map<String, Object> params) throws DaoNonRollbackException {
		
		UserTbl userTbl = null;
		String paramUserId = (String) params.get("userId");
		Integer paramMenuId = (Integer) params.get("menuId");
		
		try {
			userTbl = repository.find(UserTbl.class,UserSpecifications.getUserControlAuth(paramUserId,paramMenuId)).single();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


}

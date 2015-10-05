package kr.co.d2net.dao;

import java.util.List;
import java.util.Map;

import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface UserDao {
	public void save(UserTbl user) throws DaoRollbackException;
	public void remove(UserTbl user) throws DaoRollbackException;
	public UserTbl getUserInfoYn(String userId, String phone) throws DaoNonRollbackException;
	public UserTbl getLoginUserInfoYn(String userId, String userPasswd,String useYn) throws DaoNonRollbackException;
	public UserTbl getLoginUserInfo(String userId, String userPasswd) throws DaoNonRollbackException;
	public List<UserTbl> findUserAndAuthInfos(String userId) throws DaoNonRollbackException;
	public Long findUserCount() throws DaoNonRollbackException;
	public List<String> getUserControlAuth(Map<String, Object> params) throws DaoNonRollbackException;
}

package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface UserAuthDao {
	public void add(UserAuthTbl user) throws DaoRollbackException;
	public List<UserAuthTbl> getAuthUseYn(String id) throws DaoNonRollbackException;
	public long findUserAuthCount() throws DaoNonRollbackException;
}

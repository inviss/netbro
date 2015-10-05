package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;

public interface AuthDao {
	public List<AuthTbl> findAuthList(Search search) throws DaoNonRollbackException;
	public AuthTbl getAuthInfo(Integer authId) throws DaoNonRollbackException;
	public Integer getAuthMaxId() throws DaoNonRollbackException;
	
}

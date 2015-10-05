package kr.co.d2net.dao;

import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface RoleAuthDao {
	public void deleteRoleAuth(Integer authId) throws DaoRollbackException;
	public long findRoleAuthCount() throws DaoNonRollbackException;
	public void save(RoleAuthTbl roleauth) throws DaoRollbackException;
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.exception.DaoRollbackException;

public interface CodeDao {
	public void save(CodeTbl codeTbl) throws DaoRollbackException;
}

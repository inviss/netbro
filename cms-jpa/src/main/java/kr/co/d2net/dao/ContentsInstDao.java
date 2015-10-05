package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface ContentsInstDao {
	public void save(ContentsInstTbl contentsInstTbl) throws DaoRollbackException;
	public void deleteContentInst(ContentsInstTbl contentsInstTbl) throws DaoRollbackException;
	public ContentsInstTbl getContentsInstObj(ContentsInst contentsInst) throws DaoNonRollbackException;  
	public List<ContentsInstTbl> findContentsInst(ContentsInst contentsInst) throws DaoNonRollbackException;
	public Long count(ContentsInst contentsInst) throws DaoNonRollbackException;
}

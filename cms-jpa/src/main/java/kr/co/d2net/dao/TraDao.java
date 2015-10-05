package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface TraDao {
	public List<TraTbl> findTraList(Search search) throws DaoNonRollbackException;
	public void updateTraJobStatus(TraTbl traTbl) throws DaoRollbackException;
	public TraTbl getTraInfo(long traSeq) throws DaoNonRollbackException;
	
}

package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface SegmentDao {
	
	/**
	 * 세그먼트 정보를 insert한다 
	 * @param segmentTbl
	 * @throws DaoRollbackException
	 */
	public void save(SegmentTbl segmentTbl) throws DaoRollbackException;
	
	/**
	 * 세그먼트 정보를 삭제한다
	 * @param segmentTbl
	 * @throws DaoRollbackException
	 */
	public void delete(SegmentTbl segmentTbl)  throws DaoRollbackException;
	
 
	/**
	 * 세그먼트 정보를 리스트로 불러온다.
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<SegmentTbl> findSegmentList(Search search) throws DaoNonRollbackException;
}

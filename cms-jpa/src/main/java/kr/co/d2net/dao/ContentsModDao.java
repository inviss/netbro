package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface ContentsModDao {
	
	/**
	 * 컨텐츠의 수정 이력을 기록한다
	 * @param noticeTbl
	 * @throws DaoRollbackException
	 */
	public void save(ContentsModTbl contentsModTbl) throws DaoRollbackException;
	 
	/**
	 * 컨텐츠수정 이력 리스트를 조회한다.
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<ContentsModTbl> findContentsModList(Search search) throws DaoNonRollbackException;
	 
}

package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface DisuseDao {
	
	/**
	 * 폐기건을 신규로 신청, 수정, 취소의 작업을 하는 함수
	 * disuseClf의 값과, ctId의 존재 유무로 inser, update의 방향을 경정한다.
	 * @param disuseInfoTbl
	 * @throws DaoRollbackException
	 */
	public void save(DisuseInfoTbl disuseInfoTbl) throws DaoRollbackException;
	
	/**
	 * 폐기 신청현황에 대한 리스트를 조회한다
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<DisuseInfoTbl> findDisuseInfoList(Search search) throws DaoNonRollbackException;
	
	/**
	 * 폐기 신청건에 대한 전체 조회 건수를 얻어온다
	 * @param search
	 * @return cont
	 * @throws DaoNonRollbackException
	 */
	public Long count(Search search) throws DaoNonRollbackException;
	
	/**
	 * 
	 * @param ctId
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public DisuseInfoTbl getDisuserInfo(Long ctId) throws DaoNonRollbackException;
}

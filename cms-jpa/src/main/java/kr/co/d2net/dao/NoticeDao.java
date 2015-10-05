package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface NoticeDao {
	
	/**
	 * 공지사항을 신규로 신청, 수정, 취소의 작업을 하는 함수
	 * disuseClf의 값과, ctId의 존재 유무로 inser, update의 방향을 경정한다.
	 * @param noticeTbl
	 * @throws DaoRollbackException
	 */
	public void save(NoticeTbl noticeTbl) throws DaoRollbackException;
	
	/**
	 * 공지사항 삭제 함수
	 * @param noticeTbl
	 * @throws DaoRollbackException
	 */
	public void delete(NoticeTbl noticeTbl) throws DaoRollbackException;
	
	/**
	 * 공지사항리스틀 조회한다
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<NoticeTbl> findNoticeList(Search search) throws DaoNonRollbackException;
	
	/**
	 * 공지사항 총조회 건수를 구한다.
	 * @param search
	 * @return cont
	 * @throws DaoNonRollbackException
	 */
	public Long count(Search search) throws DaoNonRollbackException;
	
	/**
	 * 공지사항에 대한 상세 정보를 구한다
	 * @param noticeId
	 * @return NoticeTbl
	 * @throws DaoNonRollbackException
	 */
	public NoticeTbl getNoticeInfo(Long noticeId) throws DaoNonRollbackException;
}

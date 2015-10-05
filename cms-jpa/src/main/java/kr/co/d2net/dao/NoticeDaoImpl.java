package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.NoticeSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("noticeDao")
public class NoticeDaoImpl implements NoticeDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;
	private final static Logger logger = LoggerFactory.getLogger(NoticeDaoImpl.class);
	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	/**
	 * noticeNo의 존재여부에 따라 update, insert 구분 한다.
	 */
	@Override
	public void save(NoticeTbl noticeTbl) throws DaoRollbackException {

		if(noticeTbl.getNoticeId() != null && noticeTbl.getNoticeId() > 0){
			try{
				repository.save(noticeTbl);
			}catch(Exception e){
				logger.error("notice insert err : "+e);
			}
		}else{
			try{
				repository.update(noticeTbl);
			}catch(Exception e){
				logger.error("notice update err : "+e);
			}
		}

	}

	/**
	 * noticeNo를 db에서 삭제처리한다.
	 */
	@Override
	public void delete(NoticeTbl noticeTbl) throws DaoRollbackException {

		try {
			repository.remove(noticeTbl);
		} catch (Exception e) {
			logger.equals("notice delete err : "+e);
		}
	}

	/**
	 * 등록된 공지사항의 리스트를 조회한다.
	 * user명을 보여주지 않기로 결정함에 따라 userNm 검색은 보여주지 않는다.-2015.03.25 asura
	 */
	@Override
	public List<NoticeTbl> findNoticeList(Search search)
			throws DaoNonRollbackException {

		List<NoticeTbl> noticeTbls = null;

		SpecificationResult<NoticeTbl> result =  repository.find(NoticeTbl.class,NoticeSpecifications.findNotice(search));
		if(search.getPageNo() != null && search.getPageNo() > 0){
			int startNum = (search.getPageNo()-1) * SearchControls.DISCARD_LIST_COUNT;
			int endNum = startNum + SearchControls.DISCARD_LIST_COUNT;
			result.from(startNum).size(endNum);
		}
		noticeTbls = result.list();
		return noticeTbls;
	}

	/**
	 * 등록된 공지사항의 총갯수를 구한다.
	 */
	@Override
	public Long count(Search search) throws DaoNonRollbackException {
		// TODO Auto-generated method stub
		return repository.count(NoticeTbl.class,NoticeSpecifications.findNotice(search));
	}

	/**
	 * 공지사항의 상세 정보를 구한다.
	 */
	@Override
	public NoticeTbl getNoticeInfo(Long noticeId)
			throws DaoNonRollbackException {

		return repository.find(NoticeTbl.class, noticeId);
	}}

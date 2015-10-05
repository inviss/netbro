package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.DisuseDao;
import kr.co.d2net.dao.NoticeDao;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("noticeService")
@Transactional(readOnly=true)
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;
	
	/**
	 * 공지사항을 신규로 저장한다 
	 */
	@Override
	public void insertNotice(NoticeTbl noticeTbl) throws ServiceException {
		noticeDao.save(noticeTbl);
	}

	/**
	 * 공지사항을 수정한다
	 */
	@Override
	public void updateNotice(NoticeTbl noticeTbl) throws ServiceException {
		noticeDao.save(noticeTbl);
	}

	/**
	 * 등록된 공지사항을 삭제한다.
	 */
	@Override
	public void deleteNotice(NoticeTbl noticeTbl) throws ServiceException {
		noticeDao.delete(noticeTbl);
	}

	/**
	 * 공지사항 단일건에 대해서 조회를 한다.
	 */
	@Override
	public Notice getNotice(Search search) throws ServiceException {
		 
		NoticeTbl noticeTbl = noticeDao.getNoticeInfo(search.getNoticeId());
		Notice notice = new Notice();
		
		notice.setNoticeId(noticeTbl.getNoticeId());
		return notice;
	}

	/**
	 * dto로 얻은 정보를 vo로 옮겨서 화면에 담는다.
	 */
	@Override
	public List<Notice> findNoticeList(Search search) throws ServiceException {
		List<NoticeTbl> noticeTbls = noticeDao.findNoticeList(search);
		
		
		List<Notice> notices = new ArrayList<Notice>();
		
		for(NoticeTbl noticeTbl : noticeTbls){
			Notice notice = new Notice();
			
			notice.setNoticeId(noticeTbl.getNoticeId());
			
			notices.add(notice);
		}
		return notices;
	}

	@Override
	public Long countNotice(Search search) throws ServiceException {
		return noticeDao.count(search);
	}}

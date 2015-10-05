package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface NoticeService {
 
	public void insertNotice(NoticeTbl noticeTbl) throws ServiceException;
	
	public void updateNotice(NoticeTbl noticeTbl) throws ServiceException;
	
	public void deleteNotice(NoticeTbl noticeTbl) throws ServiceException;
	
	public Notice getNotice(Search search) throws ServiceException;
	
	public List<Notice> findNoticeList(Search search) throws ServiceException;
	
	public Long countNotice(Search search) throws ServiceException;
	
}

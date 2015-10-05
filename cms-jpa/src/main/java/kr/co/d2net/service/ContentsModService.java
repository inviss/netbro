package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface ContentsModService {
 
	public void insertContentsModInfo(ContentsModTbl contentsModTbl) throws ServiceException;
	 
	public List<Content> findContentsModList(Search search) throws ServiceException;
	
	public Long countContentsMod(Search search) throws ServiceException;
	
}

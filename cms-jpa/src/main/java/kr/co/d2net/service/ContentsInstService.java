package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.exception.ServiceException;

public interface ContentsInstService {
 
	public void saveContentsInst(ContentsInstTbl contentsInstTbl) throws ServiceException;
	
	public void deleteContentsInst(ContentsInstTbl contentsInstTbl) throws ServiceException;
	
	public ContentsInstTbl getContentsInst(ContentsInst contentsInst) throws ServiceException;
	
	public List<ContentsInstTbl> findContentsInst(ContentsInst contentsInst) throws ServiceException;
	
	public Long countContentsInst(ContentsInst contentsInst) throws ServiceException;
	
}

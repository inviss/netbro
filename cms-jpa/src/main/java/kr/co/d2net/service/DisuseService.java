package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface DisuseService {
 
	public void saveDisuse(DisuseInfoTbl disuseInfoTbl) throws ServiceException;
	
	public void deleteDisuse(DisuseInfoTbl disuseInfoTbl) throws ServiceException;
	
	public Disuse getDisuse(Search search) throws ServiceException;
	
	public List<Disuse> findDisuse(Search search) throws ServiceException;
	
	public Long countDisuse(Search search) throws ServiceException;
	
}

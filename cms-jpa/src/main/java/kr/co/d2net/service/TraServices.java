package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Tra;
import kr.co.d2net.exception.ServiceException;

public interface TraServices {
	public List<Tra> findTraList(Search search) throws ServiceException;
	public void updateTraJobStatus(TraTbl traTbl) throws ServiceException;
}

package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface ProBusiServices {
	public void saveProBusiInfo(ProBusiTbl proBusiTbl) throws ServiceException;
	public List<ProBusiTbl> findProBusiInfo(Long busiPartnerId) throws ServiceException;
	public void deleteBusiInfo(ProBusiTbl proBusiTbl,ProFlTbl proFlTbl,Search search) throws ServiceException;
}

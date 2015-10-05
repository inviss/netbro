package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface ProBusiDao {
	public void save(ProBusiTbl proBusiTbl) throws ServiceException;
	public List<ProBusiTbl> findProBusiList(Long id, Search search) throws ServiceException;
}

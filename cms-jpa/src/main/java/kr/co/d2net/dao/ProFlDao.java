package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface ProFlDao {
	public List<ProFlTbl> findProfileList(Search search) throws ServiceException;
	public ProFlTbl getProfileInfo(Long profileId) throws ServiceException;
	public void save(ProFlTbl proFlTbl) throws ServiceException;
}

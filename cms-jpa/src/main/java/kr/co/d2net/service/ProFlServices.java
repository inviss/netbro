package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.exception.ServiceException;

public interface ProFlServices {
	public List<ProFlTbl> findProfileList() throws ServiceException;
	public ProFlTbl getProfileInfo(Long profileId) throws ServiceException;
	
}

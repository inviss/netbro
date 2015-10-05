package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ProFlDao;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("proFlService")
@Transactional(readOnly=true)
public class ProFlServiceImpl implements ProFlServices {

	@Autowired
	private ProFlDao proFlDao;

	@Override
	public List<ProFlTbl> findProfileList() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProFlTbl getProfileInfo(Long profileId) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}



}

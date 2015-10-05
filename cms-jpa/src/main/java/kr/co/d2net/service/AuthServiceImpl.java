package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.AuthDao;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authService")
@Transactional(readOnly=true)
public class AuthServiceImpl implements AuthServices {

	@Autowired
	private AuthDao authDao;

	@Override
	public List<AuthTbl> findAuthList() throws ServiceException {
		List<AuthTbl> authTbl = null;

		return authTbl;
	
	}

}

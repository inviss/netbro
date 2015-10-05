package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.exception.ServiceException;

public interface AuthServices {
	public List<AuthTbl> findAuthList() throws ServiceException;
	
}

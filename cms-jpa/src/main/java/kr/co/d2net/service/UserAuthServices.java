package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.exception.ServiceException;

public interface UserAuthServices {
	public List<Object[]> getAuthUseYn(String id) throws ServiceException;
	public List<UserAuthTbl> getUserAuthObj(String userId);
	public long findUserAuthCount() throws ServiceException;
	public void add(UserAuthTbl user) throws ServiceException;
}

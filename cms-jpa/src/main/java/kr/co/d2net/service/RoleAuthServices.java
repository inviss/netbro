package kr.co.d2net.service;

import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.exception.ServiceException;

public interface RoleAuthServices {
	public void deleteRoleAuth(Integer authId) throws ServiceException;
	public long findRoleAuthCount() throws ServiceException;
	public void saveRoleAuthInfo(RoleAuthTbl roleauth) throws ServiceException;
}

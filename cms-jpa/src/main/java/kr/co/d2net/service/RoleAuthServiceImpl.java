package kr.co.d2net.service;

import kr.co.d2net.dao.RoleAuthDao;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("roleAuthService")
@Transactional(readOnly=true)
public class RoleAuthServiceImpl implements RoleAuthServices{

	@Autowired
	private RoleAuthDao roleAuthDao;
	
	
	@Override
	public void deleteRoleAuth(Integer authId) throws ServiceException {
		try {
			roleAuthDao.deleteRoleAuth(authId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public long findRoleAuthCount() throws ServiceException {
		try {
			roleAuthDao.findRoleAuthCount();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;
	}

	@Override
	@Transactional
	public void saveRoleAuthInfo(RoleAuthTbl roleauth) throws ServiceException {
		try {
			roleAuthDao.save(roleauth);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}

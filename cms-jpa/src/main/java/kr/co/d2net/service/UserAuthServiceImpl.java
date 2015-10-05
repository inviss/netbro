package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.UserAuthDao;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userAuthService")
@Transactional(readOnly=true)
public class UserAuthServiceImpl  implements UserAuthServices{
	
	@Autowired
	private UserAuthDao userAuthDao;

	@Override
	public List<Object[]> getAuthUseYn(String id) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<UserAuthTbl> getUserAuthObj(String userId) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public long findUserAuthCount() throws ServiceException {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public void add(UserAuthTbl user) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}

package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional(readOnly=true)
public class UserServiceImpl implements UserServices {
	
	
	@Autowired
	private UserDao userDao;
	

	@Override
	public List<Users> getUserAndAuthObj(String userId) throws ServiceException {
		try {

		} catch (Exception e) {
			//userDao.
		}
		return null;
	}

	@Override
	public UserTbl getUserObj(String userId) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public UserTbl getUserInfoYn(String userId, String phone)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public UserTbl getLoginUserInfoYn(String userId, String userPasswd,
			String useYn) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public UserTbl getLoginUserInfo(String userId, String userPasswd)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void updateUserInfo(Search search, UserTbl user)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void saveUserInfo(Search search, UserTbl user)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void updateUserInfoYn(Search search, UserTbl user)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}



}

package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ServiceException;

public interface UserServices {
	public List<Users> getUserAndAuthObj(String id) throws ServiceException;
	public UserTbl getUserObj(String userId) throws ServiceException;
	public UserTbl getUserInfoYn(String userId, String phone) throws ServiceException;
	public UserTbl getLoginUserInfoYn(String userId, String userPasswd,String useYn) throws ServiceException;
	public UserTbl getLoginUserInfo(String userId, String userPasswd) throws ServiceException;
	public void updateUserInfo(Search search,UserTbl user) throws ServiceException;
	public void saveUserInfo(Search search, UserTbl user) throws ServiceException;
	public void updateUserInfoYn(Search search,UserTbl user) throws ServiceException;
}

package kr.co.d2net.dao.test;

import kr.co.d2net.dao.UserAuthDao;
import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserAuthTbl.UserAuthId;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.service.UserAuthServices;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAuthTest extends BaseDaoConfig {
	
	@Autowired
	private UserAuthServices userAuthServices;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAuthDao userAuthDao;

	@Test
	@Ignore
	public void addAll() {
		try {
			UserAuthTbl userAuth = new UserAuthTbl();
			UserAuthId id = new UserAuthId();
		
			
			
			id.setAuthId(1);
			id.setUserId("s522522");
			
			userAuth.setId(id);
			
			
			
			userAuthServices.add(userAuth);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delete() {
		try {
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}

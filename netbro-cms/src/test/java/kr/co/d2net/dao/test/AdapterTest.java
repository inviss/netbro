package kr.co.d2net.dao.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.service.UserServices;


public class AdapterTest extends BaseDaoConfig {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;
	
	
	@Autowired
	private UserServices userServices;

	@Test
	public void test() {
		try {

			userDao.findOne("a2");
			userDao.count();
			

		} catch (Exception e) {

		}
	}
}

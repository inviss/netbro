package kr.co.d2net.dao.test;

import kr.co.d2net.dao.MenuDao;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MenuDaoTest extends BaseDaoConfig {
	
	@Autowired
	private MenuDao menuDao;
	
	
	
	@Ignore
	@Test
	public void findMenuTest() {
		try {
			menuDao.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void save(){
		
		
		
	}
}

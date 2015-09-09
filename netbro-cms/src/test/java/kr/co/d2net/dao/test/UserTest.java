package kr.co.d2net.dao.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.service.UserServices;

public class UserTest extends BaseDaoConfig {

	@Autowired
	private UserServices userServices;
	
	@Autowired
	private UserDao userDao;
	
	@Ignore
	@Test
	public void addAll() {
		try {
			Set<UserTbl> users = new HashSet<UserTbl>();

			for(int i = 0; i < 30; i++){
			UserTbl userTbl = new UserTbl();
			Date date = new Date();

			userTbl.setUserId("cms" +i);
			userTbl.setModDt(date);
			userTbl.setRegDt(date);
			userTbl.setModrId("cmsmod");
			userTbl.setRegrId("regid");
			userTbl.setUseYn("Y");
			userTbl.setUserPass("123456");
			userTbl.setUserNm("김뛰뛰");

			userServices.add(userTbl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Ignore
	@Test
	public void addAll1() {
		try {

			String filePath = "20130902061653";

			String folder1 = filePath.substring(0,4);
			String folder2 = filePath.substring(4,6);
			String folder3 = filePath.substring(6,8);
			
			System.out.println("folder1 : " + folder1);
			System.out.println("folder2 : " + folder2);
			System.out.println("folder3 : " + folder3);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delete() {
		try {
			Set<UserTbl> users = new HashSet<UserTbl>();

UserServices userServices = new UserServices();

userServices.getUserInfo("admin");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}

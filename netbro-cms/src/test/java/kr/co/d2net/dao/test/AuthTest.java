package kr.co.d2net.dao.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.service.AuthServices;


public class AuthTest extends BaseDaoConfig {

	@Autowired
	private AuthServices authServices;

	@Test
	public void addAll() {
		try {
			Set<AuthTbl> auths = new HashSet<AuthTbl>();

			for (int i = 0; i < 20; i++) {


				AuthTbl authTbl = new AuthTbl();
				Date date = new Date();
				authTbl.setAuthNm("이뛰뛰");
				authTbl.setModDt(date);
				authTbl.setModrId("김뛰뛰");
				authTbl.setRegDt(date);
				authTbl.setUseYn("Y");

				authServices.add(authTbl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

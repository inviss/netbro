package kr.co.d2net.dao.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.AttachTbl;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;


public class AttachTest extends BaseDaoConfig {
	
	@Autowired
	private AttachServices attachServices;

	@Test
	public void addAll() {
		try {
			Set<AttachTbl> auths = new HashSet<AttachTbl>();
			
			AttachTbl attachTbl = new AttachTbl();
			attachTbl.setCtId(1L);
			attachServices.add(attachTbl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

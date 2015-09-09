package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.service.BusiPartnerServices;


public class BusiPartnerTest extends BaseDaoConfig {
	
	@Autowired
	private BusiPartnerServices busiServices;

	@Test
	public void addAll() {
		try {
			Set<BusiPartnerTbl> busis = new HashSet<BusiPartnerTbl>();
			
			BusiPartnerTbl busi = new BusiPartnerTbl();
			busi.setBusiPartnerId("001");
			//users.add(userTbl);
			
//			userTbl = new UserTbl();
//			userTbl.setUserId("N");
//			users.add(userTbl);
			
			//userServices.addAll(users);
			busiServices.add(busi);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

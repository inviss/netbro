package kr.co.d2net.dao.test;


import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;

import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.RoleAuthServices;

public class RoleAuthTest extends BaseDaoConfig {
	
	@Autowired
	private RoleAuthServices roleAuthServices;
	
	@Autowired
	private AuthServices authServices;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Ignore
	@Test
	public void addAll() {
		try {
		
			Set<RoleAuthTbl> roleauths = new HashSet<RoleAuthTbl>();
			
			RoleAuthTbl roleAuth = new RoleAuthTbl();
			
			RoleAuthId id = new RoleAuthId();
	
		
			id.setMenuId(1);
			id.setAuthid(1);
			
			roleAuth.setId(id);
			roleAuth.setControlGubun("01");
		
			roleauths.add(roleAuth);
			

			roleAuthServices.addAll(roleauths);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}

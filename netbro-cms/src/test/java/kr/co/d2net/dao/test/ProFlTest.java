package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.service.ProFlServices;

public class ProFlTest extends BaseDaoConfig {
	
	@Autowired
	private ProFlServices proFlServices;

	@Test
	public void addAll() {
		try {
			Set<ProFlTbl> pros = new HashSet<ProFlTbl>();
			
			ProFlTbl pro = new ProFlTbl();
			pro.setProFlid("001");
			pros.add(pro);
			

			proFlServices.add(pro);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

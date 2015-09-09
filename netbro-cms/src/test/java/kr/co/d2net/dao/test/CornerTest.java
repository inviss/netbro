package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.CornerTbl;


import kr.co.d2net.service.CornerServices;

public class CornerTest extends BaseDaoConfig {
	
	@Autowired
	private CornerServices cornerServices;

	@Test
	public void addAll() {
		try {
			Set<CornerTbl> corners = new HashSet<CornerTbl>();
			
			CornerTbl corner = new CornerTbl();
			ContentsTbl co = new ContentsTbl();
			co.setCtId(1L);
			corner.setCnId(1L);

			//cornerServices.add(corner);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

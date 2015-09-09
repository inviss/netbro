package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProBusiTbl.ProBusiId;
import kr.co.d2net.service.ProBusiServices;

public class ProBusiTest extends BaseDaoConfig {
	
	@Autowired
	private ProBusiServices proBusiServices;

	@Test
	public void addAll() {
		try {
			Set<ProBusiTbl> categories = new HashSet<ProBusiTbl>();
			
			ProBusiTbl proBusi = new ProBusiTbl();
			ProBusiId id = new ProBusiId();
			id.setBusiPartnerId("001");
			id.setProFlId("001");
			proBusi.setId(id);
			
			categories.add(proBusi);
			
			proBusiServices.add(proBusi);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

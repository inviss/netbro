package kr.co.d2net.dao.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.service.DisuseServices;

public class DisuseTest extends BaseDaoConfig {
	
	@Autowired
	private DisuseServices disuseServices;
	

	@Test
	public void addAll() {
		try {
			Set<DisuseInfoTbl> disuses = new HashSet<DisuseInfoTbl>();
			Date date = new Date();
			DisuseInfoTbl disuseTbl = new DisuseInfoTbl();
			//disuseTbl.setDisuseNo("001");
			disuseTbl.setRegrId("cms");
			disuseTbl.setCancelDt(date);
			disuseTbl.setRegDt(date);
			disuseTbl.setDisuseClf("001");
			disuses.add(disuseTbl);
			
//			categoryTbl = new CategoryTbl();
//			categoryTbl.setCategoryId((long) 2);
//			categories.add(categoryTbl);
			
			//categoryServices.addAll(categories);
			disuseServices.add(disuseTbl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

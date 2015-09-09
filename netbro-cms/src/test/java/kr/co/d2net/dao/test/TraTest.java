package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.service.TraServices;

public class TraTest extends BaseDaoConfig {
	
	@Autowired
	private TraServices traServices;

	@Test
	public void addAll() {
		try {
			Set<TraTbl> tras = new HashSet<TraTbl>();
			
			TraTbl tra = new TraTbl();
			EquipmentTbl equip = new EquipmentTbl();
			equip.setDeviceId("TC01");
			tra.setSeq((long) 2);
			//tra.setDevice(equip);
			tra.setJobStatus("007");
			tra.setErrorCount(0);
			tras.add(tra);
//			
//			categoryTbl = new CategoryTbl();
//			categoryTbl.setCategoryId((long) 2);
//			categories.add(categoryTbl);
			
			//categoryServices.addAll(categories);
			traServices.addAll(tras);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

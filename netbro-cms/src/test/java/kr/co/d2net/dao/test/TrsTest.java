package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.service.TrsServices;

public class TrsTest extends BaseDaoConfig {
	
	@Autowired
	private TrsServices trsServices;

	@Test
	public void addAll() {
		try {
			Set<TrsTbl> trss = new HashSet<TrsTbl>();
			
			TrsTbl trs = new TrsTbl();
			EquipmentTbl equip = new EquipmentTbl();
			
			equip.setDeviceId("TC01");
			trs.setSeq((long) 1);
			//trs.setDevice(equip);
			trss.add(trs);

			trsServices.addAll(trss);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

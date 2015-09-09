package kr.co.d2net.dao.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.service.EquipmentServices;

public class EquipmentTest extends BaseDaoConfig {

	@Autowired
	private EquipmentServices equipmentServices;

	@Test
	public void addAll() {
		try {
			Set<EquipmentTbl> categories = new HashSet<EquipmentTbl>();

			EquipmentTbl equip = new EquipmentTbl();
			Date date = new Date();
			
			equip.setDeviceId("TC01");
			equip.setCtiId(111L);
			//equip.setDeviceclfCd("001");
			equip.setDeviceIp("127.0.0.1"); 

			equip.setModDt(date);
			equip.setUseYn("Y");
			equip.setWorkStatcd("001");

			categories.add(equip);


			equipmentServices.add(equip);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

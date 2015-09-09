package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.BusiPartnerCategory;
import kr.co.d2net.dto.BusiPartnerCategory.BusiPartnerCategoryId;

import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.service.BusiPartnerCategoryServices;


public class BusiPartnerCategoryTest extends BaseDaoConfig {
	
	@Autowired
	private BusiPartnerCategoryServices busiServices;

	@Test
	public void addAll() {
		try {
			Set<BusiPartnerCategory> busiPartners = new HashSet<BusiPartnerCategory>();
			BusiPartnerCategory busiPartner = new BusiPartnerCategory();
			BusiPartnerTbl busi = new BusiPartnerTbl();
			BusiPartnerCategoryId id = new BusiPartnerCategoryId();
			
			
			id.setCategoryId(1);
			id.setCtTyp("001");
			busiPartner.setId(id);
			busi.setBusiPartnerId("001");
			busiPartner.setBusiPartnerId(busi);
			busiServices.add(busiPartner);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

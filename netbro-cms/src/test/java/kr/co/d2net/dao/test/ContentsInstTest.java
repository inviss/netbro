package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.service.ContentsInstServices;

public class ContentsInstTest extends BaseDaoConfig {
	
	@Autowired
	private ContentsInstServices contentsInstServices;

	@Test
	public void addAll() {
		try {
			Set<ContentsInstTbl> contentsInsts = new HashSet<ContentsInstTbl>();
			
			ContentsInstTbl contentsInst = new ContentsInstTbl();
			
			contentsInst.setCtId(1L);
			contentsInst.setCtiFmt("101");
			contentsInst.setFlSz(1111111111L);
			contentsInst.setOrgFileNm("123123123123");
			contentsInst.setUseYn("Y");
		
				
			contentsInsts.add(contentsInst);

			contentsInstServices.add(contentsInst);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

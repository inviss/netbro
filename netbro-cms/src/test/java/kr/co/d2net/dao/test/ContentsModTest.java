package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.service.ContentsModServices;

public class ContentsModTest extends BaseDaoConfig {
	
	@Autowired
	private ContentsModServices contentsModServices;

	@Test
	public void addAll() {
		try {
			Set<ContentsModTbl> mods = new HashSet<ContentsModTbl>();
			
			ContentsModTbl mod = new ContentsModTbl();
			ContentsTbl co = new ContentsTbl();
			co.setCtId(4995L);
			mod.setContentsTbl(co);
			mod.setSeq(1L);
		
			mods.add(mod);
			
			contentsModServices.add(mod);
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

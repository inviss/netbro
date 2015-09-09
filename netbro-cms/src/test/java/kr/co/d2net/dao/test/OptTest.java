package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.Opt;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.service.OptServices;

public class OptTest extends BaseDaoConfig {
	
	@Autowired
	private OptServices optServices;

	@Test
	public void addAll() {
		try {
			Set<Opt> ops = new HashSet<Opt>();
			
			Opt opt = new Opt();
			ProFlTbl pro = new ProFlTbl();
			pro.setProFlid("001");
			
			opt.setOptId(1L);
			opt.setProFlTbl(pro);
			
			optServices.add(opt);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

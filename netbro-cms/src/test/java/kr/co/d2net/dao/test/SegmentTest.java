package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;

import kr.co.d2net.service.SegmentServices;

public class SegmentTest extends BaseDaoConfig {
	
	@Autowired
	private SegmentServices segmentServices;

	@Test
	public void addAll() {
		try {
			Set<SegmentTbl> segments = new HashSet<SegmentTbl>();
			
			SegmentTbl segment = new SegmentTbl();
			SegmentId id = new SegmentId();
			id.setCategoryId(1);
			id.setSegmentId(1);
			id.setEpisodeId(1);
			
			segment.setId(id);
			segmentServices.add(segment);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

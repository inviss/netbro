package kr.co.d2net.dao;

import kr.co.d2net.dto.SegmentTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SegmentDao extends JpaRepository<SegmentTbl, SegmentTbl.SegmentId>, JpaSpecificationExecutor<SegmentTbl> {
	
}

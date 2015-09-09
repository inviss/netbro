package kr.co.d2net.dao;

import kr.co.d2net.dto.ProBusiTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProBusiDao extends JpaRepository<ProBusiTbl, Long>, JpaSpecificationExecutor<ProBusiTbl> {
	
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.TraTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TraDao extends JpaRepository<TraTbl, Long>, JpaSpecificationExecutor<TraTbl> {
	
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.CornerTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CornerDao extends JpaRepository<CornerTbl, Long>, JpaSpecificationExecutor<CornerTbl> {
	
}

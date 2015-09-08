package kr.co.d2net.dao;

import kr.co.d2net.dto.DisuseInfoTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DisuseDao extends JpaRepository<DisuseInfoTbl, Long>, JpaSpecificationExecutor<DisuseInfoTbl> {
	
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.ContentsInstTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContentsInstDao extends JpaRepository<ContentsInstTbl, Long>, JpaSpecificationExecutor<ContentsInstTbl> {
	
}

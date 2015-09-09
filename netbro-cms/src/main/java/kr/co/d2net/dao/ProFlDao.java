package kr.co.d2net.dao;

import kr.co.d2net.dto.ProFlTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProFlDao extends JpaRepository<ProFlTbl, Long>, JpaSpecificationExecutor<ProFlTbl> {
	
}

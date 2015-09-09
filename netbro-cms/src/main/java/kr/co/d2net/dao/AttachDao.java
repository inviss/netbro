package kr.co.d2net.dao;

import kr.co.d2net.dto.AttachTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttachDao extends JpaRepository<AttachTbl, Long>, JpaSpecificationExecutor<AttachTbl> {
	
}

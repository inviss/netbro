package kr.co.d2net.dao;

import kr.co.d2net.dto.BusiPartnerTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BusiPartnerDao extends JpaRepository<BusiPartnerTbl, Long>, JpaSpecificationExecutor<BusiPartnerTbl> {
	
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.BusiPartnerCategoryTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BusiPartnerCategoryDao extends JpaRepository<BusiPartnerCategoryTbl, Long>, JpaSpecificationExecutor<BusiPartnerCategoryTbl> {
	
}

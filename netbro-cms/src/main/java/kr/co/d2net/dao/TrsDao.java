package kr.co.d2net.dao;

import kr.co.d2net.dto.TrsTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrsDao extends JpaRepository<TrsTbl, Long>, JpaSpecificationExecutor<TrsTbl> {
	
}

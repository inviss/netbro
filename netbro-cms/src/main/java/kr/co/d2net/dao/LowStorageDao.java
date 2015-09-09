package kr.co.d2net.dao;

import kr.co.d2net.dto.LowStorageTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LowStorageDao extends JpaRepository<LowStorageTbl, Integer>, JpaSpecificationExecutor<LowStorageTbl> {
	
}

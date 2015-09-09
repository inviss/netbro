package kr.co.d2net.dao;

import kr.co.d2net.dto.HighStorageTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HighStorageDao extends JpaRepository<HighStorageTbl, Integer>, JpaSpecificationExecutor<HighStorageTbl> {
	
}

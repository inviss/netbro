package kr.co.d2net.dao;

import kr.co.d2net.dto.StorageTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StorageDao extends JpaRepository<StorageTbl, Integer>, JpaSpecificationExecutor<StorageTbl> {
	
}

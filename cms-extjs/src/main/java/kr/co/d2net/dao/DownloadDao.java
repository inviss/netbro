package kr.co.d2net.dao;

import kr.co.d2net.dto.DownloadTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DownloadDao extends JpaRepository<DownloadTbl, Long>, JpaSpecificationExecutor<DownloadTbl> {
	
}

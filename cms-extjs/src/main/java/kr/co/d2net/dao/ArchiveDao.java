package kr.co.d2net.dao;

import kr.co.d2net.dto.ArchiveTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArchiveDao extends JpaRepository<ArchiveTbl, Long>, JpaSpecificationExecutor<ArchiveTbl> {
	
}

package kr.co.d2net.dao;

import kr.co.d2net.dto.ContentsModTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContentsModDao extends JpaRepository<ContentsModTbl, Long>, JpaSpecificationExecutor<ContentsModTbl> {
	
}

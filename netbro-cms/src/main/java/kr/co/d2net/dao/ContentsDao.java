package kr.co.d2net.dao;

import kr.co.d2net.dto.ContentsTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ContentsDao extends JpaRepository<ContentsTbl, Long>, JpaSpecificationExecutor<ContentsTbl> {
	
}
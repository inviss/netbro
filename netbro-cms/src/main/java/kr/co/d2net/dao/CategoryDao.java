package kr.co.d2net.dao;

import kr.co.d2net.dto.CategoryTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryDao extends JpaRepository<CategoryTbl, Integer>, JpaSpecificationExecutor<CategoryTbl> {
	
}

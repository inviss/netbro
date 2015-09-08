package kr.co.d2net.dao;

import java.util.List;

import javax.persistence.QueryHint;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CodeDao extends JpaRepository<CodeTbl, CodeTbl.CodeId>, 
								 JpaSpecificationExecutor<CodeTbl>,
								 PagingAndSortingRepository<CodeTbl, CodeTbl.CodeId>{

	//@Cacheable(value = "code", key = "#id")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	CodeTbl findOne(CodeId id);
	
	//@Cacheable(value = "code")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	CodeTbl findOne(Specification<CodeTbl> specification);
	
	//@Cacheable(value = "code")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CodeTbl> findAll(Specification<CodeTbl> specification);
	
	//@Cacheable(value = "code")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Page<CodeTbl> findAll(Pageable pageable);
	
	//@Cacheable(value = "code")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CodeTbl> findAll(Specification<CodeTbl> specification, Sort sort);
	
	//@Cacheable(value = "code")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Page<CodeTbl> findAll(Specification<CodeTbl> specification, Pageable pageable);
			
}

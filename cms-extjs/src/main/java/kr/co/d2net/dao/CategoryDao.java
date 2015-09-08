package kr.co.d2net.dao;

import java.util.List;

import javax.persistence.QueryHint;

import kr.co.d2net.dto.CategoryTbl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

public interface CategoryDao extends JpaRepository<CategoryTbl, Integer>, JpaSpecificationExecutor<CategoryTbl> {
	
	//@Cacheable(value = "category", key="#categoryId")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	CategoryTbl findOne(Integer categoryId);
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public CategoryTbl findOne(Specification<CategoryTbl> spec);
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CategoryTbl> findAll();
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CategoryTbl> findAll(Sort sort);
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CategoryTbl> findAll(Specification<CategoryTbl> specification);
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	List<CategoryTbl> findAll(Specification<CategoryTbl> specification, Sort sort);
	
	//@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Page<CategoryTbl> findAll(Specification<CategoryTbl> specification, Pageable pageable);
}

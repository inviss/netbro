package kr.co.d2net.dao;

import javax.persistence.QueryHint;

import kr.co.d2net.dto.MenuTbl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

public interface MenuDao extends JpaRepository<MenuTbl, Integer>, JpaSpecificationExecutor<MenuTbl> {
	
	@Cacheable(value = "category", key="#id")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public MenuTbl findOne(Integer id);
	
	@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<MenuTbl> findAll(Specification<MenuTbl> spec, Pageable pageable);
	
}

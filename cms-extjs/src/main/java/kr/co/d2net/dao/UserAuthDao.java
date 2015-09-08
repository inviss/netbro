package kr.co.d2net.dao;

import java.util.List;

import javax.persistence.QueryHint;

import kr.co.d2net.dto.UserAuthTbl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

public interface UserAuthDao extends JpaRepository<UserAuthTbl, UserAuthTbl.UserAuthId>, JpaSpecificationExecutor<UserAuthTbl> {

	@Cacheable(value = "category", key="#categoryId")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<UserAuthTbl> findAll();
	
	@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<UserAuthTbl> findAll(Specification<UserAuthTbl> spec, Pageable pageable);
}

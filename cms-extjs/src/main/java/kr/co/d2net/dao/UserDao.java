package kr.co.d2net.dao;

import java.util.List;

import javax.persistence.QueryHint;

import kr.co.d2net.dto.UserTbl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

public interface UserDao extends JpaRepository<UserTbl, String>, JpaSpecificationExecutor<UserTbl> {

	@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<UserTbl> findAll();
	
	@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<UserTbl> findAll(Sort sort);
	
	@Cacheable(value = "category", key="#id")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public UserTbl findOne(String id);
	
	@Cacheable(value = "category")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public UserTbl findOne(Specification<UserTbl> spec);
	
}

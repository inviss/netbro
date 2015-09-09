package kr.co.d2net.dao;

import kr.co.d2net.dto.AuthTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthDao extends JpaRepository<AuthTbl, Integer>, JpaSpecificationExecutor<AuthTbl> {
	
}

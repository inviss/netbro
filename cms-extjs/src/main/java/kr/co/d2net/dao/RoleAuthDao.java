package kr.co.d2net.dao;

import kr.co.d2net.dto.RoleAuthTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleAuthDao extends JpaRepository<RoleAuthTbl, RoleAuthTbl.RoleAuthId>, JpaSpecificationExecutor<RoleAuthTbl> {
	
}

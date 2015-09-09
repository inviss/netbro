package kr.co.d2net.dao;

import kr.co.d2net.dto.UserAuthTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAuthDao extends JpaRepository<UserAuthTbl, UserAuthTbl.UserAuthId>, JpaSpecificationExecutor<UserAuthTbl> {


	
}

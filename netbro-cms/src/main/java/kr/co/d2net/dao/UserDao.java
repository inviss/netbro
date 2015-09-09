package kr.co.d2net.dao;

import kr.co.d2net.dto.UserTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<UserTbl, String>, JpaSpecificationExecutor<UserTbl> {


	
}

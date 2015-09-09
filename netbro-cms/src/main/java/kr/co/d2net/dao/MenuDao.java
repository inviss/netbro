package kr.co.d2net.dao;

import kr.co.d2net.dto.MenuTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuDao extends JpaRepository<MenuTbl, Long>, JpaSpecificationExecutor<MenuTbl> {

}

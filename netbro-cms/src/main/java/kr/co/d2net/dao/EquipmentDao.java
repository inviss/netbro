package kr.co.d2net.dao;

import kr.co.d2net.dto.EquipmentTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentDao extends JpaRepository<EquipmentTbl, String>, JpaSpecificationExecutor<EquipmentTbl> {
	
}

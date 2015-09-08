package kr.co.d2net.dao;

import kr.co.d2net.dto.Opt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OptDao extends JpaRepository<Opt, Long>, JpaSpecificationExecutor<Opt> {
	
}

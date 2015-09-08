package kr.co.d2net.dao;

import kr.co.d2net.dto.IdGenTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IdGenDao extends JpaRepository<IdGenTbl, String>, 
								 JpaSpecificationExecutor<IdGenTbl>,
								 PagingAndSortingRepository<IdGenTbl, String>{

}

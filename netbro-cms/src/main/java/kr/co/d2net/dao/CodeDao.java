package kr.co.d2net.dao;

import kr.co.d2net.dto.CodeTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CodeDao extends JpaRepository<CodeTbl, CodeTbl.CodeId>, 
								 JpaSpecificationExecutor<CodeTbl>,
								 PagingAndSortingRepository<CodeTbl, CodeTbl.CodeId>{

}

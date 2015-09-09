package kr.co.d2net.dao;

import kr.co.d2net.dto.NoticeTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeDao extends JpaRepository<NoticeTbl, Long>, JpaSpecificationExecutor<NoticeTbl> {

}

package kr.co.d2net.dao;

import kr.co.d2net.dto.EpisodeTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EpisodeDao extends JpaRepository<EpisodeTbl, EpisodeTbl.EpisodeId>, JpaSpecificationExecutor<EpisodeTbl> {
	
}

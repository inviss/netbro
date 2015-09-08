package kr.co.d2net.dao;

import kr.co.d2net.dto.StatisticsTbl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatisticsDao extends JpaRepository<StatisticsTbl, Integer>, 
								 JpaSpecificationExecutor<StatisticsTbl>,
								 PagingAndSortingRepository<StatisticsTbl, Integer>{

}

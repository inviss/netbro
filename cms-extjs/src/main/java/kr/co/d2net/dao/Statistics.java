package kr.co.d2net.dao;


import java.util.List;

import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.exception.DaoNonRollbackException;

public interface Statistics {
	
	public List<Statistic> findStatisticForPeriod(Statistic statistic,Search search) throws DaoNonRollbackException;
	
	public List<Statistic> findStatisticForYear(Statistic statistic) throws DaoNonRollbackException;
	
	public List<StatisticsTbl> findTotalStatistic(Statistic statistic) throws DaoNonRollbackException;
	
	public List<Statistic> findYearList() throws DaoNonRollbackException;
	
	public List<Statistic> findStatisticForYearForGraph(Statistic statistic) throws DaoNonRollbackException;
	
	public void initSeq() throws DaoNonRollbackException;
	
}

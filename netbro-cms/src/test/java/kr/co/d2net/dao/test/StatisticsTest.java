package kr.co.d2net.dao.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.StatisticsServices;

public class StatisticsTest extends BaseDaoConfig {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private StatisticsServices statisticsService;
	
	
	@Ignore
	@Test
	public void groupList() {
		try {
			Statistic  info = new Statistic();
			
			info.setYearList("2013");
			List<Statistic> infos  =  statisticsService.findStatisticsListForYear(info);
			
			System.out.println("############"+infos.size());
			for(Statistic statistics : infos){
				System.out.println("############"+statistics.getCategoryId()+",  "+statistics.getCategoryNm()+",  "+statistics.getGroupId()+",  "+statistics.getDepth()+",  "+statistics.getRegist());
			   
			    
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Ignore
	@Test
	public void StatisticDetailInfo() {
		try {
			Statistic info = new Statistic();
			Search search = new Search();
			Date sdate = new Date();
			Date edate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String searchStartDate = "2014-04-01";
			String searchEndDate = "2013-04-30";
			Calendar calendar = Calendar.getInstance();	
			sdate = format.parse(searchStartDate);
			edate = format.parse(searchEndDate);
			calendar.setTime(edate);
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			searchEndDate=format.format(calendar.getTime());
			edate = format.parse(searchEndDate);
			info.setStartDD(sdate);
			info.setEndDD(edate);
			info.setCategoryId(5);
			info.setGubun("004");
			search.setPageNo(1);
			List<Statistic> infos  =  statisticsService.findStatisticDetailInfo(info, search);
			
			System.out.println("############"+infos.size());
			for(Statistic statisticsTbl : infos){
				System.out.println("############"+statisticsTbl.getRegDd()+",  "+statisticsTbl.getCtNm()+",  "+statisticsTbl.getEpisodeNm());
			   
			    
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	//@Ignore
	@Test
	public void findTotalSUM() {

		try {

			Statistic statistic = new Statistic();
			Search search = new Search();

		
			List<StatisticsTbl> infos;

			statisticsService.deleteTotalStatistcs();
			//statisticsService.fineNotUseCategoryList();
				infos  =  statisticsService.findTotalStatistics(statistic);
/*
				for(StatisticsTbl statisticsTbl : infos ){

					statisticsService.InsertTotalStatistc(statisticsTbl);


				infos = null;

			}*/

			statisticsService.initSeq();

		} catch (Exception e) {

			logger.error("Ingest WatcherThread Error - "+e.getMessage());

		}

	}
	
	
	
	@Ignore
	@Test
	public void findTotalPeriod() {

		try {

			Statistic statistic = new Statistic();
			Search search = new Search();

			Statistic info = new Statistic();
		
			Date sdate = new Date();
			Date edate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String searchStartDate = "2014-04-01";
			String searchEndDate = "2013-04-30";
			Calendar calendar = Calendar.getInstance();	
			sdate = format.parse(searchStartDate);
			edate = format.parse(searchEndDate);
			calendar.setTime(edate);
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			searchEndDate=format.format(calendar.getTime());
			edate = format.parse(searchEndDate);
			info.setStartDD(sdate);
			info.setEndDD(edate);
			
			List<Statistic> infos;

			//statisticsService.deleteTotalStatistcs();
			//statisticsService.fineNotUseCategoryList();
				infos  =  statisticsService.findStatisticsListForPeriod(info, search);
/*
				for(StatisticsTbl statisticsTbl : infos ){

					statisticsService.InsertTotalStatistc(statisticsTbl);


				infos = null;

			}*/

			statisticsService.initSeq();

		} catch (Exception e) {

			logger.error("Ingest WatcherThread Error - "+e.getMessage());

		}

	}
}

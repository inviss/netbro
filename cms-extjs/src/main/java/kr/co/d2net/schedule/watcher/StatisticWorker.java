package kr.co.d2net.schedule.watcher;

import java.util.List;

import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.StatisticsServices;
import kr.co.d2net.service.TraServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("statisticWorker")
public class StatisticWorker implements Worker {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ContentsServices contentsServices;
	@Autowired
	private ContentsInstServices contentsInstServices;
	@Autowired
	private TraServices traServices;
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private StatisticsServices statisticsService ;



	public void work() {

		try {

			Statistic statistic = new Statistic();
			List<StatisticsTbl> infos;
			//스케쥴러가 동작하기전에 기존에 존재했던 데이터를 모두 삭제처리한다. 
			statisticsService.deleteTotalStatistcs();

			//등록된 카테고리의 정보를 조회하고, 각 카테고리ID로 저장된 컨탠츠의 갯수를 구한다.
			infos  =  statisticsService.findTotalStatistics(statistic);

			//카테고리ID 기준으로 수집된 데이터들을 TABLE에 넣는다.
			for(StatisticsTbl statisticsTbl : infos ){
				statisticsService.InsertTotalStatistc(statisticsTbl);
				infos = null;
			}

			//통계테이블의 KEY 값을 초기화한다. 다음 스케쥴러가 돌때에도 1부터 시작할수있도록
			statisticsService.initSeq();

		} catch (Exception e) {
			logger.error("Ingest WatcherThread Error - "+e.getMessage());
		}

	}

}

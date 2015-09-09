package kr.co.d2net.schedule.watcher;

import java.util.List;

import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.dto.xml.Workflow;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.StatisticsServices;
import kr.co.d2net.service.TraServices;
import kr.co.d2net.service.XmlConvertorService;
import kr.co.d2net.service.XmlConvertorServiceImpl;

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


	private final XmlConvertorService<Workflow> convertorService = new XmlConvertorServiceImpl<Workflow>();

	public void work() {

		try {

			Statistic statistic = new Statistic();
			Search search = new Search();


			List<StatisticsTbl> infos;

			statisticsService.deleteTotalStatistcs();

			infos  =  statisticsService.findTotalStatistics(statistic);

			for(StatisticsTbl statisticsTbl : infos ){

				statisticsService.InsertTotalStatistc(statisticsTbl);


				infos = null;

			}

			statisticsService.initSeq();

		} catch (Exception e) {

			logger.error("Ingest WatcherThread Error - "+e.getMessage());

		}

	}

}

package kr.co.d2net.schedule.delection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.DisuseServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("betchDeleteWorker")
public class BatchDeleteWorker implements Worker {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ContentsServices contentsServices;
	@Autowired
	private ContentsInstServices contentsInstServices;
	@Autowired
	private DisuseServices disuseServices;
	
	public void work() {
		try {
			Search search = new Search();
			search.setFromDate(new Date());
			search.setToDate(new Date());
			Calendar cal = Calendar.getInstance();
		
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				search.setToDate(cal.getTime());

			
			List<ContentsTbl> contentsTbls = contentsServices.findAll(search);
			if(logger.isDebugEnabled()) {
				logger.debug("date   "+search.getToDate());
				logger.debug("delete count: "+contentsTbls.size());
			}
			for(ContentsTbl contentsTbl : contentsTbls) {
				contentsServices.deleteContent(contentsTbl);
				disuseServices.completeDiscard(contentsTbl.getCtId());
			}
		} catch (Exception e) {
			logger.error("Ingest WatcherThread Error - "+e.getMessage());
		}

	}

}

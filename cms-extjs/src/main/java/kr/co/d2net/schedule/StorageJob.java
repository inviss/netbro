package kr.co.d2net.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 스케줄러 job 스케줄러
 * @author Administrator
 *
 */
public class StorageJob extends QuartzJobBean implements StatefulJob {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	private Worker worker;
	
	private ApplicationContext ctx;
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.ctx = applicationContext;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
	throws JobExecutionException {
		worker = (Worker)ctx.getBean("storageWorker");
		worker.work();
		
	}
}

package kr.co.d2net.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class StatisticJob extends QuartzJobBean implements StatefulJob {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	private Worker worker;
	
	private ApplicationContext ctx;
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		
		this.ctx = applicationContext;
	
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
	throws JobExecutionException {

		worker = (Worker)ctx.getBean("statisticWorker");
		worker.work();
		
	}
	
}

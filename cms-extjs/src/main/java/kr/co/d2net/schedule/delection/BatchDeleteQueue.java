package kr.co.d2net.schedule.delection;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.utils.SwappingFifoQueue;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class BatchDeleteQueue {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	private static volatile SwappingFifoQueue<ContentsInstTbl> delJobs = new SwappingFifoQueue<ContentsInstTbl>();
	private ExecutorService delExec = Executors.newSingleThreadExecutor();
	
	public void start() throws Exception {
		delExec.execute(new ContentsDelExecute());
	}
	
	public static void putJob(ContentsInstTbl contentsInstTbl) {
		delJobs.put(contentsInstTbl);
	}
	
	public class ContentsDelExecute implements Runnable {

		@Override
		public void run() {
			String high = messageSource.getMessage("high.drive", null, null);
			String row = messageSource.getMessage("row.drive", null, null);
			while(true) {
				if(!delJobs.isEmpty()) {
					ContentsInstTbl contentsInstTbl = delJobs.get();
					if(logger.isDebugEnabled()) {
						logger.debug("cti_fmt: "+contentsInstTbl.getCtiFmt());
						logger.debug("fl_path: "+contentsInstTbl.getFlPath());
						logger.debug("fl_name: "+contentsInstTbl.getWrkFileNm());
					}
						File f = null;
						if(contentsInstTbl.getCtiFmt().startsWith("1")) {
							// 고화질 영상 삭제
							f = new File(high+contentsInstTbl.getFlPath(), contentsInstTbl.getWrkFileNm()+"."+contentsInstTbl.getFlExt());
						} else {
							// 저화질 영상 삭제
							f = new File(row+contentsInstTbl.getFlPath(), contentsInstTbl.getWrkFileNm()+"."+contentsInstTbl.getFlExt());
						}
						
						if(f.exists()) {
							try {
								FileUtils.forceDelete(f);
							} catch (IOException e) {
								logger.error("delete file not exists - "+f.getAbsolutePath());
							}
						}
					
				} else {
					try {
						Thread.sleep(1000L);
					} catch (Exception e) {}
				}
			}
		}
		
	}
	
	public void stop() throws Exception {
		try {
			Thread.sleep(1000L);
			if(!delExec.isShutdown()) {
				delExec.shutdownNow();
			}
			if(logger.isInfoEnabled()) {
				logger.info("Del Execute Thread shutdown !!");
			}
		} catch (Exception e) {
			logger.error("BatchDeleteQueue stop error", e);
		}
	}
}

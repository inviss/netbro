package kr.co.d2net.schedule.watcher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.TrsServices;
import kr.co.d2net.utils.SwappingFifoQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Administrator
 *
 */
@Component("transferWorker")
public class TransferJobExcutor {

	@Autowired
	private MessageSource messageSource;

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrsServices trsServices;

	@Autowired
	private EquipmentServices equipmentServices;

	private static volatile SwappingFifoQueue<Transfer> tfJobs = new SwappingFifoQueue<Transfer>();
	private ExecutorService jobReq = Executors.newSingleThreadExecutor();


	public static Integer getTfSize() {
		return tfJobs.size();
	}

	public static boolean hasJob() {
		return !tfJobs.isEmpty();
	}

	public static Transfer getJob() {
		return tfJobs.get();
	}

	public static void putJob(Transfer transfer) {
		tfJobs.put(transfer);
	}


	@PostConstruct
	public void start() {
//		jobReq.execute(new TransferJobSwap());
		if(logger.isInfoEnabled()) {
			logger.info("ScheduleTransferControlExecutor Thread start !!");
		}
	}
	
	/**
	 * 트렌스퍼 job을 조회하는 thread.
	 * @author Administrator
	 *
	 */
	public class TransferJobSwap implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					int tfSize = TransferJobExcutor.getTfSize();

					if(logger.isInfoEnabled()) {
						logger.info("TransferControlWorker Low1 Job - "+tfSize);
					}

					//List<EquipmentTbl> equipmentTbls = equipmentServices.findAll();
					//int equipCount= equipmentTbls.size() * 2;

					if(true) {

						List<Transfer> trsTbls = null;

						if(tfSize == 0) {
							/*
							 * 예약전송이 적용되는 프로파일 영상 조회(TRS_TBL에서 workStatcd가 '001' 조회.)
							 * 전송하다 서버가 죽었을 경우 workStatcd 001 부터 조회해서 다시 queue에 등록해야함
							 */
							trsTbls = trsServices.findTransferJob("001",tfSize);
							if(logger.isInfoEnabled()) {
								logger.info("statcd 001 : "+trsTbls.size());
							}

							if(trsTbls == null || trsTbls.size() <= 0){
								trsTbls = trsServices.findTransferJob("000",tfSize);
								if(logger.isInfoEnabled()) {
									logger.info("statcd 000 : "+trsTbls.size());
								}
							}
						} else {
							trsTbls = trsServices.findTransferJob("000",tfSize);
							if(logger.isInfoEnabled()) {
								logger.info("statcd 000 : "+trsTbls.size());
							}
						}

						for(int i = 0; i < trsTbls.size(); i++){

							Transfer trsTbl = trsServices.getTrsObj(trsTbls.get(i).getSeq());

							if(logger.isInfoEnabled()) {
								logger.info("work_statcd: " + trsTbl.getWorkStatcd());
							}
							if(trsTbl != null) {
								trsServices.updateTransferHisState(trsTbl);
								if(logger.isInfoEnabled()) {
									logger.info("TransferControlWorker put job : seq["+trsTbl.getSeq()+"], cti_id["+
											trsTbl.getCtiId()+"], statcd["+trsTbl.getWorkStatcd()+"]");
								}
								TransferJobExcutor.putJob(trsTbl);
							}

						}

					}
				} catch (Exception e) {
					logger.error("transferWorker Error - " + e);
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception e) {}
			}
		}

	}

	@PreDestroy
	public void stop() {
		if(!jobReq.isShutdown()) {
			jobReq.shutdownNow();
			if(logger.isInfoEnabled()) {
				logger.info("ScheduleTransferControlExecutor shutdown now!!");
			}
		}

	}

}

package kr.co.d2net.schedule.watcher;

import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.StorageServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("storageWorker")
public class StorageWorker implements Worker {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private StorageServices storageServices;
	
	/**
	 * 스토리지 관련 용량정보를 가저요는 thread.
	 * 고용량, 저용량을 구분해서 각각의 table에 저장.
	 */
	public void work() {
		
		try {
			//sigar lib를 이용해서 스토리지용량을 가져온다.
			StorageTbl highStorage = storageServices.getStorageObj("H");
			StorageTbl lowStorage = storageServices.getStorageObj("L");
			
			storageServices.add(highStorage);
			storageServices.add(lowStorage);

		} catch (Exception e) {
			logger.error("StorageWorker Error - " + e.getMessage());
		}

	}

}

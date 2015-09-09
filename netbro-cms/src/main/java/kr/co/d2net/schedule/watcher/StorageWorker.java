package kr.co.d2net.schedule.watcher;

import kr.co.d2net.dto.HighStorageTbl;
import kr.co.d2net.dto.LowStorageTbl;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.HighStorageServices;
import kr.co.d2net.service.LowStorageServices;
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
	private HighStorageServices highStorageServices;

	@Autowired
	private LowStorageServices lowStorageServices;

	public void work() {

		try {
			HighStorageTbl highStorageTbl = highStorageServices.getStorageObj();
			LowStorageTbl lowStorageTbl = lowStorageServices.getStorageObj();

			highStorageServices.add(highStorageTbl);
			lowStorageServices.add(lowStorageTbl);

		} catch (Exception e) {
			logger.error("StorageWorker Error - "+e.getMessage());
		}

	}

}

package kr.co.d2net.schedule.watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.TrsServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

//@Component("transferWorker")
public class TransferWorker implements Worker {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TrsServices trsServices;

	@Autowired
	private EquipmentServices equipmentServices;

	private Map<String, Object> params = new HashMap<String, Object>();



	@Override
	public void work() {

		/*
		try {
			int tfSize = TransferJobExcutor.getTfSize();

			if(logger.isInfoEnabled()) {
				logger.info("TransferControlWorker Low1 Job - "+tfSize);
			}

			List<EquipmentTbl> equipmentTbls = equipmentServices.findAll();
			int equipCount= equipmentTbls.size() * 2;

			if(true) {
				params.clear();
				List<Transfer> trsTbls = null;

				if(tfSize == 0) {
					trsTbls = trsServices.findTransferJob("001", null);
					if(logger.isInfoEnabled()) {
						logger.info("001===>"+trsTbls.size());
					}
				} else {
					trsTbls = trsServices.findTransferJob("000", null);
					if(logger.isInfoEnabled()) {
						logger.info("000===>"+trsTbls.size());
					}
				}


				for(int i = 0; i < trsTbls.size(); i++){

					List<Transfer> trsTbl = trsServices.getTrsObj(trsTbls.get(i).getSeq());

					for(int z = 0; z< trsTbl.size(); z++){

						Transfer transfers = new Transfer();

						transfers.setCtId(trsTbl.get(z).getCtId());
						transfers.setPriority(trsTbl.get(z).getPriority());
						transfers.setRetryCnt(trsTbl.get(z).getRetryCnt());
						transfers.setSeq(trsTbl.get(z).getSeq());
						transfers.setWorkStatcd(trsTbl.get(z).getWorkStatcd());
						transfers.setCtNm(trsTbl.get(z).getCtNm());
						transfers.setCtiId(trsTbl.get(z).getCtiId());
						transfers.setFlPath(trsTbl.get(z).getFlPath());
						transfers.setOrgFileNm(trsTbl.get(z).getOrgFileNm());
						transfers.setWrkFileNm(trsTbl.get(z).getWrkFileNm());
						transfers.setFlExt(trsTbl.get(z).getFlExt());
						transfers.setCompany(trsTbl.get(z).getCompany());
						transfers.setFtpId(trsTbl.get(z).getFtpId());
						transfers.setIp(trsTbl.get(z).getIp());
						transfers.setPort(trsTbl.get(z).getPort());
						transfers.setPassword(trsTbl.get(z).getPassword());
						transfers.setRemoteDir(trsTbl.get(z).getRemoteDir());

						if(logger.isInfoEnabled()) {
							logger.info("work_statcd: "+transfers.getWorkStatcd());
							//						logger.info("work_fileNm: "+transferHisTbl.getWrkFileNm());
							//						logger.info("pgm_nm: "+transferHisTbl.getPgmNm());
						}
						if(trsTbl != null) {
							params.clear();

							trsServices.updateTransferHisState(transfers);

							if(logger.isInfoEnabled()) {
								logger.info("TransferControlWorker put job : seq["+transfers.getSeq()+"], cti_id["+
										transfers.getCtiId()+"], statcd["+transfers.getWorkStatcd()+"]");
							}
							TransferJobExcutor.putJob(transfers);
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("transferWorker Error - "+e.getMessage());
		}
		 */
	}

}

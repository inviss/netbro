package kr.co.d2net.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.json.Status;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.schedule.watcher.TransferJobExcutor;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.TrsServices;
import kr.co.d2net.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 전송 관련된 외부 interface 와 통신하는 class.
 * @author Administrator
 *
 */
@Path("/transfer")
@Service("rfTransferService")
public class RFTransferServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrsServices trsServices;

	@Autowired
	private EquipmentServices equipmentServices;

	
	/**
	 * 외부 interface와의 통신을 통해 트랜스퍼 작업 정보를 DB에 저장하는 method.
	 * @param status
	 * @throws ServiceException
	 */
	@POST
	@Path("/job/status")
	@Consumes({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public void saveStatus(Status status) throws ServiceException {

		// 진행 클라이언트 ID
		// seq, progress, workstatcd

		if(logger.isInfoEnabled()){
			logger.info("status.getSeq() : " + status.getSeq());
			logger.info("status.getDeviceState() : " + status.getDeviceState());
			logger.info("status.getDeviceId() : " + status.getDeviceId());
		}

		Long tmpCtiId = 0L;

		EquipmentTbl equipmentTbl = equipmentServices.findOne(status.getDeviceId(),status.getDeviceNum());

		if(status.getSeq() != null) {
			TrsTbl trsTbl = trsServices.getTrsInfo(status.getSeq());

			//equiptbl의 ctiId저장값 임시 저장.
			tmpCtiId = trsTbl.getCtiId();
			
			if(status.getProgress() != null && status.getProgress() > 0) {
				trsTbl.setPrgrs(status.getProgress());
				switch(status.getProgress()){
				case 1:
					trsTbl.setTrsStrDt(Utility.getTimestamp());
					break;
				case 100:
					trsTbl.setTrsEndDt(Utility.getTimestamp());
					break;
				}
			}
			if(StringUtils.isNotBlank(status.getErrorCd())) {
				trsTbl.setErrorCd(Utility.padLeft(status.getErrorCd(), "0", 3));
			}
			trsTbl.setWorkStatcd(status.getWorkStatcd());
			
			trsServices.add(trsTbl);
		}
		//devicdState의값은 W(대기),B(진행중)
		if(status.getDeviceState().equals("W")){
			equipmentTbl.setWorkStatcd("W");
			equipmentTbl.setCtiId(null);
		}else{
			if(StringUtils.isNotBlank(status.getErrorCd())) {
				equipmentTbl.setWorkStatcd("W");
				equipmentTbl.setCtiId(null);
			} else {
				equipmentTbl.setWorkStatcd("B");
				equipmentTbl.setCtiId(tmpCtiId);
			}
		}
		equipmentServices.add(equipmentTbl);
	}



	/**
	 * 트랜스퍼job에 있는 데이터를 외부 interface에 return 하는 method.
	 * @param eqId
	 * @return
	 * @throws ServiceException
	 */
	@GET
	@Path("/job/receive/{eqId}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public Transfer jobLoad(@PathParam("eqId") String eqId) throws ServiceException {
		//작업상태 000(대기), 001(큐), 002(전달), 003(진행), 004(완료), 005(전송오류), 006(기타오류)
		//오류형태 000(연결오류), 001(접속오류), 002(원격조회오류), 003(원격쓰기오류), 004(파일없음), 005(스토리지 미연결),
		// 006(전송 후 용량 오류), 008(전송취소), 009(기타오류)
		//해당 seq의 상태를 002로 변경

		Transfer transfer = null;
		if(TransferJobExcutor.hasJob()) {
			transfer = TransferJobExcutor.getJob();
		} else {
			transfer = new Transfer();
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("transfer.getSeq : " + transfer.getSeq());
			logger.debug("transfer.getCtId : " + transfer.getCtId());
			logger.debug("transfer.getCategoryNm : " + transfer.getCategoryNm());
		}

		if(transfer.getSeq() != null){
			TrsTbl trsTbl = trsServices.getTrsInfo(transfer.getSeq());

			trsTbl.setWorkStatcd("002");
			trsServices.add(trsTbl);
		}
		return transfer;
	}

}

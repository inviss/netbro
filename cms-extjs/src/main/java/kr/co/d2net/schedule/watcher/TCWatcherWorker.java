package kr.co.d2net.schedule.watcher;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.json.Transcode;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.ClipSearchService;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.TraServices;
import kr.co.d2net.utils.ExtFileFilter;
import kr.co.d2net.utils.JSON;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("tcWatcherWorker")
public class TCWatcherWorker implements Worker {

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
	private ClipSearchService clipSearchService;
	@Autowired
	private CodeServices codeServices;

	public void work() {
		//String threadName = Thread.currentThread().getName();
		/*if(logger.isDebugEnabled()) {
			logger.debug("Scheduling Start - TCWatcherWorker - thread[" + threadName + "] has began working.");
		}*/

		try {
			String highDrive = messageSource.getMessage("high.drive", null, Locale.KOREA);
			String rowDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String folder = messageSource.getMessage("tc.xml.out", null, Locale.KOREA);

			File outDir = new File(highDrive+folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt"));

			if(files != null && files.length > 0) {
				for(File f : files) {
					ContentsInstTbl contentsInstTbl = new ContentsInstTbl();

					if(logger.isDebugEnabled()) {
						logger.debug("file name : "+f.getAbsolutePath());
					}
					String[] tmp = f.getName().split("\\."); // 123.txt.start
					String fileId = tmp[0]; // ct_id
					int length = tmp.length;

					String state = null;
					switch(length) {
					case 2:
						state = tmp[1];
						break;
					case 3:
						state = tmp[2];
						break;
					}

					if(logger.isDebugEnabled()) {
						logger.debug("fileId : "+fileId);
						logger.debug("state : "+state);
					}

					try {
						/* {"seq":402,"ct_id":416,"source_path":"\\201405\\21","source_fl_nm":"8리_외국계항공사피해보상하늘의별따_1_20140521172719","source_fl_ext":"mxf","target_fl_nm":"20140521172719","target_fl_ext":"mp4","rp_img_kfrm_seq":51} */
						String msg = FileUtils.readFileToString(f);
						if(logger.isDebugEnabled()) {
							logger.debug("tc xml: "+msg);
						}
						if(StringUtils.isNotBlank(msg)) {
							Transcode transcode = JSON.toObject(msg, Transcode.class);

							String status = "";
							// 트랜스코더 상태 변경
							TraTbl traTbl = traServices.getTraObj(transcode.getSeq());
							if(StringUtils.isNotBlank(transcode.getDeviceId())) {
								traTbl.setDeviceId("TC0"+transcode.getDeviceId());
							}
							if(state.equals("success")) {
								Long ctId = Long.valueOf(fileId);
								contentsInstTbl.setCtId(ctId);
								contentsInstTbl.setFlPath(transcode.getSourceFlPath().replaceAll("\\\\", "/"));
								contentsInstTbl.setOrgFileNm(transcode.getTargetFlNm());
								contentsInstTbl.setWrkFileNm(transcode.getTargetFlNm());
								contentsInstTbl.setFlExt(transcode.getTargetFlExt());

								File mp4 = new File(rowDrive+contentsInstTbl.getFlPath()+File.separator+contentsInstTbl.getWrkFileNm()+"."+contentsInstTbl.getFlExt());
								if(mp4.exists())
									contentsInstTbl.setFlSz(mp4.length());
								else throw new Exception("File Not Exist!");

								CodeTbl info = codeServices.getClfInfo("CFMT", transcode.getTargetFlExt());
								if(info != null) {
									CodeId id = info.getId();

									contentsInstTbl.setCtiFmt(id.getSclCd());
								} else {
									contentsInstTbl.setCtiFmt("201");
								}
								contentsInstTbl.setUseYn("Y");
								
								//영상이 최초 등록된 영상이라면 대표영상으로 지정한다
								long count = contentsInstServices.countMedia(contentsInstTbl.getCtId());
								
								if(count == 0){
									contentsInstTbl.setRpYn("Y");
								}else{
									contentsInstTbl.setRpYn("N");
								}
								contentsInstServices.add(contentsInstTbl);

								// 대표화면 적용
								if(transcode.getRpImgKfrmSeq() != null) {
									ContentsTbl contentsTbl = new ContentsTbl();
									contentsTbl.setCtId(transcode.getCtId());
									contentsTbl.setRpimgKfrmSeq(transcode.getRpImgKfrmSeq());
									contentsTbl.setDataStatCd("001");
									contentsTbl.setWrkFileNm(transcode.getTargetFlNm());
									contentsServices.updateCotentObj(contentsTbl);

									// 검색 메타정보에 반영
									clipSearchService.updateSearchMeta(contentsTbl);
								}

								status = "C";
								traTbl.setPrgrs(100);
								traTbl.setModDt(new Date());
							} else if(state.equals("start")) {
								status = "S";
								traTbl.setReqDt(new Date());
								traTbl.setPrgrs(transcode.getProgress());
							} else if(state.equals("fail")) {
								status = "E";
								traTbl.setModDt(new Date());
								traTbl.setPrgrs(transcode.getProgress());
							}


							traTbl.setJobStatus(status);

							traServices.update(traTbl);

						} else throw new Exception("tc out msg is blank!");
					} catch (Exception e) {
						logger.error("TC Status Update Error", e);
					} finally {
						FileUtils.forceDelete(f);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[TC Status Exec] WatcherThread Error - "+e.getMessage());
		}
	}

}

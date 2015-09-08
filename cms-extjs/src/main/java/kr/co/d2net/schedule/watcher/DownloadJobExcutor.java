package kr.co.d2net.schedule.watcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.DownloadTbl;
import kr.co.d2net.dto.vo.Download;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.DownloadServices;
import kr.co.d2net.utils.SwappingFifoQueue;
import kr.co.d2net.utils.Utility;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component("downloadWorker")
public class DownloadJobExcutor {

	@Autowired
	private MessageSource messageSource;

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DownloadServices downloadServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	private static volatile SwappingFifoQueue<Download> dnJobs = new SwappingFifoQueue<Download>();
	private ExecutorService downloadJob = Executors.newFixedThreadPool(2);
	private ExecutorService getDownloadJob = Executors.newSingleThreadScheduledExecutor();


	public static Integer getDnSize() {
		return dnJobs.size();
	}

	public static boolean hasJob() {
		return !dnJobs.isEmpty();
	}

	public static Download getJob() {
		return dnJobs.get();
	}

	public static void putJob(Download download) {
		dnJobs.put(download);
	}


	@PostConstruct
	public void start() {
//		getDownloadJob.execute(new DownloadJobSwap());
//		downloadJob.execute(new DownloadSwap());
//		downloadJob.execute(new DownloadSwap());
		if(logger.isInfoEnabled()) {
			logger.info("ScheduleDownloadControlExecutor Thread start !!");
		}
	}

	/**
	 * Download job을 가져오기 위한 thread.
	 * @author Administrator
	 *
	 */
	public class DownloadJobSwap implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
					int dnSize = DownloadJobExcutor.getDnSize();

					if(logger.isInfoEnabled()) {
						logger.info("DownloadControlWorker Low Job - "+dnSize);
					}

					if(true) {
						List<Download> downloads = null;

						if(dnSize == 0) {
							/*
							 * DOWNLOAD_TBL에서 workStatCd가 '001' 조회
							 * 전송하다 서버가 죽었을 경우 workStatcd 001 부터 조회해서 다시 queue에 등록해야함
							 */
							downloads = downloadServices.getDownloadJob("001");

							if(downloads == null || downloads.size() <= 0){
								downloads = downloadServices.getDownloadJob("000");
							}
						} else {
							downloads = downloadServices.getDownloadJob("000");
						}

						for(Download download : downloads){

							Download download2 = new Download();

							download2.setSeq(download.getSeq());
							download2.setCtiId(download.getCtiId());
							download2.setWorkStatCd("001");
							download2.setErrorCd(download.getErrorCd());
							download2.setReason(download.getReason());
							download2.setRegDt(Utility.getTimestamp());
							download2.setApproveId(download.getApproveId());
							download2.setPrgrs(download.getPrgrs());

							if(download2 != null){
								downloadServices.updateDownloadHisState(download2);
								DownloadJobExcutor.putJob(download2);
							}
						}
					}
				} catch (Exception e) {
					logger.error("DownloadJobSwap",e);
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception e) {}
			}
		}

	}




	/**
	 * Download job에서 job을 가져온뒤 해당 Download의 데이터 파일 전송을 하는 thread.
	 * @author Administrator
	 *
	 */
	public class DownloadSwap implements Runnable {
		@Override
		public void run() {
			while(true) {
				int dnSize = DownloadJobExcutor.getDnSize();

				if(dnSize > 0){

					Download download = null;

					if(DownloadJobExcutor.hasJob()) {
						download = DownloadJobExcutor.getJob();
					} else {
						download = new Download();
					}

					try {
						ContentsInstTbl  contentsInstTbl = contentsInstServices.getContentInstInfoByCtiId(download.getCtiId());

						Calendar cal = Calendar.getInstance();
						String dateString = String.format("%02d%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 1, cal.get(Calendar.SECOND));

						//job을 가져간다음에 workstatcd를 002로 바꿔줌
						//percent가 올라가면 workstatcd가 003으로 바뀜
						DownloadTbl downloadTbl = downloadServices.getDownloadInfo(download.getSeq());

						String tmpPath = "";

						if(Utility.getOs().equals("win")){
							tmpPath = "\\";
						}else{
							tmpPath = "/";
						}

						download.setSeq(downloadTbl.getSeq());
						download.setWorkStatCd("002");
						download.setRegDt(null);
						download.setDnStrDt(Utility.getTimestamp());
						download.setDownloadPath(contentsInstTbl.getFlPath() + dateString + tmpPath + downloadTbl.getReqId());

						downloadServices.updateDownloadHisState(download);

						String archiveDrive = messageSource.getMessage("archive.drive", null, null);
						String downloadDrive = messageSource.getMessage("download.drive", null, null);

						File filein  = null;
						File fileout = null;

						if(Utility.getOs().equals("win")){
							filein  = new File(archiveDrive + "\\" + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
							fileout = new File(downloadDrive + "\\" + contentsInstTbl.getFlPath()+ "\\" + dateString + "\\" + downloadTbl.getReqId() + "\\" + dateString, contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt() + ".tmp");
						}else{
							filein  = new File(archiveDrive  + contentsInstTbl.getFlPath() ,contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
							fileout = new File(downloadDrive + contentsInstTbl.getFlPath()  +  downloadTbl.getReqId() + "/" + dateString, contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt() + ".tmp");
						}

						if(filein.exists()){
							if(!fileout.getParentFile().exists()) 
								FileUtils.forceMkdir(fileout.getParentFile());

							BufferedInputStream  fin  = null;
							BufferedOutputStream fout = null;

							try {
								long length  = filein.length();

								long counter = 0;
								int percent = 0;
								int r = 0;
								byte[] b = new byte[10240];

								fin  = new BufferedInputStream(new FileInputStream(filein));
								fout = new BufferedOutputStream(new FileOutputStream(fileout));

								while((r = fin.read(b)) != -1) {

									counter += r;
									int tmp = (int) (100 * counter / length);
									if(tmp != percent){
										percent = tmp;
										
									    if(logger.isInfoEnabled()){
									    	logger.info("percent : " + percent);
									    }

										downloadTbl = downloadServices.getDownloadInfo(download.getSeq());
										download.setSeq(downloadTbl.getSeq());
										download.setPrgrs(percent);
										download.setWorkStatCd("003");
										download.setDnStrDt(null);
										downloadServices.updateDownloadHisState(download);

									}
									fout.write(b, 0, r);
								}
								
								fout.close();
								File convertFileNm = null;


								//전송 완료 되면 tmp파일을 해당 확장자에 맞게 변경
								if(Utility.getOs().equals("win")){
									convertFileNm = new File(downloadDrive + "\\" + contentsInstTbl.getFlPath()+ "\\" + dateString + "\\" + downloadTbl.getReqId() + "\\" + dateString, contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
								}else{
									convertFileNm = new File(downloadDrive + contentsInstTbl.getFlPath()  +  downloadTbl.getReqId() + "/" + dateString, contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
								}

								fileout.renameTo(convertFileNm);

								download.setWorkStatCd("004");
								download.setDnEndDt(Utility.getTimestamp());
								downloadServices.updateDownloadHisState(download);

							} catch (Exception e) {
								logger.error("Exception",e);
								download.setWorkStatCd("005");
								download.setErrorCd("003");
								downloadServices.updateDownloadHisState(download);
							} finally {
								if(fin != null) fin.close();
								if(fout != null) fout.close();
							}
						}else{
							download.setWorkStatCd("005");
							//004(파일없음)
							download.setErrorCd("004");
							downloadServices.updateDownloadHisState(download);
						}
					} catch (Exception e) {
						try {
							download.setWorkStatCd("005");
							download.setErrorCd("003");
							downloadServices.updateDownloadHisState(download);
							logger.error("Exception",e);
						} catch (ServiceException e1) {
							logger.error("ServiceException",e);
						}
					} 
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception e) {
					logger.error("Thread.sleep Exception",e);
				}
			}
		}

	}


	@PreDestroy
	public void stop() {
		if(!getDownloadJob.isShutdown()) {
			getDownloadJob.shutdownNow();
			if(logger.isInfoEnabled()) {
				logger.info("ScheduleDownloadJobExecutor shutdown now!!");
			}
		}

		if(!downloadJob.isShutdown()) {
			downloadJob.shutdownNow();
			if(logger.isInfoEnabled()) {
				logger.info("ScheduleDownloadJobExecutor shutdown now!!");
			}
		}

	}

}

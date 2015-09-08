package kr.co.d2net.schedule.watcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.vo.Archive;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.utils.SwappingFifoQueue;
import kr.co.d2net.utils.Utility;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component("archiveWorker")
public class ArchiveJobExcutor {

	@Autowired
	private MessageSource messageSource;

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ArchiveServices archiveServices;

	@Autowired
	private EquipmentServices equipmentServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	private static volatile SwappingFifoQueue<Archive> archiveJobs = new SwappingFifoQueue<Archive>();
	private ExecutorService archiveJob = Executors.newFixedThreadPool(2);
	private ExecutorService getArchiveJob = Executors.newSingleThreadScheduledExecutor();


	public static Integer getArchiveSize() {
		return archiveJobs.size();
	}

	public static boolean hasJob() {
		return !archiveJobs.isEmpty();
	}

	public static Archive getJob() {
		return archiveJobs.get();
	}

	public static void putJob(Archive archive) {
		archiveJobs.put(archive);
	}


	@PostConstruct
	public void start() {
//		getArchiveJob.execute(new GetArchiveSwap());
//		archiveJob.execute(new ArchiveSwap());
//		archiveJob.execute(new ArchiveSwap());
	}



	/**
	 * Archive job을 가져오기 위한 thread.
	 * @author Administrator
	 *
	 */
	public class GetArchiveSwap implements Runnable {
		@Override
		public void run() {
			while(true) {
				List<Archive> archives = null;

				try {
					int acSize = ArchiveJobExcutor.getArchiveSize();

					if(logger.isInfoEnabled()) {
						logger.info("ArchiveControlWorker Low Job - "+acSize);
					}

					if(acSize == 0){
						archives = archiveServices.getArchiveJob("001");

						if(archives == null || archives.size() <= 0){
							archives = archiveServices.getArchiveJob("000");
						}
					}else{
						archives = archiveServices.getArchiveJob("000");
					}

					for(Archive archive : archives){

						Archive archive2 = new Archive();

						archive2.setSeq(archive.getSeq());
						archive2.setCtiId(archive.getCtiId());
						archive2.setWorkStatCd("001");
						archive2.setErrorCd(archive.getErrorCd());
						archive2.setCont(archive.getCont());
						archive2.setRegDt(Utility.getTimestamp());
						archive2.setApproveId(archive.getApproveId());
						archive2.setPrgrs(archive.getPrgrs());

						if(archives != null){
							archiveServices.updateArchiveHisState(archive2);
							ArchiveJobExcutor.putJob(archive2);
						}
					}
				} catch (Exception e) {
					logger.error("ServiceException : ",e);
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception e) {}
			}
		}

	}


	/**
	 * Archive job에서 job을 가져온뒤 해당 Archive 데이터 파일 전송을 하는 thread.
	 * @author Administrator
	 *
	 */
	public class ArchiveSwap implements Runnable {
		@Override
		public void run() {
			while(true) {
				int acSize = ArchiveJobExcutor.getArchiveSize();

				if(acSize > 0){

					Archive archive = null;

					if(ArchiveJobExcutor.hasJob()) {
						archive = ArchiveJobExcutor.getJob();
					} else {
						archive = new Archive();
					}

					try {

						ContentsInstTbl  contentsInstTbl = contentsInstServices.getContentInstInfoByCtiId(archive.getCtiId());

						//job을 가져간다음에 workstatcd를 002로 바꿔줌
						//percent가 올라가면 workstatcd가 003으로 바뀜
						ArchiveTbl archiveTbl = archiveServices.getArchiveInfo(archive.getSeq());

						archive.setSeq(archiveTbl.getSeq());
						archive.setWorkStatCd("002");
						archive.setArchivePath(contentsInstTbl.getFlPath());

						archiveServices.updateArchiveHisState(archive);

						String highDrive = messageSource.getMessage("high.drive", null, null);
						String archiveDrive = messageSource.getMessage("archive.drive", null, null);

						File filein = null;
						File fileout = null;

						if(Utility.getOs().equals("win")){
							filein  = new File(highDrive + "\\" + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
							fileout = new File(archiveDrive + "\\" + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt() + ".tmp");
						}else{
							filein  = new File(highDrive  + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
							fileout = new File(archiveDrive  + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt() + ".tmp");
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

								while( (r = fin.read(b)) != -1) {

									counter += r;
									int tmp = (int) (100 * counter / length);
									if(tmp != percent){
										percent = tmp;

										logger.info("percent : " + percent);

										archiveTbl = archiveServices.getArchiveInfo(archive.getSeq());
										archive.setSeq(archiveTbl.getSeq());
										archive.setPrgrs(percent);
										archive.setWorkStatCd("003");
										archiveServices.updateArchiveHisState(archive);

									}
									fout.write(b, 0, r);
								}

								fout.close();
								File convertFileNm = null;


								//전송 완료 되면 tmp파일을 해당 확장자에 맞게 변경
								if(Utility.getOs().equals("win")){
									convertFileNm = new File(archiveDrive + "\\" + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
								}else{
									convertFileNm = new File(archiveDrive  + contentsInstTbl.getFlPath(),contentsInstTbl.getWrkFileNm() + "." + contentsInstTbl.getFlExt());
								}

								fileout.renameTo(convertFileNm);

								archive.setWorkStatCd("004");
								archiveServices.updateArchiveHisState(archive);

							} catch (IOException e) {
								logger.error("Exception",e);
								archive.setWorkStatCd("005");
								archive.setErrorCd("003");
								archiveServices.updateArchiveHisState(archive);
							} finally {
								if(fin != null) fin.close();
								if(fout != null) fout.close();
							}
						}else{
							archive.setWorkStatCd("005");
							//004(파일없음)
							archive.setErrorCd("004");
							archiveServices.updateArchiveHisState(archive);
						}
					} catch (Exception e) {
						try {
							archive.setWorkStatCd("005");
							archive.setErrorCd("001");
							archiveServices.updateArchiveHisState(archive);
						} catch (ServiceException e1) {
							logger.error("ArchiveSwap",e);
						}
					} 
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception e) {}
			}
		}

	}




	@PreDestroy
	public void stop() {
		if(!getArchiveJob.isShutdown()) {
			getArchiveJob.shutdownNow();
			if(logger.isInfoEnabled()) {
				logger.info("ScheduleGetArchiveJobExecutor shutdown now!!");
			}
		}

		if(!archiveJob.isShutdown()) {
			archiveJob.shutdownNow();
			if(logger.isInfoEnabled()) {
				logger.info("ScheduleArchiveJobExecutor shutdown now!!");
			}
		}

	}

}

package kr.co.d2net.schedule.watcher;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.json.Transcode;
import kr.co.d2net.dto.xml.MetaInfo;
import kr.co.d2net.dto.xml.Metadata;
import kr.co.d2net.dto.xml.Workflow;
import kr.co.d2net.schedule.Worker;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ClipSearchService;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.TraServices;
import kr.co.d2net.service.XmlConvertorService;
import kr.co.d2net.service.XmlConvertorServiceImpl;
import kr.co.d2net.service.XmlFileService;
import kr.co.d2net.service.XmlFileServiceImpl;
import kr.co.d2net.utils.ExtFileFilter;
import kr.co.d2net.utils.JSON;
import kr.co.d2net.utils.Utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("ingestWatcherWorker")
public class IngestWatcherWorker implements Worker {

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
	private CategoryServices categoryServices;
	@Autowired
	private EpisodeServices episodeServices;
	@Autowired
	private ClipSearchService clipSearchService;
	
	private final XmlConvertorService<Workflow> convertorService = new XmlConvertorServiceImpl<Workflow>();


	public void work() {
		/*
		String threadName = Thread.currentThread().getName();
		if(logger.isDebugEnabled()) {
			logger.debug("Scheduling Start - IngestWatcherWorker - thread[" + threadName + "] has began working.");
		}
		*/
		
		String drive = messageSource.getMessage("high.drive", null, Locale.KOREA);
		String folder = messageSource.getMessage("ingest.xml", null, Locale.KOREA);
		try {
			
			File outDir = new File(drive+folder);
			File[] files = outDir.listFiles(new ExtFileFilter("xml"));

			if(files != null && files.length > 0) {
				for(File f : files) {
					try {
						String xml = FileUtils.readFileToString(f, "utf-8");
						if(logger.isDebugEnabled()) {
							logger.debug(xml);
						}
						Workflow workflow = convertorService.unMarshaller(xml);
						MetaInfo metaInfo = workflow.getMetaInfo();
						if(logger.isDebugEnabled()) {
							logger.debug("eqId: "+metaInfo.getEqId()+", regrid: "+metaInfo.getRegrid());
						}
						
						Metadata metadata = workflow.getMetadata();
						
						ContentsTbl contentsTbl = metadata.getContentsTbl();
						contentsTbl.setCtId(null);
						contentsTbl.setUseYn("Y");
						contentsTbl.setRegDt(new Date());
						contentsTbl.setDataStatCd("000");
						contentsTbl.setProdRoute(metaInfo.getEqId());
						contentsTbl.setRegrId(StringUtils.defaultIfBlank(metaInfo.getRegrid(), metaInfo.getEqChannel()));
						contentsTbl.setCtCla(StringUtils.defaultIfBlank(contentsTbl.getCtCla(), "000"));
						contentsTbl.setCtTyp(StringUtils.defaultIfBlank(contentsTbl.getCtTyp(), "000"));
						contentsTbl.setRistClfCd(StringUtils.defaultIfBlank(contentsTbl.getRistClfCd(), "000"));

						if(contentsTbl.getDuration() != null && contentsTbl.getDuration() > 0) {
							contentsTbl.setCtLeng(Utility.changeDuration(contentsTbl.getDuration()));
						}
						
						// 카테고리명 조회 (검색엔진에 넘겨줘야함)
						if(contentsTbl.getCategoryId() > 0) {
							CategoryTbl categoryTbl = categoryServices.getCategoryObj(contentsTbl.getCategoryId());
							if(logger.isDebugEnabled()) {
								logger.debug("category_Id: "+categoryTbl.getCategoryId()+", nodes: "+categoryTbl.getNodes());
							}
							contentsTbl.setCategoryNm(categoryTbl.getCategoryNm());
							contentsTbl.setNodes(categoryTbl.getNodes());
						}
						
						if(contentsTbl.getEpisodeId() > 0) {
							EpisodeId id = new EpisodeId();
							id.setCategoryId(contentsTbl.getCategoryId());
							id.setEpisodeId(contentsTbl.getEpisodeId());
							EpisodeTbl episodeTbl = episodeServices.getEpisodeObj(id);
							contentsTbl.setEpisodeNm(episodeTbl.getEpisodeNm());
						}
						
						Long ctId = contentsServices.add(contentsTbl);
						
						if(logger.isDebugEnabled()) {
							logger.debug("ctId: "+ctId);
						}
						List<ContentsInstTbl> contentsInstTbls = metadata.getContentsInstTbls();
						boolean rowExist = false;
						
						for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
							contentsInstTbl.setUseYn("Y");
							contentsInstTbl.setCtId(ctId);
							contentsInstTbl.setRegDt(new Date());
							contentsInstTbl.setCtiFmt(contentsInstTbl.getCtiFmt());
							
							if(StringUtils.isNotBlank(contentsInstTbl.getFlPath())) {
								if(!contentsInstTbl.getFlPath().startsWith("/")) {
									contentsInstTbl.setFlPath("/"+contentsInstTbl.getFlPath());
								}
								if(!contentsInstTbl.getFlPath().endsWith("/")) {
									contentsInstTbl.setFlPath(contentsInstTbl.getFlPath()+"/");
								}
							}
							
							if(StringUtils.isBlank(contentsInstTbl.getWrkFileNm())) {
								contentsInstTbl.setWrkFileNm(contentsInstTbl.getOrgFileNm());
							}
							
							Long ctiId = contentsInstServices.add(contentsInstTbl);
							
							if(logger.isDebugEnabled()) {
								logger.debug("ctiId: "+ctiId);
							}
							
							if(contentsInstTbl.getCtiFmt().startsWith("2"))
								rowExist = true;
							
							// 고화질 메타정보를 검색엔진에 보낸다.
							if(contentsInstTbl.getCtiFmt().startsWith("1")) {
								clipSearchService.addData(contentsTbl, contentsInstTbl);
							}
							
						}
						
						if(logger.isInfoEnabled()) {
							logger.info("rowes : "+rowExist);
						}
						
						// 저화질 영상이 없으므로 트랜스코더에 변환요청을 보낸다.
						if(!rowExist) {
							String tcIn = messageSource.getMessage("tc.xml.in", null, null);
							XmlFileService xmlFileService = new XmlFileServiceImpl();
							
							String rowDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
							Transcode transcode = null;
							File rowDir = null;
							for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
								try {
									TraTbl traTbl = new TraTbl();
									traTbl.setCtId(contentsInstTbl.getCtId());
									traTbl.setRegDt(new Date());
									Long seq = traServices.add(traTbl);
									
									transcode = new Transcode();
									transcode.setSeq(seq);
									transcode.setCtId(contentsInstTbl.getCtId());
									transcode.setSourceFlPath(contentsInstTbl.getFlPath().replaceAll("\\/", "\\\\"));
									transcode.setSourceFlNm(contentsInstTbl.getWrkFileNm());
									transcode.setSourceFlExt(contentsInstTbl.getFlExt());
									
									String wrkFileNm = contentsInstTbl.getWrkFileNm().substring(contentsInstTbl.getWrkFileNm().lastIndexOf("_")+1);
									transcode.setTargetFlNm(wrkFileNm);
									transcode.setTargetFlExt("mp4");
									
									rowDir = new File(rowDrive+File.separator+contentsInstTbl.getFlPath());
									if(!rowDir.exists()) rowDir.mkdirs();
									
									if(logger.isDebugEnabled())
										logger.debug("req json: "+JSON.toString(transcode));
									xmlFileService.StringToFile(JSON.toString(transcode), drive+tcIn, contentsInstTbl.getCtId()+".txt");
								} catch (Exception e) {
									logger.error("transcode request error", e);
								}
								
							}
						}
					} catch (Exception e) {
						if(e instanceof JAXBException) {
							logger.error("Xml Parsing Error", e);
						} else if(e instanceof IOException) {
							logger.error("XML Read Error", e);
						} else {
							logger.error("ingest worker error", e);
						}
					} finally {
						FileUtils.forceDelete(f);
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("Ingest WatcherThread Error - "+e.getMessage());
		}

		/*if(logger.isDebugEnabled()) {
			logger.debug("Scheduling end - IngestWatcherWorker - thread[" + threadName + "] has completed work.");
		}*/
	}

}

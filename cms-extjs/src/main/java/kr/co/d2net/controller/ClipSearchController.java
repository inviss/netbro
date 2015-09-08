package kr.co.d2net.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.dto.AttachTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.CornerTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Attatch;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Code;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Media;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.StoryBoard;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ClipSearchService;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CornerServices;
import kr.co.d2net.service.DownloadServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.NoticeServices;
import kr.co.d2net.utils.ExtFileFilter;
import kr.co.d2net.utils.FreeMarkerTemplateMethod;
import kr.co.d2net.utils.Utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;


/**
 * 클립검색과 관련된 업무로직이 구현된 class
 * @author vayne,asura
 *
 */
@Controller
public class ClipSearchController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private FreeMarkerTemplateMethod freeMarkerTemplateMethod;

	@Autowired
	private CodeServices codeServices;

	@Autowired
	private CornerServices cornerServices;

	@Autowired
	private NoticeServices noticeServices;

	@Autowired
	private ClipSearchService clipSearchService;

	@Autowired
	private ArchiveServices archiveServices;
	
	@Autowired
	private DownloadServices downloadServices;
	/**
	 * 클립검색 카테고리를 가지고 올때.
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/clip/search.ssc", method = RequestMethod.GET)
	public ModelMap findMainCategory(ModelMap map, HttpServletRequest request) {

		Map<String, Object> tplMap = (Map<String, Object>)WebUtils.getSessionAttribute(request, "tpl");

		if (tplMap == null)
			tplMap = new HashMap<String, Object>();

		if (!tplMap.containsKey("tpl") || tplMap.get("tpl") == null) {
			freeMarkerTemplateMethod.init(tplMap);
		}


		UserTbl user = (UserTbl)WebUtils.getSessionAttribute(request, "user");

		// layout/user/header.ftl에서 default값을 비교.
		// true면 클립검색만 보이게.
		// false면 권한에 맞는 메뉴를 보여준다.
		if(user == null){
			WebUtils.setSessionAttribute(request, "default","true");
		}else{
			WebUtils.setSessionAttribute(request, "default","false");
			WebUtils.setSessionAttribute(request, "tpl", tplMap);
			request.setAttribute("tpl", tplMap);
		}

		map.addAttribute("codeId", new CodeId());	 
		map.addAttribute("search", new Search());
		map.addAttribute("notice", new Notice());
		map.addAttribute("media", new Media());
		return map;

	}


	/**
	 * 클립검색에서 검색엔진을 이용해 데이터를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/findClipSearchList.ssc", method = RequestMethod.GET)
	public ModelAndView findClipSearchList(@ModelAttribute("search") Search search)  {

		if (search.getPageNo() == null || search.getPageNo() == 0) {
			search.setPageNo(1);
		}

		if (search.getCategoryId() == null || search.getCategoryId() == 0) {
			search.setCategoryId(0);
		}

		if(logger.isInfoEnabled()){
			logger.info("search.getKeyword() : " + search.getKeyword());
			logger.info("search.getCtId() : " + search.getCtId());
			logger.info("search.getCatrgoryId() : " + search.getCategoryId());
			logger.info("search.getStartDt() : " + search.getStartDt());
			logger.info("search.getEndDt : " + search.getEndDt());
			logger.info("search.getPageNo : " + search.getPageNo());
			logger.info("search.getSearchTyp : " + search.getSearchTyp());
			logger.info("search.getDateGb : " + search.getDateGb());
			logger.info("search.getCtCla : " + search.getCtCla());
			logger.info("search.getCtTyp : " + search.getCtTyp());
			logger.info("search.getRistClfCd : " + search.getRistClfCd());
			logger.info("search.getVersion : " + search.getVersion());
		}

		ModelAndView view = new ModelAndView();
		Calendar cal = Calendar.getInstance();
		
		if(search.getStartDt() == null && search.getEndDt() == null) {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			search.setEndDt(cal.getTime());
			search.setRegEndDt(cal.getTime());

			// 프로퍼티에 설정되어 있는 조회기간을 가져온다.
			int period = 30;
			try {
				period = Integer.valueOf(messageSource.getMessage("clip.search.period", null, Locale.KOREA));
			} catch (Exception e) {
				logger.error("[clip.search.period] property read error", e);
			}
			
			cal.add(Calendar.DATE, -period);
			cal.set(Calendar.HOUR_OF_DAY, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND, 01);
			search.setStartDt(cal.getTime());
			search.setRegStartDt(cal.getTime());
		} else {
			cal.setTime(search.getStartDt());
			cal.set(Calendar.HOUR_OF_DAY, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND, 01);
			
			search.setStartDt(cal.getTime());
			search.setRegStartDt(cal.getTime());
			
			cal.setTime(search.getEndDt());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			
			search.setEndDt(cal.getTime());
			search.setRegEndDt(cal.getTime());
		}

		try {
		
			CategoryTbl categoryTbl = null;
			List<SearchMeta> tempMetas = null;
			List<SearchMeta> searchMetas = new ArrayList<SearchMeta>();

			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			if(search.getCategoryId() != 0){
				//카테고리id의 정보를 조회
				categoryTbl = categoryServices.getCategoryObj(search.getCategoryId());
				search.setNodes(categoryTbl.getNodes());
			}

			// 기본 등록일 검색, 방송일 검색일경우 brdDd
			search.setDateGb(StringUtils.defaultIfBlank(search.getDateGb(), "regDt"));
			//조회조건에 맞는 총 초외 건수조회
			long total = clipSearchService.findCount(search);
			
			if(logger.isDebugEnabled()){
				logger.debug("total : " + total);
			}
		
			view.addObject("total", total);

			//조회하고자하는 방식에 따라 조회 건수 변경
			if(search.getSearchTyp().equals("image")){
				search.setPageFrom((search.getPageNo()-1) * SearchControls.CLIP_IMAGE_COUNT);
				search.setPageSize(SearchControls.CLIP_IMAGE_COUNT);
			}else{
				search.setPageFrom((search.getPageNo()-1) * SearchControls.CLIP_LIST_COUNT);
				search.setPageSize(SearchControls.CLIP_LIST_COUNT);
			}
			
			//검색엔진 데이터 검색 요청
			tempMetas = clipSearchService.findData(search);

			if(logger.isDebugEnabled()){
				logger.debug("search.getPageFrom()       "+search.getPageFrom());
				logger.debug("search.getPageSize()       "+search.getPageSize());				
				logger.debug("tempMetas.size()                " + tempMetas.size() );
			}
			
			/* 
			 * duration을 time code로 변경
			 * 파일 경로 정보 일부 수정후 다시 리스트에 담는다.
			*/
			for(SearchMeta info : tempMetas){

				String timeCode = Utility.changeDuration(info.getDuration());
				info.setCtLeng(timeCode);

				if(logger.isDebugEnabled()){
					logger.debug("info.getCtId()     "+info.getCtId());					
				}

				String fileNm = info.getSvrFlNm();
				String fileFullPath = info.getFlPath();
				String path = fileFullPath.substring(0,fileFullPath.lastIndexOf("/")+1);
				
				if(StringUtils.isNotBlank(fileNm)){
					String substrFileNm = fileNm.substring(fileNm.lastIndexOf('_')+1);
					String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
					String fullPath = apacheIp + path + "/" + substrFileNm+"/"+info.getRpImgKfrmSeq()+".jpg";
					info.setFlPath(fullPath);					
				}else{
					String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
					String fullPath ="";
					info.setFlPath(fullPath);
				}

				//수정된 정보 다시 리스트에 담는다
				searchMetas.add(info);
			}

			if(searchMetas.size() == 0){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.002", null, null));
			}else{
				view.addObject("result", "Y");
			}

			view.addObject("metas", searchMetas);
			view.addObject("search", search);
			view.addObject("apacheIp", apacheIp);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getCause());
			view.setViewName("jsonView");	
			return view;
		} catch(Exception e){
			e.printStackTrace();
			if(e instanceof NoNodeAvailableException) {

				view.addObject("reason",messageSource.getMessage("error.016",null,null));
			}
			logger.error("ClipSearchController Exception", e);
			view.addObject("result", "N");
			view.setViewName("jsonView");	
		}

		return view;
	}



	/**
	 * findClipSearchList에서 제목 및 이미지를 클릭했을때 기본정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchBasicInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getClipSearchBasicInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();
		//저해상도 드라이므명을 properties에서 읽어온다
		String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
		try{
			if(logger.isInfoEnabled()){
				logger.info("search.getCtId: "+search.getCtId());
				logger.info("search.getKeyword: "+search.getKeyword());
			}
			
			//ct_id로 컨텐츠 정보 조회
			Content  contentsTbl = contentsServices.getConObj(search.getCtId());
			//데이더 정리 상태 코드 조회
			List<CodeTbl>codeTbl = codeServices.findCodeInfos("DSCD");
			/* initalHibernate 했을 경우 넣어야하는 로직


			if(contentsTbl.getContentsInst() != null && !contentsTbl.getContentsInst().isEmpty()) {
				Set<ContentsInstTbl> contentsInst = new HashSet<ContentsInstTbl>();
				for(ContentsInstTbl contentsInstTbl : contentsTbl.getContentsInst()) {
					contentsInstTbl.setContentsTbl(null);
					contentsInst.add(contentsInstTbl);
				}
				contentsTbl.setContentsInst(contentsInst);
			}*/

			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.
			if(contentsTbl != null) {
				ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
				contentsTbl.setVdHresol(contentsInstTbl.getVdHresol());
				contentsTbl.setVdVresol(contentsInstTbl.getVdVersol());
				contentsTbl.setFrmPerSec(contentsInstTbl.getFrmPerSec());
				
				if(logger.isDebugEnabled()) {
					logger.debug("contentsInstTbl : "+contentsInstTbl+", fl_path: "+contentsInstTbl.getFlPath());
				}

				//조회된 contentsInstTbl의 정보가 없다면 오류처리
				if(contentsInstTbl == null){
					view.addObject("result","N");
					view.addObject("reason",messageSource.getMessage("error.008", null, null));
					view.setViewName("jsonView");				

					return view;
				}

				//역으로 manyToOne으로 걸려져있는 contentsTbl의 링크를 끊기 위해서 null로 지정을 한다.
				contentsInstTbl.setContentsTbl(null);

				//카테고리의 root 정보를 가져온다.
				Search optionview = new Search();

				optionview.setDepth(0);
				optionview.setCategoryId(0);


				
				String filePath = contentsInstTbl.getFlPath();//ex): /201310/17
				String fileNm = contentsInstTbl.getWrkFileNm();//ex):로컬에서보낸거_20131017103032
				String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

				//카테고리ID가 null이 아니라면 정보 조회
				if(contentsTbl.getCategoryId()!= null){
					CategoryTbl cateogryInfo = new CategoryTbl();

					cateogryInfo = categoryServices.getCategoryObj(contentsTbl.getCategoryId());
				
					if(cateogryInfo == null) {
						view.addObject("result", "N");
						view.addObject("reason", messageSource.getMessage("error.008", null, null));
						view.setViewName("jsonView");	
					
						return view;
					}
					
					if(cateogryInfo.getCategoryNm().length()!=0){
						contentsTbl.setCategoryNm(cateogryInfo.getCategoryNm());
					}

				}

				//에피소드id가 null이 아니라면 정보조회
				if(contentsTbl.getEpisodeId() != null){
					EpisodeId Id = new EpisodeId();

					Id.setCategoryId(contentsTbl.getCategoryId());
					Id.setEpisodeId(contentsTbl.getEpisodeId());

					EpisodeTbl episode = episodeServices.getEpisodeObj(Id);

					if(episode != null){
						contentsTbl.setEpisodeNm(episode.getEpisodeNm());
					}else{
						contentsTbl.setEpisodeNm("");	
					}
				}

				//세그먼트정보는 현재 사용하지 않음으로 null 처리
				if(contentsTbl.getEpisodeId() != null){
					contentsTbl.setSegmentNm("");
				}
				
				//정리상태 코드명 정보 얻어오기
				if(contentsTbl.getDataStatCd() != null){
					for(CodeTbl code : codeTbl){
						if(contentsTbl.getDataStatCd().equals(code.getId().getSclCd())){
							contentsTbl.setDataStatNm(code.getSclNm());
						}
					}

				}else {
					//20131213  데이터 상태정보 추가후 이전 등록건들이 모두 null이기때문에 default 값을 박아준다. 소스 적용후 삭제할것.
					contentsTbl.setDataStatNm("정리전");
				}

				File rowMeida = new File(targetDrive + filePath + File.separator + substrFilepath+".mp4");
				
				if(logger.isDebugEnabled()){
					logger.debug("rowMeida  : "+rowMeida);
				}
				
				if(rowMeida.isFile()){
					view.addObject("mediaExistYn", "Y");	
				}else{
					view.addObject("mediaExistYn", "N");	
				}
				
				String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);//ex): http://14.36.147.23:8002

				view.addObject("search", search);
				view.addObject("contentsTbl", contentsTbl);
				view.addObject("result", "Y");
				view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFilepath);

				if(logger.isDebugEnabled()){
					logger.debug("filePath : " + filePath);
					logger.debug("substrFilepath : " + substrFilepath);
				}

			} else {
				view.addObject("result","N");
				view.addObject("reason",messageSource.getMessage("error.008", null, null));
				view.setViewName("jsonView");				

				return view;
			}

			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			
			return view;
		}
		
		return view;
	}




	/**
	 * 플레이어정보를 가져온다
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getVideoPlayInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getVideoPlayInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();
		String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
		try{
			if(logger.isInfoEnabled()){
				logger.info("search.getCtId: "+search.getCtId());
				logger.info("search.getKeyword: "+search.getKeyword());
			}

			Content contentsTbl = new Content();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();

			//ctid로 컨텐츠 정보 조회
			contentsTbl = contentsServices.getConObj(search.getCtId());

			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.
			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());

			//ex): /201310/17
			String filePath = contentsInstTbl.getFlPath();
			//ex):로컬에서보낸거_20131017103032
			String fileNm = contentsInstTbl.getWrkFileNm();
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

			File rowMeida = new File(targetDrive + filePath + File.separator + substrFilepath+".mp4");
			
			if(logger.isDebugEnabled()){
				logger.debug("rowMeida :  "+rowMeida);
			}

			if(rowMeida.isFile()){
				view.addObject("mediaExistYn", "Y");	
			}else{
				view.addObject("mediaExistYn", "N");	
			}
			
			//ex): http://14.36.147.23:8002
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			view.addObject("result", "Y");
			view.addObject("ctId", search.getCtId());
			view.addObject("fullPath", apacheIp + filePath+ "/" + substrFilepath);

			if(logger.isInfoEnabled()){
				logger.info("filePath : " + filePath);
				logger.info("substrFilepath : " + substrFilepath);
			}

			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			
			return view;
		}
		
		return view;
	}




	/**
	 * findClipSearchList 스토리보드정보를 가져온다.(extjs용)
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchStroyboardInfoForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView getClipSearchStroyboardInfoForExtJs(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{
			
			if(logger.isInfoEnabled()){
				logger.info("search.getCtId() : "+search.getCtId());
			}

			//ExtJS는 하나의 JSON에 하나의 ROOT를 가지는 형식을 추천하고있으므로 해동 JSON 형식에 맞게 JSON을 구성한다.
			List<StoryBoard> infos= new ArrayList<StoryBoard>();
			Content contentsTbl = new Content();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			contentsTbl = contentsServices.getConObj(search.getCtId());
			
			if(contentsTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}

			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
			
			if(contentsInstTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			
			String filePath = contentsInstTbl.getFlPath();	//ex): /201310/17
			String fileNm = contentsInstTbl.getWrkFileNm(); 	//ex): 스토리지에서보낸거1_20131017103054
			String substrFileNm = fileNm.substring(fileNm.lastIndexOf('_')+1);
			String folder = null;
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);//ex): Z:
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);	//ex): http://14.36.147.23:8002
			folder = targetDrive + filePath + "/" + substrFileNm;

			if(logger.isDebugEnabled()){
				logger.debug("folder : "+folder);
			}

			File outDir = new File(folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt,TXT"));
			String[] filesData = null;
			long duration = 0l;//변환된 duration값
			String ctLeng = null;
			ArrayList<Long> durationData = new ArrayList<Long>();//변환된 duration값을 넣을 ArrayList
			ArrayList<String> ctLengData = new ArrayList<String>();
			
			if(files != null && files.length > 0) {
				
				for(File f : files) {
					String txt = FileUtils.readFileToString(f, "utf-8");

					if(txt.indexOf(",") > -1) {
						filesData = txt.split(",");
					} else {
						filesData = new String[] {txt};
					}

					for (int j = 0; j < filesData.length; j++) {
					
						if(StringUtils.isNotBlank(filesData[j])) {
							StoryBoard storyBoard = new StoryBoard();
							ctLeng = Utility.changeDuration(Long.parseLong(filesData[j]));//ex): 0,50,100,150,200,250,300,350
							duration = Utility.changeDurationToSecond(Long.parseLong(filesData[j]));
							storyBoard.setCtLeng(ctLeng);
							storyBoard.setImg(Long.parseLong(filesData[j]));
							storyBoard.setDuration(duration);
							storyBoard.setUrl(apacheIp + filePath+ "/" + substrFileNm+"/"+storyBoard.getImg()+".jpg");
							storyBoard.setCtId(search.getCtId());
							storyBoard.setRpImg(contentsTbl.getRpimgKfrmSeq());
							infos.add(storyBoard);
						}
						
					}
				}
			}


			//스토리보드 txt파일data
			view.addObject("storyboard", infos);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getCause());
			view.setViewName("jsonView");	
			return view;
		}catch (IOException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getCause());
			view.setViewName("jsonView");	
			return view;
		}
		return view;
	}


	/**
	 * 코너의 정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchStroyboardCornerInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getClipSearchStroyboardCornerInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{
			if(logger.isDebugEnabled()){
				logger.debug("search.getCtId() : "+search.getCtId());
			}
			
			//ctId에 소속된 코너정보 리스트를 조회한다
			List<CornerTbl> cornerTbls = cornerServices.findCornerList(search.getCtId());

			//스토리보드 txt파일data
			view.addObject("cornerTbls", cornerTbls);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getCause());
			view.setViewName("jsonView");	
			return view;
		}
		return view;
	}

	/**
	 * findClipSearchList 첨부파일정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchAttachInfo.ssc", method = RequestMethod.POST)
	public ModelAndView getClipSearchAttachInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			//ctId의 매타정보를 조회한다
			Content contentsTbl = contentsServices.getConObj(search.getCtId());
			//첨부파일 정보를 조회한다
			List<AttachTbl> attachTbl = attachServices.findAttachObj(search.getCtId());

			if(logger.isInfoEnabled()){
				logger.info("search.getCtId: "+search.getCtId());
				logger.info("search.getKeyword: "+search.getKeyword());	
			}

			if(contentsTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
			
				return view;
			}

			view.addObject("result", "Y");
			view.addObject("search", search);
			view.addObject("contentsTbl", contentsTbl);
			view.addObject("attachTbl", attachTbl);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getCause());
			view.setViewName("jsonView");	
			
			return view;
		}
		
		return view;
	}



	/**
	 * 카테고리 트리 함수(extJs 전용)
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/clip/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		try{
			
			if(logger.isInfoEnabled()){
				logger.info("search.getNode() : " + search.getNode() );
			}
	
			CategoryTbl searchInfo = new CategoryTbl();
			
			//noder값(categoryId)를 가지고 카테고리의 기본정보를 조회한다. total인경우 전체조회임으로 root값을 강제로 생성한다.
			if(!search.getNode().equals("total")){
				searchInfo = categoryServices.getCategoryObj(Integer.parseInt(search.getNode()));
			}else{
				searchInfo.setDepth(0);
			}
			
			//임시리스트와 최종리스트를 생성한다.
			List<CategoryTbl> tempCategoryTbls = categoryServices.findMainCategoryForExtJs(searchInfo,search);
			List<Category> categoryTbls = new ArrayList<Category>();

			if(logger.isDebugEnabled()){
				logger.debug("size() : "+categoryTbls.size());
			}

			for(CategoryTbl info : tempCategoryTbls){
				Category category = new Category();
				category.setCategoryId(info.getCategoryId());
				category.setCategoryNm(info.getCategoryNm());
				category.setDepth(info.getDepth());
				category.setNodes(info.getNodes());
				category.setOrderNum(info.getOrderNum());
				category.setPreParent(info.getPreParent());

				List<CategoryTbl> results = categoryServices.findSubNodesList(category.getNodes());

				if(results.size() >= 1){
					category.setFinalYn("N");
				}else{
					category.setFinalYn("Y");
				}
				
				info.setEpisodeTbl(null);
				categoryTbls.add(category);
			}

			List<CategoryTreeForExtJs> store = categoryServices.makeExtJsJson(categoryTbls);
		
			if(categoryTbls.size() !=0){
				result.addObject("store", store);
				result.setViewName("jsonView");
			}else{
				result.addObject("categoryTbls", Collections.EMPTY_LIST);
				result.addObject("result", "N");
				result.setViewName("jsonView");	
			}
			
		} catch(ServiceException e){
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			return result;
		}

		return result;
	}

	/**
	 * 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/clip/findSclListForClf.ssc", method = RequestMethod.GET)
	public ModelAndView findSclListForClf(@ModelAttribute("search") Search search)  {

		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("getClfCD "+search.getClfCd()); 
		}

		CodeTbl codeTbl = new CodeTbl();
		codeTbl.setClfCd( search.getClfCd());

		try {

			//CLFCD에 종속된 코드정보를 모두조회하다
			List<CodeTbl> codeInfos  =  codeServices.findSclListForClf(codeTbl);
			List<Code> codes = new ArrayList<Code>();

			for(CodeTbl code : codeInfos){
				Code info  = new Code();
				info.setSclCd(code.getId().getSclCd());
				info.setSclNm(code.getSclNm());
				codes.add(info);
			}
			
			result.addObject("codeInfos",codes);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
			return result;
		}


	}


	/**
	 * 파일을 다운로드 요청한다.
	 * @param search
	 * @param flPath
	 * @param transFileNm
	 * @param orgFileNm
	 * @return
	 */	
	@RequestMapping(value = {"/clip/filedownload.ssc"})
	public ModelAndView FileDownload(@ModelAttribute("search") Search search, @RequestParam("flPath") String flPath,
									 @RequestParam("transFileNm") String transFileNm, @RequestParam("orgFileNm") String orgFileNm)  {

		if(logger.isInfoEnabled()){
			logger.info("flPath : "+flPath);
			logger.info("transFileNm : "+transFileNm);
			logger.info("orgFileNm : "+orgFileNm);
		}
		
		ModelAndView view = new ModelAndView();
		Map<String, Object> params = new HashMap<String, Object>();
		String baseDir = messageSource.getMessage("row.drive", null, null);

		try {
			//파일 패스 교체
			flPath = flPath.replaceAll("\\\\", "/");

			Attatch attatch = new Attatch();
			attatch.setRealNm(orgFileNm);

			String sourceFile = baseDir + File.separator + "attach" + flPath + File.separator + transFileNm;

			if(SystemUtils.IS_OS_WINDOWS) {
				sourceFile = sourceFile.replaceAll("/", "\\\\");//윈도우 시스템이라면
			}

			File f = new File(sourceFile);

			if (logger.isInfoEnabled()) {
				logger.info("source file : " + sourceFile);
				logger.info("file exist : " + f.exists());
			}

			params.put("file", f);
			params.put("attatch", attatch);

			if (f.exists()) {
				view.addObject("result", "Y");
				params.put("result", "Y");
			} else {
				view.addObject("result", "N");
				params.put("result", "N");
			}

			view.addAllObjects(params);
			view.setViewName("fileView");

		} catch (Exception e) {
			logger.error("FileDownload Error", e);
			view.addObject("result", "N");
		}

		return view;

	}


	/**
	 * 파일을 다운로드 요청하기전에 실제 스토리지에 파일이 있는지 여부를 파악한다.
	 * @param search
	 * @param flPath
	 * @param transFileNm
	 * @param orgFileNm
	 * @return
	 */	
	@RequestMapping(value = {"/clip/fileExistYn.ssc"})
	public ModelAndView fileExistYn(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getFlPath() + "+search.getFlPath());
		}
		
		ModelAndView view = new ModelAndView();
		String baseDir = messageSource.getMessage("row.drive", null, null);

		try {
		
			//파일 패스 교체
			String flPath = search.getFlPath().replaceAll("\\\\", "/");
			Attatch attatch = new Attatch();
			
			attatch.setRealNm(search.getOrgFilenm());

			String sourceFile = baseDir + File.separator + "attach" + flPath + File.separator + search.getTransFilenm();

			if(SystemUtils.IS_OS_WINDOWS){
				sourceFile = sourceFile.replaceAll("/", "\\\\");//윈도우 시스템이라면
			}

			File f = new File(sourceFile);

			if (logger.isInfoEnabled()) {
				logger.info("source file : " + sourceFile);
				logger.info("file exist : " + f.exists());
			}

			if (f.exists()) {
				
				if(logger.isDebugEnabled()){
					logger.debug("f.canExecute() : " + f.canExecute());
					logger.debug("f.canWrite() : " + f.canWrite());
					logger.debug("f.canRead() : " + f.canExecute());
				}
				
				if(f.canWrite()){
					view.addObject("result", "Y");
				}else{
					view.addObject("result", "N");	
					view.addObject("reason", messageSource.getMessage("error.011", null, null));	
				}

			} else {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.010", null, null));	
			}

			view.setViewName("jsonView");
		
		} catch (Exception e) {
			logger.error("FileDownload Error", e);
			view.addObject("result", "N");
		}

		return view;

	}

	/**
	 * 오류등록함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/clip/updateErrorArrange.ssc", method = RequestMethod.POST)
	public ModelAndView updateErrorArrange(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("updateErrorArrange.search.getCtId: "+search.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = new ContentsTbl();

			//ctId로 메타정보 조회
			contentsTbl = contentsServices.getContentObj(search.getCtId());
			
			if(contentsTbl == null) {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
			
				return view;
			}

			//이미 오류등록된 영상이 아니면 오류등록
			if(contentsTbl.getDataStatCd() != "003"){
				contentsTbl.setDataStatCd("003");
				contentsTbl.setArrangeDt(new Date());

				contentsServices.saveContentObj(contentsTbl);
				contentsTbl.setCorner(null);
				contentsTbl.setContentsInst(null);
				contentsTbl.setContentsMod(null);
				contentsTbl.setSegmentTbl(null);
				contentsTbl.setAttachTbls(null);
				view.addObject("result", "Y");
			}else{
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.014", null, null));
			}

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("search", search);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		}

		return view;

	}



	/**
	 * 공지사항여부를 파악한다 조회한다.
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/clip/findNoticeList.ssc", method = RequestMethod.GET)
	public ModelAndView findNoticeList(@ModelAttribute("notice") Notice notice)  {

		ModelAndView result = new ModelAndView();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 01);
		notice.setStartDd(cal.getTime());

		if(logger.isInfoEnabled()){
			logger.info("startDd "+notice.getStartDd());
		}

		try {

			//로그인 날짜기준으로 팝업으로 보여주어야할 공지사항이 있는지 조회
			List<NoticeTbl> noticeInfos  =  noticeServices.findAfterLoginPopUpNoticeList(notice);

			result.addObject("noticeInfos",noticeInfos);
			result.addObject("result","Y");
			result.setViewName("jsonView");
			
			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}


	/**
	 * ct_id에 소속된 검색영상 모두를 조회한다.
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/clip/findMediaList.ssc", method = RequestMethod.GET)
	public ModelAndView findMediaList(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+search.getCtId());
		}
		
		ModelAndView result = new ModelAndView();


		try {

			//ctId에 소속된 검색영상 정보 조회
			List<Media> medias = contentsInstServices.findMediaList(search.getCtId());
			result.addObject("medias",medias);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}


	/**
	 * ct_id와 cti_fmt의 정보를 가지고 상세정보를 조회한다
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/clip/getMediaInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getMediaInfo(@ModelAttribute("meida") Media meida)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+meida.getCtId());
			logger.info("search.getCtiFmt() "+meida.getCtiFmt());
		}
		
		ModelAndView result = new ModelAndView();

		try {
			
			//각 검색영상메타의 상세정보 조회
			Media mediaInfo = contentsInstServices.getMediaInfo(meida);
			
			if(logger.isInfoEnabled()){
				logger.info("mediaInfo   getCtId "+mediaInfo.getCtId());
			}
			
			result.addObject("medias",mediaInfo);
			result.addObject("result","Y");
			result.setViewName("jsonView");
			
			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}

	
	
	/**
	 * 다운로드를 요청한다.
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/clip/insertDownloadContent.ssc", method = RequestMethod.POST)
	public ModelAndView insertDownloadContent(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+search.getCtId()); 
			logger.info("search.getUserId() "+search.getUserId()); 
		}
		
		ModelAndView result = new ModelAndView();

		try {
			ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstInfoByCtId(search.getCtId(),"10");

			//이미 아카이브 요청이 된건인지에 대해서 확인한다.
			int count = archiveServices.checkCompleteArchive(contentsInstTbl.getCtiId());
			
			//아카이브가 된건에 한해서 다운로드가 가능하다.
			if(count == 0){
				result.addObject("result","F");
				result.addObject("reason" , "아카이브가 되지 않은 건입니다.");
			}else{
				downloadServices.add(contentsInstTbl.getCtiId(),search.getUserId());
				result.addObject("result","Y");
			}
			
			result.setViewName("jsonView");
			
			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}
	
	
}

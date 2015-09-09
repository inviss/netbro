package kr.co.d2net.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.d2net.dto.AttachTbl;
import kr.co.d2net.dto.Attatch;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.CornerTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.StoryBoard;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.index.SearchMetaIndex;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsModServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CornerServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.UserServices;
import kr.co.d2net.utils.ExtFileFilter;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 컨텐츠 검색과 관련된 업무로직이 담겨잇는 함수
 * @author asura
 *
 */
@Controller
public class ContentsController {

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
	private CodeServices codeServices;

	@Autowired
	private CornerServices cornerServices;

	@Autowired
	private ContentsModServices contentsModServices;

	@Autowired
	private UserServices userServices;
	/**
	 * 컨텐츠 검색 화면 로딩시 필요한 코드정보 조회
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/search.ssc", method = RequestMethod.GET)
	public ModelMap findContentsSearch(ModelMap map)  {

		Search search = new Search();
		search.setDepth(0);
		search.setCategoryId(0);
		try {
			List<CategoryTbl>category = categoryServices.findMainCategory(search);
			map.addAttribute("categories", category);
			map.addAttribute("search", new Search());
			map.addAttribute("codeTbl", new CodeTbl());
			map.addAttribute("codeId", new CodeId());
			map.addAttribute("storyBoard", new StoryBoard());

		} catch (ServiceException e) {
			map.addAttribute("categories", Collections.EMPTY_LIST);
			map.addAttribute("search", new Search());
			map.addAttribute("codeTbl", new CodeTbl());
			map.addAttribute("codeId", new CodeId());
			map.addAttribute("storyBoard", new StoryBoard());
		}

		return map;

	}

	/**
	 * 우측 화면 리스트의 정보를 보여주는 화면.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/findSearchList.ssc", method = RequestMethod.GET)
	public ModelAndView findContentsSearchList(@ModelAttribute("search") Search search, 
			ModelMap map)  {
		ModelAndView view = new ModelAndView();

		if (search.getPageNo() == null || search.getPageNo() == 0) {

			search.setPageNo(1);
			logger.debug("pageNo: "+search.getPageNo());

		}
		String paramValue ="";
		search.setSearchTyp("list");
		if(logger.isDebugEnabled()){

			logger.debug("search.getKeyword() : " + search.getKeyword());
			logger.debug("search.getCtId() : " + search.getCtId());
			logger.debug("search.getCatrgoryId() : " + search.getCategoryId());
			logger.debug("getStartDt :"+search.getStartDt());
			logger.debug("getEndDt :"+search.getEndDt());
			logger.debug("getSearchTyp :"+search.getSearchTyp());
			logger.debug("getDataStatCd :"+search.getDataStatCd());
			logger.debug("getCtTyp :"+search.getCtTyp());
		}

		CategoryTbl info = new CategoryTbl();

		if(search.getCategoryId() != 0){

			try {
				info = categoryServices.getCategoryObj(search.getCategoryId());

				if(info == null) {
					view.addObject("result", "N");
					view.addObject("reason", messageSource.getMessage("error.009", null, null));
					view.setViewName("jsonView");	
					return view;
				}
				search.setNodes(info.getNodes());
				logger.debug("getSearchTyp :"+search.getNodes());
			} catch (ServiceException e) {
				view.addObject("result", "N");
				view.addObject("reason", e.getMessage());
				view.setViewName("jsonView");	
				return view;
			}
		}



		//2013. 12. 11 
		if(search.getStartDt() == null && search.getEndDt() == null){
			Date today = new Date();
			search.setStartDt(today);
			search.setEndDt(today);
		}
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat startFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat endFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		Date sDate = new Date();
		Date eDate = new Date();


		String tempToday = startFormat.format(search.getStartDt());

		//프로퍼티에 설정되어 있는 조회기간을 가져온다.
		String period = messageSource.getMessage("clip.search.period", null, Locale.KOREA);

		cal.add(cal.DATE, Integer.parseInt(period));
		eDate = cal.getTime();

		String  tempBeforeToday = startFormat.format(eDate);
		try {
			sDate = endFormat.parse(tempToday+"010101");
			eDate = endFormat.parse(tempBeforeToday+"595959");
		} catch (ParseException e) {
			logger.error("change date formate exception : " +e);
		}
		search.setStartDt(sDate);
		search.setEndDt(eDate);


		List<Content> Contents = null;

		try {

			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			// 첫 조회시에만 전체 조회수를 가져옴.
			// 이후부터는 검색 리스트의 하단에 이어붙이기를 하므로 전체 조회수 필요없음.
			long total = 0L;
			search.setCtiFmt("10");
			total = contentsServices.countAllContents(search);

			logger.debug("total : " + total);
			if(total == -1){
				view.addObject("result","N");
				view.addObject("reason",messageSource.getMessage("error.004", null, null));
				view.setViewName("jsonView");
				return view;
			}else{
				view.addObject("total", total);
			}
			if(search.getPageNo() == null){

				search.setPageNo(0);

			}

			if(search.getSearchTyp().equals("image")){

				Contents = contentsServices.getAllContentsInfo(search);

			}else if(search.getSearchTyp().equals("list")){

				Contents = contentsServices.getAllContentsInfo(search);	

			}

			if(Contents.size() == 0){
				view.addObject("result", "N");
				view.addObject("reason",messageSource.getMessage("error.002", null, null));
			}else{
				view.addObject("result", "Y");
			}

			view.addObject("metas", Contents);

			view.addObject("search", search);
			view.addObject("apacheIp", apacheIp);
			view.setViewName("jsonView");

		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}



	/**
	 * 영상id로 기본정보 검색.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/getContentsBasicInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsBasicInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();


			contentsTbl = contentsServices.getContentObj(search.getCtId());
			if(contentsTbl == null){
				view.addObject("result","N");
				view.addObject("reason",messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");				

				return view;
			}

			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.

			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
			if(contentsInstTbl == null){
				view.addObject("result","N");
				view.addObject("result",messageSource.getMessage("error.008", null, null));
				view.setViewName("jsonView");				


				return view;
			}

			Search optionview = new Search();
			optionview.setCategoryId(0);
			optionview.setDepth(0);
			List<CategoryTbl> categoryTbl = new ArrayList<CategoryTbl>();
			List<CategoryTbl> tempCategoryTbl = categoryServices.findMainCategory(optionview);
			List<CodeTbl>codeTbl = codeServices.getCodeInfos("DSCD");

			String filePath = contentsInstTbl.getFlPath(); //contentsInstTbl.getFlPath(); // /201309/05
			String fileNm = contentsInstTbl.getWrkFileNm(); //contentsInstTbl.getFlPath(); // /201309/05
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);


			for(CategoryTbl info : tempCategoryTbl){
				info.setEpisodeTbl(null);
				categoryTbl.add(info);
			}
			if(contentsTbl.getCategoryId() != null){

				CategoryTbl cateogryInfo = new CategoryTbl();

				cateogryInfo = categoryServices.getCategoryObj(contentsTbl.getCategoryId());
				if(cateogryInfo == null) {
					view.addObject("result", "N");
					view.addObject("reason", messageSource.getMessage("error.009", null, null));
					view.setViewName("jsonView");	
					return view;
				}
				if(cateogryInfo.getCategoryNm().length() != 0){

					contentsTbl.setCategoryNm(cateogryInfo.getCategoryNm());

				}

			}

			if(contentsTbl.getEpisodeId() != null){

				EpisodeId Id = new EpisodeId();
				Id.setCategoryId(contentsTbl.getCategoryId());
				Id.setEpisodeId(contentsTbl.getEpisodeId());
				EpisodeTbl episode = episodeServices.episodeInfo(Id);

				if(episode != null){

					contentsTbl.setEpisodeNm(episode.getEpisodeNm());

				}else{

					contentsTbl.setEpisodeNm("");	

				}

			}

			if(contentsTbl.getEpisodeId() != null){

				contentsTbl.setSegmentNm("");

			}	


			if(contentsTbl.getDataStatCd() != null){

				logger.debug("######################contentsTbl.getDataStatCd()   "+contentsTbl.getDataStatCd());
				logger.debug("######################codeTbl   "+codeTbl.size());
				for(CodeTbl code : codeTbl){

					if(contentsTbl.getDataStatCd().equals(code.getId().getSclCd())){

						contentsTbl.setDataStatNm(code.getSclNm());

					}
				}

			}else {
				//20131213  데이터 상태정보 추가후 이전 등록건들이 모두 null이기때문에 default 값을 박아준다. 소스 적용후 삭제할것.
				contentsTbl.setDataStatNm("정리전");

			}

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			if(logger.isDebugEnabled()){

				logger.debug("search.getCtId: "+search.getCtId());
				logger.debug("search.getKeyword: "+search.getKeyword());

			}

			//정리중인 데이터인경우 중복 저장을 방지하기 위해서 저장버튼을 비활성화 시킨다.
			//최고관리자인 경우는 기존과 동일하나 최고관리자가 아닌 다른 역할자의 경우 정리중인 데이터를 더이상 수정치 못하도록 수정 20140421 by asura
		if(contentsTbl.getDataStatCd().equals("001")){
			List<Users> users = userServices.findUserAndAuthObj(search.getModrId());

			search.setDataStatCd(contentsTbl.getDataStatCd());

			ContentsModTbl contentsModTbl =  contentsModServices.getLastModInfo(search);
			String mainAdminYn ="N";
			//마지막 저장자와 현재 조회자의 id가 동일 하다면 버튼을 보여준다.
			if(search.getModrId().equals(contentsModTbl.getModId())){
				view.addObject("button", "Y");	
			}else {
				//마지막 저장자와 현재 조회자의 id가 같지 않지만 현재 조회자의 역활이 admin이면 버튼을 활성화 아니라면 비활성화한다.
				for(Users info : users){
					//최고관리자라면 무조건버튼을 보이도록한다.
					
					if(info.getAuthId() == 1){
						mainAdminYn = "Y";
						
					}
				}

				
				if(mainAdminYn.equals("Y")){
					view.addObject("button", "Y");	
				}else{
					view.addObject("button", "N");	
				}
			}
			
		}else{
			view.addObject("button", "Y");	
		}
			File rowMeida = new File(targetDrive + filePath + File.separator + substrFilepath+".mp4");
			logger.debug("##########rowMeida   "+rowMeida);
			if(rowMeida.isFile()){
				view.addObject("mediaExistYn", "Y");	
			}else{
				view.addObject("mediaExistYn", "N");	
			}
			logger.debug("##################keyword   " +contentsTbl.getKeyWords());
			view.addObject("search", search);			
			view.addObject("contentsTbl", contentsTbl);	
			view.addObject("codeTbl", codeTbl);	
			view.addObject("categoryList", categoryTbl);
			view.addObject("fullPath", apacheIp + filePath + File.separator +substrFilepath);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * ct_id로 스토리보드 정보 검색
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/getContentStoryBoardInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentStoryBoardInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();

		try{

			logger.debug("search.getCtId() : "+search.getCtId());

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			contentsTbl = contentsServices.getContentObj(search.getCtId());
			if(contentsTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.004", null, null));
				view.setViewName("jsonView");	
				return view;
			}


			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
			if(contentsInstTbl == null) {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}

			String filePath = contentsInstTbl.getFlPath(); //contentsInstTbl.getFlPath(); // /201309/05
			String fileNm = contentsInstTbl.getWrkFileNm(); //contentsInstTbl.getFlPath(); // /201309/05
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

			String folder = null;
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			folder = targetDrive + filePath + File.separator + substrFilepath;

			logger.debug(folder);

			File outDir = new File(folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt,TXT"));

			String[] filesData = null;
			//변환된 duration값
			String ctLeng = null;
			Long duration =0l;
			//변환된 duration값을 넣을 ArrayList
			ArrayList<Long> durationData = new ArrayList<Long>();
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
						//logger.debug("filesData  "+filesData[j]);
						if(StringUtils.isNotBlank(filesData[j])) {
							//나머지 경우 .txt값 ex:)0,50,100,150,200,250,300,350, --> 이 값은 변환 가능
							ctLeng = Utility.changeDuration(Long.parseLong(filesData[j]));
							duration = Utility.changeDurationToSecond(Long.parseLong(filesData[j]));
							ctLengData.add(ctLeng);
							durationData.add(duration);

						}

					}

				}

			}

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			List<CornerTbl> cornerTbls = cornerServices.findCornerList(search.getCtId());

			if(cornerTbls.size() == 0){
				CornerTbl info = new CornerTbl();
				info.setDuration(0L);
				info.setsDuration(0L);
				info.setCnNm("코너제목");
				info.setCnCont("코너내용");
				cornerTbls.add(info);
			}
			
			
			
			//정리중인 데이터인경우 중복 저장을 방지하기 위해서 저장버튼을 비활성화 시킨다.
			//최고관리자인 경우는 기존과 동일하나 최고관리자가 아닌 다른 역할자의 경우 정리중인 데이터를 더이상 수정치 못하도록 수정 20140421 by asura
		if(contentsTbl.getDataStatCd().equals("001")){
			List<Users> users = userServices.findUserAndAuthObj(search.getModrId());

			search.setDataStatCd(contentsTbl.getDataStatCd());

			ContentsModTbl contentsModTbl =  contentsModServices.getLastModInfo(search);
			String mainAdminYn ="N";
			//마지막 저장자와 현재 조회자의 id가 동일 하다면 버튼을 보여준다.
			if(search.getModrId().equals(contentsModTbl.getModId())){
				view.addObject("button", "Y");	
			}else {
				//마지막 저장자와 현재 조회자의 id가 같지 않지만 현재 조회자의 역활이 admin이면 버튼을 활성화 아니라면 비활성화한다.
				for(Users info : users){
					//최고관리자라면 무조건버튼을 보이도록한다.
					
					if(info.getAuthId() == 1){
						mainAdminYn = "Y";
						
					}
				}

				
				if(mainAdminYn.equals("Y")){
					view.addObject("button", "Y");	
				}else{
					view.addObject("button", "N");	
				}
			}
			
		}else{
			view.addObject("button", "Y");	
		}
			//스토리보드 txt파일data
			view.addObject("result", "Y");
			view.addObject("storyboard", filesData);
			view.addObject("storyboard", filesData);
			view.addObject("durationData", durationData);
			view.addObject("cornerTbls", cornerTbls);
			view.addObject("ctLengData", ctLengData);
			view.addObject("contentsTbl", contentsTbl);
			view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFilepath);
			view.setViewName("jsonView");

		} catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * ct_id로 첨부파일 정보 검색
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/getContentsAttachInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsAttachInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("search.getCtId() : "+search.getCtId());

			}

			ContentsTbl contentsTbl = contentsServices.getContentObj(search.getCtId());
			List<AttachTbl> attachTbl = attachServices.getAttachObj(search.getCtId());

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			if(logger.isDebugEnabled()){

				logger.debug("attachTbl : "+attachTbl);
				logger.debug("search.getCtId: "+search.getCtId());
				logger.debug("search.getKeyword: "+search.getKeyword());

			}

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("attachTbl", attachTbl);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * 기본정보 저장함수.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateBasicInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateBasicInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("saveBasicInfo.search.getCtId: "+search.getCtId());
				logger.debug("saveBasicInfo.search.getContentNm: "+search.getContentNm());
				logger.debug("saveBasicInfo.search.getCategoryId: "+search.getCategoryId());
				logger.debug("saveBasicInfo.search.getEpisodeId: "+search.getEpisodeId());
				logger.debug("saveBasicInfo.search.getSegmentId: "+search.getSegmentId());
				logger.debug("saveBasicInfo.search.getSpcInfo: "+search.getSpcInfo());
				logger.debug("saveBasicInfo.search.getKeyword: "+search.getKeyword());
				logger.debug("saveBasicInfo.search.getModrId: "+search.getModrId());

			}

			ContentsTbl contentsTbl = contentsServices.getContentObj(search.getCtId());

			contentsTbl.setCtNm(search.getContentNm());
			contentsTbl.setCtId(search.getCtId());
			contentsTbl.setCategoryId(search.getCategoryId());
			contentsTbl.setEpisodeId(search.getEpisodeId());
			contentsTbl.setSegmentId(search.getSegmentId());
			contentsTbl.setSpcInfo(search.getSpcInfo());
			contentsTbl.setKeyWords(search.getKeyword());
			//정리전으로 값을 저장한다.
			contentsTbl.setDataStatCd("001");
			contentsTbl.setModrId(search.getModrId());
			ContentsTbl updateInfo = contentsServices.saveContentObj(contentsTbl);

			CategoryTbl info = categoryServices.getCategoryObj(updateInfo.getCategoryId());
			updateInfo.setNodes(info.getNodes());
			updateInfo.setCategoryNm(info.getCategoryNm());

			EpisodeId id = new EpisodeId();
			id.setCategoryId(updateInfo.getCategoryId());
			id.setEpisodeId(updateInfo.getEpisodeId());
			EpisodeTbl episodeTbl = episodeServices.getEpisodeObj(id);
			updateInfo.setEpisodeNm(episodeTbl.getEpisodeNm());


			contentsTbl.setCorner(null);
			contentsTbl.setContentsInst(null);
			contentsTbl.setContentsMod(null);
			contentsTbl.setSegmentTbl(null);
			contentsTbl.setAttachTbls(null);

			ContentsInstTbl contentsInstTbl  =  contentsInstServices.getContentInstObj(contentsTbl.getCtId());

			SearchMetaIndex searchMetaIndex = new SearchMetaIndex();
			searchMetaIndex.updateSearchMeta(updateInfo,contentsInstTbl);



			view.addObject("result", "Y");
			view.addObject("contentsTbl", contentsTbl);
			view.addObject("search", search);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		} catch (Exception e) {

			if(e.getCause() instanceof PersistenceException) {

				view.addObject("reason",messageSource.getMessage("error.003",null,null));
			} else if(e instanceof NoNodeAvailableException) {

				view.addObject("reason",messageSource.getMessage("error.016",null,null));
			} else {
				logger.error("########################PersistenceException error###################");
				view.addObject("reason",messageSource.getMessage("error.015",null,null));
			}

			view.addObject("result", "N");
			view.setViewName("jsonView");	
			return view;
		}


		return view;

	}


	/**
	 * 첨부파일 삭제 함수. 
	 * 파일 삭제와, db에 등록되어잇는 함수 모두 삭제
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/contents/deleteAttachFile.ssc"})
	public ModelAndView DeleteAttachFile(HttpServletRequest request)  {

		ModelAndView view = new ModelAndView();
		AttachTbl attachTbl = new AttachTbl();

		String baseDir = messageSource.getMessage("row.drive", null, null);
		try {

			attachTbl.setSeq(Long.parseLong(request.getParameter("seq")));
			AttachTbl deleteObj = attachServices.getAttach(attachTbl.getSeq());

			if(logger.isDebugEnabled()){

				logger.debug("deleteObj.getSeq()   " + deleteObj.getSeq());
				logger.debug("deleteObj.getOrgFilenm()  "+deleteObj.getOrgFilenm());
				logger.debug("deleteObj.getTransFilenm()  "+deleteObj.getTransFilenm());
				logger.debug("deleteObj.getFlPath()   "+deleteObj.getFlPath());

			}


			String sourceFile = baseDir + File.separator + "attach" + deleteObj.getFlPath() + File.separator + deleteObj.getTransFilenm();

			File f = new File(sourceFile);

			if(f.delete()){

				logger.debug("file delete sucess");
				attachServices.delete(attachTbl);
				view.addObject("result", "Y");
				view.setViewName("jsonView");
			}else {

				logger.debug("file delete fail");
				view.addObject("result", "F");
				view.addObject("reason", messageSource.getMessage("error.013", null, null));
				view.setViewName("jsonView");
			}



			return view;
		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


	}

	/**
	 * 파일을 업로드 한다.
	 * db에 업로드 정보를 저장한다.
	 * @param request
	 * @param response
	 * @param file
	 * @throws IOException 
	 * @
	 */
	@ResponseBody
	@RequestMapping(value="/contents/uploadFile.ssc", produces="text/plain")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("uploadFile") MultipartFile file) throws IOException 
	{

		PrintWriter writer = response.getWriter();

		try {

			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String attach = messageSource.getMessage("attach", null, Locale.KOREA);

			String savePath = targetDrive + attach; // 복사할 경로

			AttachTbl attachTbl = new AttachTbl();

			String[] saveFile = Utility.uploadFile(file, savePath);

			if(saveFile == null || saveFile.length == 0 || saveFile[0] == null) {

				logger.error("writeImageContent error : saveFile is null");
				writer.print("N"); // 결과 전달
				writer.flush();
				writer.close();

			}else{

				if(logger.isDebugEnabled()){

					logger.debug("target path: "+saveFile[0]);
					logger.debug("filename   : "+saveFile[1]);
					logger.debug("orgFileNm   : "+saveFile[2]);

				}

				Long ctId = Long.parseLong(request.getParameter("ctId"));

				attachTbl.setCtId(ctId);
				attachTbl.setRegDt(new Date());
				attachTbl.setRegrId("");
				attachTbl.setFlPath("/" + saveFile[0]);
				attachTbl.setTransFilenm(saveFile[1]);
				attachTbl.setOrgFilenm(saveFile[2]);

				attachServices.add(attachTbl);
				response.setContentType("text/html; charset=utf-8");

				//view.setViewName("jsonView");
				writer.print("Y"); // 결과 전달
			}

		} catch (Exception e) {

			logger.error("writeImageContent", e);
			writer.print("N"); // 결과 전달

		}

		writer.flush();
		writer.close();
		//return view;

	}

	/**
	 * 파일을 다운로드 요청한다.
	 * @param search
	 * @param flPath
	 * @param transFileNm
	 * @param orgFileNm
	 * @return
	 */	
	@RequestMapping(value = {"/contents/filedownload.ssc"})
	public ModelAndView FileDownload(@ModelAttribute("search") Search search,
			@RequestParam("flPath") String flPath,
			@RequestParam("transFileNm") String transFileNm,
			@RequestParam("orgFileNm") String orgFileNm)  {

		ModelAndView view = new ModelAndView();

		Map<String, Object> params = new HashMap<String, Object>();
		String baseDir = messageSource.getMessage("row.drive", null, null);

		try {
			//파일 패스 교체
			flPath = flPath.replaceAll("\\\\", "/");

			Attatch attatch = new Attatch();
			attatch.setRealNm(orgFileNm);

			String sourceFile = baseDir + File.separator + "attach" + flPath + File.separator + transFileNm;

			if(SystemUtils.IS_OS_WINDOWS) sourceFile = sourceFile.replaceAll("/", "\\\\");//윈도우 시스템이라면

			File f = new File(sourceFile);

			if (logger.isDebugEnabled()) {

				logger.debug("source file : " + sourceFile);
				logger.debug("file exist : " + f.exists());

			}

			params.put("file", f);
			params.put("attatch", attatch);

			if (f.exists()) {

				view.addObject("result", "Y");

			} else {

				view.addObject("result", "N");

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
	 * 정리완료 저장함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateCompleteArrange.ssc", method = RequestMethod.POST)
	public ModelAndView updateCompleteArrange(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("updateCompleteArrange.search.getCtId: "+search.getCtId());

			}

			ContentsTbl contentsTbl = new ContentsTbl();

			contentsTbl = contentsServices.getContentObj(search.getCtId());
			if(contentsTbl == null) {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			contentsTbl.setDataStatCd("002");
			contentsTbl.setArrangeDt(new Date());
			contentsTbl.setModrId(search.getModrId());

			contentsServices.saveContentObj(contentsTbl);
			contentsTbl.setCorner(null);
			contentsTbl.setContentsInst(null);
			contentsTbl.setContentsMod(null);
			contentsTbl.setSegmentTbl(null);
			contentsTbl.setAttachTbls(null);

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("search", search);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * 오류등록함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateErrorArrange.ssc", method = RequestMethod.POST)
	public ModelAndView updateErrorArrange(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("updateErrorArrange.search.getCtId: "+search.getCtId());

			}

			ContentsTbl contentsTbl = new ContentsTbl();


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
				contentsTbl.setModrId(search.getModrId());
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
		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * 오류취소함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateCancleError.ssc", method = RequestMethod.POST)
	public ModelAndView updateCancleError(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("updateCancleError.search.getCtIds: "+search.getCtIds());

			}

			String[] ctIds = search.getCtIds().split(",");

			for(int i = 0; i < ctIds.length; i++){
				ContentsTbl contentsTbl = new ContentsTbl();

				contentsTbl = contentsServices.getContentObj(Long.parseLong(ctIds[i]));
				if(contentsTbl == null) {
					view.addObject("result", "N");
					view.addObject("reason", messageSource.getMessage("error.006", null, null));
					view.setViewName("jsonView");	
					return view;
				}
				contentsTbl.setDataStatCd("000");
				contentsTbl.setArrangeDt(new Date());
				contentsTbl.setModrId(search.getModrId());

				contentsServices.saveContentObj(contentsTbl);
				contentsTbl.setCorner(null);
				contentsTbl.setContentsInst(null);
				contentsTbl.setContentsMod(null);
				contentsTbl.setSegmentTbl(null);
				contentsTbl.setAttachTbls(null);
				view.addObject("result", "Y");
				view.addObject("contentsTbl", contentsTbl);
				view.addObject("search", search);
				view.setViewName("jsonView");

			}

		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}


		return view;

	}

	/**
	 * 대표화면 지정 함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateRpimg.ssc", method = RequestMethod.POST)
	public ModelAndView updateRpimg(@ModelAttribute("storyBoard") StoryBoard storyBoard)   {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("updateRpimg.storyBoard.getRpImg: "+storyBoard.getRpImg());
				logger.debug("updateRpimg.storyBoard.getCtId: "+storyBoard.getCtId());
			}
			ContentsTbl contentsTbl = new ContentsTbl();

			contentsTbl = contentsServices.getContentObj(storyBoard.getCtId());
			if(contentsTbl == null)  {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			contentsTbl.setRpimgKfrmSeq(storyBoard.getRpImg());
			contentsTbl.setModrId(storyBoard.getModrId());

			ContentsTbl updateInfo = contentsServices.saveContentObj(contentsTbl);
			CategoryTbl info = new CategoryTbl();


			info = categoryServices.getCategoryObj(updateInfo.getCategoryId());
			if(info == null)  {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.009", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			contentsTbl.setCorner(null);
			contentsTbl.setContentsInst(null);
			contentsTbl.setContentsMod(null);
			contentsTbl.setSegmentTbl(null);
			contentsTbl.setAttachTbls(null);


			SearchMetaIndex searchMetaIndex = new SearchMetaIndex();
			searchMetaIndex.updateRpImgKfrm(updateInfo);

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("result", "Y");
			view.setViewName("jsonView");


		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

		}



		return view;

	}

	/**
	 * 스토리보드 편집후 저장 합수.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/updateStoryBoardImgs.ssc", method = RequestMethod.POST)
	public ModelAndView updateStoryBoardImgs(@ModelAttribute("storyBoard") StoryBoard storyBoard)  {

		ModelAndView view = new ModelAndView();

		try{

			logger.debug("storyBoard.getCtId() : "+storyBoard.getCtId());
			logger.debug("storyBoard.getDeleteImgs() : "+storyBoard.getDeleteImgs());
			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			contentsTbl = contentsServices.getContentObj(storyBoard.getCtId());
			if(contentsTbl == null)  {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}


			contentsInstTbl = contentsInstServices.getContentInstObj(storyBoard.getCtId());
			if(contentsInstTbl == null) {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			String filePath = contentsInstTbl.getFlPath(); //contentsInstTbl.getFlPath(); // /201309/05
			String fileNm = contentsInstTbl.getWrkFileNm(); //contentsInstTbl.getFlPath(); // /201309/05
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

			logger.debug("filePath     "+filePath);
			logger.debug("fileNm       "+fileNm);
			logger.debug("substrFilepath "+substrFilepath);
			String folder = null;
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);
			String text ="";


			folder = targetDrive + filePath + File.separator + substrFilepath;

			logger.debug(folder);

			File outDir = new File(folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt,TXT"));

			String[] filesData = null;

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

							String[] imgs = storyBoard.getDeleteImgs().split(",");

							for(int k = 0; k < imgs.length; k++){

								if(imgs[k].equals(filesData[j])){

									filesData[j] =null;
									logger.debug("folder+ File.separator + imgs[k]  "+folder+ File.separator + imgs[k]);
									//실제 파일 삭제
									File delImg = new File(folder+ File.separator + imgs[k]+".jpg");

									if(delImg.delete()){

										logger.debug("file delete sucess  "+imgs[k]);

									} else {

										logger.debug("file delete fail " +imgs[k]);

									}

								}	

							}

						}

					}

					for (int j = 0; j < filesData.length; j++) {

						if(StringUtils.isNotBlank(filesData[j])) {

							if(text == ""){
								text = filesData[j];

							}else{

								text += "," + filesData[j];
							}


						}

					}

				}

			}

			logger.debug("text   "+text);
			//스토리보드 txt파일data
			//새로 txt 파일을 생성한다
			try{
				BufferedWriter out = new BufferedWriter(new FileWriter(folder+ File.separator + substrFilepath+".txt"));
				out.write(text);
				out.close();
			}catch(IOException e){
				logger.error(e.toString());
			}
			view.addObject("result", "Y");
			view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFilepath);
			view.setViewName("jsonView");

		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	

		}catch (IOException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
		}

		return view;

	}

	/**
	 * 코너정보 저장
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/insertCornerInfo.ssc", method = RequestMethod.POST)
	public ModelAndView insertCornerInfo(@ModelAttribute("storyBoard") StoryBoard storyBoard)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isDebugEnabled()){

				logger.debug("insertCornerInfo.storyBoard.dividIds: "+storyBoard.getDividImgs());
				logger.debug("insertCornerInfo.storyBoard.getCtId: "+storyBoard.getCtId());
				logger.debug("insertCornerInfo.storyBoard.getCnNm: "+storyBoard.getCnNm());
				logger.debug("insertCornerInfo.storyBoard.getCnCont: "+storyBoard.getCnCont());
			}


			CornerTbl cornerTbl = new CornerTbl();

			cornerTbl.setCtId(storyBoard.getCtId());
			Long count = cornerServices.count(cornerTbl);
			/*if(count > 0){
			cornerServices.delete(cornerTbl);
			}*/
			if(count > 0){
				List<CornerTbl> cornerInfo = cornerServices.findCornerList(storyBoard.getCtId());
				cornerServices.updateConrnerInfo(cornerInfo,storyBoard);
			}else{
				cornerServices.add(storyBoard);
			}

			view.addObject("result", "Y");
			view.setViewName("jsonView");


		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	

		}catch (Exception e) {
			logger.debug("##################error   " +e);
			logger.debug("##################errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			return view;

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
	@RequestMapping(value = {"/contents/fileExistYn.ssc"})
	public ModelAndView fileExistYn(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		String baseDir = messageSource.getMessage("row.drive", null, null);

		try {
			//파일 패스 교체
			String flPath = search.getFlPath().replaceAll("\\\\", "/");

			Attatch attatch = new Attatch();
			attatch.setRealNm(search.getOrgFilenm());

			String sourceFile = baseDir + File.separator + "attach" + flPath + File.separator + search.getTransFilenm();

			if(SystemUtils.IS_OS_WINDOWS) sourceFile = sourceFile.replaceAll("/", "\\\\");//윈도우 시스템이라면

			File f = new File(sourceFile);

			if (logger.isDebugEnabled()) {

				logger.debug("source file : " + sourceFile);
				logger.debug("file exist : " + f.exists());

			}


			if (f.exists()) {

				logger.debug("f.canExecute() : " + f.canExecute());
				logger.debug("f.canWrite() : " + f.canWrite());
				logger.debug("f.canRead() : " + f.canExecute());
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


}

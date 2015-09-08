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
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.CornerTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Attatch;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Code;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.dto.vo.Media;
import kr.co.d2net.dto.vo.StoryBoard;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ClipSearchService;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsModServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CornerServices;
import kr.co.d2net.service.DownloadServices;
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

	@Autowired
	private ClipSearchService clipSearchService;

	@Autowired
	private ArchiveServices archiveServices;

	@Autowired
	private DownloadServices downloadServices;

	/**
	 * 컨텐츠 검색 화면 로딩시 필요한 코드정보 조회
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/search.ssc", method = RequestMethod.GET)
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
			map.addAttribute("media", new Media());
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
	 * 좌측 화면 리스트의 정보를 보여주는 화면.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/findSearchList.ssc", method = RequestMethod.GET)
	public ModelAndView findContentsSearchList(@ModelAttribute("search") Search search)  {
		ModelAndView view = new ModelAndView();

		if (search.getPageNo() == null || search.getPageNo() == 0) {
			search.setPageNo(1);
		}

		String paramValue =""; 

		if(logger.isInfoEnabled()){
			logger.info("search.getKeyword() : " + search.getKeyword());
			logger.info("search.getCtId() : " + search.getCtId());
			logger.info("search.getCatrgoryId() : " + search.getCategoryId());
			logger.info("getStartDt :"+search.getStartDt());
			logger.info("getEndDt :"+search.getEndDt());
			logger.info("getSearchTyp :"+search.getSearchTyp());
			logger.info("getDataStatCd :"+search.getDataStatCd());
			logger.info("getCtTyp :"+search.getCtTyp());
			logger.info("pageNo: "+search.getPageNo());
		}

		CategoryTbl info = new CategoryTbl();

		if(search.getCategoryId() != 0){

			try {
				//카테고리id의 메타정보를 조회한다
				info = categoryServices.getCategoryObj(search.getCategoryId());

				if(info == null) {
					view.addObject("result", "N");
					view.addObject("reason", messageSource.getMessage("error.009", null, null));
					view.setViewName("jsonView");	

					return view;
				}

				search.setNodes(info.getNodes()); 
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
			sDate = endFormat.parse(tempToday+"000000");
			eDate = endFormat.parse(tempBeforeToday+"235959");
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

			//조회조건에 맞는 메타 정보를 조회한다
			Contents = contentsServices.findAllContentsInfo(search);

			List<Content> resultList = new ArrayList<Content>();

			for(Content content : Contents){
				String filePath = content.getFlPath();				
				String fileNm = content.getWrkFileNm();
				String substrFileNm = fileNm.substring(fileNm.lastIndexOf('_')+1);
				String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
				String fullPath = apacheIp + filePath+ "/" + substrFileNm+"/"+content.getRpimgKfrmSeq()+".jpg";

				content.setFlPath(fullPath);

				resultList.add(content);
			}

			if(Contents.size() == 0){
				view.addObject("result", "N");
				view.addObject("reason",messageSource.getMessage("error.002", null, null));
			}else{
				view.addObject("result", "Y");
			}

			view.addObject("metas", resultList);
			view.addObject("search", search);
			view.addObject("apacheIp", apacheIp);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	

			return view;

		}catch (Exception e) {
			logger.error("error :  " +e);
			logger.error("errorcode :  " +e.getCause());

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
	@RequestMapping(value = "/contents/arrange/getContentsBasicInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsBasicInfo(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.debug("search.getCtId() : "+search.getCtId());
			logger.debug("search.getKeyword: "+search.getKeyword());
		}

		ModelAndView view = new ModelAndView();

		try{

			Content contentsTbl = new Content();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();
			
			//ctid의 메타정보를 조회한다
			contentsTbl = contentsServices.getConObj(search.getCtId());

			if(contentsTbl == null){
				view.addObject("result","N");
				view.addObject("reason",messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");				

				return view;
			}

			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.
			if(logger.isDebugEnabled()){
				logger.debug("contentsTbl.getCont() : "+contentsTbl.getCont());
			}

			//ctid의 고해상도 메타정보를 조회한다
			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());

			contentsTbl.setVdHresol(contentsInstTbl.getVdHresol());
			contentsTbl.setVdVresol(contentsInstTbl.getVdVersol());
			contentsTbl.setFrmPerSec(contentsInstTbl.getFrmPerSec());

			//고해상도 메타정보가 없다면 오류처리한다
			if(contentsInstTbl == null){
				view.addObject("result","N");
				view.addObject("result",messageSource.getMessage("error.008", null, null));
				view.setViewName("jsonView");				

				return view;
			}

			//역으로 manyToOne으로 걸려져있는 contentsTbl의 링크를 끊기 위해서 null로 지정을 한다.
			contentsInstTbl.setContentsTbl(null);

			Search optionview = new Search();
			optionview.setCategoryId(0);
			optionview.setDepth(0);

			List<CategoryTbl> categoryTbl = new ArrayList<CategoryTbl>();
			
			//depth와 카테고리id정보로 카테로리 정보를 조회한다
			List<CategoryTbl> tempCategoryTbl = categoryServices.findMainCategory(optionview);
			//데이터 정리 상태값을 조회한다
			List<CodeTbl>codeTbl = codeServices.findCodeInfos("DSCD");

			String filePath = contentsInstTbl.getFlPath(); //contentsInstTbl.getFlPath(); // /201309/05
			String fileNm = contentsInstTbl.getWrkFileNm(); //contentsInstTbl.getFlPath(); // /201309/05
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			for(CategoryTbl info : tempCategoryTbl){
				info.setEpisodeTbl(null);
				categoryTbl.add(info);
			}

			//카테고리id가 null이 아니라면 메타정보를 조회한다
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

			//에피소드id가 null이 아니라면 메타정보 조회
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

			//세그먼트 정보를 현재 사용하지 않기 때문에 공맥 처리한다
			if(contentsTbl.getEpisodeId() != null){
				contentsTbl.setSegmentNm("");
			}	

			//데이터정리 상태값이 null이 아니라면 메타정보를 조회한다 
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

			//정리중인 데이터인경우 중복 저장을 방지하기 위해서 저장버튼을 비활성화 시킨다.
			//최고관리자인 경우는 기존과 동일하나 최고관리자가 아닌 다른 역할자의 경우 정리중인 데이터를 더이상 수정치 못하도록 수정 20140421 by asura
			if(contentsTbl.getDataStatCd().equals("001")){
				List<Users> users = userServices.getUserAndAuthObj(search.getModrId());

				search.setDataStatCd(contentsTbl.getDataStatCd());

				ContentsModTbl contentsModTbl =  contentsModServices.getLastModInfo(search);
				String mainAdminYn ="N";
				//마지막 저장자와 현재 조회자의 id가 동일 하다면 버튼을 보여준다.

				if(contentsModTbl.getModId() != null &&  search.getModrId().equals(contentsModTbl.getModId())){
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

			if(logger.isDebugEnabled()){
				logger.debug("rowMeida  : "+rowMeida);
			}

			if(rowMeida.isFile()){
				view.addObject("mediaExistYn", "Y");	
			}else{
				view.addObject("mediaExistYn", "N");	
			}

			if(logger.isDebugEnabled()){
				logger.debug("keyword :  " +contentsTbl.getKeyWords());
			}

			view.addObject("search", search);			
			view.addObject("contentsTbl", contentsTbl);	
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
			logger.error("error  : " +e);
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
	@RequestMapping(value = "/contents/arrange/getContentsAttachInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsAttachInfo(@ModelAttribute("search") Search search, ModelMap map)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() : "+search.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			//ctid로 메타정보를 조회한다
			ContentsTbl contentsTbl = contentsServices.getContentObj(search.getCtId());
			//첨부파일정보를 조회한다
			List<Attatch> attatchs = attachServices.findAttach(search.getCtId());

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			if(logger.isDebugEnabled()){
				logger.debug("attatchs : "+attatchs);
				logger.debug("search.getCtId : "+search.getCtId());
				logger.debug("search.getKeyword : "+search.getKeyword());
			}

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("attachTbl", attatchs);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	

			return view;
		}catch (Exception e) {
			logger.error("error :  " +e);
			logger.error("errorcode  : " +e.getCause());
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
	@RequestMapping(value = "/contents/arrange/updateBasicInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateBasicInfo(@ModelAttribute("search") Search search)  {

		if(logger.isDebugEnabled()){
			logger.debug("saveBasicInfo.search.getCtId: "+search.getCtId());
			logger.debug("saveBasicInfo.search.getContentNm: "+search.getCtNm());
			logger.debug("saveBasicInfo.search.getCategoryId: "+search.getCategoryId());
			logger.debug("saveBasicInfo.search.getEpisodeId: "+search.getEpisodeId());
			logger.debug("saveBasicInfo.search.getSegmentId: "+search.getSegmentId());
			logger.debug("saveBasicInfo.search.getCont: "+search.getCont());
			logger.debug("saveBasicInfo.search.getKeyword: "+search.getKeyword());
			logger.debug("saveBasicInfo.search.getModrId: "+search.getModrId());
			logger.debug("saveBasicInfo.search.getRpImg: "+search.getRpImg());
			logger.debug("saveBasicInfo.search.getRistClfCd: "+search.getRistClfCd());
		}

		ModelAndView view = new ModelAndView();

		try{

			//검색엔진 사용어부를 properties에서 가져온후 검색엔진 사용여부에 따라 검색엔진 업데이틍부를 판단한다.
			String searchEngine = messageSource.getMessage("cms.searchEngine", null, Locale.KOREA);

			ContentsTbl contentsTbl = contentsServices.getContentObj(search.getCtId());

			contentsTbl.setCtNm(search.getCtNm());
			contentsTbl.setCtId(search.getCtId());
			contentsTbl.setCategoryId(search.getCategoryId());
			contentsTbl.setEpisodeId(search.getEpisodeId());
			contentsTbl.setSegmentId(search.getSegmentId());
			contentsTbl.setCont(search.getCont());
			contentsTbl.setKeyWords(search.getKeyword());
			contentsTbl.setRpimgKfrmSeq(search.getRpImg());
			//정리전으로 값을 저장한다.
			contentsTbl.setDataStatCd("001");
			contentsTbl.setModrId(search.getModrId());
			contentsTbl.setRistClfCd(search.getRistClfCd());
			ContentsTbl updateInfo = contentsServices.saveContentObj(contentsTbl);

			//카테고리 정보와 에피스도 정보를 조회하 검색엔진에 node값과 카테고리정보를 수정하다
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

			//검색엔진을 사용하다면 업데이트목록을 검색엔진에 전달.
			if(searchEngine.equals("Y")){				
				clipSearchService.updateData(updateInfo,contentsInstTbl);
			}

			view.addObject("result", "Y");
			view.addObject("contentseeTbl", contentsTbl);
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
	@RequestMapping(value = {"/contents/arrange/deleteAttachFile.ssc"})
	public ModelAndView DeleteAttachFile(HttpServletRequest request)  {

		if(logger.isInfoEnabled()){
			logger.info("request.getParameter seq : " +request.getParameter("seq"));
		}
		ModelAndView view = new ModelAndView();
		AttachTbl attachTbl = new AttachTbl();

		String baseDir = messageSource.getMessage("row.drive", null, null);
		try {

			attachTbl.setSeq(Long.parseLong(request.getParameter("seq")));
			AttachTbl deleteObj = attachServices.getAttach(attachTbl.getSeq());

			if(logger.isInfoEnabled()){
				logger.info("deleteObj.getSeq()   " + deleteObj.getSeq());
				logger.info("deleteObj.getOrgFilenm()  "+deleteObj.getOrgFilenm());
				logger.info("deleteObj.getTransFilenm()  "+deleteObj.getTransFilenm());
				logger.info("deleteObj.getFlPath()   "+deleteObj.getFlPath());
			}

			String sourceFile = baseDir + File.separator + "attach" + deleteObj.getFlPath() + File.separator + deleteObj.getTransFilenm();
			File f = new File(sourceFile);

			if(f.delete()){

				if(logger.isInfoEnabled()){
					logger.info("file delete sucess");
				}

				attachServices.delete(attachTbl);
				view.addObject("result", "Y");
				view.setViewName("jsonView");
			}else {

				if(logger.isInfoEnabled()){
					logger.debug("file delete fail");
				}

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
			logger.error("error :  " +e);
			logger.error("errorcode :  " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	

			return view;
		}
	}

	/**
	 * 파일을 업로드 한다. 현재 extjs버전 구현중 구현완료후 주석처리 소스부분 지울것
	 * db에 업로드 정보를 저장한다.
	 * @param request
	 * @param response
	 * @param file
	 * @throws IOException 
	 * @
	 */
	
	@ResponseBody
	@RequestMapping(value="/contents/arrange/uploadFile.ssc", produces="multipart/form")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("uploadFile") MultipartFile file) throws IOException 
	{
		
		if(logger.isInfoEnabled()){
			logger.info("request   " +request.getAttributeNames());
			logger.info("response  " +response);
			logger.info("file  " +file);
		}

		PrintWriter writer = response.getWriter();
		 
		try {
			
			//properties에서 저장할 경로정보를 가져온다
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
	 
	}

	/**
	 * 파일을 다운로드 요청한다.
	 * @param search
	 * @param flPath
	 * @param transFileNm
	 * @param orgFileNm
	 * @return
	 */	
	@RequestMapping(value = {"/contents/arrange/filedownload.ssc"})
	public ModelAndView FileDownload(@ModelAttribute("search") Search search, @RequestParam("flPath") String flPath, @RequestParam("transFileNm") String transFileNm, @RequestParam("orgFileNm") String orgFileNm)  {


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
	@RequestMapping(value = "/contents/arrange/updateCompleteArrange.ssc", method = RequestMethod.POST)
	public ModelAndView updateCompleteArrange(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(logger.isInfoEnabled()){
				logger.info("updateCompleteArrange.search.getCtId: "+search.getCtId());
			}

			ContentsTbl contentsTbl = new ContentsTbl();
			
			//기존정보를 조회한다
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
			logger.error("error :  " +e);
			logger.error("errorcode :  " +e.getCause());
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
	@RequestMapping(value = "/contents/arrange/updateErrorArrange.ssc", method = RequestMethod.POST)
	public ModelAndView updateErrorArrange(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("updateErrorArrange.search.getCtId: "+search.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = new ContentsTbl();
			//ctid로 메타정보 조회
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
			logger.error("error : " +e);
			logger.error("errorcode : " +e.getCause());
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
	@RequestMapping(value = "/contents/arrange/updateCancleError.ssc", method = RequestMethod.POST)
	public ModelAndView updateCancleError(@ModelAttribute("search") Search search)  {

		if(logger.isDebugEnabled()){
			logger.debug("updateCancleError.search.getCtIds: "+search.getCtIds());
		}

		ModelAndView view = new ModelAndView();

		try{

			String[] ctIds = search.getCtIds().split(",");

			for(int i = 0; i < ctIds.length; i++){
				ContentsTbl contentsTbl = new ContentsTbl();

				//ctid로 메타정보를 조회한다
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
			logger.error("error   " +e);
			logger.error("errorcode   " +e.getCause());
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
	@RequestMapping(value = "/contents/arrange/updateRpimg.ssc", method = RequestMethod.POST)
	public ModelAndView updateRpimg(@ModelAttribute("storyBoard") StoryBoard storyBoard)   {

		if(logger.isInfoEnabled()){
			logger.info("updateRpimg.storyBoard.getRpImg: "+storyBoard.getRpImg());
			logger.info("updateRpimg.storyBoard.getCtId: "+storyBoard.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = new ContentsTbl();

			//ctid로 메타정보를 조회한다
			contentsTbl = contentsServices.getContentObj(storyBoard.getCtId());

			if(contentsTbl == null)  {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	

				return view;
			}

			contentsTbl.setRpimgKfrmSeq(storyBoard.getRpImg());
			contentsTbl.setModrId(storyBoard.getModrId());

			//변경된 대표화면 키프레임 순번을 저장한다.
			ContentsTbl updateInfo = contentsServices.saveContentObj(contentsTbl);
			ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstInfoByCtId(updateInfo.getCtId(),"20");

			updateInfo.setWrkFileNm(contentsInstTbl.getWrkFileNm());

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

			clipSearchService.updateSearchMeta(updateInfo);

			view.addObject("contentsTbl", contentsTbl);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {

			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	

			return view;
		}catch (Exception e) {
			logger.error("error   " +e);
			logger.error("errorcode   " +e.getCause());
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
	@RequestMapping(value = "/contents/arrange/updateStoryBoardImgs.ssc", method = RequestMethod.POST)
	public ModelAndView updateStoryBoardImgs(@ModelAttribute("storyBoard") StoryBoard storyBoard)  {

		if(logger.isInfoEnabled()){
			logger.info("storyBoard.getCtId() : "+storyBoard.getCtId());
			logger.info("storyBoard.getDeleteImgs() : "+storyBoard.getDeleteImgs());
		}

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			//ctid로 메타정보를 조회한다
			contentsTbl = contentsServices.getContentObj(storyBoard.getCtId());

			if(contentsTbl == null)  {
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	

				return view;
			}

			//ctid로 고해상도 메타정보를 조회한다
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

			if(logger.isDebugEnabled()){
				logger.debug("filePath     "+filePath);
				logger.debug("fileNm       "+fileNm);
				logger.debug("substrFilepath "+substrFilepath);
			}

			String folder = null;
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);
			String text ="";

			folder = targetDrive + filePath + File.separator + substrFilepath;

			if(logger.isDebugEnabled()){
				logger.debug(folder);
			}

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
										if(logger.isDebugEnabled()){
											logger.debug("file delete sucess  "+imgs[k]);
										}
									} else {
										if(logger.isDebugEnabled()){
											logger.debug("file delete fail " +imgs[k]);
										}
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

			if(logger.isDebugEnabled()){
				logger.debug("text   "+text);
			}

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
	@RequestMapping(value = "/contents/arrange/insertCornerInfo.ssc", method = RequestMethod.POST)
	public ModelAndView insertCornerInfo(@ModelAttribute("storyBoard") StoryBoard storyBoard)  {

		if(logger.isInfoEnabled()){
			logger.info("insertCornerInfo.storyBoard.dividIds: "+storyBoard.getDividImgs());
			logger.info("insertCornerInfo.storyBoard.getCtId: "+storyBoard.getCtId());
			logger.info("insertCornerInfo.storyBoard.getCnNm: "+storyBoard.getCnNm());
			logger.info("insertCornerInfo.storyBoard.getCnCont: "+storyBoard.getCnCont());
		}

		ModelAndView view = new ModelAndView();

		try{

			CornerTbl cornerTbl = new CornerTbl();

			cornerTbl.setCtId(storyBoard.getCtId());
			
			//코너의 총 갯수를 조회한다
			Long count = cornerServices.count(cornerTbl);

			
			//총갯수가 0개이상이면 기존정보를 조회후 delete,insert를 0개이하면 신규등록을 한다.
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
			logger.error("error   " +e);
			logger.error("errorcode   " +e.getCause());
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
	@RequestMapping(value = {"/contents/arrange/fileExistYn.ssc"})
	public ModelAndView fileExistYn(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getFlPath() : "+search.getFlPath());
			logger.info("search.getTransFilenm() : "+search.getTransFilenm());
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

			if (logger.isDebugEnabled()) {
				logger.debug("source file : " + sourceFile);
				logger.debug("file exist : " + f.exists());
			}

			if (f.exists()) {

				if (logger.isDebugEnabled()) {
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
	 * 카테고리 트리 함수(extJs 전용)
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/arrange/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getNode()  : " + search.getNode() );
		}

		ModelAndView result = new ModelAndView();

		try{

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

			if(logger.isDebugEnabled()){
				logger.debug("result : "+result);
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
	 * 플레이어정보를 가져온다
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/getVideoPlayInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getVideoPlayInfo(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId: "+search.getCtId());
			logger.info("search.getKeyword: "+search.getKeyword());
		}

		ModelAndView view = new ModelAndView();
		String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);

		try{

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();

			//ctid로 메타정보를 조회한다
			contentsTbl = contentsServices.getContentObj(search.getCtId());

			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.
			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());	
			String filePath = contentsInstTbl.getFlPath();//ex): /201310/17
			String fileNm = contentsInstTbl.getWrkFileNm();//ex):로컬에서보낸거_20131017103032
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			File rowMeida = new File(targetDrive + filePath + File.separator + substrFilepath+".mp4");

			if(logger.isDebugEnabled()){
				logger.debug("rowMeida   "+rowMeida);
			}

			if(rowMeida.isFile()){
				view.addObject("mediaExistYn", "Y");	
			}else{
				view.addObject("mediaExistYn", "N");	
			}

			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);//ex): http://14.36.147.23:8002

			view.addObject("result", "Y");
			view.addObject("fullPath", apacheIp + filePath+ "/" + substrFilepath);

			if(logger.isDebugEnabled()){
				logger.debug("filePath : " + filePath);
				logger.debug("substrFilepath : " + substrFilepath);
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
	 * findcontentsSearchList 스토리보드정보를 가져온다.(extjs용)
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/getContentsStroyboardInfoForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsStroyboardInfoForExtJs(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() : "+search.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			//ExtJS는 하나의 JSON에 하나의 ROOT를 가지는 형식을 추천하고있으므로 해동 JSON 형식에 맞게 JSON을 구성한다.
			List<StoryBoard> infos= new ArrayList<StoryBoard>();
			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			//ctid로 메타정보를 조회한다
			contentsTbl = contentsServices.getContentObj(search.getCtId());
			
			if(contentsTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
			
				return view;
			}

			//ctid로 고해상도 메타정보를 조회한다
			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
			
			if(contentsInstTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
			
				return view;
			}
			
			
			String filePath = contentsInstTbl.getFlPath();//ex): /201310/17
			String fileNm = contentsInstTbl.getWrkFileNm();	//ex): 스토리지에서보낸거1_20131017103054
			String substrFileNm = fileNm.substring(fileNm.lastIndexOf('_')+1);

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			String folder = null;
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);	//ex): Z:
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);//ex): http://14.36.147.23:8002
			folder = targetDrive + filePath + "/" + substrFileNm;

			if(logger.isDebugEnabled()){
				logger.debug("folder : "+folder);
			}

			File outDir = new File(folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt,TXT"));
			String[] filesData = null;
			long duration = 0l;//변환된 duration값
			String ctLeng = null;
			ArrayList<Long> durationData = new ArrayList<Long>();	//변환된 duration값을 넣을 ArrayList
			ArrayList<String> ctLengData = new ArrayList<String>();
			
			
			//txt파일이 존재한다면 그파일을 읽어서 안의 duration 데이터를 ctleng으로 치환하여 리스트에 넣어준다
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
							//ex): 0,50,100,150,200,250,300,350
							ctLeng = Utility.changeDuration(Long.parseLong(filesData[j]));
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
	@RequestMapping(value = "/contents/arrange/getContentsSearchStroyboardCornerInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getContentsSearchStroyboardCornerInfo(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() : "+search.getCtId());
		}

		ModelAndView view = new ModelAndView();

		try{

			//ctid에 소속된 코너정보를 모두 조회한다
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
	 * 에피소드 정보를 조회하는 함수.
	 * 에피소드 id는 필수 입력값이며, 에피소드 명은 없어도 조회가 가능하다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/getEpisodeSearch.ssc", method = RequestMethod.GET)
	public ModelAndView getEpisodeSearchList(@ModelAttribute("search") Search search)  {
		
		ModelAndView result = new ModelAndView();
		
		try {
			
			//카테고리id에 소속된 에피소드 리스트를 모두 조회한다
			List<Episode> episodeTbls = episodeServices.episodeSearchList(search);

			if(episodeTbls.size() > 0 ){
				result.addObject("episodeTbls",episodeTbls);
				result.addObject("result", "Y");
				result.setViewName("jsonView");
			}else{
				result.addObject("result", "Y");
				result.addObject("episodeTbls",Collections.EMPTY_LIST);
				result.addObject("reason","검색된 에피소드정보가 없습니다.");
				result.setViewName("jsonView");
			}

			return result;
		} catch (ServiceException e) {
			result.addObject("result", "N");
			result.addObject("episodeTbls",Collections.EMPTY_LIST);
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");	
		
			return result;
		}
	}


	/**
	 * 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/findSclListForClf.ssc", method = RequestMethod.GET)
	public ModelAndView findSclListForClf(@ModelAttribute("search") Search search)  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){
			logger.debug("getClfCD "+search.getClfCd()); 
		}

		CodeTbl codeTbl = new CodeTbl();
		codeTbl.setClfCd( search.getClfCd());

		try {

			//clfcd에 소속된 코드 정보를 조회한다
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
	 * 코너정보 저장,대표화면 지정내용 저장, 샷삭제, 코너합친 내용모두 저장
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/updateStoryBoardInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateStoryBoardInfo(@ModelAttribute("storyBoard") StoryBoard storyBoard)  {

		if(logger.isInfoEnabled()){
			logger.info("insertCornerInfo.storyBoard.dividIds: "+storyBoard.getDividImgs());
			logger.info("insertCornerInfo.storyBoard.getCtId: "+storyBoard.getCtId());
			logger.info("insertCornerInfo.storyBoard.getCnNm: "+storyBoard.getCnNm());
			logger.info("insertCornerInfo.storyBoard.getCnCont: "+storyBoard.getCnCont());
			logger.info("insertCornerInfo.storyBoard.getRpImg: "+storyBoard.getRpImg());
			logger.info("insertCornerInfo.storyBoard.getShowImgs: "+storyBoard.getShowImgs()); 
		}

		ModelAndView view = new ModelAndView();

		try{
			//코너정보를 수정,저장,삭제 를 한번에 행한다
			cornerServices.updateAllStoryBoardInfo(storyBoard);
		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
		}catch (Exception e) {
			logger.error("error   " +e);
			logger.error("errorcode   " +e.getCause());
			view.addObject("result", "N");
			view.addObject("reason",messageSource.getMessage("error.015",null,null));
			view.setViewName("jsonView");	
			
			return view;
		}

		view.addObject("result", "Y");
		return view;
	}

	/**
	 * ct_id에 소속된 검색영상 모두를 조회한다.
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/findMediaList.ssc", method = RequestMethod.GET)
	public ModelAndView findMediaList(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+search.getCtId());
		}
		
		ModelAndView result = new ModelAndView();

		try {
			//ctid에 소속된 저해상도 메타정보를 조회한다
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
	@RequestMapping(value = "/contents/arrange/getMediaInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getMediaInfo(@ModelAttribute("meida") Media meida)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+meida.getCtId());
			logger.info("search.getCtiFmt() "+meida.getCtiFmt());
		}
		
		ModelAndView result = new ModelAndView();

		try {

			//각 저해상도 메타 정볼르 상세 조회한다
			Media mediaInfo = contentsInstServices.getMediaInfo(meida);
			
			if(logger.isDebugEnabled()){
				logger.debug("mediaInfo   getCtId "+mediaInfo.getCtId());
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
	 * 대표컨텐츠를 지정한다
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/updateRpContent.ssc", method = RequestMethod.POST)
	public ModelAndView updateRpContent(@ModelAttribute("meida") Media meida)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtiId() "+meida.getCtiId()); 
			logger.info("search.getCtId() "+meida.getCtId()); 
		}
		
		ModelAndView result = new ModelAndView();

		try {
			//대표컨텐츠를 지정한다
			contentsInstServices.updateRpContent(meida);

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
	 * 아카이브를 요청한다
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/insertArchiveContent.ssc", method = RequestMethod.POST)
	public ModelAndView insertArchiveContent(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+search.getCtId()); 
			logger.info("search.getUserId() "+search.getUserId()); 
		}
		
		ModelAndView result = new ModelAndView();


		try {
			//고해상도 정보를 조회한다
			ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstInfoByCtId(search.getCtId(),"10");

			if(logger.isDebugEnabled()){
				logger.debug("getctiId  "+ contentsInstTbl.getCtiId());
			}
			
			//이미 아카이브 요청이 된건인지에 대해서 확인한다.
			int count = archiveServices.checkArchive(contentsInstTbl.getCtiId());

			//아카이브 요청건이 한건도 없다면 아카이브 요청을 한다
			if(count == 0 ){
				archiveServices.add(contentsInstTbl.getCtiId(),search.getUserId());
				result.addObject("result","Y");
			}else{
				//이미 아카이브 요청이 된건에 대해서는 아카이브 요청을 하지 않는다.
				result.addObject("result","E");
				result.addObject("reason","이미 아카이브 요청된  건입니다.");
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


	/**
	 * 다운로드를 요청한다.
	 * @param notice 
	 * @return
	 */
	@RequestMapping(value = "/contents/arrange/insertDownloadContent.ssc", method = RequestMethod.POST)
	public ModelAndView insertDownloadContent(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getCtId() "+search.getCtId()); 
			logger.info("search.getUserId() "+search.getUserId()); 
		}

		ModelAndView result = new ModelAndView();

		try {
		
			//고해상도 정보를 조회한다
			ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstInfoByCtId(search.getCtId(),"10");

			//이미 아카이브 요청이 된건인지에 대해서 확인한다.
			int count = archiveServices.checkCompleteArchive(contentsInstTbl.getCtiId());

			//아카이브가 된건에 한해서 다운로드가 가능하다.
			if(count == 0){
				result.addObject("result","F");
				result.addObject("reason" , "아카이브가 되지 않은 건입니다.");
			}else{
				//다운로드 요청큐에 다운로드를 요청한다
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

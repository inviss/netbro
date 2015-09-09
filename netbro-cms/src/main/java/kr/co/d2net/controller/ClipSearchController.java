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
import kr.co.d2net.dto.Attatch;
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
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.search.index.SearchMetaIndex;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CornerServices;
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

		Search search = new Search();

		search.setDepth(0);
		search.setCategoryId(0);

		CategoryTbl categoryTbl  = new CategoryTbl();

		categoryTbl.setCategoryId(0);
		categoryTbl.setDepth(-1);
		try {
			List<CategoryTbl>category = categoryServices.findMainCategory(categoryTbl, search);
			map.addAttribute("codeId", new CodeId());
			map.addAttribute("categories", category);
			map.addAttribute("search", new Search());
			map.addAttribute("notice", new Notice());
			return map;

		} catch (ServiceException e) {
			map.addAttribute("codeId", new CodeId());
			map.addAttribute("categories", Collections.EMPTY_LIST);
			map.addAttribute("search", new Search());
			map.addAttribute("notice", new Notice());

			return map;
		}
	}


	/**
	 * 클립검색에서 검색엔진을 이용해 데이터를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/findClipSearchList.ssc", method = RequestMethod.GET)
	public ModelAndView findClipSearchList(@ModelAttribute("search") Search search, HttpServletRequest request)  {

		if (search.getPageNo() == null || search.getPageNo() == 0) {
			search.setPageNo(1);
		}

		if (search.getCategoryId() == null || search.getCategoryId() == 0) {
			search.setCategoryId(0);
		}
		 
		if(logger.isDebugEnabled()){
			logger.debug("search.getKeyword() : " + search.getKeyword());
			logger.debug("search.getCtId() : " + search.getCtId());
			logger.debug("search.getCatrgoryId() : " + search.getCategoryId());
			logger.debug("search.getStartDt() : " + search.getStartDt());
			logger.debug("search.getEndDt : " + search.getEndDt());
			logger.debug("search.getPageNo : " + search.getPageNo());
			logger.debug("search.getSearchTyp : " + search.getSearchTyp());
		}
		 
		search.setSearchTyp("list");
		ModelAndView view = new ModelAndView();
		SearchMetaIndex searchMeta = new SearchMetaIndex();

		Calendar cal = Calendar.getInstance();
		if(search.getStartDt() == null && search.getEndDt() == null){
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			search.setEndDt(cal.getTime());

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
			search.setPageNo(1);
		} else {
			cal.setTime(search.getEndDt());

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);

			search.setEndDt(cal.getTime());
		}

		try {
			CategoryTbl categoryTbl = null;
			List<SearchMeta> tempMetas = null;
			List<SearchMeta> searchMetas = new ArrayList<SearchMeta>();

			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			if(search.getCategoryId() != 0){

				
					categoryTbl = categoryServices.getCategoryObj(search.getCategoryId());
				
				search.setNodes(categoryTbl.getNodes());
			}

			// 기본 등록일 검색, 방송일 검색일경우 brdDd
			search.setDateGb(StringUtils.defaultIfBlank(search.getDateGb(), "regDt"));
			long total = searchMeta.count(search);
			if(logger.isDebugEnabled()){
				logger.debug("total : " + total);
			}
			view.addObject("total", total);

			if(search.getSearchTyp().equals("image")){

				search.setPageFrom((search.getPageNo()-1) * SearchControls.CLIP_IMAGE_COUNT);
				search.setPageSize(search.getPageNo() * SearchControls.CLIP_IMAGE_COUNT);

			}else{

				search.setPageFrom((search.getPageNo()-1) * SearchControls.CLIP_LIST_COUNT);
				search.setPageSize(search.getPageNo() * SearchControls.CLIP_LIST_COUNT);

			}

			tempMetas = searchMeta.query(search);
			//duration을 time code로
			for(SearchMeta info : tempMetas){

				String timeCode = Utility.changeDuration(info.getDuration());
				info.setCtLeng(timeCode);
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
			logger.error("###########################################################e   "+e.getStackTrace());
			if(e instanceof NoNodeAvailableException) {
				
				view.addObject("reason",messageSource.getMessage("error.016",null,null));
			} 
			view.addObject("result", "N");
			view.setViewName("jsonView");	
		} finally {
			searchMeta.closeConnection();
		}
		return view;
	}



	/**
	 * findClipSearchList에서 제목 및 이미지를 클릭했을때 기본정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchBasicInfo.ssc", method = RequestMethod.POST)
	public ModelAndView getClipSearchBasicInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();
		String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);
		try{
			if(logger.isDebugEnabled()){
				logger.debug("search.getCtId: "+search.getCtId());
				logger.debug("search.getKeyword: "+search.getKeyword());
			}

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();


			contentsTbl = contentsServices.getContentObj(search.getCtId());


			//고해상도 영상만 조회
			//ex): cti_fmt => 10x로 시작하는거.

			contentsInstTbl = contentsInstServices.getContentInstObj(search.getCtId());
			if(contentsInstTbl == null){
				view.addObject("result","N");
				view.addObject("reason",messageSource.getMessage("error.008", null, null));
				view.setViewName("jsonView");				

				return view;
			}

			//카테고리의 root 정보를 가져온다.
			Search optionview = new Search();

			optionview.setDepth(0);
			optionview.setCategoryId(0);
			List<CategoryTbl> categoryTbls = new ArrayList<CategoryTbl>();
			List<CategoryTbl> tempCategoryInfos = categoryServices.findMainCategory(optionview);

			for(CategoryTbl cateInfo : tempCategoryInfos){
				cateInfo.setEpisodeTbl(null);
				categoryTbls.add(cateInfo);
			}


			//ex): /201310/17
			String filePath = contentsInstTbl.getFlPath();
			//ex):로컬에서보낸거_20131017103032
			String fileNm = contentsInstTbl.getWrkFileNm();
			String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

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

			if(contentsTbl.getEpisodeId() != null)
				contentsTbl.setSegmentNm("");

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);


			File rowMeida = new File(targetDrive + filePath + File.separator + substrFilepath+".mp4");
			logger.debug("##########rowMeida   "+rowMeida);
			if(rowMeida.isFile()){
				view.addObject("mediaExistYn", "Y");	
			}else{
				view.addObject("mediaExistYn", "N");	
			}
			//ex): http://14.36.147.23:8002
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			view.addObject("search", search);
			view.addObject("contentsTbl", contentsTbl);
			view.addObject("categoryList", categoryTbls);
			view.addObject("result", "Y");
			//view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFilepath);
			view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFilepath);

			logger.debug("filePath : " + filePath);
			logger.debug("substrFilepath : " + substrFilepath);

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
	 * findClipSearchList 스토리보드정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchStroyboardInfo.ssc", method = RequestMethod.POST)
	public ModelAndView getClipSearchStroyboardInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();

		try{
			if(logger.isDebugEnabled()){
				logger.debug("search.getCtId() : "+search.getCtId());
			}

			ContentsTbl contentsTbl = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl();	

			contentsTbl = contentsServices.getContentObj(search.getCtId());
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
			//ex): /201310/17
			String filePath = contentsInstTbl.getFlPath();

			//ex): 스토리지에서보낸거1_20131017103054
			String fileNm = contentsInstTbl.getWrkFileNm();
			String substrFileNm = fileNm.substring(fileNm.lastIndexOf('_')+1);

			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);

			String folder = null;
			//ex): Z:
			String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);

			//ex): http://14.36.147.23:8002
			String apacheIp = messageSource.getMessage("apache.ip", null, Locale.KOREA);

			folder = targetDrive + filePath + File.separator + substrFileNm;

			if(logger.isDebugEnabled()){
				logger.debug("folder : "+folder);
			}

			File outDir = new File(folder);
			File[] files = outDir.listFiles(new ExtFileFilter("txt,TXT"));

			String[] filesData = null;
			//변환된 duration값
			long duration = 0l;
			String ctLeng = null;
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
						if(StringUtils.isNotBlank(filesData[j])) {
							//ex): 0,50,100,150,200,250,300,350
							ctLeng = Utility.changeDuration(Long.parseLong(filesData[j]));
							duration = Utility.changeDurationToSecond(Long.parseLong(filesData[j]));
							durationData.add(duration);
							ctLengData.add(ctLeng);
						}
					}
				}
			}
			List<CornerTbl> cornerTbls = cornerServices.findCornerList(search.getCtId());

			if(cornerTbls.size() == 0){
				CornerTbl info = new CornerTbl();
				info.setDuration(0L);
				info.setsDuration(0L);
				info.setCnNm("코너제목");
				info.setCnCont("코너내용");
				cornerTbls.add(info);
			}
			//스토리보드 txt파일data
			view.addObject("storyboard", filesData);
			view.addObject("durationData", durationData);
			view.addObject("ctLengData", ctLengData);
			view.addObject("cornerTbls", cornerTbls);
			view.addObject("search", search);
			view.addObject("contentsTbl", contentsTbl);
			view.addObject("result", "Y");
			view.addObject("fullPath", apacheIp + filePath+ File.separator + substrFileNm);

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
	 * findClipSearchList 첨부파일정보를 가져온다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/clip/getClipSearchAttachInfo.ssc", method = RequestMethod.POST)
	public ModelAndView getClipSearchAttachInfo(@ModelAttribute("search") Search search,
			ModelMap map)  {

		ModelAndView view = new ModelAndView();

		try{

			ContentsTbl contentsTbl = contentsServices.getContentObj(search.getCtId());
			List<AttachTbl> attachTbl = attachServices.getAttachObj(search.getCtId());

			if(logger.isDebugEnabled()){
				logger.debug("search.getCtId: "+search.getCtId());
				logger.debug("search.getKeyword: "+search.getKeyword());	
			}

			if(contentsTbl == null){
				view.addObject("result", "N");
				view.addObject("reason", messageSource.getMessage("error.006", null, null));
				view.setViewName("jsonView");	
				return view;
			}
			contentsTbl.setSegmentTbl(null);
			contentsTbl.setContentsInst(null);
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
	 * 카테고리 트리 함수
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/clip/findCategoryList.ssc", method = RequestMethod.GET)
	public ModelAndView findCategroyTreeSearch(@ModelAttribute("search") Search search, HttpServletRequest request)  {
		ModelAndView result = new ModelAndView();
		try{
			logger.debug("######################search  " + search.getDepth() );
			logger.debug("######################search  " + search.getCategoryId());
			logger.debug("######################request  " + request.getParameter("categoryId") );
			if(search.getDepth() == null){
				search.setDepth(0);
				search.setCategoryId(0);
			}
			List<CategoryTbl> tempCategoryTbls = categoryServices.findMainCategory(search);
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
			if(categoryTbls.size() !=0){
				result.addObject("categoryTbls", categoryTbls);
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
	 * 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/clip/findSclListForClf.ssc", method = RequestMethod.POST)
	public ModelAndView findSclListForClf(@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){

			logger.debug("getClfCD "+codeId.getClfCD());

		}

		CodeTbl codeTbl = new CodeTbl();

		codeTbl.setClfCd(codeId.getClfCD());

		try {

			List<CodeTbl> codeInfos  =  codeServices.findSclListForClf(codeTbl);

			result.addObject("codeInfos",codeInfos);
			result.addObject("result","Y");
			result.setViewName("jsonView");
			logger.debug("result  "+result);
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
	
/**
 * 오류등록함수
 * @param search
 * @return
 */
@RequestMapping(value = "/clip/updateErrorArrange.ssc", method = RequestMethod.POST)
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
 * @param clfCd
 * @param sclCd
 * @return
 */
@RequestMapping(value = "/clip/findNoticeList.ssc", method = RequestMethod.POST)
public ModelAndView findNoticeList(@ModelAttribute("notice") Notice notice)  {

	ModelAndView result = new ModelAndView();
	Date today = new Date();
	Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 01);
		notice.setStartDd(cal.getTime());
	if(logger.isDebugEnabled()){

		logger.debug("startDd "+notice.getStartDd());

	}

	

	try {

		List<NoticeTbl> noticeInfos  =  noticeServices.findAfterLoginPopUpNoticeList(notice);

		result.addObject("noticeInfos",noticeInfos);
		result.addObject("result","Y");
		result.setViewName("jsonView");
		logger.debug("result  "+result);
		return result;
	}  catch (ServiceException e) {
		result.addObject("result","N");
		result.addObject("reason",e.getMessage());
		result.setViewName("jsonView");

		return result;
	}


}



}

package kr.co.d2net.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.DownloadTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Archive;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Download;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.DownloadServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 공지사항관련 제어 함수가 들어있는 class
 * @author asura
 *
 */
@Controller
public class ArchiveController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ArchiveServices archiveServices;

	@Autowired
	private DownloadServices downloadServices;

	/**
	 * 코드 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/contents/archive/search.ssc", method = RequestMethod.GET)
	public ModelMap notice(ModelMap map)  {
		map.addAttribute("search", new Search());
		map.addAttribute("codeTbl", new CodeTbl());
		map.addAttribute("codeId", new CodeId());
		map.addAttribute("notice", new Notice());

		return map;
	}

	/**
	 * 카테고리 트리 함수(extJs 전용)
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/archive/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		try{
			if(logger.isInfoEnabled()){
				logger.info("search.getNode() : " + search.getNode());	
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
			
			if(logger.isInfoEnabled()){
				logger.info("result : "+result);
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
	 *아카이브 리스트 조회 함수
	 * 입력된 값을 받아서 리스트를 조회한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/archive/findArchiveList.ssc", method = RequestMethod.GET)
	public ModelAndView findArchiveList(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		
		if(logger.isInfoEnabled()){
			logger.info("search.getPageNo() : "  + search.getPageNo());
			logger.info("search.getStartDt() : "  + search.getStartDt());
			logger.info("search.getEndDt() : "  + search.getEndDt());
			logger.info("search.getSearchGb() : "  + search.getSearchGb());
			logger.info("search.getKeyword() : "  + search.getKeyword());
			logger.info("search.getCategoryId() : "  + search.getCategoryId());			
		}
		
		try{ 
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
				sDate = endFormat.parse(tempToday + "000000");
				eDate = endFormat.parse(tempBeforeToday + "235959");
			} catch (ParseException e) {
				logger.error("change date formate exception : " +e);
			}
			
			search.setStartDt(sDate);
			search.setEndDt(eDate);

			List<Archive> archiveInfos = archiveServices.findArchiveList(search);
			
			result.addObject("archiveInfos", archiveInfos);
			result.setViewName("jsonView");	
			
			return result;
		} catch(ServiceException e){
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			
			return result;
		}

	}


	/**
	 *아카이브 리스트 조회 함수
	 * 입력된 값을 받아서 리스트를 조회한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/archive/getArchiveInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getArchiveInfo(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		
		if(logger.isInfoEnabled()){
			logger.info("search.getCtiId()  "  + search.getCtiId());			
		}
		
		try{ 
			Archive archiveInfo = archiveServices.getArchiveObj(search.getCtiId());
			
			result.addObject("archiveInfo", archiveInfo);
			result.setViewName("jsonView");	
			
			return result;
		} catch(ServiceException e){
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			
			return result;
		}

	}


	/**
	 *다운로드 리스트 조회 함수
	 * 입력된 값을 받아서 리스트를 조회한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/archive/findDownloadList.ssc", method = RequestMethod.GET)
	public ModelAndView findDownloadList(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		if(logger.isInfoEnabled()){
			logger.info("search.getPageNo() : "  + search.getPageNo());
			logger.info("search.getStartDt() : "  + search.getStartDt());
			logger.info("search.getEndDt() : "  + search.getEndDt());
			logger.info("search.getSearchGb() : "  + search.getSearchGb());
			logger.info("search.getKeyword() : "  + search.getKeyword());
			logger.info("search.getCategoryId() : "  + search.getCategoryId());		
		}
		
		try{ 

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

			List<Download> downloadInfos = downloadServices.findDownloadList(search);
			result.addObject("downloadInfos", downloadInfos);
			result.setViewName("jsonView");	
			return result;
		
		} catch(ServiceException e){
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			return result;
		}

	}


	/**
	 *다운로드 상세 정보  조회 함수
	 * 입력된 값을 받아서 리스트를 조회한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/archive/getDownloadInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getDownloadInfo(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("search.getCtiId()  "  + search.getCtiId());			
		}
		
		try{ 

			Download download = downloadServices.getDownloadObj(search.getCtiId());
			result.addObject("download", download);
			result.setViewName("jsonView");	
			
			return result;
		} catch(ServiceException e){
		
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			return result;
		}
	}



	/**
	 * 아카이브 재전송 요청을 한다
	 * @param codeTbl
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/contents/archive/updateRetryArchvie.ssc", method = RequestMethod.POST)
	public ModelAndView updateRetryArchvie(@ModelAttribute("search") Search search)  {

		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("search.getSeq() : " + search.getSeq());	
		}
		
		try {
			ArchiveTbl archive = new ArchiveTbl();
			
			archive.setSeq(search.getSeq());
			archiveServices.updateRetryArchive(archive);
			
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","F");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}
	}



	/**
	 * 다운로드 재전송 요청을 한다
	 * @param codeTbl
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/contents/archive/updateRetryDownload.ssc", method = RequestMethod.POST)
	public ModelAndView updateRetryDownload(@ModelAttribute("search") Search search)  {

		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("search.getSeq()    "+ search.getSeq());
		}

		try {
			DownloadTbl downloadTbl = new DownloadTbl();
			downloadTbl.setSeq(search.getSeq());
			
			downloadServices.updateRetryDownload(downloadTbl);
			
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","F");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}
	}

}

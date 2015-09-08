package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Code;
import kr.co.d2net.dto.vo.Discard;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ClipSearchService;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.DisuseServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.RoleAuthServices;

import org.apache.commons.lang.StringUtils;
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
@Controller
public class DisuseController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private CodeServices codeServices;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private ClipSearchService clipSearchService;


	@Autowired
	private DisuseServices disuseServices;


	@RequestMapping(value="/contents/discard/search.ssc", method = RequestMethod.GET)
	public ModelMap code(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("discard", new Discard());

		return map;
	}




	@RequestMapping(value="/contents/discard/insertDisUse.ssc", method = RequestMethod.POST)
	public ModelAndView insertDisUse(@RequestParam(value = "ctIds", required = false) String ctIds) {

		if(logger.isInfoEnabled()){
			logger.info("ctIds : "+ctIds);
		}

		ModelAndView result = new ModelAndView();

		//검색엔진 사용어부를 properties에서 가져온후 검색엔진 사용여부에 따라 검색엔진 업데이틍부를 판단한다.
		String searchEngine = messageSource.getMessage("cms.searchEngine", null, Locale.KOREA);

		if(StringUtils.isNotBlank(ctIds)) {
			String[] aryCtId = null;

			if(ctIds.indexOf(",") > -1) {
				aryCtId = ctIds.split(",");
			} else {
				aryCtId = new String[] {ctIds};
			}

			for(String ctId : aryCtId) {
				if(StringUtils.isNotBlank(ctId)) {
					ContentsTbl contentsTbl;

					try {
						//ctid로 메타정보를 조회한다
						contentsTbl = contentsServices.getContentObj(Long.valueOf(ctId));

						//이미 폐기된 영상인지를 파악후 폐기가 되지 않은 영상에 한해서 폐기 절차 진행
						if(contentsTbl.getDelDd() == null){
							try {
								contentsTbl.setDelDd(new Date());
								contentsTbl.setUseYn("Y");
								contentsServices.saveContentObj(contentsTbl);
							} catch (ServiceException e) {
								result.addObject("result","N");
								result.addObject("errorCont","저장중 오류가 발생하였습니다.");
								result.setViewName("jsonView");
								return result;
							}

							//폐기등록을 한다
							disuseServices.insertDiscardInfo(Long.valueOf(ctId));

							//검색엔진 사용여부에 따라서 결과감을 검색엔진에 넘김다
							if(searchEngine.equals("Y")){
								clipSearchService.deleteData(Long.valueOf(ctId));								
							}

						}else{
							result.addObject("result","N");
							result.addObject("errorCont","이미 폐기된 영상입니다.");
							result.setViewName("jsonView");

							return result;
						}

					} catch (NumberFormatException e1) {
						logger.error("insertDisUse NumberFormatException"+e1);
					} catch (ServiceException e1) {
						logger.error("insertDisUse ServiceException"+e1);
					}
				}
			}
		}

		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}



	@RequestMapping(value="/contents/discard/cancleDisuse.ssc", method = RequestMethod.POST)
	public ModelAndView cancleDisUse(@ModelAttribute("discard") Discard discard) {

		if(logger.isInfoEnabled()){
			logger.info("discard.getDisuseNos() : "+discard.getDisuseNos());
		}

		ModelAndView result = new ModelAndView();

		//검색엔진 사용어부를 properties에서 가져온후 검색엔진 사용여부에 따라 검색엔진 업데이틍부를 판단한다.
		String searchEngine = messageSource.getMessage("cms.searchEngine", null, Locale.KOREA);

		
		//폐기취소 건에 대해서 조회후 폐기 취소처리를 하며, 만약 검색엔진을 사용한다면 해당 메타를 다시 검색엔진에 등록시켜준다
		if(StringUtils.isNotBlank(discard.getDisuseNos())) {
			String[] disuseNo = discard.getDisuseNos().split(",");
		
			for(int i = 0; i<disuseNo.length;i++){
				ContentsTbl contentsTbl  = new ContentsTbl();
				ContentsInstTbl contentsInstTbl = new ContentsInstTbl(); 

				try {
					DisuseInfoTbl info = disuseServices.getDisuseNoInfo(Long.valueOf(disuseNo[i]));

					contentsInstTbl =	contentsInstServices.getContentInstObj(info.getCtId());
					contentsTbl = contentsServices.getContentObj(info.getCtId());

				}  catch (ServiceException e) {
					logger.error("cancleDisUse ServiceException "+e);
				}
				contentsTbl.setDelDd(null);

				try {

					//검색엔진 사용여부에 따라서 결과감을 검색엔진에 넘김다
					if(searchEngine.equals("Y")){
						contentsServices.saveContentObj(contentsTbl);						
					}
					
					disuseServices.cancleDiscard(Long.valueOf(disuseNo[i]));
				} catch (ServiceException e1) {
					result.addObject("result","Y");
					result.setViewName("jsonView");

					return result;
				}

				//카테고리 정보검색
				CategoryTbl cateogryTbl;
			
				try {
					cateogryTbl = categoryServices.getCategoryObj(contentsTbl.getCategoryId());
					contentsTbl.setCategoryNm(cateogryTbl.getCategoryNm());
				} catch (ServiceException e) {
					logger.error("ServiceException "+e);
				}

				//에피소드 정보 검색
				EpisodeId id = new EpisodeId();
				EpisodeTbl episodeTbl = new EpisodeTbl();
			
				id.setCategoryId(contentsTbl.getCategoryId());
				id.setEpisodeId(contentsTbl.getEpisodeId());
				
				try {
					episodeTbl = episodeServices.getEpisodeObj(id);
				} catch (ServiceException e) {
					logger.equals("ServiceException "+e);
					result.addObject("result","N");
					result.setViewName("jsonView");

					return result;
				}
				contentsTbl.setEpisodeId(episodeTbl.getEpisodeId());
				contentsTbl.setEpisodeNm(episodeTbl.getEpisodeNm());

				//세그먼트 정보 검색
				contentsTbl.setSegmentId(1);
				clipSearchService.addData(contentsTbl, contentsInstTbl);
			}
		}

		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}

	/**
	 * 카테고리 트리 함수(extJs 전용)
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/discard/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getNode()  " + search.getNode() );
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
	 * 폐기된 영상의 리스트를 조회하는 함수
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/contents/discard/findDiscardInfoList.ssc", method = RequestMethod.GET)
	public ModelAndView findDiscardInfoList(@ModelAttribute("discard") Discard discard)  {
		
		if(logger.isInfoEnabled()){
			logger.info("discard.getStartDt()   :  "+discard.getStartDt());
			logger.info("discard.getEndDt()     :  "+discard.getEndDt());
			logger.info("discard.getDisuseClf() :  "+discard.getDisuseClf());
			logger.info("discard.getPageNo() :  "+discard.getPageNo());
		}
		
		ModelAndView result = new ModelAndView();

		try{
			//폐기등록된 영상에 대해서 조회한다
			List<Discard> discards= disuseServices.findDiscardInfoList(discard);
			//총조회건수를 구한다
			int totalCount = disuseServices.countDiscard(discard);

			result.addObject("discards", discards);
			result.addObject("result", "Y");
			result.addObject("total", totalCount);
			result.setViewName("jsonView");	
		
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
	@RequestMapping(value = "/contents/discard/findSclListForClf.ssc", method = RequestMethod.GET)
	public ModelAndView findSclListForClf(@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("getClfCD "+search.getClfCd()); 
		}

		ModelAndView result = new ModelAndView();
		CodeTbl codeTbl = new CodeTbl();

		codeTbl.setClfCd( search.getClfCd());

		try {

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
}

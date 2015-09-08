package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.exception.RequestParamException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.SegmentServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class AdminController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private SegmentServices segmentServices;

	@Autowired
	private ContentsServices contentsServices;

	/**
	 *  카테고리 시작시 불리는 함수 카테코리 화면 최초 로드시 필요한 코드값을 로드한다.
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/category/category.ssc", method = RequestMethod.GET)
	public ModelMap getCategorySearch(ModelMap map)  {


		map.addAttribute("search", new Search());
		map.addAttribute("categoryTbl", new CategoryTbl());
		map.addAttribute("newCategory", new Category());
		map.addAttribute("episodeTbl", new EpisodeTbl());
		map.addAttribute("episodeId", new EpisodeId());
		return map;
	}



	/**
	 * 카테고리 정보를 신규로 입력하는 함수 
	 * @param request
	 * @param categoryTbl
	 * @return
	 */


	@RequestMapping(value = "/admin/category/insertCategory.ssc", method = RequestMethod.POST)
	public ModelAndView insertNewCategory(@ModelAttribute("newCategory") Category newCategory)  {

		if(logger.isInfoEnabled()){
			logger.info("newCategory.getCategoryNm() : " + newCategory.getCategoryNm());
			logger.info("newCategory.getCategoryId() : " + newCategory.getCategoryId());
			logger.info("newCategory.getType() : " + newCategory.getType());
		}

		ModelAndView result = new ModelAndView();
		try{
			//카테고리 정보를 신규로 생성한다
			CategoryTbl categoryTbl = categoryServices.addCategory(newCategory);

			result.addObject(categoryTbl);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");

			return result;
		} 
	}


	/**
	 * 카테고리 정보를 업데이트 한다.
	 * @param categoryTbl
	 * @param categoryId
	 * @param categoryNm
	 * @return
	 */


	@RequestMapping(value = "/admin/category/updateCategory.ssc", method = RequestMethod.POST)
	public ModelAndView updateNewCategory(@ModelAttribute("newCategory") Category newCategory, @ModelAttribute("categoryTbl") CategoryTbl categoryTbl)  {

		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("aaaaa.getCategoryNm() : "+categoryTbl.getCategoryId());
			logger.info("newCategory.getCategoryNm() : "+newCategory.getCategoryNm());
			logger.info("newCategory.getCategoryId() : "+newCategory.getCategoryId());
			logger.info("newCategory.getDirection() : "+newCategory.getDirection());
		}

		Search search = new Search();
		CategoryTbl inputInfo = new CategoryTbl();

		try{
			//카테고리 id로 단일건을 조회한다
			inputInfo = categoryServices.getCategoryObj(newCategory.getCategoryId());

			CategoryTbl pre = new CategoryTbl();

			//inputInfo의 preparent의 값이 null이 아니면 최상위 노드 일경우 inputInfo의 정보를 상위 카테고리 정보로 사용하고
			//null이 아니면 inputInfo의 preparent의 값을 사용한다
			if(inputInfo.getPreParent() != null){

				pre = categoryServices.getCategoryObj(inputInfo.getPreParent());

				search.setCategoryId(pre.getCategoryId());
				search.setDepth(inputInfo.getDepth());
			}else{
				pre = inputInfo;
				search.setCategoryId(0);
				search.setDepth(0);
			}

			//depth와 카테고리id를 기준으로 카테고리리스트 정보를 조회한다
			List<CategoryTbl> subnodes =  categoryServices.findMainCategory(search);
 
			if(inputInfo.getOrderNum() == 1 && newCategory.getDirection().equals("up")){
				result.addObject("result","N");
				result.addObject("info","최상위 노드입니다. 더이상 상위로 이동시킬수 없습니다.");
				result.setViewName("jsonView");

				return result;
			}else if(subnodes.size() == inputInfo.getOrderNum() && newCategory.getDirection().equals("down")){

				result.addObject("result","N");
				result.addObject("info","최하위 노드입니다. 더이상 하위로 이동시킬수 없습니다.");
				result.setViewName("jsonView");

				return result;
			}

			CategoryTbl preInfo;
			//코너명만 변경할 시에는 orderNum의 값을 0으로 지정한다.		 
			newCategory.setOrderNum(0);
			preInfo = categoryServices.updateCategory(newCategory);


			CategoryTbl info = new CategoryTbl();

			if(preInfo.getPreParent() != null){
				info =categoryServices.getCategoryObj(preInfo.getPreParent());
			}else{
				info = preInfo;
			}

			info.setEpisodeTbl(null);

			result.addObject("result","Y");
			result.addObject("info",info);
			result.setViewName("jsonView");

			return result;
		}catch (ServiceException e) {
			result.addObject("result", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	
			return result;
		} catch (RequestParamException e) {
			result.addObject("result", "N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");	
			return result;
		}

	}



	/**
	 * 카테고리 정보를삭제 한다.
	 * @param categoryTbl
	 * @param categoryId
	 * @param categoryNm
	 * @return
	 */
	@RequestMapping(value = "/admin/category/deleteCategory.ssc", method = RequestMethod.POST)
	public ModelAndView deleteNewCategory(@ModelAttribute("Newcategroy") Category newCategory)  {

		if(logger.isInfoEnabled()){		
			logger.info("newCategory.getCategoryId()    :"+newCategory.getCategoryId());
		}

		ModelAndView result = new ModelAndView();
		Search episodeSearch = new Search();

		episodeSearch.setCategoryId(newCategory.getCategoryId());
		episodeSearch.setEpisodeNm("");
		String deleteYn ="";

		//카테고리 삭제전에 해당카테고리로 등록된 컨넨츠가 있는지 확인.
		List<Episode> episodeTbls;

		try {
			
			//카테고리id로 등록된 메타건수를 확인
			long categoryCount = contentsServices.findContentsForCategoryId(newCategory.getCategoryId());

			//20130924 하위카테고리가 존재하면 삭제불가 로직 추가.
			if(categoryCount > 0){
				result.addObject("result", "N");
				result.addObject("reason", "사용되고있는 카테고리입니다.");
				result.setViewName("jsonView");
				return result;
			}

			//카테고리삭제
			deleteYn = categoryServices.deleteNewCategory(newCategory);

			if(deleteYn.equals("Y")){
				result.addObject("result", "Y");
				result.setViewName("jsonView");
			}else{
				result.addObject("result", "N");
				result.addObject("reason", "하위노드가 존재하는  카테고리입니다.");
				result.setViewName("jsonView");
			}
			return result;
		} catch (ServiceException e) {
			result.addObject("result", "N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");	
			return result;
		}
	}


	/**
	 * 에피소드 정보를 조회하는 함수.
	 * 에피소드 id는 필수 입력값이며, 에피소드 명은 없어도 조회가 가능하다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/category/findEpisodeSearch.ssc", method = RequestMethod.GET)
	public ModelAndView findEpisodeSearchList(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){		
			logger.info("+search.getPageNo()   "+search.getPageNo());
			logger.info("search.getEpisodeNm()  "+search.getEpisodeNm());
			logger.info("search.getCategoryId()  "+search.getCategoryId());
		}

		try {
			if(search.getPageNo() == null){
				search.setPageNo(1);
			}
			
			//카테고리에 소속된 에피소드 정보조회
			List<Episode> episodeTbls = episodeServices.episodeSearchList(search);
			
			if(episodeTbls.size() > 0 ){

				long total = episodeServices.allEpisodeCount(search);

				result.addObject("episodeTbls",episodeTbls);
				result.addObject("total",total);
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
	 * 에피소드 정보를 신규로 저장한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/insertEpisode.ssc", method = RequestMethod.POST)
	public ModelAndView insertEpisode(@ModelAttribute("episodeTbl") EpisodeTbl episodeTbl,@ModelAttribute("episodeId") EpisodeId id)  {

		ModelAndView result = new ModelAndView();
		String addResult = "";
		
		try{
			//에피소드 정보 신규 저장
			addResult = episodeServices.addEpisodeInfo(episodeTbl, id);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");
			return result;
		}
	}

	/**
	 * 에피소드 정보를 업데이트 한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/updateEpisode.ssc", method = RequestMethod.POST)
	public ModelAndView updateEpisode(@ModelAttribute("episodeTbl") EpisodeTbl episodeTbl,@ModelAttribute("episodeId") EpisodeId id)  {
		ModelAndView result = new ModelAndView();

		EpisodeTbl updateEpisode = new EpisodeTbl();

		//저장하기전에 에피소드의 정보를 조회하여beans에 넣는다.
		try {
			//기존 에피소드 정보 조회
			updateEpisode = episodeServices.getEpisodeObj(id);

			//수정할 부분을 beans에 새로 넣는다.
			if(updateEpisode != null){

				updateEpisode.setEpisodeNm(episodeTbl.getEpisodeNm());

				//에피소드 정보 수정
				episodeServices.add(updateEpisode);

				result.addObject("result","Y");
				result.setViewName("jsonView");

			}else{

				result.addObject("result","N");
				result.addObject("reason", "수정할 대상이 없습니다.");
				result.setViewName("jsonView");

			}
			return result;
		} catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");
			return result;
		}

	}

	/**
	 * 에피소드를 삭제 한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/deleteEpisode.ssc", method = RequestMethod.POST)
	public ModelAndView deleteEpisode(@ModelAttribute("episodeId") EpisodeId id)  {

		ModelAndView result = new ModelAndView(); 
		EpisodeTbl episodeTbl = new EpisodeTbl();

		episodeTbl.setId(id);
		
		try {
			//카테고리id, 에피소드id로 등록된 컨텐츠 정보 조회
			Long count = contentsServices.findContentsForEpisode(id);

			if(count == 0){
				episodeServices.deleteEpisodeInfo(episodeTbl);

				result.addObject("result","Y");
				result.setViewName("jsonView");
			}else{
				result.addObject("result","N");
				result.addObject("reason","해당 에피소드id로 등록되어있는 컨텐츠가 존재합니다. 삭제하시려면 등록된 컨텐츠를 모두 해지시켜주싶시오");
				result.setViewName("jsonView");
			}
			return result;

		} catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");
			return result;
		}


	}

	/**
	 * 세그먼트 정보를 조회한다.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/category/SegmentSearch.ssc", method = RequestMethod.GET)
	public ModelAndView getSegmentSearchList(@ModelAttribute("search") Search search, ModelMap map)  {

		ModelAndView result = new ModelAndView();
		try {

			List<SegmentTbl> segmentTbls = segmentServices.segmentSearchList(search);

			result.addObject("segmentTbls",segmentTbls);
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");
			return result;
		}
	}

	/**
	 * 세그먼트 정보를 저장한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/insertSegment.ssc", method = RequestMethod.POST)
	public ModelAndView insertSegment(HttpServletRequest request,@ModelAttribute("search") Search search) {

		ModelAndView result = new ModelAndView();

	/*
	 * 세그먼트 기능 사용하지 않음
	 * */
		result.addObject("result","Y");
		result.setViewName("jsonView");
		return result;
	}

	/**
	 * 세그먼트 정보를 저장한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/updateSegmentssc", method = RequestMethod.POST)
	public ModelAndView updateSegment(HttpServletRequest request,@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();

		/*
		 * 세그먼트 기능 사용하지 않음
		 * */
		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}

	/**
	 * 세그먼트 정보를 삭제 한다.
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/deleteSegment.ssc", method = RequestMethod.POST)
	public ModelAndView deleteSegment(HttpServletRequest request,@ModelAttribute("search") Search search) {
		ModelAndView result = new ModelAndView();

		/*
		 * 세그먼트 기능 사용하지 않음
		 * */
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
	@RequestMapping(value="/admin/category/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		try{
			
			if(logger.isInfoEnabled()){
				logger.info("search.getNode()  " + search.getNode() );
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

			if(logger.isInfoEnabled()){
				logger.info("size() : "+categoryTbls.size());
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
	 * 카테고리 순서정보를 변경하다.
	 * @param categoryTbl
	 * @param categoryId
	 * @param categoryNm
	 * @return
	 */


	@RequestMapping(value = "/admin/category/updateChangeOrder.ssc", method = RequestMethod.POST)
	public ModelAndView updateChangeOrder(
			@RequestParam("categoryIds") List<Integer> categoryList, @RequestParam("parentsIds") List<String> parentsIds, @RequestParam("depths") List<Integer> depths)  {

		ModelAndView result = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("categoryList : "+categoryList.size()+", parentsIds: "+parentsIds.size()+", depths: "+depths.size());
		}
		
		//각각의 정보를 배열에 담는다.
		List<Category> categorys = new ArrayList<Category>();
		
		int len = categoryList.size();
		//배열에 담은 카테고리 정보를 list로 변환하여 담는다.
		for(int i = 0; i < len; i++){
			
			Category category = new Category();
			category.setCategoryId(categoryList.get(i));
			//extjs의 경우 root가 total이므로 null으로 치환해준다.
			if(parentsIds.get(i).equals("total")){
				category.setParents(null);
			}else{
				category.setParents(Integer.valueOf(parentsIds.get(i)));
			}
			
			category.setDepth(depths.get(i));
			
			categorys.add(category);
			
		}
		
		String outCome = categoryServices.updateChangeOrder(categorys);
		logger.debug("return value");
		result.addObject("result",outCome); 
		result.setViewName("jsonView");

		return result;
	}
	
	
}

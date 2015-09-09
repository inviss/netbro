package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.exception.DataNotFoundException;
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
	 * 카테고리 트리 함수
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/category/findCategoryList.ssc", method = RequestMethod.GET)
	public ModelAndView findCategroyTreeSearch(@ModelAttribute("search") Search search)  {

		ModelAndView result = new ModelAndView();
		try{

			logger.debug("search.getDepth()       : "+search.getDepth());
			logger.debug("search.getCategoryId()  : "+search.getCategoryId());
			CategoryTbl info = new CategoryTbl();

			if(search.getCategoryId() != 0){

				info = categoryServices.getCategoryObj(search.getCategoryId());

				//만약 search의 depth-1의 값이 카테고리의 depth값과 동일하지 않는다면 search의 depth값을 +1로 만들어준다
				//하위노드 위치변경시 트리노드 변경을 위하여.
				if(info.getDepth() != (search.getDepth()-1)){

					search.setDepth(info.getDepth()+1);

				}

			}else{

				info.setCategoryId(0);
				info.setDepth(-1);
				info.setPreParent(0);

			}

			List<CategoryTbl> TempCategorys = categoryServices.findMainCategory(info,search);

			List<Category> categoryTbls = new ArrayList<Category>();

			if(logger.isDebugEnabled()){

				logger.debug("size()   "+TempCategorys.size());

			}

			for(CategoryTbl addEpisodeTbl : TempCategorys){

				Category category = new Category();
				category.setCategoryId(addEpisodeTbl.getCategoryId());
				category.setCategoryNm(addEpisodeTbl.getCategoryNm());
				category.setDepth(addEpisodeTbl.getDepth());
				category.setNodes(addEpisodeTbl.getNodes());
				category.setOrderNum(addEpisodeTbl.getOrderNum());
				category.setPreParent(addEpisodeTbl.getPreParent());

				List<CategoryTbl> results;

				results = categoryServices.findSubNodesList(category.getNodes());


				if(results.size() >= 1){

					category.setFinalYn("N");

				}else{

					category.setFinalYn("Y");

				}

				if(logger.isDebugEnabled()){

					logger.debug("newCategory.getFinalYn()  : " +category.getFinalYn());

				}

				categoryTbls.add(category);

			}

			if(categoryTbls.size() != 0){
				result.addObject("result", "Y");
				result.addObject("categoryTbls", categoryTbls);
				result.setViewName("jsonView");

			}else{
				result.addObject("result", "N");
				result.addObject("categoryTbls", "N");
				result.setViewName("jsonView");	

			}

			if(logger.isDebugEnabled()){
				logger.debug("result   "+result);
			}
		} catch (ServiceException e) {
			result.addObject("result", "N");
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getMessage());
			result.setViewName("jsonView");	
			return result;
		}
		return result;
	}




	/**
	 * 카테고리 정보를 신규로 입력하는 함수 
	 * @param request
	 * @param categoryTbl
	 * @return
	 */


	@RequestMapping(value = "/admin/category/insertCategory.ssc", method = RequestMethod.POST)
	public ModelAndView insertNewCategory(@ModelAttribute("newCategory") Category newCategory)  {

		ModelAndView result = new ModelAndView();
		try{
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
	public ModelAndView updateNewCategory(@ModelAttribute("newCategory") Category newCategory)  {

		ModelAndView result = new ModelAndView();
		if(logger.isDebugEnabled()){
			logger.debug("newCategory.getCategoryNm()  :  "+newCategory.getCategoryNm());
			logger.debug("newCategory.getCategoryId()  :  "+newCategory.getCategoryId());
			logger.debug("newCategory.getDirection()   :  "+newCategory.getDirection());
		}

		Search search = new Search();

		CategoryTbl inputInfo = new CategoryTbl();

		try{
			inputInfo = categoryServices.getCategoryObj(newCategory.getCategoryId());



			CategoryTbl pre = new CategoryTbl();

			//최상위 노드 일경우 inputInfo의 정보를 상위 카테고리 정보로 사용한다.
			if(inputInfo.getPreParent() != null){

				pre = categoryServices.getCategoryObj(inputInfo.getPreParent());

				search.setCategoryId(pre.getCategoryId());
				search.setDepth(inputInfo.getDepth());
			}else{
				pre = inputInfo;
				search.setCategoryId(0);
				search.setDepth(0);
			}
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
			logger.error("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe"+  e.getMessage());
			logger.error( "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" +e.getCause());
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

		ModelAndView result = new ModelAndView();
		Search episodeSearch = new Search();
		episodeSearch.setCategoryId(newCategory.getCategoryId());
		episodeSearch.setEpisodeNm("");
		String deleteYn ="";
		//카테고리 삭제전에 해당 카테고리에 소속된 에피소드가 존재하는지 확인
		List<EpisodeTbl> episodeTbls;
		try {
			episodeTbls = episodeServices.episodeSearchList(episodeSearch);
			//20130924 하위카테고리가 존재하면 삭제불가 로직 추가.
			if(episodeTbls.size() > 0){
				result.addObject("result", "N");
				result.addObject("reason", "하위 에피소드 정보가 존재합니다.");
				result.setViewName("jsonView");
				return result;
			}
		

			deleteYn = categoryServices.deleteNewCategory(newCategory);


			result.addObject("result", "Y");
			result.setViewName("jsonView");

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
	@RequestMapping(value = "/admin/category/getEpisodeSearch.ssc", method = RequestMethod.GET)
	public ModelAndView getEpisodeSearchList(@ModelAttribute("search") Search search, ModelMap map)  {
		ModelAndView result = new ModelAndView();
		try {

			List<EpisodeTbl> episodeTbls = episodeServices.episodeSearchList(search);
			if(episodeTbls.size() > 0 ){

				for(EpisodeTbl episodeTbl : episodeTbls){

					episodeTbl.setCategoryTbl(null);
					episodeTbl.setSegmentTbl(null);

				}

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
			updateEpisode = episodeServices.episodeInfo(id);

			//수정할 부분을 beans에 새로 넣는다.
			if(updateEpisode != null){

				updateEpisode.setEpisodeNm(episodeTbl.getEpisodeNm());

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
			logger.debug("result   "+result);

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
		Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
		String segmentNm = request.getParameter("segmentNm");
			Integer maxEpisodeId = episodeServices.MaxCount(categoryId);

		EpisodeTbl episodeTbl = new EpisodeTbl();
		EpisodeId id = new EpisodeId();
		id.setCategoryId(categoryId);
		id.setEpisodeId(maxEpisodeId+1);
		episodeTbl.setId(id);
		episodeTbl.setEpisodeNm(segmentNm);
		episodeTbl.setRegDt(new Date());
		episodeTbl.setUseYn("Y");
		episodeServices.add(episodeTbl);
		 */
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
		/*	Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
		Integer episodeId = Integer.parseInt(request.getParameter("episodeId"));
		String episodeNm = request.getParameter("episodeNm");
		EpisodeId id = new EpisodeId();
		EpisodeTbl episodeTbl = new EpisodeTbl();
		id.setCategoryId(categoryId);
		id.setEpisodeId(episodeId);
		episodeTbl = episodeServices.episodeInfo(id);
		episodeTbl.setEpisodeNm(episodeNm);
		episodeServices.add(episodeTbl);*/
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
		/*Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
		Integer episodeId = Integer.parseInt(request.getParameter("episodeId"));
		logger.debug("i`m here1111111111111111111");
		EpisodeId id = new EpisodeId();
		EpisodeTbl episodeTbl = new EpisodeTbl();
		id.setCategoryId(categoryId);
		id.setEpisodeId(episodeId);
		episodeTbl.setId(id);
		 episodeServices.delete(episodeTbl);*/
		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}
}

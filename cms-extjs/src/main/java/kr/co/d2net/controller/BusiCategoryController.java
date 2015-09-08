package kr.co.d2net.controller;

import java.util.Collections;
import java.util.List;

import kr.co.d2net.dao.BusiPartnerCategoryDao;
import kr.co.d2net.dao.BusiPartnerDao;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.BusiPartner;
import kr.co.d2net.dto.vo.BusiPartnerCategory;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.BusiPartnerCategoryServices;
import kr.co.d2net.service.BusiPartnerServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.ProBusiServices;
import kr.co.d2net.service.ProFlServices;
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


/**
 * 
 * @author Administrator
 *
 */
@Controller
public class BusiCategoryController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private SegmentServices segmentServices;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private BusiPartnerServices busiPartnerServices;

	@Autowired
	private BusiPartnerDao busiPartnerDao;

	@Autowired
	private BusiPartnerCategoryDao busiPartnerCategoryDao;

	@Autowired
	private ProBusiServices proBusiServices;

	@Autowired
	private ProFlServices proFlServices;

	@Autowired
	private BusiPartnerCategoryServices busiPartnerCategoryServices;

	/**
	 *  카테고리 시작시 불리는 함수 카테코리 화면 최초 로드시 필요한 코드값을 로드한다.
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/busicategory/busicategory.ssc", method = RequestMethod.GET)
	public ModelMap getCategorySearch(ModelMap map)  {

		map.addAttribute("search", new Search());
		map.addAttribute("categoryTbl", new CategoryTbl());
		map.addAttribute("newCategory", new Category());
		map.addAttribute("episodeTbl", new EpisodeTbl());
		map.addAttribute("episodeId", new EpisodeId());
		map.addAttribute("busiPartnerTbl", new BusiPartnerTbl());
		return map;
	}

	
	/**
	 * 비지니스 카테고리의 리스트를 조회한다.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/busicategory/findBusiCategoryList.ssc", method = RequestMethod.GET)
	public ModelAndView findBusiCategoryList(@ModelAttribute("busiPartnerTbl") BusiPartnerTbl partnerTbl,@ModelAttribute("categoryTbl") CategoryTbl categoryTbl,@ModelAttribute("search") Search search)  {
		ModelAndView result = new ModelAndView();
		try {
			if(logger.isInfoEnabled()){
				logger.info("categoryTbl.getCategoryId() : " + categoryTbl.getCategoryId());	
			}
			//비지니스파트너 리스트를 조회하는 함수.
			List<BusiPartner> busiPartners =  busiPartnerServices.findBusiPartnerList();
			//비지니스파트너 리스트 클릭시 카테고리에 맵핑된 비지니스 파트너 ID 조회하는 함수.
			//List<BusiPartnerCategory> busiPartnerCategories  = busiPartnerCategoryServices.getBusiPartnerInfo(partnerTbl, categoryTbl, search);
			List<BusiPartnerCategory> findBusiPartnerInfo  = busiPartnerCategoryServices.findBusiPartnerInfo(partnerTbl, categoryTbl, search);
			//비지니스파트너ID에 맵핑된 프로파일 네임을 조회하는 함수(UI에서 프로파일 네임을 표시하기 위함).
			List<BusiPartnerCategory> findBusiPartnerMappingProfileNm  = busiPartnerCategoryServices.findBusiPartnerMappingProfileNm(partnerTbl, categoryTbl, search);

			result.addObject("busiPartnerTbl",busiPartners);
			result.addObject("busiPartner",findBusiPartnerMappingProfileNm);
			result.addObject("busiPartnerCategories",findBusiPartnerInfo);
			result.addObject("result", "Y");
			result.setViewName("jsonView");

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
	 * 카테고리맵핑에서 정보를 저장하는 method.
	 * @param partnerTbl
	 * @param categoryTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/busicategory/saveBusiCategoryInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveBusiCategoryInfo(@ModelAttribute("busiPartnerTbl") BusiPartnerTbl partnerTbl,@ModelAttribute("categoryTbl") CategoryTbl categoryTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("partnerTbl.getBusiPartnerId() : " + partnerTbl.getTmpBusiPartnerId());
			logger.info("categoryTbl.getCategoryId() : " + categoryTbl.getCategoryId());
		}

		try {
			//기존 클릭된 정보를 지운다.
			busiPartnerCategoryServices.deleteBusiInfo(partnerTbl, categoryTbl, search);
			//카테고리 맵핑된 정보를 저장한다.
			busiPartnerCategoryServices.saveBusiCategoryPartner(partnerTbl, categoryTbl);
			
			view.setViewName("jsonView");
			view.addObject("result","Y");
			return view;
		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("episodeTbls",Collections.EMPTY_LIST);
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		}catch (Exception e){
			view.addObject("result", "N");
			view.addObject("episodeTbls",Collections.EMPTY_LIST);
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");	
			return view;
		}

	}


}

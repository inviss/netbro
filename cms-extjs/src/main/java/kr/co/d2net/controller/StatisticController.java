package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.StatisticsServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;

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
 * CMS사용자관리와 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */
@Controller
public class StatisticController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private UserServices userServices;

	@Autowired
	private AuthServices authServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private UserAuthServices userAuthServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MenuServices menuServices;

	@Autowired
	private StatisticsServices statisticsService;


	/*
	 * 기간별 통계 초기화
	 * */
	@RequestMapping(value="/statistic/statistic.ssc", method = RequestMethod.GET)
	public ModelMap getStatisticInfo(ModelMap map) {


		map.addAttribute("search", new Search());
		map.addAttribute("statistic", new Statistic());
		return map;
	}

	/*
	 * 연별별 통계 초기화
	 * */
	@RequestMapping(value="/statistic/year/statistic.ssc", method = RequestMethod.GET)
	public ModelMap getStatisticForYearInfo(ModelMap map) {


		map.addAttribute("search", new Search());
		map.addAttribute("statistic", new Statistic());
		return map;
	}

	/**
	 * 기간별 통계조회를 한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/statistic/findStatisticListForPeriod.ssc", method = RequestMethod.GET)

	public ModelAndView findStatisticListForPeriod(@ModelAttribute("search") Search search,@ModelAttribute("statistic") Statistic statistic )  {

		if(logger.isInfoEnabled()){
			logger.info("statisticsTbl getStartDD "+statistic.getStartDD());
			logger.info("statisticsTbl getEndDD "+statistic.getEndDD());
			logger.info("statisticsTbl getCategoryId "+statistic.getCategoryId());
			logger.info("search page "+search.getPageNo());
		}

		ModelAndView result = new ModelAndView();
		
		try{

			Calendar cal2 = Calendar.getInstance();

			cal2.set(Calendar.HOUR_OF_DAY, 23);
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 59);
			statistic.setEndDD(cal2.getTime());

			if(search.getPageNo()== null ){
				search.setPageNo(0);
			}

			//기간별 통계조회
			List<Statistic> infos  =  statisticsService.findStatisticsListForPeriod(statistic,search);

			long totalCount=0;

			//카테고리 id의 값이 0 즉 전체조회라면 카테고리의 총합을 구하고, 카테고리 id의 값이 존재한다면 1로 정해준다.
			if(statistic.getCategoryId() == 0){
				totalCount =categoryServices.count();
			}else{
				totalCount =1l;
			}

			result.addObject("totalCount",totalCount);
			result.addObject("result","Y");
			result.addObject("statisticsTbls",infos);
			result.addObject("pageSize",SearchControls.STATISTIC_GRAPH_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
			
			return result;
		}
	}

	/**
	 * 통계데이터에 저장되어있는 연도의 리스트를 출력한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value="/statistic/findYearList.ssc", method = RequestMethod.GET)

	public ModelAndView findYearList()  {

		ModelAndView result = new ModelAndView();		
		
		try {

			//연도리스트를 조회
			List<Statistic> infos  =  statisticsService.findYearList();

			//만약 contentsTbl에 등록된 영상이 없다면 올해의 년도를 넣어준다.
			if(infos.size() == 0){
				Statistic thisYear = new Statistic();
				Calendar cal = Calendar.getInstance();
				int year = cal.get(cal.YEAR);
				thisYear.setYearList(String.valueOf(year));
				infos.add(thisYear);
			}
			
			result.addObject("result","Y");
			result.addObject("statistics",infos);
			result.setViewName("jsonView");

			return result;
		} catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
			return result;
		}

	}



	/**
	 * 연별 통계조회를 한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value="/statistic/findStatisticListForYear.ssc", method = RequestMethod.GET)

	public ModelAndView findStatisticListForYear(@ModelAttribute("search") Search search,@ModelAttribute("statistic") Statistic statistic )  {

		if(logger.isInfoEnabled()){
			logger.info("statisticsTbl getYearList "+statistic.getYearList());
		}
		
		ModelAndView result = new ModelAndView();
		
		try{

			if(search.getPageNo()== null){
				search.setPageNo(0);
			}

			//연도별 통계 데이터 조회
			List<Statistic> infos  =  statisticsService.findStatisticsListForYear(statistic);

			int totalCount=0;

			result.addObject("totalCount",totalCount);
			result.addObject("statisticsTbls",infos);
			result.addObject("pageSize",SearchControls.STATISTIC_LIST_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
			return result;
		}

	}



	/**
	 * 연별 통계그래프에 쓰일 데이터를 조회한다.
	 * @param search
	 * @param statistic
	 * @return
	 */
	@RequestMapping(value = "/statistic/findStatisticListForYearForGraph.ssc", method = RequestMethod.GET)

	public ModelAndView findStatisticListForYearForGraph(@ModelAttribute("search") Search search,@ModelAttribute("statistic") Statistic statistic )  {

		if(logger.isInfoEnabled()){
			logger.info("statisticsTbl getYearList "+statistic.getYearList());
		}

		ModelAndView result = new ModelAndView();

		if(search.getPageNo() == null){
			search.setPageNo(0);
		}
		
		try{
			//그래프에 사용될 데이터를 조회
			List<Statistic> infos  =  statisticsService.findStatisticsListForYearForGraph(statistic);

			//String[] yearMonth={"01","02","03","04","05","06","07","08","09","10","11","12"};

			int totalCount=0;

			result.addObject("totalCount",totalCount);
			result.addObject("statisticsTbls",infos);
			result.addObject("pageSize",SearchControls.STATISTIC_GRAPH_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
		
			return result;
		}

	}



	/**
	 * 카테고리 트리 함수(extJs 전용)
	 * 입력된 값을 받아서 하위 카테고리 정보를 조회한다. 
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/statistic/findCategoryListForExtJs.ssc", method = RequestMethod.GET)
	public ModelAndView findCategoryListForExtJs(@ModelAttribute("search") Search search, HttpServletRequest request)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getNode() : " + search.getNode() );			
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

		} catch(ServiceException e){
			result.addObject("categoryTbls", "N");
			result.addObject("reason", e.getCause());
			result.setViewName("jsonView");	

			return result;
		}

		return result;
	}




	/**
	 * 상세조회를 한다.
	 * @param search
	 * @param statistic
	 * @return
	 */
	@RequestMapping(value = "/statistic/findStatisticListForDetail.ssc", method = RequestMethod.GET)

	public ModelAndView findStatisticListForDetail(@ModelAttribute("search") Search search,@ModelAttribute("statistic") Statistic statistic )  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){

			logger.debug("statisticsTbl getStartDD "+statistic.getStartDD());
			logger.debug("statisticsTbl getEndDD "+statistic.getEndDD());
			logger.debug("statisticsTbl getCategoryId "+statistic.getCategoryId());
			logger.debug("statisticsTbl getYearList "+statistic.getYearList());
			logger.debug("statisticsTbl getGubun "+statistic.getGubun());
			logger.debug("search getPageNo "+statistic.getPageNum());

		}
		try{

			//총조회 건 수조회
			int totalCount= statisticsService.totalCountStatisticDetailInfo(statistic);

			//상세 통계내역을 조회
			List<Statistic> infos  =  statisticsService.findStatisticDetailInfo(statistic,search);

			if(logger.isDebugEnabled()){
				logger.debug("totalCount  " + totalCount);
			}

			result.addObject("totalCount",totalCount);
			result.addObject("statistics",infos);
			result.addObject("pageSize",SearchControls.CODE_LIST_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch(ServiceException e){
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");
			return result;
		}
	}




}

package kr.co.d2net.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
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

	@RequestMapping(value="/statistic/statistic.ssc", method = RequestMethod.GET)
	public ModelMap getStatisticInfo(ModelMap map) {


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

		ModelAndView result = new ModelAndView();
try{
		if(logger.isDebugEnabled()){
			logger.debug("statisticsTbl getStartDD "+statistic.getStartDD());
			logger.debug("statisticsTbl getEndDD "+statistic.getEndDD());
			logger.debug("statisticsTbl getCategoryId "+statistic.getCategoryId());
			logger.debug("search page "+search.getPageNo());
		}
	
		Calendar cal2 = Calendar.getInstance();
		
		
			cal2.set(Calendar.HOUR_OF_DAY, 23);
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 59);
			statistic.setEndDD(cal2.getTime());
			
		
		if(search.getPageNo()== null ){

			search.setPageNo(0);

		}

		List<Statistic> infos  =  statisticsService.findStatisticsListForPeriod(statistic,search);

		long totalCount=0;

		//카테고리 id의 값이 0 즉 전체조회라면 카테고리의 총합을 구하고, 카테고리 id의 값이 존재한다면 1로 정해준다.
		if(statistic.getCategoryId() == 0){

			totalCount =categoryServices.count();

		}else{

			totalCount =1l;

		}

		logger.debug("#############totalCount   "+totalCount);
		
		
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
	result.addObject("years",infos);
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

		ModelAndView result = new ModelAndView();
try{
		if(logger.isDebugEnabled()){

			logger.debug("statisticsTbl getYearList "+statistic.getYearList());

		}

		if(search.getPageNo()== null){

			search.setPageNo(0);

		}

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

		ModelAndView result = new ModelAndView();

		logger.debug("statisticsTbl getYearList "+statistic.getYearList());

		if(search.getPageNo() == null){

			search.setPageNo(0);

		}
		try{
		List<Statistic> infos  =  statisticsService.findStatisticsListForYearForGraph(statistic);

		for(Statistic info : infos){

			logger.debug("infos " +info.getBeforeArrange());

		}

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


}

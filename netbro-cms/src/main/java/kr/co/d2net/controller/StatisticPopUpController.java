package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.CategoryTbl;
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

import org.hibernate.Hibernate;
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
public class StatisticPopUpController {

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
	
	@RequestMapping(value = "/statistic/popup/statistic.ssc", method = RequestMethod.GET)
	public ModelMap getStatisticInfo(ModelMap map)  {

		map.addAttribute("search", new Search());
		map.addAttribute("statistic", new Statistic());
		
		return map;
		
	}



	/**
	 * 상세조회를 한다.
	 * @param search
	 * @param statistic
	 * @return
	 */
	@RequestMapping(value = "/statistic/popup/findStatisticListForDetail.ssc", method = RequestMethod.GET)
	
	public ModelAndView findStatisticListForDetail(@ModelAttribute("search") Search search,@ModelAttribute("statistic") Statistic statistic )  {
		
		ModelAndView result = new ModelAndView();
		
		if(logger.isDebugEnabled()){
			
		logger.debug("statisticsTbl getStartDD "+statistic.getStartDD());
		logger.debug("statisticsTbl getEndDD "+statistic.getEndDD());
		logger.debug("statisticsTbl getCategoryId "+statistic.getCategoryId());
		logger.debug("statisticsTbl getYearList "+statistic.getYearList());
		logger.debug("statisticsTbl getGubun "+statistic.getGubun());
		logger.debug("search getPageNo "+search.getPageNo());
		
		}
		try{
		if(search.getPageNo() == null){
			
			search.setPageNo(0);
			
		}
		
		CategoryTbl categoryTbl = new CategoryTbl();
		
		categoryTbl = categoryServices.getCategoryObj(statistic.getCategoryId());
		if(categoryTbl == null) {
			result.addObject("result", "N");
			result.addObject("reason", messageSource.getMessage("error.009", null, null));
			result.setViewName("jsonView");	
			return result;
		}
		//Hibernate.initialize(categoryTbl.getEpisodeTbl());
		categoryTbl.setEpisodeTbl(null);
		int totalCount= statisticsService.totalCountStatisticDetailInfo(statistic);
		
		List<Statistic> infos  =  statisticsService.findStatisticDetailInfo(statistic,search);
		
		//categoryTbl.setEpisodeTbl(null);
		
	
		logger.debug("totalCount  " + totalCount);
		
		result.addObject("totalCount",totalCount);
		result.addObject("statistics",infos);
		result.addObject("categoryTbl",categoryTbl);
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

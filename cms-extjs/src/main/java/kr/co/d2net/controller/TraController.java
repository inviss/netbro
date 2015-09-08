package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Tra;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.TraServices;

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
 * 트렌스코딩과 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class TraController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private AuthServices authServices;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TraServices traServices;

	@Autowired
	private TraDao traDao;



	/**
	 * 트렌스코더 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/work/tra/tra.ssc", method = RequestMethod.GET)
	public ModelMap tra(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("traTbl", new TraTbl());

		return map;
	}



	/**
	 * 트랜스코딩 리스트정보를 가져온다.
	 * @param search
	 * @return
	 */

	@RequestMapping(value = "/tra/findTraList.ssc", method = RequestMethod.GET)
	public ModelAndView findTraList(@ModelAttribute("search") Search search) {

		if(logger.isInfoEnabled()){
			logger.info("search.getTraContentNm() : "+search.getTraContentNm());
			logger.info("search.getWorkStat()  : " +search.getWorkStat());
			logger.info("search.getStartDt()  : " +search.getStartDt());
			logger.info("search.getEndDt()  : " +search.getEndDt());
			logger.info("search.getCategoryId()  : " +search.getCategoryId());
			logger.info("pageNo: "+search.getPageNo());
			logger.info("setCategoryId: "+search.getCategoryId());
		}
		ModelAndView view = new ModelAndView();

		try{
			if (search.getPageNo() == null || search.getPageNo() == 0) {
				search.setPageNo(1);
			}

			if (search.getCategoryId() == null || search.getCategoryId() == 0) {
				search.setCategoryId(0);
			}

			CategoryTbl info = new CategoryTbl();

			if(search.getCategoryId() != 0){
				try {
					info = categoryServices.getCategoryObj(search.getCategoryId());

					if(info == null) {
						view.addObject("result", "N");
						view.addObject("reason", messageSource.getMessage("error.009", null, null));
						view.setViewName("jsonView");	
						return view;
					}
					search.setNodes(info.getNodes());

					if(logger.isDebugEnabled())
						logger.debug("search.setNodes : " + search.getNodes());

				} catch (ServiceException e) {
					view.addObject("result", "N");
					view.addObject("reason", e.getMessage());
					view.setViewName("jsonView");	
					return view;
				}
			}

			Integer totalCount = traServices.getTraCount(search);
			if(logger.isDebugEnabled())
				logger.debug("totalCount : " + totalCount);

			//tra의 리스트를 조회.
			List<Tra> traTbls = traServices.findTraInfos(search);
			
			//카테고리 selectbox 가져오기.
			List<CategoryTbl> categoryTbls = categoryServices.findAll("nodes");
			
			//hibernate session 문제 해결하기 윈한 방법.
			for(CategoryTbl categoryTbl : categoryTbls){
				categoryTbl.setEpisodeTbl(null);
			}

			view.addObject("categoryTbls",categoryTbls);
			view.addObject("traTbls", traTbls);
			view.addObject("search", search);
			view.addObject("totalCount",totalCount);
			view.addObject("pageSize",SearchControls.TRA_LIST_COUNT);
			view.addObject("result","Y");
			
			view.setViewName("jsonView");

			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			
			return view;
		}

	}




	/**
	 * 트랜스코딩 재요청을 한다.
	 * @param traTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/tra/recommandTraObj.ssc", method = RequestMethod.POST)
	public ModelAndView recommandTraObj(@ModelAttribute("traTbl") TraTbl traTbl,@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			traServices.updateTraJobStatus(traTbl);
			view.setViewName("jsonView");
			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

	}
}

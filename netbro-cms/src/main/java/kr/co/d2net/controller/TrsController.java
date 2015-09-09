package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.TrsDao;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.TrsServices;

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
 * 권한설정과 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class TrsController {

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
	private TrsServices trsServices;
	
	@Autowired
	private TrsDao trsDao;



	/**
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/work/trs/trs.ssc", method = RequestMethod.GET)
	public ModelMap trs(ModelMap map) {

		map.addAttribute("search", new Search());

		return map;
	}



	/**
	 * 권한별 리스트 기본정보를 가져온다.
	 * @param search
	 * @return
	 */

	@RequestMapping(value = "/tra/findTrsList.ssc", method = RequestMethod.GET)
	public ModelAndView findTrsList(@ModelAttribute("search") Search search) throws Exception {

		ModelAndView view = new ModelAndView();
		
		try{
			
			List<TrsTbl> traTbls = trsDao.findAll();

			view.addObject("trsTbls", traTbls);
			view.setViewName("jsonView");

		} catch (Exception e) {
			logger.error("authList", e);
		}
		return view;
	}

}

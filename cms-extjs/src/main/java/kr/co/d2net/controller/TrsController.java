package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.TrsDao;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Trs;
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
	 * 트렌스퍼 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
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

	@RequestMapping(value = "/trs/findTrsList.ssc", method = RequestMethod.GET)
	public ModelAndView findTrsList(@ModelAttribute("search") Search search) throws Exception {

		ModelAndView view = new ModelAndView();

		try{
			if (search.getPageNo() == null || search.getPageNo() == 0) {
				search.setPageNo(1);
			}
			
			if(logger.isInfoEnabled()){
				logger.info("search.getCategoryId() : " + search.getCategoryId());
			}

			//tra의 리스트를 조회.
			List<Trs> traTbls = trsServices.findTrsInfos(search);
			Integer totalCount = trsServices.getTrsCount(search);

			view.addObject("trsTbls", traTbls);
			view.addObject("totalCount",totalCount);
			view.addObject("pageSize",SearchControls.TRA_LIST_COUNT);
			view.setViewName("jsonView");

		} catch (Exception e) {
			logger.error("authList", e);
		}
		return view;
	}


	/**
	 * 컨텐츠 전송관리의 재요청을 하는 method.
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/trs/retryTrsObj.ssc", method = RequestMethod.POST)
	public ModelAndView retryTrsObj(@ModelAttribute("search") Search search) throws Exception {

		ModelAndView view = new ModelAndView();

		try{
			if(logger.isInfoEnabled()){
				logger.info("search.getTrsSeq() : " + search.getTrsSeq());
			}

			trsServices.retryTrsObj(search);
			view.addObject("result","Y");
			view.setViewName("jsonView");

		} catch (Exception e) {
			view.addObject("result","N");
			view.setViewName("jsonView");
			logger.error("retryTrsObj", e);
		}
		return view;
	}

}

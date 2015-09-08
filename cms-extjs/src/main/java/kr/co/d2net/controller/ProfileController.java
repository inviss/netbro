package kr.co.d2net.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Profile;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.ProFlServices;
import kr.co.d2net.service.RoleAuthServices;
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
 * 프로파일설정과 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class ProfileController {

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
	private ProFlServices proFlServices;

	@Autowired
	private UserServices userServices;


	/**
	 * 프로파일 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/profile/profile.ssc", method = RequestMethod.GET)
	public ModelMap proFile(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("profile", new ProFlTbl());

		return map;
	}


	/**
	 * 프로파일 리스트 정보를 가져온다.
	 * @param proFlTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/profile/findProfileList.ssc", method = RequestMethod.GET)
	public ModelAndView findProfileList(@ModelAttribute("profile") ProFlTbl proFlTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{
			if(search.getPageNo() == null){
				search.setPageNo(0);
			}

			//프로파일의 기본 정보를 조회.
			List<Profile>profiles = proFlServices.findProfileList();

			view.addObject("profileInfos", profiles);
			view.addObject("search", search);
			view.addObject("pageSize",SearchControls.USER_LIST_COUNT);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());		
			view.addObject("userInfos", Collections.EMPTY_LIST);
			view.addObject("search", search);
			view.addObject("totalCount",0);
			view.addObject("pageSize",SearchControls.USER_LIST_COUNT);
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}


	/**
	 * 프로파일리스트에서 클릭한 정보값을 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/profile/getProfileInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getProfileInfo(@ModelAttribute("profile") ProFlTbl proFlTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("proFlTbl.getProFlid() : " + proFlTbl.getProFlId());
		}

		try{
			//List<Profile> profile = proFlServices.getProfileInfo(proFlTbl.getProFlId());
			Profile profile = proFlServices.getProfileInfo(proFlTbl.getProFlId());

			view.addObject("result","Y");
			view.addObject("proFlTbl", profile);
			view.setViewName("jsonView");

			return view;
		} catch (ServiceException e) {
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			return view;
		}

	}


	/**
	 * 프로파일정보를 저장한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/profile/saveProfileInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveProfileInfo(@ModelAttribute("profile") ProFlTbl proFlTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("proFlTbl.getFlNameRule : " + proFlTbl.getFlNameRule());
			logger.info("proFlTbl.getFlNameRule : " + proFlTbl.getPriority());
		}

		try{
			proFlServices.add(proFlTbl);
			
			view.setViewName("jsonView");
			view.addObject("result","Y");
			
			return view;
		} catch (ServiceException e) {
			view.setViewName("jsonView");
			view.addObject("result","N");
			
			return view;
		}

	}

	/**
	 * 프로파일정보를 업데이트한다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/profile/updateProfileInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateProfileInfo(@ModelAttribute("profile") ProFlTbl proFlTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("proFlTbl.getFlNameRule : " + proFlTbl.getFlNameRule());
			logger.info("proFlTbl.getProFlId : " + proFlTbl.getProFlId());
		}

		ProFlTbl proFlTbl2 = new ProFlTbl();

		try{
			proFlTbl2 = proFlServices.getProFlObj(proFlTbl.getProFlId());

			//hibernate session 문제를 해결하기 위한것(null넣은거).
			proFlTbl2.setOpt(null);
			proFlTbl2.setProbusi(null);
			proFlTbl2.setFlNameRule(proFlTbl.getFlNameRule());
			proFlTbl2.setVdoBitRate(proFlTbl.getVdoBitRate());
			proFlTbl2.setVdoFS(proFlTbl.getVdoFS());
			proFlTbl2.setVdoSync(proFlTbl.getVdoSync());
			proFlTbl2.setProFlnm(proFlTbl.getProFlnm());
			proFlTbl2.setVdoCodec(proFlTbl.getVdoCodec());
			proFlTbl2.setServBit(proFlTbl.getServBit());
			proFlTbl2.setKeyFrame(proFlTbl.getKeyFrame());
			proFlTbl2.setExt(proFlTbl.getExt());
			proFlTbl2.setChanPriority(proFlTbl.getChanPriority());	
			proFlTbl2.setPriority(proFlTbl.getPriority());
			proFlTbl2.setUseYn(proFlTbl.getUseYn());
			proFlTbl2.setModrId(proFlTbl.getModrId());
			proFlTbl2.setModDt(new Date());

			proFlServices.add(proFlTbl2);

			view.setViewName("jsonView");
			view.addObject("result","Y");
			
			return view;

		} catch (ServiceException e) {
			view.setViewName("jsonView");
			view.addObject("result","N");
			
			return view;
		}

	}

}

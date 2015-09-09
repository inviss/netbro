package kr.co.d2net.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.RoleAuthServices;

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
public class AuthController {

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



	/**
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/auth/auth.ssc", method = RequestMethod.GET)
	public ModelMap userAuth(ModelMap map) {

		map.addAttribute("search", new Search());

		return map;
	}



	/**
	 * 권한별 리스트 기본정보를 가져온다.
	 * @param search
	 * @return
	 */

	@RequestMapping(value = "/admin/auth/findAuthList.ssc", method = RequestMethod.GET)
	public ModelAndView findAuthList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			List<AuthTbl> authTbls = authServices.findAll();

			for(AuthTbl authTbl : authTbls)
				if(authTbls != null){
					authTbl.setRoleAuth(null);
					authTbl.setUserAuth(null);
				}

			if(authTbls != null){

			}

			view.addObject("authTbl", authTbls);
			view.addObject("result","Y");
			view.setViewName("jsonView");

			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("authTbl", Collections.emptyList());
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");

			return view;
		}

	}


	/**
	 * 권한 기본정보를 가져온다
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/getAuthInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getAuthInfo(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		if(logger.isDebugEnabled()){
			logger.debug("search.getAuthId() : " + search.getAuthId());
		}

		try{
			
			AuthTbl authTbl = new AuthTbl();
			
			authTbl = authServices.getAuthObj(search.getAuthId());
			
			String roleAuth = "";

			//menuTbl에 menu id값은 2,3,4,5
			//ex): menuid =  2 클립검색, 3	컨텐츠관리, 4	작업관리, 5	통계 , 6 관리
			for(int i = 2; i<=13; i++){
				
				RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
				RoleAuthId roleAuthId = new RoleAuthId();

				roleAuthId.setAuthid(search.getAuthId());
				roleAuthId.setMenuId(i);
				roleAuthTbl.setId(roleAuthId);
				roleAuthTbl = roleAuthServices.getRoleAuthObj(roleAuthId);
				roleAuth += ","+roleAuthTbl.getControlGubun();


			}

			authTbl.setRoleAuth(null);
			authTbl.setUserAuth(null);

			view.addObject("result","Y");
			view.addObject("authTbl", authTbl);
			view.addObject("roleAuth",roleAuth.substring(1));
			view.setViewName("jsonView");
			return view;
			
		} catch (ServiceException e) {
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			return view;
		}
		
	}


	/**
	 * 권한정보를 저장한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/saveAuthInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveAuthInfo(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			authServices.saveRoleAuth(search);
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
	 * 권한정보를 업데이트한다
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/updateAuthInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateAuthInfo(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();
		try{

			//update를 할때 기존값을 지우고 난 후 insert하는 방식으로 update한다.
			roleAuthServices.deleteRoleAuth(search.getAuthId());
			authServices.updateRoleAuth(search);

			view.addObject("result","Y");
			view.setViewName("jsonView");
			return view;

		} catch (ServiceException e) {
			view.addObject("result","N");
			view.setViewName("jsonView");
			return view;
		}
		
	}


}

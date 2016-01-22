package kr.co.d2net.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Users;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;
import kr.co.d2net.utils.Utility;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.util.WebUtils;



/**
 * CMS사용자관리와 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */
@Controller
public class UserController {

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


	@RequestMapping(value="/login.ssc", method = RequestMethod.GET)
	public ModelMap userLogin(HttpServletRequest request, @ModelAttribute("search") Search search, ModelMap map) {

		return map;
	}

	/**
	 * 로그인시 UI에 alert창(id와비밀번호가 틀리면 UI에서 alert창 열리게 하는) 실행.
	 * @param request
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getUseYn.ssc", method = RequestMethod.POST)
	public ModelAndView getUseYn(HttpServletRequest request,@ModelAttribute("search") Search search, ModelMap map) {


		if(logger.isDebugEnabled()){
			logger.debug("search.getUserId() : " + search.getUserId());
			logger.debug("search.getUserPasswd() : " + search.getUserPasswd());
		}

		ModelAndView view = new ModelAndView();

		try {
			UserTbl user = userServices.getLoginUserInfoYn(search.getUserId(), Utility.encodeMD5(search.getUserPasswd()),"Y");

			if(user != null){
				view.addObject("user", "Y");
			}else{
				view.addObject("user", "N");
			}

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

		view.setViewName("jsonView");
		return view;
	}


	/**
	 * 로그인시 사용자 존재 여부 확인 및 접근 메뉴 조회 확인.
	 * @param request
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/userLoginAction.ssc", method = RequestMethod.POST)
	public String userLoginAction(HttpServletRequest request,@ModelAttribute("search") Search search, ModelMap map) {

		if(logger.isDebugEnabled()){
			logger.debug("search.getUserId() : " + search.getUserId());
			logger.debug("search.getUserPasswd() : " + search.getUserPasswd());
		}


		if(StringUtils.isBlank(search.getUserId()) || StringUtils.isBlank(search.getUserPasswd())) {
			return "redirect:/clip/search.ssc"; 
		}

		UserTbl user = null;

		Map<String, Object> params = new HashMap<String, Object>();      

		params.put("userId", search.getUserId());
		params.put("userPass", Utility.encodeMD5(search.getUserPasswd()));

		try {
			user = userServices.getLoginUserInfoYn(search.getUserId(), Utility.encodeMD5(search.getUserPasswd()),"Y");

			if(logger.isDebugEnabled())
				logger.debug("user : " + user);

			if(user != null){
				// 사용자 접근 가능메뉴 조회
				List<MenuTbl> menus =  menuServices.findUserMenus(params);				
				user.setMenus(menus);

				// 사용자의 첫번째 페이지로 이동
				//MenuTbl menu = menuServices.getFirstMenu(params);

				WebUtils.setSessionAttribute(request, "user", user);

				return "redirect:" + "/clip/search.ssc";

			}/*
			else{
				UserTbl check = userServices.getLoginUserInfo(search.getUserId(), Utility.encodeMD5(search.getUserPasswd()));

				if(check != null){
					user = userServices.getLoginUserInfoYn("guest", "","N");

					params.put("userId", "guest");
					List<MenuTbl> menus =  menuServices.findUserMenus(params);				
					user.setMenus(menus);

					WebUtils.setSessionAttribute(request, "user", user);

					return "redirect:/clip/search.ssc";

				}else{
					return "redirect:/clip/search.ssc"; 
				}
			}
			 */
		} catch (ServiceException e) {
			
			return "redirect:/clip/search.ssc";
		}
		return null;
	}



	/**
	 * 로그아웃 버튼 클릭시 세션 끊고 클립검색으로 이동.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginout.ssc", method = RequestMethod.POST)
	public String loginOut(HttpServletRequest request)  {

		HttpSession session = request.getSession();

		session.invalidate();

		return "redirect:/clip/search.ssc";
	}


	/**
	 * 사용자관리UI에서 역활데이터를 가져올때.
	 * @param search
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/user/user.ssc", method = RequestMethod.GET)
	public ModelMap userSearch(@ModelAttribute("search") Search search, ModelMap map)  {
		try {
			
			List<AuthTbl> authTbls = authServices.findAll();
			
			map.addAttribute("authTbl", authTbls);
			map.addAttribute("search", search);
			map.addAttribute("userTbl", new UserTbl());
			
			return map;
		} catch (ServiceException e) {
			map.addAttribute("authTbl", Collections.EMPTY_LIST);
			map.addAttribute("search",search);
			map.addAttribute("userTbl", new UserTbl());
			return map;
		}
	}


	/**
	 * 사용자정보를 save한다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/user/saveUserInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveUserInfo(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();
		try{

			//전체 user정보를 가져와 UI에서 사용자 중복체크
			List<UserTbl> userTbls = userServices.findAll();

			for(UserTbl userTbl : userTbls)
				if(userTbl != null){
					userTbl.setUserAuth(null);
				}

			UserTbl userTbl = new UserTbl();

			//userId,userPhone을 이용해 사용자 중복체크
			UserTbl getUserYN = userServices.getUserInfoYn(search.getUserId(),search.getUserPhone());

			if(getUserYN == null){

				if(logger.isDebugEnabled()){
					logger.debug("search.getUserNm() : "+search.getUserNm());
					logger.debug("search.getUserPhone() : "+search.getUserPhone());
					logger.debug("search.getUserPasswd() : "+search.getUserPasswd());
					logger.debug("search.getUserId() : "+search.getUserId());
					logger.debug("search.getAuthId() : "+search.getAuthId());
				}


				String tmpAuthId = search.getAuthId().toString();

				for(int i = 0; i<tmpAuthId.length(); i++){
					Integer authid = tmpAuthId.charAt(i)-48;
					search.setAuthId(authid);
					userServices.saveUserInfo(search);

				}

			}
			view.addObject("result", "Y");
			view.addObject("userTbl", userTbl);
			view.addObject("userTbls", userTbls);
			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());
			view.addObject("userTbl", new UserTbl());
			view.addObject("userTbls", Collections.EMPTY_LIST);
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}

	/**
	 * 입력받은 유저정보를 update한다.
	 * @param search
	 * @param userTbl
	 * @return
	 */
	@RequestMapping(value = "/admin/user/updateUserInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateUserInfo(@ModelAttribute("search") Search search, @ModelAttribute("user")UserTbl user)  {

		ModelAndView view = new ModelAndView();
		try{
			if(logger.isDebugEnabled()){
				logger.debug(user.getUserId());
			}

			UserTbl userTbl = userServices.getUserObj(search.getUserId());

			//DB의 passwd데이터와 UI에서의 입력값passwd데이터 비교.
			if(userTbl.getUserPass().equals(Utility.encodeMD5(search.getUserPasswd()))){
				//update를 할때 기존값을 지우고 난 후 insert하는 방식으로 update한다.
				userAuthServices.deleteUserAuth(search.getUserId());

				String tmpAuthId = search.getAuthId().toString();

				for(int i = 0; i<tmpAuthId.length(); i++){
					Integer authid = tmpAuthId.charAt(i)-48;
					search.setAuthId(authid);
					userServices.updateUserInfo(search, user);

				}


				//S = SUCCESS
				view.addObject("result", "S");

			}else{
				//F = FAIL
				view.addObject("result", "F");
			}

			view.setViewName("jsonView");

		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());			
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}

	@RequestMapping(value = "/admin/user/updatePasswd.ssc", method = RequestMethod.POST)
	public ModelAndView updatePasswd(@ModelAttribute("search") Search search, @ModelAttribute("user")UserTbl user)  {

		if(logger.isDebugEnabled()){
			logger.debug("search.getOldPasswd : " + search.getOldPasswd());
			logger.debug("search.getNewPasswd : " + search.getNewPasswd());

		}

		ModelAndView view = new ModelAndView();
		try{
			UserTbl userTbl = userServices.getUserObj(search.getUserId());

			//DB의 passwd데이터와 UI에서의 입력값passwd데이터 비교.
			if(userTbl.getUserPass().equals(Utility.encodeMD5(search.getOldPasswd()))){
				//기존비밀번호와 신규입력받은 비밀번호를 비교.
				if(search.getOldPasswd().equals(search.getNewPasswd())){
					//S = SUCCESS
					view.addObject("result", "E");
				}else{
					userTbl.setUserPass(Utility.encodeMD5(search.getNewPasswd()));
					userServices.add(userTbl);

					//S = SUCCESS
					view.addObject("result", "S");
				}
			}else{
				//F = FAIL
				view.addObject("result", "F");
			}
			view.setViewName("jsonView");
		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());			
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}


	/**
	 * 사용자리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/user/findUserList.ssc", method = RequestMethod.GET)
	public ModelAndView findUserList(@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if (search.getPageNo() == null || search.getPageNo() == 0) {
				search.setPageNo(1);
			}else{
				search.setPageNo(search.getPageNo()+1);
			}

			//User의 카운터 수를 조회.
			Long totalCount = userServices.getAllUserCount();
			//User의 기본 정보를 조회.
			List<Users> userInfos  =  userServices.findUserInfo(search);

			view.addObject("userInfos", userInfos);
			view.addObject("search", search);
			view.addObject("totalCount",totalCount);
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
	 * 사용자의 사용자기본정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/user/getUserInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getUserInfo(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			//User기본정보를 가져옴.
			//UserTbl userTbl = userServices.getUserObj(search.getUserId());
			List<Users> userTbl  = userServices.findUserAndAuthObj(search.getUserId());

			//사용자관리 역활 selected box.
			List<AuthTbl> authTbls = authServices.findAll();

			for(AuthTbl auth : authTbls)
				if(auth != null){
					auth.setRoleAuth(null);
					auth.setUserAuth(null);
				}

			view.addObject("userTbl", userTbl);
			view.addObject("authTbls", authTbls);
			view.setViewName("jsonView");
			view.addObject("result", "Y");
		} catch (ServiceException e) {
			view.addObject("result", "N");
			view.addObject("userTbl", new UserTbl());
			view.addObject("authTbls", Collections.EMPTY_LIST);
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}



	/**
	 * 사용자를 삭제한다.(useYn값을 이용해서 Y면 findUserList에 보이게 N이면 findUserList에 안보이게)
	 * 실제 데이터가 삭제되는것은 아님.
	 * @param search
	 * @param user
	 * @return

	@RequestMapping(value = "/admin/user/deleteUserInfo.ssc", method = RequestMethod.POST)
	public ModelMap deleteUserInfo(@ModelAttribute("search") Search search,@ModelAttribute("user")UserTbl user) {

		ModelMap view = new ModelMap();

		try{
			//USER_TBL의 useYn 값 업데이트.
			userServices.updateUserInfoYn(search, user);

			//역활 데이터 
			List<AuthTbl> authTbls = authServices.findAll();

			view.addAttribute("authTbl", authTbls);

		} catch (Exception e) {
			logger.error("getUserInfo", e);
		}
		return view;
	}
	 */

}
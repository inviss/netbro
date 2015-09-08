package kr.co.d2net.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Auth;
import kr.co.d2net.dto.vo.MenuTreeForExtJs;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.MenuServices;
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

	@Autowired
	private MenuServices menuServices;



	/**
	 * 권한 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
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
			List<Auth> authTbls = authServices.findAuthList();

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

		try{

			if(logger.isInfoEnabled())
				logger.info("search.getAuthId() : " + search.getAuthId());

			//Auth auth = authServices.getAuthInfo(Integer.parseInt(search.getAuthId()));
			Auth auth = authServices.getAuthInfo(Integer.parseInt(search.getAuthId()));

			//해당 권한값을 담는 변수.
			String roleAuth = "";

			int tmpMenuCount = (int) menuServices.findMenuCount();

			for(int i = 0; i < tmpMenuCount-1; i++){
				RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
				RoleAuthId roleAuthId = new RoleAuthId();

				roleAuthId.setAuthId(Integer.parseInt(search.getAuthId()));
				roleAuthId.setMenuId(i + 2);
				roleAuthTbl.setId(roleAuthId);
				roleAuthTbl = roleAuthServices.getRoleAuthObj(roleAuthId);
				roleAuth += ","+roleAuthTbl.getControlGubun();
			}

			view.addObject("result","Y");
			view.addObject("authTbl", auth);
			view.addObject("controlGubun",roleAuth.substring(1));
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
			roleAuthServices.deleteRoleAuth(Integer.parseInt(search.getAuthId()));
			authServices.updateRoleAuth(search);

			view.addObject("result","Y");
			view.setViewName("jsonView");
			return view;

		} catch (ServiceException e) {
			view.addObject("result","N");
			view.setViewName("jsonView");
			return view;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return view;
		}

	}


	/**
	 * 메뉴리스트를 조회하는 method.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/findMenuList.ssc", method = RequestMethod.GET)
	public ModelAndView findMenuList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			if(logger.isDebugEnabled()){
				logger.debug("search.getMenuId()   " + search.getMenuId());
				logger.debug("search.getUserId()   " + search.getUserId());
			}

			//menu를 조회하는 함수.
			List<Menus> menus = menuServices.findMenuList();

			view.addObject("menuTbls", menus);
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
	 * 권한관리에서 권한목록을 tree로 보여지게 함.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/findMenuTree.ssc", method = RequestMethod.GET)
	public ModelAndView findMenuTree(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();
		MenuTbl menuTbl = new MenuTbl();

		try {
			if(logger.isInfoEnabled()){
				logger.info("search.getNode() : " + search.getNode());
			}

			if(search.getNode().equals("root")){
				menuTbl.setDepth(0);
			}else{
				menuTbl.setMenuId(Integer.parseInt(search.getNode()));
				menuTbl.setDepth(1);
			} 

			List<Menus> tmpMenuTbls = menuServices.findMainMenuForExtJs(menuTbl, search);
			List<MenuTreeForExtJs> store = menuServices.makeExtJsJson(tmpMenuTbls);

			view.setViewName("jsonView");
			view.addObject("root", store);

			return view;
		} catch (ServiceException e) {
			view.addObject("result","N");
			view.addObject("authTbl", Collections.emptyList());
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");

			return view;
		}catch(Exception e){
			return view;
		}
		
	}


	/**
	 * 대메뉴의 서브메뉴를 조회하는 method.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/auth/findSubMenuList.ssc", method = RequestMethod.GET)
	public ModelAndView findSubMenuList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			if(logger.isInfoEnabled()){
				logger.info("search.getMenuId() : " + search.getMenuId());
				logger.info("search.getUserId() : " + search.getUserId());
			}

			MenuTbl info = menuServices.getMenuObj(search.getMenuId());
			search.setNodes(info.getNodes());
			
			//sub메뉴를 조회하는 함수.
			List<Menus> menuTbls = menuServices.findSubMenuList(search);

			view.addObject("menuTbls", menuTbls);
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

}

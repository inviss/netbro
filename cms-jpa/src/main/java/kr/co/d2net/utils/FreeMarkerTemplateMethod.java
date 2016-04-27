package kr.co.d2net.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.d2net.dao.CodeDao;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * FreeMarker Template Method
 * 
 * 화면에서 빈번하게 사용할 Function 모음
 *
 */
public class FreeMarkerTemplateMethod {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CodeDao codeDao;
	@Autowired
	private UserServices userService;
	//@Autowired
	//private CodeServices codeService;
	@Autowired
	private MenuServices menuServices;
	@Autowired
	private AuthServices authServices;
	@Autowired
	private UserAuthServices userAuthServices;



	/**
	 * <p>
	 * 권한별 메뉴 리스트
	 * </p>
	 * 
	 * @author Kang Myeong Seong
	 * 
	 *         <pre>
	 * userId을 입력받아 접근권한이 가능한 메뉴 리스트를 돌려준다.
	 * </pre>
	 */
	public class findUserMenus implements TemplateMethodModel {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public TemplateModel exec(List arg0) throws TemplateModelException {

			List<MenuTbl> menuList = new ArrayList<MenuTbl>();			
			List items = new ArrayList();


			Map<String, Object> params = new HashMap<String, Object>();

			String userId = (String) arg0.get(0);

			Object authUseYn = null;

			params.put("userId", userId);

			//userId를 이용해 authTbl의 권한 사용 유무를 확인.

			try {
				authUseYn = userAuthServices.getAuthUseYn(userId);
				
				if(authUseYn.equals("N")){
					params.put("userId", "guest");
				}
				
				menuList = menuServices.findUserMenus(params);
				
			} catch (ServiceException e1) {

			}

			logger.debug("menuList : " + menuList);

			//MENU값의 중복 체크 로직.			
			if(menuList != null){
				for(int i = 0; i<menuList.size();i++){
					if(!items.contains(menuList.get(i))){
						items.add(menuList.get(i));
					}
				}
			}

			logger.debug("items : " + items);

			return (TemplateModel) BeansWrapper.getDefaultInstance().wrap(
					items);
		}
	}


	/**
	 * userId을 입력받아 접근권한이 가능한 버튼을 돌려준다
	 * 
	 *
	 */
	public class getAccessRule implements TemplateMethodModel {
		@SuppressWarnings("unchecked")
		public TemplateModel exec(List args) throws TemplateModelException {

			String userId = (String) args.get(0);
			String menuId = (String) args.get(1);

			Map<String, Object> params = new HashMap<String, Object>();
		
			params.put("userId", userId);
			params.put("menuId", menuId);

			List<String> auth = null;
			Object authUseYn = null;
			String tmp = "";
			String tmp1 = "";
			String tmp2 = "";
			String tmp3 = "";


			try {
				//userId를 이용해 authTbl의 권한 사용 유무를 확인.
				authUseYn = userAuthServices.getAuthUseYn(userId);
				
				String tmp_authUseYn = authUseYn.toString();
				
				//userId,menuId를 이용해 roleAuthTbl의 controlGubun의 데이터 값을 가져온다.
				//ex) R : 읽기,RW : 쓰기,L : 제한.
				logger.debug("authUseYn.toString() : " + authUseYn.toString());
				//if(authUseYn.toString().equals("[Y, Y]")){
				if(tmp_authUseYn.contains("Y")){
					logger.debug("#######################userId  "+userId);
					logger.debug("#######################menuId  "+menuId);
					//auth = (List<String>)userService.getUserControlAuth(params);
					for (int i = 0; i < auth.size(); i++) {
						tmp = auth.get(i);

						if(tmp.equals("RW")){
							tmp1 = tmp;
						}else if(tmp.equals("R")){
							tmp2 = tmp;
						}else{
							tmp3 = tmp;
						}

						if(((tmp1.equals("RW") && tmp2.equals("R")))||tmp1.equals("RW")){
							tmp = "RW";
						}else if(((tmp1.equals("RW") && tmp3.equals("L")))||tmp1.equals("RW")){
							tmp = "RW";
						}else if(((tmp2.equals("R") && tmp3.equals("L")))||tmp2.equals("R")){
							tmp = "R";
						}else if(tmp3.equals("L")){
							tmp = "L";
						}
					}
				}else{
					logger.debug("##################################################################i`m here");
					params.put("userId", "guest");
					//auth = (List<String>)userService.getUserControlAuth(params);
					for (int i = 0; i < auth.size(); i++) {
						tmp = auth.get(i);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new SimpleScalar(tmp);
		}
	}



	public void init(Map<String, Object> ftlMap) {

		ftlMap.put("findUserMenus", new findUserMenus());
		ftlMap.put("getAccessRule", new getAccessRule());

	}
}

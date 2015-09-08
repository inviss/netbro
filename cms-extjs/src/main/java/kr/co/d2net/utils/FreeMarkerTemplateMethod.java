package kr.co.d2net.utils;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import kr.co.d2net.dao.CodeDao;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.dto.vo.UserAuth;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
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
	@Autowired
	private CodeServices codeService;
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
				logger.error("findUserMenus error", e1);
			}

			//MENU값의 중복 체크 로직.			
			if(menuList != null){
				for(int i = 0; i<menuList.size();i++){
					if(!items.contains(menuList.get(i))){
						items.add(menuList.get(i));
					}
				}
			}

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
		public TemplateModel exec(List args) throws TemplateModelException {

			String userId = (String) args.get(0);
			String menuId = (String) args.get(1);

			Map<String, Object> params = new HashMap<String, Object>();

			params.put("userId", userId);
			params.put("menuId", menuId);

			List<String> auth = null;
			String tmp = "";
			String tmpAuthYn = "";
			
			String tmpRW = "";
			String tmpR = "";

			if(logger.isDebugEnabled()) {
				logger.debug("userId: "+userId);
				logger.debug("menuId: "+menuId);
			}
			
			try {
				//userId를 이용해 authTbl의 권한 사용 유무를 확인.
				List<Object[]> object = userAuthServices.getAuthUseYn(userId);

				for(int i = 0; i<object.size(); i++){

					Object object2 = object.get(i);

					if(object2.toString().equals("Y")){
						tmpAuthYn = "Y";
					}
				}

				//userId,menuId를 이용해 roleAuthTbl의 controlGubun의 데이터 값을 가져온다.
				//ex) R : 읽기,RW : 쓰기,L : 제한.
				if(tmpAuthYn.equals("Y")){
					auth = (List<String>)userService.getUserControlAuth(params);
					
					for(int i = 0; i<auth.size(); i++){
						String tmpAuthGubun = auth.get(i);
						
						if(tmpAuthGubun.equals("RW")){
							tmpRW = "RW";
						}else if (tmpAuthGubun.equals("R")){
							tmpR = "R";
						}
					}
					
					if(StringUtils.isNotBlank(tmpRW)){
						tmp = "RW";
					}else if(StringUtils.isNotBlank(tmpR)){
						tmp = "R";
					}else{
						tmp = "L";
					}
					
					logger.debug("tmp : " + tmp);
					
				}else{
					params.put("userId", "guest");
					auth = (List<String>)userService.getUserControlAuth(params);
					for (int i = 0; i < auth.size(); i++) {
						tmp = auth.get(i);
					}
				}
			} catch (Exception e) {
				logger.error("getAccessRule error", e);
			}
			return new SimpleScalar(tmp);
		}
	}

	/**
	 * cms의 현재 버전을 리턴한다.
	 */
	public class getCmsSearchEngine implements TemplateMethodModel {
		public TemplateModel exec(List args) throws TemplateModelException {

			String searchEngine = messageSource.getMessage("cms.searchEngine", null, Locale.KOREA);

			return new SimpleScalar(searchEngine);
		}
	}

	
	/**
	 * cms의 스트리밍 사용여부 확인
	 */
	public class getStreamingYn implements TemplateMethodModel {
		public TemplateModel exec(List args) throws TemplateModelException {
			
			String result = messageSource.getMessage("cms.streming", null, Locale.KOREA);
			 
			return new SimpleScalar(result);
		}
	}
	
	/**
	 * cms의 카달로깅 사용여부 확인
	 */
	public class getCatalogingYn implements TemplateMethodModel {
		public TemplateModel exec(List args) throws TemplateModelException {
			
			String result = messageSource.getMessage("cms.catalog", null, Locale.KOREA);
			 
			return new SimpleScalar(result);
		}
	}
 
	public void init(Map<String, Object> ftlMap) {
		ftlMap.put("getStreamingYn", new getStreamingYn());
		ftlMap.put("getCatalogingYn", new getCatalogingYn());
		ftlMap.put("getCmsSearchEngine", new getCmsSearchEngine());
		ftlMap.put("findUserMenus", new findUserMenus());
		ftlMap.put("getAccessRule", new getAccessRule());

	}
}

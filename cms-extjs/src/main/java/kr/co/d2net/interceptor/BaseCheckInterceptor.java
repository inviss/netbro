package kr.co.d2net.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.d2net.utils.FreeMarkerTemplateMethod;
import kr.co.d2net.dto.UserTbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

/**
 * 사용자 세션체크하여 만료된 사용자는 로그인 페이지로 강제이동 </br>
 * 프리마커 템플릿 정보를 요청시 갱신하여 Request 객체에 전달한다.
 * @author Administrator
 */
public class BaseCheckInterceptor extends HandlerInterceptorAdapter {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FreeMarkerTemplateMethod freeMarkerTemplateMethod;

	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Map<String, Object> tplMap = (Map<String, Object>)WebUtils.getSessionAttribute(request, "tpl");

		if (tplMap == null)
			tplMap = new HashMap<String, Object>();

		UserTbl user = (UserTbl)WebUtils.getSessionAttribute(request, "user");

		if(user == null) {

			WebUtils.setSessionAttribute(request, "default","default");
			response.sendRedirect(request.getContextPath() + "/login.ssc");
			return false;

		}else{ 

			if (!tplMap.containsKey("tpl") || tplMap.get("tpl") == null) {
				freeMarkerTemplateMethod.init(tplMap);
			}
		}
		WebUtils.setSessionAttribute(request, "tpl", tplMap);
		request.setAttribute("tpl", tplMap);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		request.setAttribute("ctx", request.getContextPath());

		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0);
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		/*Map<String, Object> tplMap = (Map<String, Object>) WebUtils.getSessionAttribute(request, "tpl");
			if (tplMap != null)
			tplMap.clear();*/
	}

}

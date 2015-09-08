package kr.co.d2net.viewer;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.tiles2.TilesView;

public class ExtTilesView extends TilesView {
	private boolean exposeSpringMacroModel = false;    
	public void setExposeSpringMacroModel(boolean exposeSpringMacroModel) {        
		this.exposeSpringMacroModel = exposeSpringMacroModel;    
	}    
	
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {        
		if (exposeSpringMacroModel) {            
			if (model.containsKey(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {                
				throw new ServletException("Cannot expose bind macro helper '" +AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE +"' because of an existing model object of the same name");            
			}            
			model.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, new RequestContext(request, response, getServletContext(), model));     
		}        
		super.renderMergedOutputModel(model, request, response);    
	}
}

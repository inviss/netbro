package kr.co.d2net.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebTestController {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/hello", method = RequestMethod.GET)
	public ModelMap hello(ModelMap map) {
		
		try {
		} catch (Exception e) {
			logger.error("editList Error", e);
		}
		
		return map;
	}
	
	@RequestMapping(value="/world", method = RequestMethod.GET)
	public ModelMap world(ModelMap map) {
		
		try {
		} catch (Exception e) {
			logger.error("editList Error", e);
		}
		
		return map;
	}
	
}

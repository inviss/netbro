package kr.co.d2net.adapter.mvc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
			
	@Override
	public Integer convert(String source) {
		if(logger.isDebugEnabled()) {
			logger.debug("MVC Converter is Integer value: "+source);
		}
		return (StringUtils.isBlank(source)) ? 0 : Integer.valueOf(source);
	}

}

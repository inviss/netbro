package kr.co.d2net.adapter.mvc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class LongConverter implements Converter<String, Long> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
			
	@Override
	public Long convert(String source) {
		if(logger.isDebugEnabled()) {
			logger.debug("MVC Converter is Long value: "+source);
		}
		return (StringUtils.isBlank(source)) ? 0 : Long.valueOf(source);
	}

}

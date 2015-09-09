package kr.co.d2net.mvc.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Date convert(String source) {
		if (logger.isDebugEnabled()) {
			logger.debug("MVC Converter is Date value: " + source);
		}
		Date d = null;
		try {
			if (StringUtils.isNotBlank(source)) {
				SimpleDateFormat sdf = null;
				try {
					if(source.indexOf("-") > -1) {
						sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					} else if(source.indexOf("/") > -1) {
						sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
					} else if(source.indexOf(".") > -1) {
						sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
					} else {
						sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					}
				} catch (Exception e) {
					logger.error("Date Converter Error", e);
				}

				d = sdf.parse(source);
			}
		} catch (Exception e) {
			logger.error("Date Converter Error", e);
		}
		return (d == null) ? null : d;
	}

}

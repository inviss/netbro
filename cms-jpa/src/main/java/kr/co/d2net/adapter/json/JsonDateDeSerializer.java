package kr.co.d2net.adapter.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeSerializer extends JsonDeserializer<Date> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Date date = null;
		try {
			if(StringUtils.isNotBlank(jp.getText()))
				date = dateFormat.parse(jp.getText());
		} catch (Exception e) {
			logger.error("deserialize", e);
		}
		return date;
	}

}

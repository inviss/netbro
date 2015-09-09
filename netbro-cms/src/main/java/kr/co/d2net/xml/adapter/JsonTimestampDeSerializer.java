package kr.co.d2net.xml.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTimestampDeSerializer extends JsonDeserializer<Date> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

package kr.co.d2net.dto.xml;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;

import kr.co.d2net.search.adapter.DateTypeAdapter;
import kr.co.d2net.search.adapter.TimestampTypeAdapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebMessage implements Decoder.Text<WebMessage>, Encoder.Text<WebMessage> {

	private static Gson gson;
	static {
		gson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.excludeFieldsWithoutExposeAnnotation()
		.registerTypeAdapter(java.sql.Timestamp.class, new TimestampTypeAdapter())
		.registerTypeAdapter(java.util.Date.class, new DateTypeAdapter())
		.create();
	}
	
	@Override
	public String encode(WebMessage message) throws EncodeException {
		return gson.toJson(message);
	}

	@Override
	public WebMessage decode(String message) throws DecodeException {
		return gson.fromJson(message, WebMessage.class);
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}
	
}

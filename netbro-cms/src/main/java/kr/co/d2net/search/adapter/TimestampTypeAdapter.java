package kr.co.d2net.search.adapter;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
	private final DateFormat dateFormat;

	public TimestampTypeAdapter() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
	}

	@Override
	public Timestamp deserialize(JsonElement json, Type type, JsonDeserializationContext jc) throws JsonParseException {
		try {
			synchronized (dateFormat) {
				Date date = dateFormat.parse(json.getAsString());
				return new Timestamp(date.getTime());
			}
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Timestamp date, Type type,
			JsonSerializationContext jc) {
		synchronized (dateFormat) {
			return new JsonPrimitive(dateFormat.format(new Date(date.getTime())));
		}
	}
}

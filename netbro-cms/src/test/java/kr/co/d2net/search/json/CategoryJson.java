package kr.co.d2net.search.json;

import kr.co.d2net.dto.json.Transcode;
import kr.co.d2net.search.adapter.DateTypeAdapter;
import kr.co.d2net.search.adapter.TimestampTypeAdapter;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.utils.JSON;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CategoryJson {

	Gson gson;
	
	@Ignore
	@Test
	public void objToJson() {
		try {
			gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.excludeFieldsWithoutExposeAnnotation()
			.registerTypeAdapter(java.sql.Timestamp.class, new TimestampTypeAdapter())
			.registerTypeAdapter(java.util.Date.class, new DateTypeAdapter())
			.create();
			
			SearchMeta categoryView = new SearchMeta();
			
			categoryView.setCategoryId(1);
			categoryView.setEpisodeId(1);
			/*
			ContentView contentView = new ContentView();
			contentView.setCtId(1L);
			contentView.setCtNm("test");
			contentView.setKeyWords("test");
			*/
		//	contentView.setBrdDd(DateUtils.getWantDay(-100000));
			
			//categoryView.setContentView(contentView);
			
			
			System.out.println(gson.toJson(categoryView));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void objToJson2() {
		try {
			Transcode transcode = new Transcode();
			transcode.setCtId(1L);
			transcode.setSeq(1L);
			transcode.setSourceFlPath("/201309/30");
			
			String xml = JSON.toString(transcode);
			System.out.println(xml);
			
			transcode = JSON.toObject(xml, Transcode.class);
			System.out.println(transcode.getSeq());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

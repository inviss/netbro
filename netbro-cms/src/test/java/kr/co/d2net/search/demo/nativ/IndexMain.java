package kr.co.d2net.search.demo.nativ;

import java.util.Date;

import kr.co.d2net.search.Cluster;
import kr.co.d2net.search.adapter.DateTypeAdapter;
import kr.co.d2net.search.adapter.TimestampTypeAdapter;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.search.index.SearchMetaIndex;
import kr.co.d2net.utils.DateUtils;

import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.replication.ReplicationType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IndexMain {
	public static final String INDEX_NAME = "cms";
	public static final String INDEX_TYPE = "metadata";

	static Gson gson;

	static{
		try{
			gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.excludeFieldsWithoutExposeAnnotation()
			.registerTypeAdapter(java.sql.Timestamp.class, new TimestampTypeAdapter())
			.registerTypeAdapter(java.util.Date.class, new DateTypeAdapter())
			.create();
		}catch(Exception e){

		}
	}

	/**
	 * INDEX_TYPE의 맵핑정보를 삭제한다.
	 * @throws Exception
	 */
	public static void deleteMapping() throws Exception{
		DeleteMappingRequest mappingRequest = Requests.deleteMappingRequest(INDEX_NAME).type(INDEX_TYPE);   
		Cluster.getInstance().getClient().admin().indices().deleteMapping(mappingRequest);
	}

	/**
	 * INDEX를 삭제한다.
	 * @throws Exception
	 */
	public static void deleteIndex() throws Exception {
		Cluster.getInstance().getClient().admin().indices().prepareDelete(INDEX_NAME).execute().actionGet();
	}

	/**
	 * INDEX_TYPE의 맵핑정보를 생성한다.
	 * @throws Exception
	 */
	public static void createMapping() throws Exception{
		Cluster.getInstance().createMapping(SearchMeta.class);
	}

	/**
	 * Index 및 설정정보와 Index type 맵핑정보를 생성한다.
	 * @throws Exception
	 */
	public static void createIndexMapping()throws Exception {
		if (!Cluster.getInstance().existsIndex(Cluster.getInstance().getMapper(SearchMeta.class).getIndexName())) {
			Cluster.getInstance().createIndex(SearchMeta.class);
			Cluster.getInstance().createMapping(SearchMeta.class);
		}
	}

	/**
	 * 색인정보를 추가한다.
	 * @throws Exception
	 */
	public static void addIndex()throws Exception {

		SearchMeta category = new SearchMeta();

		category.setCategoryId(6);
		category.setSegmentId(1);
		category.setEpisodeId(1);
		category.setCategoryNm("편집");
		category.setEpisodeNm("기본회차");
		category.setSegmentNm("기본");
		category.setCtNm("한국을");
		category.setCtId(55L);
		category.setDuration(18001L);
		category.setBrdDd(DateUtils.getWantDay(0).getTime());
		category.setRistClfCd("000");
		category.setCtTyp("000");
		category.setCtCla("000");
		category.setFlPath("/201403/24/sample008_인기가요3_20140324151948.mxf");
		category.setNodes("6");
		category.setLockStatCd("000");
		category.setCtiFmt("101");
		category.setRegDt(DateUtils.getWantDay(0).getTime());

		Cluster.getInstance().getClient().prepareIndex(INDEX_NAME, INDEX_TYPE)
		.setId(String.valueOf(category.getCtId()))
		.setSource(gson.toJson(category)).execute().actionGet();  
	}

	public static void addBulkIndex()throws Exception {
		BulkRequestBuilder bulkRequest = Cluster.getInstance().getClient().prepareBulk();

		long ctId = 0;
		for(int i=1; i<1000; i++) {
			Date d = DateUtils.getWantDay(-(1000 -i));
			for(int j=1; j<=5; j++) {
				ctId ++;

				SearchMeta category = new SearchMeta();

				category.setCategoryId(i);
				category.setSegmentId(1);
				category.setEpisodeId(j);
				category.setCategoryNm("카테고리 "+i);
				category.setEpisodeNm("카테고리 "+i+" 에피소드 "+j+"회");
				category.setSegmentNm("세그먼트");

				category.setCtId(ctId);
				category.setCtNm("수동등록 "+ctId+"번째");
				category.setCtCla("001");
				category.setCtTyp("001");
				category.setCont("색인 테스트를 진행하는중 입니다.");
				category.setVdQlty("HD");
				category.setAspRtpCd("001");
				category.setKeyWords("1박, 2일, 강호동, 1박2일");
				category.setRpImgKfrmSeq(ctId);
				category.setRegDt(d.getTime());
				category.setBrdDd(d.getTime());
				category.setRegrid("kang");
				category.setVdHresol(1080);
				category.setVdVresol(1920);
				category.setCtiFmt("001");
				category.setFlPath("/mp2/aaa/bbb");
				category.setProdRoute("001");

				bulkRequest.add(Cluster.getInstance().getClient().prepareIndex(INDEX_NAME, INDEX_TYPE).setId(String.valueOf(category.getCtId())).setSource(gson.toJson(category)))
				.setReplicationType(ReplicationType.ASYNC)
				.setConsistencyLevel(WriteConsistencyLevel.QUORUM)
				.setRefresh(false);

				if(ctId % 100 == 0) {
					bulkRequest.execute().actionGet(5000);
					bulkRequest = Cluster.getInstance().getClient().prepareBulk();
				}
			}
		}

		bulkRequest.execute().actionGet(5000);
		bulkRequest = Cluster.getInstance().getClient().prepareBulk();
	}

	/**
	 * 색인데이타 삭제
	 * @throws Exception
	 */
	public static void clearData()throws Exception {
		Cluster.getInstance().getClient().prepareDeleteByQuery(INDEX_NAME).setQuery(QueryBuilders.matchAllQuery()).execute().actionGet(); 
	}
	
	public static void getData()throws Exception {
		SearchMetaIndex index = new SearchMetaIndex();
		SearchMeta searchMeta = index.get("160");
		System.out.println(searchMeta.getCtNm());
	}
	
	public static void deleteData() throws Exception {
		SearchMetaIndex index = new SearchMetaIndex();
		
		Long ctId = 368L;
		SearchMeta meta = index.get(String.valueOf(ctId));
		if(meta != null) {
			DeleteRequest request = new DeleteRequest("cms", "metadata", String.valueOf(ctId));
			Cluster.getInstance().getClient().delete(request).actionGet();
			
			Cluster.getInstance().closeClient();
		}
	}

	public static void main(String[] args) throws Exception{
		//createIndexMapping();
		//deleteMapping();
		//deleteIndex();
		//createMapping();
		//addIndex();
		//addBulkIndex();
		//clearData();
		//getData();
		deleteData();
	}
}

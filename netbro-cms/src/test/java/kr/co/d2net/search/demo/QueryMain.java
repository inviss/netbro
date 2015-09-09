package kr.co.d2net.search.demo;

import kr.co.d2net.search.demo.nativ.IndexMain;

import org.elasticsearch.action.search.SearchOperationThreading;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.sort.SortOrder;


public class QueryMain {

	public static void main(String[] args) throws Exception {

		// cluster.name 을 안 줄 경우 NoNodeAvailable 에러 발생.
		Settings settings = ImmutableSettings
		        .settingsBuilder()
		        .put("cluster.name", "cluster_es1")
		        .build();
		
		TransportClient client = new TransportClient(settings)
		        .addTransportAddress(new InetSocketTransportAddress("14.36.147.23", 9300)
		);
		
		SearchResponse response = client.prepareSearch(IndexMain.INDEX_NAME)    
		        //.setQuery(QueryBuilders.termQuery("ct_nm", "16번째"))    
		        .addSort("ct_id", SortOrder.DESC)
		        .setFrom(0).setSize(60).setExplain(true)   
		        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
		        .execute()    
		        .actionGet();
		
		client.close();
	}
}

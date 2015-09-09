package kr.co.d2net.search.demo.nativ;

import java.util.List;

import kr.co.d2net.search.Cluster;
import kr.co.d2net.utils.DateUtils;

import org.elasticsearch.action.search.SearchOperationThreading;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;


public class QueryMain {
	
	public static void execute(QueryBuilder query){
		Client client = Cluster.getInstance().getClient();  
		SearchResponse response = client.prepareSearch(IndexMain.INDEX_NAME)    
        .setQuery(query)    
        .addSort("reg_dt", SortOrder.DESC)
        .setFrom(0).setSize(60).setExplain(true)   
        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
        .execute()    
        .actionGet();  

		printResponse(response);
	}
	
	public static void termQuery(){
		Client client = Cluster.getInstance().getClient();  
		SearchResponse response = client.prepareSearch(IndexMain.INDEX_NAME)    
        //.setQuery(QueryBuilders.termQuery("ct_nm", "16번째"))    
        .addSort("ct_id", SortOrder.DESC)
        .setFrom(0).setSize(60).setExplain(true)   
        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
        .execute()    
        .actionGet();  
		
		client.close();

		printResponse(response);
	}
	
	public static void filteredQuery(){
		FilteredQueryBuilder query = QueryBuilders.filteredQuery(    
				QueryBuilders.termQuery("key_words", "1박"),     
				FilterBuilders.rangeFilter("brd_dd")    
                    .from(DateUtils.getWantDay(-50, "yyyy-MM-dd"))    
                    .to(DateUtils.getToday("yyyy-MM-dd"))    
                    .includeLower(true)    
                    .includeUpper(false)    
                );
		
		Client client = Cluster.getInstance().getClient();  
		
		SearchResponse response = client.prepareSearch(IndexMain.INDEX_NAME)    
        .setQuery(query)    
        .addSort("reg_dt", SortOrder.DESC)
        .setFrom(0).setSize(60).setExplain(true)
        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
        .execute()    
        .actionGet();  

		printResponse(response);
	}
	
	public static void queryFacet(){
		Client client = Cluster.getInstance().getClient();  
		
		QueryBuilder query = QueryBuilders.matchAllQuery();
		
		SearchResponse response = client.prepareSearch(IndexMain.INDEX_NAME)    
        .setQuery(query)    
        .addSort("reg_dt", SortOrder.DESC)
        .addFacet(FacetBuilders.termsFacet("f").field("episode_nm").size(10000))
        .addFacet(FacetBuilders.dateHistogramFacet("f2").field("reg_dt").interval("year"))
        .setSize(2)
        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
        .execute()    
        .actionGet();  

		Facets facets = response.getFacets();
		if(facets != null){
			TermsFacet  f = (TermsFacet)facets.facetsAsMap().get("f");
			System.out.println("Total:"+f.getTotalCount());
			System.out.println("Missing:"+f.getMissingCount());
			System.out.println("Other:"+f.getOtherCount());
			
			List<? extends Entry> list = f.getEntries();
			System.out.println("Entries:"+list.size());
			for(Entry e : list){
				System.out.println(e.getTerm() + "\t" + e.getCount());
			}
			
			DateHistogramFacet  f2 = (DateHistogramFacet)facets.facetsAsMap().get("f2");
			List<? extends DateHistogramFacet.Entry> list2 = f2.getEntries();
			System.out.println("Entries:"+list2.size());
			for(DateHistogramFacet.Entry e : list2){
				System.out.println(e.getTotalCount() + "\t" + e.getCount());
			}
		}
		
		printResponse(response);
	}
	
	public static void queryGeo(double lat, double lng, double distance){
		//create query
		QueryBuilder query = QueryBuilders.filteredQuery(    
				QueryBuilders.matchAllQuery(),     
				FilterBuilders.geoDistanceFilter("location")   
                    .distance(distance, DistanceUnit.KILOMETERS)
                    .lon(lng)
                    .lat(lat)  
                );  
		
		//execute query
		SearchResponse response = Cluster.getInstance().getClient().prepareSearch(IndexMain.INDEX_NAME)    
        .setQuery(query)    
        .addSort(SortBuilders.geoDistanceSort("location").point(lat, lng)) //sort by distance
        .setFrom(0).setSize(60).setExplain(true)   
        .setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD)
        .execute()    
        .actionGet();  

		printResponse(response);
	}
	
	
	/**
	 * 검색결과를 출력한다.
	 * @param response
	 */
	private static void printResponse(SearchResponse response){
		SearchHits hits = response.getHits();  
		System.out.println(response.getTookInMillis() + " ms Query TotalHits:" + hits.getTotalHits());
        for (SearchHit hit : hits) {   
            System.out.println(hit.getSource());   
        } 
	}

	public static void queryGeo(){
		queryGeo(39.7655391,116.9799914,1000);
	}
	
	public static void main(String[] args) {
		//queryGeo(); 
		termQuery();
		//queryFacet();
		//filteredQuery();
	}
}

package kr.co.d2net.search.demo.geo;

import java.util.Date;

import kr.co.d2net.search.Cluster;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;

public class GeoDemo {
	static final String INDEX_NAME = "cms";
	static final String INDEX_TYPE = "category";
	static Cluster cluster = Cluster.getInstance();
	
	public static void createIndexMapping()throws Exception {
		cluster.getClient().admin().indices().prepareCreate(INDEX_NAME).execute().actionGet();  

		XContentBuilder mapping = XContentFactory.jsonBuilder()   
	       .startObject()   
	         .startObject(INDEX_NAME)   
	         .startObject("properties")          
	         	.startObject("id").field("type", "integer").field("store", "yes").endObject()
	         	.startObject("title").field("type", "string").field("store", "yes").endObject()     
	         	.startObject("location").field("type", "geo_point").endObject()    
	         	.startObject("createAt").field("type", "date").endObject()                  
	         .endObject()   
	        .endObject()   
	      .endObject();   
	 PutMappingRequest mappingRequest = Requests.putMappingRequest(INDEX_NAME).type(INDEX_TYPE).source(mapping);   
	 cluster.getClient().admin().indices().putMapping(mappingRequest).actionGet(); 
	}
	
	public static void addData(int id,String title,double lng,double lat)throws Exception {
		 XContentBuilder doc = XContentFactory.jsonBuilder()   
	     .startObject()       
	     	.field("id", id)
	         .field("title", title)   
	         .field("location", String.format("%f,%f", lat,lng))     
	         .field("createAt", new Date())                             
	    .endObject();   
		 cluster.getClient().prepareIndex(INDEX_NAME,INDEX_TYPE).setSource(doc).execute().actionGet();  
	}
	
	public static void query(double lat,double lng,double distance){
		//create query
		QueryBuilder query = QueryBuilders.filteredQuery(    
				QueryBuilders.matchAllQuery(),     
				FilterBuilders.geoDistanceFilter("location")   
                    .distance(distance, DistanceUnit.KILOMETERS)
                    .lon(lng)
                    .lat(lat)  
                );  
		
		//execute query
		SearchResponse response = cluster.getClient().prepareSearch(INDEX_NAME)    
        .setQuery(query)    
        .addSort(SortBuilders.geoDistanceSort("location").point(lat, lng)) //sort by distance
        .setFrom(0).setSize(60).setExplain(true)   
        .execute()    
        .actionGet();  

		SearchHits hits = response.getHits();   
		
		//print result
		System.out.println(response.getTookInMillis() + " ms Query TotalHits:" + hits.getTotalHits());
        for (SearchHit hit : hits) {   
            System.out.println(hit.getSource());   
        } 
	}
	
	public static void main(String[] args) throws Exception{
		//createIndexMapping();
		//randomAddData(99999);
		query(22.851060,118.587100,100d);
	}
}

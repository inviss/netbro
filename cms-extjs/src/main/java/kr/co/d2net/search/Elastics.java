package kr.co.d2net.search;

import java.io.IOException;
import java.util.Map;

import kr.co.d2net.utils.JSON;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class Elastics {

	public static Client getClient() {
		return Clusters.getClient();
	}
	
	public static Client getClient(String clusterName, String clusterNodes) {
		return Clusters.getClient(clusterName, clusterNodes);
	}
	
	public static XContentBuilder makeJSONBuilder() throws IOException {
		return XContentFactory.jsonBuilder();
	}
	
	public static void createMapping(String indexName, String indexType, XContentBuilder mapping) {
		PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexType).source(mapping);
        getClient().admin().indices().putMapping(mappingRequest).actionGet();
	}
	
	public static void createMapping(Class<?> clsss) {
		String indexName = Mappers.getIndexName(clsss);
        String indexType = Mappers.getIndexType(clsss);
        XContentBuilder mapping = Mappers.createMapping(clsss);
        createMapping(indexName, indexType, mapping);
	}
	
	public static void createIndex(String indexName) throws IOException {
		 getClient().admin().indices().prepareCreate(indexName).setSettings(
				 ImmutableSettings.settingsBuilder().loadFromSource(Mappers.createSetting().string())
				 ).execute().actionGet();
	}
	
	public static void createIndex(Class<?> clsss) throws IOException {
		createIndex(getIndexName(clsss));
	}
	
	public static SearchRequestBuilder makeSearchRequestBuilder(String indexName, String indexType) {
		return getClient().prepareSearch(indexName).setTypes(indexType);
	}
	
	public static SearchRequestBuilder makeSearchRequestBuilder(Class<?> clsss) {
		return makeSearchRequestBuilder(getIndexName(clsss), getIndexType(clsss));
	}
	
	public static void clearIndex(String indexName) {
		QueryBuilder query = QueryBuilders.matchAllQuery();
        getClient().prepareDeleteByQuery(indexName).setQuery(query).execute().actionGet();
	}
	
	public static void clearIndex(Class<?> clsss){
		clearIndex(getIndexName(clsss));
	}
	
	public static void index(String indexName, String indexType, Map<String,Object> source){
		getClient().prepareIndex(indexName, indexType).setSource(source).execute().actionGet();
	}
	
	public static void index(String indexName, String indexType, String id, Map<String,Object> source){
		getClient().prepareIndex(indexName, indexType, id).setSource(source).execute().actionGet();
	}
	
	public static void index(Object entity){
		String indexName = Mappers.getIndexName(entity.getClass());
        String indexType = Mappers.getIndexType(entity.getClass());
        String id = Mappers.getId(entity);
        index(indexName, indexType, id, JSON.toMap(entity));
	}
	
	public static void update(Object entity) {
		String indexName = Mappers.getIndexName(entity.getClass());
        String indexType = Mappers.getIndexType(entity.getClass());
        String id = Mappers.getId(entity);
		UpdateRequest request = new UpdateRequest(indexName, indexType, id);
		request.doc(JSON.toMap(entity));
		getClient().update(request);
	}
	
	public static void dropIndex(String indexName){
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        getClient().admin().indices().delete(request);
	}
	
	public static void dropIndex(Class<?> clsss){
		dropIndex(getIndexName(clsss));
	}
	
	public static void delete(String indexName, String indexType, String id) {
		DeleteRequest request = new DeleteRequest(indexName, indexType, id);
		getClient().delete(request);
	}
	
	public static void delete(Object entity) {
		String indexName = Mappers.getIndexName(entity.getClass());
        String indexType = Mappers.getIndexType(entity.getClass());
		delete(indexName, indexType, String.valueOf(JSON.toMap(entity).get("ctId")));
	}
	
	public static GetResponse get(String indexName, String indexType, String id) {
		GetRequest request = new GetRequest(indexName, indexType, id);
		GetResponse response = getClient().get(request).actionGet();
		return response;
	}
	
	public static boolean existsIndex(String indexName){
		IndicesExistsRequest request = new IndicesExistsRequest(indexName);
        return getClient().admin().indices().exists(request).actionGet().isExists();
	}
	
	public static boolean existsIndex(Class<?> clsss){
		return existsIndex(getIndexName(clsss));
	}
	
	public static void deleteMapping(String indexName, String indexType) {
		DeleteMappingRequest deleteMappingRequest = Requests.deleteMappingRequest(indexName).types(indexType);
		getClient().admin().indices().deleteMapping(deleteMappingRequest);
	}
	
	public static void deleteMapping(Class<?> clsss) {
		deleteMapping(getIndexName(clsss),getIndexType(clsss));
	}
	
	public static String getIndexName(Class<?> clsss){
		return Mappers.getIndexName(clsss);
	}
	
	public static String getIndexType(Class<?> clsss){
		return Mappers.getIndexType(clsss);
	}
}

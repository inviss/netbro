package kr.co.d2net.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.d2net.search.config.Configure;
import kr.co.d2net.search.config.NodeAddr;
import kr.co.d2net.utils.JSON;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author D2Net
 *
 */

@SuppressWarnings("unchecked")
public class Cluster {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private static Cluster instance;
	private final Map<Class<?>, Mapper<?>> mappers = new HashMap<Class<?>, Mapper<?>>();
	private Client client;
	private Configure conf;

	public static synchronized Cluster getInstance(){
		if(instance == null){
			instance = new Cluster();
		}
		return instance;
	}

	private Cluster(){
		conf = Configure.getInstance();
		client = getClient(conf.getClusterName(), conf.getNodes());
	}

	public synchronized void register(Mapper<?> mapper){
		Mapper<?> old = mappers.get(mapper.getEntityClass());
		if(old != null){
			return;
		}
		if(!existsIndex(mapper.getIndexName())){
			createIndex(mapper);
			createMappingAtCluster(mapper);
		}

		mappers.put(mapper.getEntityClass(), mapper);
	}

	public synchronized <E>  Index<E> getIndex(Class<E> entityClass){
		Mapper<E> mapper = (Mapper<E>)mappers.get(entityClass);
		if(mapper == null){
			mapper = new Mapper<E>(entityClass);
			register(mapper);
		}
		Index<E> index = new Index<E>(mapper.getEntityClass());
		return index;
	}

	public void createMappingAtCluster(Mapper<?> mapper) {
		logger.debug("create mapping '" + mapper.getIndexName() + "' at cluster.");
		createMapping(mapper);
	}

	protected static Client getClient(String clusterName, String host, int port){
		Builder builder = ImmutableSettings.settingsBuilder();
		if(StringUtils.isNotBlank(clusterName)){
			builder.put("cluster.name",clusterName);
		}
		Settings settings = builder.build();
		return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	/**
	 * 클러스터로 묶인 각 노드를 연결하여 클라이언트 생성
	 * @param clusterName 클러스터명
	 * @param nodes 노드리스트
	 * @return
	 */
	protected static Client getClient(String clusterName ,List<NodeAddr> nodes){
		Builder builder = ImmutableSettings.settingsBuilder();
		if(StringUtils.isNotBlank(clusterName)){
			builder.put("cluster.name", clusterName);
		}

		Settings settings = builder.build();
		TransportClient client =  new TransportClient(settings);
		for(NodeAddr addr : nodes){
			client.addTransportAddress(new InetSocketTransportAddress(addr.getHost(), addr.getPort()));
		}
		return client;
	}

	/**
	 * 인덱스 생성시 입력된 ID로 조회
	 * @param indexName 인덱스명
	 * @param indexType 인덱스 유형
	 * @param id
	 * @return
	 */
	public GetResponse get(String indexName, String indexType, String id){
		GetRequest request = new GetRequest(indexName, indexType, id);
		GetResponse  response = getClient().get(request).actionGet();
		closeClient();
		return response;
	}


	/**
	 * 인덱스 유형에 맵핑을 제거
	 * @param indexName
	 * @param indexType
	 */
	public void deleteMapping(String indexName, String indexType) {
		DeleteMappingRequest mappingRequest = Requests.deleteMappingRequest(indexName).type(indexType);
		getClient().admin().indices().deleteMapping(mappingRequest);
		closeClient();
	}

	/**
	 * 맵핑 Class를 기반으로 맵핑 제거
	 * @param clsss
	 */
	public void deleteMapping(Class<?> clsss) {
		Mapper<?> mapper = getMapper(clsss);
		deleteMapping(mapper);
	}

	public void deleteMapping(Mapper<?> mapper) {
		deleteMapping(mapper.getIndexName(),mapper.getIndexType());
	}

	/**
	 * 인텍스 유형에 맵핑 생성
	 * @param indexName
	 * @param indexType
	 * @param mapping
	 */
	public void createMapping(String indexName, String indexType, XContentBuilder mapping){
		PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexType).source(mapping);
		getClient().admin().indices().putMapping(mappingRequest).actionGet();
		closeClient();
	}

	/**
	 * 맵핑이 설정된 Mapper 객체를 전달받아 생성
	 * @param clsss
	 */
	public void createMapping(Mapper<?> mapper){
		String indexName = mapper.getIndexName();
		String indexType = mapper.getIndexType();
		XContentBuilder mapping = mapper.getMapping();

		createMapping(indexName, indexType, mapping);
	}

	/**
	 * Annotation 설정된 맵핑 클래스를 전달받아서 맵핑 생성
	 * @param clsss
	 */
	public void createMapping(Class<?> clsss){
		Mapper<?> mapper = getMapper(clsss);
		String indexName = mapper.getIndexName();
		String indexType = mapper.getIndexType();
		XContentBuilder mapping = mapper.getMapping();

		createMapping(indexName, indexType, mapping);
	}

	/**
	 * Cluster에 인덱스를 추가
	 * 설정정보를 Mapper 클래스에 추가한 후 json 방식으로 전달
	 * @param indexName
	 */
	public void createIndex(Mapper<?> mapper) {
		try {
			getClient().admin().indices().prepareCreate(mapper.getIndexName())
			.setSettings(ImmutableSettings.settingsBuilder().loadFromSource(mapper.getSetting().string()))
			//.setSettings(ImmutableSettings.settingsBuilder().loadFromClasspath("settings.json"))
			.execute().actionGet();
			closeClient();
		} catch (Exception e) {
			logger.error("create index error", e);
		}

	}

	/**
	 * Cluster에 인덱스를 추가
	 * 인덱스 설정 Entity 클래스를 전달받아 Mapper 생성 후 인덱스 생성
	 * @param indexName
	 */
	public void createIndex(Class<?> clsss){
		createIndex(getMapper(clsss));
	}


	@SuppressWarnings("rawtypes")
	public synchronized Mapper<?> getMapper(Class<?> clsss){
		Mapper<?>  mapper = mappers.get(clsss);
		if(mapper == null){
			mapper = new Mapper(clsss);
			register(mapper);
		}
		return mapper;
	}

	public SearchRequestBuilder makeSearchRequestBuilder(String indexName){
		return getClient().prepareSearch(indexName);
	}

	/**
	 * Cluster에 생성된 Index의 정보를 모두 제거
	 * @param indexName
	 */
	public void clearIndex(String indexName){
		QueryBuilder query = QueryBuilders.matchAllQuery();
		getClient().prepareDeleteByQuery(indexName).setQuery(query).execute().actionGet();  
		closeClient();
	}

	/**
	 * Mapper 클래스를 전달받아서 생성된 인덱스 정보를 모두 제거
	 * @param clsss
	 */
	public void clearIndex(Mapper<?> mapper){
		clearIndex(mapper.getIndexName());
	}

	public void index(String indexName ,String indexType, Map<String,Object> source){
		getClient().prepareIndex(indexName, indexType).setConsistencyLevel(WriteConsistencyLevel.ONE).setSource(source).execute().actionGet();
		closeClient();
	}

	public void index(String indexName, String indexType, String id, Map<String,Object> source){
		getClient().prepareIndex(indexName, indexType, id).setConsistencyLevel(WriteConsistencyLevel.ONE).setSource(source).execute().actionGet();
		closeClient();
	}

	public void index(Mapper<?> mapper , Object entity){
		String indexName = mapper.getIndexName();
		String indexType = mapper.getIndexType();
		Object id = mapper.getIdValue(entity);
		if(id == null){
			index(indexName, indexType, JSON.toMap(entity));
			return;
		}
		index(indexName, indexType, id.toString(), JSON.toMap(entity));
	}

	/**
	 * 인덱스 삭제
	 * @param indexName
	 */
	public void dropIndex(String indexName){
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
		getClient().admin().indices().delete(request);
		closeClient();
	}


	/**
	 * 인덱스가 존재하는지 체크
	 * @param indexName
	 * @return
	 */
	public boolean existsIndex(String indexName){
		IndicesExistsRequest request = new IndicesExistsRequest(indexName);
		return getClient().admin().indices().exists(request).actionGet().isExists();
	}

	public Client getClient(){
		if(client != null) return client;
		else {
			client = getClient(conf.getClusterName(), conf.getNodes());
			return client;
		}
	}

	public void closeClient() {
		if(client != null) {
			client.close();
			client = null;
		}
	}
}

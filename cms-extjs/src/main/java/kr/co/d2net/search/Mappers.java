package kr.co.d2net.search;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.Id;
import kr.co.d2net.search.annotation.Indexed;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.ObjectUtils;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mappers {
	final static Logger logger = LoggerFactory.getLogger(Mappers.class);

	private static final Map<Class<?>, String> indexNamesCache = new HashMap<Class<?>, String>();
	private static final Map<Class<?>, String> indexTypesCache = new HashMap<Class<?>, String>();
	
	private static Configure configure  = Configure.getInstance();

	public static XContentBuilder createSetting() {
		try {
			XContentBuilder setting = XContentFactory.jsonBuilder();
			setting.startObject();
			
			setting.startObject("index");
			
			setting.field("number_of_shards", configure.getNumberOfShards());
			setting.field("number_of_replicas", configure.getNumberOfReplicas());

			setting.startObject("routing");
			setting.field("required", "true");
			setting.field("path", configure.getRoutingPath());
			setting.endObject();
			
			setting.field("refresh_interval", configure.getRefreshInterval());
			setting.startObject("merge").startObject("policy");
			setting.field("segments_per_tier", 5);
			setting.endObject().endObject(); //policy, merge

			setting.startObject("analysis");
			setting.startObject("analyzer");
			setting.startObject("korean_index");
			setting.field("type", "custom");
			setting.field("tokenizer", configure.getAnalyzer());
			setting.endObject(); //korean_index
			setting.startObject("korean_query");
			setting.field("type", "custom");
			setting.field("tokenizer", "korean_query_tokenizer");
			setting.endObject(); //korean_query
			setting.endObject(); //analyzer
			setting.startObject("tokenizer");
			setting.startObject("korean_query_tokenizer");
			setting.field("type", configure.getAnalyzer());
			setting.field("compound_noun_min_length", 100);
			setting.endObject(); //korean_query_tokenizer
			setting.endObject(); //tokenizer
			setting.endObject(); //analysis

			setting.startObject("store");
			setting.field("type", configure.getStoreType());
			setting.startObject("compress");
			setting.field("stored", "true");
			setting.field("tv", "true");
			setting.endObject().endObject(); //store


			setting.endObject().endObject(); //index, root

			return setting;
		} catch (Exception e) {
			logger.error("can not create setting!", e);
			throw new RuntimeException(e);
		}
	}

	public static XContentBuilder createMapping(Class<?> clazz) {
		try {
			XContentBuilder mapping = XContentFactory.jsonBuilder();
			Entity entity = clazz.getAnnotation(Entity.class);
			if (entity == null) {
				throw new RuntimeException("'Entity' annotation not found:" + clazz.getName());
			}

			Field[] fields = clazz.getDeclaredFields();
			mapping.startObject().startObject(entity.indexType()).startObject("properties");
			for(Field field : fields) {
				Indexed indexed = field.getAnnotation(Indexed.class);
				if (indexed == null) {
					continue;
				}

				String name = indexed.name();
				String type = indexed.type();
				boolean store = indexed.store();
				String index = indexed.index();
				String format = indexed.format();
				String termVector = indexed.termVector();
				String analyzer = indexed.analyzer();
				String includeInAll = indexed.includeInAll();

				name = (name.isEmpty() ? field.getName() : name);

				if (type.isEmpty()) {
					type = field.getType().getSimpleName().toLowerCase();
				}

				mapping.startObject(name).field("type", type).field("store", (store) ? "yes"  :"no");

				mapping.field("index", index);

				if(!format.isEmpty()) {
					mapping.field("format", format);
				}

				mapping.field("term_vector", termVector);

				if (!analyzer.isEmpty()) {
					mapping.field("analyzer", analyzer);
				}

				mapping.field("include_in_all", includeInAll);
				mapping.endObject();
			}
			return mapping;
		} catch (Exception e) {
			logger.error("can not create mapping for "+clazz.getName()+" : "+e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String getIndexName(Class<?> clazz) {
		String indexName = indexNamesCache.get(clazz);
		if(indexName != null) {
			return indexName;
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null) {
			throw new RuntimeException("'Entity' annotation not found: "+clazz.getName());
		}

		indexName = entity.indexName();
		indexNamesCache.put(clazz, indexName);

		return indexName;
	}

	public static String getIndexType(Class<?> clazz) {
		String indexType = indexTypesCache.get(clazz);
		if(indexType != null) {
			return indexType;
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null) {
			throw new RuntimeException("'Entity' annotation not found: "+clazz.getName());
		}

		indexType = entity.indexType();
		indexTypesCache.put(clazz, indexType);

		return indexType;
	}
	
	public static String getId(Object o) {
		Method methods[] = o.getClass().getDeclaredMethods();
		try {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].isAnnotationPresent(Id.class)) {
					Object value = methods[i].invoke(o);
					return String.valueOf(value);
				}
			}
		} catch (Exception e) {
			logger.error(o.getClass().getName()+" getId value Error", e);
		}
		return null;
	}

	public static <E> E entity(Class<?> clazz, GetResponse res) {
		if(!res.isExists()) {
			return null;
		}
		try {
			Map<String, Object> map = res.getSource();
			return entity(clazz, map);
		} catch (Exception e) {
			logger.error("source to obj convertor error", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E> E entity(Class<?> clazz, Map<String, Object> map) {
		try {
			E obj = (E)clazz.newInstance();
			ObjectUtils.setProperties(obj, map);

			return obj;
		} catch (Exception e) {
			logger.error("source to obj convertor error", e);
		}
		return null;
	}

	public static <E> List<E> entity(Class<?> clazz, SearchResponse res){
		List<E> result = new ArrayList<E>();
		SearchHits totalHits = res.getHits();
		SearchHit[] hits = totalHits.getHits();
		for(SearchHit hit : hits){
			E e = entity(clazz, hit.getSource());
			result.add(e);
		}
		return result;
	}
}

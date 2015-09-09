package kr.co.d2net.search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.Id;
import kr.co.d2net.search.annotation.Indexed;
import kr.co.d2net.search.annotation.Settings;
import kr.co.d2net.search.annotation.SubEntity;
import kr.co.d2net.utils.ClassUtils;
import kr.co.d2net.utils.ObjectFactory;
import kr.co.d2net.utils.ObjectUtils;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mapper<E> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private Class<E> entityClass;
	private XContentBuilder mapping;
	private String indexName;
	private String indexType;
	private String idField;
	private ObjectFactory<E> objectFactory;

	Mapper(Class<E> entityClass) {
		this.entityClass = entityClass;
		objectFactory = new ObjectFactory<E>(entityClass);
		init();
	}

	private void init(){
		//id
		Field field = ClassUtils.getAnnotationedField(entityClass, Id.class);
		if (field != null) {
			idField =  field.getName();
		}

		Entity entity = entityClass.getAnnotation(Entity.class);
		if (entity == null) {
			throw new RuntimeException("'Entity' annotation not found:" + entityClass.getName());
		}

		//indexName
		indexName = (entity.indexName());
		//indexType
		indexType = (entity.indexType());
	}

	public void addMapping(XContentBuilder mapping, Field field, Indexed indexed) {
		try {
			String name = indexed.name();
			String type = indexed.type();
			boolean store = indexed.store();
			String index = indexed.index();
			String format = indexed.format();
			boolean termVector = indexed.termVector();
			String analyzer = indexed.analyzer();
			boolean includeInAll = indexed.includeInAll();

			name = (name.isEmpty() ? field.getName() : name);

			if (type.isEmpty()) {
				type = field.getType().getSimpleName().toLowerCase();
			}

			mapping.startObject(name).field("type", type).field("store", (store) ? "yes"  :"no");

			if (!index.isEmpty()) {
				mapping.field("index", index);
			} else {
				mapping.field("index", "no");
			}

			if(!format.isEmpty()) {
				mapping.field("format", format);
			}

			if (termVector) {
				mapping.field("term_vector", "yes");
			}

			if (!analyzer.isEmpty()) {
				mapping.field("analyzer", analyzer);
			}

			if(includeInAll) {
				mapping.field("include_in_all", "true");
			} else {
				mapping.field("include_in_all", "false");
			}
			mapping.endObject();
		} catch (Exception e) {
			throw new RuntimeException("can not create mapping for '" + entityClass.getName() + "' : " + e.getMessage());
		}

	}


	/**
	 * Entity Class의 annotation 정보를 이용하여 맵핑 정보를 생성한다.
	 * @param clsss
	 * @return
	 */
	public synchronized XContentBuilder getMapping() {
		if (mapping != null) {
			mapping = null;
		}
		try {
			mapping = XContentFactory.jsonBuilder();

			//id
			Field id = ClassUtils.getAnnotationedField(entityClass, Id.class);
			if (id != null) {
				idField =  id.getName();
			}

			Entity entity = entityClass.getAnnotation(Entity.class);
			if (entity == null) {
				throw new RuntimeException("'Entity' annotation not found:" + entityClass.getName());
			}

			Map<String, Field> fields = ClassUtils.getAllFields(entityClass);

			mapping.startObject().startObject(entity.indexType()).startObject("properties");
			for (Field field : fields.values()) {

				SubEntity subEntity = field.getAnnotation(SubEntity.class);
				if(subEntity != null) {
					//id
					id = ClassUtils.getAnnotationedField(field.getType(), Id.class);
					if (id != null) {
						idField =  id.getName();
					}
					Map<String, Field> subFields = ClassUtils.getAllFields(field.getType());
					if(subFields != null && !subFields.isEmpty()) {
						mapping.startObject(subEntity.indexType()).startObject("properties");
						for (Field subField : subFields.values()) {
							Indexed indexed = subField.getAnnotation(Indexed.class);
							if (indexed == null) {
								continue;
							}
							addMapping(mapping, subField, indexed);
						}
						mapping.endObject().endObject();
					}
					continue;
				}
				Indexed indexed = field.getAnnotation(Indexed.class);
				if (indexed == null) {
					continue;
				}

				addMapping(mapping, field, indexed);
			}

			mapping.endObject().endObject().endObject();

			return mapping;
		} catch (Exception e) {
			throw new RuntimeException("can not create mapping for '" + entityClass.getName() + "' : " + e.getMessage());
		}
	}

	public synchronized XContentBuilder getSetting() {
		if (mapping != null) {
			return mapping;
		}
		try {
			mapping = XContentFactory.jsonBuilder().prettyPrint();

			Entity entity = entityClass.getAnnotation(Entity.class);
			if (entity == null) {
				throw new RuntimeException("'Entity' annotation not found:" + entityClass.getName());
			}

			Settings settings = entityClass.getAnnotation(Settings.class);
			if (settings == null) {
				throw new RuntimeException("'Entity' annotation not found:" + entityClass.getName());
			}

			int shards = settings.shards();
			int replicas = settings.replicas();
			int refresh = settings.refresh();
			String termIndex = settings.termIndex();

			boolean analysis = settings.analysis();
			String[] analyzers = settings.analyzer();
			String analyzerType = settings.analyzerType();
			String[] analyzerTokens = settings.analyzerToken();

			String tokenizer = settings.tokenizer();
			String tokenizerType = settings.tokenizerType();
			int compundLength = settings.compundLength();

			boolean routing = settings.routing();
			String routPath = settings.routPath();

			mapping.startObject();
			mapping.field("number_of_shards", shards);
			mapping.field("number_of_replicas", replicas);

			mapping.startObject("index");
			mapping.field("refresh_interval", refresh+"s");
			mapping.field("term_index_interval", termIndex);

			mapping.startObject("store").startObject("compress");
			mapping.field("stored", "true");
			mapping.field("tv", "true");
			mapping.endObject().endObject(); // compress close / store close

			if(analysis) {
				mapping.startObject("analysis");
				mapping.startObject("analyzer");

				for(int i=0; i<analyzers.length; i++) {
					mapping.startObject(analyzers[i]);
					mapping.field("type", analyzerType);
					mapping.field("tokenizer", analyzerTokens[i]);
					mapping.endObject(); // input analyzer close
				}
				mapping.endObject(); // analyzer close

				mapping.startObject("tokenizer").startObject(tokenizer);
				mapping.field("type", tokenizerType);
				mapping.field("compound_noun_min_length", compundLength);
				mapping.endObject().endObject();
				mapping.endObject(); // analysis
			}

			mapping.endObject(); // index close

			if(routing) {
				mapping.startObject("routing");
				mapping.field("required", "true");
				mapping.field("path", routPath);
				mapping.endObject(); // routing close
			}

			mapping.endObject();

			logger.debug(mapping.string());

			return mapping;
		} catch (Exception e) {
			throw new RuntimeException("can not create mapping for '" + entityClass.getName() + "'", e);
		}
	}

	public synchronized String getIndexName() {
		return indexName;
	}

	public synchronized String getIndexType() {
		return indexType;
	}

	public E entity(GetResponse res) {
		if (!res.isExists()) {
			return null;
		}
		Map<String, Object> map = res.getSource();
		return entity(map);
	}

	public E entity(Map<String, Object> map) {
		try {
			E obj = objectFactory.newInstance();
			objectFactory.setProperties(obj, map);
			return obj;
		} catch (Exception e1) {
			logger.error("error", e1);
		}
		return null;
	}

	public List<E> entity(SearchResponse res) {
		List<E> result = new ArrayList<E>();
		SearchHits searchHits = res.getHits();
		SearchHit[] hits = searchHits.getHits();
		for (SearchHit hit : hits) {
			E e = entity(hit.getSource());
			result.add(e);
		}
		return result;
	}

	public Class<E> getEntityClass() {
		return this.entityClass;
	}

	public String getIdAnnotationFieldName() {
		return idField;
	}

	public Object getIdValue(Object entity){
		String idFieldName = getIdAnnotationFieldName();
		if(idFieldName == null){
			return null;
		}
		return ObjectUtils.getPropertyValue(entity, idFieldName);
	}

}

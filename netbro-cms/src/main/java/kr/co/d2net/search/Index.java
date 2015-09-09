package kr.co.d2net.search;

import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.FilterBuilders.prefixFilter;

import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.utils.ClassUtils;
import kr.co.d2net.utils.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.count.CountRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchOperationThreading;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class Index<E> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private Mapper<E> mapper ;
	private Cluster cluster = Cluster.getInstance();

	public Index(Class<E> clsss){
		mapper = (Mapper<E>)Cluster.getInstance().getMapper(clsss);
	}

	protected Index(){
		Class<E> entityClass = ClassUtils.getGenericActualType(this.getClass(), 0);
		mapper = (Mapper<E>)Cluster.getInstance().getMapper(entityClass);
	}

	public E get(String id) {
		GetResponse res = cluster.get(getIndexName(), getIndexType(), id);
		return mapper.entity(res);
	}

	public boolean exists(String id){
		GetResponse res = cluster.get(getIndexName(), getIndexType(), id);
		return res.isExists();
	}

	public void createSchemeOnClusterIfNotExists() {
		if (!existIndexAtCluster()) {
			createIndexAtCluster();
			createMappingAtCluster();
		}
	}

	public SearchRequestBuilder createSearchRequestBuilder() {
		return cluster.makeSearchRequestBuilder(getIndexName());
	}

	public List<CountEntry> facetQuery(QueryBuilder query, String facetField, int size) {
		return facetQuery(query, FacetBuilders.termsFacet(facetField).field(facetField).size(size), facetField);
	}

	public List<CountEntry> facetQuery(QueryBuilder query,TermsFacetBuilder facetBuilder,String facetName) {
		List<CountEntry> result = new ArrayList<CountEntry>();
		TermsFacet facet = (TermsFacet)facet(query, facetBuilder, facetName);
		List<? extends Entry> list = facet.getEntries();
		for (Entry e : list) {
			CountEntry ce = new CountEntry(e.getCount(), e.getTerm());
			result.add(ce);
		}
		return result;
	}

	public List<CountEntry> facetQuery(QueryBuilder query, DateHistogramFacetBuilder facetBuilder,String facetName) {
		List<CountEntry> result = new ArrayList<CountEntry>();
		DateHistogramFacet facet = (DateHistogramFacet) facet(query, facetBuilder, facetName);
		List<? extends DateHistogramFacet.Entry> list = facet.getEntries();
		for (DateHistogramFacet.Entry e : list) {
			CountEntry ce = new CountEntry(e.getCount(), e.getTime());
			result.add(ce);
		}
		return result;
	}

	public Facet facet(QueryBuilder query, FacetBuilder facetBuilder, String facetName) {
		SearchRequestBuilder search = createSearchRequestBuilder();
		search.setQuery(query);
		search.addFacet(facetBuilder);
		return search.execute().actionGet().getFacets().facetsAsMap().get(facetName);
	}

	public long countQuery(QueryBuilder query) {
		CountRequest request = Requests.countRequest(this.getIndexName());
		request.query(query);
		return cluster.getClient().count(request).actionGet().getCount();
	}

	public List<E> executeQuery(QueryBuilder query) {
		SearchRequestBuilder search = cluster.makeSearchRequestBuilder(getIndexName());
		search.setQuery(query);
		return executeQuery(search);
	}

	public List<E> executeQuery(QueryBuilder query, int size) {
		SearchRequestBuilder search = cluster.makeSearchRequestBuilder(getIndexName());
		search.setQuery(query);
		search.setSize(size);
		return executeQuery(search);
	}

	public List<E> executeQuery(QueryBuilder query, int from, int size) {
		SearchRequestBuilder search = cluster.makeSearchRequestBuilder(getIndexName());
		search.setQuery(query);
		search.setFrom(from);
		search.setSize(size);

		System.out.println(query);

		return executeQuery(search);
	}

	public List<E> executeQuery(QueryBuilder query, FilterBuilder filter, int from, int size) {
		SearchRequestBuilder search = cluster.makeSearchRequestBuilder(getIndexName());
		search.setQuery(query);
		search.setFrom(from);
		if(filter != null)
			search.setFilter(filter);
		search.setSize(size);

		return executeQuery(search);
	}

	public List<E> executeQuery(SearchRequestBuilder search) {
		search.addSort("ct_id", SortOrder.DESC);
		search.setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD);
		SearchResponse response = search.execute().actionGet();
		return mapper.entity(response);
	}

	public void createMappingAtCluster() {
		logger.debug("create mapping '" + getIndexName() + "' at cluster.");
		cluster.createMapping(this.mapper);
	}

	public void deleteMappingAtCluster() {
		logger.debug("delete mapping '" + getIndexName() + "' at cluster.");
		cluster.deleteMapping(getEntityClass());
	}

	public void createIndexAtCluster() {
		logger.debug("create index '" + getIndexName() + "' at cluster.");
		cluster.createIndex(getEntityClass());
	}

	public boolean existIndexAtCluster() {
		return cluster.existsIndex(this.mapper.getIndexName());
	}

	public Class<?> getEntityClass() {
		return ClassUtils.getGenericActualType(this.getClass(), 0);
	}

	public void addIndex(E e) {
		cluster.index(this.mapper, e);
	}

	public String getIndexName() {
		return mapper.getIndexName();
	}

	public String getIndexType() {
		return mapper.getIndexType();
	}

	public void clearIndex() {
		cluster.clearIndex(getIndexName());
	}

	public long count(String fieldName, Object fieldValue) {
		QueryBuilder query = QueryBuilders.fieldQuery(fieldName, fieldValue);
		return countQuery(query);
	}

	public List<E> query(String fieldName, Object fieldValue) {
		QueryBuilder query = QueryBuilders.fieldQuery(fieldName, fieldValue);
		return executeQuery(query);
	}

	public List<E> query(String fieldName, Object fieldValue, Integer from, Integer size) {
		QueryBuilder query = QueryBuilders.fieldQuery(fieldName, fieldValue);
		return executeQuery(query, from, size);
	}

	/**
	 * Multi Field Search
	 * @param fields
	 * @param fieldValue
	 * @return
	 */
	public long count(Search search) {
		BoolFilterBuilder filter = FilterBuilders.boolFilter();
		if((search.getLft() != null && search.getLft() > 0) && (search.getRgt() != null && search.getRgt() > 0)) {
			filter.must(FilterBuilders.andFilter(
					rangeFilter("lft").from(search.getLft()), rangeFilter("rgt").to(search.getRgt())
					));
		}

		if((search.getCategoryId() != null && search.getCategoryId() > 0) ) {
			filter.must(FilterBuilders.orFilter(
					prefixFilter("nodes", search.getCategoryId()+"."), termFilter("category_id", search.getCategoryId())
					));
		}

		if(search.getStartDt() != null && search.getEndDt() != null) {
			if(search.getDateGb().equals("brdDd"))
				filter.must(rangeFilter("brd_dd").from(search.getStartDt().getTime()).to(search.getEndDt().getTime()));
			else
				filter.must(rangeFilter("reg_dt").from(search.getStartDt().getTime()).to(search.getEndDt().getTime()));
		}

		if(StringUtils.isNotBlank(search.getCtCla())) {
			filter.must(termFilter("ct_cla", search.getCtCla()));
		}

		if(StringUtils.isNotBlank(search.getCtTyp())) {
			filter.must(termFilter("ct_typ", search.getCtTyp()));
		}

		if(StringUtils.isNotBlank(search.getKeyword()))
			return countQuery(filteredQuery(multiMatchQuery(search.getKeyword(), SearchControls.SEARCH_COLUMNS), filter));
		else
			return countQuery(filteredQuery(QueryBuilders.matchAllQuery(), filter));
	}

	public List<E> query(Search search) {
		BoolFilterBuilder filter = FilterBuilders.boolFilter();

		if((search.getLft() != null && search.getLft() > 0) && (search.getRgt() != null && search.getRgt() > 0)) {
			filter.must(FilterBuilders.andFilter(
					rangeFilter("lft").from(search.getLft()), rangeFilter("rgt").to(search.getRgt())
					));
		}

		if((search.getCategoryId() != null && search.getCategoryId() > 0) ) {
			filter.must(FilterBuilders.orFilter(
					prefixFilter("nodes", search.getCategoryId()+"."), termFilter("category_id", search.getCategoryId())
				));
		}

		if(search.getStartDt() != null && search.getEndDt() != null) {
			if(search.getDateGb().equals("brdDd"))
				filter.must(rangeFilter("brd_dd").from(search.getStartDt().getTime()).to(search.getEndDt().getTime()));
			else
				filter.must(rangeFilter("reg_dt").from(search.getStartDt().getTime()).to(search.getEndDt().getTime()));
		}

		if(StringUtils.isNotBlank(search.getCtCla())) {
			filter.must(termFilter("ct_cla", search.getCtCla()));
		}

		if(StringUtils.isNotBlank(search.getCtTyp())) {
			filter.must(termFilter("ct_typ", search.getCtTyp()));
		}

		if(StringUtils.isNotBlank(search.getKeyword()))
			return executeQuery(filteredQuery(multiMatchQuery(search.getKeyword(), SearchControls.SEARCH_COLUMNS), filter), search.getPageFrom(), search.getPageSize());
		else
			return executeQuery(filteredQuery(QueryBuilders.matchAllQuery(), filter), search.getPageFrom(), search.getPageSize());
	}

	public long countWithBetween(String fieldName, Object fieldValue, String betweenField,  Long beginOf, Long endOf) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		return countQuery(query);
	}

	public long countWithBetween(String fieldName, Object fieldValue, String[] betweenFields,  Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}

		return countQuery(query);
	}

	public long countWithBetween(String fieldName, Object fieldValue, String betweenField, String beginOf, String endOf) {
		return countWithBetween(fieldName, fieldValue, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String betweenField,  Long beginOf, Long endOf) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String[] betweenFields,  Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String betweenField, String beginOf, String endOf) {
		return queryWithBetween(fieldName, fieldValue, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String betweenField,  Long beginOf, Long endOf, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String[] betweenFields,  Object[] beginOfs, Object[] endOfs, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.fieldQuery(fieldName, fieldValue));
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String fieldName, Object fieldValue, String betweenField, String beginOf, String endOf, Integer from, Integer size) {
		return queryWithBetween(fieldName, fieldValue, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null), from, size);
	}

	public long count(String[] fieldNames, Object[] fieldValues) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.must(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		return countQuery(query);
	}

	public List<E> query(String[] fieldNames, Object[] fieldValues) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.must(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		return executeQuery(query);
	}

	public List<E> query(String[] fieldNames, Object[] fieldValues, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.must(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		return executeQuery(query, from, size);
	}

	public long countWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField, Long beginOf, Long endOf) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.should(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenField != null) {
			query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		}
		return countQuery(query);
	}

	public long countWithBetween(String[] fieldNames, Object[] fieldValues, String[] betweenFields, Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.should(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenFields != null) {
			for(int i=0; i < betweenFields.length; i++) {
				if(betweenFields[i]!=null){
					query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
				}
			}
		}
		return countQuery(query);
	}

	public long countWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField, String beginOf, String endOf) {
		return countWithBetween(fieldNames, fieldValues, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField,  Long beginOf, Long endOf) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.must(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenField != null) {
			query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		}
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String[] betweenFields,  Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.must(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenFields != null) {
			for(int i=0; i < betweenFields.length; i++) {
				query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
			}
		}
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField, String beginOf, String endOf) {
		return queryWithBetween(fieldNames, fieldValues, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField,  Long beginOf, Long endOf, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.should(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenField != null) {
			query.must(QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf));
		}
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String[] betweenFields,  Object[] beginOfs, Object[] endOfs, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if(fieldNames != null) {
			for (int i = 0; i < fieldNames.length; i++) {
				query.should(QueryBuilders.fieldQuery(fieldNames[i], fieldValues[i]));
			}
		}
		if(betweenFields != null) {
			for(int i=0; i < betweenFields.length; i++) {
				if(betweenFields[i]!=null){
					query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
				}
			}
		}
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String[] fieldNames, Object[] fieldValues, String betweenField, String beginOf, String endOf, Integer from, Integer size) {
		return queryWithBetween(fieldNames, fieldValues, betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null), from, size);
	}

	public long countWithBetween(String betweenField, Long beginOf, Long endOf) {
		QueryBuilder query = QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf);
		return countQuery(query);
	}

	public long countWithBetween(String[] betweenFields, Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}
		return countQuery(query);
	}

	public long countWithBetween(String betweenField, String beginOf, String endOf) {
		return countWithBetween(betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String betweenField, Long beginOf, Long endOf) {
		QueryBuilder query = QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf);
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String[] betweenFields, Object[] beginOfs, Object[] endOfs) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}
		return executeQuery(query);
	}

	public List<E> queryWithBetween(String betweenField, String beginOf, String endOf) {
		return queryWithBetween(betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null));
	}

	public List<E> queryWithBetween(String betweenField, Long beginOf, Long endOf, Integer from, Integer size) {
		QueryBuilder query = QueryBuilders.rangeQuery(betweenField).from(beginOf).to(endOf);
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String[] betweenFields, Object[] beginOfs, Object[] endOfs, Integer from, Integer size) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		for(int i=0; i < betweenFields.length; i++) {
			query.must(QueryBuilders.rangeQuery(betweenFields[i]).from(beginOfs[i]).to(endOfs[i]));
		}
		return executeQuery(query, from, size);
	}

	public List<E> queryWithBetween(String betweenField, String beginOf, String endOf, Integer from, Integer size) {
		return queryWithBetween(betweenField, DateUtils.getFmtDateLong(beginOf, null), DateUtils.getFmtDateLong(endOf, null), from, size);
	}

	public long countAll() {
		QueryBuilder query = QueryBuilders.matchAllQuery();
		return countQuery(query);
	}

	public List<E> queryAll() {
		QueryBuilder query = QueryBuilders.matchAllQuery();
		return executeQuery(query);
	}

	public List<E> queryAll(Integer from, Integer size) {
		QueryBuilder query = QueryBuilders.matchAllQuery();
		return executeQuery(query, from, size);
	}

	public void closeConnection() {
		cluster.closeClient();
	}
}

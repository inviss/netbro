package kr.co.d2net.search;

import static org.elasticsearch.index.query.FilterBuilders.prefixFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.dto.search.Search;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.ClassUtils;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Index<T> {

	final static Logger logger = LoggerFactory.getLogger(Index.class);
	
	private static Configure configure  = Configure.getInstance();

	public void createMappingAtCluster(){
		Elastics.createMapping(getEntityClass());
	}

	public void deleteMappingAtCluster(){
		Elastics.deleteMapping(getEntityClass());
	}

	public void createIndexAtCluster() throws IOException {
		Elastics.createIndex(getEntityClass());
	}

	public boolean existIndexAtCluster(){
		return Elastics.existsIndex(getEntityClass());
	}

	public Class<?> getEntityClass(){
		return ClassUtils.getGenericActualType(this.getClass(), 0);
	}

	public void addIndex(T t) {
		Elastics.index(t);
	}
	
	public void updateIndex(T t) {
		Elastics.update(t);
	}

	public T getIndex(Long id) {
		GetResponse response = Elastics.get(getIndexName(), getIndexType(), String.valueOf(id));
		return Mappers.entity(getEntityClass(), response);
	}

	public void deleteIndex(Long id) {
		Elastics.delete(getIndexName(), getIndexType(), String.valueOf(id));
	}

	public String getIndexName(){
		return Mappers.getIndexName(getEntityClass());
	}

	public String getIndexType(){
		Class<?> clsss = ClassUtils.getGenericActualType(this.getClass(), 0);
		return Mappers.getIndexType(clsss);
	}

	public void clearIndex(){
		Elastics.clearIndex(getIndexName());
	}
	
	public QueryBuilder queryBuilder(Search search) {
		BoolFilterBuilder filter = FilterBuilders.boolFilter();

		if(search.getCategoryId() != null && search.getCategoryId() > 0) {
			filter.must(FilterBuilders.orFilter(
					prefixFilter("nodes", search.getNodes()+"."), termFilter("category_id", search.getCategoryId())
					));
		}

		if(search.getBrdStartDt() != null && search.getBrdEndDt() != null) {
			filter.must(rangeFilter("brd_dd").from(search.getBrdStartDt().getTime()).to(search.getBrdEndDt().getTime()));
		}

		if(search.getRegStartDt() != null && search.getRegEndDt() != null) {
			filter.must(rangeFilter("reg_dt").from(search.getRegStartDt().getTime()).to(search.getRegEndDt().getTime()));
		}

		if(StringUtils.isNotBlank(search.getCtCla())) {
			filter.must(termFilter("ct_cla", search.getCtCla()));
		}

		if(StringUtils.isNotBlank(search.getCtTyp())) {
			filter.must(termFilter("ct_typ", search.getCtTyp()));
		}
		
		if(StringUtils.isNotBlank(search.getRistClfCd())) {
			filter.must(termFilter("rist_clf_cd", search.getCtTyp()));
		}
		
		if(StringUtils.isNotBlank(search.getKeyword()))
			return filteredQuery(multiMatchQuery(search.getKeyword(), configure.getSearchColumns().split(",")), filter);
		else
			return filteredQuery(QueryBuilders.matchAllQuery(), filter);
	}
	
	public long count(Search search) {
		return countQuery(queryBuilder(search));
	}

	public List<T> query(Search search) {
		return executeQuery(queryBuilder(search), search.getPageFrom(), search.getPageSize());
	}
	
	public List<FacetEntry> facet(Search search) {
		return facetQuery(queryBuilder(search), "category_id", 10);
	}
	
	public List<FacetEntry> facetQuery(QueryBuilder query, String facetField, int size) {
		return facetQuery(query, FacetBuilders.termsFacet(facetField).field(facetField).size(size), facetField);
	}

	public List<FacetEntry> facetQuery(QueryBuilder query, TermsFacetBuilder facetBuilder, String facetName) {
		List<FacetEntry> result = new ArrayList<FacetEntry>();
		TermsFacet facet = (TermsFacet)facet(query, facetBuilder, facetName);
		List<? extends Entry> list = facet.getEntries();
		for (Entry e : list) {
			FacetEntry ce = new FacetEntry(e.getCount(), e.getTerm());
			result.add(ce);
		}
		return result;
	}
	
	public Facet facet(QueryBuilder query, FacetBuilder facetBuilder, String facetName) {
		SearchRequestBuilder search = Elastics.makeSearchRequestBuilder(getIndexName(), getIndexType());
		search.setQuery(query);
		search.addFacet(facetBuilder);
		return search.execute().actionGet().getFacets().facetsAsMap().get(facetName);
	}
	
	public long countQuery(QueryBuilder query) {
		SearchRequestBuilder search = Elastics.makeSearchRequestBuilder(getIndexName(), getIndexType());
		search.setQuery(query);
		search.setSize(1);

		return executeSearchAsTotalHits(search);
	}

	public List<T> executeQuery(QueryBuilder query, int from, int size) {
		SearchRequestBuilder search = Elastics.makeSearchRequestBuilder(getIndexName(), getIndexType());
		search.setQuery(query);
		search.setFrom(from);
		search.setSize(size);
		
		return executeSearchAsResult(search);
	}

	protected long executeSearchAsTotalHits(SearchRequestBuilder search){
		SearchResponse response = search.execute().actionGet();
		long totalHits = response.getHits().getTotalHits();
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("Query total %d hits use %d ms", totalHits, response.getTookInMillis()));
		}
		return totalHits;
	}

	protected List<T> executeSearchAsResult(SearchRequestBuilder search) {
		search.addSort("ct_id", SortOrder.DESC);
		//search.setOperationThreading(SearchOperationThreading.THREAD_PER_SHARD);
		SearchResponse response = search.execute().actionGet();
		return Mappers.entity(getEntityClass(), response);
	}

}

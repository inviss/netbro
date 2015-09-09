package kr.co.d2net.search.demo.facet;

import java.util.List;

import kr.co.d2net.search.demo.index.CategoryIndex;
import kr.co.d2net.utils.JSON;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;


public class DateHistogramFacetMain {

	public static void main(String[] args) {
		CategoryIndex index = new CategoryIndex();
		
		DateHistogramFacetBuilder facetBuilder = FacetBuilders.dateHistogramFacet("facet1").field("reg_dt").interval("day");
		DateHistogramFacet facet = (DateHistogramFacet) index.facet(QueryBuilders.matchAllQuery(), facetBuilder, "facet1");
		List<? extends DateHistogramFacet.Entry>  list = facet.getEntries();
		
		for(DateHistogramFacet.Entry e : list){
			System.out.println(JSON.toJsonString(e));
		}
	}
}

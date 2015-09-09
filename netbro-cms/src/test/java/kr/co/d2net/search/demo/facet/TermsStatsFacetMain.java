package kr.co.d2net.search.demo.facet;

import java.util.List;

import kr.co.d2net.search.demo.index.CategoryIndex;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacetBuilder;


public class TermsStatsFacetMain {

	public static void main(String[] args) {
		CategoryIndex index = new CategoryIndex();
		
		TermsStatsFacetBuilder facetBuilder = FacetBuilders.termsStatsFacet("facet1").keyField("appkey").valueField("day");
		facetBuilder.size(10);
		//facetBuilder.facetFilter(FilterBuilders.numericRangeFilter("datetime").from(0).to(DateUtils.getZeroHourMillis(new Date())));
		
		TermsStatsFacet facet = (TermsStatsFacet) index.facet(QueryBuilders.matchAllQuery(), facetBuilder, "facet1");
		List<? extends TermsStatsFacet.Entry>  list = facet.getEntries();
		
		for(TermsStatsFacet.Entry e : list){
			System.out.println(e.getTerm() +" : "  + e.getCount() +" , "+ e.getTotalCount());
		}
		
		System.out.println(index.count("appkey", "APPKEY93"));
	}
}

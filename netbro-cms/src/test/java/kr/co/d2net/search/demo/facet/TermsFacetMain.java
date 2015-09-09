package kr.co.d2net.search.demo.facet;

import java.util.List;

import kr.co.d2net.search.demo.index.CategoryIndex;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;


public class TermsFacetMain {

	public static void main(String[] args) {
		CategoryIndex index = new CategoryIndex();
		
		TermsFacetBuilder facetBuilder = FacetBuilders.termsFacet("facet1").fields("appkey");
		facetBuilder.size(100);
		TermsFacet facet = (TermsFacet) index.facet(QueryBuilders.matchAllQuery(), facetBuilder, "facet1");
		List<? extends TermsFacet.Entry>  list = facet.getEntries();
		for(TermsFacet.Entry e : list){
			System.out.println(e.getTerm() +" : "  + e.getCount());
		}

		System.out.println(index.countQuery(QueryBuilders.fieldQuery("appkey", "APPKEY93")));
		System.out.println(index.countQuery(QueryBuilders.termQuery("appkey", "APPKEY93")));
	}
}

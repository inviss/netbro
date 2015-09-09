package kr.co.d2net.search.demo.facet;

import java.util.List;
import java.util.concurrent.TimeUnit;

import kr.co.d2net.search.demo.index.CategoryIndex;
import kr.co.d2net.utils.JSON;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.facet.histogram.HistogramFacet.ComparatorType;
import org.elasticsearch.search.facet.histogram.HistogramFacetBuilder;


public class HistogramFacetMain {

	public static void main(String[] args) {
		CategoryIndex index = new CategoryIndex();
		
		HistogramFacetBuilder facetBuilder = FacetBuilders.histogramFacet("facet1").keyField("date").valueField("day").interval(1,TimeUnit.DAYS);
		facetBuilder.comparator(ComparatorType.KEY);

		HistogramFacet facet = (HistogramFacet) index.facet(QueryBuilders.matchAllQuery(), facetBuilder, "facet1");
		List<? extends HistogramFacet.Entry>  list = facet.getEntries();
		
		for(HistogramFacet.Entry e : list){
			System.out.println(JSON.toJsonString(e));
		}
	}
}

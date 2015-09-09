package kr.co.d2net.search.demo.index;

import kr.co.d2net.search.Index;
import kr.co.d2net.search.index.SearchMeta;
import kr.co.d2net.search.index.SearchMetaIndex;

public class CategoryIndex extends Index<SearchMeta>{

	public static void main(String[] args) {
		SearchMetaIndex categoryIndex = new SearchMetaIndex();
		SearchMeta category = new SearchMeta();

		category.setCategoryId(100);
		category.setSegmentId(1);
		category.setEpisodeId(1);
		category.setCategoryNm("카테고리 "+1);
		category.setEpisodeNm("카테고리 "+1+" 에피소드 "+1+"회");
		category.setSegmentNm("세그먼트");
		
		categoryIndex.addIndex(category);
	}

}

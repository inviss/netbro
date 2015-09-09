package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.Set;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.service.ContentsServices;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentsTest extends BaseDaoConfig {
	
	@Autowired
	private ContentsServices contentsServices;

	@Ignore
	@Test
	public void findContentsAll() {
		try {
			Search search = new Search();
			search.setKeyword("테스트");
			search.setCtiFmt("1");
			contentsServices.findFilterAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void findContents() {
		try {
		///	contentsDaoJpa.findContents(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void getContent() {
		try {
			ContentsTbl contentsTbl = contentsServices.getContentObj(1L);
			Set<ContentsInstTbl> contentsInstTbls = contentsTbl.getContentsInst();
			for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
				System.out.println(contentsInstTbl.getOrgFileNm());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void addAll() {
		try {
			
			Set<ContentsTbl> contents = new HashSet<ContentsTbl>();
			
			ContentsTbl content = new ContentsTbl();
			
			content.setCategoryId(5);
			content.setEpisodeId(2);
			content.setSegmentId(1);
			content.setCtNm("test");
			//content.setSegmentTbl(segmentTbl);
			contents.add(content);
			
			//categoryServices.addAll(categories);
			contentsServices.add(content);
			//contentsServices.find();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void countAll() {
		try {
			Search search = new Search();
			//search.setKeyword("테스트");
			search.setCtiFmt("1");
			contentsServices.countAllContents(search);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Ignore
	@Test
	public void findTest() {
		try {
			Search search = new Search();
		//	search.setKeyword("11");
			search.setCtiFmt("10");
			search.setPageNo(0);
			contentsServices.getAllContentsInfo(search);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

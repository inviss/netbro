package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.service.EpisodeServices;


public class EpisodeTest extends BaseDaoConfig {
	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EpisodeServices episodeServices;
	
	@Ignore
	@Test
	public void addAll() {
		try {
			Set<EpisodeTbl> categories = new HashSet<EpisodeTbl>();
			
			EpisodeTbl episodeTbl = new EpisodeTbl();
			EpisodeId id = new EpisodeId();
			
			id.setCategoryId(2);
			id.setEpisodeId(3);
			
			episodeTbl.setId(id);
			categories.add(episodeTbl);
			
			episodeServices.add(episodeTbl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void EpisodeList() {
		try {

			System.out.println("i`m here");
			String episodeNm ="없음";
			int categoryId=1;
			logger.debug("search.getEpisodeNm()"+episodeNm);
			logger.debug("search.getCategoryId()"+categoryId);
			Search search = new Search();
			search.setCategoryId(categoryId);
			search.setEpisodeNm(episodeNm);
			List<EpisodeTbl> EpisodeTbls = episodeServices.episodeSearchList(search);
			ModelAndView view = new ModelAndView();
			
			view.addObject("epsodeTbls", EpisodeTbls);
			
			view.setViewName("jsonView");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Ignore
	@Test
	public void UpdateTest(){
		try {
			int categoryId = 361;
			EpisodeId id = new EpisodeId();
			id.setCategoryId(361);
			id.setEpisodeId(2);
			EpisodeTbl episode =  episodeServices.episodeInfo(id);
			logger.debug("#################" + episode.getEpisodeNm());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void DeleteTest(){
		try {
			int categoryId = 361;
			EpisodeId id = new EpisodeId();
			id.setCategoryId(30);
			id.setEpisodeId(2);
			EpisodeTbl ep = new EpisodeTbl();
			ep.setId(id);
			 episodeServices.delete(ep);
			logger.debug("#################" );
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void maxTest(){
		try {
			Integer categoryId=30;
			 episodeServices.MaxCount(categoryId);
			logger.debug("#################" );
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.dao.EpisodeDao;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("episodeService")
@Transactional(readOnly=true)
public class EpisodeServiceImpl implements EpisodeService {

	private final static Logger logger = LoggerFactory.getLogger(EpisodeServiceImpl.class);
	
	@Autowired
	EpisodeDao episodeDao;
	
	@Override
	public void insertEpisode(EpisodeTbl episodeTbl) throws ServiceException {
		episodeDao.save(episodeTbl);
	}

	@Override
	public void updateEpisode(EpisodeTbl episodeTbl) throws ServiceException {
		episodeDao.save(episodeTbl);
	}

	@Override
	public void deleteEpisode(EpisodeTbl episodeTbl) throws ServiceException {
		episodeDao.delete(episodeTbl);
	}

	@Override
	public Episode getEpisode(Search search) throws ServiceException {
		EpisodeTbl episodeTbl = episodeDao.getEpisodeInfo(search);
		Episode episode = new Episode();
		
		episode.setCategoryId(episodeTbl.getCategoryId());
		episode.setEpisodeId(episodeTbl.getEpisodeId());
		episode.setUseYn(episodeTbl.getUseYn().toString());
		episode.setRegDt(episodeTbl.getRegDt());
		episode.setRegrId(episodeTbl.getRegrId()); 
		
		return episode;
	}

	@Override
	public List<Episode> findEpisodeList(Search search) throws ServiceException {
		
		List<EpisodeTbl> episodeTbls = null;
		try{
			episodeTbls = episodeDao.findEpisodeInfoList(search);
		}catch(Exception e){
			logger.error("err  "+e);
		}
		
		
		List<Episode> episodes = new ArrayList<Episode>();
		
		/*for(EpisodeTbl episodeTbl : episodeTbls){
			Episode episode = new Episode();
			
			episode.setCategoryId(episodeTbl.getCategoryId());
			episode.setEpisodeId(episodeTbl.getEpisodeId());
			episode.setUseYn(episodeTbl.getUseYn().toString());
			episode.setRegDt(episodeTbl.getRegDt());
			episode.setRegrId(episodeTbl.getRegrId()); 
			
			episodes.add(episode);
		}
		 */
		return episodes;
	}

	@Override
	public Long countEpisode(Search search) throws ServiceException {
		return episodeDao.count(search);
	}}

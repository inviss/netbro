package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface EpisodeService {
 
	/**
	 * 에피소드 정보를 신규로 저장한다.
	 * 
	 * @param episodeTbl
	 * @throws ServiceException
	 */
	public void insertEpisode(EpisodeTbl episodeTbl) throws ServiceException;
	
	/**
	 * 에피소드 정보를 수정한다
	 * @param episodeTbl
	 * @throws ServiceException
	 */
	public void updateEpisode(EpisodeTbl episodeTbl) throws ServiceException;
	
	/**
	 * 에피소드 정보를 삭제한다.
	 * 에피소드를 삭제하기전에 기본 episode로 등록된 contents 정보가 있는지를 확인하고 
	 * 없다면 삭제하고 있다면 실패처리한다.
	 * @param episodeTbl
	 * @throws ServiceException
	 */
	public void deleteEpisode(EpisodeTbl episodeTbl) throws ServiceException;
	
	/**
	 * 에피소드 상세 정보를 조회한다
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public Episode getEpisode(Search search) throws ServiceException;
	
	/**
	 * categoryId에 등록된 모든 카테고리 정보를 구한다.
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public List<Episode> findEpisodeList(Search search) throws ServiceException;
	
	/**
	 * 카테로리에 등록된 에피소드의 총갯수를 구한
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public Long countEpisode(Search search) throws ServiceException;
	
}

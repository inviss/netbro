package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface EpisodeDao {
	
	 /**
	  *  에피소드 정보를 저장한다.
	  *  episodeId가 null 또는  0일때는 insert, 그 이외의 경우에는 update를 한다.
	  * @param episodeTbl
	  * @throws DaoRollbackException
	  */
	public void save(EpisodeTbl episodeTbl) throws DaoRollbackException;
	
	 /**
	  *  에피소드 정보를 삭제한다 
	  * @param episodeTbl
	  * @throws DaoRollbackException
	  */
	public void delete(EpisodeTbl episodeTbl) throws DaoRollbackException;
	
	/**
	 * categoryID에 등록된 에피소드 리스트를 구한다.
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<EpisodeTbl> findEpisodeInfoList(Search search) throws DaoNonRollbackException;
	
   /**
    * 등록된 episdoe의 갯수를 구한다.
    * @param search
    * @return
    * @throws DaoNonRollbackException
    */
	public Long count(Search search) throws DaoNonRollbackException;
	
	/**
	 * episode의 단일 건 정보를 구한다.
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public EpisodeTbl getEpisodeInfo(Search search) throws DaoNonRollbackException;
	
	   /**
	    * 카테고리에 소속된 에피소드id중 가장 큰값을 조회한다.
	    * @param search
	    * @return
	    * @throws DaoNonRollbackException
	    */
		public Integer getMaxEpisdoe(Integer categoryId) throws DaoNonRollbackException;
	
}

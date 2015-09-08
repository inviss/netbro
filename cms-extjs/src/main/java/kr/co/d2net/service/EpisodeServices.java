package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.EpisodeDao;
import kr.co.d2net.dao.filter.EpissodeLikeSpecifications;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 에피소드 관련정보를 조회하기위해서 사용되는 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class EpisodeServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EpisodeDao episodeDao;

	@Autowired
	private SegmentServices segmentServices;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;


	/**
	 * 에피소드를 신규로 추가한다.
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void add(EpisodeTbl episode) throws ServiceException {
		episodeDao.save(episode);
	}

	/**
	 * 신규 에피소드 정보를 저장한다
	 * @param episodeTbl
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public String addEpisodeInfo(EpisodeTbl episodeTbl, EpisodeId id) throws ServiceException {

		Integer maxEpisodeId = MaxCount(id.getCategoryId());
		EpisodeTbl newEpisode = new EpisodeTbl();

		//카테고리 신규 추가할 때 maxEpisodeId의 값은 없다.
		//maxEpisodeId+1 일때 nullPointException
		if(maxEpisodeId != null){
			id.setEpisodeId(maxEpisodeId+1);
		}else{
			id.setEpisodeId(1);
		}

		newEpisode.setId(id);
		newEpisode.setEpisodeNm(episodeTbl.getEpisodeNm());
		newEpisode.setRegDt(new Date());
		newEpisode.setUseYn("Y");
		
		episodeDao.save(newEpisode);

		//20131007 에피소드가 생성되면 세그먼트를 무조건 생성해준다.(임시)
		SegmentTbl segment = new SegmentTbl();
		SegmentId segid = new SegmentId();
		segid.setCategoryId(id.getCategoryId());
		segid.setEpisodeId(maxEpisodeId);
		
		//카테고리 신규 추가할 때 maxEpisodeId의 값은 없다.
		//maxEpisodeId+1 일때 nullPointException
		if(logger.isDebugEnabled()){
			logger.debug("segid.getEpisodeId()    "+segid.getEpisodeId());
		}

		if(segid.getEpisodeId() != null){
			segid.setEpisodeId(maxEpisodeId+1);
			segid.setSegmentId(1);
		}else{
			segid.setEpisodeId(1);
			segid.setSegmentId(1);
		}

		segment.setId(segid);
		segment.setSegmentNm("기본값");

		segmentServices.add(segment);

		return "Y";

	}

	/**
	 * 에피소드 정보를 삭제한다.  
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void delete(EpisodeTbl episode)throws ServiceException {
		episodeDao.delete(episode.getId());
	}

	/**
	 * 에피소드 정보를 삭제한다. 세그먼트정보를 먼저 삭제후 에피소드 정보를 삭제한다.
	 * @param episode
	 */
	@Modifying
	@Transactional
	public void deleteEpisodeInfo(EpisodeTbl episode)throws ServiceException{

		/*2013.10.08 세그먼트를 무조건 삭제한다*/
		SegmentId segid = new SegmentId();
		segid.setCategoryId(episode.getId().getCategoryId());
		segid.setEpisodeId(episode.getId().getEpisodeId());
		
		/*2013.12.06 현재 세그먼트는 무조건 생성임으로 1이다.		 */
		segid.setSegmentId(1);
		segmentServices.delete(segid);

		episodeDao.delete(episode.getId());
	}


	/**
	 * 에피소드 정보를 조회한다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Episode> episodeSearchList(Search search)throws ServiceException {

		List<Episode> episdoes = new ArrayList<Episode>();
		String[] ctFields = {"episodeNm","episodeId", "regDt", "regrId","categoryId"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<EpisodeTbl> from = criteriaQuery.from(EpisodeTbl.class); 


		criteriaQuery.where(ObjectLikeSpecifications.episodeNmLike(criteriaBuilder, criteriaQuery, from, search));

		Selection<String>[] s = new Selection[ctFields.length];

		int i=0;
		for(int j=0; j<ctFields.length; j++) {
			if(ctFields[j].equals("episodeId")){
				s[i] = from.get("id").get("episodeId");
			}else if(ctFields[j].equals("categoryId")){
				s[i] = from.get("id").get("categoryId");
			}else{
				s[i] = from.get(ctFields[j]);
			}
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("id").get("episodeId")));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 
		 * searchTyp가 imgage면 한페이지에 25개씩
		 * searchTyp가 list면 한페이지에 50개씩
		 */
		int startPage = 0;
		int endPage = 0;

		startPage = (search.getPageNo()-1) * SearchControls.EPISODE_LIST_COUNT;
		endPage = startPage+SearchControls.EPISODE_LIST_COUNT;

		if(logger.isDebugEnabled()){
			logger.debug("startPage :"+startPage);
			logger.debug("endPage :"+endPage);
		}

		typedQuery.setFirstResult(startPage);
		typedQuery.setMaxResults(endPage);

		List<Object[]> list2 = typedQuery.getResultList();


		for(Object[] list : list2) {
			Episode episdoe = new Episode();

			i=0;
			for(int j=0; j<ctFields.length; j++) {
				ObjectUtils.setProperty(episdoe, ctFields[j], list[i]);
				i++;
			}
			episdoes.add(episdoe);
		}

		return episdoes;
	}


	/**
	 * 총갯수 조회
	 * @param episode
	 */

	public long allEpisodeCount(Search search)throws ServiceException{

		return episodeDao.count(EpissodeLikeSpecifications.episodeNmLike(search.getCategoryId(), search.getEpisodeNm()));
	}


	/**
	 * 카테고리에 소속된 에피소드id중 가장 높은 값을 조회하는 함수.
	 * @param categoryId
	 * @return
	 */
	public Integer MaxCount(Integer categoryId)throws ServiceException{

		Integer result = 0;

		/*
		 * 서비스단에서는 트랜잭션 단위의 묶음으로 처리해야함.
		 * DB 핸들링은 Dao단에서 처리하도록 위임을 하는게 맞음.
		 */
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);

		Root<EpisodeTbl> from = criteriaQuery.from(EpisodeTbl.class);

		Path<Integer> episodeId = from.get("id").get("episodeId");
		criteriaQuery.select(criteriaBuilder.max(episodeId));

		criteriaQuery.where(criteriaBuilder.equal(from.get("id").get("categoryId"),categoryId));

		result = em.createQuery(criteriaQuery).getSingleResult();

		return result;
	}

	/**
	 * category_id, episode_id를 가지고 에피소드 1건을 조회하는 함수.
	 * @param id
	 * @return
	 */
	public EpisodeTbl getEpisodeObj(EpisodeId id )throws ServiceException{
		EpisodeTbl episodeTbl = null;

		episodeTbl = episodeDao.findOne(id);
		return episodeTbl;
	}


	/**
	 * category_id로 등록되어있는 모든 에피소드를 조회한다.
	 * @param Id
	 * @return
	 */
	public List<EpisodeTbl> findEpisodeList(Integer categoryId )throws ServiceException{
		List<EpisodeTbl> infos= episodeDao.findAll(EpissodeLikeSpecifications.allEpisodeInfo(categoryId));
		return infos;
	}

}

package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.DisuseSpecifications;
import kr.co.d2net.dao.filter.EpisodeSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("episodeDao")
public class EpisodeDaoImpl implements EpisodeDao {

	private final static Logger logger = LoggerFactory.getLogger(EpisodeDaoImpl.class);

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	@Transactional
	public void save(EpisodeTbl episodeTbl) throws DaoRollbackException {
		try{
			logger.debug("getEpisodeId  " + episodeTbl.getEpisodeId());
			logger.debug("getCategoryId  " + episodeTbl.getCategoryId());
			logger.debug("getEpisodeNm  " + episodeTbl.getEpisodeNm());
			repository.save(episodeTbl);
		}catch(Exception e){
			logger.error("episode insert err : " +e);
		}
	}

	@Override
	public List<EpisodeTbl> findEpisodeInfoList(Search search)
			throws DaoNonRollbackException {
		return repository.find(EpisodeTbl.class,EpisodeSpecifications.findEpisodeOnlyByParams(search)).list();
	}

	@Override
	public Long count(Search search) throws DaoNonRollbackException {
		return repository.count(EpisodeTbl.class,EpisodeSpecifications.findEpisodeOnlyByParams(search));
	}

	@Override
	public EpisodeTbl getEpisodeInfo(Search search)
			throws DaoNonRollbackException {
		return repository.find(EpisodeTbl.class,EpisodeSpecifications.getEpisodeWithPK(search.getCategoryId(),search.getEpisodeId())).single();
	}

	@Override
	@Transactional
	public void delete(EpisodeTbl episodeTbl) throws DaoRollbackException {
		repository.remove(episodeTbl);
	}

	@Override
	public Integer getMaxEpisdoe(Integer categoryId) throws DaoNonRollbackException {
		EpisodeTbl episodeTbl = repository.find(EpisodeTbl.class,EpisodeSpecifications.getMaxEpisdoe(categoryId)).size(1).single();
		
		if(episodeTbl == null){
			episodeTbl = new EpisodeTbl();
			episodeTbl.setEpisodeId(0);
		}
		return episodeTbl.getEpisodeId();
	}}

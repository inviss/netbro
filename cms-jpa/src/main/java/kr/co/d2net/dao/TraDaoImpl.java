package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("traDao")
public class TraDaoImpl implements TraDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public List<TraTbl> findTraList(Search search) throws DaoNonRollbackException {
		List<TraTbl> traTbls = null;

		try {
			SpecificationResult<TraTbl> result = repository.find(TraTbl.class);

			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			traTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findProfileList Error", e);
		}
		return traTbls;
	}

	/*
	 * (non-Javadoc)
	 * @see kr.co.d2net.dao.TraDao#getTraCount(kr.co.d2net.dto.vo.Search)
	@Override
	public Integer getTraCount(Search search) throws DaoNonRollbackException {

		TraTbl traTbl = null;

		try {
			traTbl = repository.find(traTbl.class,search);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getProfileInfo Error", e);
		}
	}
	 */

	@Override
	public void updateTraJobStatus(TraTbl tra) throws DaoRollbackException {
		try {
			if(tra.getSeq() != null && tra.getSeq() > 0L){
				TraTbl traTbl = getTraInfo(tra.getSeq());

				traTbl.setJobStatus("I");
				traTbl.setPrgrs(0);

				repository.update(traTbl);
			}else{
				repository.save(tra);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public TraTbl getTraInfo(long traSeq) throws DaoNonRollbackException {
		TraTbl traTbl = null;

		try {
			traTbl = repository.find(TraTbl.class,traSeq);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getProfileInfo Error", e);
		}
		return traTbl;
	}



}

package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.ServiceException;

import org.springframework.stereotype.Repository;

@Repository("proFlDao")
public class ProFlDaoImpl implements ProFlDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public List<ProFlTbl> findProfileList(Search search) throws ServiceException {

		List<ProFlTbl> proFlTbls = null;

		try {
			SpecificationResult<ProFlTbl> result = repository.find(ProFlTbl.class);

			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			proFlTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findProfileList Error", e);
		}

		return proFlTbls;
	}



	@Override
	public ProFlTbl getProfileInfo(Long profileId) throws ServiceException {

		ProFlTbl proFlTbl = null;

		try {
			proFlTbl = repository.find(ProFlTbl.class,profileId);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getProfileInfo Error", e);
		}

		return proFlTbl;
	}
	
	
	@Override
	public void save(ProFlTbl proFlTbl) throws ServiceException {

		try {
			if(proFlTbl.getProFlId() != null && proFlTbl.getProFlId() > 0L)
				repository.save(proFlTbl);
			else
				repository.update(proFlTbl);

		} catch (Exception e) {
			throw new DaoRollbackException("", "Profile save Error", e);
		}
	}

}

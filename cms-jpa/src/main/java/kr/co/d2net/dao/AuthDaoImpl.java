package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.AuthSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;

import org.springframework.stereotype.Repository;

@Repository("authDao")
public class AuthDaoImpl implements AuthDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public List<AuthTbl> findAuthList(Search search) throws DaoNonRollbackException {
		
		List<AuthTbl> authTbls = null;
		
		try {
			SpecificationResult<AuthTbl> result = repository.find(AuthTbl.class);
			
			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			authTbls = result.list();
			
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findAuthList Error", e);
		}
		return authTbls;
	}

	@Override
	public AuthTbl getAuthInfo(Integer authId) throws DaoNonRollbackException {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Integer getAuthMaxId() throws DaoNonRollbackException {
		
		AuthTbl authTbl = null;
		
		try {
			authTbl = repository.find(AuthTbl.class,AuthSpecifications.getAuthMaxId()).single();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}



}

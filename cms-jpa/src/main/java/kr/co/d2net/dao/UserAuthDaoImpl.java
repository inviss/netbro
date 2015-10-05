package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("userAuthDao")
public class UserAuthDaoImpl implements UserAuthDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	
	
	@Override
	public void add(UserAuthTbl user) throws DaoRollbackException {
		try {
			if(StringUtils.isNotBlank(user.getUserId()) && user.getAuthId() != null && user.getAuthId() > 0L){
				repository.update(user);
			}else{
				repository.save(user);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("", "UserAuth save Error", e);
		}
		
	}

	
	
	@Override
	public List<UserAuthTbl> getAuthUseYn(String id) throws DaoNonRollbackException {
		
		List<UserAuthTbl> userAuthTbls = null;
		
		try {
			SpecificationResult<UserAuthTbl> result = repository.find(UserAuthTbl.class);
			userAuthTbls = result.list();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getAuthUseYn Error", e);
		}
		return userAuthTbls;
	}

	
	
	@Override
	public long findUserAuthCount() throws DaoNonRollbackException {
		
		long userAuthCount = 0L;
		
		try {
			userAuthCount = repository.count(UserAuthTbl.class);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findUserAuthCount Error", e);
		}
		// TODO Auto-generated method stub
		return userAuthCount;
	}

	

}

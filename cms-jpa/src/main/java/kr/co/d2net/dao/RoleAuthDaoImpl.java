package kr.co.d2net.dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("roleAuthDao")
public class RoleAuthDaoImpl implements RoleAuthDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public void deleteRoleAuth(Integer authId) throws DaoRollbackException {
		try {
			repository.remove(authId);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public long findRoleAuthCount() throws DaoNonRollbackException {
		long roleAuthCount = 0L;
		try {
			 roleAuthCount = repository.count(RoleAuthTbl.class);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findRoleAuthCount", e);
		}
		return roleAuthCount;
	}

	@Override
	public void save(RoleAuthTbl roleauth) throws DaoRollbackException {
		try {
			if(roleauth.getAuthId() != null && roleauth.getAuthId() > 0L)
				repository.update(roleauth);
			else
				repository.save(roleauth);
		} catch (Exception e) {
			throw new DaoRollbackException("", "RoleAuth save Error", e);
		}

	}



}

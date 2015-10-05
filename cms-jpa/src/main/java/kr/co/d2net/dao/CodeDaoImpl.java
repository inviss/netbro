package kr.co.d2net.dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.Operator;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("codeDao")
public class CodeDaoImpl implements CodeDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;
	
	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}
	
	@Override
	public void save(CodeTbl codeTbl) throws DaoRollbackException {
		if(codeTbl.getOperator() == Operator.UPDATE)
			repository.update(codeTbl);
		else 
			repository.save(codeTbl);
	}
	
}

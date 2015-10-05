package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.d2net.dao.filter.SegmentSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.Operator;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

@Repository(value="segmentDao")
public class SegmentDaoImpl implements SegmentDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;
	
	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}
	
	@Override
	@Transactional
	public void save(SegmentTbl segmentTbl) throws DaoRollbackException {
			repository.save(segmentTbl);
	}

	@Override
	@Transactional
	public void delete(SegmentTbl segmentTbl) throws DaoRollbackException {
		repository.removeAndFlush(segmentTbl);
	}

	@Override
	public List<SegmentTbl> findSegmentList(Search search)
			throws DaoNonRollbackException {
		return repository.find(SegmentTbl.class,SegmentSpecifications.findSegment(search)).list();
	}

}

package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.filter.ContentsModSpecifications;
import kr.co.d2net.dao.filter.NoticeSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("contentsModDao")
public class ContentsModDaoImpl implements ContentsModDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;
	private final static Logger logger = LoggerFactory.getLogger(ContentsModDaoImpl.class);
	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}
	
	@Override
	public void save(ContentsModTbl contentsModTbl) throws DaoRollbackException {
		repository.save(contentsModTbl);
	}

	@Override
	public List<ContentsModTbl> findContentsModList(Search search)
			throws DaoNonRollbackException {
		return repository.find(ContentsModTbl.class,ContentsModSpecifications.findContentsMod(search)).list();
	}}

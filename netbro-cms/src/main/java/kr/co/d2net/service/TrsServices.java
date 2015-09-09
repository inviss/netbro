package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.TrsDao;
import kr.co.d2net.dto.TrsTbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class TrsServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrsDao trsDao;

	@PersistenceContext
	private EntityManager em;


	public Page<TrsTbl> findAllTrs(Specification<TrsTbl> specification, Pageable pageable) {
		return trsDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<TrsTbl> trses) {
		trsDao.save(trses);
	}
	
	@Modifying
	@Transactional
	public void add(TrsTbl trs) {
		trsDao.save(trs);
	}

}

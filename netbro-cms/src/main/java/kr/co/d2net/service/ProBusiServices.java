package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.ProBusiDao;
import kr.co.d2net.dto.ProBusiTbl;

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
public class ProBusiServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProBusiDao proBusiDao;

	@PersistenceContext
	private EntityManager em;


	public Page<ProBusiTbl> findAllProBusi(Specification<ProBusiTbl> specification, Pageable pageable) {
		return proBusiDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<ProBusiTbl> probusies) {
		proBusiDao.save(probusies);
	}
	
	@Modifying
	@Transactional
	public void add(ProBusiTbl probusi) {
		proBusiDao.save(probusi);
	}

}

package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.ProFlDao;
import kr.co.d2net.dto.ProFlTbl;

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
public class ProFlServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProFlDao proFlDao;

	@PersistenceContext
	private EntityManager em;


	public Page<ProFlTbl> findAllProFl(Specification<ProFlTbl> specification, Pageable pageable) {
		return proFlDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<ProFlTbl> profls) {
		proFlDao.save(profls);
	}
	
	@Modifying
	@Transactional
	public void add(ProFlTbl profl) {
		proFlDao.save(profl);
	}

}

package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.DisuseDao;
import kr.co.d2net.dto.DisuseInfoTbl;

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
public class DisuseServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DisuseDao disuseDao;

	@PersistenceContext
	private EntityManager em;


	public Page<DisuseInfoTbl> findAllDisuse(Specification<DisuseInfoTbl> specification, Pageable pageable) {
		return disuseDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<DisuseInfoTbl> disuses) {
		disuseDao.save(disuses);
	}
	
	@Modifying
	@Transactional
	public void add(DisuseInfoTbl disuse) {
		disuseDao.save(disuse);
	}

}

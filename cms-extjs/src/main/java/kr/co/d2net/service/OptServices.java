package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.OptDao;
import kr.co.d2net.dto.Opt;

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
/**
 * 
 * @author Administrator
 *
 */
public class OptServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OptDao optDao;

	@PersistenceContext
	private EntityManager em;


	public Page<Opt> findAllOpt(Specification<Opt> specification, Pageable pageable) {
		return optDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<Opt> opts) {
		optDao.save(opts);
	}
	
	@Modifying
	@Transactional
	public void add(Opt opt) {
		optDao.save(opt);
	}

}

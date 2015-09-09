package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.BusiPartnerDao;
import kr.co.d2net.dto.BusiPartnerTbl;

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
public class BusiPartnerServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BusiPartnerDao busiDao;

	@PersistenceContext
	private EntityManager em;


	public Page<BusiPartnerTbl> findAllBusiPartner(Specification<BusiPartnerTbl> specification, Pageable pageable) {
		return busiDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<BusiPartnerTbl> auths) {
		busiDao.save(auths);
	}
	
	@Modifying
	@Transactional
	public void add(BusiPartnerTbl auth) {
		busiDao.save(auth);
	}

}

package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.BusiPartnerCategoryDao;
import kr.co.d2net.dto.BusiPartnerCategory;

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
public class BusiPartnerCategoryServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BusiPartnerCategoryDao busiDao;

	@PersistenceContext
	private EntityManager em;


	public Page<BusiPartnerCategory> findAllBusiPartnerCategory(Specification<BusiPartnerCategory> specification, Pageable pageable) {
		return busiDao.findAll(specification, pageable);
	}
	
	@Modifying
	@Transactional
	public void addAll(Set<BusiPartnerCategory> busiPartners) {
		busiDao.save(busiPartners);
	}
	
	@Modifying
	@Transactional
	public void add(BusiPartnerCategory busiPartner) {
		busiDao.save(busiPartner);
	}

}

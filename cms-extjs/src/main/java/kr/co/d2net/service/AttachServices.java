package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.AttachDao;
import kr.co.d2net.dao.filter.AttachSpecifications;
import kr.co.d2net.dto.AttachTbl;
import kr.co.d2net.dto.vo.Attatch;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 첨부파일 관련정보를 조회하기위한 함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class AttachServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AttachDao attachDao;

	@PersistenceContext
	private EntityManager em;


	public Page<AttachTbl> findAllAttach(Specification<AttachTbl> specification, Pageable pageable) {
		return attachDao.findAll(specification, pageable);
	}

	@Autowired
	private MessageSource messageSource;


	@Modifying
	@Transactional
	public void add(AttachTbl attachTbl) throws ServiceException {
		attachDao.save(attachTbl);
	}


	@Modifying
	@Transactional
	public void delete(AttachTbl attachTbl) throws ServiceException  {
		attachDao.delete(attachTbl);
	}

	/**
	 * 첨부파일 정보를 조회한다.
	 * @param ctId
	 * @return
	 */
	public List<AttachTbl> findAttachObj(Long ctId) throws ServiceException {
		/* paging 처리할때 사용
		final PageRequest pageRequest = new PageRequest(
				  0, 20, new Sort(
				    new Order(Direction.ASC, "ct_id"), 
				    new Order(Direction.DESC, "other")
				  ) 
				);
		Page<AttachTbl> page = attachDao.findAll(AttachSpecifications.formatLike(ctId), pageRequest);
		 */

		// Order 안에 ','로 구분하여 여러개 지정가능
		Sort sort = new Sort(new Order(Direction.DESC, "seq"));
		return attachDao.findAll(AttachSpecifications.formatLike(ctId), sort);
	}

	
	/**
	 * 첨부파일 정보를 조회한다.
	 * @param ctId
	 * @return
	 */
	public List<Attatch> findAttach(Long ctId) throws ServiceException {
		/* paging 처리할때 사용
		final PageRequest pageRequest = new PageRequest(
				  0, 20, new Sort(
				    new Order(Direction.ASC, "ct_id"), 
				    new Order(Direction.DESC, "other")
				  ) 
				);
		Page<AttachTbl> page = attachDao.findAll(AttachSpecifications.formatLike(ctId), pageRequest);
		 */

		// Order 안에 ','로 구분하여 여러개 지정가능
		Sort sort = new Sort(new Order(Direction.DESC, "seq"));
		List<AttachTbl> list = attachDao.findAll(AttachSpecifications.formatLike(ctId), sort);
		List<Attatch> result = new ArrayList<Attatch>();
		
		for(AttachTbl info : list){
			Attatch attatch = new Attatch();
			
			attatch.setSeq(info.getSeq());
			attatch.setCtId(info.getCtId());
			attatch.setAttachType(info.getAttachType());
			attatch.setOrgFilenm(info.getOrgFilenm());
			attatch.setTransFilenm(info.getTransFilenm());
			attatch.setFlPath(info.getFlPath());
			attatch.setRegDt(info.getRegDt());
			
			result.add(attatch);
		}
		return result;
	}

	
	
	/**
	 * 첨부파일 정보 단일건을 조회한다.
	 * @param seq
	 * @return AttachTbl
	 */
	public AttachTbl getAttach(Long seq) throws ServiceException {
		return attachDao.findOne(seq);
	}
}

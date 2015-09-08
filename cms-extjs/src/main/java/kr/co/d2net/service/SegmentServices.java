package kr.co.d2net.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.SegmentDao;
import kr.co.d2net.dao.filter.SegmentLikeSpecifications;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 세그먼트관려 정보를 조회하기위한 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class SegmentServices {


	@Autowired
	private SegmentDao segmentDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 세그먼트 정보를 저장한다.
	 * @param segment
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(SegmentTbl segment) throws ServiceException{
		segmentDao.save(segment);
	}

	/**
	 * 세그먼트 정보를 삭제한다.
	 * @param id
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void delete(SegmentId id) throws ServiceException{
		segmentDao.delete(id);
	}

	/**
	 * 세그먼트 정보를 조회한다. 
	 * category_id,episode_id는 필수이며, segment_nm은 공백이여도 조회할수있다.
	 * @param search
	 * @return
	 */
	public List<SegmentTbl> segmentSearchList(Search search) throws ServiceException{
		List<SegmentTbl> segmentTbls=segmentDao.findAll(SegmentLikeSpecifications.SegmentNmLike(search.getCategoryId(),search.getEpisodeId(), search.getEpisodeNm()));

		for(SegmentTbl segmentTbl : segmentTbls){

			segmentTbl.setContentsTbls(null);

		}
		return segmentTbls;
	}

}

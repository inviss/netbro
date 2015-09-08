package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.DisuseDao;
import kr.co.d2net.dao.filter.DiscardSpecifications;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Discard;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

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


	/**
	 * 폐기 신청 정보를 저장한다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String insertDiscardInfo(long ctId) throws ServiceException {

		DisuseInfoTbl  disuseInfoTbl = new DisuseInfoTbl();
		disuseInfoTbl.setDisuseClf("001");
		disuseInfoTbl.setDisuseRsl(""); 
		disuseInfoTbl.setRegDt(new Date());
		disuseInfoTbl.setRegrId("");
		disuseInfoTbl.setCtId(ctId);

		disuseDao.save(disuseInfoTbl);

		return "Y";

	}


	/**
	 * 폐기된 영상의 리스트 정보를 조회한다
	 * @param statisticsTbl
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public List<Discard> findDiscardInfoList(Discard discard) throws ServiceException{

		String[] discardFields = {"disuseNo","disuseClf","regDt","disuseDd","ctId"};
		String[] ctFields = {"ctNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<DisuseInfoTbl> from = criteriaQuery.from(DisuseInfoTbl.class);
		Root<ContentsTbl> con = criteriaQuery.from(ContentsTbl.class); 
		criteriaQuery.where(DiscardSpecifications.discardSearch(criteriaBuilder,criteriaQuery,from,con,discard));

		Selection[] s = new Selection[discardFields.length + ctFields.length];

		int i=0;

		for(int j = 0; j < discardFields.length; j++) {
			s[i] = from.get(discardFields[j]);
			i++;
		}

		for(int j = 0; j < ctFields.length; j++) {	
			s[i] = con.get(ctFields[j]);				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("regDt")));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 한페이지에 21개씩
		 */
		int startPage = (discard.getPageNo()-1) * SearchControls.DISCARD_LIST_COUNT;

		if(logger.isDebugEnabled()){
			logger.debug("startPage :"+startPage);
		}

		typedQuery.setMaxResults(SearchControls.DISCARD_LIST_COUNT);
		typedQuery.setFirstResult(startPage);

		List<Object[]> list2 = typedQuery.getResultList();

		if(logger.isDebugEnabled()){
			logger.debug("list2 :"+list2.size());
		}

		List<Discard> discards = new ArrayList<Discard>();

		for(Object[] list : list2) {
			Discard info = new Discard();

			i=0;

			for(int j = 0; j < discardFields.length; j++) {
				ObjectUtils.setProperty(info, discardFields[j], list[i]);
				i++;
			}

			for(int j = 0; j < ctFields.length; j++) {
				ObjectUtils.setProperty(info, ctFields[j], list[i]);
				i++;
			}
			discards.add(info);
		}

		return discards;
	}	


	/**
	 * 폐기된 영상전체 조회 갯수를 구한다
	 * @param statisticsTbl
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public int countDiscard(Discard discard) throws ServiceException{

		String[] discardFields = {"disuseNo","disuseClf","regDt","disuseDd","ctId"};
		String[] ctFields = {"ctNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<DisuseInfoTbl> from = criteriaQuery.from(DisuseInfoTbl.class);
		Root<ContentsTbl> con = criteriaQuery.from(ContentsTbl.class); 
		criteriaQuery.where(DiscardSpecifications.discardSearch(criteriaBuilder,criteriaQuery,from,con,discard));

		Selection[] s = new Selection[discardFields.length + ctFields.length];

		int i=0;

		for(int j = 0; j < discardFields.length; j++) {
			s[i] = from.get(discardFields[j]);
			i++;
		}

		for(int j = 0; j < ctFields.length; j++) {	
			s[i] = con.get(ctFields[j]);				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("regDt")));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);


		List<Object[]> list2 = typedQuery.getResultList();

		if(logger.isDebugEnabled()){
			logger.debug("list2 :"+list2.size());
		}


		return list2.size();
	}


	/**
	 * 파일 삭제후 폐기 완료로 상태값을 바꾼다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String completeDiscard(long ctId) throws ServiceException {

		DisuseInfoTbl info = disuseDao.findOne(DiscardSpecifications.findInfoByCtId(ctId));
		DisuseInfoTbl  disuseInfoTbl = new DisuseInfoTbl();
		info.setDisuseClf("002");
		info.setDisuseDd(new Date());

		disuseDao.save(info);

		return "Y";
	}


	/**
	 * 폐기취소의 값을 업데이트한다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String cancleDiscard(long disuseNo) throws ServiceException {

		DisuseInfoTbl info = disuseDao.findOne(disuseNo);
		DisuseInfoTbl  disuseInfoTbl = new DisuseInfoTbl();

		info.setDisuseClf("003");
		info.setDisuseDd(null);
		info.setCancelDt(new Date());

		disuseDao.save(info);

		return "Y";

	}


	/**
	 * 폐기된 영상전체 조회 갯수를 구한다
	 * @param statisticsTbl
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public DisuseInfoTbl getDisuseNoInfo(long disuseNo) throws ServiceException{

		DisuseInfoTbl info = disuseDao.findOne(disuseNo);

		return info;
	}
}

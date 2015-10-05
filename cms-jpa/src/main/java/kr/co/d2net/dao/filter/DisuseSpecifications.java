package kr.co.d2net.dao.filter;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.DisuseInfoTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisuseSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(DisuseSpecifications.class);

	/**
	 * contentsInstTbl의 where where 조건절을 구현한다. 검색하고자 하는 
	 * 필드의 값이 존재하는 경우에만 where 조건을 추가한다.
	 * @param contentsInst
	 * @return
	 */
	@SuppressWarnings("serial")
	public static Specification<DisuseInfoTbl> findDisuseList(final Search search) {
		return new AbstractSpecification<DisuseInfoTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<DisuseInfoTbl> root) {
			   
				Join<DisuseInfoTbl, ContentsTbl> ctJoin = root.join(DisuseInfoTbl_.contentsTbl, JoinType.INNER);
				
				Predicate defultJoin = cb.conjunction();
				
				//폐기 컨텐츠 명 검색
				Predicate p1 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtNm())){
					if(logger.isDebugEnabled()) {
						logger.debug("Disuse[ct_nm]: "+search.getCtNm());
					}
					p1 = contentNmLike(cb, ctJoin, search.getCtNm());
				}
				
				//폐기 상세 코드 검색
				Predicate p2 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getDisuseClf())){
					if(logger.isDebugEnabled()) {
						logger.debug("Disuse[disuse_clf]: "+search.getDisuseClf());
					}
					p2 = disuseClfEqual(cb, root, search.getDisuseClf());
				}
				
				//폐기 등록일검색
				Predicate p3 = cb.conjunction(); 
				if(search.getStartDt() != null && search.getEndDt() != null){
					if(logger.isDebugEnabled()) {
						logger.debug("Disuse[startDt]: "+search.getStartDt());
						logger.debug("Disuse[endDt]: "+search.getEndDt());
					}
					 
					Calendar start = Calendar.getInstance();
					Calendar end = Calendar.getInstance();
					
					start.setTime(search.getStartDt());
					start.set(Calendar.HOUR_OF_DAY, 00);
					start.set(Calendar.MINUTE, 00);
					start.set(Calendar.SECOND, 01);
					search.setStartDt(start.getTime());
					
					end.setTime(search.getEndDt());
					end.set(Calendar.HOUR_OF_DAY, 23);
					end.set(Calendar.MINUTE, 59);
					end.set(Calendar.SECOND, 59);
					search.setEndDt(end.getTime());
					
					p3 = cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root,search.getEndDt()));
				}
			 
				if(search.getOperator() == Operator.LIST){
				 	Path<String> disuseClf = root.get(DisuseInfoTbl_.disuseClf);
				 	Path<Date> regDt = root.get(DisuseInfoTbl_.regDt);
					Path<Date> disuseDd = root.get(DisuseInfoTbl_.disuseDd);
					Path<Long> ctId = root.get(DisuseInfoTbl_.ctId);
				    Path<String> ctNm = ctJoin.get(ContentsTbl_.ctNm);
					
					cq.multiselect(ctId, disuseClf, regDt, disuseDd,ctNm);
					
					cq.orderBy(cb.desc(root.get(DisuseInfoTbl_.regDt)));
				}
		 
				return cb.and(defultJoin,p1,p2,p3);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<DisuseInfoTbl> betweenRegDate(final Date startDt, final Date endDt) {
		return new AbstractSpecification<DisuseInfoTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<DisuseInfoTbl> root) {
				return cb.and(fromRegDt(cb, root, startDt), toRegDt(cb, root, endDt));
			}
		};
	}
	
	public static Predicate contentNmLike(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String ctNm) {
		return cb.like(path.get(ContentsTbl_.ctNm), "%"+ctNm+"%");
	}
	
	public static Predicate disuseClfEqual(final CriteriaBuilder cb, final Path<DisuseInfoTbl> path, final String disuseClf) {
		return cb.equal(path.get(DisuseInfoTbl_.disuseClf), disuseClf);
	}
	
	public static Predicate fromRegDt(final CriteriaBuilder cb, final Path<DisuseInfoTbl> path, final Date startDt) {
		return cb.greaterThanOrEqualTo(path.get(DisuseInfoTbl_.regDt), startDt);
	}

	public static Predicate toRegDt(final CriteriaBuilder cb, final Path<DisuseInfoTbl> path, final Date endDt) {
		return cb.lessThanOrEqualTo(path.get(DisuseInfoTbl_.regDt), endDt);
	}
	
}

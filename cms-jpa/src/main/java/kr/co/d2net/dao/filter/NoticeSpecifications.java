package kr.co.d2net.dao.filter;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.NoticeTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoticeSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(NoticeSpecifications.class);

	/**
	 * contentsInstTbl의 where where 조건절을 구현한다. 검색하고자 하는 
	 * 필드의 값이 존재하는 경우에만 where 조건을 추가한다.
	 * @param contentsInst
	 * @return
	 */
	@SuppressWarnings("serial")
	public static Specification<NoticeTbl> findNotice(final Search search) {
		return new AbstractSpecification<NoticeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<NoticeTbl> root) {
			    
				//제목검색
				Predicate p1 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())){
					if(logger.isDebugEnabled()) {
						logger.debug("Notice[Keyword]: "+search.getKeyword());
					}
					p1 = keyWordLike(cb, root, search.getCtNm());
				}
			 
				//폐기 등록일검색
				Predicate p2 = cb.conjunction(); 
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
					
					p2 = cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root,search.getEndDt()));
				}
			 
				if(search.getOperator() == Operator.LIST){
				 	Path<Long> noticeId = root.get(NoticeTbl_.noticeId);
					Path<String> cont = root.get(NoticeTbl_.cont);
					Path<String> popUpYn = root.get(NoticeTbl_.popUpYn);
					Path<String> regId = root.get(NoticeTbl_.regId);
					Path<String> title = root.get(NoticeTbl_.title);
				 	Path<Date> regDt = root.get(NoticeTbl_.regDt);
					Path<Date> end = root.get(NoticeTbl_.endDd);
				 
					
					cq.multiselect(noticeId, cont, popUpYn, regId,title,regDt,end);
					
					cq.orderBy(cb.desc(root.get(NoticeTbl_.regDt)));
				}
		 
				return cb.and(p1,p2);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<NoticeTbl> betweenRegDate(final Date startDt, final Date endDt) {
		return new AbstractSpecification<NoticeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<NoticeTbl> root) {
				return cb.and(fromRegDt(cb, root, startDt), toRegDt(cb, root, endDt));
			}
		};
	}
	
	public static Predicate keyWordLike(final CriteriaBuilder cb, final Path<NoticeTbl> path, final String keyword) {
		return cb.like(path.get(NoticeTbl_.title), "%"+keyword+"%");
	}
	 
	public static Predicate fromRegDt(final CriteriaBuilder cb, final Path<NoticeTbl> path, final Date startDt) {
		return cb.greaterThanOrEqualTo(path.get(NoticeTbl_.regDt), startDt);
	}

	public static Predicate toRegDt(final CriteriaBuilder cb, final Path<NoticeTbl> path, final Date endDt) {
		return cb.lessThanOrEqualTo(path.get(NoticeTbl_.regDt), endDt);
	}
	
}

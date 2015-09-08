package kr.co.d2net.dao.filter;

import java.util.Calendar;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.DisuseInfoTbl_;
import kr.co.d2net.dto.vo.Discard;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public class DiscardSpecifications {

	final static Logger logger = LoggerFactory.getLogger(DiscardSpecifications.class);

	/**
	 * 폐기조회
	 * @param search
	 * @return
	 */
	public static Predicate discardSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<DisuseInfoTbl> from,Root<ContentsTbl> con,Discard discard) {

		Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.equal(from.get(DisuseInfoTbl_.ctId), con.get(ContentsTbl_.ctId))); 
		//폐기 컨텐츠 명 검색
		if(StringUtils.isNotBlank(discard.getCtNm())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(con.get(ContentsTbl_.ctNm), "%"+discard.getCtNm()+"%"));
		}
		//폐기 상세 코드 검색
		if(StringUtils.isNotBlank(discard.getDisuseClf())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(DisuseInfoTbl_.disuseClf), discard.getDisuseClf()));
		}
		//폐기 시작일
		if(discard.getStartDt() != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(discard.getStartDt());
			cal.set(Calendar.HOUR_OF_DAY, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND, 01);
			discard.setStartDt(cal.getTime());
			logger.debug("discard.getStartDt() : " + discard.getStartDt());
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(DisuseInfoTbl_.regDt), discard.getStartDt()));
		}
		//폐기  종료일
		if(discard.getEndDt() != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(discard.getEndDt());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			discard.setEndDt(cal.getTime());
			if(logger.isInfoEnabled())
				logger.info("discard.setEndDt() : " + discard.getEndDt());
			
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.lessThanOrEqualTo(from.get(DisuseInfoTbl_.regDt), discard.getEndDt()));
		}
		return totalWhere; 
	}


	/**
	 * 카테고리 id 검색
	 * @param search
	 * @return
	 */
	public static Specification<DisuseInfoTbl> findInfoByCtId(final Long ctId) {
		return new Specification<DisuseInfoTbl>() {
			@Override
			public Predicate toPredicate(Root<DisuseInfoTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get(DisuseInfoTbl_.ctId),ctId));							
			}
		};
	}
}

package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.StatisticsTbl_;
import kr.co.d2net.dto.vo.Statistic;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsSpecifications {

	final static Logger logger = LoggerFactory.getLogger(StatisticsSpecifications.class);

	/**
	 * 기간별 조회를 한다.
	 * @param search
	 * @return
	 */
	public static Predicate StatisticsDetailSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user,Join<ContentsTbl, ContentsInstTbl> contentsInstTbl,Statistic info) {
		if( StringUtils.isNotBlank(info.getGubun())){
			if(info.getGubun().equals("regist")){//등록건
				return criteriaBuilder.and(CategoryIdEqual( criteriaBuilder, criteriaQuery, from,user, info)
						,ContentsCtiFmtLike( criteriaBuilder, criteriaQuery, from,contentsInstTbl, info)
						,discardStat( criteriaBuilder, criteriaQuery, from, user, info)
						,dataStatCdEqual( criteriaBuilder, criteriaQuery, from, user, info)
						,dateBetween( criteriaBuilder, criteriaQuery, from, user, info)
						);
			}else if(info.getGubun().equals("beforeArrange")){//정리전
				return criteriaBuilder.and(CategoryIdEqual( criteriaBuilder, criteriaQuery, from,user, info)
						,ContentsCtiFmtLike( criteriaBuilder, criteriaQuery, from,contentsInstTbl, info)
						,discardStat( criteriaBuilder, criteriaQuery, from, user, info)
						,dataStatCdEqual( criteriaBuilder, criteriaQuery, from, user, info)
						,dateBetween( criteriaBuilder, criteriaQuery, from, user, info)
						);

			}else if(info.getGubun().equals("completeArrange")){//정리완료
				return criteriaBuilder.and(CategoryIdEqual( criteriaBuilder, criteriaQuery, from,user, info)
						,ContentsCtiFmtLike( criteriaBuilder, criteriaQuery, from,contentsInstTbl, info)
						,discardStat( criteriaBuilder, criteriaQuery, from, user, info)
						,dataStatCdEqual( criteriaBuilder, criteriaQuery, from, user, info)
						,dateBetween( criteriaBuilder, criteriaQuery, from, user, info)
						);
			}else if(info.getGubun().equals("discard")){//폐기
				return criteriaBuilder.and(CategoryIdEqual( criteriaBuilder, criteriaQuery, from,user, info)
						,ContentsCtiFmtLike( criteriaBuilder, criteriaQuery, from,contentsInstTbl, info)
						,disCardYn( criteriaBuilder, criteriaQuery, from, user, info)
						);
			}else if(info.getGubun().equals("error")){//오류
				return criteriaBuilder.and(CategoryIdEqual( criteriaBuilder, criteriaQuery, from,user, info)
						,ContentsCtiFmtLike( criteriaBuilder, criteriaQuery, from,contentsInstTbl, info)
						,discardStat( criteriaBuilder, criteriaQuery, from, user, info)
						,dataStatCdEqual( criteriaBuilder, criteriaQuery, from, user, info)
						,dateBetween( criteriaBuilder, criteriaQuery, from, user, info)
						);
			}
		}
		return null; 
	}



	/**
	 * 카테고리 id 검색
	 * @param search
	 * @return
	 */
	public static Predicate CategoryIdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user,Statistic info) {
		return criteriaBuilder.equal(from.get(ContentsTbl_.categoryId), info.getCategoryId());
	}


	/**
	 * 데이터 정리상태 검색(정리전,정리완료)
	 * @param search
	 * @return
	 */
	public static Predicate dataStatCdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user,Statistic info) {
		if(info.getGubun().equals("regist")){//등록건
			return criteriaBuilder.notEqual(from.get(ContentsTbl_.dataStatCd), "003");
		}else if(info.getGubun().equals("beforeArrange")){//정리전건
			return criteriaBuilder.or(criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "000"),criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "001"));
		}else if(info.getGubun().equals("completeArrange")){//정리완료건
			return criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "002");
		}else if(info.getGubun().equals("error")){//오류건
			return criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "003");
		}
		return null;
	}

	/**
	 * 폐기상태 검색
	 * @param search
	 * @return
	 */
	public static Predicate discardStat(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user,Statistic info) {
		if(info.getGubun().equals("regist") || info.getGubun().equals("beforeArrange") || info.getGubun().equals("completeArrange") || info.getGubun().equals("error")){//등록건,정리전건,정리완료건,오류건
			return criteriaBuilder.isNull(from.get(ContentsTbl_.delDd));
		}else if(info.getGubun().equals("discard")){//폐기건
			return criteriaBuilder.isNotNull(from.get(ContentsTbl_.delDd));
		}
		return null;
	}


	/**
	 * 폐기건 조회
	 * @param search
	 * @return
	 */
	public static Predicate disCardYn(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user ,Statistic  info) {
		if(logger.isInfoEnabled()){
			logger.info("statisticsTbl.getStartDD() : " + info.getStartDD());
			logger.info("statisticsTbl.getEndDD(): " + info.getEndDD());
		}
		return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.delDd), info.getStartDD()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.delDd), info.getEndDD()));
	}

	/**
	 * 등록일을 기준으로 기간검색
	 * @param search
	 * @return
	 */
	public static Predicate dateBetween(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, SegmentTbl> user ,Statistic info) {
		if(info.getStartDD() != null && info.getEndDD() != null) {
			if(logger.isInfoEnabled()){
				logger.info("statisticsTbl.getStartDD() : " + info.getStartDD());
				logger.info("statisticsTbl.getEndDD(): " + info.getEndDD());
			}
			if(info.getGubun().equals("regist")){//등록건
				return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), info.getStartDD()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), info.getEndDD()));
			}else if(info.getGubun().equals("beforeArrange")){//정리전건
				return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), info.getStartDD()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), info.getEndDD()));	
			}else if(info.getGubun().equals("completeArrange")){//정리완료건
				return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), info.getStartDD()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), info.getEndDD()));	
			}else if(info.getGubun().equals("error")){//오류건
				return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), info.getStartDD()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), info.getEndDD()));	
			}
		}
		return null; 
	}


	/**
	 * 고해상도 영상검색
	 * @param search
	 * @return
	 */
	public static Predicate ContentsCtiFmtLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> contentsInst,Statistic info) {
		return criteriaBuilder.like(contentsInst.get(ContentsInstTbl_.ctiFmt),"1%");
	}



	/**
	 * 연간 통계용 그래프 데이터를 조회한다
	 * @param search
	 * @return
	 */
	public static Predicate YearEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<StatisticsTbl> from,Statistic info) {
		return criteriaBuilder.equal(criteriaBuilder.substring(from.get(StatisticsTbl_.regDd),1,4), info.getYearList());
	}

}

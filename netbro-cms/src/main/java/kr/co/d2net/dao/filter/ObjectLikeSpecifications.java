package kr.co.d2net.dao.filter;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectLikeSpecifications {

	final static Logger logger = LoggerFactory.getLogger(ObjectLikeSpecifications.class);

	/**
	 * 컨텐츠 검색집합. 컨텐츠 조회에 필요한 where 조건이 모두 존재하며 각 beans의 값유무에 따라 조회조건이 추가 삭제된다.
	 * @param search
	 * @return
	 */
	public static Predicate contentsFilterSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category,Search search) {

				Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.like(inst.get(ContentsInstTbl_.ctiFmt), search.getCtiFmt()+"%"),criteriaBuilder.equal(from.get("useYn"), "Y"));
		
				if(StringUtils.isNotBlank(search.getDataStatCd())){
					totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), search.getDataStatCd()));
				}
				
				if(StringUtils.isNotBlank(search.getCtTyp())){
					totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.ctTyp), search.getCtTyp()));
				}
				
				if(search.getStartDt() != null){
					totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), search.getStartDt()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), search.getEndDt()));
				}
				
				if(StringUtils.isNotBlank(search.getKeyword())){
					totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(from.get(ContentsTbl_.ctNm), search.getKeyword()+"%"));
				}
				
				if(search.getCategoryId() != 0){
					totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.or(criteriaBuilder.equal(from.<Integer>get("categoryId"), search.getCategoryId()),criteriaBuilder.like(category.<String>get("nodes"), search.getNodes()+".%")));
				}
			
				return totalWhere; 
		
	}

	/**
	 * 사용자 관리 UI JOIN관련.
	 * @param cb
	 * @param cq
	 * @param userAuth
	 * @param userAuth1
	 * @param search
	 * @return
	 */
	public static Predicate userFilterSearch(CriteriaBuilder cb,CriteriaQuery<Object[]> cq, Join<UserTbl, UserAuthTbl> userAuth,Join<AuthTbl, UserAuthTbl> userAuth1,Search search) {

		return cb.and(cb.equal(userAuth, userAuth1));

	}

	/**
	 * 영상의 포맷코드를 like로 검색
	 * @param search
	 * @return
	 */
	public static Predicate formatLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String prefix) {

		if(StringUtils.isNotBlank(prefix)) {

			String likePattern = prefix+"%";

			return criteriaBuilder.like(inst.get(ContentsInstTbl_.ctiFmt), likePattern);

		} else return null;

	}


	/**
	 * 검색 키워드로 like 검색
	 * @param search
	 * @return
	 */
	public static Predicate keyWordLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String prefix) {

		if(StringUtils.isNotBlank(prefix)) {

			String likePattern = prefix+"%";

			return criteriaBuilder.like(from.get(ContentsTbl_.ctNm), likePattern);

		}else return null; 

	}


	/**
	 * 등록일을 기준으로 기간검색
	 * @param search
	 * @return
	 */
	public static Predicate dateBetween(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,Date startDt, Date endDt) {

		if(startDt != null && endDt != null) {

			return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), startDt),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), endDt));

		}else return null; 

	}

	
	/**
	 * 데이터 상태 equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate dataStatCdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String dataStatCd) {

		if(StringUtils.isNotBlank(dataStatCd)) {

			return criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), dataStatCd);

		}else return null; 

	}
	
	/**
	 * 영상유형  equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate ctTypEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String ctTyp) {

		if(StringUtils.isNotBlank(ctTyp)) {

			return criteriaBuilder.equal(from.get(ContentsTbl_.ctTyp), ctTyp);

		}else return null; 

	}
	
	/**
	 * 카테고리 nodes like 검색
	 * @param search
	 * @return
	 */
	public static Predicate nodesLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst
			,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category
			,String nodes) {

		if(StringUtils.isNotBlank(nodes)) {

			return criteriaBuilder.like(category.<String>get("nodes"), nodes+"%");

		}else return null; 

	}
	
	/**
	 * 카테고리 id equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate categoryIdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst
			,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category
			,Integer categoryId) {

		if(categoryId != 0) {

			return criteriaBuilder.equal(from.<Integer>get("categoryId"), categoryId);

		}else return null; 

	}

}

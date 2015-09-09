package kr.co.d2net.dao.filter;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class ContentsLikeSpecifications {

	final static Logger logger = LoggerFactory.getLogger(ContentsLikeSpecifications.class);

	/**
	 * 
	 * @param search
	 * @return
	 */
	public static Specification<ContentsTbl> contentsFilterSearch(Search search) {

		Specifications<ContentsTbl> specifications = where(formatLike(search));

		return specifications.and(keywordLike(search.getKeyword())).and(dateBetween(search.getStartDt(),search.getEndDt())).and(FindDataStatCd(search));

	}

	/**
	 * 영상의 포맷코드를 like로 검색
	 * @param search
	 * @return
	 */
	public static Specification<ContentsTbl> formatLike(final Search search) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Join<ContentsTbl, ContentsInstTbl> inst = root.join("contentsInst", JoinType.INNER);
				Join<ContentsTbl, SegmentTbl> segment = root.join("segmentTbl", JoinType.INNER);
				Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
				Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);

				if(StringUtils.isNotBlank(search.getCtiFmt())) {

					String likePattern = getLikePattern(search.getCtiFmt());   
					if(search.getCategoryId() != 0){
						String nodesLikePattern = getLikePattern(search.getNodes());   
						return cb.and(cb.like(inst.get(ContentsInstTbl_.ctiFmt), likePattern),cb.or(
								cb.equal(root.<Integer>get("categoryId"), search.getCategoryId())
								,cb.like(category.<String>get("nodes"), nodesLikePattern))
								);
					}else{
						return cb.like(inst.get(ContentsInstTbl_.ctiFmt), likePattern);
					}

				} else return null;

			}

			private String getLikePattern(final String contentsFormat) {

				StringBuilder pattern = new StringBuilder();
				pattern.append(contentsFormat);
				pattern.append(".%");

				return pattern.toString();

			}

		};

	}

	/**
	 * 검색 키워드로 like 검색
	 * @param search
	 * @return
	 */
	public static Specification<ContentsTbl> keywordLike(final String keywords) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(StringUtils.isNotBlank(keywords)) {

					String likePattern = getLikePattern(keywords);  
					logger.debug("######################likePattern   "+likePattern);
					return cb.like(root.get(ContentsTbl_.ctNm), likePattern);

				} else return null;

			}

			private String getLikePattern(final String keywords) {

				StringBuilder pattern = new StringBuilder();
				pattern.append("%");
				pattern.append(keywords);
				pattern.append("%");

				return pattern.toString();

			}

		};

	}

	/**
	 * 등록일을 기준으로 기간검색
	 * @param search
	 * @return
	 */
	public static Specification<ContentsTbl> dateBetween(final Date startDt, final Date endDt) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(startDt != null && endDt != null) {

					return cb.and(cb.greaterThanOrEqualTo(root.<Date>get("regDt"), startDt), 
							cb.and(cb.lessThanOrEqualTo(root.<Date>get("regDt"), endDt)));

				} else return null;

			}

		};

	}
	
	
	/**
	 * 카테고리id 기준 검색
	 * @param search
	 * @return
	 */
	public static Specification<ContentsTbl> equalCategory(final Integer CategryId) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(CategryId == 0) {

					return cb.equal(root.<Integer>get("categoryId"), CategryId);

				} else return null;

			}

		};

	}
	
	/**
	 * 에피소드id  기준 검색
	 * @param episodeId
	 * @return
	 */
	public static Specification<ContentsTbl> equalEpisode(final EpisodeId id) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(id != null) {

					return cb.and(cb.equal(root.<Integer>get("categoryId"), id.getCategoryId()),cb.equal(root.<Integer>get("episodeId"), id.getEpisodeId()));

				} else return null;

			}

		};

	}
	
	
	public static Specification<ContentsInstTbl> ContentsNmLike(Search search) {

		return new Specification<ContentsInstTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsInstTbl> ContentsRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				String likePattern = getLikePattern("1");     

				return cb.like(cb.lower(ContentsRoot.<String>get("cti_fmt")), likePattern);

			}

			private String getLikePattern(final String contentsFormat) {

				StringBuilder pattern = new StringBuilder();
				pattern.append(contentsFormat);
				pattern.append("%");

				return pattern.toString();

			}

		};

	}

	public static Specification<ContentsTbl> deleteRangeSearch(final Search search) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> ContentsRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.and(cb.equal(ContentsRoot.<Date>get("useYn"), "Y"), 
						cb.lessThanOrEqualTo(ContentsRoot.<Date>get("delDd"), search.getToDate()));

			}

		};

	}

	public static Specification<ContentsInstTbl> findContentIntByCtId(final Long ctId) {

		return new Specification<ContentsInstTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsInstTbl> ContentsRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.equal(ContentsRoot.<Long>get("ctId"), ctId);
			}

		};

	}

	
	
	public static Specification<ContentsTbl> FindDataStatCd(final Search search) {

		return new Specification<ContentsTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsTbl> ContentsRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				if(StringUtils.isNotBlank(search.getDataStatCd())){
					logger.debug("##############search.getDataStatCd()   "+search.getDataStatCd());
					return cb.equal(ContentsRoot.<String>get("dataStatCd"), search.getDataStatCd());
				
				} else return null;
			
			}
		
		};

	}
	
	
}

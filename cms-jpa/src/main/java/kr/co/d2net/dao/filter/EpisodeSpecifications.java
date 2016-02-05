package kr.co.d2net.dao.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.DisuseInfoTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 코드관련 조회함수의 where 조건을 생성하는 함수.
 * @author asura
 *
 */
public class EpisodeSpecifications {

private final static Logger logger = LoggerFactory.getLogger(EpisodeSpecifications.class);
	
	@SuppressWarnings("serial")
	public static Specification<EpisodeTbl> findEpisodeOnlyByParams(final Search search) {
		return new AbstractSpecification<EpisodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<EpisodeTbl> root) {

				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Predicate predicate = null;
				// category_id
				//Predicate p1 = cb.conjunction();
				if(search.getCategoryId() != null && search.getCategoryId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Episode[category_id]: "+search.getCategoryId());
					}
					predicate = cb.and(categoryEquals(cb, root, search.getCategoryId()));
					predicates.add(predicate);
				}

				// episode_nm like
				//Predicate p2 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Episode[keyword]: "+search.getKeyword());
					}
					predicate = cb.and(episodeNmLikes(cb, root, search.getKeyword()));
					predicates.add(predicate);
				}

				 
				if(search.getOperator() == Operator.LIST) {
					cq.orderBy(cb.asc(root.get(EpisodeTbl_.episodeId)));
				}

				return cb.and((Predicate[])predicates.toArray());
			}
		};
	}
	
	@SuppressWarnings("serial")
	public static Specification<EpisodeTbl> getMaxEpisdoe(final Integer categoryId) {
		return new AbstractSpecification<EpisodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<EpisodeTbl> root) {
				cq.orderBy(cb.desc(root.get(EpisodeTbl_.episodeId)));
				return cb.and(categoryEquals(cb,root,categoryId));
			}
		};
	}
	@SuppressWarnings("serial")
	public static Specification<EpisodeTbl> getEpisodeWithPK(final Integer categoryId, final Integer episodeId) {
		return new AbstractSpecification<EpisodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<EpisodeTbl> root) {
				return cb.and(categoryEquals(cb, root, categoryId), episodeEquals(cb, root, categoryId));
			}
		};
	}
	
	public static Predicate categoryEquals(final CriteriaBuilder cb, final Path<EpisodeTbl> path, final Integer categoryId) {
		return cb.equal(path.get(EpisodeTbl_.categoryId), categoryId);
	}
	public static Predicate episodeEquals(final CriteriaBuilder cb, final Path<EpisodeTbl> path, final Integer episodeId) {
		return cb.equal(path.get(EpisodeTbl_.episodeId), episodeId);
	}
	
	public static Predicate episodeNmLikes(final CriteriaBuilder cb, final Path<EpisodeTbl> path, final String keyword) {
		return cb.like(path.get(EpisodeTbl_.episodeNm), duplexPattern(keyword));
	}
	
	public static Predicate useYn(final CriteriaBuilder cb, final Path<EpisodeTbl> path, final String useYn) {
		return cb.equal(path.get(EpisodeTbl_.useYn), useYn);
	}

	private static String duplexPattern(final String keyword) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(keyword);
		pattern.append("%");

		return pattern.toString();
	}
	
}

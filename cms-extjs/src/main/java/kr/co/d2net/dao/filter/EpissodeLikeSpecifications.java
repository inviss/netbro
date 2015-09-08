package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.EpisodeTbl;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * hibernate에서  where 조건문을 생성할때 사용하는 함수.
 * @author asura
 *
 */
public class EpissodeLikeSpecifications {

	/**
	 * like 겁색을 하기위해서 where 조건 쿼리를 생성한다.
	 * @param categoryId
	 * @param episodeNm
	 * @return
	 */
	public static Specification<EpisodeTbl> episodeNmLike(final Integer categoryId, final String episodeNm) {
		return new Specification<EpisodeTbl>() {
			@Override
			public Predicate toPredicate(Root<EpisodeTbl> episodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(StringUtils.isNotBlank(episodeNm)) {
					String likePattern = getLikePattern(episodeNm);
					return cb.and(cb.equal(episodeRoot.<String>get("id").get("categoryId"), categoryId), (cb.like(episodeRoot.<String>get("episodeNm"), likePattern)));						
				} else {
					return cb.and(cb.equal(episodeRoot.<String>get("id").get("categoryId"), categoryId));
				}
			}

			private String getLikePattern(final String episodeNm) {
				StringBuilder pattern = new StringBuilder();
				pattern.append("%");
				pattern.append(episodeNm);
				pattern.append("%");
				return pattern.toString();
			}
		};
	}

	/**
	 * 에피소드 정보를 조회하는 조건(cateogry_id, epsode_id) 반드시 필요.
	 * @param categoryId
	 * @param episodeId
	 * @return
	 */
	public static Specification<EpisodeTbl> episodeInfo(final Integer categoryId,final Integer episodeId){
		return new Specification<EpisodeTbl>() {
			@Override
			public Predicate toPredicate(Root<EpisodeTbl> episodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(episodeRoot.<String>get("id").get("categoryId"), categoryId), cb.equal(episodeRoot.<String>get("id").get("episodeId"), episodeId));
			};
		};
	}


	/**
	 * 카테고리 id별로 등록된 모든 에피소드 정보 검색
	 * @param categoryId 
	 * @return
	 */
	public static Specification<EpisodeTbl> allEpisodeInfo(final Integer categoryId){
		return new Specification<EpisodeTbl>() {
			@Override
			public Predicate toPredicate(Root<EpisodeTbl> episodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(episodeRoot.<String>get("id").get("categoryId"), categoryId));
			};
		};
	}
}

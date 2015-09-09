package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;

import org.springframework.data.jpa.domain.Specification;

public class SegmentLikeSpecifications {
	public static Specification<SegmentTbl> SegmentNmLike(final Integer categoryId, final Integer episodeId, final String segmentNm) {

		return new Specification<SegmentTbl>() {
			@Override
			public Predicate toPredicate(Root<SegmentTbl> segmentRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String likePattern = getLikePattern(segmentNm);   
				if(segmentNm.equals("")||segmentNm == null){
					return cb.and(cb.equal(segmentRoot.<String>get("id").get("categoryId"), categoryId),cb.equal(segmentRoot.<String>get("id").get("episodeId"), episodeId));
				}else{
					return cb.and(cb.equal(segmentRoot.<String>get("id").get("categoryId"), categoryId),cb.equal(segmentRoot.<String>get("id").get("episodeId"), episodeId), (cb.like(cb.lower(segmentRoot.<String>get("episodeNm")), likePattern)));						
				}
			}


			private String getLikePattern(final String segmentNm) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(segmentNm);
				pattern.append("%");
				return pattern.toString();
			}
		};
	}

	public static Specification<EpisodeTbl> episodeInfo(final Integer categoryId,final Integer episodeId){

		return new Specification<EpisodeTbl>() {
			@Override
			public Predicate toPredicate(Root<EpisodeTbl> episodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.and(cb.equal(episodeRoot.<String>get("id").get("categoryId"), categoryId), cb.equal(episodeRoot.<String>get("id").get("episodeId"), episodeId));
			};
		};
	}
}

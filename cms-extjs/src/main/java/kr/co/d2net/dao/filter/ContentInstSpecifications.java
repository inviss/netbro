package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;

import org.springframework.data.jpa.domain.Specification;

/**
 * ContentsInst_Tbl 쿼리문핸들링 class.
 * @author Administrator
 *
 */
public class ContentInstSpecifications {
	
	public static Specification<ContentsInstTbl> formatLike(final Long ctId, final String formatCode) {

		return new Specification<ContentsInstTbl>() {
			
			@Override
			public Predicate toPredicate(Root<ContentsInstTbl> contentInstRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				String likePattern = getLikePattern(formatCode);                
				
				return cb.and(cb.equal(contentInstRoot.get(ContentsInstTbl_.ctId), ctId), (cb.like(contentInstRoot.get(ContentsInstTbl_.ctiFmt), likePattern)));
			
			}

			private String getLikePattern(final String formatCode) {
				
				StringBuilder pattern = new StringBuilder();
				pattern.append(formatCode.toLowerCase());
				pattern.append("%");
				return pattern.toString();
				
			}
			
		};
		
	}
	
	public static Specification<ContentsInstTbl> mediaInfo(final Long ctId, final String formatCode) {

		return new Specification<ContentsInstTbl>() {
			
			@Override
			public Predicate toPredicate(Root<ContentsInstTbl> contentInstRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				String likePattern = getLikePattern(formatCode);                
				
				return cb.and(cb.equal(contentInstRoot.get(ContentsInstTbl_.ctId), ctId), (cb.notLike(contentInstRoot.get(ContentsInstTbl_.ctiFmt), likePattern)));
			
			}

			private String getLikePattern(final String formatCode) {
				
				StringBuilder pattern = new StringBuilder();
				pattern.append(formatCode.toLowerCase());
				pattern.append("%");
				return pattern.toString();
				
			}
			
		};
		
	}
	
	
	public static Specification<ContentsInstTbl> formatNotLike(final Long ctId, final String formatCode) {

		return new Specification<ContentsInstTbl>() {
			
			@Override
			public Predicate toPredicate(Root<ContentsInstTbl> contentInstRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				String likePattern = getLikePattern(formatCode);                
				
				return cb.and(cb.equal(contentInstRoot.get(ContentsInstTbl_.ctId), ctId), (cb.notLike(contentInstRoot.get(ContentsInstTbl_.ctiFmt), likePattern)));
			
			}

			private String getLikePattern(final String formatCode) {
				
				StringBuilder pattern = new StringBuilder();
				pattern.append(formatCode.toLowerCase());
				pattern.append("%");
				return pattern.toString();
				
			}
			
		};
		
	}
}

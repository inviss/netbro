package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.AttachTbl;

import org.springframework.data.jpa.domain.Specification;
/**
 * 첨부파일의 쿼리문 핸들링 class.
 * @author vayne
 *
 */
public class AttachSpecifications {
	public static Specification<AttachTbl> formatLike(final Long ctId) {
		return new Specification<AttachTbl>() {
			@Override
			public Predicate toPredicate(Root<AttachTbl> attachRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(attachRoot.<String>get("ctId"), ctId);
			}
		};
	}
}

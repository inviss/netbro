package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.TrsTbl;

import org.springframework.data.jpa.domain.Specification;
/**
 * TRS의 쿼리문 핸들링 class.
 * @author vayne
 *
 */
public class TrsSpecifications {
	public static Specification<TrsTbl> formatLike(final Long seq) {
		return new Specification<TrsTbl>() {
			@Override
			public Predicate toPredicate(Root<TrsTbl> trsRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(trsRoot.<String>get("seq"), seq);
			}
		};
	}
}

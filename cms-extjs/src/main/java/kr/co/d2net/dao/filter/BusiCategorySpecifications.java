package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.BusiPartnerCategoryTbl;

import org.springframework.data.jpa.domain.Specification;
/**
 * 카테고리맵핑의 쿼리문 핸들링 class.
 * @author vayne
 *
 */
public class BusiCategorySpecifications {
	public static Specification<BusiPartnerCategoryTbl> formatLike(final BusiPartnerCategoryTbl ctId) {
		return new Specification<BusiPartnerCategoryTbl>() {
			@Override
			public Predicate toPredicate(Root<BusiPartnerCategoryTbl> attachRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(attachRoot.<String>get("busiPartnerId"), ctId.getBusiPartnerId());
			}
		};
	}
}

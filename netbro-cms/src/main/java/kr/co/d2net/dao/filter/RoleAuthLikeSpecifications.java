package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;

import org.springframework.data.jpa.domain.Specification;
/** 
 * ROLE_AUTH_TBL의 쿼리문 핸들링 class.
 * @author vayne
 *
 */
public class RoleAuthLikeSpecifications {

	public static Specification<RoleAuthTbl> validator(final RoleAuthId id) {
		return new Specification<RoleAuthTbl>() {
			@Override
			public Predicate toPredicate(Root<RoleAuthTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.equal(userRoot.<String>get("authId"), id));
			}
		};
	}
}

package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;

import org.springframework.data.jpa.domain.Specification;

/**
 * USER_TBL 쿼리문핸들링 class.
 * @author vayne
 *
 */
public class UserSpecifications {

	public static Specification<UserTbl> validator(final String userId, final String phoneNo) {
		return new Specification<UserTbl>() {
			@Override
			public Predicate toPredicate(Root<UserTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.equal(userRoot.<String>get("userId"), userId), cb.equal(userRoot.<String>get("userPhone"), phoneNo));
			}
		};
	}

	public static Specification<UserTbl> loginValidator(final String userId, final String userPasswd,final String useYn) {
		return new Specification<UserTbl>() {
			@Override
			public Predicate toPredicate(Root<UserTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(useYn.equals("Y")){
					return cb.and(cb.equal(userRoot.<String>get("userId"), userId), cb.equal(userRoot.<String>get("userPass"), userPasswd),cb.equal(userRoot.<String>get("useYn"), "Y"));
				}else{
					return cb.and(cb.equal(userRoot.<String>get("userId"), userId));
				}
			}
		};
	}

	public static Specification<UserTbl> loginValidator1(final String userId, final String userPasswd) {
		return new Specification<UserTbl>() {
			@Override
			public Predicate toPredicate(Root<UserTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.and(cb.equal(userRoot.<String>get("userId"), userId), cb.equal(userRoot.<String>get("userPass"), userPasswd));

			}
		};
	}

	public static Specification<UserTbl> findUserValidator(final String userId) {
		return new Specification<UserTbl>() {
			@Override
			public Predicate toPredicate(Root<UserTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.equal(userRoot.<String>get("userId"), userId));
			}
		};
	}

	public static Specification<UserTbl> formatLike(final String name) {

		return new Specification<UserTbl>() {
			@Override
			public Predicate toPredicate(Root<UserTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.like(userRoot.<String>get("userNm"), getLikePattern(name)));
			}

			private String getLikePattern(final String userNm) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(userNm);
				pattern.append("%");
				return pattern.toString();
			}
		};
	}

	public static Specification<UserAuthTbl> authLikeUserId(final String userId) {
		return new Specification<UserAuthTbl>() {
			@Override
			public Predicate toPredicate(Root<UserAuthTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.equal(userRoot.<String>get("id").get("userId"), userId);
			}
		};
	}
}

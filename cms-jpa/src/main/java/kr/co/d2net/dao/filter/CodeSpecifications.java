package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl_;
import kr.co.d2net.dto.vo.Search;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(CodeSpecifications.class);

	@SuppressWarnings("serial")
	public static Specification<CodeTbl> findCodesOnlyByParams(final Search search) {
		return new AbstractSpecification<CodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CodeTbl> root) {

				// clf_cd
				Predicate p1 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getClfCd())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Code[clf_cd]: "+search.getClfCd());
					}
					p1 = cb.and(searchClfCd(cb, root, search.getClfCd()));
				}

				// scl_cd
				Predicate p2 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getSclCd())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Code[scl_cd]: "+search.getSclCd());
					}
					p2 = cb.and(searchSclCd(cb, root, search.getSclCd()));
				}

				// clf_gubun
				Predicate p3 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getClfGubun())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Code[clf_gubun]: "+search.getClfGubun());
					}
					p3 = cb.and(searchSclCd(cb, root, search.getClfGubun()));
				}

				// use_yn
				Predicate p4 = cb.conjunction();
				String useYn = StringUtils.defaultIfBlank(search.getUseYn(), "Y");
				if(logger.isDebugEnabled()) {
					logger.debug("Code[use_yn]: "+useYn);
				}
				p4 = cb.and(searchUseYn(cb, root, useYn));

				return cb.and(p1, p2, p3, p4);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<CodeTbl> findCodesByClfCd(final String clfCd) {
		return new AbstractSpecification<CodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CodeTbl> root) {
				return cb.and(searchClfCd(cb, root, clfCd), searchUseYn(cb, root, "Y"));
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<CodeTbl> getCodeWithPK(final String clfCd, final String sclCd) {
		return new AbstractSpecification<CodeTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CodeTbl> root) {
				return cb.and(searchClfCd(cb, root, clfCd), searchSclCd(cb, root, sclCd), searchUseYn(cb, root, "Y"));
			}
		};
	}

	public static Predicate searchClfCd(final CriteriaBuilder cb, final Path<CodeTbl> path, final String clfCd) {
		return cb.equal(path.get(CodeTbl_.clfCd), clfCd);
	}

	public static Predicate searchSclCd(final CriteriaBuilder cb, final Path<CodeTbl> path, final String sclCd) {
		return cb.equal(path.get(CodeTbl_.sclCd), sclCd);
	}

	public static Predicate searchClfGubun(final CriteriaBuilder cb, final Path<CodeTbl> path, final String clfGubun) {
		return cb.equal(path.get(CodeTbl_.clfGubun), clfGubun);
	}

	public static Predicate searchUseYn(final CriteriaBuilder cb, final Path<CodeTbl> path, final String useYn) {
		return cb.equal(path.get(CodeTbl_.useYn), useYn);
	}

}

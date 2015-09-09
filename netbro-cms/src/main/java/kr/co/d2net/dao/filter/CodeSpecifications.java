package kr.co.d2net.dao.filter;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CodeTbl;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * 코드관련 조회함수의 where 조건을 생성하는 함수.
 * @author asura
 *
 */
public class CodeSpecifications {



	/**
	 * 분류코드와, 분류 상세코드를 가지고 조회를 한다.
	 * @param ClfCd
	 * @param SclCd
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> CodeInfo(final String ClfCd, final String SclCd) {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(StringUtils.isNotBlank(SclCd)){

					return cb.and(cb.equal(CodeRoot.<String>get("id").get("clfCD"), ClfCd),cb.equal(CodeRoot.<String>get("id").get("sclCd"), SclCd));							

				}else{

					return cb.and(cb.equal(CodeRoot.<String>get("id").get("clfCD"), ClfCd));

				}

			}

		};

	}

	

	/**
	 * 상세코드명으로 like 검색을 한다.
	 * @param ClfCd
	 * @param ClfNm
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> SclNmLike(final String sclNm) {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				String likePattern="";

				if(StringUtils.isNotBlank(sclNm)){

					likePattern = getLikePattern(sclNm); 

					return cb.like(CodeRoot.<String>get("sclNm"), likePattern);
				}

				return null;	
			}

			private String getLikePattern(final String contentsFormat) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(contentsFormat);
				pattern.append("%");
				return pattern.toString();

			}

		};

	}
	/**
	 * 분류코드명으로 like 검색을 한다.
	 * @param ClfNm
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> ClfNmLike(final String clfNm) {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				String likePattern="";

				if(StringUtils.isNotBlank(clfNm)){

					likePattern = getLikePattern(clfNm); 

					return cb.like(CodeRoot.<String>get("clfNM"), likePattern);
				}

				return null;	
			}

			private String getLikePattern(final String contentsFormat) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(contentsFormat);
				pattern.append("%");
				return pattern.toString();

			}

		};

	}
	/**
	 * 분류코드로 equal 검색을 한다.
	 * @param ClfCd
	 * @param ClfNm
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> ClfEqual(final String clfCd) {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(StringUtils.isNotBlank(clfCd)){

					return cb.equal(CodeRoot.<String>get("id").get("clfCD"),clfCd);

				}

				return null;

			}

		};

	}	

	/**
	 * 분류코드로 LIKE 검색을 한다.
	 * @param ClfCd	
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> ClfLike(final String clfCd) {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(StringUtils.isNotBlank(clfCd)){

					return cb.like(CodeRoot.<String>get("id").<String>get("clfCD"),clfCd);

				}

				return null;

			}

		};

	}	

	/**
	 * 코드관리검색을 한다
	 * @param search
	 * @return
	 */
	public static Specification<CodeTbl> codeFilterSearch(String clfCd, String SclNm) {

		Specifications<CodeTbl> specifications = where(ClfEqual(clfCd));

		return specifications.and(SclNmLike(SclNm));

	}

	/**
	 * 븐류코드명과 분류상세코드명으로 코드관리검색을 한다
	 * @param search
	 * @return
	 */
	public static Specification<CodeTbl> codeFilterSearchByNm(String gubun, String keyWord) {

		
		
		if(gubun.equals("clfNm")){
			Specifications<CodeTbl> specifications = where(ClfNmLike(keyWord));
			return specifications;
		
		}else if(gubun.equals("sclNm")){
			Specifications<CodeTbl> specifications = where(SclNmLike(keyWord));
			return specifications;	
		}else {
			Specifications<CodeTbl> specifications = where(SclNmLike(keyWord)).and(ClfEqual(gubun));
			return specifications;	
		
		}

	}
	/**
	 * 데이터 정리 상태 코드검색을 한다
	 * @param search
	 * @return
	 */
	public static Specification<CodeTbl> dataStatCdSearch(String clfCd, String SclNm) {

		Specifications<CodeTbl> specifications = where(ClfEqual(clfCd));

		return specifications.and(SclNmLike(SclNm));

	}

	/**
	 * 시스템 코드를 제외하는 조건 생성
	 *
	 * @return CriteriaBuilder
	 */
	public static Specification<CodeTbl> ClfNotEqual() {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.notLike(CodeRoot.<String>get("id").<String>get("clfCD"),"S%");				

			}

		};

	}		

}

package kr.co.d2net.dao.filter;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CategoryTbl;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * 코드관련 조회함수의 where 조건을 생성하는 함수.
 * @author asura
 *
 */
public class CategorySpecifications {


	/**
	 * 하위카테고리를 조회하기위한 where 조건
	 * @param  nodes
	 * @param depth
	 * @param orderNum
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> findSubList(final String nodes, final Integer depth) {

		Specifications<CategoryTbl> specifications = where(DepthEqual(depth));

		return specifications.and(NodesLike(nodes));
	}
	
	public static Specification<CategoryTbl> findSubList(final Integer categoryId, final Integer depth) {
		Specifications<CategoryTbl> specifications = where(PreParentEqual(categoryId)).and(DepthEqual(depth));
		return specifications;
	}
	
	public static Specification<CategoryTbl> findSubList(final Integer categoryId) {
		Specifications<CategoryTbl> specifications = where(PreParentEqual(categoryId));
		return specifications;
	}
	
	/**
	 * 순번을 업데이트하기위한 리스트 where 조건
	 * @param  nodes
	 * @param depth
	 * @param orderNum
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> findListForOderNum(final String nodes, final Integer depth, final Integer orderNum) {

		Specifications<CategoryTbl> specifications = where(DepthEqual(depth));

		return specifications.and(NodesLike(nodes)).and(OrderNumGreatThan(orderNum));

	}

	/**
	 * 순번을 업데이트하기위한 리스트 where 조건(between)
	 * @param  nodes
	 * @param depth
	 * @param startOrder
	 * @param endOrder
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> findListForOderNumByBetWeen(final String nodes, final Integer depth, final Integer startOrder , final Integer endOrder) {

		Specifications<CategoryTbl> specifications = where(DepthEqual(depth));

		return specifications.and(NodesLike(nodes)).and(OrderNumBetween(startOrder,endOrder));

	}
	

	/**
	 * depth로 검색한다.
	 * @param depth
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> DepthEqual(final Integer depth) {

		return new Specification<CategoryTbl>() {
			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(depth != null){
					return cb.equal(newCategoryRoot.<Integer>get("depth"), depth);
				}else return null;
			}
		};

	}
	
	/**
	 * depth로 검색한다.
	 * @param depth
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> PreParentEqual(final Integer categoryId) {

		return new Specification<CategoryTbl>() {
			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(categoryId != null && categoryId > 0){
					return cb.equal(newCategoryRoot.<Integer>get("preParent"), categoryId);
				} else {
					return cb.equal(newCategoryRoot.<Integer>get("depth"), 0);
				}
			}
		};

	}
	
	/**
	 * nodes 컬럼값으로 like 검색을 한다.
	 * @param ClfNm
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> NodesLike(final String nodes) {

		return new Specification<CategoryTbl>() {

			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				String likePattern="";

				if(StringUtils.isNotBlank(nodes)){

					likePattern = getLikePattern(nodes); 

					return cb.like(newCategoryRoot.<String>get("nodes"), likePattern);
				}

				return null;	
			}

			private String getLikePattern(final String contentsFormat) {
				StringBuilder pattern = new StringBuilder();
				pattern.append(contentsFormat);
				pattern.append(".%");
				return pattern.toString();

			}

		};

	}
	
	
	/**
	 * 주어진 값보다 큰값을 찾는다(order_num기준)
	 * @param orderNum
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> OrderNumGreatThan(final Integer orderNum) {

		return new Specification<CategoryTbl>() {

			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(orderNum != null){

					return cb.greaterThanOrEqualTo(newCategoryRoot.<Integer>get("orderNum"), orderNum);

				}

				return null;

			}

		};

	}	

	/**
	 * 주어진 값보다 작은값을 찾는다(order_num기준)
	 * @param orderNum
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> OrderNumLessThan(final Integer orderNum) {

		return new Specification<CategoryTbl>() {

			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(orderNum != null){

					return cb.lessThanOrEqualTo(newCategoryRoot.<Integer>get("orderNum"), orderNum);

				}

				return null;

			}

		};

	}	
	
	/**
	 * 주어진 값사이의 값을 찾는다(order_num기준)
	 * @param orderNum
	 * @return CriteriaBuilder
	 */
	public static Specification<CategoryTbl> OrderNumBetween(final Integer startOrder,final Integer endOrder) {

		return new Specification<CategoryTbl>() {

			@Override
			public Predicate toPredicate(Root<CategoryTbl> newCategoryRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				if(startOrder != null && endOrder != null){

					return cb.between(newCategoryRoot.<Integer>get("orderNum"), startOrder,endOrder);

				}

				return null;

			}

		};

	}	
	/**
	 * 븐류코드명과 분류상세코드명으로 코드관리검색을 한다
	 * @param search
	 * @return
	 */
	/*public static Specification<CodeTbl> codeFilterSearchByNm(String gubun, String keyWord) {

		
		
		if(gubun.equals("clfNm")){
			Specifications<CodeTbl> specifications = where(ClfNmLike(keyWord));
			return specifications;
		
		}else {
			Specifications<CodeTbl> specifications = where(SclNmLike(keyWord));
			return specifications;	
		
		}

	}*/
	/**
	 * 데이터 정리 상태 코드검색을 한다
	 * @param search
	 * @return
	 */
	/*public static Specification<CodeTbl> dataStatCdSearch(String clfCd, String SclNm) {

		Specifications<CodeTbl> specifications = where(ClfEqual(clfCd));

		return specifications.and(SclNmLike(SclNm));

	}*/

	/**
	 * 시스템 코드를 제외하는 조건 생성
	 *
	 * @return CriteriaBuilder
	 */
	/*public static Specification<CodeTbl> ClfNotEqual() {

		return new Specification<CodeTbl>() {

			@Override
			public Predicate toPredicate(Root<CodeTbl> CodeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.notLike(CodeRoot.<String>get("id").<String>get("clfCD"),"S%");				

			}

		};

	}		
*/
}

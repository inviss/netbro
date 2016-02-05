package kr.co.d2net.dao.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.UseEnum;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 코드관련 조회함수의 where 조건을 생성하는 함수.
 * @author asura
 *
 */
public class CategorySpecifications {

	private final static Logger logger = LoggerFactory.getLogger(CategorySpecifications.class);

	@SuppressWarnings("serial")
	public static Specification<CategoryTbl> findCategoryOnlyByParams(final Search search) {
		return new AbstractSpecification<CategoryTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CategoryTbl> root) {

				List<Predicate> predicates = new ArrayList<Predicate>();
				
				// category_id
				Predicate predicate = null;
				//Predicate p1 = cb.conjunction();
				if(search.getCategoryId() != null && search.getCategoryId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Category[category_id]: "+search.getCategoryId());
					}
					predicate = cb.and(subListByParent(cb, root, search.getCategoryId()));
					predicates.add(predicate);
				}

				// depth
				//Predicate p2 = cb.conjunction();
				if(search.getDepth() != null && search.getDepth() > -1) {
					if(logger.isDebugEnabled()) {
						logger.debug("Category[depth]: "+search.getDepth());
					}
					predicate = cb.and(subListByDepth(cb, root, search.getDepth()));
					predicates.add(predicate);
				}

				// nodes
				//Predicate p3 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getNodes())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Category[node]: "+search.getNodes());
					}
					predicate = cb.and(nodeLikes(cb, root, search.getNodes()));
					predicates.add(predicate);
				}

				// orderNum
				//Predicate p4 = cb.conjunction();
				if(search.getOrderNum() != null && search.getOrderNum() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Category[orderNum]: "+search.getOrderNum());
					}
					predicate = cb.and(orderNumGreatThan(cb, root, search.getOrderNum()));
					predicates.add(predicate);
				}
				
				// use_yn
				//Predicate p5 = cb.conjunction();
				String useYn = StringUtils.defaultIfBlank(search.getUseYn(), "Y");
				if(logger.isDebugEnabled()) {
					logger.debug("Category[use_yn]: "+useYn);
				}
				predicate = cb.and(useYn(cb, root, useYn));
				predicates.add(predicate);

				if(search.getOperator() == Operator.LIST) {
					Path<Integer> order = root.get(CategoryTbl_.orderNum);
					cq.orderBy(cb.asc(order));
				}

				return cb.and((Predicate[])predicates.toArray());
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<CategoryTbl> getCategoryWithPK(final Integer categoryId) {
		return new AbstractSpecification<CategoryTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CategoryTbl> root) {
				return cb.and(categoryObj(cb, root, categoryId));
			}
		};
	}

	public static Predicate categoryObj(final CriteriaBuilder cb, final Path<CategoryTbl> path, final Integer category) {
		return cb.equal(path.get(CategoryTbl_.categoryId), category);
	}

	public static Predicate subListByParent(final CriteriaBuilder cb, final Path<CategoryTbl> path, final Integer parent) {
		return cb.equal(path.get(CategoryTbl_.preParent), parent);
	}

	public static Predicate subListByDepth(final CriteriaBuilder cb, final Path<CategoryTbl> path, final Integer depth) {
		return cb.equal(path.get(CategoryTbl_.depth), depth);
	}

	public static Predicate nodeLikes(final CriteriaBuilder cb, final Path<CategoryTbl> path, final String nodes) {
		return cb.like(path.get(CategoryTbl_.nodes), singlePattern(nodes));
	}

	public static Predicate useYn(final CriteriaBuilder cb, final Path<CategoryTbl> path, final String useYn) {
		if(useYn.equals("Y")){
			return cb.equal(path.get(CategoryTbl_.useYn), UseEnum.Y);
		}else{
			return cb.equal(path.get(CategoryTbl_.useYn), UseEnum.N);			
		}
	}

	private static String singlePattern(final String keyword) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(keyword);
		pattern.append("%");

		return pattern.toString();
	}
	
	public static Predicate orderNumGreatThan(final CriteriaBuilder cb, final Path<CategoryTbl> path, final Integer orderNum) {
		return cb.greaterThanOrEqualTo(path.get(CategoryTbl_.orderNum), orderNum);
	}


}

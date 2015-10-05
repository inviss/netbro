package kr.co.d2net.dao.filter;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsModTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.DisuseInfoTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentsModSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(ContentsModSpecifications.class);

	/**
	 * contentsInstTbl의 where where 조건절을 구현한다. 검색하고자 하는 
	 * 필드의 값이 존재하는 경우에만 where 조건을 추가한다.
	 * @param contentsInst
	 * @return
	 */
	@SuppressWarnings("serial")
	public static Specification<ContentsModTbl> findContentsMod(final Search search) {
		return new AbstractSpecification<ContentsModTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsModTbl> root) {
			    
				Predicate defultJoin = cb.conjunction();
				
				defultJoin= cb.equal(root.get(ContentsModTbl_.ctId),search.getCtId());
			 
				if(search.getOperator() == Operator.LIST){
				 	Path<String> dataStatCd = root.get(ContentsModTbl_.dataStatcd);
				 	Path<Date> modDt = root.get(ContentsModTbl_.modDt);
					Path<String> modId = root.get(ContentsModTbl_.modId);
					Path<Long> ctId = root.get(ContentsModTbl_.ctId);
				    Path<Long> seq = root.get(ContentsModTbl_.seq);
					
					cq.multiselect(dataStatCd, modId, modDt, ctId,seq);
					
					cq.orderBy(cb.desc(root.get(ContentsModTbl_.modDt)));
				}
		 
				return cb.and(defultJoin);
			}
		};
	}
 
	
}

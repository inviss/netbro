package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.search.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
/** 
 * ROLE_AUTH_TBL의 쿼리문 핸들링 class.
 * @author vayne
 *
 */
public class MenuSpecifications {
	
	final static Logger logger = LoggerFactory.getLogger(ObjectLikeSpecifications.class);

	public static Specification<MenuTbl> validator(final Integer depth) {
		return new Specification<MenuTbl>() {
			@Override
			public Predicate toPredicate(Root<MenuTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.equal(root.<Integer>get("depth"), depth);
			}
		};
	}
	
	
	/*
	 * 
	
	public static Expression menuFilterSearch(CriteriaBuilder cb,CriteriaQuery cq, Search search, Root<MenuTbl> root,MenuTbl menuTbl){
		//(cb.equal(root.<Integer>get("depth"), menuTbl.getDepth());

		Predicate totalWhere = null;
		
		if(menuTbl.getDepth() == 0 && search.getNode().equals("root")){
			logger.debug("getMenuId  ============== 0");
			totalWhere = cb.and((cb.equal(root.<Integer>get("depth"), menuTbl.getDepth())));
		}else if(menuTbl.getMenuId() != 0){
			logger.debug("getMenuId  ============== !!!!!0");
			totalWhere = cb.and((cb.equal(root.<Integer>get("depth"), menuTbl.getDepth()+1)),cb.equal(root.get("groupId"), search.getNode()));
		}
		
		return totalWhere; 

	}
	 */
	
}

package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.EquipmentTbl;

import org.springframework.data.jpa.domain.Specification;

/**
 * USER_TBL 쿼리문핸들링 class.
 * @author vayne
 *
 */
public class EquipSpecifications {
	public static Specification<EquipmentTbl> equipFilterSearchByNm(final String searchGubun,final String searchNm) {
		return new Specification<EquipmentTbl>() {
			@Override
			public Predicate toPredicate(Root<EquipmentTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(searchGubun.equals("id")){
					return cb.and(cb.like(userRoot.<String>get("deviceId"), "%"+searchNm+"%"));
				}else{
					return cb.and(cb.like(userRoot.<String>get("deviceNm"), "%"+searchNm+"%"));
				}
				
			}
		};
	}
	
	
	public static Specification<EquipmentTbl> equipKinds(final String equipKinds) {
		return new Specification<EquipmentTbl>() {
			@Override
			public Predicate toPredicate(Root<EquipmentTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(cb.like(userRoot.get("id").<String>get("deviceId"), "%"+equipKinds+"%"),cb.equal(userRoot.get("id").<Integer>get("deviceNum"), 1));
			}
		};
	}
	
	
	/**
	 * 
	 * @param deviceId
	 * @param deviceNum
	 * @return
	 */
	public static Specification<EquipmentTbl> getEquipInfo(final String deviceId,final Integer deviceNum) {
		return new Specification<EquipmentTbl>() {
			@Override
			public Predicate toPredicate(Root<EquipmentTbl> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(cb.equal(userRoot.get("id").<String>get("deviceId"), deviceId),cb.equal(userRoot.get("id").<Integer>get("deviceNum"), deviceNum));
			}
		};
	}

}

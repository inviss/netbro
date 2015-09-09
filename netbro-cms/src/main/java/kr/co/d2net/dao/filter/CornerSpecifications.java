package kr.co.d2net.dao.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CornerTbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public class CornerSpecifications {

	final static Logger logger = LoggerFactory.getLogger(CornerSpecifications.class);



	/**
	 * 영상id로 검색
	 * @param search
	 * @return
	 */
	public static Specification<CornerTbl> CtIdEqual(final Long ctId) {

		return new Specification<CornerTbl>() {

			@Override
			public Predicate toPredicate(Root<CornerTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			
				if(ctId != 0 ) {

					logger.debug("##############ctId "+ctId);
					return cb.equal(root.get("ctId"), ctId);

				} else return null;

			}

		

		};

	}

	
	
}

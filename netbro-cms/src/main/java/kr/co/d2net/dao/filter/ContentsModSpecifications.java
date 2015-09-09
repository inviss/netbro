package kr.co.d2net.dao.filter;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsModTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class ContentsModSpecifications {

	final static Logger logger = LoggerFactory.getLogger(ContentsModSpecifications.class);



	/**
	 * contents_tbl의 데이터상태값을 가지고 가장 최근의 수정상태를 조회한다.
	 * @param search
	 * @return
	 */
	public static Specification<ContentsModTbl> whereCodition(final Search search) {

		return new Specification<ContentsModTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsModTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			
				Join<ContentsTbl, ContentsModTbl> inst = root.join("contentsTbl", JoinType.INNER);
						return cb.and(cb.equal(root.get(ContentsModTbl_.dataStatcd), search.getDataStatCd()) ,cb.equal(inst.<Long>get("ctId"), search.getCtId()));
							
			}

		};

	}
	/**
	 * 기본정보 조회시 해당컨텐츠를 마지막으로 수정한 사람의id를 가져온다.
	 * @param search
	 * @return
	 */
	public static Specification<ContentsModTbl> ctIdEqual(final Search search) {

		return new Specification<ContentsModTbl>() {

			@Override
			public Predicate toPredicate(Root<ContentsModTbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
						return cb.equal(root.get("ctId"), search.getCtId());
							
			}

		};

	}

	
}

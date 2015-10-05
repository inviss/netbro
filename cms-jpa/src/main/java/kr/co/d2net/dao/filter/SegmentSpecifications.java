package kr.co.d2net.dao.filter;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.DisuseInfoTbl_;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.NoticeTbl_;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(SegmentSpecifications.class);

	/**
	 * contentsInstTbl의 where where 조건절을 구현한다. 검색하고자 하는 
	 * 필드의 값이 존재하는 경우에만 where 조건을 추가한다.
	 * @param contentsInst
	 * @return
	 */
	@SuppressWarnings("serial")
	public static Specification<SegmentTbl> findSegment(final Search search) {
		return new AbstractSpecification<SegmentTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<SegmentTbl> root) {
			    
				//제목검색
				Predicate p1 = cb.conjunction();
				Predicate p2 = cb.conjunction();
				if((search.getCategoryId() != null && search.getCategoryId() > 0) && (search.getEpisodeId() != null && search.getEpisodeId() > 0)){
					if(logger.isDebugEnabled()) {
						logger.debug("Segment[categoryId]: "+search.getCategoryId());
						logger.debug("Segment[episodeId]: "+search.getEpisodeId());
					}
					p1 = episodeIdEqual(cb, root, search.getEpisodeId());
					p2 = categoryEqual(cb, root, search.getCategoryId());
				}
			 
					 
					cq.orderBy(cb.desc(root.get(SegmentTbl_.regDt)));
			 
				return cb.and(p1,p2);
			}
		};
	}

	
	public static Predicate episodeIdEqual(final CriteriaBuilder cb, final Path<SegmentTbl> path, final Integer episodeId) {
		return cb.equal(path.get(SegmentTbl_.episodeId), episodeId);
	}
	
	public static Predicate categoryEqual(final CriteriaBuilder cb, final Path<SegmentTbl> path, final Integer categoryId) {
		return cb.equal(path.get(SegmentTbl_.categoryId), categoryId);
	}
	 
	
	 
}

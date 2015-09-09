package kr.co.d2net.dao.filter;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.json.User;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Notice;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * 공지사항관련  쿼리문핸들링 class.
 * @author vayne
 *
 */
public class NoticeSpecifications {

	public static Specification<NoticeTbl> fromNow(final Notice notice) {
		return new Specification<NoticeTbl>() {
			@Override
			public Predicate toPredicate(Root<NoticeTbl> noticeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.greaterThanOrEqualTo(noticeRoot.<Date>get("endDd"), notice.getStartDd())
						,cb.equal(noticeRoot.get("popUpYn"), "Y"));
			}
		};
	}

	
	public static Specification<NoticeTbl> noticeWhere(final Notice notice) {
		return new Specification<NoticeTbl>() {
			@Override
			public Predicate toPredicate(Root<NoticeTbl> noticeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				Root<UserTbl> user = query.from(UserTbl.class);
				Predicate commonWhere = cb.equal(noticeRoot.get("regId"), user.get("userId"));
				if(StringUtils.isNotBlank(notice.getKeyword())){
				if(notice.getSearchFiled().equals("title")){
					return cb.and(commonWhere,cb.like(noticeRoot.<String>get("title"), "%"+notice.getKeyword()+"%"));
				}else{
					return cb.and(commonWhere,cb.like(user.<String>get("userNm"), "%"+notice.getKeyword()+"%"));
				}
				}
				return commonWhere;
			}
		};
	}

	

	
}

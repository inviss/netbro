package kr.co.d2net.dao.filter;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.NoticeTbl_;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.dto.vo.Notice;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * 공지사항관련  쿼리문핸들링 class.
 * @author vayne
 *
 */
public class NoticeSpecifications {

	/**
	 * 
	 * @param notice
	 * @return
	 */
	public static Specification<NoticeTbl> fromNow(final Notice notice) {
		return new Specification<NoticeTbl>() {
			@Override
			public Predicate toPredicate(Root<NoticeTbl> noticeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				return cb.and(cb.greaterThanOrEqualTo(noticeRoot.get(NoticeTbl_.endDd), notice.getStartDd())
						,cb.equal(noticeRoot.get(NoticeTbl_.popUpYn), "Y"));
			}
		};
	}


	/**
	 * 
	 * @param notice
	 * @return
	 */
	public static Specification<NoticeTbl> noticeWhere(final Notice notice) {
		return new Specification<NoticeTbl>() {
			@Override
			public Predicate toPredicate(Root<NoticeTbl> noticeRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {     
				Root<UserTbl> user = query.from(UserTbl.class);
				Predicate commonWhere = cb.equal(noticeRoot.get(NoticeTbl_.regId), user.get(UserTbl_.userId));
				if(StringUtils.isNotBlank(notice.getKeyword())){
					if(notice.getSearchFiled().equals("title")){
						return cb.and(commonWhere,cb.like(noticeRoot.get(NoticeTbl_.title), "%"+notice.getKeyword()+"%"));
					}else{
						return cb.and(commonWhere,cb.like(user.<String>get(UserTbl_.userNm), "%"+notice.getKeyword()+"%"));
					}
				}
				return commonWhere;
			}
		};
	}

}

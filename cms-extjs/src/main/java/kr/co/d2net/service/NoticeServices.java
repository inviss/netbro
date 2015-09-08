package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.NoticeDao;
import kr.co.d2net.dao.filter.NoticeSpecifications;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.NoticeTbl_;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private MessageSource messageSource;

	@PersistenceContext
	private EntityManager em;

	/**
	 * 신규로 공지사항을 생성한다.
	 * 
	 * @param params
	 * @return
	 */
	@Modifying
	@Transactional
	public void insertNotice(Notice notice) throws ServiceException {

		NoticeTbl noticeTbl = new NoticeTbl();
		noticeTbl.setCont(notice.getCont());
		noticeTbl.setTitle(notice.getTitle());
		noticeTbl.setRegDt(new Date());
		noticeTbl.setRegId(notice.getRegId());
		noticeTbl.setPopUpYn(notice.getPopUpYn());

		if (notice.getPopUpYn().equals("Y")) {
			Calendar endDd = Calendar.getInstance();
			endDd.setTime(notice.getEndDd());
			endDd.set(Calendar.HOUR_OF_DAY, 23);
			endDd.set(Calendar.MINUTE, 59);
			endDd.set(Calendar.SECOND, 59);
			notice.setEndDd(endDd.getTime());
			noticeTbl.setStartDd(notice.getStartDd());
			noticeTbl.setEndDd(notice.getEndDd());
		}
		
		noticeDao.save(noticeTbl);
	}

	/**
	 * 공지사항을 수정한다.
	 * 
	 * @param params
	 * @return
	 */
	@Modifying
	@Transactional
	public void updateNotice(Notice notice) throws ServiceException {

		NoticeTbl noticeTbl = getNoticeObj(notice);
		
		noticeTbl.setCont(notice.getCont());
		noticeTbl.setTitle(notice.getTitle());
		noticeTbl.setModDt(new Date());
		noticeTbl.setModId(notice.getRegId());
		noticeTbl.setPopUpYn(notice.getPopUpYn());
		
		if (notice.getPopUpYn().equals("Y")) {
			Calendar endDd = Calendar.getInstance();
			endDd.setTime(notice.getEndDd());
			endDd.set(Calendar.HOUR_OF_DAY, 23);
			endDd.set(Calendar.MINUTE, 59);
			endDd.set(Calendar.SECOND, 59);
			notice.setEndDd(endDd.getTime());
			noticeTbl.setStartDd(notice.getStartDd());
			noticeTbl.setEndDd(notice.getEndDd());
		}
		
		noticeTbl.setNoticeId(notice.getNoticeId());
		noticeDao.save(noticeTbl);
	}

	/**
	 * 공지사항 상세정보를 조회한다.
	 * 
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */

	public NoticeTbl getNoticeObj(Notice notice) throws ServiceException {
		return noticeDao.findOne(notice.getNoticeId());

	}

	public Notice getNoticeInfo(Notice notice) throws ServiceException {

		String[] noticeFields = { "authId", "title", "cont", "regId", "modId",
				"regDt", "startDd", "endDd", "modDt", "popUpYn" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<NoticeTbl> root = cq.from(NoticeTbl.class);

		Path<Long> noticeId = root.get(NoticeTbl_.noticeId);
		Path<String> title = root.get(NoticeTbl_.title);
		Path<String> cont = root.get(NoticeTbl_.cont);
		Path<String> regId = root.get(NoticeTbl_.regId);
		Path<String> modId = root.get(NoticeTbl_.modId);
		Path<Date> regDt = root.get(NoticeTbl_.regDt);
		Path<Date> startDd = root.get(NoticeTbl_.startDd);
		Path<Date> endDd = root.get(NoticeTbl_.endDd);
		Path<Date> modDt = root.get(NoticeTbl_.modDt);
		Path<String> popUpYn = root.get(NoticeTbl_.popUpYn);

		cq.multiselect(noticeId, title, cont, regId, modId, regDt, startDd,
				endDd,modDt,popUpYn);
		cq.where(cb.equal(noticeId, notice.getNoticeId()));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		Object[] tempObj = typedQuery.getSingleResult();
		Notice notice2 = new Notice();

		for(int j = 0; j<noticeFields.length; j++){
			ObjectUtils.setProperty(notice2, noticeFields[j], tempObj[j]);			
		}
		return notice2;
	}

	/**
	 * 공지사항 리스트를 조회한다
	 * 
	 * @param notice
	 * @return List<NoticeTbl>
	 */

	public List<Notice> findNoticeList(Notice notice, Search search)
			throws ServiceException {

		String[] noticeFields = { "noticeId", "cont", "endDd", "startDd",
				"popUpYn", "regDt", "regId", "title" };
		String[] userFields = { "userNm" };

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder
				.createQuery(Object[].class);

		Root<NoticeTbl> from = criteriaQuery.from(NoticeTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);

		Predicate commonWhere = criteriaBuilder.equal(from.get("regId"),
				user.get("userId"));

		if (StringUtils.isNotBlank(notice.getKeyword())) {
			if (notice.getSearchFiled().equals("title")) {
				commonWhere = criteriaBuilder.and(
						commonWhere,
						criteriaBuilder.like(from.<String> get("title"), "%"
								+ notice.getKeyword() + "%"));
			} else {
				commonWhere = criteriaBuilder.and(
						commonWhere,
						criteriaBuilder.like(user.<String> get("userNm"), "%"
								+ notice.getKeyword() + "%"));
			}
		}

		criteriaQuery.where(commonWhere);

		Selection[] s = new Selection[noticeFields.length + userFields.length];

		int i = 0;

		for (int j = 0; j < noticeFields.length; j++) {
			s[i] = from.get(noticeFields[j]);
			i++;
		}

		for (int j = 0; j < userFields.length; j++) {
			s[i] = user.get(userFields[j]);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)).orderBy(
				criteriaBuilder.desc(from.get("noticeId")));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		int startPage = 0;
		int endPage = 0;

		startPage = (notice.getPageNo() - 1) * SearchControls.NOTICE_LIST_COUNT;
		endPage = startPage + SearchControls.NOTICE_LIST_COUNT;

		if (logger.isDebugEnabled()) {
			logger.debug("startPage :" + startPage);
			logger.debug("endPage :" + endPage);
		}

		typedQuery.setFirstResult(startPage);
		typedQuery.setMaxResults(endPage);

		List<Object[]> list2 = typedQuery.getResultList();

		System.out.println("list2 :" + list2.size());
		List<Notice> notices = new ArrayList<Notice>();

		for (Object[] list : list2) {
			Notice info = new Notice();

			i = 0;
			for (int j = 0; j < noticeFields.length; j++) {
				ObjectUtils.setProperty(info, noticeFields[j], list[i]);
				i++;
			}

			for (int j = 0; j < userFields.length; j++) {
				ObjectUtils.setProperty(info, userFields[j], list[i]);
				i++;
			}
			notices.add(info);
		}
		return notices;
	}

	/**
	 * 공지사항 총조회갯수를 구한다
	 * 
	 * @param notice
	 * @return List<NoticeTbl>
	 */

	public Long countNotice(Notice notice) throws ServiceException {
		return noticeDao.count(NoticeSpecifications.noticeWhere(notice));
	}

	/**
	 * 로그인시 로그인 날 기준으로 팝업공지 정보를 조회한다
	 * 
	 * @param notice
	 * @return List<NoticeTbl>
	 */

	public List<NoticeTbl> findAfterLoginPopUpNoticeList(Notice notice)
			throws ServiceException {
		return noticeDao.findAll(NoticeSpecifications.fromNow(notice));
	}

	/**
	 * 공지사항을 삭제한다.
	 * 
	 * @param params
	 * @return
	 */
	@Modifying
	@Transactional
	public void deleteNotice(Notice notice) throws ServiceException {
		noticeDao.delete(notice.getNoticeId());
	}
}

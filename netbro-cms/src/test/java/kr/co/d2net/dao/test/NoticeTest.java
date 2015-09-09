package kr.co.d2net.dao.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.MenuDao;
import kr.co.d2net.dao.NoticeDao;
import kr.co.d2net.dao.filter.StatisticsSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.UserServices;
import kr.co.d2net.utils.ObjectUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class NoticeTest extends BaseDaoConfig {
	@PersistenceContext
	private EntityManager em;

	
	@Autowired
	private NoticeDao noticeDao;
	@Ignore
	@Test
	public void addAll() {
		try {
			NoticeTbl info = new NoticeTbl();
			info.setCont("11");
			info.setRegDt(new Date());
			info.setRegId("admin");
			info.setPopUpYn("N");
			noticeDao.save(info);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Ignore
	@Test
	public void getList() {

		String[] noticeFields = {"noticeId", "cont", "endDd","startDd","popUpYn","regDt","regId","title"};
		String[] userFields = {"userNm"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<NoticeTbl> from = criteriaQuery.from(NoticeTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);
		
		Predicate commonWhere = criteriaBuilder.equal(from.get("regId"), user.get("userId"));
		criteriaQuery.where(commonWhere);
	
		//criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.<Integer>get("menuId"), role_auth.get("id").get("menuId"))));
		Selection[] s = new Selection[noticeFields.length + userFields.length];

		int i=0;

		for(int j = 0; j < noticeFields.length; j++) {

			s[i] = from.get(noticeFields[j]);
			i++;

		}

		for(int j = 0; j < userFields.length; j++) {	

			s[i] = user.get(userFields[j]);				
			i++;

		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("noticeId")));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);

	

		List<Object[]> list2 = typedQuery.getResultList();

		//logger.debug("list2 :"+list2.size());
		System.out.println("list2 :"+list2.size());
		List<Notice> notices = new ArrayList<Notice>();

		int k=0;
		for(Object[] list : list2) {/*

			Statistic info = new Statistic();

			i=0;

			for(int j = 0; j < menuFields.length; j++) {

				ObjectUtils.setProperty(info, menuFields[j], list[i]);
				i++;

			}

			for(int j = 0; j < roleAuthFields.length; j++) {

				ObjectUtils.setProperty(info, roleAuthFields[j], list[i]);
				i++;

			}

		
			//20131218 페이지 값 1이 넘어오면 30건 조회하는 현상발생. 원인 분석필요

			if(k < 20){

				statistics.add(info);
				k++;

			}

		*/}

	///	return statistics;

	}
	
	@Test
	public void getuserInfo() {
		UserServices services = new UserServices();
		try {
			services.getUserObj("void");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

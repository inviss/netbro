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
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.MenuDao;
import kr.co.d2net.dao.filter.StatisticsSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.utils.ObjectUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MenuTest extends BaseDaoConfig {
	@PersistenceContext
	private EntityManager em;

	
	@Autowired
	private MenuDao menuDao;

	@Test
	public void addAll() {
		try {
			//Set<MenuTbl> menus = new HashSet<MenuTbl>();
			
			MenuTbl menu = new MenuTbl();
			//menu.setMenuId(1);
			//menu.setLft(1);
			menu.setMenuEnNm("notice");
			menu.setMenuNm("공지사항관리");
		//	menu.setRgt(14);
			menu.setUrl(" /admin/category/category.ssc");
			menu.setUseYn("Y");
			
			menuDao.save(menu);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Ignore
	@Test
	public void StatisticDetailInfo() {

		String[] menuFields = {"menuId","menuNm","url","menuEnNm"};
		String[] roleAuthFields = {"controlGubun"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<MenuTbl> from = criteriaQuery.from(MenuTbl.class);
		Root<RoleAuthTbl> role_auth = criteriaQuery.from(RoleAuthTbl.class);
		Root<UserAuthTbl> user_auth = criteriaQuery.from(UserAuthTbl.class);
		Root<UserTbl> userId = criteriaQuery.from(UserTbl.class);
		
		
		criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.get("menuId"), role_auth.get("id").get("menuId")),
												criteriaBuilder.equal(role_auth.get("id").get("authId"), user_auth.get("id").get("authId")),
												criteriaBuilder.equal(user_auth.get("id").get("userId"), userId.get("userId")),
												criteriaBuilder.equal(userId.get("userId"), "a7")));

		//criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.<Integer>get("menuId"), role_auth.get("id").get("menuId"))));
		Selection[] s = new Selection[menuFields.length + roleAuthFields.length];

		int i=0;

		for(int j = 0; j < menuFields.length; j++) {

			s[i] = from.get(menuFields[j]);
			i++;

		}

		for(int j = 0; j < roleAuthFields.length; j++) {	

			s[i] = role_auth.get(roleAuthFields[j]);				
			i++;

		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("menuId")));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);

	

		List<Object[]> list2 = typedQuery.getResultList();

		//logger.debug("list2 :"+list2.size());
		System.out.println("list2 :"+list2.size());
		List<Statistic> statistics = new ArrayList<Statistic>();

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
	
	
	
	
}

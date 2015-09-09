package kr.co.d2net.dao.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;


import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.elasticsearch.monitor.MonitorService;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.controller.MonitoringController;
import kr.co.d2net.controller.UserController;
import kr.co.d2net.dao.RoleAuthDao;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.AuthTbl_;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserAuthTbl.UserAuthId;
import kr.co.d2net.dto.UserAuthTbl_;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;

import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.*;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.*;

public class SampleTest extends BaseDaoConfig {


	final Logger logger = LoggerFactory.getLogger(getClass());



	@Autowired
	private UserServices userServices;

	@Autowired
	private RoleAuthServices authServices;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleAuthDao roleAuthDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MenuServices menuServices;



@Test
@Ignore
public void addAll() {


	try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserTbl> cq = cb.createQuery(UserTbl.class);
		Metamodel m = em.getMetamodel();
		EntityType<UserTbl> userEntity = m.entity(UserTbl.class);



		Root<UserTbl> root = cq.from(UserTbl.class);
		Root<UserAuthTbl> root1 = cq.from(UserAuthTbl.class);
		Root<AuthTbl> root2 = cq.from(AuthTbl.class);

		Path<String> userId = root.get(UserTbl_.userId);
		Path<String> userNm = root.get(UserTbl_.userNm);
		Path<String> userPasswd = root.get(UserTbl_.userPass);
		Path<String> userPhone = root.get(UserTbl_.userPhone);

		Join<UserTbl,UserAuthTbl> test = root.join(userEntity.getSet("userTbl",UserAuthTbl.class),JoinType.INNER);

		//Path<String> userId1 = root1.get(UserAuthTbl_.userId);

		cq.multiselect(userId,userNm,userPasswd,userPhone);
		em.createQuery(cq).getResultList();

		//return (Integer) em.createQuery(cq).getSingleResult();
	} catch (Exception e) {
		logger.error("findCategory Error", e);
	}
	//return null;


}

@Test
@Ignore
public void test(){

	String[] ctFields = {"ctId", "ctNm", "categoryId"};
	String[] ctiFields = {"ctiId", "flPath", "wrkFileNm"};

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery cq = cb.createQuery();


	Root<UserTbl> root = cq.from(UserTbl.class);
	Root<UserAuthTbl> root1 = cq.from(UserAuthTbl.class);
	Root<AuthTbl> root2 = cq.from(AuthTbl.class);


	Path<String> userId = root.get(UserTbl_.userId);
	Path<String> userNm = root.get(UserTbl_.userNm);
	Path<String> userPasswd = root.get(UserTbl_.userPass);
	Path<String> userPhone = root.get(UserTbl_.userPhone);

	Path<String> authNm = root2.get(AuthTbl_.authNm);
	Path<Integer> authId = root2.get(AuthTbl_.authId);


	//cq.multiselect(root,root1,root2);
	//cq.multiselect(userNm.alias("userNm"),userId.alias("userId"),userPhone.alias("userPhone"),userPasswd.alias("userpasswd"),authNm.alias("authNm"),authId.alias("authId"));

	cq.where(cb.and(cb.equal(userId,root1.get("id").get("userId")),cb.equal(root1.get("id").get("authId"),root2.get("authId")),cb.equal(root.get("userId"), "a2")));



	em.createQuery(cq).getResultList();



	//Join<UserTbl, UserAuthTbl>  p = (Join<UserTbl, UserAuthTbl>) root.fetch(UserTbl_.userId);




}







@Test
@Ignore
public void test23() throws ServiceException{
	Search search = new Search();

	Integer authId = 12;

	String tmp = authId.toString();


	UserServices services = new UserServices();
	UserAuthServices authServices = new UserAuthServices();

	UserTbl userTbl = new UserTbl();
	UserAuthTbl userAuthTbl = new UserAuthTbl();
	UserAuthId userAuthId = new UserAuthId();

	for(int i = 0; i<tmp.length(); i++){

		Integer aa = tmp.charAt(i)-48;

		search.setAuthId(aa);
		search.setUserId("a2");
		userAuthId.setAuthId(search.getAuthId());
		userAuthId.setUserId(search.getUserId());
		userAuthTbl.setId(userAuthId);

		//userTbl.addUserAuth(userAuthTbl);
		//userDao.save(userTbl);
		authServices.add(userAuthTbl);

	}

}


@Test
@Ignore
public void test25(){/*
	List<MenuTbl> menuList = new ArrayList<MenuTbl>();
	List items = new ArrayList();
	Map<String, Object> params = new HashMap<String, Object>();

	params.put("userId", "a2");

	menuList = menuServices.findUserMenus(params);

	if(menuList != null){
		for(int i = 0; i<menuList.size();i++){
			if(!items.contains(menuList.get(i))){
				items.add(menuList.get(i));
			}
		}
	}

	List<MenuTbl> menuList2 = new ArrayList<MenuTbl>(new HashSet<MenuTbl>(menuList));
	HashSet<MenuTbl> menuList3 = new HashSet<MenuTbl>(menuList);

	logger.debug("menuList2 : " + menuList2);
	logger.debug("menuList3 : " + menuList3);
	logger.debug("menuList3 : " + items);
*/}

@Test
public void test111(){
	String test = "/201402/06/샘플1_201402061030.mov.mov";
	
	logger.debug(test.substring(test.indexOf('_')+1,test.lastIndexOf('.')));
	
}


}
package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.MenuDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.MenuTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.MenuTreeChildren;
import kr.co.d2net.dto.vo.MenuTreeForExtJs;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class MenuServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private MessageSource messageSource;


	@PersistenceContext
	private EntityManager em;

	@Modifying
	@Transactional
	public void addAll(Set<MenuTbl> menus) {
		menuDao.save(menus);
	}

	@Modifying
	@Transactional
	public void add(MenuTbl menu) throws ServiceException{
		menuDao.save(menu);
	}


	/**
	 * 메뉴 정보를 조회함.
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public List<MenuTbl> findUserMenus(Map<String, Object> params) throws ServiceException{

		String[] menuFields = {"menuId", "menuNm", "menuEnNm","regDt","url"};
		String[] roleAuthFields = {"controlGubun"};
		String[] userFields = {"userId"};
		String[] authFields = {"authId"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<MenuTbl> from = criteriaQuery.from(MenuTbl.class);
		Root<RoleAuthTbl> roleAuth = criteriaQuery.from(RoleAuthTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);

		Join<UserTbl, UserAuthTbl> userAuth = user.join("userAuth", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.roleAuthInMenuForUser(criteriaBuilder,criteriaQuery,from,roleAuth,user,userAuth,String.valueOf(params.get("userId"))));

		Selection[] s = new Selection[menuFields.length + roleAuthFields.length +userFields.length+authFields.length];

		int i=0;

		for(int j=0; j<menuFields.length; j++) {
			s[i] = from.get(menuFields[j]);
			i++;
		}
		for(int j=0; j<roleAuthFields.length; j++) {	
			s[i] = roleAuth.get(roleAuthFields[j]);				
			i++;
		}
		for(int j=0; j<userFields.length; j++) {	
			s[i] = user.get(userFields[j]);				
			i++;
		}
		for(int j=0; j<authFields.length; j++) {	
			s[i] = userAuth.get("id").get("authId");				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.asc(from.get("menuId")));

		/* Query Cache 추가 */
		TypedQuery<Object[]> typedQuery = em.createQuery(select).setHint("org.hibernate.cacheable", true);

		List<Object[]> list2 = typedQuery.getResultList(); 
		List<MenuTbl> menus = new ArrayList<MenuTbl>();

		for(Object[] list : list2) {
			MenuTbl menu = new MenuTbl();

			i=0;
			for(int j=0; j<menuFields.length; j++) {
				ObjectUtils.setProperty(menu, menuFields[j], list[i]);
				i++;
			}
			for(int j=0; j<roleAuthFields.length; j++) {
				ObjectUtils.setProperty(menu, roleAuthFields[j], list[i]);
				i++;
			}
			for(int j=0; j<userFields.length; j++) {
				ObjectUtils.setProperty(menu, userFields[j], list[i]);
				i++;
			}
			for(int j=0; j<authFields.length; j++) {
				ObjectUtils.setProperty(menu, authFields[j], list[i]);
				i++;
			}
			menus.add(menu);
		}
		return menus;
	}



	/**
	 * 메뉴테이블에 정보 존재여부 파악
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public long findMenuCount() throws ServiceException{
		return menuDao.count();
	}


	/**
	 * 권한정보 입력창에서 radiobox를 가져오기 위한 method
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Menus> findMenuList() throws ServiceException{

		String[] menuFields = {"menuId","menuNm", "menuEnNm","url","useYn"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<MenuTbl> root = cq.from(MenuTbl.class);

		Path<Integer> menuId = root.get(MenuTbl_.menuId);
		Path<String> menuNm = root.get(MenuTbl_.menuNm);
		Path<String> menuEnNm = root.get(MenuTbl_.menuEnNm);
		Path<String> url = root.get(MenuTbl_.url);
		Path<String> useYn = root.get(MenuTbl_.useYn);

		cq.multiselect(menuId,menuNm,menuEnNm,url,useYn);
		cq.orderBy(cb.asc(menuId));

		/* Query Cache 추가 */
		TypedQuery<Object[]> typedQuery = em.createQuery(cq).setHint("org.hibernate.cacheable", true);

		List<Object[]> menuList =  typedQuery.getResultList();

		if(menuList != null){
			List<Menus> menus = new ArrayList<Menus>();

			for(Object[] list : menuList) {
				Menus tra = new Menus();

				int i = 0;
				for(int j = 0; j<menuFields.length; j++){
					ObjectUtils.setProperty(tra, menuFields[j], list[i]);			
					i++;
				}
				menus.add(tra);
			}
			return menus;
		}else{
			return Collections.EMPTY_LIST;
		}
	}



	/**
	 * 메뉴id에 소속된 메뉴 리스트를조회한다.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public List<Menus> findSubMenuList(Search search) throws ServiceException{

		String[] menuFields = {"menuId", "menuNm", "menuEnNm","regDt","url"};
		String[] roleAuthFields = {"controlGubun"};
		String[] userFields = {"userId"};
		String[] authFields = {"authId"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<MenuTbl> from = criteriaQuery.from(MenuTbl.class);
		Root<RoleAuthTbl> roleAuth = criteriaQuery.from(RoleAuthTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);

		Join<UserTbl, UserAuthTbl> userAuth = user.join("userAuth", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.subMenuForUser(criteriaBuilder,criteriaQuery,from,roleAuth,user,userAuth,search));

		Selection[] s = new Selection[menuFields.length + roleAuthFields.length +userFields.length+authFields.length];
		int i=0;

		for(int j=0; j<menuFields.length; j++) {
			s[i] = from.get(menuFields[j]);
			i++;
		}
		for(int j=0; j<roleAuthFields.length; j++) {	
			s[i] = roleAuth.get(roleAuthFields[j]);				
			i++;
		}
		for(int j=0; j<userFields.length; j++) {	
			s[i] = user.get(userFields[j]);				
			i++;
		}
		for(int j=0; j<authFields.length; j++) {	
			s[i] = userAuth.get("id").get("authId");				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.asc(from.get("menuId")));

		/* Query Cache 추가 */
		TypedQuery<Object[]> typedQuery = em.createQuery(select).setHint("org.hibernate.cacheable", true);

		List<Object[]> list2 = typedQuery.getResultList();
		List<Menus> menus = new ArrayList<Menus>();

		for(Object[] list : list2) {
			Menus menu = new Menus();

			i=0;
			for(int j=0; j<menuFields.length; j++) {
				ObjectUtils.setProperty(menu, menuFields[j], list[i]);
				i++;
			}
			for(int j=0; j<roleAuthFields.length; j++) {
				ObjectUtils.setProperty(menu, roleAuthFields[j], list[i]);
				i++;
			}
			for(int j=0; j<userFields.length; j++) {
				ObjectUtils.setProperty(menu, userFields[j], list[i]);
				i++;
			}
			for(int j=0; j<authFields.length; j++) {
				ObjectUtils.setProperty(menu, authFields[j], list[i]);
				i++;
			}
			menus.add(menu);
		}
		return menus;
	}


	/**
	 * 메뉴id별 정보를 얻는다
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public MenuTbl getMenuObj(Integer MenuId) throws ServiceException{
		return menuDao.findOne(MenuId);
	}


	/**
	 * 
	 * @param menuTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public List<Menus> findMainMenuForExtJs(MenuTbl menuTbl,Search search) throws ServiceException  {

		String[] menuFields = {"menuId","menuNm", "menuEnNm","depth","nodes","orderNum"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<MenuTbl> root = cq.from(MenuTbl.class);

		Path<Integer> menuId = root.get(MenuTbl_.menuId);
		Path<String> menuNm = root.get(MenuTbl_.menuNm);
		Path<String> menuEnNm = root.get(MenuTbl_.menuEnNm);
		Path<Integer> depth = root.get(MenuTbl_.depth);
		Path<String> nodes = root.get(MenuTbl_.nodes);
		Path<Integer> orderNum = root.get(MenuTbl_.orderNum);

		cq.multiselect(menuId,menuNm,menuEnNm,depth,nodes,orderNum);

		if( menuTbl.getDepth() == 0){
			cq.where(cb.equal(root.<Integer>get("depth"), menuTbl.getDepth()));			
		}else{
			cq.where(cb.and(cb.equal(root.<Integer>get("depth"),1),cb.like(root.<String>get("nodes"),String.valueOf(menuTbl.getMenuId())+'%')));			
		}

		cq.orderBy(cb.asc(menuId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> menuList =  typedQuery.getResultList();

		if(logger.isDebugEnabled())
			logger.debug("menuList : "+menuList.size());

		if(menuList != null){
			List<Menus> menus = new ArrayList<Menus>();

			for(Object[] list : menuList) {
				Menus menus2 = new Menus();

				int i = 0;
				for(int j = 0; j<menuFields.length; j++){
					ObjectUtils.setProperty(menus2, menuFields[j], list[i]);			
					i++;
				}

				menus.add(menus2);
			}
			return menus;
		}else{
			return Collections.EMPTY_LIST;
		}
	}




	/**
	 * 
	 * @param menus
	 * @return
	 * @throws ServiceException 
	 */
	public List<MenuTreeForExtJs> makeExtJsJson(List<Menus> menus) throws ServiceException   {
		List<MenuTreeForExtJs> lists = new ArrayList<MenuTreeForExtJs>();


		if(menus.size() != 0){
			for(Menus info : menus){
				MenuTreeForExtJs store = new MenuTreeForExtJs();

				store.setId(info.getMenuId());
				store.setText(info.getMenuNm());

				if(!info.getMenuNm().equals("CMS")){
					if(info.getDepth().equals(0)){						 

						try {
							List<Menus> list = findSubMenuForExtJs(info);

							if(list.size() == 0){								  
								store.setLeaf(true);
							}else{
								if(logger.isDebugEnabled()){
									logger.debug("info.getNodes : " + info.getNodes());
									logger.debug("list.size : " + list.size());
								}

								store.setLeaf(false);
								List<MenuTreeChildren> childrens = new ArrayList<MenuTreeChildren>();

								for(Menus menus2 : list){
									MenuTreeChildren children = new MenuTreeChildren();

									children.setId(menus2.getMenuId());
									children.setText(menus2.getMenuNm());
									children.setLeaf("false"); 
									childrens.add(children);

									if(logger.isDebugEnabled())
										logger.debug("childrens.size() : " +  childrens.size());
								}
								store.setChildren(childrens);
							}

						} catch (ServiceException e) {
							throw new ServiceException("makeExtJsJson",e);
						}
					}else{
						if(logger.isDebugEnabled())
							logger.debug("income one depth");

						store.setLeaf(true); 
					}
					lists.add(store);
				}
			}
		}
		return lists;
	}


	/**
	 * 
	 * @param menuTbl
	 * @return
	 * @throws ServiceException
	 */
	public List<Menus> findSubMenuForExtJs(Menus menuTbl) throws ServiceException  {

		String[] menuFields = {"menuId","menuNm", "menuEnNm","depth","nodes","orderNum"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<MenuTbl> root = cq.from(MenuTbl.class);

		Path<Integer> menuId = root.get(MenuTbl_.menuId);
		Path<String> menuNm = root.get(MenuTbl_.menuNm);
		Path<String> menuEnNm = root.get(MenuTbl_.menuEnNm);
		Path<Integer> depth = root.get(MenuTbl_.depth);
		Path<String> nodes = root.get(MenuTbl_.nodes);
		Path<Integer> orderNum = root.get(MenuTbl_.orderNum);
		
		
		cq.multiselect(menuId,menuNm,menuEnNm,depth,nodes,orderNum);
		cq.where(cb.and(cb.like(root.<String>get("nodes"), menuTbl.getNodes() + ",%"),cb.equal(root.<Integer>get("depth"), 1))
				);
		cq.orderBy(cb.asc(menuId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> menuList =  typedQuery.getResultList();

		if(menuList != null){
			List<Menus> menus = new ArrayList<Menus>();

			for(Object[] list : menuList) {
				Menus tra = new Menus();

				int i = 0;
				for(int j = 0; j<menuFields.length; j++){
					ObjectUtils.setProperty(tra, menuFields[j], list[i]);			
					i++;
				}

				menus.add(tra);
			}
			return menus;
		}else{
			return Collections.EMPTY_LIST;
		}
	}

}

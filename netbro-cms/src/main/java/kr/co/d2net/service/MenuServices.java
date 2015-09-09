package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.MenuDao;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.MenuTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl_;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


	public Page<MenuTbl> findAllMenu(Specification<MenuTbl> specification, Pageable pageable) {
		return menuDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<MenuTbl> menus) {
		menuDao.save(menus);
	}

	@Modifying
	@Transactional
	public void add(MenuTbl menu) throws ServiceException{
		String message = "";
		try{
		menuDao.save(menu);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}catch (JDBCConnectionException e) {
			throw new ConnectionTimeOutException("000", message, e);
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}


	/**
	 * 메뉴 정보를 조회함.
	 * @param params
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<MenuTbl> findUserMenus(Map<String, Object> params) throws ServiceException{

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();


		String message = "";


		Root<MenuTbl> root1 = cq.from(MenuTbl.class);
		Root<RoleAuthTbl> root2 = cq.from(RoleAuthTbl.class);
		Root<UserTbl> root3 = cq.from(UserTbl.class);

		Join<UserTbl, UserAuthTbl> inst = root3.join("userAuth", JoinType.INNER);

		Path<Integer> menuId = root1.get(MenuTbl_.menuId);
		Path<String> menuNm = root1.get(MenuTbl_.menuNm);
		Path<String> menuEnNm = root1.get(MenuTbl_.menuEnNm);
		Path<String> url = root1.get(MenuTbl_.url);
		Path<String> controlGubun = root2.get(RoleAuthTbl_.controlGubun);
		Path<String> userId = root3.get(UserTbl_.userId);
		Path<Integer> authId = inst.get("id").get("authId");

		String[] selection = {"menuId","menuNm","menuEnNm","url","controlGubun","userId","authId"};

		cq.multiselect(menuId,menuNm,menuEnNm,url,controlGubun,userId,authId);
		cq.where(cb.and(cb.equal(menuId,root2.get("id").get("menuId"))),cb.equal(root2.get("id").get("authId"),authId),cb.notLike(controlGubun, "%L%"),cb.and(cb.greaterThan(root1.<Integer>get("menuId"), 1)),cb.lessThan(root1.<Integer>get("menuId"), 7),cb.equal(root3.get("userId"), params.get("userId")));
		cq.groupBy(menuId,menuNm,menuEnNm,url,controlGubun,userId,authId);
		cq.orderBy(cb.asc(menuId));

		try {
			TypedQuery<Object[]> typedQuery = em.createQuery(cq);
			List<Object[]> results = typedQuery.getResultList();

			List<MenuTbl> menus = new ArrayList<MenuTbl>();

			for(Object[] list : results) {

				MenuTbl menu = new MenuTbl();

				int i = 0;
				for(int j = 0; j<selection.length; j++){
					ObjectUtils.setProperty(menu, selection[j], list[i]);			
					i++;
				}
				menus.add(menu);
			}
			return menus;
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.000",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.000",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}

	
	
	/**
	 * 메뉴테이블에 정보 존재여부 파악
	 * @param params
	 * @return
	 */
	@SuppressWarnings("static-access")
	public long findMenuCount() throws ServiceException{

		String message = "";
		try {
				
			return menuDao.count();
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.000",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.000",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}
}

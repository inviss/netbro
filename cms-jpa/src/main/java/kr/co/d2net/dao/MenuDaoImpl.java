package kr.co.d2net.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.vo.MenuTreeForExtJs;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.ServiceException;

import org.springframework.stereotype.Repository;

@Repository("menuDao")
public class MenuDaoImpl implements MenuDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public List<MenuTbl> findUserMenus(Map<String, Object> params)
			throws ServiceException {
		List<MenuTbl> menuTbls = null;

		try {
			SpecificationResult<MenuTbl> result = repository.find(MenuTbl.class);

			menuTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findUserMenus Error", e);
		}

		return menuTbls;
	}

	@Override
	public Long findMenuCount() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuTbl> findMainMenuForExtJs(MenuTbl menuTbl, Search search)
			throws ServiceException {
		List<MenuTbl> menuTbls = null;

		try {
			SpecificationResult<MenuTbl> result = repository.find(MenuTbl.class);

			menuTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findMainMenuForExtJs Error", e);
		}

		return menuTbls;
	}

	@Override
	public List<MenuTbl> findSubMenuForExtJs(Menus menuTbl)
			throws ServiceException {
		List<MenuTbl> menuTbls = null;

		try {
			SpecificationResult<MenuTbl> result = repository.find(MenuTbl.class);

			menuTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findSubMenuForExtJs Error", e);
		}

		return menuTbls;
	}

	@Override
	public List<MenuTreeForExtJs> makeExtJsJson(List<Menus> menus)
			throws ServiceException {
		List<MenuTbl> menuTbls = null;
		List<MenuTreeForExtJs> extJs = null;

		try {
			SpecificationResult<MenuTbl> result = repository.find(MenuTbl.class);

			menuTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "makeExtJsJson Error", e);
		}

		return extJs;
	}

	

}

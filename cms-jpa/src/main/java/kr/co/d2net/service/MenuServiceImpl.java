package kr.co.d2net.service;

import java.util.List;
import java.util.Map;

import kr.co.d2net.dao.MenuDao;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.vo.MenuTreeForExtJs;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("menuService")
@Transactional(readOnly=true)
public class MenuServiceImpl implements MenuServices {

	@Autowired
	private MenuDao menuDao;

	@Override
	public List<MenuTbl> findUserMenus(Map<String, Object> params)
			throws ServiceException {
		try {
			menuDao.findUserMenus(params);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Long findMenuCount() throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<Menus> findMainMenuForExtJs(MenuTbl menuTbl, Search search)
			throws ServiceException {
		try {
			menuDao.findMainMenuForExtJs(menuTbl,search);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<Menus> findSubMenuForExtJs(Menus menuTbl)
			throws ServiceException {
		try {
			menuDao.findSubMenuForExtJs(menuTbl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<MenuTreeForExtJs> makeExtJsJson(List<Menus> menus)
			throws ServiceException {
		try {
			menuDao.makeExtJsJson(menus);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


}

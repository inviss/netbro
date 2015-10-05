package kr.co.d2net.dao;

import java.util.List;
import java.util.Map;

import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.vo.MenuTreeForExtJs;
import kr.co.d2net.dto.vo.Menus;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface MenuDao {
	public List<MenuTbl> findUserMenus(Map<String, Object> params) throws ServiceException;
	public Long findMenuCount() throws ServiceException;
	public List<MenuTbl> findMainMenuForExtJs(MenuTbl menuTbl,Search search) throws ServiceException;
	public List<MenuTbl> findSubMenuForExtJs(Menus menuTbl) throws ServiceException;
	public List<MenuTreeForExtJs> makeExtJsJson(List<Menus> menus) throws ServiceException;	
}

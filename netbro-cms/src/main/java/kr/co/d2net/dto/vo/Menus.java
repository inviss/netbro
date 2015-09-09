package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.criteria.Path;

import kr.co.d2net.dto.MenuTbl_;

public class Menus implements Serializable{

	public Menus() {}

	private static final long serialVersionUID = 1787599149394060846L;

	private Integer  menuId;	//사용자ID
	private String menuNm;	//사용자명
	private String menuEnNm;	//비밀번호
	private String url;	//비밀번호
	
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getMenuEnNm() {
		return menuEnNm;
	}
	public void setMenuEnNm(String menuEnNm) {
		this.menuEnNm = menuEnNm;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


}

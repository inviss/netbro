package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class RoleAuth implements Serializable{

	public RoleAuth() {}

	private static final long serialVersionUID = 1787599149394060846L;

	private Integer  menuId;	//사용자ID
	private Integer authId;	//사용자명
	private String controlGubun;	//사용자명
	
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getAuthId() {
		return authId;
	}
	public void setAuthId(Integer authId) {
		this.authId = authId;
	}
	public String getControlGubun() {
		return controlGubun;
	}
	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}
	
}

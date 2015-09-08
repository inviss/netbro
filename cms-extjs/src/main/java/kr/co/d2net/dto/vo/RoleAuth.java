package kr.co.d2net.dto.vo;


public class RoleAuth  {

	private Integer  menuId;	//메뉴ID
	private Integer authId;	//권한ID
	private String controlGubun;	//권한구분
	
	
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

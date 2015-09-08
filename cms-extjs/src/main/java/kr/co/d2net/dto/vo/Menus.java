package kr.co.d2net.dto.vo;


public class Menus {

	private Integer  menuId;	//메뉴ID
	private String menuNm;	//메뉴명
	private String menuEnNm;	//메뉴명(영어)
	private String url;	//url
	private Integer preParent;//직전노드
	private Integer depth;//깊이
	private String nodes;	//최상위카테고리 -> 현재카테고리까지 순서
	private Integer orderNum;//순번
	
	
	public Integer getPreParent() {
		return preParent;
	}
	public void setPreParent(Integer preParent) {
		this.preParent = preParent;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
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

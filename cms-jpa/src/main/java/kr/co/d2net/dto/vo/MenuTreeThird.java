package kr.co.d2net.dto.vo;

import java.util.List;

public class MenuTreeThird {
	
	private String expanded;
	private String text;
	private List<MenuTreeForExtJs> children;
	public String getExpanded() {
		return expanded;
	}
	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}
	public List<MenuTreeForExtJs> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeForExtJs> children) {
		this.children = children;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

}

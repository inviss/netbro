package kr.co.d2net.dto.vo;

import java.util.List;

public class MenuTreeForExtJs {
	
	private String text;
	private Integer id;
	private boolean leaf;
	private String cls;
	private boolean expanded;
	private boolean read;
	private boolean write;
	private boolean limit;
	private List<MenuTreeChildren> children;
	
	
	
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean isWrite() {
		return write;
	}
	public void setWrite(boolean write) {
		this.write = write;
	}
	public boolean isLimit() {
		return limit;
	}
	public void setLimit(boolean limit) {
		this.limit = limit;
	}
	public List<MenuTreeChildren> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeChildren> childrens) {
		this.children = childrens;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
 
	
}

package kr.co.d2net.dto.vo;


public class CategoryTreeForExtJs{
	
	private String text; //카테고리노드명
	private Integer id;	//카테고리id
	private boolean leaf;	//하위노드여브
	private String cls;	//이미지
	
	
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
	
	
	
}

package kr.co.d2net.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="category")
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {
	
	@JsonProperty("category_id")
	@XmlAttribute(name="category_id")
	private Integer categoryId;
	@JsonProperty("category_nm")
	@XmlAttribute(name="category_nm")
	private String categoryNm;
	@XmlAttribute(name="lft")
	private Integer lft;
	@XmlAttribute(name="rgt")
	private Integer rgt;
	@JsonProperty("final_yn")
	@XmlAttribute(name="final_yn")
	private String finalYn;
	@XmlAttribute(name="depth")
	private Integer depth;
	@JsonProperty("pre_parent")
	@XmlAttribute(name="pre_parent")
	private Integer preParent;
	@JsonProperty("nodes")
	@XmlAttribute(name="nodes")
	private String nodes;
	@JsonProperty("order_num")
	@XmlAttribute(name="order_num")
	private Integer orderNum;
	@JsonProperty("group_id")
	@XmlAttribute(name="group_id")
	private Integer groupId;
	
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryNm() {
		return categoryNm;
	}
	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}
	public Integer getLft() {
		return lft;
	}
	public void setLft(Integer lft) {
		this.lft = lft;
	}
	public Integer getRgt() {
		return rgt;
	}
	public void setRgt(Integer rgt) {
		this.rgt = rgt;
	}
	public String getFinalYn() {
		return finalYn;
	}
	public void setFinalYn(String finalYn) {
		this.finalYn = finalYn;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Integer getPreParent() {
		return preParent;
	}
	public void setPreParent(Integer preParent) {
		this.preParent = preParent;
	}
	
	
}

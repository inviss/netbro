package kr.co.d2net.dto.vo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Category {
	
	private Integer categoryId;			//카테고리ID
	private String categoryNm;			//카테고리명
	private Integer preParent;			//직전노드
	private Integer depth;				//깊이
	private String nodes;				//최상위카테고리 -> 현재카테고리까지 순서
	private Integer orderNum;			//순번
	private String type;				//SUB - 하위노드 생성 , NEXT - 다음노드 생성
	private String finalYn;				//하위노드가 있는지 여부 존재시 Y 아닐시 N
	private String direction;			//위치 변경 방향 up : depth +1, down : depth -1;
	private String node;
	private String newCategoryNm;        //신규생성 카테고리명

	//2015.02.11 카테고리 순서 변경 beans 추가
	private String categoryIds;
	private String parentsIds;
	private String depths;
	private Integer parents;
	
	
	
	public String getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getParentsIds() {
		return parentsIds;
	}
	public void setParentsIds(String parentsIds) {
		this.parentsIds = parentsIds;
	}
	public String getDepths() {
		return depths;
	}
	public void setDepths(String depths) {
		this.depths = depths;
	}
	public Integer getParents() {
		return parents;
	}
	public void setParents(Integer parents) {
		this.parents = parents;
	}
	public String getNewCategoryNm() {
		return newCategoryNm;
	}
	public void setNewCategoryNm(String newCategoryNm) {
		this.newCategoryNm = newCategoryNm;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFinalYn() {
		return finalYn;
	}
	public void setFinalYn(String finalYn) {
		this.finalYn = finalYn;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	
}

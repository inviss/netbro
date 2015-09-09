package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable{
	
	private Integer categoryId;	//카테고리ID
	private String categoryNm;	//카테고리명
	private Integer preParent;//직전노드
	private Integer depth;//깊이
	private String nodes;	//최상위카테고리 -> 현재카테고리까지 순서
	private Integer orderNum;//순번
	private String type;//SUB - 하위노드 생성 , NEXT - 다음노드 생성
	private String finalYn;//하위노드가 있는지 여부 존재시 Y 아닐시 N
	private String direction;//위치 변경 방향 up : depth +1, down : depth -1;
	
	
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getFinalYn() {
		return finalYn;
	}
	public void setFinalYn(String finalYn) {
		this.finalYn = finalYn;
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
	
	
}

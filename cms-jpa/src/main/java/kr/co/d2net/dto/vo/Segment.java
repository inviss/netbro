package kr.co.d2net.dto.vo;

import java.util.Date;

public class Segment {
	
	Integer categoryId; //카테고리id
	Integer episodeId; //에피소드id
	Integer segmentId; //세그먼트id
	String useYn; //사용여부
	String regrId; //등록자id
	Date regDt; //등록일
	String modrId; //수정자id
	Date modDt; // 수정일
	String segmentNm; //세그먼트 명 
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getEpisodeId() {
		return episodeId;
	}
	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}
	public Integer getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Integer segmentId) {
		this.segmentId = segmentId;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public String getSegmentNm() {
		return segmentNm;
	}
	public void setSegmentNm(String segmentNm) {
		this.segmentNm = segmentNm;
	}
	
	
	
}

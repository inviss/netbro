package kr.co.d2net.dto.vo;

import java.util.Date;

public class BusiPartnerCategory {
	
	private Integer categoryId;	//프로그램코드
	private Integer busiPartnerId;	//카테고리ID
	private String proFlNm;	//카테고리명
	private String ctTyp;	//콘텐츠타입
	private String recYn;	//녹화여부
	private Date bgnTime;	//시작시간
	private Date endTime;	//종료시간
	private String regId;	//등록자ID
	private Date regDt;	//등록일자
	private String modId;	//수정자ID
	private Date modDt;	//수정일자
	
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCtTyp() {
		return ctTyp;
	}
	public void setCtTyp(String ctTyp) {
		this.ctTyp = ctTyp;
	}
	public String getRecYn() {
		return recYn;
	}
	public void setRecYn(String recYn) {
		this.recYn = recYn;
	}
	public Date getBgnTime() {
		return bgnTime;
	}
	public void setBgnTime(Date bgnTime) {
		this.bgnTime = bgnTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public Integer getBusiPartnerId() {
		return busiPartnerId;
	}
	public void setBusiPartnerId(Integer busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}
	public String getProFlNm() {
		return proFlNm;
	}
	public void setProFlNm(String proFlNm) {
		this.proFlNm = proFlNm;
	}

	
	
}

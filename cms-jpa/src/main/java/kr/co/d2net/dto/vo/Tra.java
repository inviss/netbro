package kr.co.d2net.dto.vo;

import java.util.Date;

public class Tra{

	private String  deviceId;	//장비번호
	private String ctNm;	//콘텐츠명
	private Long seq;	//순번
	private Date reqDt;		//등록일
	private Date regDt;	//
	private Date modDt; //수정일시
	private Integer errorCount;	//에러횟수
	private String jobStatus;	//작업상태
	private String modrId;	//수정자ID
	private Integer prgrs;		//진행률
	private Long ctId;	//컨텐츠ID	
	private String categoryNm;	//카테고리명	
	private String proFlnm;	//프로파일명	

	public String getProFlnm() {
		return proFlnm;
	}
	public void setProFlnm(String proFlnm) {
		this.proFlnm = proFlnm;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	public Date getReqDt() {
		return reqDt;
	}
	public void setReqDt(Date reqDt) {
		this.reqDt = reqDt;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public Integer getPrgrs() {
		return prgrs;
	}
	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public String getCategoryNm() {
		return categoryNm;
	}
	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}


}

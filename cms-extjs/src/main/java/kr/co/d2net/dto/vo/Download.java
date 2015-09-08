package kr.co.d2net.dto.vo;

import java.util.Date;

public class Download {
	 
	private Long seq;	//seq
	private Long ctiId;	//컨텐츠인스트ID
	private String workStatCd;	//작업코드
	private String errorCd;	//에러코드
	private String reason;	//설명
	private Date regDt;	//등록일
	private String approveId;	//등록자
	private Integer prgrs;	//진행률
	private Date dnStrDt;	//다운로드요청시간
	private Date dnEndDt;	//다운로드완료시간
	private String downloadPath;	//다운로드 경로
	private String reqId;	//다운로드 경로
	private String ctNm;	//제목
	private String categoryNm;	//카테고리명
	private String episodeNm;	//에피소드명
	private String cont;	//사유
	private String ctLeng;	//영상길이
	private String userNm;	//요청자 명
	private String sclNm;	//작업상태
	
	
	
	public String getSclNm() {
		return sclNm;
	}

	public void setSclNm(String sclNm) {
		this.sclNm = sclNm;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public Date getDnStrDt() {
		return dnStrDt;
	}

	public void setDnStrDt(Date dnStrDt) {
		this.dnStrDt = dnStrDt;
	}

	public Date getDnEndDt() {
		return dnEndDt;
	}

	public void setDnEndDt(Date dnEndDt) {
		this.dnEndDt = dnEndDt;
	}

	public Integer getPrgrs() {
		return prgrs;
	}

	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}

	public String getEpisodeNm() {
		return episodeNm;
	}

	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public String getWorkStatCd() {
		return workStatCd;
	}

	public void setWorkStatCd(String workStatCd) {
		this.workStatCd = workStatCd;
	}

	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}
	
	
}

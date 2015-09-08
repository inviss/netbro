package kr.co.d2net.dto.vo;

import java.util.Date;

public class Archive {
	 
	private Long seq;	//순번
	private Long ctiId;	//컨텐츠인스트ID
	private String workStatCd;	//작업코드
	private String errorCd;	//에러코드
	private String cont;	//설명
	private Date regDt;	//등록일
	private String approveId;	//등록자
	private Integer prgrs;	//진행률
	private String archivePath;	//아카이브 경로
	
	private String ctNm;	//영상제목
	private String categoryNm;	//카테고리명
	private String episodeNm;	//에피소드명
	private String ctLeng;	//영상길이
	
	private String sclNm;	//상태명
	private String userNm;	//유저명
	
	
	
	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getSclNm() {
		return sclNm;
	}

	public void setSclNm(String sclNm) {
		this.sclNm = sclNm;
	}

	public String getArchivePath() {
		return archivePath;
	}

	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
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

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
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

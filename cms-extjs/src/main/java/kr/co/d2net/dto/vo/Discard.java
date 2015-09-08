package kr.co.d2net.dto.vo;

import java.util.Date;

public class Discard {

	public Discard() {}

	private Long ctId;	//영상id
	private Long disuseNo;	//폐기번호
	private Date disuseDd;	//폐기일
	private String disuseRsl;	//폐기사유
	private String disuseClf;	//폐기코드
	private Date regDt;	//등록일자
	private String regrId;	//등록자ID
	private String cancelCont;	//취소사유
	private Date cancelDt;	//취소일자
	private String ctNm;	//영상명 
	private Date startDt;	//검색 시작일자
	private Date endDt;	//검색 종료일자
	private Integer pageNo; // 페이지
	private String ctIds; // 영상ID묶음 ,로 구분
	private String disuseNos; // 폐기ID 묶음
	
	
	
	public String getDisuseNos() {
		return disuseNos;
	}
	public void setDisuseNos(String disuseNos) {
		this.disuseNos = disuseNos;
	}
	public String getCtIds() {
		return ctIds;
	}
	public void setCtIds(String ctIds) {
		this.ctIds = ctIds;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public Long getDisuseNo() {
		return disuseNo;
	}
	public void setDisuseNo(Long disuseNo) {
		this.disuseNo = disuseNo;
	}
	public Date getDisuseDd() {
		return disuseDd;
	}
	public void setDisuseDd(Date disuseDd) {
		this.disuseDd = disuseDd;
	}
	public String getDisuseRsl() {
		return disuseRsl;
	}
	public void setDisuseRsl(String disuseRsl) {
		this.disuseRsl = disuseRsl;
	}
	public String getDisuseClf() {
		return disuseClf;
	}
	public void setDisuseClf(String disuseClf) {
		this.disuseClf = disuseClf;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public String getCancelCont() {
		return cancelCont;
	}
	public void setCancelCont(String cancelCont) {
		this.cancelCont = cancelCont;
	}
	public Date getCancelDt() {
		return cancelDt;
	}
	public void setCancelDt(Date cancelDt) {
		this.cancelDt = cancelDt;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	

}

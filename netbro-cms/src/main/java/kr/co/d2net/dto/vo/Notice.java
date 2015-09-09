package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class Notice implements Serializable{
	
	private Long noticeId;	//공지사항id
	private String title;	//공지사항 제목
	private Date startDd;	//팝업 시작일
	private Date endDd;	//팝업 종료일
	private String cont;	//공지 내용
	private Date regDt;	//등록일
	private String regId;	//등록자
	private Date modDt;	//수정일W
	private String modId;	//수정자
	private String popUpYn;	//팝업 여부
	private String keyword;	//키워드
	private String searchFiled;	//검색필드명
	private Integer pageNo;	//페이지 번호
	private String userNm;	//작성자 명
	
	
	
	
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearchFiled() {
		return searchFiled;
	}
	public void setSearchFiled(String searchFiled) {
		this.searchFiled = searchFiled;
	}
	public Long getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartDd() {
		return startDd;
	}
	public void setStartDd(Date startDd) {
		this.startDd = startDd;
	}
	public Date getEndDd() {
		return endDd;
	}
	public void setEndDd(Date endDd) {
		this.endDd = endDd;
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
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	public String getPopUpYn() {
		return popUpYn;
	}
	public void setPopUpYn(String popUpYn) {
		this.popUpYn = popUpYn;
	}
	
	
}

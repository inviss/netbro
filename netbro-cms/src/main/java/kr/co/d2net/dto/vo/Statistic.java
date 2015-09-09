package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class Statistic implements Serializable{
	
	public Statistic() {}
	
	private static final long serialVersionUID = 1787599149394060846L;

	private Integer categoryId;	//카테고리ID
	private String regDd;	//등록월
	private Long regist;	//등록건
	private Long beforeArrange;	//정리전건
	private Long completeArrange;	//정리완료건
	private Long discard;	//폐기건
	private Integer depth;	//depth
	private Integer groupId;	//컨텐츠ID
	private String nodes; //노드별 순번
	private String categoryNm;	//카테고리명
	private Date startDD;	//검색 시작일
	private Date endDD;	//검색 종료일
	private String gubun;	//검색구분 001- 등록건, 002-정리전, 003-정리완료건, 004-폐기건
	private String userNm;	//유저명
	private String ctNm;	//컨텐츠명
	private String episodeNm;	//에피소드명
	private String ctLeng;	//영상길이
	private String brdDd;	//방송일
	private String vdQlty;	//화질
	private Long error;	//에러
	private String yearList;	//연도리스트
	private Date regDt;	//등록일
	private String vdHresol;	//비디오 수평해상도
	private String vdVresol;	//비디오 수직해상도
	private Long count; //건수조회

				   
				   
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getVdHresol() {
		return vdHresol;
	}
	public void setVdHresol(String vdHresol) {
		this.vdHresol = vdHresol;
	}
	public String getVdVresol() {
		return vdVresol;
	}
	public void setVdVresol(String vdVresol) {
		this.vdVresol = vdVresol;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getYearList() {
		return yearList;
	}
	public void setYearList(String yearList) {
		this.yearList = yearList;
	}
	
	public Long getError() {
		return error;
	}
	public void setError(Long error) {
		this.error = error;
	}
	public String getBrdDd() {
		return brdDd;
	}
	public void setBrdDd(String brdDd) {
		this.brdDd = brdDd;
	}
	public String getVdQlty() {
		return vdQlty;
	}
	public void setVdQlty(String vdQlty) {
		this.vdQlty = vdQlty;
	}
	public String getEpisodeNm() {
		return episodeNm;
	}
	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}
	public String getCtLeng() {
		return ctLeng;
	}
	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getRegDd() {
		return regDd;
	}
	public void setRegDd(String regDd) {
		this.regDd = regDd;
	}
	public Long getRegist() {
		return regist;
	}
	public void setRegist(Long regist) {
		this.regist = regist;
	}
	public Long getBeforeArrange() {
		return beforeArrange;
	}
	public void setBeforeArrange(Long beforeArrange) {
		this.beforeArrange = beforeArrange;
	}
	public Long getCompleteArrange() {
		return completeArrange;
	}
	public void setCompleteArrange(Long completeArrange) {
		this.completeArrange = completeArrange;
	}
	public Long getDiscard() {
		return discard;
	}
	public void setDiscard(Long discard) {
		this.discard = discard;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	public String getCategoryNm() {
		return categoryNm;
	}
	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}
	public Date getStartDD() {
		return startDD;
	}
	public void setStartDD(Date startDD) {
		this.startDD = startDD;
	}
	public Date getEndDD() {
		return endDD;
	}
	public void setEndDD(Date endDD) {
		this.endDD = endDD;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	
	
	
}

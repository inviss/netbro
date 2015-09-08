package kr.co.d2net.dto.vo;

import java.util.Date;

public class Code {
 
	private String clfCd;	//구분코드
	private String sclCd;	//구분상세코드
	private String clfNm;	//구분명
	private String sclNm;	//구분상세코드명
	private String codeCont;	//코드설명
	private String rmk1; // 비고1
	private String rmk2;	//	비고2
	private String modrId;	//	수정자
	private String regrId;	//	등록자
	private String clfGubun;	//S : 시스템 사용 코드 U : 유저사용코드
	private String useYn;	//	사용여부
	private String gubun;	//	조회구분 코드
	private String keyword;	//	검색단어
	private String createWay;	//생성방식
	private Date regDt;		//등록일
	private Date modDt;		//수정일
	
	
	public String getClfCd() {
		return clfCd;
	}
	public void setClfCd(String clfCd) {
		this.clfCd = clfCd;
	}
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getClfNm() {
		return clfNm;
	}
	public void setClfNm(String clfNm) {
		this.clfNm = clfNm;
	}
	public String getSclNm() {
		return sclNm;
	}
	public void setSclNm(String sclNm) {
		this.sclNm = sclNm;
	}
	public String getCodeCont() {
		return codeCont;
	}
	public void setCodeCont(String codeCont) {
		this.codeCont = codeCont;
	}
	public String getRmk1() {
		return rmk1;
	}
	public void setRmk1(String rmk1) {
		this.rmk1 = rmk1;
	}
	public String getRmk2() {
		return rmk2;
	}
	public void setRmk2(String rmk2) {
		this.rmk2 = rmk2;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public String getClfGubun() {
		return clfGubun;
	}
	public void setClfGubun(String clfGubun) {
		this.clfGubun = clfGubun;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCreateWay() {
		return createWay;
	}
	public void setCreateWay(String createWay) {
		this.createWay = createWay;
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

}

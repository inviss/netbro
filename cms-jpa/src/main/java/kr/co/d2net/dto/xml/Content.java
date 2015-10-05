package kr.co.d2net.dto.xml;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.co.d2net.adapter.xml.DateAdapter;
import kr.co.d2net.adapter.xml.DatetimeAdapter;
import kr.co.d2net.adapter.xml.UrlEncodeAdapter;
import kr.co.d2net.dto.UseEnum;

@XmlRootElement(name="contents")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {
	@XmlElement(name="ct_id")
	private Long ctId;	//컨텐츠ID
	
	@XmlElement(name="ct_typ")
	private String ctTyp;	//콘텐츠유형코드

	@XmlElement(name="ct_cla")
	private String ctCla;	//콘텐츠구분코드

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="ct_nm")
	private String ctNm;	//컨텐츠명

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="cont")
	private String cont;	//내용

	@XmlElement(name="vd_qlty")
	private String vdQlty;	//화질코드

	@XmlElement(name="asp_rto_cd")
	private String aspRtoCd;	//종횡비코드

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="key_words")
	private String keyWords;	//키워드

	@XmlElement(name="kfrm_path")
	private String kfrmPath;	//키프레임경로

	@XmlElement(name="rpimg_kfrm_seq")
	private Long rpimgKfrmSeq;	//대표화면키프레임순번

	@XmlElement(name="tot_kfrm_nums")
	private Integer totKfrmNums;	//총키프레임수

	@Enumerated(EnumType.STRING)
	@XmlElement(name="use_yn")
	//private String useYn;	//사용여부
	private UseEnum useEnum;

	@XmlElement(name="reg_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	private Date regDt;	//등록일시

	@XmlElement(name="regrid")
	private String regrId;	//등록자ID

	@XmlElement(name="modrid")
	private String modrId;	//수정자ID

	@XmlElement(name="mod_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	private Date modDt;	//수정일시

	@XmlElement(name="del_dd")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date delDd;	//삭제일자

	@XmlElement(name="data_stat_cd")
	private String dataStatCd;	//자료상태코드

	@XmlElement(name="ct_leng")
	private String ctLeng;	//콘텐츠길이

	@XmlElement(name="ct_seq")
	private Long ctSeq;	//콘텐츠일련번호

	@XmlElement(name="duration")
	private Integer duration;	//DURATION

	@XmlElement(name="brd_dd")
	private Date brdDd;	//방송일자

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="spc_info")
	private String spcInfo;	//특이사항

	@XmlElement(name="lock_stat_cd")
	private String lockStatcd;	//Lock상태

	@XmlElement(name="prod_route")
	private String prodRoute;	//자료상태코드
	
	@XmlElement(name="category_id")
	private Integer categoryId;	//카테고리id
	
	@XmlElement(name="episode_id")
	private Integer episodeId;	//에피소드 id
	
	@XmlElement(name="segment_id")
	private Integer segmentId;	//세그먼트id
	
	@XmlElement(name="arrange_dt")
	private Date arrangeDt;	//세그먼트id
	
	@XmlElement(name="rist_clf_cd")
	private String ristClfCd;	// 사용제한 등급

	
	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public String getCtTyp() {
		return ctTyp;
	}

	public void setCtTyp(String ctTyp) {
		this.ctTyp = ctTyp;
	}

	public String getCtCla() {
		return ctCla;
	}

	public void setCtCla(String ctCla) {
		this.ctCla = ctCla;
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getVdQlty() {
		return vdQlty;
	}

	public void setVdQlty(String vdQlty) {
		this.vdQlty = vdQlty;
	}

	public String getAspRtoCd() {
		return aspRtoCd;
	}

	public void setAspRtoCd(String aspRtoCd) {
		this.aspRtoCd = aspRtoCd;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getKfrmPath() {
		return kfrmPath;
	}

	public void setKfrmPath(String kfrmPath) {
		this.kfrmPath = kfrmPath;
	}

	public Long getRpimgKfrmSeq() {
		return rpimgKfrmSeq;
	}

	public void setRpimgKfrmSeq(Long rpimgKfrmSeq) {
		this.rpimgKfrmSeq = rpimgKfrmSeq;
	}

	public Integer getTotKfrmNums() {
		return totKfrmNums;
	}

	public void setTotKfrmNums(Integer totKfrmNums) {
		this.totKfrmNums = totKfrmNums;
	}

	public UseEnum getUseEnum() {
		return useEnum;
	}

	public void setUseEnum(UseEnum useEnum) {
		this.useEnum = useEnum;
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

	public Date getDelDd() {
		return delDd;
	}

	public void setDelDd(Date delDd) {
		this.delDd = delDd;
	}

	public String getDataStatCd() {
		return dataStatCd;
	}

	public void setDataStatCd(String dataStatCd) {
		this.dataStatCd = dataStatCd;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}

	public Long getCtSeq() {
		return ctSeq;
	}

	public void setCtSeq(Long ctSeq) {
		this.ctSeq = ctSeq;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getBrdDd() {
		return brdDd;
	}

	public void setBrdDd(Date brdDd) {
		this.brdDd = brdDd;
	}

	public String getSpcInfo() {
		return spcInfo;
	}

	public void setSpcInfo(String spcInfo) {
		this.spcInfo = spcInfo;
	}

	public String getLockStatcd() {
		return lockStatcd;
	}

	public void setLockStatcd(String lockStatcd) {
		this.lockStatcd = lockStatcd;
	}

	public String getProdRoute() {
		return prodRoute;
	}

	public void setProdRoute(String prodRoute) {
		this.prodRoute = prodRoute;
	}

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

	public Date getArrangeDt() {
		return arrangeDt;
	}

	public void setArrangeDt(Date arrangeDt) {
		this.arrangeDt = arrangeDt;
	}

	public String getRistClfCd() {
		return ristClfCd;
	}

	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}
	
}

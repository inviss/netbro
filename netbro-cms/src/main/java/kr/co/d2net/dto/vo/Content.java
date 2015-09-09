package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class Content implements Serializable{
	
	public Content() {}
	
	public Content(Long ctId, String ctNm, String cont) {
		this.ctId = ctId;
		this.ctNm = ctNm;
		this.cont = cont;
	}
	
	public Content(Long ctId, String ctNm, String ctCla, String cont) {
		this.ctId = ctId;
		this.ctNm = ctNm;
		this.ctCla = ctCla;
		this.cont = cont;
	}

	private static final long serialVersionUID = 1787599149394060846L;

	private Long ctId;	//컨텐츠ID
	private String ctTyp;	//콘텐츠유형코드
	private String ctCla;	//콘텐츠구분코드
	private String ctNm;	//컨텐츠명
	private String cont;	//내용
	private String vdQlty;	//화질코드
	private String aspRtoCd;	//종횡비코드
	private String keyWords;	//키워드
	private String kfrmPath;	//키프레임경로
	private Long rpimgKfrmSeq;	//대표화면키프레임순번
	private Integer totKfrmNums;	//총키프레임수
	private String useYn;	//사용여부
	private Date regDt;	//등록일시
	private String regrId;	//등록자ID
	private String modrId;	//수정자ID
	private Date modDt;	//수정일시
	private Date delDd;	//삭제일자
	private String dataStatCd;	//자료상태코드
	private String ctLeng;	//콘텐츠길이
	private Long ctSeq;	//콘텐츠일련번호
	private Long duration;	//DURATION
	private Date brdDd;	//방송일자
	private String spcInfo;	//특이사항
	private Integer lockStatcd;	//Lock상태
	private String prodRoute;	//자료상태코드
	private Integer categoryId;	//자료상태코드
	private Integer episodeId;	//자료상태코드
	private Integer segmentId;	//자료상태코드
	private String categoryNm;
	private String episodeNm;
	private String segmentNm;
	private Integer vdHresol;
	private Integer vdVresol;
	private Integer lft;
	private Integer rgt;
	
	
	// ContentsInstTbl
	private Long ctiId;
	private String flPath;
	private String wrkFileNm;
	
	
	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public String getWrkFileNm() {
		return wrkFileNm;
	}


	public void setWrkFileNm(String wrkFileNm) {
		this.wrkFileNm = wrkFileNm;
	}


	public Integer getLft() {
		return lft;
	}


	public void setLft(Integer lft) {
		this.lft = lft;
	}


	public Integer getRgt() {
		return rgt;
	}


	public void setRgt(Integer rgt) {
		this.rgt = rgt;
	}


	public String getEpisodeNm() {
		return episodeNm;
	}


	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}


	public String getSegmentNm() {
		return segmentNm;
	}


	public void setSegmentNm(String segmentNm) {
		this.segmentNm = segmentNm;
	}


	public String getCategoryNm() {
		return categoryNm;
	}


	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}


	public Integer getVdHresol() {
		return vdHresol;
	}


	public void setVdHresol(Integer vdHresol) {
		this.vdHresol = vdHresol;
	}


	public Integer getVdVresol() {
		return vdVresol;
	}


	public void setVdVresol(Integer vdVresol) {
		this.vdVresol = vdVresol;
	}


	public String getFlPath() {
		return flPath;
	}


	public void setFlPath(String flPath) {
		this.flPath = flPath;
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


//	public String getKfrmPxCd() {
//		return kfrmPxCd;
//	}
//
//
//	public void setKfrmPxCd(String kfrmPxCd) {
//		this.kfrmPxCd = kfrmPxCd;
//	}


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


	public String getUseYn() {
		return useYn;
	}


	public void setUseYn(String useYn) {
		this.useYn = useYn;
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


	public Long getDuration() {
		return duration;
	}


	public void setDuration(Long duration) {
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
	
	public String getProdRoute() {
		return prodRoute;
	}
	public void setProdRoute(String prodRoute) {
		this.prodRoute = prodRoute;
	}

	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	
	public void setLockStatcd(Integer lockStatcd) {
		this.lockStatcd = lockStatcd;
	}
	public Integer getLockStatcd() {
		return lockStatcd;
	}
	
}

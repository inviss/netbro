package kr.co.d2net.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.co.d2net.xml.adapter.DateAdapter;
import kr.co.d2net.xml.adapter.DatetimeAdapter;
import kr.co.d2net.xml.adapter.UrlEncodeAdapter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement(name="contents")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="CONTENTS_TBL")
public class ContentsTbl extends BaseObject{

	@XmlElement(name="ct_id")
	@Id
	@TableGenerator(name = "CT_ID_SEQ", table = "ID_GEN_TBL", 
			pkColumnName = "ENTITY_NAME", pkColumnValue = "CT_ID_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CT_ID_SEQ")
	@BusinessKey	
	@Column(name="CT_ID", length=16)
	private Long ctId;	//컨텐츠ID
	
	@XmlElement(name="ct_typ")
	@Column(name="CT_TYP", length=5, nullable=true)
	private String ctTyp;	//콘텐츠유형코드

	@XmlElement(name="ct_cla")
	@Column(name="CT_CLA", length=10, nullable=true)
	private String ctCla;	//콘텐츠구분코드

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="ct_nm")
	@Column(name="CT_NM", length=300, nullable=true)
	private String ctNm;	//컨텐츠명

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="cont")
	@Column(name="CONT", length=4000, nullable=true)
	private String cont;	//내용

	@XmlElement(name="vd_qlty")
	@Column(name="VD_QLTY", length=5, nullable=true)
	private String vdQlty;	//화질코드

	@XmlElement(name="asp_rto_cd")
	@Column(name="ASP_RTO_CD", length=5, nullable=true)
	private String aspRtoCd;	//종횡비코드

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="key_words")
	@Column(name="KEY_WORDS", length=1000, nullable=true)
	private String keyWords;	//키워드

	@XmlElement(name="kfrm_path")
	@Column(name="KFRM_PATH", length=256, nullable=true)
	private String kfrmPath;	//키프레임경로

	@XmlElement(name="rpimg_kfrm_seq")
	@Column(name="RPIMG_KFRM_SEQ", nullable=true)
	private Long rpimgKfrmSeq;	//대표화면키프레임순번

	@XmlElement(name="tot_kfrm_nums")
	@Column(name="TOT_KFRM_NUMS", nullable=true)
	private Integer totKfrmNums;	//총키프레임수

	@XmlElement(name="use_yn")
	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

	@XmlElement(name="reg_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@XmlElement(name="regrid")
	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@XmlElement(name="modrid")
	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@XmlElement(name="mod_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@XmlElement(name="del_dd")
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	@Column(name="DEL_DD", nullable=true)
	private Date delDd;	//삭제일자

	@XmlElement(name="data_stat_cd")
	@Column(name="DATA_STAT_CD", length=5, nullable=true)
	private String dataStatCd;	//자료상태코드

	@XmlElement(name="ct_leng")
	@Column(name="CT_LENG", length=11, nullable=true)
	private String ctLeng;	//콘텐츠길이

	@XmlElement(name="ct_seq")
	@Column(name="CT_SEQ", nullable=true)
	private Long ctSeq;	//콘텐츠일련번호

	@XmlElement(name="duration")
	@Column(name="DURATION", nullable=true)
	private Integer duration;	//DURATION

	@XmlElement(name="brd_dd")
	@Temporal(TemporalType.DATE)
	@Column(name="BRD_DD", nullable=true)
	private Date brdDd;	//방송일자

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="spc_info")
	@Column(name="SPC_INFO", nullable=true)
	@Lob
	private String spcInfo;	//특이사항

	@XmlElement(name="lock_stat_cd")
	@Column(name="LOCK_STAT_CD", length=5, nullable=true)
	private String lockStatcd;	//Lock상태

	@XmlElement(name="prod_route")
	@Column(name="PROD_ROUTE", length=30, nullable=true)
	private String prodRoute;	//자료상태코드
	
	@XmlElement(name="category_id")
	@Column(name="CATEGORY_ID", nullable=true)
	private Integer categoryId;	//카테고리id
	
	@XmlElement(name="episode_id")
	@Column(name="EPISODE_ID", nullable=true)
	private Integer episodeId;	//에피소드 id
	
	@XmlElement(name="segment_id")
	@Column(name="SEGMENT_ID", nullable=true)
	private Integer segmentId;	//세그먼트id
	
	@XmlElement(name="arrange_dt")
	@Column(name="ARRANGE_DT", nullable=true)
	private Date arrangeDt;	//세그먼트id
	
	@XmlElement(name="rist_clf_cd")
	@Column(name="RIST_CLF_CD", length=5, nullable=true)
	private String ristClfCd;	// 사용제한 등급
	
	@Transient
	private String categoryNm;
	@Transient
	private String episodeNm;
	@Transient
	private String segmentNm;
	
	@Transient
	private Integer vdHresol;
	@Transient
	private Integer vdVresol;
	@Transient
	private String flPath;
	@Transient
	private Integer lft;
	@Transient
	private Integer rgt;
	@Transient
	private String wrkFileNm;
	@Transient
	private String dataStatNm;
	@Transient
	private String nodes;
	
	
	
	public String getRistClfCd() {
		return ristClfCd;
	}
	
	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}


	public String getNodes() {
		return nodes;
	}


	public void setNodes(String nodes) {
		this.nodes = nodes;
	}


	public String getDataStatNm() {
		return dataStatNm;
	}


	public void setDataStatNm(String dataStatNm) {
		this.dataStatNm = dataStatNm;
	}


	public Date getArrangeDt() {
		return arrangeDt;
	}


	public void setArrangeDt(Date arrangeDt) {
		this.arrangeDt = arrangeDt;
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
	
	public void setLockStatcd(String lockStatcd) {
		this.lockStatcd = lockStatcd;
	}
	public String getLockStatcd() {
		return lockStatcd;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Transient
	@OneToMany(targetEntity=CornerTbl.class, fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL, mappedBy="contentsTbl")
	private Set<CornerTbl> corner;

	public Set<CornerTbl> getCorner() {
		return corner;
	}
	public void setCorner(Set<CornerTbl> corner) {
		this.corner = corner;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	//@Transient
	@OneToMany(targetEntity=ContentsInstTbl.class, fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL, mappedBy="contentsTbl")
	private Set<ContentsInstTbl> contentsInst = null;

	public Set<ContentsInstTbl> getContentsInst() {
		return contentsInst;
	}
	public void setContentsInst(Set<ContentsInstTbl> contentsInst) {
		this.contentsInst = contentsInst;
	}
	public void addContentsInst(ContentsInstTbl contentsInstTbl) {
		//기본적으로null로 지정되어있기대문에 값이 null이라면 제네릭으로 선언된 동일한 set을 생성해 준다.
		if(contentsInst == null) {
			contentsInst = new HashSet<ContentsInstTbl>();
		}
		this.contentsInst.add(contentsInstTbl);
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Transient
	@OneToMany(targetEntity=ContentsModTbl.class, fetch=FetchType.LAZY,
			mappedBy="contentsTbl", cascade=CascadeType.ALL)
	private Set<ContentsModTbl>contentsMod;

	public Set<ContentsModTbl> getContentsMod() {
		return contentsMod;
	}

	public void setContentsMod(Set<ContentsModTbl> contentsMod) {
		this.contentsMod = contentsMod;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Transient
	@OneToMany(targetEntity=AttachTbl.class, fetch=FetchType.LAZY,
			mappedBy="contentsTbl", cascade=CascadeType.ALL)
	private Set<AttachTbl>attachTbls;

	public Set<AttachTbl> getAttachTbls() {
		return attachTbls;
	}
	public void setAttachTbls(Set<AttachTbl> attachTbls) {
		this.attachTbls = attachTbls;
	}
	
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="CATEGORY_ID", updatable=false, insertable=false),
		@JoinColumn(name="EPISODE_ID", updatable=false, insertable=false),
		@JoinColumn(name="SEGMENT_ID", updatable=false, insertable=false)
	})
	private	 SegmentTbl segmentTbl;

	public SegmentTbl getSegmentTbl() {
		return segmentTbl;
	}
	public void setSegmentTbl(SegmentTbl segmentTbl) {
		this.segmentTbl = segmentTbl;
	}

	
	
}

package kr.co.d2net.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="CONTENTS_TBL",
	indexes = {
		@Index(name="contents_idx1", columnList="category_id, episode_id, segment_id", unique=false),
		@Index(name="contents_idx2", columnList="category_id, episode_id", unique=false),
		@Index(name="contents_idx3", columnList="category_id", unique=false)
	})
@NamedQueries({
	@NamedQuery(
			name="Content.getContentsById",
			query="SELECT c FROM ContentsTbl as c WHERE c.ctId = :ctId",
			hints = {
					@QueryHint(name="org.hibernate.cacheable", value="true"),
			}
	),
	@NamedQuery(
			name="Content.findContentsByCateId",
			query="SELECT c FROM ContentsTbl as c WHERE c.categoryId = :categoryId",
			hints = {
					@QueryHint(name="org.hibernate.cacheable", value="true"),
			}
	),
	@NamedQuery(
			name="Content.findContentsByCateNepisId",
			query="SELECT c FROM ContentsTbl as c WHERE c.categoryId = :categoryId AND c.episodeId = :episodeId",
			hints = {
					@QueryHint(name="org.hibernate.cacheable", value="true"),
			}
	),
	@NamedQuery(
			name="Content.findContentsByCateNepisNsegId",
			query="SELECT c FROM ContentsTbl as c WHERE c.categoryId = :categoryId AND c.episodeId = :episodeId AND c.segmentId = :segmentId",
			hints = {
					@QueryHint(name="org.hibernate.cacheable", value="true"),
			}
	)
})
public class ContentsTbl extends BaseObject{
	
	public ContentsTbl() {}
	
	public ContentsTbl(Long ctId, String ctNm, String categoryNm, String episodeNm) {
		this.ctId = ctId;
		this.ctNm = ctNm;
		this.categoryNm = categoryNm;
		this.episodeNm = episodeNm;
	}

	@Id
	@TableGenerator(name = "CT_ID_SEQ", table = "ID_GEN_TBL", 
			pkColumnName = "ENTITY_NAME", pkColumnValue = "CT_ID_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CT_ID_SEQ")
	@BusinessKey	
	@Column(name="CT_ID", length=16)
	private Long ctId;	//컨텐츠ID
	
	@Column(name="CT_TYP", length=5)
	private String ctTyp;	//콘텐츠유형코드

	@Column(name="CT_CLA", length=10)
	private String ctCla;	//콘텐츠구분코드

	@Column(name="CT_NM", length=300)
	private String ctNm;	//컨텐츠명

	@Column(name="CONT", length=4000)
	private String cont;	//내용

	@Column(name="VD_QLTY", length=5)
	private String vdQlty;	//화질코드

	@Column(name="ASP_RTO_CD", length=5)
	private String aspRtoCd;	//종횡비코드

	@Column(name="KEY_WORDS", length=1000)
	private String keyWords;	//키워드

	@Column(name="KFRM_PATH", length=256)
	private String kfrmPath;	//키프레임경로

	@Column(name="RPIMG_KFRM_SEQ")
	private Long rpimgKfrmSeq;	//대표화면키프레임순번

	@Column(name="TOT_KFRM_NUMS")
	private Integer totKfrmNums;	//총키프레임수

	/*
	 * '사용여부'를 대부분의 테이블에서 사용하므로 공통으로 사용할 수 있는 Enum class 정의
	 * 'EnumType.STRING' 로 선언하면 'Y' or 'N' 문자열로 저장이 됨
	 * Object를 set할때 contents.setUseYn(UseEnum.Y) or UseEnum.N 로 선언하여 save 하면 됨.
	 * Object를 비교할때는 if(contents.getUseYn() == UseEnum.Y) 로 비교함.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'")
	private UseEnum useYn;		//사용여부

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT")
	private Date regDt;	//등록일시

	/*
	 * 수동으로 등록시 등록자의ID를 넣고, 장비가 생성하는 메타의 경우는 null
	 * 2015.3.25. 강명성 차장님과 회의후 결정
	 */
	@Column(name="REGRID", length=30)
	private String regrId;	//등록자ID 

	@Column(name="MODRID", length=30)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT")
	private Date modDt;	//수정일시

	@Temporal(TemporalType.DATE)
	@Column(name="DEL_DD")
	private Date delDd;	//삭제일자

	@Column(name="DATA_STAT_CD", length=5)
	private String dataStatCd;	//자료상태코드

	@Column(name="CT_LENG", length=11)
	private String ctLeng;	//콘텐츠길이

	@Column(name="CT_SEQ")
	private Long ctSeq;	//콘텐츠일련번호

	@Column(name="DURATION")
	private Integer duration;	//DURATION

	@Temporal(TemporalType.DATE)
	@Column(name="BRD_DD")
	private Date brdDd;	//방송일자

	@Column(name="SPC_INFO")
	@Lob
	private String spcInfo;	//특이사항

	@Column(name="LOCK_STAT_CD", length=5)
	private String lockStatcd;	//Lock상태


	/*
	 * 수동으로 등록시 사전에 정의된 값을 넣고, 장비가 생성하는 메타의 경우는 장비id(equipmentTbl의  deviceId를 넣는다)
	 * 2015.3.25. 강명성 차장님과 회의후 결정
	 */
	@Column(name="PROD_ROUTE", length=30)
	private String prodRoute;	//자료상태코드
	
	@Column(name="CATEGORY_ID", nullable=false)
	private Integer categoryId;	//카테고리id
	
	@Column(name="EPISODE_ID", nullable=false)
	private Integer episodeId;	//에피소드 id
	
	@Column(name="SEGMENT_ID", nullable=false)
	private Integer segmentId;	//세그먼트id
	
	@Column(name="ARRANGE_DT")
	private Date arrangeDt;	//세그먼트id
	
	@Column(name="RIST_CLF_CD", length=5)
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
	
	
	
	public UseEnum getUseYn() {
		return useYn;
	}

	public void setUseYn(UseEnum useYn) {
		this.useYn = useYn;
	}

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
	@OrderBy(value = "ctiId DESC")
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
		@JoinColumn(name="CATEGORY_ID", referencedColumnName = "CATEGORY_ID", updatable=false, insertable=false),
		@JoinColumn(name="EPISODE_ID", referencedColumnName = "EPISODE_ID", updatable=false, insertable=false),
		@JoinColumn(name="SEGMENT_ID", referencedColumnName = "SEGMENT_ID", updatable=false, insertable=false)
	})
	private	 SegmentTbl segmentTbl;

	public SegmentTbl getSegmentTbl() {
		return segmentTbl;
	}
	public void setSegmentTbl(SegmentTbl segmentTbl) {
		this.segmentTbl = segmentTbl;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToOne(mappedBy="contentsTbl", optional=true, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private DisuseInfoTbl disuseInfoTbl;

	public DisuseInfoTbl getDisuseInfoTbl() {
		return disuseInfoTbl;
	}
	public void setDisuseInfoTbl(DisuseInfoTbl disuseInfoTbl) {
		this.disuseInfoTbl = disuseInfoTbl;
	}
	
	
}

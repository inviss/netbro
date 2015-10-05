package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@IdClass(value=SegmentPK.class)
@Table(name="SEGMENT_TBL")
public class SegmentTbl {

	/**
	 * 세그먼트의 키값은 자동 생성이 아니라 외부로부터 받기 때문에
	 * 추가 및 수정 요청 모두 키값은 존재하게 된다.
	 * DAO에서 인식할 수 있도록 add() or update() 구분을 해주도록 한다.
	 * segmentTbl.add() or segmentTbl.update()
	 * 
	 * @author kms
	 *
	 */
	public static enum Operator {
		ADD,
		UPDATE;
	}
	private Operator operator = Operator.ADD;
	public SegmentTbl withOperator(Operator operator) {
		this.operator = operator;
		return this;
	}
	public Operator getOperator() {
		return operator;
	}
	
	public SegmentTbl add() {
		return withOperator(Operator.ADD);
	}

	public SegmentTbl update() {
		return withOperator(Operator.UPDATE);
	}
	
	@Id
	@Column(name="SEGMENT_ID", length=4)
	private Integer segmentId;
	@Id
	@Column(name="EPISODE_ID", length=4)
	private Integer episodeId;
	@Id
	@Column(name="CATEGORY_ID", length=4)
	private Integer categoryId;

	
	/*
	 * '사용여부'를 대부분의 테이블에서 사용하므로 공통으로 사용할 수 있는 Enum class 정의
	 * 'EnumType.STRING' 로 선언하면 'Y' or 'N' 문자열로 저장이 됨
	 * Object를 set할때 contents.setUseYn(UseEnum.Y) or UseEnum.N 로 선언하여 save 하면 됨.
	 * Object를 비교할때는 if(contents.getUseYn() == UseEnum.Y) 로 비교함.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'")
	private UseEnum useYn;		//사용여부

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자Id

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자아이디

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@Column(name="SEGMENT_NM", length=30, nullable=true)
	private String segmentNm;	//회차명

	
	public UseEnum getUseYn() {
		return useYn;
	}
	public void setUseYn(UseEnum useYn) {
		this.useYn = useYn;
	}
	public Integer getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Integer segmentId) {
		this.segmentId = segmentId;
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

	public String getRegrId() {
		return regrId;
	}

	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
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

	public String getSegmentNm() {
		return segmentNm;
	}

	public void setSegmentNm(String segmentNm) {
		this.segmentNm = segmentNm;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID", insertable = false, updatable = false),
		@JoinColumn(name = "EPISODE_ID", referencedColumnName = "EPISODE_ID", insertable = false, updatable = false) 
	})
	private EpisodeTbl episodeTbl;

	public EpisodeTbl getEpisodeTbl() {
		return episodeTbl;
	}
	public void setEpisodeTbl(EpisodeTbl episodeTbl) {
		this.episodeTbl = episodeTbl;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause = "use_yn = 'Y'")
	@OneToMany(targetEntity=ContentsTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="segmentTbl")
	private Set<ContentsTbl> contentsTbls;

	public Set<ContentsTbl> getContentsTbls() {
		return contentsTbls;
	}
	public void setContentsTbls(Set<ContentsTbl> contentsTbls) {
		this.contentsTbls = contentsTbls;
	}


}

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@IdClass(value=EpisodePK.class)
@Table(name="EPISODE_TBL")
public class EpisodeTbl {
	
	/**
	 * 에피소드의 키값은 자동 생성이 아니라 외부로부터 받기 때문에
	 * 추가 및 수정 요청 모두 키값은 존재하게 된다.
	 * DAO에서 인식할 수 있도록 add() or update() 구분을 해주도록 한다.
	 * episodeTbl.add() or episodeTbl.update()
	 * 
	 * @author kms
	 *
	 */
	public static enum Operator {
		ADD,
		UPDATE;
	}
	private Operator operator = Operator.ADD;
	public EpisodeTbl withOperator(Operator operator) {
		this.operator = operator;
		return this;
	}
	public Operator getOperator() {
		return operator;
	}
	
	public EpisodeTbl add() {
		return withOperator(Operator.ADD);
	}

	public EpisodeTbl update() {
		return withOperator(Operator.UPDATE);
	}
	
	public EpisodeTbl(){};
	
	
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
	private String regrId;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;

	@Column(name="EPISODE_NM", length=30, nullable=true)
	private String episodeNm;
	
	
	public UseEnum getUseYn() {
		return useYn;
	}
	public void setUseYn(UseEnum useYn) {
		this.useYn = useYn;
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


	public String getEpisodeNm() {
		return episodeNm;
	}

	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}


	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false)
	@JoinColumn(name="category_id", updatable=false, insertable=false)
	private CategoryTbl categoryTbl;

	public CategoryTbl getCategoryTbl() {
		return categoryTbl;
	}

	public void setCategoryTbl(CategoryTbl categoryTbl) {
		this.categoryTbl = categoryTbl;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(targetEntity=SegmentTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="episodeTbl")
	private Set<SegmentTbl> segmentTbl;

	public Set<SegmentTbl> getSegmentTbl() {
		return segmentTbl;
	}

	public void setSegmentTbl(Set<SegmentTbl> segmentTbl) {
		this.segmentTbl = segmentTbl;
	}

}

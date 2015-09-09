package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="EPISODE_TBL")
public class EpisodeTbl{

	@Embeddable
	public static class EpisodeId extends BaseObject {

		private static final long serialVersionUID = 3656048480870828753L;

		@BusinessKey
		@Column(name="EPISODE_ID", length=4)
		private Integer episodeId;	//?뚯감ID

		@BusinessKey
		@Column(name="CATEGORY_ID", length=4)
		private Integer categoryId;

		public EpisodeId() {}

		public EpisodeId(Integer episodeId, Integer categoryId) {
			this.episodeId = episodeId;
			this.categoryId = categoryId;
		}
		public Integer getEpisodeId() {
			return episodeId;
		}
		public void setEpisodeId(Integer episodeId) {
			this.episodeId = episodeId;
		}
		public Integer getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Integer categoryId) {
			this.categoryId = categoryId;
		}

	}

	@EmbeddedId
	private EpisodeId id = new EpisodeId();

	public EpisodeId getId() {
		return id;
	}

	public void setId(EpisodeId id) {
		this.id = id;
	}


	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//?ъ슜?щ?

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//?깅줉?륤d

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//?깅줉?쇱떆

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//?섏젙?먯븘?대뵒

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//?섏젙?쇱떆

	@Column(name="EPISODE_NM", length=30, nullable=true)
	private String episodeNm;	//?뚯감紐?
	@Transient
	private Integer categoryId;
	@Transient
	private Integer episodeId;
	public String getUseYn() {
		return useYn;
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

	public void setUseYn(String useYn) {
		this.useYn = useYn;
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




	
	@ManyToOne(optional=false)
	@JoinColumn(name="category_id", updatable=false, insertable=false)
	private CategoryTbl categoryTbl;

	public CategoryTbl getCategoryTbl() {
		return categoryTbl;
	}

	public void setCategoryTbl(CategoryTbl categoryTbl) {
		this.categoryTbl = categoryTbl;
	}
	
	
	@OneToMany(targetEntity=SegmentTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="episodeTbl")
	private Set<SegmentTbl> segmentTbl;

	public Set<SegmentTbl> getSegmentTbl() {
		return segmentTbl;
	}

	public void setSegmentTbl(Set<SegmentTbl> segmentTbl) {
		this.segmentTbl = segmentTbl;
	}

}

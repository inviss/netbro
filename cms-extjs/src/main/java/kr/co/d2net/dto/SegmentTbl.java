package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="SEGMENT_TBL")
public class SegmentTbl extends BaseObject{
	
	@Embeddable
	public static class SegmentId extends BaseObject {
	
		@BusinessKey
		@Column(name="SEGMENT_ID", length=4)
		private Integer segmentId;	//회차ID
		
		@BusinessKey
		@Column(name="EPISODE_ID", length=4)
		private Integer episodeId;	//회차ID
		
		@BusinessKey
		@Column(name="CATEGORY_ID", length=4)
		private Integer categoryId;
		
		
		public SegmentId() {}
		public SegmentId(Integer segmentId, Integer categoryId, Integer episodeId) {
			this.segmentId = segmentId;
			this.categoryId = categoryId;
			this.episodeId = episodeId;
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
	
	}

	@EmbeddedId
	private SegmentId id = new SegmentId();

	public SegmentId getId() {
		return id;
	}

	public void setId(SegmentId id) {
		this.id = id;
	}
	
	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부
	
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
	

	public String getUseYn() {
		return useYn;
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

	public String getSegmentNm() {
		return segmentNm;
	}

	public void setSegmentNm(String segmentNm) {
		this.segmentNm = segmentNm;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumns({
	    @JoinColumn(name = "CATEGORY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "EPISODE_ID", insertable = false, updatable = false) 
	})
	private EpisodeTbl episodeTbl;
	
	public EpisodeTbl getEpisodeTbl() {
		return episodeTbl;
	}
	public void setEpisodeTbl(EpisodeTbl episodeTbl) {
		this.episodeTbl = episodeTbl;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(targetEntity=ContentsTbl.class, fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL, mappedBy="segmentTbl")
	private Set<ContentsTbl> contentsTbls;

	public Set<ContentsTbl> getContentsTbls() {
		return contentsTbls;
	}
	public void setContentsTbls(Set<ContentsTbl> contentsTbls) {
		this.contentsTbls = contentsTbls;
	}
	
	
}

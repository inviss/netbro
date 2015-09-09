package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;




@Entity
@Table(name="BUSI_PARTNER_CATEGORY")
public class BusiPartnerCategory{
	
	public BusiPartnerCategory(){}
	
	@Embeddable
	public static class BusiPartnerCategoryId extends BaseObject {
	
		private static final long serialVersionUID = 3656048480870828753L;

		@BusinessKey
		@Column(name="CATEGORY_ID", length=16)
		private Integer categoryId;	//프로그램코드
		
		
		@BusinessKey
		@Column(name="CT_TYP", length=5)
		private String ctTyp;	//콘텐츠타입

		
		public BusiPartnerCategoryId() {}
		public BusiPartnerCategoryId(Integer categoryId, String ctTyp) {
			this.categoryId = categoryId;
			this.ctTyp = ctTyp;
		}
		
		
		public Integer getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Integer categoryId) {
			this.categoryId = categoryId;
		}
		public String getCtTyp() {
			return ctTyp;
		}

		public void setCtTyp(String ctTyp) {
			this.ctTyp = ctTyp;
		}
		
	}

	@EmbeddedId
	private BusiPartnerCategoryId id = new BusiPartnerCategoryId();

	public BusiPartnerCategoryId getId() {
		return id;
	}

	public void setId(BusiPartnerCategoryId id) {
		this.id = id;
	}
	
	@Column(name="RECYN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String recYn;	//녹화여부

	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="BGN_TIME", nullable=true)
	private Date bgnTime;	//시작시간

	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="END_TIME", nullable=true)
	private Date endTime;	//종료시간

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일자

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일자

	@Column(name="AUDIO_MODE_CODE", length=2, nullable=true)
	private String audioModeCode;	//오디오모드코드


	public String getRecYn() {
		return recYn;
	}

	public void setRecYn(String recYn) {
		this.recYn = recYn;
	}

	public Date getBgnTime() {
		return bgnTime;
	}

	public void setBgnTime(Date bgnTime) {
		this.bgnTime = bgnTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public String getAudioModeCode() {
		return audioModeCode;
	}

	public void setAudioModeCode(String audioModeCode) {
		this.audioModeCode = audioModeCode;
	}
	
	@BusinessKey
	@ManyToOne(optional=false)
	@JoinColumn(name="BUSI_PARTNERID", updatable=false, insertable=true)
	private BusiPartnerTbl busiPartnerId;	//비지니스파트너ID

	public BusiPartnerTbl getBusiPartnerId() {
		return busiPartnerId;
	}

	public void setBusiPartnerId(BusiPartnerTbl busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}
	
	
}

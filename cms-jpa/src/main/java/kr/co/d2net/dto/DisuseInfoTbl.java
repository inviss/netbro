package kr.co.d2net.dto;


import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name="DISUSE_INFO_TBL")
public class DisuseInfoTbl extends BaseObject {

	
	public DisuseInfoTbl() {}
	
	public DisuseInfoTbl(Long ctId,String disuseClf, Date regDt,Date disuseDd,String ctNm) {
		this.ctId = ctId;
		this.disuseClf = disuseClf;
	 	this.regDt = regDt;
	 	this.disuseDd = disuseDd;
	 	this.ctNm = ctNm;
	 
	}
	
	@Id
	@BusinessKey
	@Column(name="CT_ID", length=16)
	private Long ctId;	//컨텐츠ID

	@Column(name="DISUSE_DD", length=100, nullable=true)
	private Date disuseDd;	//폐기일
	
	@Lob
	@Column(name="DISUSE_RSL", nullable=true)
	private String disuseRsl;	//폐기사유

	@Column(name="DISUSE_CLF", length=4, nullable=true)
	private String disuseClf;	//폐기코드

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일자

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일자

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	
	@Transient
	private String ctNm;//컨텐츠 명
	
	
	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public Date getDisuseDd() {
		return disuseDd;
	}

	public void setDisuseDd(Date disuseDd) {
		this.disuseDd = disuseDd;
	}

	public String getDisuseRsl() {
		return disuseRsl;
	}

	public void setDisuseRsl(String disuseRsl) {
		this.disuseRsl = disuseRsl;
	}

	public String getDisuseClf() {
		return disuseClf;
	}

	public void setDisuseClf(String disuseClf) {
		this.disuseClf = disuseClf;
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

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	public String getModrId() {
		return modrId;
	}

	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	
	@OneToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="ct_id", referencedColumnName="ct_id", updatable=false, insertable=false)
	private ContentsTbl contentsTbl;

	public ContentsTbl getContentsTbl() {
		return contentsTbl;
	}
	public void setContentsTbl(ContentsTbl contentsTbl) {
		this.contentsTbl = contentsTbl;
	}
	

}

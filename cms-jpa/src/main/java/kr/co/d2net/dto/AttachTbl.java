package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="ATTACH_TBL")
public class AttachTbl extends BaseObject{

	@Id
	@TableGenerator(name = "ATTACH_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "ATTACH_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ATTACH_SEQ")
	@BusinessKey	
	@Column(name="SEQ", length=16)
	private Long seq;	//구분코드

	@Column(name="CT_ID", length=16)
	private Long ctId;	//구분상세코드

	@Column(name="ORG_FILE_NM", length=256, nullable=true)
	private String orgFilenm;	//구분명

	@Column(name="TRANS_FILE_NM", length=256, nullable=true)
	private String transFilenm;	//구분상세코드명

	@Column(name="ATTACH_TYPE", length=3, nullable=true)
	private String attachType;	//코드설명

	@Column(name="FL_PATH", length=256, nullable=true)
	private String flPath;	//비고1

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYN;	//사용여부


	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
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
	public String getUseYN() {
		return useYN;
	}
	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}

	public String getOrgFilenm() {
		return orgFilenm;
	}

	public void setOrgFilenm(String orgFilenm) {
		this.orgFilenm = orgFilenm;
	}

	public String getTransFilenm() {
		return transFilenm;
	}

	public void setTransFilenm(String transFilenm) {
		this.transFilenm = transFilenm;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}
	
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}


	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	//@Transient
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="CT_ID", updatable=false, insertable=false)
	private ContentsTbl contentsTbl;

	public ContentsTbl getContentsTbl() {
		return contentsTbl;
	}
	public void setContentsTbl(ContentsTbl contentsTbl) {
		this.contentsTbl = contentsTbl;
	}
	
	

}

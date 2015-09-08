package kr.co.d2net.dto;


import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="DISUSE_INFO_TBL")
public class DisuseInfoTbl extends BaseObject {

	@Id
	@BusinessKey
	@TableGenerator(name = "DISUSE_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "DISUSE_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DISUSE_ID_SEQ")
	@Column(name="DISUSE_NO", length=16)
	private Long disuseNo;	//폐기번호

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

	@Lob
	@Column(name="CANCEL_CONT", nullable=true)
	private String cancelCont;	//취소사유

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CANCEL_DT", nullable=true)
	private Date cancelDt;	//취소일자

	@Column(name="CT_ID", length=16)
	private Long ctId;	//컨텐츠ID
	
	
	
	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public Long getDisuseNo() {
		return disuseNo;
	}

	public void setDisuseNo(Long disuseNo) {
		this.disuseNo = disuseNo;
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

	public String getCancelCont() {
		return cancelCont;
	}

	public void setCancelCont(String cancelCont) {
		this.cancelCont = cancelCont;
	}

	public Date getCancelDt() {
		return cancelDt;
	}

	public void setCancelDt(Date cancelDt) {
		this.cancelDt = cancelDt;
	}

}

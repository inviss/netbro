package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="TRS_TBL")
public class TrsTbl extends BaseObject {
	private static final long serialVersionUID = 8043949665831948354L;
	

	@Id
	@TableGenerator(name = "TRS_SEQ", table = "ID_GEN_TBL", 
		pkColumnName = "ENTITY_NAME", pkColumnValue = "TRS_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TRS_SEQ")
	@BusinessKey
	@Column(name="SEQ", length=16)
	private Long seq;	//순번

	@Column(name="CTI_ID", length=16, nullable=true)
	private Long ctiId;	//영상 인스턴스ID

	@Column(name="CT_ID", length=16, nullable=true)
	private Long ctId;	//콘텐츠ID

	@Column(name="TRS_STATUS", length=1, nullable=true)
	private String trsStatus;	//진행상태

	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//진행률
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date reg_dt;	//등록일
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQ_DT", nullable=true)
	private Date reqDt;	//요청일

	@Column(name="REQRID", length=30, nullable=true)
	private String reqrId;	//요청자

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자


	@Column(name="ERROR_CD", length=3, nullable=true)
	private String errorCd;	//에러코드

	@Column(name="ERROR_CONT", length=100, nullable=true)
	private String errorCont;	//에러코드 내용

	@Column(name="PRIORITY", length=100, nullable=true)
	private String priority;	//우선순위

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

	@Column(name="TC_TYPE", length=4, nullable=true)
	private String tcType;	//작업형태
	
	@Column(name="DEVICE_ID", columnDefinition="char(4)", nullable=true)
	private String deviceId;	//장비ID
	

	
	public Date getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(Date reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public String getTrsStatus() {
		return trsStatus;
	}

	public void setTrsStatus(String trsStatus) {
		this.trsStatus = trsStatus;
	}

	public Integer getPrgrs() {
		return prgrs;
	}

	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

	public Date getReqDt() {
		return reqDt;
	}

	public void setReqDt(Date reqDt) {
		this.reqDt = reqDt;
	}

	public String getReqrId() {
		return reqrId;
	}

	public void setReqrId(String reqrId) {
		this.reqrId = reqrId;
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


	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public String getErrorCont() {
		return errorCont;
	}

	public void setErrorCont(String errorCont) {
		this.errorCont = errorCont;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getTcType() {
		return tcType;
	}

	public void setTcType(String tcType) {
		this.tcType = tcType;
	}

	@ManyToOne
	@JoinColumn(name="DEVICE_ID", updatable=false, insertable=false)
	private EquipmentTbl deviceTbl;

	public EquipmentTbl getDeviceTbl() {
		return deviceTbl;
	}
	public void setDeviceTbl(EquipmentTbl deviceTbl) {
		this.deviceTbl = deviceTbl;
	}

}

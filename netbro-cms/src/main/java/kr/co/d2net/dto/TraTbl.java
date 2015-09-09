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
@Table(name="TRA_TBL")
public class TraTbl extends BaseObject {
	private static final long serialVersionUID = 8043949665831948354L;
	
	@Id
	@TableGenerator(name = "TRA_SEQ", table = "ID_GEN_TBL", 
		pkColumnName = "ENTITY_NAME", pkColumnValue = "TRA_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TRA_SEQ")
	@BusinessKey
	@Column(name="SEQ", length=16)
	private Long seq;	//순번

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//수정일시

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQ_DT", nullable=true)
	private Date reqDt;	//등록일시


	@Column(name="REQ_USRID", length=15, nullable=true)
	private String reqUsrid;	//등록자ID

	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시


	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID


	@Column(name="CT_ID", length=16, nullable=true)
	private Long ctId;	//콘텐츠인스턴스ID


	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//진행률


	@Column(name="ERROR_COUNT", length=10, nullable=true)
	private Integer errorCount;	//전송실패 횟수


	@Column(name="JOB_STATUS", length=3, nullable=true)
	private String jobStatus;	//job상태
	
	@Column(name="DEVICE_ID", columnDefinition="char(4)", nullable=true)
	private String deviceId;	//장비ID
	

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
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

	public Date getReqDt() {
		return reqDt;
	}


	public void setReqDt(Date reqDt) {
		this.reqDt = reqDt;
	}


	public String getReqUsrid() {
		return reqUsrid;
	}


	public void setReqUsrid(String reqUsrid) {
		this.reqUsrid = reqUsrid;
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


	public Long getCtId() {
		return ctId;
	}


	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}


	public Integer getPrgrs() {
		return prgrs;
	}


	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}


	public Integer getErrorCount() {
		return errorCount;
	}


	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}


	public String getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
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

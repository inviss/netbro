package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name="TRS_TBL")
public class TrsTbl extends BaseObject {
	

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

	
	@Column(name="WORK_STATCD", length=3, nullable=true)
	private String workStatcd;	//작업상태

	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//진행률
	
	@Column(name="RETRY_CNT", length=2)
	private Integer retryCnt;	//전송실패횟수
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일
	
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

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TRS_STR_DT", nullable=true)
	private Date trsStrDt;	//등록시간
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TRS_END_DT", nullable=true)
	private Date trsEndDt;	//종료시간
	
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
	
	@Column(name="PRO_FLID", length=30)
	private Long proFlId;	//프로파일ID
	
	@Column(name="BUSI_PARTNERID", length=30)
	private Long busiPartnerId;	//사업자ID
	
	@Column(name="DEVICE_ID", length=6)
	private String deviceId;	//장비ID
	
	@Column(name="DEVICE_NUM", length = 1)
	private Integer deviceNum;	//장비ID
	
	
	public Integer getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}

	public Integer getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(Integer retryCnt) {
		this.retryCnt = retryCnt;
	}


	public Long getProFlId() {
		return proFlId;
	}

	public void setProFlId(Long proFlId) {
		this.proFlId = proFlId;
	}

	public Long getBusiPartnerId() {
		return busiPartnerId;
	}

	public void setBusiPartnerId(Long busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}


	public Date getRegDt() {
		return regDt;
	}

	public String getWorkStatcd() {
		return workStatcd;
	}

	public Date getTrsStrDt() {
		return trsStrDt;
	}

	public void setTrsStrDt(Date trsStrDt) {
		this.trsStrDt = trsStrDt;
	}

	public Date getTrsEndDt() {
		return trsEndDt;
	}

	public void setTrsEndDt(Date trsEndDt) {
		this.trsEndDt = trsEndDt;
	}

	public void setWorkStatcd(String workStatcd) {
		this.workStatcd = workStatcd;
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
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumns(value = {
			@JoinColumn(name="DEVICE_ID", updatable=false, insertable=false),
			@JoinColumn(name="DEVICE_NUM", updatable=false, insertable=false)
	})
	private EquipmentTbl deviceTbl;

	public EquipmentTbl getDeviceTbl() {
		return deviceTbl;
	}
	public void setDeviceTbl(EquipmentTbl deviceTbl) {
		this.deviceTbl = deviceTbl;
	}

}
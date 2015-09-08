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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
@Entity
@Table(name="DOWNLOAD_TBL")
public class DownloadTbl extends BaseObject{

	@Id
	@TableGenerator(name = "DOWNLOAD_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "DOWNLOAD_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DOWNLOAD_SEQ")
	@BusinessKey	
	@Column(name="SEQ", length=16)
	private Long seq;	//seq

	@Column(name="CTI_ID", length=16)
	private Long ctiId;	//컨텐츠인스트ID

	@Column(name="WORK_STAT_CD", length=3, nullable=true)
	private String workStatCd;	//작업코드

	@Column(name="ERROR_CD", length=3, nullable=true)
	private String errorCd;	//에러코드

	@Column(name="REASON", length=4000, nullable=true)
	private String reason;	//설명

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일

	@Column(name="APPROVE_ID", length=10, nullable=true)
	private String approveId;	//등록자

	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//등록자
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DN_STR_DT", nullable=true)
	private Date dnStrDt;	//다운로드요청시간
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DN_END_DT", nullable=true)
	private Date dnEndDt;	//다운로드요청시간
	
	@Column(name="DOWNLOAD_PATH", length=30, nullable=true)
	private String downloadPath;	//등록자
	
	@Column(name="DOWNLOAD_NM", length=256, nullable=true)
	private String downloadNm;	//등록자
	
	@Column(name="REQ_ID", length=10, nullable=true)
	private String reqId;	//요청자 id
	
	
	
	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getDownloadNm() {
		return downloadNm;
	}

	public void setDownloadNm(String downloadNm) {
		this.downloadNm = downloadNm;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public Date getDnStrDt() {
		return dnStrDt;
	}

	public void setDnStrDt(Date dnStrDt) {
		this.dnStrDt = dnStrDt;
	}

	public Date getDnEndDt() {
		return dnEndDt;
	}

	public void setDnEndDt(Date dnEndDt) {
		this.dnEndDt = dnEndDt;
	}

	public Integer getPrgrs() {
		return prgrs;
	}

	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
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

	public String getWorkStatCd() {
		return workStatCd;
	}

	public void setWorkStatCd(String workStatCd) {
		this.workStatCd = workStatCd;
	}

	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}


	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="CTI_ID", updatable=false, insertable=false)  
	private  ContentsInstTbl contentsInstTbls;

	public ContentsInstTbl getContentsInstTbls() {
		return contentsInstTbls;
	}

	public void setContentsInstTbls(ContentsInstTbl contentsInstTbls) {
		this.contentsInstTbls = contentsInstTbls;
	}
	

}

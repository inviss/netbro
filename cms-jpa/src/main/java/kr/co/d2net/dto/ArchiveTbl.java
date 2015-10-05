package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Entity
@Table(name="ARCHIVE_Tbl")
public class ArchiveTbl extends BaseObject{

	@Id
	@TableGenerator(name = "ARCHIVE_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "ARCHIVE_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ARCHIVE_SEQ")
	@BusinessKey	
	@Column(name="SEQ", length=16)
	private Long seq;	//seq

	@Column(name="CTI_ID", length=16)
	private Long ctiId;	//컨텐츠인스트ID

	@Column(name="WORK_STAT_CD", length=3, nullable=true)
	private String workStatCd;	//작업코드

	@Column(name="ERROR_CD", length=3, nullable=true)
	private String errorCd;	//에러코드

	@Column(name="CONT", length=4000, nullable=true)
	private String cont;	//설명

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일

	@Column(name="APPROVE_ID", length=10, nullable=true)
	private String approveId;	//등록자

	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//등록자
	
	@Column(name="ARCHIVE_PATH", length=13, nullable=true)
	private String archivePath;	//등록자
	

	
	public String getArchivePath() {
		return archivePath;
	}

	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
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

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
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

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@OneToOne(optional=false)
	@JoinColumn(name="CTI_ID", updatable=false, insertable=false)
	private  ContentsInstTbl contentsInstTbls;


	public ContentsInstTbl getContentsInstTbls() {
		return contentsInstTbls;
	}

	public void setContentsInstTbls(ContentsInstTbl contentsInstTbls) {
		this.contentsInstTbls = contentsInstTbls;
	}
	

}

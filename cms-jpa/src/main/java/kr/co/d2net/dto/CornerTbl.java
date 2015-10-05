package kr.co.d2net.dto;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@SuppressWarnings("serial")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="CORNER_TBL")
public class CornerTbl extends BaseObject{

	@Id
	@TableGenerator(name = "CORNER_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "CN_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CORNER_ID_SEQ")
	@BusinessKey
	@Column(name="CN_ID", length=16)
	private Long cnId;	//콘텐츠인스턴스ID


	@Column(name="CN_NM", length=1200, nullable=true)
	private String cnNm;	//코너명

	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="BGN_TIME", nullable=true)
	private Date bgmTime;	//시작시간

	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="END_TIME", nullable=true)
	private Date endTime;	//종료시간

	@Lob
	@Column(name="CN_CONT", nullable=true)
	private String cnCont;	//코너 내용

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일

	@Column(name="REG_ID", length=30, nullable=true)
	private String regId;	//등록자

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일

	@Column(name="MOD_ID", length=30, nullable=true)
	private String modId;	//수정자

	@Column(name="DURATION", length=12, nullable=true)
	private Long duration;	//DURATION

	@Column(name="RPIMG_KFRM_SEQ", length=10, nullable=true)
	private Long rpimgKfrmSeq;	//대표화면키프레임순번
	
	@Column(name="CT_ID", length=16, nullable=false)
	private Long ctId;	// CT_ID
	
	@Column(name="S_DURATION", length=12, nullable=true)
	private Long sDuration;	//S_DURATION


	public Long getsDuration() {
		return sDuration;
	}

	public void setsDuration(Long sDuration) {
		this.sDuration = sDuration;
	}

	public Date getBgmTime() {
		return bgmTime;
	}

	public void setBgmTime(Date bgmTime) {
		this.bgmTime = bgmTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public String getCnNm() {
		return cnNm;
	}

	public void setCnNm(String cnNm) {
		this.cnNm = cnNm;
	}
	
	public String getCnCont() {
		return cnCont;
	}

	public void setCnCont(String cnCont) {
		this.cnCont = cnCont;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public Long getCnId() {
		return cnId;
	}
	public void setCnId(Long cnId) {
		this.cnId = cnId;
	}


	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}
	
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getRpimgKfrmSeq() {
		return rpimgKfrmSeq;
	}

	public void setRpimgKfrmSeq(Long rpimgKfrmSeq) {
		this.rpimgKfrmSeq = rpimgKfrmSeq;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

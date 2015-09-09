package kr.co.d2net.dto;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Table(name="NOTICE_TBL")
public class NoticeTbl extends BaseObject{


	private static final long serialVersionUID = 3656048480870828753L;

	@Id
	@TableGenerator(name = "NOTICE_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "NOTICE_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "NOTICE_SEQ")
	@BusinessKey
	@Column(name="NOTICE_ID", length=16)
	private Long noticeId;	//공지사항id


	@Column(name="TITLE", length=1200, nullable=true)
	private String title;	//공지사항 제목

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_DD", nullable=true)
	private Date startDd;	//팝업 시작일
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_DD", nullable=true)
	private Date endDd;	//팝업 종료일
	@Lob
	@Column(name="CONT", nullable=true)
	private String cont;	//공지 내용

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

	@Column(name="POPUP_YN", length=1, nullable=true)
	private String popUpYn;	//팝업 여부

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDd() {
		return startDd;
	}

	public void setStartDd(Date startDd) {
		this.startDd = startDd;
	}

	public Date getEndDd() {
		return endDd;
	}

	public void setEndDd(Date endDd) {
		this.endDd = endDd;
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

	public String getPopUpYn() {
		return popUpYn;
	}

	public void setPopUpYn(String popUpYn) {
		this.popUpYn = popUpYn;
	}

	
	
}

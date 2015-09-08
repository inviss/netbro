package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.co.d2net.xml.adapter.DatetimeAdapter;
import kr.co.d2net.xml.adapter.TimeAdapter;
import kr.co.d2net.xml.adapter.UrlEncodeAdapter;

import org.eclipse.persistence.annotations.JoinFetch;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement(name="contents_inst")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="CONTENTS_INST_TBL")
public class ContentsInstTbl extends BaseObject {

	@XmlElement(name="cti_id")
	@Id
	@TableGenerator(name = "CONTENTS_INST_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "CTI_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CONTENTS_INST_ID_SEQ")
	@BusinessKey
	@Column(name="CTI_ID", length=16)
	private Long ctiId;	//콘텐츠인스턴스ID

	@XmlElement(name="cti_fmt")
	@Column(name="CTI_FMT", length=5, nullable=false)
	private String ctiFmt;	//콘텐츠인스턴스포맷코드

	@XmlElement(name="me_cd")
	@Column(name="ME_CD", length=5, nullable=true)
	private String meCd;	//ME분리코드

	@XmlElement(name="bit_rt")
	@Column(name="BIT_RT", length=10, nullable=true)
	private String bitRt;	//비트전송율

	@XmlElement(name="drp_frm_yn")
	@Column(name="DRP_FRM_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String drpFrmYn;	//드롭프레임여부

	@XmlElement(name="vd_hresol")
	@Column(name="VD_HRESOL", length=8, nullable=true)
	private Integer vdHresol;	//비디오수평해상도

	@XmlElement(name="vd_vresol")
	@Column(name="VD_VRESOL", length=8, nullable=true)
	private Integer vdVresol;	//비디오수직해상도

	@XmlElement(name="record_type_cd")
	@Column(name="RECORD_TYPE_CD", length=5, nullable=true)
	private String recordTypeCd;	//녹음방식코드

	@XmlElement(name="file_path")
	@Column(name="FL_PATH", length=256, nullable=true)
	private String flPath;	//파일경로

	@XmlElement(name="fl_sz")
	@Column(name="FL_SZ", length=16, nullable=false)
	private Long flSz;	//파일크기

	@XmlElement(name="reg_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@XmlElement(name="regrid")
	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@XmlElement(name="mod_dt")
	@XmlJavaTypeAdapter(DatetimeAdapter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@XmlElement(name="modrid")
	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@XmlElement(name="color_cd")
	@Column(name="COLOR_CD", length=5, nullable=true)
	private String colorCd;	//색상코드

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="org_file_nm")
	@Column(name="ORG_FILE_NM", length=256, nullable=false)
	private String orgFileNm;	//원파일명

	@XmlElement(name="deviceid")
	@Column(name="DEVICEID", length=15, nullable=true)
	private String deviceId;	//장비ID

	@XmlElement(name="audio_yn")
	@Column(name="AUDIO_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String audioYn;	//오디오여부

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="wrk_file_nm")
	@Column(name="WRK_FILE_NM", length=256, nullable=true)
	private String wrkFileNm;	//작업파일명

	@XmlElement(name="pro_flid")
	@Column(name="PRO_FLID", length=30, nullable=true)
	private String proFlid;	//프로파일ID

	@XmlJavaTypeAdapter(UrlEncodeAdapter.class)
	@XmlElement(name="usr_file_nm")
	@Column(name="USR_FILE_NM", length=255, nullable=true)
	private String usrFileNm;	//유저 파일 이름

	@XmlElement(name="file_ext")
	@Column(name="FL_EXT", length=8, nullable=true)
	private String flExt;	//파일확장명

	@XmlElement(name="use_yn")
	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=false)
	private String useYn;	//사용여부

	@XmlElement(name="frm_per_sec")
	@Column(name="FRM_PER_SEC", length=10, nullable=true)
	private String frmPerSec;	//프레임

	@XmlElement(name="av_gubun")
	@Column(name="AV_GUBUN", length=10, nullable=true)
	private String avGubun;	//'A:오디오' 'V:비디오'

	@XmlElement(name="start_time")
	@XmlJavaTypeAdapter(TimeAdapter.class)
	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="START_TIME", nullable=true)
	private Date startTime;	//시작시간

	@XmlElement(name="end_time")
	@XmlJavaTypeAdapter(TimeAdapter.class)
	@DateTimeFormat(pattern="HH:mm:ss")
	@Temporal(TemporalType.TIME)
	@Column(name="END_TIME", nullable=true)
	private Date endTime;	//종료시간
	
	@XmlElement(name="end_time")
	@Column(name="CT_ID", length=16)
	private Long ctId;

	@XmlElement(name="rp_yn")
	@Column(name="RP_YN", columnDefinition="char(1) default 'N'", nullable=true)
	private String rpYn;	//대표화면 지정여부
	
	
	
	public Integer getVdVresol() {
		return vdVresol;
	}

	public void setVdVresol(Integer vdVresol) {
		this.vdVresol = vdVresol;
	}

	public String getRpYn() {
		return rpYn;
	}

	public void setRpYn(String rpYn) {
		this.rpYn = rpYn;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCtiFmt() {
		return ctiFmt;
	}

	public void setCtiFmt(String ctiFmt) {
		this.ctiFmt = ctiFmt;
	}

	public String getMeCd() {
		return meCd;
	}

	public void setMeCd(String meCd) {
		this.meCd = meCd;
	}

	public String getBitRt() {
		return bitRt;
	}

	public void setBitRt(String bitRt) {
		this.bitRt = bitRt;
	}

	public String getDrpFrmYn() {
		return drpFrmYn;
	}

	public void setDrpFrmYn(String drpFrmYn) {
		this.drpFrmYn = drpFrmYn;
	}

	public Long getCtiId() {
		return ctiId;
	}
	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public Integer getVdHresol() {
		return vdHresol;
	}

	public void setVdHresol(Integer vdHresol) {
		this.vdHresol = vdHresol;
	}

	public Integer getVdVersol() {
		return vdVresol;
	}

	public void setVdVersol(Integer vdVersol) {
		this.vdVresol = vdVersol;
	}

	public String getRecordTypeCd() {
		return recordTypeCd;
	}

	public void setRecordTypeCd(String recordTypeCd) {
		this.recordTypeCd = recordTypeCd;
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}

	public Long getFlSz() {
		return flSz;
	}

	public void setFlSz(Long flSz) {
		this.flSz = flSz;
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

	public String getColorCd() {
		return colorCd;
	}

	public void setColorCd(String colorCd) {
		this.colorCd = colorCd;
	}

	public String getOrgFileNm() {
		return orgFileNm;
	}

	public void setOrgFileNm(String orgFileNm) {
		this.orgFileNm = orgFileNm;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getAudioYn() {
		return audioYn;
	}

	public void setAudioYn(String audioYn) {
		this.audioYn = audioYn;
	}

	public String getWrkFileNm() {
		return wrkFileNm;
	}

	public void setWrkFileNm(String wrkFileNm) {
		this.wrkFileNm = wrkFileNm;
	}

	public String getProFlid() {
		return proFlid;
	}

	public void setProFlid(String proFlid) {
		this.proFlid = proFlid;
	}

	public String getUsrFileNm() {
		return usrFileNm;
	}

	public void setUsrFileNm(String usrFileNm) {
		this.usrFileNm = usrFileNm;
	}

	public String getFlExt() {
		return flExt;
	}

	public void setFlExt(String flExt) {
		this.flExt = flExt;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getFrmPerSec() {
		return frmPerSec;
	}

	public void setFrmPerSec(String frmPerSec) {
		this.frmPerSec = frmPerSec;
	}

	public String getAvGubun() {
		return avGubun;
	}

	public void setAvGubun(String avGubun) {
		this.avGubun = avGubun;
	}


	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="CT_ID", updatable=false, insertable=false)
	@JoinFetch
	private ContentsTbl contentsTbl;

	public ContentsTbl getContentsTbl() {
		return contentsTbl;
	}
	public void setContentsTbl(ContentsTbl contentsTbl) {
		this.contentsTbl = contentsTbl;
	}
	
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToOne(targetEntity=ArchiveTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="contentsInstTbls")
	private ArchiveTbl archiveTbl;


	public ArchiveTbl getArchiveTbl() {
		return archiveTbl;
	}

	public void setArchiveTbl(ArchiveTbl archiveTbl) {
		this.archiveTbl = archiveTbl;
	}
	
	
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(targetEntity=DownloadTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="contentsInstTbls")
	private Set<DownloadTbl> downloadTbl;
	
	public Set<DownloadTbl> getDownloadTbl() {
		return downloadTbl;
	}

	public void setDownloadTbl(Set<DownloadTbl> downloadTbl) {
		this.downloadTbl = downloadTbl;
	}

}

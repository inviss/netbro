package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="PRO_FL_TBL")
public class ProFlTbl extends BaseObject{
	
	@Id
	@TableGenerator(name = "PROFILE_ID_SEQ", table = "ID_GEN_TBL", 
		pkColumnName = "ENTITY_NAME", pkColumnValue = "PROFILE_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PROFILE_ID_SEQ")
	@Column(name="PRO_FLID", length=30)
	private Long proFlId;	//프로파일ID

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@Column(name="SERV_BIT", length=30, nullable=true)
	private String servBit;	//서비스BIT

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@Column(name="EXT", length=30, nullable=true)
	private String ext;	//확장자

	@Column(name="VDO_CODEC", length=30, nullable=true)
	private String vdoCodec;	//비디오코덱

	@Column(name="VDO_BIT_RATE", length=30, nullable=true)
	private String vdoBitRate;	//비디오비트레이트

	@Column(name="VDO_HORI", length=30, nullable=true)
	private String vdoHori;	//비디오가로

	@Column(name="VDO_VERT", length=30, nullable=true)
	private String vdoVert;	//비디오세로

	@Column(name="VDO_F_S", length=30, nullable=true)
	private String vdoFS;	//비디오FS

	@Column(name="VDO_SYNC", length=30, nullable=true)
	private String vdoSync;	//비디오종회맞춤

	@Column(name="AUD_CODEC", length=15, nullable=true)
	private String audCodec;	//오디오코덱

	@Column(name="AUD_BIT_RATE", length=30, nullable=true)
	private String audBitRate;	//오디오비트레이트

	@Column(name="AUD_CHAN", length=30, nullable=true)
	private String audChan;	//오디오채널

	@Column(name="AUD_S_RATE", length=30, nullable=true)
	private String audSRate;	//오디오샘플레이트

	@Column(name="KEY_FRAME", length=30, nullable=true)
	private String keyFrame;	//키프레임

	@Column(name="CHANPRIORITY", length=2, nullable=true)
	private Integer chanPriority;	//변경순위

	@Column(name="PRIORITY", length=2, nullable=true)
	private Integer priority;	//순위

	@Column(name="PRO_FLNM", length=40, nullable=true)
	private String proFlnm;	//프로파일명

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@Column(name="PIC_KIND", length=2, nullable=true)
	private String picKind;	//그림종류

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

	@Column(name="FL_NAME_RULE", length=8, nullable=true)
	private String flNameRule;	//파일이름규칙



	public Long getProFlId() {
		return proFlId;
	}

	public void setProFlId(Long proFlId) {
		this.proFlId = proFlId;
	}

	public String getRegrId() {
		return regrId;
	}

	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}

	public String getServBit() {
		return servBit;
	}

	public void setServBit(String servBit) {
		this.servBit = servBit;
	}

	public String getModrId() {
		return modrId;
	}

	public void setModrId(String modrId) {
		this.modrId = modrId;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getVdoCodec() {
		return vdoCodec;
	}

	public void setVdoCodec(String vdoCodec) {
		this.vdoCodec = vdoCodec;
	}

	public String getVdoBitRate() {
		return vdoBitRate;
	}

	public void setVdoBitRate(String vdoBitRate) {
		this.vdoBitRate = vdoBitRate;
	}

	public String getVdoHori() {
		return vdoHori;
	}

	public void setVdoHori(String vdoHori) {
		this.vdoHori = vdoHori;
	}

	public String getVdoVert() {
		return vdoVert;
	}

	public void setVdoVert(String vdoVert) {
		this.vdoVert = vdoVert;
	}

	public String getVdoFS() {
		return vdoFS;
	}

	public void setVdoFS(String vdoFS) {
		this.vdoFS = vdoFS;
	}

	public String getVdoSync() {
		return vdoSync;
	}

	public void setVdoSync(String vdoSync) {
		this.vdoSync = vdoSync;
	}

	public String getAudCodec() {
		return audCodec;
	}

	public void setAudCodec(String audCodec) {
		this.audCodec = audCodec;
	}

	public String getAudBitRate() {
		return audBitRate;
	}

	public void setAudBitRate(String audBitRate) {
		this.audBitRate = audBitRate;
	}

	public String getAudChan() {
		return audChan;
	}

	public void setAudChan(String audChan) {
		this.audChan = audChan;
	}

	public String getAudSRate() {
		return audSRate;
	}

	public void setAudSRate(String audSRate) {
		this.audSRate = audSRate;
	}

	public String getKeyFrame() {
		return keyFrame;
	}

	public void setKeyFrame(String keyFrame) {
		this.keyFrame = keyFrame;
	}

	public Integer getChanPriority() {
		return chanPriority;
	}

	public void setChanPriority(Integer chanPriority) {
		this.chanPriority = chanPriority;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getProFlnm() {
		return proFlnm;
	}

	public void setProFlnm(String proFlnm) {
		this.proFlnm = proFlnm;
	}

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getPicKind() {
		return picKind;
	}

	public void setPicKind(String picKind) {
		this.picKind = picKind;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getFlNameRule() {
		return flNameRule;
	}

	public void setFlNameRule(String flNameRule) {
		this.flNameRule = flNameRule;
	}

//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@OneToMany(targetEntity=Opt.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="proFlTbl")
//	private Set<Opt> opt;
//
//	public Set<Opt> getOpt() {
//		return opt;
//	}
//
//	public void setOpt(Set<Opt> opt) {
//		this.opt = opt;
//	}
//
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@OneToMany(targetEntity=ProBusiTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="proFlTbl")
//	private Set<ProBusiTbl> probusi;
//
//	public Set<ProBusiTbl> getProbusi() {
//		return probusi;
//	}
//
//	public void setProbusi(Set<ProBusiTbl> probusi) {
//		this.probusi = probusi;
//	}


}

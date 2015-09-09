package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="EQUIPMENT_TBL")
public class EquipmentTbl extends BaseObject{
	private static final long serialVersionUID = 8043949665831948354L;


	@Id
	@BusinessKey
	@Column(name="DEVICE_ID", columnDefinition="char(4)")
	private String deviceId;	//장비ID


	@Column(name="DEVICE_NM", length=255, nullable=true)
	private String deviceNm;	//장비이름

	@Column(name="DEVICE_IP", length=15, nullable=true)
	private String deviceIp;	//장비IP

	@Column(name="DEVICE_PORT", length=8, nullable=true)
	private String devicePort;	//장비Port

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부


	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@Column(name="CTI_ID", length=16, nullable=true)
	private Long ctiId;	//콘텐츠인스턴스ID

	@Column(name="WORK_STATCD", length=3, nullable=true)
	private String workStatcd;	//장비상태


	@Column(name="DEVICECLF_CD", length=5, nullable=true)
	private String deviceClfCd;	//
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceNm() {
		return deviceNm;
	}

	public void setDeviceNm(String deviceNm) {
		this.deviceNm = deviceNm;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(String devicePort) {
		this.devicePort = devicePort;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
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

	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public String getWorkStatcd() {
		return workStatcd;
	}

	public void setWorkStatcd(String workStatcd) {
		this.workStatcd = workStatcd;
	}

	public String getDeviceClfCd() {
		return deviceClfCd;
	}

	public void setDeviceClfCd(String deviceClfCd) {
		this.deviceClfCd = deviceClfCd;
	}

	@OneToMany(targetEntity=TraTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="deviceId")
	private Set<TraTbl> tra;

	public Set<TraTbl> getTra() {
		return tra;
	}

	public void setTra(Set<TraTbl> tra) {
		this.tra = tra;
	}

	@OneToMany(targetEntity=TrsTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="deviceTbl")
	private Set<TrsTbl> trs;

	public Set<TraTbl> getTrs() {
		return tra;
	}

	public void setTrs(Set<TrsTbl> trs) {
		this.trs = trs;
	}

}

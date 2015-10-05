package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable(true)
@Entity
@IdClass(EquipmentPK.class)
@Table(name="EQUIPMENT_TBL")
public class EquipmentTbl{

	@Id
	@Column(name="DEVICE_ID", length=6)
	private String deviceId;	//장비ID

	@Id
	@Column(name="DEVICE_NUM", length=2)
	private Integer deviceNum;	//

	@Column(name="DEVICE_NM", length=255, nullable=true)
	private String deviceNm;	//장비이름

	@Column(name="DEVICE_IP", length=15, nullable=true)
	private String deviceIp;	//장비IP

	@Column(name="DEVICE_PORT", length=4, nullable=true)
	private Integer devicePort;	//장비Port

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

	@Column(name="PRGRS", length=3, nullable=true)
	private Integer prgrs;	//장비상태
	
	
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	public Integer getPrgrs() {
		return prgrs;
	}

	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
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

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
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

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@OneToMany(targetEntity=TraTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="deviceTbl")
	private Set<TraTbl> tra;

	public Set<TraTbl> getTra() {
		return tra;
	}

	public void setTra(Set<TraTbl> tra) {
		this.tra = tra;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@OneToMany(targetEntity=TrsTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="deviceTbl")
	private Set<TrsTbl> trs;

	public Set<TraTbl> getTrs() {
		return tra;
	}

	public void setTrs(Set<TrsTbl> trs) {
		this.trs = trs;
	}

}
package kr.co.d2net.dto.vo;

import java.util.Date;

public class Equip{

	private String tmpDeviceId;	//장비ID
	private String deviceId;	//장비ID
	private String deviceNm;	//장비이름
	private String deviceIp;	//장비IP
	private Integer devicePort;	//장비Port
	private String useYn;	//사용여부
	private Date regDt;	//등록일시
	private String regrId;	//등록자ID
	private Date modDt;	//수정일시
	private String modrId;	//수정자ID
	private Long ctiId;	//콘텐츠인스턴스ID
	private String workStatcd;	//장비상태
	private String equipKinds;	// 장비선택
	private String equipAdd;	// 장비추가,인스턴스추가 선택
	private Integer instanceAdd;	//인스턴스 추가장비
	private Integer deviceNum;	//인스턴스 추가장비
	
	
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
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
	public String getEquipKinds() {
		return equipKinds;
	}
	public void setEquipKinds(String equipKinds) {
		this.equipKinds = equipKinds;
	}
	public String getEquipAdd() {
		return equipAdd;
	}
	public void setEquipAdd(String equipAdd) {
		this.equipAdd = equipAdd;
	}
	public Integer getInstanceAdd() {
		return instanceAdd;
	}
	public void setInstanceAdd(Integer instanceAdd) {
		this.instanceAdd = instanceAdd;
	}
	public String getTmpDeviceId() {
		return tmpDeviceId;
	}
	public void setTmpDeviceId(String tmpDeviceId) {
		this.tmpDeviceId = tmpDeviceId;
	}

}

package kr.co.d2net.dto.vo;


public class Monitoring  {

	private String  deviceId;	//장비id
	private String  deviceClfCd;	//장비 종류코드
	private String  deviceNm;	//장비명
	private String  deviceIp;	//장비ip
	private String  workStatCd;	//작업상태
	private String ctNm;	//콘텐츠명
	private Integer prgrs;	//진행률
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceClfCd() {
		return deviceClfCd;
	}
	public void setDeviceClfCd(String deviceClfCd) {
		this.deviceClfCd = deviceClfCd;
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
	public String getWorkStatCd() {
		return workStatCd;
	}
	public void setWorkStatCd(String workStatCd) {
		this.workStatCd = workStatCd;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	public Integer getPrgrs() {
		return prgrs;
	}
	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

}

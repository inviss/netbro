package kr.co.d2net.dto.vo;

import java.io.Serializable;

public class Monitoring implements Serializable{

	public Monitoring() {}

	private static final long serialVersionUID = 1787599149394060846L;

	private String  deviceId;	//장비번호
	private String  deviceClfCd;	//장비번호
	private String  deviceNm;	//장비번호
	private String  deviceIp;	//장비번호
	private String  workStatCd;	//장비번호
	private String ctNm;	//콘텐츠명
	
	private Integer prgrs;
	
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

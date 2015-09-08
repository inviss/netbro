package kr.co.d2net.dto.vo;

import java.util.Date;

public class BusiPartner {
	
	private Long busiPartnerId;	//사업자ID
	private String regrId;	//수정자ID
	private Date regDt;	//수정일시
	private String modrId;	//등록자ID
	private Date modDt;	//등록일시
	private String password;	//패스워드
	private String company;	//업체명
	private String servYn;	//사용여부
	private String ftpServYn;	//	FTP 서비스 여부
	private String ip;	//아이피
	private String port;	//포트
	private String transMethod;	//전송방식
	private String remoteDir;	//전송 타겟 디렉토리
	private String ftpId;	//FTP ID
	private String srvUrl;	//프로그램코드
	private String tmpBusiPartnerId;
	
	
	
	public Long getBusiPartnerId() {
		return busiPartnerId;
	}
	public void setBusiPartnerId(Long busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}
	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getServYn() {
		return servYn;
	}
	public void setServYn(String servYn) {
		this.servYn = servYn;
	}
	public String getFtpServYn() {
		return ftpServYn;
	}
	public void setFtpServYn(String ftpServYn) {
		this.ftpServYn = ftpServYn;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getTransMethod() {
		return transMethod;
	}
	public void setTransMethod(String transMethod) {
		this.transMethod = transMethod;
	}
	public String getRemoteDir() {
		return remoteDir;
	}
	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	public String getFtpId() {
		return ftpId;
	}
	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
	}
	public String getSrvUrl() {
		return srvUrl;
	}
	public void setSrvUrl(String srvUrl) {
		this.srvUrl = srvUrl;
	}
	public String getTmpBusiPartnerId() {
		return tmpBusiPartnerId;
	}
	public void setTmpBusiPartnerId(String tmpBusiPartnerId) {
		this.tmpBusiPartnerId = tmpBusiPartnerId;
	}

}

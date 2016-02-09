package kr.co.d2net.dto.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {
	
	@JsonProperty("ct_id")
	private Long ctId;	//콘텐츠영상ID
	
	@JsonProperty("priority")	
	private String priority;	//우선순위
	
	@JsonProperty("retry_cnt")	
	private Integer retryCnt;	//재전송횟수
	
	@JsonProperty("seq")
	private Long seq;	//순번
	
	@JsonProperty("work_statcd")
	private String workStatcd;	//작업상태
	
	@JsonProperty("ct_nm")
	private String ctNm;	//콘텐츠명
	
	@JsonProperty("cti_id")
	private Long ctiId;	//콘텐츠인스트ID
	
	@JsonProperty("fl_path")
	private String flPath;	//파일경로
	
	@JsonProperty("org_file_nm")
	private String orgFileNm; //원본파일명
	
	@JsonProperty("wrk_file_nm")
	private String wrkFileNm;	//작업파일명
	
	@JsonProperty("fl_ext")
	private String flExt;	//파일확장자
	
	@JsonProperty("pro_flnm")
	private String proFlnm;	//프로파일명
	
	@JsonProperty("category_nm")
	private String categoryNm;	//카테고리명
	
	@JsonProperty("episode_nm")
	private String episodeNm;	//에피소드명
	
	@JsonProperty("brd_dd")
	private Date brdDd;	//생성일자
	
	@JsonProperty("ct_leng")
	private String ctLeng;	//영상길이
	
	@JsonProperty("vdo_bit_rate")
	private String vdoBitRate;	//비디오비트레이트
	
	@JsonProperty("company")
	private String company;	//업체명
	
	@JsonProperty("user_id")
	private String ftpId;	// FTP ID
	
	@JsonProperty("ftp_ip")
	private String ip;	//FTP IP
	
	@JsonProperty("ftp_port")
	private Integer port;	//FTP PORT
	
	@JsonProperty("user_pass")
	private String password;	//passwd
	
	@JsonProperty("remote_dir")
	private String remoteDir;	//전송타겟지
	
	@JsonProperty("method")
	private String method;	//전송방법
	
	@JsonProperty("prgrs")
	private Integer prgrs;	//진행률
	
	@JsonProperty("device_id")
	private String deviceId;	//장비ID
	
	
	
	public Integer getPrgrs() {
		return prgrs;
	}

	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(Integer retryCnt) {
		this.retryCnt = retryCnt;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getWorkStatcd() {
		return workStatcd;
	}

	public void setWorkStatcd(String workStatcd) {
		this.workStatcd = workStatcd;
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}

	public String getOrgFileNm() {
		return orgFileNm;
	}

	public void setOrgFileNm(String orgFileNm) {
		this.orgFileNm = orgFileNm;
	}

	public String getWrkFileNm() {
		return wrkFileNm;
	}

	public void setWrkFileNm(String wrkFileNm) {
		this.wrkFileNm = wrkFileNm;
	}

	public String getFlExt() {
		return flExt;
	}

	public void setFlExt(String flExt) {
		this.flExt = flExt;
	}

	public String getProFlnm() {
		return proFlnm;
	}

	public void setProFlnm(String proFlnm) {
		this.proFlnm = proFlnm;
	}

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}

	public String getEpisodeNm() {
		return episodeNm;
	}

	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}

	public Date getBrdDd() {
		return brdDd;
	}

	public void setBrdDd(Date brdDd) {
		this.brdDd = brdDd;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}

	public String getVdoBitRate() {
		return vdoBitRate;
	}

	public void setVdoBitRate(String vdoBitRate) {
		this.vdoBitRate = vdoBitRate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFtpId() {
		return ftpId;
	}

	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	
}

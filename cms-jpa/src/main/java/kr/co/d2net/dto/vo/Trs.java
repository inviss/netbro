package kr.co.d2net.dto.vo;

import java.util.Date;

public class Trs{

	private Long ctId;	//컨텐츠ID
	private String priority;	//우선순위
	private Integer retryCnt;	//재전송횟수
	private Long seq;	//순번
	private String workStatcd;	//작업상태
	private String ctNm;	//영상이름
	private Long ctiId;	//콘텐츠인스트Id
	private String flPath;	//파일경로
	private String orgFileNm;	//원본파일명
	private String wrkFileNm;	//작업파일명
	private String flExt;	//영상확장자
	private String proFlnm;	//프로파일명
	private String categoryNm;	//카테고리명
	private String episodeNm;	//에피소드명
	private Date brdDd;	//생성일자
	private String ctLeng;	//영상길이
	private String vdoBitRate;	//영상비트레이트
	private String company;	//FTP 업체명
	private String ftpId;	//FTP id
	private String ip;	//FTP IP
	private Integer port;	//FTP PORT
	private String password;	//FTP PASSWORD
	private String remoteDir;	//FTP 타격지
	private String method;	// 전송방식
	private Integer prgrs;	//진행률
	private Date regDt;	//등록일자
	private Date reqDt;	//수정일
	private Date modDt;	//수정일자
	private Date trsStrDt;	//전송시작시간
	private Date trsEndDt;	//전송종료시간
	private String deviceId;	//장비ID
	
	
	public Date getTrsStrDt() {
		return trsStrDt;
	}
	public void setTrsStrDt(Date trsStrDt) {
		this.trsStrDt = trsStrDt;
	}
	public Date getTrsEndDt() {
		return trsEndDt;
	}
	public void setTrsEndDt(Date trsEndDt) {
		this.trsEndDt = trsEndDt;
	}
	public Date getReqDt() {
		return reqDt;
	}
	public void setReqDt(Date reqDt) {
		this.reqDt = reqDt;
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
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
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
}

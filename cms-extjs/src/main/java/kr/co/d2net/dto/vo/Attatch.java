package kr.co.d2net.dto.vo;

import java.util.Date;

public class Attatch {

	private Integer fid;
	private String realNm;
	private String currNm;
	private String ext;
	private String loc;
	private Long size;
	private Date regDtm;
	private String useYn;
	
	private Long seq;	//키id
	private Long ctId;	//영상id
	private String orgFilenm;	//원본파일명
	private String transFilenm;	//변환파일명
	private String attachType;	//첨부파일 유형
	private String flPath;	//파일경로
	private Date regDt;	//등록일
	private String regrId;	//등록자
	
	
	
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public String getOrgFilenm() {
		return orgFilenm;
	}
	public void setOrgFilenm(String orgFilenm) {
		this.orgFilenm = orgFilenm;
	}
	public String getTransFilenm() {
		return transFilenm;
	}
	public void setTransFilenm(String transFilenm) {
		this.transFilenm = transFilenm;
	}
	public String getAttachType() {
		return attachType;
	}
	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}
	public String getFlPath() {
		return flPath;
	}
	public void setFlPath(String flPath) {
		this.flPath = flPath;
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
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getRealNm() {
		return realNm;
	}
	public void setRealNm(String realNm) {
		this.realNm = realNm;
	}
	public String getCurrNm() {
		return currNm;
	}
	public void setCurrNm(String currNm) {
		this.currNm = currNm;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Date getRegDtm() {
		return regDtm;
	}
	public void setRegDtm(Date regDtm) {
		this.regDtm = regDtm;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

}

package kr.co.d2net.dto;

import java.util.Date;

public class Attatch {

	private static final long serialVersionUID = 8358640782956784919L;
	
	private Integer fid;
	private String realNm;
	private String currNm;
	private String ext;
	private String loc;
	private Long size;
	private Date regDtm;
	private String useYn;
	
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

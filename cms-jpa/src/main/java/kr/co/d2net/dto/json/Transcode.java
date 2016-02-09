package kr.co.d2net.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transcode {
	
	@JsonProperty("seq")
	private Long seq;	//순번
	@JsonProperty("ct_id")
	private Long ctId;	//콘텐츠인스트I
	@JsonProperty("source_path")
	private String sourceFlPath;	//경로
	@JsonProperty("source_fl_nm")
	private String sourceFlNm;	//파일명
	@JsonProperty("source_fl_ext")
	private String sourceFlExt;	//파일확장자
	@JsonProperty("target_fl_nm")
	private String targetFlNm;	//타겟지파일명
	@JsonProperty("target_fl_ext")
	private String targetFlExt;	//타겟지파일확장자
	@JsonProperty("rp_img_kfrm_seq")
	private Long rpImgKfrmSeq;	//대표이미지순번
	@JsonProperty("device_id")
	private String deviceId;	//장비ID
	@JsonProperty("progress")
	private Integer progress;	//진행률
	
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	public Long getRpImgKfrmSeq() {
		return rpImgKfrmSeq;
	}
	public void setRpImgKfrmSeq(Long rpImgKfrmSeq) {
		this.rpImgKfrmSeq = rpImgKfrmSeq;
	}
	public String getSourceFlNm() {
		return sourceFlNm;
	}
	public void setSourceFlNm(String sourceFlNm) {
		this.sourceFlNm = sourceFlNm;
	}
	public String getSourceFlExt() {
		return sourceFlExt;
	}
	public void setSourceFlExt(String sourceFlExt) {
		this.sourceFlExt = sourceFlExt;
	}
	public String getTargetFlNm() {
		return targetFlNm;
	}
	public void setTargetFlNm(String targetFlNm) {
		this.targetFlNm = targetFlNm;
	}
	public String getTargetFlExt() {
		return targetFlExt;
	}
	public void setTargetFlExt(String targetFlExt) {
		this.targetFlExt = targetFlExt;
	}
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
	public String getSourceFlPath() {
		return sourceFlPath;
	}
	public void setSourceFlPath(String sourceFlPath) {
		this.sourceFlPath = sourceFlPath;
	}
	
	
}

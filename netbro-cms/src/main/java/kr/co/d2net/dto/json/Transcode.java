package kr.co.d2net.dto.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Transcode {
	
	@JsonProperty("seq")
	private Long seq;
	@JsonProperty("ct_id")
	private Long ctId;
	@JsonProperty("source_path")
	private String sourceFlPath;
	@JsonProperty("source_fl_nm")
	private String sourceFlNm;
	@JsonProperty("source_fl_ext")
	private String sourceFlExt;
	@JsonProperty("target_fl_nm")
	private String targetFlNm;
	@JsonProperty("target_fl_ext")
	private String targetFlExt;
	@JsonProperty("rp_img_kfrm_seq")
	private Long rpImgKfrmSeq;
	@JsonProperty("device_id")
	private String deviceId;
	@JsonProperty("progress")
	private Integer progress;
	
	
	
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

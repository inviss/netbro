package kr.co.d2net.dto.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Status {
	@JsonProperty("seq")
	private Long seq;	//순번
	@JsonProperty("progress")
	private Integer progress;	//진행률
	@JsonProperty("work_statcd")
	private String workStatcd;	//작업상태코드
	@JsonProperty("device_id")
	private String deviceId;	//장비ID
	@JsonProperty("device_num")
	private Integer deviceNum;	//장비번호
	@JsonProperty("device_state")
	private String deviceState; //장비상태(W=wait, B=busy)
	@JsonProperty("error_cd")
	private String errorCd;	//에러코드

	
	public String getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}

	public Integer getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

}

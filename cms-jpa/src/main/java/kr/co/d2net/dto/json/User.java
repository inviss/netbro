package kr.co.d2net.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	@JsonProperty("user_id")
	private String userId;	//사용자ID
	@JsonProperty("user_pass")
	private String userPass;	//사용자Passwd
	@JsonProperty("auth_id")
	private Integer[] authIds;	//권한ID
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public Integer[] getAuthIds() {
		return authIds;
	}
	public void setAuthIds(Integer[] authIds) {
		this.authIds = authIds;
	}
	
}

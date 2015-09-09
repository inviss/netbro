package kr.co.d2net.dto.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class User {
	
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("user_pass")
	private String userPass;
	@JsonProperty("auth_id")
	private Integer[] authIds;
	
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

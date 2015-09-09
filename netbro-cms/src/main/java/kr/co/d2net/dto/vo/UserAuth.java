package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class UserAuth implements Serializable{

	public UserAuth() {}

	private static final long serialVersionUID = 1787599149394060846L;

	private String  userId;	//사용자ID
	private Integer authId;	//사용자명
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getAuthId() {
		return authId;
	}
	public void setAuthId(Integer authId) {
		this.authId = authId;
	}
	
}

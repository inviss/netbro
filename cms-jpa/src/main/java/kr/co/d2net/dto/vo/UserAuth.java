package kr.co.d2net.dto.vo;


public class UserAuth{

	private String  userId;	//사용자ID
	private Integer authId;	//권한ID

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

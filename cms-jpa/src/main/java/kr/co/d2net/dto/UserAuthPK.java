package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class UserAuthPK implements Serializable {
	
	@BusinessKey
	private Integer authId;

	@BusinessKey
	private String userId;

	public UserAuthPK() {}
	public UserAuthPK(Integer authId, String userId) {
		this.authId = authId;
		this.userId = userId;
	}
	
	@Override
	public boolean equals(Object obj) {
        return BeanUtils.equals(this, obj);
    }
	@Override
    public int hashCode() {
        return BeanUtils.hashCode(this);
    }
	@Override
    public String toString() {
        return BeanUtils.toString(this);
    }
}

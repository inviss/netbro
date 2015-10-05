package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class RoleAuthPK implements Serializable{

	@BusinessKey
	private Integer menuId;	//회차ID
	
	@BusinessKey
	private Integer authId;	//회차ID
	
	public RoleAuthPK() {}
	
	public RoleAuthPK(Integer menuId, Integer authId ) {
		this.menuId = menuId;
		this.authId = authId; 
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

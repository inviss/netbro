package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class CodePK implements Serializable {
	
	@BusinessKey
	private String clfCd;	//구분코드
	
	@BusinessKey
	private String sclCd;	//구분상세코드
	
	public CodePK() {}
	public CodePK(String clfCd, String sclCd) {
		this.clfCd = clfCd;
		this.sclCd = sclCd;
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

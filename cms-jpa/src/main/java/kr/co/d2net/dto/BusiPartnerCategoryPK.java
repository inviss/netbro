package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class BusiPartnerCategoryPK implements Serializable {
	
	@BusinessKey
	private Integer categoryId;	//구분코드
	
	@BusinessKey
	private String ctTyp;	//구분상세코드
	
	public BusiPartnerCategoryPK() {}
	public BusiPartnerCategoryPK(Integer categoryId, String ctTyp) {
		this.categoryId = categoryId;
		this.ctTyp = ctTyp;
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

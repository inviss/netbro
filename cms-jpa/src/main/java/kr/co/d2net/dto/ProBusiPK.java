package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class ProBusiPK implements Serializable {
	
	@BusinessKey
	private Long busiPartnerId;	//사업자ID
	
	@BusinessKey
	private Long proFlId;			//프로파일ID
	
	public ProBusiPK() {}
	public ProBusiPK(Long busiPartnerId, Long proFlId) {
		this.busiPartnerId = busiPartnerId;
		this.proFlId = proFlId;
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

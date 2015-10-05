package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class ContentsInstPK implements Serializable {
	
	@BusinessKey
	private Integer ctId;	//영상id
	
	@BusinessKey
	private Integer ctiId;	//영상 인스턴스 id
	
	public ContentsInstPK() {}
	public ContentsInstPK(Integer ctId, Integer ctiId) {
		this.ctiId = ctiId;
		this.ctId = ctId;
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

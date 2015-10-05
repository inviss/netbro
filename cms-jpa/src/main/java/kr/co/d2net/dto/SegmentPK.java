package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class SegmentPK implements Serializable {
	
	@BusinessKey
	private Integer segmentId;	//세그먼트ID
	
	@BusinessKey
	private Integer episodeId;	//회차ID
	
	@BusinessKey
	private Integer categoryId;
	
	
	public SegmentPK() {}
	public SegmentPK(Integer segmentId, Integer categoryId, Integer episodeId) {
		this.segmentId = segmentId;
		this.categoryId = categoryId;
		this.episodeId = episodeId;
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

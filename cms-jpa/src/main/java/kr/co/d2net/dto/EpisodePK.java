package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class EpisodePK implements Serializable {
	
	@BusinessKey
	private Integer episodeId;

	@BusinessKey
	private Integer categoryId;

	public EpisodePK() {}

	public EpisodePK(Integer episodeId, Integer categoryId) {
		this.episodeId = episodeId;
		this.categoryId = categoryId;
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

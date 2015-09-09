package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class Episode implements Serializable{
	
	public Episode() {}
	
	
	private static final long serialVersionUID = 1787599149394060846L;

	Integer categoryId=0;
	Integer episodeId=0;
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getEpisodeId() {
		return episodeId;
	}
	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}
	
}

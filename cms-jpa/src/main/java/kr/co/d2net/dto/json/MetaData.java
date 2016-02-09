package kr.co.d2net.dto.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaData {
	
	@JsonProperty("category_id")
	private Integer categoryId;
	@JsonProperty("category_nm")
	private String categoryNm;
	@JsonProperty("episode_id")
	private Integer episodeId;
	@JsonProperty("episode_nm")
	private String episodeNm;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryNm() {
		return categoryNm;
	}
	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}
	public Integer getEpisodeId() {
		return episodeId;
	}
	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}
	public String getEpisodeNm() {
		return episodeNm;
	}
	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}
	
	@JsonProperty("ctDatas")
	private List<CtData> ctDatas = new ArrayList<CtData>();

	public List<CtData> getCtDatas() {
		return ctDatas;
	}
	public void setCtDatas(List<CtData> ctDatas) {
		this.ctDatas = ctDatas;
	}
	public void addCtData(CtData ctData) {
		ctDatas.add(ctData);
	}
	
	@JsonProperty("codes")
	private List<CommonMeta> commonMetas = new ArrayList<CommonMeta>();

	public List<CommonMeta> getCommonMetas() {
		return commonMetas;
	}
	public void setCommonMetas(List<CommonMeta> commonMetas) {
		this.commonMetas = commonMetas;
	}
	public void addCommonMeta(CommonMeta commonMeta) {
		commonMetas.add(commonMeta);
	}
	
}

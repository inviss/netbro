package kr.co.d2net.dto.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class CtData {
	
	@JsonProperty("ct_id")
	private Long ctId;
	@JsonProperty("ct_nm")
	private String ctNm;
	@JsonProperty("ct_cla")
	private String ctCla;
	@JsonProperty("ct_typ")
	private String ctTyp;
	@JsonProperty("ct_cla_nm")
	private String ctClaNm;
	@JsonProperty("ct_typ_nm")
	private String ctTypNm;
	@JsonProperty("rist_clf_cd")
	private String ristClfCd;
	@JsonProperty("rist_clf_cd_nm")
	private String ristClfCdNm;
	@JsonProperty("file_path")
	private String filePath;
	@JsonProperty("file_nm")
	private String fileNm;
	@JsonProperty("rp_img_no")
	private Long rpImgNo;
	@JsonProperty("category_id")
	private Integer categoryId;
	@JsonProperty("category_nm")
	private String categoryNm;
	@JsonProperty("episode_id")
	private Integer episodeId;
	@JsonProperty("episode_nm")
	private String episodeNm;
	@JsonProperty("ct_leng")
	private String ctLeng;
	@JsonProperty("data_stat_cd")
	private String dataStatCd;
	@JsonProperty("spc_info")
	private String spcInfo;
	@JsonProperty("file_ext")
	private String fileExt;
	
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getCtLeng() {
		return ctLeng;
	}
	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}
	public String getDataStatCd() {
		return dataStatCd;
	}
	public void setDataStatCd(String dataStatCd) {
		this.dataStatCd = dataStatCd;
	}
	public String getSpcInfo() {
		return spcInfo;
	}
	public void setSpcInfo(String spcInfo) {
		this.spcInfo = spcInfo;
	}
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public Long getRpImgNo() {
		return rpImgNo;
	}
	public void setRpImgNo(Long rpImgNo) {
		this.rpImgNo = rpImgNo;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	public String getCtCla() {
		return ctCla;
	}
	public void setCtCla(String ctCla) {
		this.ctCla = ctCla;
	}
	public String getCtTyp() {
		return ctTyp;
	}
	public void setCtTyp(String ctTyp) {
		this.ctTyp = ctTyp;
	}
	public String getCtClaNm() {
		return ctClaNm;
	}
	public void setCtClaNm(String ctClaNm) {
		this.ctClaNm = ctClaNm;
	}
	public String getCtTypNm() {
		return ctTypNm;
	}
	public void setCtTypNm(String ctTypNm) {
		this.ctTypNm = ctTypNm;
	}
	public String getRistClfCd() {
		return ristClfCd;
	}
	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}
	public String getRistClfCdNm() {
		return ristClfCdNm;
	}
	public void setRistClfCdNm(String ristClfCdNm) {
		this.ristClfCdNm = ristClfCdNm;
	}
	
}


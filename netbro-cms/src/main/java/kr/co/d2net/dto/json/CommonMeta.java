package kr.co.d2net.dto.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class CommonMeta {
	
	@JsonProperty("clf_cd")
	private String clfCd;
	@JsonProperty("scl_cd")
	private String sclCd;
	@JsonProperty("clf_nm")
	private String clfNm;
	@JsonProperty("scl_nm")
	private String sclNm;
	
	public String getClfCd() {
		return clfCd;
	}
	public void setClfCd(String clfCd) {
		this.clfCd = clfCd;
	}
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getClfNm() {
		return clfNm;
	}
	public void setClfNm(String clfNm) {
		this.clfNm = clfNm;
	}
	public String getSclNm() {
		return sclNm;
	}
	public void setSclNm(String sclNm) {
		this.sclNm = sclNm;
	}
	
	
}

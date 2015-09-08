package kr.co.d2net.dto.vo;

import java.util.Date;

public class Auth {
	
	private Integer authId;		//권한ID	
	private String authNm;	//권한명	
	private String authSubNm;	//권한서브명
	private String regrId;	//등록자ID	
	private Date regDt;//등록일시
	private String modrId;	//수정자ID
	private Date modDt;//수정일시
	private String useYn;	//사용여부
	
	
	public Integer getAuthId() {
		return authId;
	}
	public void setAuthId(Integer authId) {
		this.authId = authId;
	}
	public String getAuthNm() {
		return authNm;
	}
	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}
	public String getAuthSubNm() {
		return authSubNm;
	}
	public void setAuthSubNm(String authSubNm) {
		this.authSubNm = authSubNm;
	}
	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
}

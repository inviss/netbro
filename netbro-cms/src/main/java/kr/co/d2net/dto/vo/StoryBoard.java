package kr.co.d2net.dto.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class StoryBoard implements Serializable{
	
	public StoryBoard() {}

	private String deleteImgs;
	private Long rpImg;
	private Long ctId;
	private String flPath;
	private String dividImgs;
	private String cnNm;
	private String cnCont;
	private String modrId;
	
	
	
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public String getCnNm() {
		return cnNm;
	}
	public void setCnNm(String cnNm) {
		this.cnNm = cnNm;
	}
	public String getCnCont() {
		return cnCont;
	}
	public void setCnCont(String cnCont) {
		this.cnCont = cnCont;
	}
	public String getDividImgs() {
		return dividImgs;
	}
	public void setDividImgs(String dividImgs) {
		this.dividImgs = dividImgs;
	}
	public String getFlPath() {
		return flPath;
	}
	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}
	public String getDeleteImgs() {
		return deleteImgs;
	}
	public void setDeleteImgs(String deleteImgs) {
		this.deleteImgs = deleteImgs;
	}
	public Long getRpImg() {
		return rpImg;
	}
	public void setRpImg(Long rpImg) {
		this.rpImg = rpImg;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	
	
}

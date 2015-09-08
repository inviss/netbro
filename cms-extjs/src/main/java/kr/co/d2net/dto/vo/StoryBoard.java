package kr.co.d2net.dto.vo;


/**
 * @author Administrator
 *
 */
public class StoryBoard  {
	 

	private String deleteImgs;//삭제이미지들 ,로 구분한다
	private Long rpImg; // 대표화면 이미지
	private Long ctId;//영상id
	private String flPath;//파일경로
	private String dividImgs;//코너분할 대상 이미지 ,로 구분한다
	private String cnNm;//코너명
	private String cnCont;//코너내용
	private String modrId;//수정자id
	private Long img;//이미지파일명
	private Long duration;//duration
	private String ctLeng;//영상길이
	private String url;//경로
	private String rpimgYn;//대표화면여부
	private String showImgs;//스토리보드에 뿌려질 이미지모음 ,로 구분한다
	
	public String getShowImgs() {
		return showImgs;
	}
	public void setShowImgs(String showImgs) {
		this.showImgs = showImgs;
	}
	public String getRpimgYn() {
		return rpimgYn;
	}
	public void setRpimgYn(String rpimgYn) {
		this.rpimgYn = rpimgYn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getImg() {
		return img;
	}
	public void setImg(Long img) {
		this.img = img;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getCtLeng() {
		return ctLeng;
	}
	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}
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

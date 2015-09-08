package kr.co.d2net.dto.vo;


public class Media {
	
	private Long ctId;	//콘텐츠ID
	private Long ctiId;	//콘텐츠ID
	private String ctiFmt;	//콘텐츠인스턴스포맷코드
	private String flPath;	//파일경로
	private String flExt;	//파일확장명
	private String frmPerSec;	//프레임
	private Integer vdHresol; //세로해상도
	private Integer vdVresol; //가로해상도
	private String aspRtoCd; //화면비
	private String vdQlty; //화질
	private String ctLeng; //영상길이
	
	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public String getCtiFmt() {
		return ctiFmt;
	}

	public void setCtiFmt(String ctiFmt) {
		this.ctiFmt = ctiFmt;
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}

	public String getFlExt() {
		return flExt;
	}

	public void setFlExt(String flExt) {
		this.flExt = flExt;
	}

	public String getFrmPerSec() {
		return frmPerSec;
	}

	public void setFrmPerSec(String frmPerSec) {
		this.frmPerSec = frmPerSec;
	}

 
	public Integer getVdHresol() {
		return vdHresol;
	}

	public void setVdHresol(Integer vdHresol) {
		this.vdHresol = vdHresol;
	}

	public Integer getVdVresol() {
		return vdVresol;
	}

	public void setVdVresol(Integer vdVresol) {
		this.vdVresol = vdVresol;
	}

	public String getAspRtoCd() {
		return aspRtoCd;
	}

	public void setAspRtoCd(String aspRtoCd) {
		this.aspRtoCd = aspRtoCd;
	}

	public String getVdQlty() {
		return vdQlty;
	}

	public void setVdQlty(String vdQlty) {
		this.vdQlty = vdQlty;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}
	
	
	
}

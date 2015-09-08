package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.co.d2net.xml.adapter.DateAdapter;
import kr.co.d2net.xml.adapter.DatetimeAdapter;
import kr.co.d2net.xml.adapter.UrlEncodeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

public class ContentsSearchView extends BaseObject{
	@Column(name="BRD_DD", nullable=true)
	private Date brdDd;	//방송일자
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시
	@Column(name="CT_ID", length=16)
	private Long ctId;	//컨텐츠ID
	@Column(name="CT_NM", length=300, nullable=true)
	private String ctNm;	//컨텐츠명
	@Column(name="VD_QLTY", length=5, nullable=true)
	private String vdQlty;	//화질코드
	@Column(name="DEL_DD", nullable=true)
	private Date delDd;	//삭제일자
	@Column(name="VD_VRESOL", length=8, nullable=true)
	private Integer vdVresol;
	@Column(name="VD_HRESOL", length=8, nullable=true)
	private Integer vdHresol;
	@Column(name="FL_PATH", length=256, nullable=true)
	private String flPath;
	@Column(name="WRK_FILE_NM", length=256, nullable=true)
	private String wrkFileNm;
	@Column(name="CATEGORY_ID", nullable=true)
	private Integer categoryId;	//카테고리id
	@Column(name="CTI_FMT", length=5, nullable=false)
	private String ctiFmt;	//콘텐츠인스턴스포맷코드
	
	public Date getBrdDd() {
		return brdDd;
	}
	public void setBrdDd(Date brdDd) {
		this.brdDd = brdDd;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
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
	public String getVdQlty() {
		return vdQlty;
	}
	public void setVdQlty(String vdQlty) {
		this.vdQlty = vdQlty;
	}
	public Date getDelDd() {
		return delDd;
	}
	public void setDelDd(Date delDd) {
		this.delDd = delDd;
	}
	public Integer getVdVresol() {
		return vdVresol;
	}
	public void setVdVresol(Integer vdVresol) {
		this.vdVresol = vdVresol;
	}
	public Integer getVdHresol() {
		return vdHresol;
	}
	public void setVdHresol(Integer vdHresol) {
		this.vdHresol = vdHresol;
	}
	public String getFlPath() {
		return flPath;
	}
	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}
	public String getWrkFileNm() {
		return wrkFileNm;
	}
	public void setWrkFileNm(String wrkFileNm) {
		this.wrkFileNm = wrkFileNm;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCtiFmt() {
		return ctiFmt;
	}
	public void setCtiFmt(String ctiFmt) {
		this.ctiFmt = ctiFmt;
	}
	
	
}

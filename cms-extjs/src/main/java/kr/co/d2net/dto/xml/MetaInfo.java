package kr.co.d2net.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="info")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaInfo {
	
	@XmlElement(name="eq_id")
	private String eqId;//장비id
	
	@XmlElement(name="eq_channel")
	private String eqChannel;//장비채널
	
	@XmlElement(name="status")
	private String status;//작업상태
	
	@XmlElement(name="content")
	private Integer prgrs;//진행률
	
	@XmlElement(name="regrid")
	private String regrid;//등록자id

	public String getEqId() {
		return eqId;
	}
	public void setEqId(String eqId) {
		this.eqId = eqId;
	}

	public String getEqChannel() {
		return eqChannel;
	}
	public void setEqChannel(String eqChannel) {
		this.eqChannel = eqChannel;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPrgrs() {
		return prgrs;
	}
	public void setPrgrs(Integer prgrs) {
		this.prgrs = prgrs;
	}

	public String getRegrid() {
		return regrid;
	}
	public void setRegrid(String regrid) {
		this.regrid = regrid;
	}
	
}

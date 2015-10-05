package kr.co.d2net.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="web")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebRoot {
	
	@XmlAttribute(name="version")
	private String version = "1.0";
	
	@XmlAttribute(name="copy_writer")
	private String writer = "d2net.co.kr";
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
}

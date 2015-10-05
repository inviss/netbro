package kr.co.d2net.dto.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;

@XmlRootElement(name="metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata {

	@XmlElement(name="contents")
	private ContentsTbl contentsTbl;
	public ContentsTbl getContentsTbl() {
		return contentsTbl;
	}
	public void setContentsTbl(ContentsTbl contentsTbl) {
		this.contentsTbl = contentsTbl;
	}
	
	@XmlElement(name="contents_inst")
	private List<ContentsInstTbl> contentsInstTbls = new ArrayList<ContentsInstTbl>();

	public List<ContentsInstTbl> getContentsInstTbls() {
		return contentsInstTbls;
	}
	public void setContentsInstTbls(List<ContentsInstTbl> contentsInstTbls) {
		this.contentsInstTbls = contentsInstTbls;
	}
	public void addContentsInstTbl(ContentsInstTbl contentsInstTbl) {
		contentsInstTbls.add(contentsInstTbl);
	}
	
}

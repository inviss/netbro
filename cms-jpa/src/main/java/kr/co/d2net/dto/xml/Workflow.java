package kr.co.d2net.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class Workflow {
	
	@XmlElement(name="info")
	private MetaInfo metaInfo;
	
	public MetaInfo getMetaInfo() {
		return metaInfo;
	}
	public void setMetaInfo(MetaInfo metaInfo) {
		this.metaInfo = metaInfo;
	}
	
	@XmlElement(name="metadata")
	private Metadata metadata;
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	
}

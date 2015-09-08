package kr.co.d2net.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="episode")
@XmlAccessorType(XmlAccessType.FIELD)
public class Episode {
	@XmlAttribute(name="episode_id")
	private Integer episodeId;//에피소드id
	@XmlAttribute(name="episode_nm")
	private String episodeNm;//에피소드명
	
	public Integer getEpisodeId() {
		return episodeId;
	}
	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}
	public String getEpisodeNm() {
		return episodeNm;
	}
	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}
	
}

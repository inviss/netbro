package kr.co.d2net.dto.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class Categories {
	
	@XmlElement(name="category")
	private List<Category> categories  = new ArrayList<Category>();
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	@XmlElement(name="episode")
	private List<Episode> episodes  = new ArrayList<Episode>();
	public List<Episode> getEpisodes() {
		return episodes;
	}
	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}
	public void addEpisode(Episode episode) {
		this.episodes.add(episode);
	}
}

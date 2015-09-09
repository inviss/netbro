package kr.co.d2net.search.index;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.Indexed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(indexName="", indexType="fields")
public class AttachField {

	@Expose
	@SerializedName("date")
	@Indexed(name="date", type="long", index="not_analyzed", store=true, includeInAll=false)
	private Long regDt;
	
	@Expose
	@SerializedName("file")
	@Indexed(name="file", type="string", index="no", store=true, includeInAll=false)
	private String file;
	
	@Expose
	@SerializedName("title")
	@Indexed(name="title", type="string", index="analyzed", analyzer="kr_analyzer", termVector=true, store=true, includeInAll=false)
	private String title;
	
	@Expose
	@SerializedName("author")
	@Indexed(name="author", type="string", index="analyzed", analyzer="kr_analyzer", termVector=true, store=true, includeInAll=false)
	private String author;
	
	@Expose
	@SerializedName("keywords")
	@Indexed(name="keywords", type="string", index="analyzed", analyzer="kr_analyzer", termVector=true, store=true, includeInAll=false)
	private String keywords;
	
	@Expose
	@SerializedName("content_type")
	@Indexed(name="content_type", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String contentType;
	
	@Expose
	@SerializedName("content_length")
	@Indexed(name="content_length", type="string", index="not_analyzed", store=true, includeInAll=false)
	private Long contentLength;

	public Long getRegDt() {
		return regDt;
	}

	public void setRegDt(Long regDt) {
		this.regDt = regDt;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	
}

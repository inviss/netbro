package kr.co.d2net.search.index;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.SubEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(indexName="", indexType="file")
public class AttatchFile {
	/*
	@Expose
	@SerializedName("type")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	*/
	@Expose
	@SerializedName("fields")
	@SubEntity(indexType="fields")
	private AttachField attachField;

	public AttachField getAttachField() {
		return attachField;
	}

	public void setAttachField(AttachField attachField) {
		this.attachField = attachField;
	}
	
}

package kr.co.d2net.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ID_GEN_TBL")
public class IdGenTbl {
	
	@Id
	@Column(name="ENTITY_NAME", length=30)
	private String entityName;
	private Long value;
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	
}

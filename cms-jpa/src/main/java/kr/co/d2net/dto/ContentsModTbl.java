package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="CONTENTS_MOD_TBL", indexes = {
		@Index(name="contents_mod_idx1", columnList="CT_ID", unique=false)
	})
public class ContentsModTbl extends BaseObject{

	public ContentsModTbl(){};
	
	public ContentsModTbl(String dataStatCd, String modId, Date modDt, Long ctId, Long seq){
		this.dataStatcd = dataStatCd;
		this.modDt = modDt;
		this.modId = modId;
		this.ctId = ctId;
		this.seq = seq;
	};
	
	
	@TableGenerator(name = "CONTENTS_MOD_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "CONTENTS_MOD_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CONTENTS_MOD_SEQ")
	@Id
	@BusinessKey
	@Column(name="SEQ", length=16)
	private Long seq;	//콘텐츠인스턴스ID

	@Column(name="MOD_ID", length=30)
	private String modId;	//정리자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", length=6)
	private Date modDt;	//정리일

	@Column(name="DATA_STAT_CD", length=3, nullable=false)
	private String dataStatcd;	//데이터 상태 코드
	@Transient
	private Long ctId;	//데이터 상태 코드

	
	
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	 
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}


	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}


	public String getDataStatcd() {
		return dataStatcd;
	}
	public void setDataStatcd(String dataStatcd) {
		this.dataStatcd = dataStatcd;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="CT_ID", updatable=false, insertable=true)
	private ContentsTbl contentsTbl;

	public ContentsTbl getContentsTbl() {
		return contentsTbl;
	}
	public void setContentsTbl(ContentsTbl contentsTbl) {
		this.contentsTbl = contentsTbl;
	}
	

}
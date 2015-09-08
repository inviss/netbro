package kr.co.d2net.dto;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name="OPT")
public class Opt extends BaseObject{

	@Id
	@BusinessKey
	@Column(name="OPT_ID", length=16)
	private Long optId;	//회차ID

	
	@Column(name="OPT_DESC", nullable=true)
	@Lob
	private String optDesc;	//옵션설명


	@Column(name="OPT_INFO", length=200, nullable=true)
	private String optInfo;	//옵션정보


	@Column(name="DEFAULT_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String defaultYn;	//디폴트여부


	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부


	public String getOptDesc() {
		return optDesc;
	}


	public void setOptDesc(String optDesc) {
		this.optDesc = optDesc;
	}


	public String getOptInfo() {
		return optInfo;
	}


	public void setOptInfo(String optInfo) {
		this.optInfo = optInfo;
	}


	public String getDefaultYn() {
		return defaultYn;
	}


	public Long getOptId() {
		return optId;
	}


	public void setOptId(Long optId) {
		this.optId = optId;
	}


	public void setDefaultYn(String defaultYn) {
		this.defaultYn = defaultYn;
	}


	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false)
	@JoinColumn(name="pro_flid", updatable=false, insertable=true)
	private ProFlTbl proFlTbl;
	
	public ProFlTbl getProFlTbl() {
		return proFlTbl;
	}

	public void setProFlTbl(ProFlTbl proFlTbl) {
		this.proFlTbl = proFlTbl;
	}


}

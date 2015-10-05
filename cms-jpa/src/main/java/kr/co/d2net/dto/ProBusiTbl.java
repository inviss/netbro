package kr.co.d2net.dto;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@IdClass(value=ProBusiPK.class)
@Table(name="PRO_BUSI_TBL")
public class ProBusiTbl{

	@Id
	@Column(name="BUSI_PARTNERID",length =30)
	private Long busiPartnerId;	//사업자ID

	@Id
	@Column(name="PRO_FLID",length =30)
	private Long proFlId;			//프로파일ID

	@Lob
	@Column(name="REMARK", nullable=true)
	private String remark;	//비고
	
	
	@Transient
	private String tmpProflId;
	
	@Transient
	private String tmpBusiPartnerId;


	public String getTmpProflId() {
		return tmpProflId;
	}

	public void setTmpProflId(String tmpProflId) {
		this.tmpProflId = tmpProflId;
	}

	public String getTmpBusiPartnerId() {
		return tmpBusiPartnerId;
	}

	public void setTmpBusiPartnerId(String tmpBusiPartnerId) {
		this.tmpBusiPartnerId = tmpBusiPartnerId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Long getBusiPartnerId() {
		return busiPartnerId;
	}

	public void setBusiPartnerId(Long busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}

	public Long getProFlId() {
		return proFlId;
	}

	public void setProFlId(Long proFlId) {
		this.proFlId = proFlId;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false)
	@JoinColumn(name="BUSI_PARTNERID", updatable=false, insertable=false)
	private BusiPartnerTbl busiPartnerTbl;

	public BusiPartnerTbl getBusiPartnerTbl() {
		return busiPartnerTbl;
	}

	public void setBusiPartnerTbl(BusiPartnerTbl busiPartnerTbl) {
		this.busiPartnerTbl = busiPartnerTbl;
	}



	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false)
	@JoinColumn(name="PRO_FLID", updatable=false, insertable=false)
	private ProFlTbl proFlTbl;

	public ProFlTbl getProFlTbl() {
		return proFlTbl;
	}

	public void setProFlTbl(ProFlTbl proFlTbl) {
		this.proFlTbl = proFlTbl;
	}

}

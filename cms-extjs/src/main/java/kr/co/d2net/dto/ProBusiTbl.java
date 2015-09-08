package kr.co.d2net.dto;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="PRO_BUSI_TBL")
public class ProBusiTbl{

	public ProBusiTbl() {}
	
	@Embeddable
	public static class ProBusiId extends BaseObject {

		public ProBusiId() {}
		
		@BusinessKey
		@Column(name="BUSI_PARTNERID",length =30)
		private Long busiPartnerId;	//사업자ID
		
		@BusinessKey
		@Column(name="PRO_FLID",length =30)
		private Long proFlId;			//프로파일ID

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
	
	}

	@EmbeddedId
	private ProBusiId id = new ProBusiId();

	public ProBusiId getId() {
		return id;
	}

	public void setId(ProBusiId id) {
		this.id = id;
	}

	@Lob
	@Column(name="REMARK", nullable=true)
	private String remark;	//비고

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(optional=false)
	@JoinColumn(name="BUSI_PARTNERID", updatable=false, insertable=false)
	private BusiPartnerTbl busiPartnerId;
	
	public BusiPartnerTbl getBusiPartnerId() {
		return busiPartnerId;
	}

	public void setBusiPartnerId(BusiPartnerTbl busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
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

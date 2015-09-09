package kr.co.d2net.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="PRO_BUSI_TBL")
public class ProBusiTbl {
	

	public ProBusiTbl() {}
	
	@Embeddable
	public static class ProBusiId extends BaseObject {

		private static final long serialVersionUID = 8621165438593537768L;
		
		public ProBusiId() {}
		
		@BusinessKey
		@Column(name="BUSI_PARTNERID",length =30)
		private String busiPartnerId;	//사업자ID
		
		@BusinessKey
		@Column(name="PRO_FLID",length =30)
		private String proFlId;			//프로파일ID

		public String getBusiPartnerId() {
			return busiPartnerId;
		}

		public void setBusiPartnerId(String busiPartnerId) {
			this.busiPartnerId = busiPartnerId;
		}

		public String getProFlId() {
			return proFlId;
		}

		public void setProFlId(String proFlId) {
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
	

	@ManyToOne(optional=false)
	@JoinColumn(name="BUSI_PARTNERID", updatable=false, insertable=false)
	private BusiPartnerTbl busiPartnerId;
	
	public BusiPartnerTbl getBusiPartnerId() {
		return busiPartnerId;
	}

	public void setBusiPartnerId(BusiPartnerTbl busiPartnerId) {
		this.busiPartnerId = busiPartnerId;
	}

	
	@ManyToOne(optional=false)
	@JoinColumn(name="PRO_FLID", updatable=false, insertable=false)
	private ProFlTbl proFlid;


	public ProFlTbl getProFlid() {
		return proFlid;
	}

	public void setProFlid(ProFlTbl proFlid) {
		this.proFlid = proFlid;
	}
	
}

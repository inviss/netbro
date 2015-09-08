package kr.co.d2net.dto.vo;

public class ProBusi {

	private Long busiPartnerId;	//사업자ID
	private Long proFlId;			//프로파일ID
	private String remark;	//비고
	
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}

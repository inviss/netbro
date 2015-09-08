package kr.co.d2net.dto.vo;


public class Storage{
	
	private Integer storageId;
	private Long totalVolume;	//전체 용량
	private Long useVolume;	//사용 용량
	private Long idleVolume;	// 가용 용량
	private Integer limit;	//임계치
	private String storagePath;	//스토리지경로
	private String storageGubun;	//스토리지구분(H:고용량,L:저용량)
	private Long volume;	//스토리지용량(통계용)
	private String partNm; //스토리지부문 명칭명(총량,사용량,허용량)
	
	 
	public Long getVolume() {
		return volume;
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public Long getIdleVolume() {
		return idleVolume;
	}
	public void setIdleVolume(Long idleVolume) {
		this.idleVolume = idleVolume;
	}
	public Integer getStorageId() {
		return storageId;
	}
	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}
	public Long getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(Long totalVolume) {
		this.totalVolume = totalVolume;
	}
	public Long getUseVolume() {
		return useVolume;
	}
	public void setUseVolume(Long useVolume) {
		this.useVolume = useVolume;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public String getStorageGubun() {
		return storageGubun;
	}
	public void setStorageGubun(String storageGubun) {
		this.storageGubun = storageGubun;
	}
	public String getPartNm() {
		return partNm;
	}
	public void setPartNm(String partNm) {
		this.partNm = partNm;
	}
	

}

package kr.co.d2net.dto;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="STORAGE_TBL")
public class StorageTbl extends BaseObject{
	
	@Id
	@BusinessKey
	@Column(name="STORAGE_ID", length=2)
	@TableGenerator(name = "STORAGE_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "STORAGE_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "STORAGE_ID_SEQ")
	private Integer storageId;		//스토리지ID	

	@Column(name="TOTAL_VOLUME", length=30, nullable=true)
	private Long totalVolume;	//전체 용량

	@Column(name="USE_VOLUME", length=30, nullable=true)
	private Long useVolume;	//사용 용량
	
	@Column(name="IDLE_VOLUME", length=30, nullable=true)
	private Long idleVolume;	// 가용 용량
	
	@Column(name="LIMIT", length=3, nullable=true)
	private Integer limit;	//임계치
	
	@Column(name="STORAGE_PATH", length=100, nullable=true)
	private String storagePath;	//스토리지경로
	
	@Column(name="STORAGE_GUBUN", length=2, nullable=true)
	private String storageGubun;	//스토리지구분(H:고용량,L:저용량)

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

	public Long getIdleVolume() {
		return idleVolume;
	}

	public void setIdleVolume(Long idleVolume) {
		this.idleVolume = idleVolume;
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

}

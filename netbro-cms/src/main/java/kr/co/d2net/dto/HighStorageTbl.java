package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="HIGH_STORAGE_TBL")
public class HighStorageTbl extends BaseObject{
	private static final long serialVersionUID = 8043949665831948354L;

	@Id
	@BusinessKey
	@Column(name="STORAGE_ID", length=2)
	private Integer storageId;		//스토리지ID	

	@Column(name="TOTAL_VOLUME", length=30, nullable=true)
	private Long totalVolume;	//전체 용량

	@Column(name="USE_VOLUME", length=30, nullable=true)
	private Long useVolume;	//사용 용량
	
	@Column(name="IDLE_VOLUME", length=30, nullable=true)
	private Long idleVolume;	// 가용 용량
	
	@Column(name="HIGH_LIMIT", length=3, nullable=true)
	private Integer highLimit;	//임계치
	
	@Column(name="STORAGE_PATH", length=100, nullable=true)
	private String storagePath;	//스토리지경로

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


	public Integer getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(Integer highLimit) {
		this.highLimit = highLimit;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	

}

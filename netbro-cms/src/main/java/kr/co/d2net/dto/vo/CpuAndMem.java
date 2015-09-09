package kr.co.d2net.dto.vo;

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

import kr.co.d2net.dto.BaseObject;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Cpu&Mem 정보를 담기 위한 beans
 * @author vayne
 *
 */
public class CpuAndMem{

	private String useCpu;		//사용 Cpu용량
	private String idleCpu;		//남은 cpu용량

	private Long useMem;		//사용 Mem 용량
	private Long idleMem;		//남은 Mem 용량
	private Long totalMem;		//전체 Mem 용량
	
	
	public String getUseCpu() {
		return useCpu;
	}
	public void setUseCpu(String useCpu) {
		this.useCpu = useCpu;
	}
	public String getIdleCpu() {
		return idleCpu;
	}
	public void setIdleCpu(String idleCpu) {
		this.idleCpu = idleCpu;
	}
	public Long getUseMem() {
		return useMem;
	}
	public void setUseMem(Long useMem) {
		this.useMem = useMem;
	}
	public Long getIdleMem() {
		return idleMem;
	}
	public void setIdleMem(Long idleMem) {
		this.idleMem = idleMem;
	}
	public Long getTotalMem() {
		return totalMem;
	}
	public void setTotalMem(Long totalMem) {
		this.totalMem = totalMem;
	}

}

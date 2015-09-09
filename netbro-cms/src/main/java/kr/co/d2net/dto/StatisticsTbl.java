package kr.co.d2net.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.googlecode.mp4parser.h264.model.ChromaFormat;

import java.util.Date;


@Entity
@Table(name="STATISTICS_TBL")
public class StatisticsTbl extends BaseObject{
	
	public StatisticsTbl() {}

	@Id
	@BusinessKey
	@TableGenerator(name = "STATISTICS_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "STATISTICS_SEQ", valueColumnName = "VALUE", initialValue = 1,allocationSize = 1)
@GeneratedValue(strategy = GenerationType.TABLE, generator = "STATISTICS_SEQ")
	@Column(name="SEQ", length=10)
	private Integer seq;	//카테고리ID
	
	
	@Column(name="CATEGORY_ID", length=10)
	private Integer categoryId;	//카테고리ID
	
	@Column(name="REG_DD",  length=6)
	private String regDd;	//등록월
	
	@Column(name="REGIST", length=16)
	private Long regist;	//등록건
	
	@Column(name="BEFORE_ARRANGE", length=16)
	private Long beforeArrange;	//전리전건
	
	@Column(name="COMPLETE_ARRANGE", length=16)
	private Long completeArrange;	//정리완료건
	
	@Column(name="ERROR", length=16)
	private Long error;	//오류
	
	
	
	@Column(name="DISCARD", length=16)
	private Long discard;	//폐기건

	@Column(name="DEPTH", length=10)
	private Integer depth;	//depth
	
	@Column(name="GROUP_ID", length=10)
	private Integer groupId;	//컨텐츠ID
	

	

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Long getError() {
		return error;
	}

	public void setError(Long error) {
		this.error = error;
	}

	
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}



	public String getRegDd() {
		return regDd;
	}

	public void setRegDd(String regDd) {
		this.regDd = regDd;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	


	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Long getRegist() {
		return regist;
	}

	public void setRegist(Long regist) {
		this.regist = regist;
	}

	public Long getBeforeArrange() {
		return beforeArrange;
	}

	public void setBeforeArrange(Long beforeArrange) {
		this.beforeArrange = beforeArrange;
	}

	public Long getCompleteArrange() {
		return completeArrange;
	}

	public void setCompleteArrange(Long completeArrange) {
		this.completeArrange = completeArrange;
	}

	public Long getDiscard() {
		return discard;
	}

	public void setDiscard(Long discard) {
		this.discard = discard;
	}
	
	
	
}

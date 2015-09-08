package kr.co.d2net.dto;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
@Entity
@Table(name="CATEGORY_TBL")
public class CategoryTbl extends BaseObject {
	
	@Id
	@TableGenerator(name = "CATE_ID_SEQ", table = "ID_GEN_TBL", 
    	pkColumnName = "ENTITY_NAME", pkColumnValue = "CATEGORY_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CATE_ID_SEQ")
	@BusinessKey
	@Column(name="CATEGORY_ID", length=10)
	private Integer categoryId;	//카테고리ID
	
	@Column(name="CATEGORY_NM", length=30, nullable=true)
	private String categoryNm;	//카테고리명

	@Column(name="PREPARENT", length=10)
	private Integer preParent;//직전노드
	
	@Column(name="DEPTH", length=10)
	private Integer depth;//깊이
	
	@Column(name="NODES", length=255, nullable=true)
	private String nodes;	//상위카테고리부터 현제카테고리까지
	
	@Column(name="ORDER_NUM", length=10)
	private Integer orderNum;//카테고리 순번
	
	@Column(name="GROUP_ID", length=10)
	private Integer groupId;//그룹id
	
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}

	public Integer getPreParent() {
		return preParent;
	}

	public void setPreParent(Integer preParent) {
		this.preParent = preParent;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}


	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(targetEntity=EpisodeTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="categoryTbl")
	private Set<EpisodeTbl> episodeTbl;

	public Set<EpisodeTbl> getEpisodeTbl() {
		return episodeTbl;
	}

	public void setEpisodeTbl(Set<EpisodeTbl> episodeTbl) {
		this.episodeTbl = episodeTbl;
	}
	
}

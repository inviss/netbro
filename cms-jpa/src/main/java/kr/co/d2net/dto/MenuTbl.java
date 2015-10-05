package kr.co.d2net.dto;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name="MENU_TBL")
public class MenuTbl extends BaseObject {
	
	@Id
	@TableGenerator(name = "MENU_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "MENU_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MENU_ID_SEQ")
	@BusinessKey
	@Column(name="MENU_ID", length=4)
	private Integer menuId;	//메뉴ID

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@BusinessKey
	@Column(name="MENU_NM", length=30, nullable=true)
	private String menuNm;	//메뉴명

	@Column(name="URL", length=100, nullable=true)
	private String url;
	
	//UI Header에 image명을 가져올때 사용.
	@Column(name="MENU_EN_NM", length=20, nullable=true)
	private String menuEnNm;
	
	@Column(name="NODES", length=255, nullable=true)
	private String nodes;	//상위카테고리부터 현제카테고리까지
	
	@Column(name="ORDER_NUM", length=10)
	private Integer orderNum;//카테고리 순번
	
	@Column(name="GROUP_ID", length=10)
	private Integer groupId;//그룹id

	@Column(name="DEPTH", length=10)
	private Integer depth;//깊이

 
	@Transient
	private String controlGubun;


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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getControlGubun() {
		return controlGubun;
	}

	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getRegrId() {
		return regrId;
	}

	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}


	public String getModrId() {
		return modrId;
	}

	public void setModrId(String modrId) {
		this.modrId = modrId;
	}

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	public String getMenuEnNm() {
		return menuEnNm;
	}

	public void setMenuEnNm(String menuEnNm) {
		this.menuEnNm = menuEnNm;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(targetEntity=RoleAuthTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="menuTbl")
	private Set<RoleAuthTbl> roleAuth;

	public Set<RoleAuthTbl> getRoleAuth() {
		return roleAuth;
	}

	public void setRoleAuth(Set<RoleAuthTbl> roleAuth) {
		this.roleAuth = roleAuth;
	}

}

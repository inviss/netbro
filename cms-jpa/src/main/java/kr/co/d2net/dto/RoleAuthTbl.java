package kr.co.d2net.dto;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@IdClass(value=RoleAuthPK.class)
@Transactional
@Table(name="ROLE_AUTH_TBL")
public class RoleAuthTbl{

	public RoleAuthTbl() {}

	@Id
	@Column(name="MENU_ID", length=4)
	private Integer menuId;
	
	@Id
	@Column(name="AUTH_ID", length=4)
	private Integer authId;
 
 

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getAuthId() {
		return authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	@Column(name="CONTROL_GUBUN", length=40, nullable=true)
	private String controlGubun;	//접근구분

	public String getControlGubun() {
		return controlGubun;
	}

	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@ManyToOne(optional=false)
	@JoinColumn(name="MENU_ID", updatable=false, insertable=false)
	private MenuTbl menuTbl;

	public MenuTbl getMenuTbl() {
		return menuTbl;
	}

	public void setMenuTbl(MenuTbl menuTbl) {
		this.menuTbl = menuTbl;
	}


	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@ManyToOne(optional=false)
	@JoinColumn(name="AUTH_ID", updatable=false, insertable=false)
	private AuthTbl authTbl;

	public AuthTbl getAuthTbl() {
		return authTbl;
	}

	public void setAuthTbl(AuthTbl authTbl) {
		this.authTbl = authTbl;
	}

}

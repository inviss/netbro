package kr.co.d2net.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Transactional
@Table(name="ROLE_AUTH_TBL")
public class RoleAuthTbl{
	
	public RoleAuthTbl() {}
	
	@Embeddable
	public static class RoleAuthId extends BaseObject {

		private static final long serialVersionUID = 8621165438593537768L;
		
		@BusinessKey
		@Column(name="menu_id", length = 4)
		private Integer menuId;
		
		@BusinessKey
		@Column(name="auth_id", length = 2)
		private Integer authId;

		public Integer getMenuId() {
			return menuId;
		}

		public void setMenuId(Integer menuId) {
			this.menuId = menuId;
		}

		public Integer getAuthid() {
			return authId;
		}

		public void setAuthid(Integer authId) {
			this.authId = authId;
		}
		
	}
	
	
	@EmbeddedId
	private RoleAuthId id = new RoleAuthId();
	
	public RoleAuthId getId() {
		return id;
	}

	public void setId(RoleAuthId id) {
		this.id = id;
	}
	
	
	@Column(name="CONTROL_GUBUN", length=40, nullable=true)
	private String controlGubun;	//접근구분
	
	public String getControlGubun() {
		return controlGubun;
	}

	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}
	
	@BusinessKey
	@ManyToOne(optional=false)
	@JoinColumn(name="menu_id", updatable=false, insertable=false)
	private MenuTbl menuTbl;
	
	public MenuTbl getMenuTbl() {
		return menuTbl;
	}

	public void setMenuTbl(MenuTbl menuTbl) {
		this.menuTbl = menuTbl;
	}

	
	@BusinessKey
	@ManyToOne(optional=false)
	@JoinColumn(name="auth_id", updatable=false, insertable=false)
	private AuthTbl authTbl;

	public AuthTbl getAuthTbl() {
		return authTbl;
	}

	public void setAuthTbl(AuthTbl authTbl) {
		this.authTbl = authTbl;
	}
	
}

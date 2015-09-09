package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="USER_AUTH_TBL")
public class UserAuthTbl{

	@Embeddable
	public static class UserAuthId extends BaseObject {

		private static final long serialVersionUID = 8366346005090152760L;

		public UserAuthId() {}

		@BusinessKey
		@Column(name="AUTH_ID", length = 2)
		private Integer authId;
		
		@BusinessKey
		@Column(name="USER_ID", length=30)
		private String userId;

		public Integer getAuthId() {
			return authId;
		}

		public void setAuthId(Integer authId) {
			this.authId = authId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}

	@EmbeddedId
	private UserAuthId id = new UserAuthId();

	public UserAuthId getId() {
		return id;
	}

	public void setId(UserAuthId id) {
		this.id = id;
	}


	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;//수정일시


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

	@ManyToOne(optional=false)
	@JoinColumn(name="AUTH_ID", updatable=false, insertable=false)
	private AuthTbl authTbl;		//권한ID	
	
	public AuthTbl getAuthTbl() {
		return authTbl;
	}

	public void setAuthTbl(AuthTbl authTbl) {
		this.authTbl = authTbl;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name="USER_ID", updatable=false, insertable=false)
	private UserTbl userTbl;

	public UserTbl getUserTbl() {
		return userTbl;
	}

	public void setUserTbl(UserTbl userTbl) {
		this.userTbl = userTbl;
	}

}
